package com.capgemini.sesp.ast.android.module.tsp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.VectorDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.capgemini.sesp.ast.android.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.ClusterRenderer;
import com.google.maps.android.collections.MarkerManager;
import com.google.maps.android.geometry.Point;
import com.google.maps.android.projection.SphericalMercatorProjection;
import com.google.maps.android.ui.IconGenerator;
import com.google.maps.android.ui.SquareTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @param <T>
 */
public class SESPClusterRenderer<T extends ClusterItem> implements ClusterRenderer<T> {
    private static final boolean SHOULD_ANIMATE;
    private final GoogleMap mMap;
    private final IconGenerator mIconGenerator;
    private final ClusterManager<T> mClusterManager;
    private final float mDensity;
    private final Context mContext;
    private boolean mAnimate;
    private static final int[] BUCKETS;
    private ShapeDrawable mColoredCircleBackground;
    private ShapeDrawable mOutline;
    private Set<SESPClusterRenderer.MarkerWithPosition> mMarkers = Collections.newSetFromMap(new ConcurrentHashMap());
    private SparseArray<BitmapDescriptor> mIcons = new SparseArray();
    private SESPClusterRenderer.MarkerCache<T> mMarkerCache = new SESPClusterRenderer.MarkerCache();
    private int mMinClusterSize = 4;
    private Set<? extends Cluster<T>> mClusters;
    private Map<Marker, Cluster<T>> mMarkerToCluster = new HashMap();
    private Map<Cluster<T>, Marker> mClusterToMarker = new HashMap();
    private float mZoom;
    private final SESPClusterRenderer<T>.ViewModifier mViewModifier = new SESPClusterRenderer.ViewModifier();
    private ClusterManager.OnClusterClickListener<T> mClickListener;
    private ClusterManager.OnClusterInfoWindowClickListener<T> mInfoWindowClickListener;
    private ClusterManager.OnClusterItemClickListener<T> mItemClickListener;
    private ClusterManager.OnClusterItemInfoWindowClickListener<T> mItemInfoWindowClickListener;
    private static final TimeInterpolator ANIMATION_INTERP;
    private BitmapDescriptor descriptorSelected,descriptorNotSelected;
    private  int sampler = 0;
    public boolean forced = false;


    public SESPClusterRenderer(Context context, GoogleMap map, ClusterManager<T> clusterManager) {
        this.mMap = map;
        this.mAnimate = true;
        this.mDensity = context.getResources().getDisplayMetrics().density;
        this.mIconGenerator = new IconGenerator(context);
        this.mIconGenerator.setContentView(this.makeSquareTextView(context));
        this.mIconGenerator.setTextAppearance(com.google.maps.android.R.style.amu_ClusterIcon_TextAppearance);
        this.mIconGenerator.setBackground(this.makeClusterBackground());
        this.mClusterManager = clusterManager;
        mContext = context;

        setMinClusterSize(1);
    }

    public void onAdd() {
        this.mClusterManager.getMarkerCollection().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                return SESPClusterRenderer.this.mItemClickListener != null && SESPClusterRenderer.this.mItemClickListener.onClusterItemClick((T) SESPClusterRenderer.this.mMarkerCache.get(marker));
            }
        });
        this.mClusterManager.getMarkerCollection().setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            public void onInfoWindowClick(Marker marker) {
                if (SESPClusterRenderer.this.mItemInfoWindowClickListener != null) {
                    SESPClusterRenderer.this.mItemInfoWindowClickListener.onClusterItemInfoWindowClick((T) SESPClusterRenderer.this.mMarkerCache.get(marker));
                }

            }
        });
        this.mClusterManager.getClusterMarkerCollection().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                return SESPClusterRenderer.this.mClickListener != null && SESPClusterRenderer.this.mClickListener.onClusterClick((Cluster) SESPClusterRenderer.this.mMarkerToCluster.get(marker));
            }
        });
        this.mClusterManager.getClusterMarkerCollection().setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            public void onInfoWindowClick(Marker marker) {
                if (SESPClusterRenderer.this.mInfoWindowClickListener != null) {
                    SESPClusterRenderer.this.mInfoWindowClickListener.onClusterInfoWindowClick((Cluster) SESPClusterRenderer.this.mMarkerToCluster.get(marker));
                }

            }
        });
    }

    public void onRemove() {
        this.mClusterManager.getMarkerCollection().setOnMarkerClickListener((GoogleMap.OnMarkerClickListener)null);
        this.mClusterManager.getMarkerCollection().setOnInfoWindowClickListener((GoogleMap.OnInfoWindowClickListener)null);
        this.mClusterManager.getClusterMarkerCollection().setOnMarkerClickListener((GoogleMap.OnMarkerClickListener)null);
        this.mClusterManager.getClusterMarkerCollection().setOnInfoWindowClickListener((GoogleMap.OnInfoWindowClickListener)null);
    }

    private LayerDrawable makeClusterBackground() {
        this.mColoredCircleBackground = new ShapeDrawable(new OvalShape());
        mOutline = new ShapeDrawable(new OvalShape());
        mOutline.getPaint().setColor(-2130706433);
        LayerDrawable background = new LayerDrawable(new Drawable[]{mOutline, this.mColoredCircleBackground});
        int strokeWidth = (int)(this.mDensity * 3.0F);
        background.setLayerInset(1, strokeWidth, strokeWidth, strokeWidth, strokeWidth);
        return background;
    }

    private SquareTextView makeSquareTextView(Context context) {
        SquareTextView squareTextView = new SquareTextView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-2, -2);
        squareTextView.setLayoutParams(layoutParams);
        squareTextView.setId(com.google.maps.android.R.id.amu_text);
        int twelveDpi = (int)(12.0F * this.mDensity);
        squareTextView.setPadding(twelveDpi, twelveDpi, twelveDpi, twelveDpi);
        return squareTextView;
    }

    protected int getColor(int clusterSize) {
        float hueRange = 220.0F;
        float sizeRange = 300.0F;
        float size = Math.min((float)clusterSize, 300.0F);
        float hue = (300.0F - size) * (300.0F - size) / 90000.0F * 220.0F;
        return Color.HSVToColor(new float[]{hue, 1.0F, 0.6F});
    }

    protected String getClusterText(int bucket) {
        return String.valueOf(bucket);
    }

    protected int getBucket(Cluster<T> cluster) {

        return cluster.getSize();
    }

    public int getMinClusterSize() {
        return this.mMinClusterSize;
    }

    public void setMinClusterSize(int minClusterSize) {
        this.mMinClusterSize = minClusterSize;
    }

    protected boolean shouldRenderAsCluster(Cluster<T> cluster) {
        return (cluster.getSize() >= this.mMinClusterSize && mZoom < 13);
    }

    public void onClustersChanged(Set<? extends Cluster<T>> clusters) {
        this.mViewModifier.queue(clusters);
    }

    public void setOnClusterClickListener(ClusterManager.OnClusterClickListener<T> listener) {
        this.mClickListener = listener;
    }

    public void setOnClusterInfoWindowClickListener(ClusterManager.OnClusterInfoWindowClickListener<T> listener) {
        this.mInfoWindowClickListener = listener;
    }

    public void setOnClusterItemClickListener(ClusterManager.OnClusterItemClickListener<T> listener) {
        this.mItemClickListener = listener;
    }

    public void setOnClusterItemInfoWindowClickListener(ClusterManager.OnClusterItemInfoWindowClickListener<T> listener) {
        this.mItemInfoWindowClickListener = listener;
    }

    public void setAnimation(boolean animate) {
        this.mAnimate = animate;
    }

    private static double distanceSquared(Point a, Point b) {
        return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
    }

    private static Point findClosestCluster(List<Point> markers, Point point) {
        if (markers != null && !markers.isEmpty()) {
            double minDistSquared = 10000.0D;
            Point closest = null;
            Iterator pointIterator = markers.iterator();

            while(pointIterator.hasNext()) {
                Point candidate = (Point)pointIterator.next();
                double dist = distanceSquared(candidate, point);
                if (dist < minDistSquared) {
                    closest = candidate;
                    minDistSquared = dist;
                }
            }

            return closest;
        } else {
            return null;
        }
    }


    protected void onBeforeClusterItemRendered(T item, MarkerOptions markerOptions) {

        //sla, tr, sla & tr

        sampler = 360;

        if (((WorkOrder) item).sla.contains("null")){//no sla
            sampler = 90;
        }
        if ((((WorkOrder) item).workorderLiteTO.getTimeReservationEnd() == null)){//no tr
            if (sampler == 90)
                sampler = 180;
            else
                sampler = 270;
        }


        VectorDrawable vectorDrawable = (VectorDrawable) mContext.getDrawable(R.drawable.ic_navigation);
        vectorDrawable.setTint(Color.HSVToColor(new float[]{sampler, 1.0F, 0.6F}));
        vectorDrawable.setAlpha(0xFF);

       markerOptions.icon(TspUtil.generateBitmap(vectorDrawable));
    }

    protected void onBeforeClusterRendered(Cluster<T> cluster, MarkerOptions markerOptions) {

        sampler = 360;

        if (!((SESPCluster<T>) cluster).containsSLA){
            sampler = 90;
        }
        if (!((SESPCluster) cluster).containsTR){
            if (sampler == 90)
                sampler = 180;
            else
                sampler = 270;

        }

        int colorInt = Color.HSVToColor(new float[]{sampler, 1.0F, 0.6F});

        int bucket = this.getBucket(cluster);
        BitmapDescriptor descriptor = null;
        if (((SESPCluster)cluster).isSelected())
        {
            this.mColoredCircleBackground.getPaint().setColor(colorInt);
            this.mOutline.getPaint().setColor(Color.parseColor("#FFA366"));
        }
        else {
            mOutline.getPaint().setColor(-2130706433);
            this.mColoredCircleBackground.getPaint().setColor(colorInt);
        }
        descriptor = BitmapDescriptorFactory.fromBitmap(this.mIconGenerator.makeIcon(this.getClusterText(bucket)));

        markerOptions.icon(descriptor);
    }

    protected void onClusterRendered(Cluster<T> cluster, Marker marker) {
    }

    protected void onClusterItemRendered(T clusterItem, Marker marker) {
    }

    public Marker getMarker(T clusterItem) {
        return this.mMarkerCache.get(clusterItem);
    }

    public T getClusterItem(Marker marker) {
        return (T)this.mMarkerCache.get(marker);
    }

    public Marker getMarker(Cluster<T> cluster) {
        return (Marker)this.mClusterToMarker.get(cluster);
    }

    public Cluster<T> getCluster(Marker marker) {
        return (Cluster)this.mMarkerToCluster.get(marker);
    }

    static {
        SHOULD_ANIMATE = Build.VERSION.SDK_INT >= 11;
        BUCKETS = new int[]{10, 20, 50, 100, 200, 500, 1000};
        ANIMATION_INTERP = new DecelerateInterpolator();
    }

    @TargetApi(12)
    private class AnimationTask extends AnimatorListenerAdapter implements ValueAnimator.AnimatorUpdateListener {
        private final SESPClusterRenderer.MarkerWithPosition markerWithPosition;
        private final Marker marker;
        private final LatLng from;
        private final LatLng to;
        private boolean mRemoveOnComplete;
        private MarkerManager mMarkerManager;

        private AnimationTask(SESPClusterRenderer.MarkerWithPosition markerWithPosition, LatLng from, LatLng to) {
            this.markerWithPosition = markerWithPosition;
            this.marker = markerWithPosition.marker;
            this.from = from;
            this.to = to;
        }

        public void perform() {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0F, 1.0F);
            valueAnimator.setInterpolator(SESPClusterRenderer.ANIMATION_INTERP);
            valueAnimator.addUpdateListener(this);
            valueAnimator.addListener(this);
            valueAnimator.start();
        }

        public void onAnimationEnd(Animator animation) {
            if (this.mRemoveOnComplete) {
                Cluster<T> cluster = (Cluster) SESPClusterRenderer.this.mMarkerToCluster.get(this.marker);
                SESPClusterRenderer.this.mClusterToMarker.remove(cluster);
                SESPClusterRenderer.this.mMarkerCache.remove(this.marker);
                SESPClusterRenderer.this.mMarkerToCluster.remove(this.marker);
                this.mMarkerManager.remove(this.marker);
            }

            this.markerWithPosition.position = this.to;
        }

        public void removeOnAnimationComplete(MarkerManager markerManager) {
            this.mMarkerManager = markerManager;
            this.mRemoveOnComplete = true;
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float fraction = valueAnimator.getAnimatedFraction();
            double lat = (this.to.latitude - this.from.latitude) * (double)fraction + this.from.latitude;
            double lngDelta = this.to.longitude - this.from.longitude;
            if (Math.abs(lngDelta) > 180.0D) {
                lngDelta -= Math.signum(lngDelta) * 360.0D;
            }

            double lng = lngDelta * (double)fraction + this.from.longitude;
            LatLng position = new LatLng(lat, lng);
            this.marker.setPosition(position);
        }
    }

    private static class MarkerWithPosition {
        private final Marker marker;
        private LatLng position;

        private MarkerWithPosition(Marker marker) {
            this.marker = marker;
            this.position = marker.getPosition();
        }

        public boolean equals(Object other) {
            return other instanceof SESPClusterRenderer.MarkerWithPosition ? this.marker.equals(((SESPClusterRenderer.MarkerWithPosition)other).marker) : false;
        }

        public int hashCode() {
            return this.marker.hashCode();
        }
    }

    private class CreateMarkerTask {
        private final Cluster<T> cluster;
        private final Set<SESPClusterRenderer.MarkerWithPosition> newMarkers;
        private final LatLng animateFrom;

        public CreateMarkerTask(Cluster<T> c, Set<SESPClusterRenderer.MarkerWithPosition> markersAdded, LatLng animateFrom) {
            this.cluster = c;
            this.newMarkers = markersAdded;
            this.animateFrom = animateFrom;
        }

        private void perform(SESPClusterRenderer<T>.MarkerModifier markerModifier) {
            if (SESPClusterRenderer.this.shouldRenderAsCluster(this.cluster)) {
                Marker marker = (Marker) SESPClusterRenderer.this.mClusterToMarker.get(this.cluster);
                SESPClusterRenderer.MarkerWithPosition markerWithPosition;
                if (marker == null || forced) {
                    MarkerOptions markerOptionsx = (new MarkerOptions()).position(this.animateFrom == null ? this.cluster.getPosition() : this.animateFrom);
                    SESPClusterRenderer.this.onBeforeClusterRendered(this.cluster, markerOptionsx);
                    marker = SESPClusterRenderer.this.mClusterManager.getClusterMarkerCollection().addMarker(markerOptionsx);
                    SESPClusterRenderer.this.mMarkerToCluster.put(marker, this.cluster);
                    SESPClusterRenderer.this.mClusterToMarker.put(this.cluster, marker);
                    markerWithPosition = new SESPClusterRenderer.MarkerWithPosition(marker);
                    if (this.animateFrom != null) {
                        markerModifier.animate(markerWithPosition, this.animateFrom, this.cluster.getPosition());
                    }
                } else {
                    markerWithPosition = new SESPClusterRenderer.MarkerWithPosition(marker);
                }

                SESPClusterRenderer.this.onClusterRendered(this.cluster, marker);
                this.newMarkers.add(markerWithPosition);
            } else {
                Iterator tIterator = this.cluster.getItems().iterator();

                while(tIterator.hasNext()) {
                    T item = (T)tIterator.next();
                    Marker markerx = SESPClusterRenderer.this.mMarkerCache.get(item);
                    SESPClusterRenderer.MarkerWithPosition markerWithPositionx;
                    if (markerx == null) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        if (this.animateFrom != null) {
                            markerOptions.position(this.animateFrom);
                        } else {
                            markerOptions.position(item.getPosition());
                        }

                        if (item.getTitle() != null && item.getSnippet() != null) {
                            markerOptions.title(item.getTitle());
                            markerOptions.snippet(item.getSnippet());
                        } else if (item.getSnippet() != null) {
                            markerOptions.title(item.getSnippet());
                        } else if (item.getTitle() != null) {
                            markerOptions.title(item.getTitle());
                        }

                        SESPClusterRenderer.this.onBeforeClusterItemRendered(item, markerOptions);
                        markerx = SESPClusterRenderer.this.mClusterManager.getMarkerCollection().addMarker(markerOptions);
                        markerWithPositionx = new SESPClusterRenderer.MarkerWithPosition(markerx);
                        SESPClusterRenderer.this.mMarkerCache.put(item, markerx);
                        if (this.animateFrom != null) {
                            markerModifier.animate(markerWithPositionx, this.animateFrom, item.getPosition());
                        }
                    } else {
                        markerWithPositionx = new SESPClusterRenderer.MarkerWithPosition(markerx);
                    }

                    SESPClusterRenderer.this.onClusterItemRendered(item, markerx);
                    this.newMarkers.add(markerWithPositionx);
                }

            }
        }
    }

    private static class MarkerCache<T> {
        private Map<T, Marker> mCache;
        private Map<Marker, T> mCacheReverse;

        private MarkerCache() {
            this.mCache = new HashMap();
            this.mCacheReverse = new HashMap();
        }

        public Marker get(T item) {
            return (Marker)this.mCache.get(item);
        }

        public T get(Marker m) {
            return this.mCacheReverse.get(m);
        }

        public void put(T item, Marker m) {
            this.mCache.put(item, m);
            this.mCacheReverse.put(m, item);
        }

        public void remove(Marker m) {
            T item = this.mCacheReverse.get(m);
            this.mCacheReverse.remove(m);
            this.mCache.remove(item);
        }
    }

    @SuppressLint({"HandlerLeak"})
    private class MarkerModifier extends Handler implements MessageQueue.IdleHandler {
        private static final int BLANK = 0;
        private final Lock lock;
        private final Condition busyCondition;
        private Queue<SESPClusterRenderer<T>.CreateMarkerTask> mCreateMarkerTasks;
        private Queue<SESPClusterRenderer<T>.CreateMarkerTask> mOnScreenCreateMarkerTasks;
        private Queue<Marker> mRemoveMarkerTasks;
        private Queue<Marker> mOnScreenRemoveMarkerTasks;
        private Queue<SESPClusterRenderer<T>.AnimationTask> mAnimationTasks;
        private boolean mListenerAdded;

        private MarkerModifier() {
            super(Looper.getMainLooper());
            this.lock = new ReentrantLock();
            this.busyCondition = this.lock.newCondition();
            this.mCreateMarkerTasks = new LinkedList();
            this.mOnScreenCreateMarkerTasks = new LinkedList();
            this.mRemoveMarkerTasks = new LinkedList();
            this.mOnScreenRemoveMarkerTasks = new LinkedList();
            this.mAnimationTasks = new LinkedList();
        }

        public void add(boolean priority, SESPClusterRenderer<T>.CreateMarkerTask c) {
            this.lock.lock();
            this.sendEmptyMessage(ViewModifier.RUN_TASK);
            if (priority) {
                this.mOnScreenCreateMarkerTasks.add(c);
            } else {
                this.mCreateMarkerTasks.add(c);
            }

            this.lock.unlock();
        }

        public void remove(boolean priority, Marker m) {
            this.lock.lock();
            this.sendEmptyMessage(ViewModifier.RUN_TASK);
            if (priority) {
                this.mOnScreenRemoveMarkerTasks.add(m);
            } else {
                this.mRemoveMarkerTasks.add(m);
            }

            this.lock.unlock();
        }

        public void animate(SESPClusterRenderer.MarkerWithPosition marker, LatLng from, LatLng to) {
            this.lock.lock();
            this.mAnimationTasks.add(SESPClusterRenderer.this.new AnimationTask(marker, from, to));
            this.lock.unlock();
        }

        @TargetApi(11)
        public void animateThenRemove(SESPClusterRenderer.MarkerWithPosition marker, LatLng from, LatLng to) {
            this.lock.lock();
            SESPClusterRenderer<T>.AnimationTask animationTask = SESPClusterRenderer.this.new AnimationTask(marker, from, to);
            animationTask.removeOnAnimationComplete(SESPClusterRenderer.this.mClusterManager.getMarkerManager());
            this.mAnimationTasks.add(animationTask);
            this.lock.unlock();
        }

        public void handleMessage(Message msg) {
            if (!this.mListenerAdded) {
                Looper.myQueue().addIdleHandler(this);
                this.mListenerAdded = true;
            }

            this.removeMessages(ViewModifier.RUN_TASK);
            this.lock.lock();

            try {
                int i = 0;

                while(true) {
                    if (i >= 10) {
                        if (!this.isBusy()) {
                            this.mListenerAdded = false;
                            Looper.myQueue().removeIdleHandler(this);
                            this.busyCondition.signalAll();
                        } else {
                            this.sendEmptyMessageDelayed(ViewModifier.RUN_TASK, 10L);
                        }
                        break;
                    }

                    this.performNextTask();
                    ++i;
                }
            } finally {
                this.lock.unlock();
            }

        }

        @TargetApi(11)
        private void performNextTask() {
            if (!this.mOnScreenRemoveMarkerTasks.isEmpty()) {
                this.removeMarker((Marker)this.mOnScreenRemoveMarkerTasks.poll());
            } else if (!this.mAnimationTasks.isEmpty()) {
                ((SESPClusterRenderer.AnimationTask)this.mAnimationTasks.poll()).perform();
            } else if (!this.mOnScreenCreateMarkerTasks.isEmpty()) {
                ((SESPClusterRenderer.CreateMarkerTask)this.mOnScreenCreateMarkerTasks.poll()).perform(this);
            } else if (!this.mCreateMarkerTasks.isEmpty()) {
                ((SESPClusterRenderer.CreateMarkerTask)this.mCreateMarkerTasks.poll()).perform(this);
            } else if (!this.mRemoveMarkerTasks.isEmpty()) {
                this.removeMarker((Marker)this.mRemoveMarkerTasks.poll());
            }

        }

        private void removeMarker(Marker m) {
            Cluster<T> cluster = (Cluster) SESPClusterRenderer.this.mMarkerToCluster.get(m);
            SESPClusterRenderer.this.mClusterToMarker.remove(cluster);
            SESPClusterRenderer.this.mMarkerCache.remove(m);
            SESPClusterRenderer.this.mMarkerToCluster.remove(m);
            SESPClusterRenderer.this.mClusterManager.getMarkerManager().remove(m);
        }

        public boolean isBusy() {
            boolean isbusy;
            try {
                this.lock.lock();
                isbusy = !this.mCreateMarkerTasks.isEmpty() || !this.mOnScreenCreateMarkerTasks.isEmpty() || !this.mOnScreenRemoveMarkerTasks.isEmpty() || !this.mRemoveMarkerTasks.isEmpty() || !this.mAnimationTasks.isEmpty();
            } finally {
                this.lock.unlock();
            }

            return isbusy;
        }

        public void waitUntilFree() {
            while(this.isBusy()) {
                this.sendEmptyMessage(0);
                this.lock.lock();

                try {
                    if (this.isBusy()) {
                        this.busyCondition.await();
                    }
                } catch (InterruptedException var5) {
                    throw new RuntimeException(var5);
                } finally {
                    this.lock.unlock();
                }
            }

        }

        public boolean queueIdle() {
            this.sendEmptyMessage(0);
            return true;
        }
    }

    private class RenderTask implements Runnable {
        final Set<? extends Cluster<T>> clusters;
        private Runnable mCallback;
        private Projection mProjection;
        private SphericalMercatorProjection mSphericalMercatorProjection;
        private float mMapZoom;

        private RenderTask(Set<? extends Cluster<T>> clusters) {
            this.clusters = clusters;
        }

        public void setCallback(Runnable callback) {
            this.mCallback = callback;
        }

        public void setProjection(Projection projection) {
            this.mProjection = projection;
        }

        public void setMapZoom(float zoom) {
            this.mMapZoom = zoom;
            this.mSphericalMercatorProjection = new SphericalMercatorProjection(256.0D * Math.pow(2.0D, (double)Math.min(zoom, SESPClusterRenderer.this.mZoom)));
        }

        @SuppressLint({"NewApi"})
        public void run() {
            /*if (this.clusters.equals(SESPClusterRenderer.this.mClusters)) {
                this.mCallback.run();
            } else*/ {
                SESPClusterRenderer<T>.MarkerModifier markerModifier = SESPClusterRenderer.this.new MarkerModifier();
                float zoom = this.mMapZoom;
                boolean zoomingIn = zoom > SESPClusterRenderer.this.mZoom;
                float zoomDelta = zoom - SESPClusterRenderer.this.mZoom;
                Set<SESPClusterRenderer.MarkerWithPosition> markersToRemove = SESPClusterRenderer.this.mMarkers;
                LatLngBounds visibleBounds = this.mProjection.getVisibleRegion().latLngBounds;
                List<Point> existingClustersOnScreen = null;
                if (SESPClusterRenderer.this.mClusters != null && SESPClusterRenderer.SHOULD_ANIMATE) {
                    existingClustersOnScreen = new ArrayList();
                    Iterator iterator = SESPClusterRenderer.this.mClusters.iterator();

                    while(iterator.hasNext()) {
                        Cluster<T> c = (Cluster)iterator.next();
                        if (SESPClusterRenderer.this.shouldRenderAsCluster(c) && visibleBounds.contains(c.getPosition())) {
                            Point pointx = this.mSphericalMercatorProjection.toPoint(c.getPosition());
                            existingClustersOnScreen.add(pointx);
                        }
                    }
                }

                Set<SESPClusterRenderer.MarkerWithPosition> newMarkers = Collections.newSetFromMap(new ConcurrentHashMap());
                Iterator iterator = this.clusters.iterator();

                while(true) {
                    while(true) {
                        Point p;
                        while(iterator.hasNext()) {
                            Cluster<T> cx = (Cluster)iterator.next();
                            boolean onScreenx = visibleBounds.contains(cx.getPosition());
                            if (zoomingIn && onScreenx && SESPClusterRenderer.SHOULD_ANIMATE) {
                                p = this.mSphericalMercatorProjection.toPoint(cx.getPosition());
                                Point closestx = SESPClusterRenderer.findClosestCluster(existingClustersOnScreen, p);
                                if (closestx != null && SESPClusterRenderer.this.mAnimate) {
                                    LatLng animateTo = this.mSphericalMercatorProjection.toLatLng(closestx);
                                    markerModifier.add(true, SESPClusterRenderer.this.new CreateMarkerTask(cx, newMarkers, animateTo));
                                }
                                else if (forced){
                                    markerModifier.add(true, SESPClusterRenderer.this.new CreateMarkerTask(cx, newMarkers, (LatLng)null));
                                }
                                else {
                                    markerModifier.add(true, SESPClusterRenderer.this.new CreateMarkerTask(cx, newMarkers, (LatLng)null));
                                }
                            } else {
                                markerModifier.add(onScreenx, SESPClusterRenderer.this.new CreateMarkerTask(cx, newMarkers, (LatLng)null));
                            }
                        }

                        markerModifier.waitUntilFree();
                        markersToRemove.removeAll(newMarkers);
                        List<Point> newClustersOnScreen = null;
                        Iterator clusterIterator;
                        if (SESPClusterRenderer.SHOULD_ANIMATE) {
                            newClustersOnScreen = new ArrayList();
                            clusterIterator = this.clusters.iterator();

                            while(clusterIterator.hasNext()) {
                                Cluster<T> cxx = (Cluster)clusterIterator.next();
                                if (SESPClusterRenderer.this.shouldRenderAsCluster(cxx) && visibleBounds.contains(cxx.getPosition())) {
                                    p = this.mSphericalMercatorProjection.toPoint(cxx.getPosition());
                                    newClustersOnScreen.add(p);
                                }
                            }
                        }

                        clusterIterator = markersToRemove.iterator();

                        while(true) {
                            while(true) {
                                while(clusterIterator.hasNext()) {
                                    SESPClusterRenderer.MarkerWithPosition marker = (SESPClusterRenderer.MarkerWithPosition)clusterIterator.next();
                                    boolean onScreen = visibleBounds.contains(marker.position);
                                    if ((!zoomingIn && zoomDelta > -3.0F && onScreen && SESPClusterRenderer.SHOULD_ANIMATE)|| forced) {
                                        Point point = this.mSphericalMercatorProjection.toPoint(marker.position);
                                        Point closest = SESPClusterRenderer.findClosestCluster(newClustersOnScreen, point);
                                        if (closest != null && SESPClusterRenderer.this.mAnimate) {
                                            LatLng animateTox = this.mSphericalMercatorProjection.toLatLng(closest);
                                            markerModifier.animateThenRemove(marker, marker.position, animateTox);
                                        } else {
                                            markerModifier.remove(true, marker.marker);
                                        }
                                    } else {
                                        markerModifier.remove(onScreen, marker.marker);
                                    }
                                }

                                markerModifier.waitUntilFree();
                                SESPClusterRenderer.this.mMarkers = newMarkers;
                                SESPClusterRenderer.this.mClusters = this.clusters;
                                SESPClusterRenderer.this.mZoom = zoom;
                                this.mCallback.run();
                                forced = false;
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint({"HandlerLeak"})
    private class ViewModifier extends Handler {
        private static final int RUN_TASK = 0;
        private static final int TASK_FINISHED = 1;
        private boolean mViewModificationInProgress;
        private SESPClusterRenderer<T>.RenderTask mNextClusters;

        private ViewModifier() {
            this.mViewModificationInProgress = false;
            this.mNextClusters = null;
        }

        public void handleMessage(Message msg) {
            //mClusterManager.cluster();
            if (msg.what == TASK_FINISHED) {
                this.mViewModificationInProgress = false;
                if (this.mNextClusters != null) {
                    this.sendEmptyMessage(RUN_TASK);
                }

            } else {
                this.removeMessages(RUN_TASK);
                if (!this.mViewModificationInProgress) {
                    if (this.mNextClusters != null) {
                        Projection projection = SESPClusterRenderer.this.mMap.getProjection();
                        SESPClusterRenderer.RenderTask renderTask;
                        synchronized(this) {
                            renderTask = this.mNextClusters;
                            this.mNextClusters = null;
                            this.mViewModificationInProgress = true;
                        }

                        renderTask.setCallback(new Runnable() {
                            public void run() {
                                SESPClusterRenderer.ViewModifier.this.sendEmptyMessage(TASK_FINISHED);
                            }
                        });
                        renderTask.setProjection(projection);
                        renderTask.setMapZoom(SESPClusterRenderer.this.mMap.getCameraPosition().zoom);
                        (new Thread(renderTask)).start();
                    }
                }
            }
        }

        public void queue(Set<? extends Cluster<T>> clusters) {
            synchronized(this) {
                this.mNextClusters = SESPClusterRenderer.this.new RenderTask(clusters);
            }

            this.sendEmptyMessage(RUN_TASK);
        }
    }
}
