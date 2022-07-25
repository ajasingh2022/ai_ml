package com.capgemini.sesp.ast.android.module.tsp;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Environment;
import android.widget.Toast;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.WorkorderCache;
import com.capgemini.sesp.ast.android.module.others.WorkorderLiteTO;
import com.capgemini.sesp.ast.android.module.util.ApplicationAstSep;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TspUtil  {

    public static class CONSTANTS{
        public static final String G_API_URL ="https://maps.googleapis.com/maps/api/" ;
        public static final String G_METHOD_DIRECTION="directions/";
        public static final String G_METHOD_DM="distancematrix/";
        public static final String OUTPUT = "json?";
    }

    static GoogleMap mMap;
    static ArrayList<LatLng> mWayPoints = new ArrayList<>();
    static Polyline polyline=null;
    static MapsActivity mMapsActivity = null;
    static Context mContext = null;
    private static List<WorkorderLiteTO> wlist;
    static ProgressDialog progressDialog;
    static ArrayList<Marker> markers = new ArrayList<Marker>();

    public static List<WorkOrder> popWosToList(){
        wlist=WorkorderCache.getWorkordersLite(true);

        ArrayList<WorkOrder> returnValue = new ArrayList<>();

        for (WorkorderLiteTO workorderLiteTO:wlist){
            WorkOrder workOrder = new WorkOrder();
            workOrder.workorderLiteTO = workorderLiteTO;
            workOrder.sla= String.valueOf(workorderLiteTO.getSlaDeadline());
            WorkorderCustomWrapperTO wo = WorkorderCache.getWorkorderByCaseId(workorderLiteTO.getId(), ApplicationAstSep.workOrderClass);
            workOrder.latLng=new LatLng(Double.parseDouble(wo.getWgs84Latitude()),Double.parseDouble(wo.getWgs86Longitude()));
            returnValue.add(workOrder);
        }

        //popfromFile(returnValue);


        return returnValue;
    }

    public  void findRouteBetween(LatLng from, LatLng to){

        DownloadTask downloadTask = new DownloadTask();
        downloadTask.downloadCallBack = new DownloadCallBack() {
            @Override
            public void DownloadCallBackListener(String returnValue) {
                List<List<HashMap<String, String>>> road = parseAndSaveToDB(returnValue, from, to);

                ArrayList arrayList = new ArrayList<>();
                arrayList.add(from);
                arrayList.add(to);
                arrayList.add(road);
                arrayList.add(drawPolyline(road,false));
                mMapsActivity.polylines.add(arrayList);
            }

            @Override
            public void ComputeCallBack(ArrayList<WorkOrder> returnValues) {

            }
        };
        downloadTask.execute(getDirectionsUrl(from,to));

    }

    private List<List<HashMap<String, String>>> parseAndSaveToDB(String returnValue, LatLng from, LatLng to) {

        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try{
            jObject = new JSONObject(returnValue);
            DirectionJSONParser parser = new DirectionJSONParser();

            // Starts parsing data
            routes = parser.parse(jObject);
        }catch(Exception e){
            e.printStackTrace();
        }
        return routes;

    }

    public static BitmapDescriptor generateBitmap(VectorDrawable drawable){

        Bitmap bitmap = null;
        /*if (drawable instanceof BitmapDrawable)
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        else*/ {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            return BitmapDescriptorFactory.fromBitmap(bitmap);
        }

        //return BitmapDescriptorFactory.fromBitmap(bitmap);
    }



    protected Polyline drawPolyline(List<List<HashMap<String, String>>> result,boolean selected) {
        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;

        Polyline polyline=null;

        for (Marker marker : markers){
            marker.remove();
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(generateBitmap((VectorDrawable) mContext.getDrawable(R.drawable.ic_dashboard_navigation_24dp)));


        if (polyline != null)
            polyline.remove();
        // Traversing through all the routes

        try {


            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);

                }

                if (selected) {
                    markerOptions.rotation((float) SphericalUtil.computeHeading(points.get(0), points.get(points.size() - 1)));
                    markerOptions.position(points.get(points.size() / 2));
                    markers.add(mMap.addMarker(markerOptions));
                }
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                if (selected) {
                    lineOptions.width(6);
                    lineOptions.zIndex(1000);
                    lineOptions.color(Color.argb(0xFF,168, 50, 107));
                }
                else {
                    lineOptions.zIndex(10);
                    lineOptions.width(3);
                    lineOptions.color(Color.argb(0xFF, 28, 21, 107));
                }
            }



            // Drawing polyline in the Google Map for the i-th route
            polyline = TspUtil.mMap.addPolyline(lineOptions);
            polyline.setClickable(true);
        }catch (Exception e){
            Toast.makeText(mMapsActivity,"Service Not Available",Toast.LENGTH_SHORT).show();
        }
        return polyline;
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        //key
        String key = "key="+ DatabaseHandler.getMapApiKey();

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = CONSTANTS.G_API_URL+CONSTANTS.G_METHOD_DIRECTION +CONSTANTS.OUTPUT + parameters;

        return url;
    }



    private static void popfromFile(ArrayList<WorkOrder> returnValue) {

        SimpleDateFormat inputFormat  = new SimpleDateFormat("ddMMyyyyHH:mm");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM d HH:hh:ss z yyyy");


        File sdcard = Environment.getExternalStorageDirectory();

//Get the text file
        File file = new File(sdcard,"file.txt");

//Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int i=0;

            while ((line = br.readLine()) != null) {

                WorkOrder workOrder = new WorkOrder();
                WorkorderLiteTO workorderLiteTO = new WorkorderLiteTO();
                workorderLiteTO.setId((long) i++);
                workorderLiteTO.setIdCaseT(58L);
                String[] inputs=line.split(" ");
                try {
                    if (inputs[0] .equals("null")){
                        workOrder.sla = "null";
                    }
                    else
                        workOrder.sla = simpleDateFormat.format(inputFormat.parse(inputs[0]));

                    if (inputs[1] .equals("null")){
                        workorderLiteTO.setTimeReservationEnd(null);
                        workorderLiteTO.setTimeReservationStart(null);
                    }
                    else {
                        workorderLiteTO.setTimeReservationEnd(inputFormat.parse(inputs[2]));
                        workorderLiteTO.setTimeReservationStart(inputFormat.parse(inputs[1]));
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                {
                    String[] lll = inputs[3].split(",");
                    workOrder.latLng = new LatLng(Double.parseDouble(lll[0]),Double.parseDouble(lll[1]));
                }
                workOrder.setWorkorderLiteTO(workorderLiteTO);
                returnValue.add(workOrder);
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }

    }

    private static LatLng findFarAwayLocation(LatLng origin,ArrayList<LatLng> mWayPoints){

        double maxDist = 0;
        int index = -1;

        for (LatLng latLng:mWayPoints){
            double distance = findDistanceBwn(origin,latLng);
            if (maxDist < distance){
                index = mWayPoints.indexOf(latLng);
                maxDist = distance;
            }

        }
        if (index != -1)
        return
                mWayPoints.get(index);
        else
            return origin;
    }

    private static double findDistanceBwn(LatLng latLng, LatLng item) {

        double lat2, lat1, lon2, lon1, el1, el2;
        el1 = 0;
        el2 = 0;

        lat1 = latLng.latitude;
        lat2 = item.latitude;

        lon1 = latLng.longitude;
        lon2 = item.longitude;


        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);


    }


    private static String getDirectionsUrl(LatLng origin){
        LatLng dest=mWayPoints.get(0);

        String str_origin;
        // Origin of route
        if (mMapsActivity.mMyLocation.equals(null)) {
            Toast.makeText(mMapsActivity, "Current location not detected yet", Toast.LENGTH_SHORT).show();
            return null;
        }
        else
        str_origin = "origin="+mMapsActivity.mMyLocation.getLatitude()+","+mMapsActivity.mMyLocation.getLongitude();

        // Destination of route
        dest=findFarAwayLocation(origin,mWayPoints);

        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        String way_points = "waypoints=optimize:true|";

        for (LatLng latLng:mWayPoints){
            way_points=way_points+latLng.latitude+","+latLng.longitude+"|";
        }
        way_points = way_points.substring(0,way_points.length()-1);

        // Sensor enabled
        String sensor = "sensor=false";

        //key
        String key = "key="+DatabaseHandler.getMapApiKey();

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+way_points+"&"+key;

        // Output format

        // Building the url to the web service
        String url = CONSTANTS.G_API_URL+CONSTANTS.G_METHOD_DIRECTION+CONSTANTS.OUTPUT+"?"+parameters;

        return url;
    }

    public static void findRoute(MapsActivity mapsActivity, ArrayList<WorkOrder> workOrderSequence) {
        mContext = mapsActivity.getApplicationContext();
        mMapsActivity = (MapsActivity) mapsActivity;
        mWayPoints = mMapsActivity.mWayPoints;
        polyline = mMapsActivity.polyline;
        mMap = mapsActivity.mMap;

        try {
            String url = getDirectionsUrl(new LatLng(51.511442, -0.131894));
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.downloadCallBack = new DownloadCallBack() {
                @Override
                public void DownloadCallBackListener(String returnValue) {
                    ParserTask parserTask = new ParserTask();
                    parserTask.execute(returnValue);
                }

                @Override
                public void ComputeCallBack(ArrayList<WorkOrder> returnValues) {

                }
            };
            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }catch (Exception e)
        {
            Toast.makeText(mMapsActivity,"Please try again",Toast.LENGTH_SHORT).show();
        }
    }
    public static BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}
