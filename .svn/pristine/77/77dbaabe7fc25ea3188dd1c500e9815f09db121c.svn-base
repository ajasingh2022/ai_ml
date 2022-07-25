package com.capgemini.sesp.ast.android.module.tsp;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.communication.CommunicationHelper;
import com.capgemini.sesp.ast.android.module.tsp.DM.DistanceMatrix;
import com.capgemini.sesp.ast.android.module.tsp.DM.Element;
import com.capgemini.sesp.ast.android.module.tsp.DM.Row;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.maps.android.clustering.Cluster;
import com.skvader.rsp.cft.webservice_client.bean.to.custom.CaseTCustomTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.capgemini.sesp.ast.android.module.tsp.TspUtil.mContext;
import static com.capgemini.sesp.ast.android.module.tsp.TspUtil.mMapsActivity;

public class Compute {

    public static Long TOTAL_TIME = 0L;
    public static Long STD_TT = 8L;
    public static Long STD_WT = 22L;
    public static Date endTime = new Date();
    LinkedHashMap<WorkOrder, Date> workOrderDateHashMap = new LinkedHashMap<>();
    private List<WorkOrder> choosedWorkOrders, choosedWorkOrdersonSLA, choosedWorkOrdersTR, mandateWoForTheDay;
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM d HH:hh:ss z yyyy");
    HashMap<LatLng, ArrayList<WorkOrder>> clusterWorkOrders;
    int indexTr, indexSLA;
    private ArrayList<SESPCluster> clusterItemsSelected;
    static ProgressDialog progressDialog;
    private static SESPCluster currentLocationCluster;


    /**
     * @param sespCluster
     * @return time taken for completing all Work orders in cluster
     */

    public static float timeTakenForCluster(SESPCluster sespCluster) {

        float toatal_time = 0;

        for (WorkOrder workOrder : (ArrayList<WorkOrder>) sespCluster.getItems()) {
            //add regular time for travelling and work taken
            CaseTCustomTO caseTS = ObjectCache.getIdObject(CaseTCustomTO.class, workOrder.getWorkorderLiteTO().getIdCaseT());
            float wt = caseTS.getAverageWorkingMinutes();

            toatal_time = toatal_time + STD_TT + wt;

        }
        toatal_time = toatal_time - STD_TT;
        return toatal_time;
    }

    private class DistanceFromTo {
        Element element;
        LatLng latLng;
    }

    /**
     * @param workOrders
     * @return workOrders sorted on SLA Nearest to today will be first
     */

    public static List<WorkOrder> sortOnSLA(List<WorkOrder> workOrders) {

        /**
         * get work orders with sla
         * convert sla to timestamp (EPOC)
         * subtract sla from current EPOC .
         * Sort in ascending
         * any -ve Values alert
         */

        ArrayList returnValue = new ArrayList(workOrders);
        Collections.sort(returnValue, new Comparator<WorkOrder>() {
            @Override
            public int compare(WorkOrder o1, WorkOrder o2) {
                Date o1Date, o2Date;
                try {
                    o1Date = simpleDateFormat.parse(o1.sla);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 1;
                }
                try {
                    o2Date = simpleDateFormat.parse(o2.sla);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return -1;
                }
                if (o1Date.compareTo(o2Date) != 0)
                    return o1Date.compareTo(o2Date);
                else {
                    if (o1.getWorkorderLiteTO().getTimeReservationEnd() == null)
                        return 1;
                    if (o2.workorderLiteTO.getTimeReservationEnd() == null)
                        return -1;
                    else return o1.getWorkorderLiteTO().getTimeReservationEnd()
                            .compareTo(o2.workorderLiteTO.getTimeReservationEnd());
                }
            }
        });

        return returnValue;
    }


    /**
     * @param workOrders
     * @return workOrders sorted on timeReservation start Nearest to current time will be first
     */

    public static List<WorkOrder> sortOnTimeReservationEnd(List<WorkOrder> workOrders) {

        /**
         * get work orders with time reservation
         *
         */

        ArrayList arrayList = new ArrayList(workOrders);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM d HH:hh:ss z yyyy");
        Collections.sort(arrayList, new Comparator<WorkOrder>() {
            @Override
            public int compare(WorkOrder o1, WorkOrder o2) {
                Date o1Date, o2Date;
                try {
                    o1Date = o1.workorderLiteTO.getTimeReservationEnd();
                    if (o1Date == null)
                        return 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    return 1;
                }
                try {
                    o2Date = o2.workorderLiteTO.getTimeReservationEnd();
                    if (o2Date == null)
                        return -1;
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }
                return o1Date.compareTo(o2Date);
            }
        });

        return arrayList;
    }

    /**
     * Request GCloud Api for distance matrix among the clusters ,will save it to local db
     * Data can be reused to save cost
     *
     * @param locations
     */

    public static void fillDistancematrixAmongClusters(ArrayList<Cluster> locations) {

        currentLocationCluster = new SESPCluster(new LatLng(TspUtil.mMapsActivity.mMyLocation.getLatitude(),
                TspUtil.mMapsActivity.mMyLocation.getLongitude()));
        locations.add(currentLocationCluster);
        //progressDialog.setMessage("Downloading Data");
        ArrayList<LatLng> allItems = new ArrayList<>();
        /**
         * select L_FROM from G_DISTANCE_MATRIX where L_FROM == locations
         */

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select _id,L_FROM,L_TO from G_DISTANCE_MATRIX where L_FROM LIKE '");
        DatabaseHandler databaseHandler = DatabaseHandler.createDatabaseHandler();
        SQLiteDatabase liteDatabase = databaseHandler.getReadableDatabase();
        for (int i = 0; i < locations.size(); i++) {
            LatLng latLng = locations.get(i).getPosition();
            allItems.add(latLng);
        }

        ArrayList<LatLng> origins;
        ArrayList<LatLng> destinations;

        for (int i = 0; i < locations.size(); i++) {
            origins = new ArrayList<>();
            destinations = new ArrayList(allItems);
            LatLng origin = locations.get(i).getPosition();
            origins.add(origin);
            destinations.remove(allItems.get(i));

            String longitudeO =   String.valueOf(origin.longitude).length() >6?
                    String.valueOf(origin.longitude).substring(0, 6):String.valueOf(origin.longitude);
            String latitudeO = String.valueOf(origin.latitude).length() >6?
                    String.valueOf(origin.latitude).substring(0, 6):String.valueOf(origin.latitude);

            String queryString = stringBuilder.toString().concat("%").concat(latitudeO)
                    .concat("%").concat(longitudeO).concat("%'");

            Cursor cursor = liteDatabase.rawQuery(queryString, null);
            if (cursor.getCount() > 0) {
                while (cursor != null && cursor.moveToNext()) {
                    removeIfDataAvailable(destinations, cursor.getString(2));
                }

            }
            cursor.close();

            if (destinations.size() > 0) {

                String url = TspUtil.CONSTANTS.G_API_URL + TspUtil.CONSTANTS.G_METHOD_DM +
                        TspUtil.CONSTANTS.OUTPUT + "units=imperial";
                url = formUrlForDistanceMatrix(url, origins, destinations);

                DownloadTask downloadTask = new DownloadTask();
                ArrayList<LatLng> finalDestinations = destinations;
                ArrayList<LatLng> finalOrigins = origins;
                parseAndSaveToDB(downloadTask.doInBackground(url), finalDestinations, finalOrigins);
            }
        }

        locations.remove(currentLocationCluster);
        //progressDialog.setMessage("Finding Feasible Sequence");
    }

    private static String downloadUrl(String strUrl) throws IOException {
        String data = null;
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("URL Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    private static void removeIfDataAvailable(ArrayList<LatLng> destinations, String value) {

        ArrayList removables = new ArrayList<LatLng>();
        for (LatLng latLng : destinations) {

            if (latLng.toString().compareToIgnoreCase(value) == 0) {
                removables.add(latLng);
            }
        }

        destinations.removeAll(removables);
    }

    /**
     * Will Parse and save distance matrix to DB
     *
     * @param jsonObject
     * @param finalOrigins
     */

    private static synchronized void parseAndSaveToDB(String jsonObject, ArrayList<LatLng> finalDestinations, ArrayList<LatLng> finalOrigins) {

        DistanceMatrix distanceMatrix = null;
        try {
            distanceMatrix = CommunicationHelper.JSONMAPPER.readValue(jsonObject, DistanceMatrix.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder quStringBuilder = new StringBuilder();
        quStringBuilder.append("insert into G_DISTANCE_MATRIX (L_FROM,L_TO,ELEMENT) values( ");

        DatabaseHandler databaseHandler = DatabaseHandler.createDatabaseHandler();

        Row row = null;
        try {
            row = distanceMatrix.getRows().get(0);
        }catch (Exception e){

        }

        if (row!=null) {
            for (int i = 0; i < row.getElements().size(); i++) {
                Element element = row.getElements().get(i);
                Gson gson = new Gson();

                ContentValues contentValues = new ContentValues();
                contentValues.put("L_FROM", finalOrigins.get(0).toString());
                contentValues.put("L_TO", finalDestinations.get(i).toString());
                contentValues.put("ELEMENT", gson.toJson(element));
                contentValues.put("iu_timestamp", System.currentTimeMillis());
                databaseHandler.getWritableDatabase().insert("G_DISTANCE_MATRIX", null, contentValues);

            }
        }

    }

    private static String formUrlForDistanceMatrix(String url, ArrayList<LatLng> origins, ArrayList<LatLng> destinations) {

        StringBuilder originString = new StringBuilder();
        originString.append("origins=");
        originString.append(String.valueOf(origins.get(origins.size() - 1).latitude)).append(",")
                .append(String.valueOf(origins.get(origins.size() - 1).longitude));


        StringBuilder destinationString = new StringBuilder();
        destinationString.append("destinations=");
        for (int i = 0; i < destinations.size() - 1; i++) {
            LatLng latLng = destinations.get(i);
            destinationString.append(String.valueOf(latLng.latitude)).append(",")
                    .append(String.valueOf(latLng.longitude)).append("|");

        }
        destinationString.append(String.valueOf(destinations.get(destinations.size() - 1).latitude)).append(",")
                .append(String.valueOf(destinations.get(destinations.size() - 1).longitude));


        url = url + "&" + originString.toString() + "&" + destinationString.toString() + "&key="
                + DatabaseHandler.getMapApiKey();

        return url;
    }

    //Needs to be called only from non ui thread
    private long getActualTravelTime(LatLng sourcei, LatLng desti) {
        Long ttime = -1l;
        ArrayList dest = new ArrayList<LatLng>();
        ArrayList source = new ArrayList<LatLng>();
        dest.add(desti);
        source.add(sourcei);



        String url = TspUtil.CONSTANTS.G_API_URL + TspUtil.CONSTANTS.G_METHOD_DM +
                TspUtil.CONSTANTS.OUTPUT + "units=imperial";

        url = formUrlForDistanceMatrix(url, source, dest);

        try {
            DistanceMatrix distanceMatrixT = null;
            try {
                distanceMatrixT = CommunicationHelper.JSONMAPPER.readValue(downloadUrl(url), DistanceMatrix.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Row row = distanceMatrixT.getRows().get(0);
            Element element = row.getElements().get(0);

            ttime = Long.parseLong(String.valueOf(element.getDuration().getValue()));
        } catch (Exception e) {
            e.printStackTrace();
        }


        return ttime;
    }


    /**
     * @param etc               estimated time of completion of previous work order
     * @param previousWorkOrder
     * @return next feasible work order to perform
     */
    private WorkOrder findNextWorkOrder(Date etc, WorkOrder previousWorkOrder) {
        if (previousWorkOrder != null)
            etc = workOrderDateHashMap.get(previousWorkOrder);

        LatLng pCluster = null;
        WorkOrder returnValue = null;
        ArrayList<DistanceFromTo> distanceMatrix = null;

        //Starting Point
        if (false) {
            Date SLAEndTime = null;

            WorkOrder workOrderSla = null;
            indexSLA = 0;
            while (true) {
                if (indexSLA <choosedWorkOrdersonSLA.size()) {
                    workOrderSla = choosedWorkOrdersonSLA.get(indexSLA++);
                    if (canTimeReservationBeCovered(workOrderSla, etc, pCluster, distanceMatrix) && !workOrderDateHashMap.containsKey(workOrderSla)) {
                        if (!workOrderSla.sla.contains("null")) {
                            try {
                                SLAEndTime = simpleDateFormat.parse(workOrderSla.sla);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        break;
                    }
                }
                else break;
            }


            WorkOrder workOrderTr = null;
            indexTr = 0;
            while (true) {
                if (indexTr <choosedWorkOrdersTR.size()) {
                    workOrderTr = choosedWorkOrdersTR.get(indexTr++);
                    if (canTimeReservationBeCovered(workOrderTr, etc, pCluster, distanceMatrix) && !workOrderDateHashMap.containsKey(workOrderTr)) {
                        returnValue = workOrderTr;
                        break;
                    }
                }
                else break;
            }

            //find Nearest cluster from current location

            WorkOrder workOrderNearest = null;
            Collections.sort(clusterItemsSelected, new Comparator<SESPCluster>() {
                @Override
                public int compare(SESPCluster sespCluster, SESPCluster t1) {
                    return (int) (findDistanceBwn(new LatLng(mMapsActivity.mMyLocation.getLatitude(),
                            mMapsActivity.mMyLocation.getLongitude()), sespCluster.getPosition())
                            - findDistanceBwn(new LatLng(mMapsActivity.mMyLocation.getLatitude(),
                            mMapsActivity.mMyLocation.getLongitude()), t1.getPosition()));

                }
            });

            outerloop1:
            for (int i = 0; i < clusterItemsSelected.size(); i++) {
                SESPCluster sespCluster = clusterItemsSelected.get(i);
                int indexT = 0;
                for (WorkOrder workOrder : (ArrayList<WorkOrder>) sespCluster.getItems()) {
                    if (canTimeReservationBeCovered(workOrder, etc, pCluster, distanceMatrix)) {
                        workOrderNearest = workOrder;
                        break outerloop1;
                    }
                }
            }

            if (workOrderNearest == null){
                Toast.makeText(mContext,"No work orders in selection can be planned at the " +
                                "movement",
                        Toast.LENGTH_SHORT).show();
                return null;
            }

            CaseTCustomTO caseTS = ObjectCache.getIdObject(CaseTCustomTO.class, workOrderSla.getWorkorderLiteTO().getIdCaseT());
            CaseTCustomTO caseTN = ObjectCache.getIdObject(CaseTCustomTO.class, workOrderNearest.getWorkorderLiteTO().getIdCaseT());
            CaseTCustomTO caseTTR = ObjectCache.getIdObject(CaseTCustomTO.class, workOrderTr.getWorkorderLiteTO().getIdCaseT());

            if (SLAEndTime == null) {
                returnValue = workOrderNearest;
            } else if ((SLAEndTime.getTime() - System.currentTimeMillis())
                    > ((workOrderSla.getTravelTime() * 1000) + (caseTS.getAverageWorkingMinutes() * 60000)
                    + caseTN.getAverageWorkingMinutes() * 60000 + workOrderNearest.getTravelTime() * 1000)) {
                returnValue = workOrderNearest;
            } else
                returnValue = workOrderSla;

            if (returnValue == workOrderNearest) {
                if (workOrderTr.getWorkorderLiteTO().getTimeReservationEnd() != null) {
                    if (!((workOrderTr.getWorkorderLiteTO().getTimeReservationEnd().getTime() - System.currentTimeMillis())
                            > ((workOrderTr.getTravelTime() * 1000) + (caseTTR.getAverageWorkingMinutes() * 60000)
                            + caseTN.getAverageWorkingMinutes() * 60000 + workOrderNearest.getTravelTime() * 1000)))
                        returnValue = workOrderTr;
                }

            }

            {

                LatLng s = new LatLng(TspUtil.mMapsActivity.mMyLocation.getLatitude(),
                        TspUtil.mMapsActivity.mMyLocation.getLongitude());
                Long ttime = returnValue.travelTime;

                Date date = new Date();
                long timestamp = System.currentTimeMillis();
                final CaseTCustomTO caseT = ObjectCache.getIdObject(CaseTCustomTO.class, returnValue.getWorkorderLiteTO().getIdCaseT());
                timestamp = timestamp + (ttime * 1000) + (caseT.getAverageWorkingMinutes() * 1000 * 60);
                date.setTime(timestamp);
                workOrderDateHashMap.put(returnValue, date);
                returnValue.setTravelTime(ttime);

                return returnValue;
            }

        } else
            {
            StringBuilder inString = new StringBuilder();

            if (previousWorkOrder == null){
                previousWorkOrder = new WorkOrder();
                pCluster = currentLocationCluster.getPosition();
            }
             {
                for (Map.Entry<LatLng, ArrayList<WorkOrder>> map : clusterWorkOrders.entrySet()) {

                    if (map.getValue().contains(previousWorkOrder)) {
                        pCluster = map.getKey();
                    } else {
                        inString.append("'").append(map.getKey().toString()).append("',");
                    }
                }
            }

            distanceMatrix = new ArrayList<>();

            if  (!inString.toString().equals("")) {
                try {
                    String s = "";


                    s = inString.toString().substring(0, inString.toString().length() - 1);
                    String queryInit = "Select element,L_TO from G_DISTANCE_MATRIX where L_FROM like \"%";

                    String queryCondition = (String.valueOf(pCluster.latitude).length() >6?
                            String.valueOf(pCluster.latitude).substring(0, 6):
                            String.valueOf(pCluster.latitude) )+ "%" + (String.valueOf(pCluster.longitude).length() >6?
                                    String.valueOf(pCluster.longitude).substring(0, 6):
                            String.valueOf(pCluster.longitude)) + "%\"" + " AND "
                            + "L_TO IN ( " + s + " )";

                    DatabaseHandler databaseHandler = DatabaseHandler.createDatabaseHandler();
                    Cursor cursor = databaseHandler.getReadableDatabase().rawQuery(queryInit.concat(queryCondition), null);

                    Gson gson = new Gson();

                    while (cursor != null && cursor.moveToNext()) {
                        try {
                            Element element = gson.fromJson(cursor.getString(0), Element.class);
                            String ll = cursor.getString(1);
                            ll = ll.replace("lat/lng:", "");
                            ll = ll.replace("(", "");
                            ll = ll.replace(")", "");
                            ll = ll.replace(" ", "");
                            String[] latLngs = ll.split(",");

                            LatLng latLng = new LatLng(Double.parseDouble(latLngs[0]), Double.parseDouble(latLngs[1]));
                            DistanceFromTo distanceFromTo = new DistanceFromTo();
                            distanceFromTo.element = element;
                            distanceFromTo.latLng = latLng;
                            distanceMatrix.add(distanceFromTo);
                        } catch (Exception e) {

                        }
                    }
                } catch (Exception e) {
                    mMapsActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //update ui here
                            // display toast here
                        }
                    });
                    return null;
                }
            }


            ArrayList<WorkOrder> woInSameCluster = clusterWorkOrders.get(pCluster);
            WorkOrder notPerformedWoInSameCluster = null;
            WorkOrder nearestNeighbour = null;

            CaseTCustomTO caseTSLA = null;
            CaseTCustomTO caseTNeare = null;
            CaseTCustomTO caseTTr = null;

            if (woInSameCluster != null) {
                for (WorkOrder workOrder : woInSameCluster) {
                    if (!workOrderDateHashMap.containsKey(workOrder) && canTimeReservationBeCovered(workOrder, etc, pCluster, distanceMatrix)) {
                        notPerformedWoInSameCluster = workOrder;
                        caseTNeare = ObjectCache.getIdObject(CaseTCustomTO.class, notPerformedWoInSameCluster.getWorkorderLiteTO().getIdCaseT());
                        break;
                    }
                }
            }

            if (notPerformedWoInSameCluster == null) {

                //Find Nearest Work order/Cluster

                LatLng nC = null;

                Collections.sort(distanceMatrix, new Comparator<DistanceFromTo>() {
                    @Override
                    public int compare(DistanceFromTo distanceFromTo, DistanceFromTo t1) {
                        try {
                            return distanceFromTo.element.getDuration().getValue().compareTo(t1.element.getDuration().getValue());
                        }catch (Exception e){
                            return  0;
                        }
                    }
                });

                outerLoop:
                for (int i = 0; i < distanceMatrix.size(); i++) {
                    DistanceFromTo distanceFromTo = distanceMatrix.get(i);
                    for (Map.Entry entry : clusterWorkOrders.entrySet()) {
                        if (((LatLng) entry.getKey()).latitude == distanceFromTo.latLng.latitude &&
                                ((LatLng) entry.getKey()).longitude == distanceFromTo.latLng.longitude) {
                            if (!((workOrderDateHashMap.keySet().containsAll((ArrayList) entry.getValue())))) {
                                nC = (LatLng) entry.getKey();
                                if (nC != null) {
                                    for (WorkOrder workOrder : clusterWorkOrders.get(nC)) {
                                        if (!workOrderDateHashMap.containsKey(workOrder) && canTimeReservationBeCovered(workOrder, etc, pCluster, distanceMatrix)) {
                                            nearestNeighbour = workOrder;
                                            caseTNeare = ObjectCache.getIdObject(CaseTCustomTO.class, nearestNeighbour.getWorkorderLiteTO().getIdCaseT());
                                            break outerLoop;
                                        }
                                    }

                                }
                            }
                        }
                    }

                }
            } else
                nearestNeighbour = notPerformedWoInSameCluster;


            WorkOrder workOrderSla = null;
            Date slaEnds = null;
            try {
                for (int i = 0; i < choosedWorkOrdersonSLA.size(); i++) {
                    WorkOrder workOrder = choosedWorkOrdersonSLA.get(i);
                    if (!workOrderDateHashMap.containsKey(workOrder) && canTimeReservationBeCovered(workOrder, etc, pCluster, distanceMatrix)) {
                        workOrderSla = workOrder;
                        caseTSLA = ObjectCache.getIdObject(CaseTCustomTO.class, workOrderSla.getWorkorderLiteTO().getIdCaseT());
                        break;
                    }

                }
                slaEnds = simpleDateFormat.parse(workOrderSla.sla);

            } catch (Exception e) {
                e.printStackTrace();
            }

            WorkOrder workOrderTr = null;
            Date trEnds = null;
            try {
                for (int i = 0; i < choosedWorkOrdersTR.size(); i++) {
                    WorkOrder workOrder = choosedWorkOrdersTR.get(i);
                    if (!workOrderDateHashMap.containsKey(workOrder) && canTimeReservationBeCovered(workOrder, etc, pCluster, distanceMatrix)) {
                        workOrderTr = workOrder;
                        caseTTr = ObjectCache.getIdObject(CaseTCustomTO.class, workOrderTr.getWorkorderLiteTO().getIdCaseT());
                        break;
                    }

                }
                trEnds = workOrderTr.workorderLiteTO.getTimeReservationEnd();

            } catch (Exception e) {
                e.printStackTrace();
            }


            if (workOrderSla == null || slaEnds == null) {

                returnValue = nearestNeighbour;
            } else if ((slaEnds.getTime() - etc.getTime())
                    > ((workOrderSla.getTravelTime() * 1000) + (caseTSLA.getAverageWorkingMinutes() * 60000)
                    + caseTNeare.getAverageWorkingMinutes() * 60000 + nearestNeighbour.getTravelTime() * 1000)) {
                returnValue = nearestNeighbour;
            } else returnValue = workOrderSla;

            if (returnValue == nearestNeighbour) {
                if (workOrderTr != null && workOrderTr.getWorkorderLiteTO().getTimeReservationEnd() != null) {
                    if (!((workOrderTr.getWorkorderLiteTO().getTimeReservationEnd().getTime() - System.currentTimeMillis())
                            > ((workOrderTr.getTravelTime() * 1000) + (caseTTr.getAverageWorkingMinutes() * 60000)
                            + caseTNeare.getAverageWorkingMinutes() * 60000 + nearestNeighbour.getTravelTime() * 1000)))
                        returnValue = workOrderTr;
                }
            }

            if (returnValue == nearestNeighbour && (!mandateWoForTheDay.contains(nearestNeighbour))) {
                if (workOrderTr != null) {
                    returnValue = workOrderTr;
                } else if (workOrderSla != null) {
                    returnValue = workOrderSla;
                }
            }

            if (returnValue == null) {
                Date tr = null;
                Date today = new Date();
                today.setTime(System.currentTimeMillis());
                try {
                    //sla = simpleDateFormat.parse(workOrderSla.sla);

                    Calendar cal = Calendar.getInstance();
                    Calendar cal1 = Calendar.getInstance();
                    cal1.setTime(today);
                    for (WorkOrder workOrder : choosedWorkOrders) {

                        if (!workOrderDateHashMap.containsKey(workOrder)) {
                            if (canTimeReservationBeCovered(workOrder, etc, pCluster, distanceMatrix)) {
                                returnValue = workOrder;
                                break;
                            } else {
                                tr = workOrder.workorderLiteTO.getTimeReservationStart();
                                cal.setTime(tr);
                                if (cal.get(Calendar.DATE) == cal1.get(Calendar.DATE)) {
                                    returnValue = workOrder;
                                    break;
                                }
                            }

                        }
                    }

                } catch (Exception e) {

                }
            }
            if (returnValue != null) {

                long timestamp = etc.getTime();
                long travelTime = findDistanceBwnUsingDM(distanceMatrix, pCluster, returnValue);
                if (travelTime <0){
                    return null;
                }
                returnValue.setTravelTime(travelTime);
                CaseTCustomTO caseT = ObjectCache.getIdObject(CaseTCustomTO.class, returnValue.getWorkorderLiteTO().getIdCaseT());
                timestamp = (timestamp + (travelTime * 1000) + (caseT.getAverageWorkingMinutes() * 1000 * 60));
                Date date = new Date();
                date.setTime(timestamp);
                workOrderDateHashMap.put(returnValue, date);
                return returnValue;
            }

        }
        return null;
    }

    private boolean canTimeReservationBeCovered(WorkOrder workOrder, Date etcP, LatLng pCluster, ArrayList<DistanceFromTo> distanceMatrix) {

        long travelTime = -1;
        if (distanceMatrix != null && pCluster != null) {
            travelTime = findDistanceBwnUsingDM(distanceMatrix, pCluster, workOrder);

            if (travelTime <0){
                return false;
            }

        }

        else {
            LatLng s = new LatLng(TspUtil.mMapsActivity.mMyLocation.getLatitude(),
                    TspUtil.mMapsActivity.mMyLocation.getLongitude());

            travelTime = getActualTravelTime(s, workOrder.latLng);
        }
        if(travelTime == -1){
            return  false;
        }


        CaseTCustomTO caseT = ObjectCache.getIdObject(CaseTCustomTO.class, workOrder.getWorkorderLiteTO().getIdCaseT());
        workOrder.setTravelTime(travelTime);

        if (workOrder.getWorkorderLiteTO().getTimeReservationEnd() == null) {
            return true;
        }

        if ((workOrder.getWorkorderLiteTO().getTimeReservationStart().getTime()
                <= (etcP.getTime() + (travelTime * 1000))) &&
                workOrder.getWorkorderLiteTO().getTimeReservationEnd().getTime()
                        > (etcP.getTime() + (travelTime * 1000) + caseT.getAverageWorkingMinutes() * 60000)) {

            return true;

        }

        return false;
    }

    private long findDistanceBwnUsingDM(ArrayList<DistanceFromTo> distanceMatrix, LatLng pCluster, WorkOrder workOrder) {

        /**
         * returns distance between pCluster and workOrder using distance matrix
         */
        LatLng selectionCluster = null;

        for (Map.Entry entry : clusterWorkOrders.entrySet()) {

            if (((ArrayList) entry.getValue()).contains(workOrder)) {
                selectionCluster = (LatLng) entry.getKey();
            }

        }

        if (selectionCluster == pCluster)
            return STD_TT*60;

        for (DistanceFromTo distanceFromTo : distanceMatrix) {
            if (selectionCluster.latitude == distanceFromTo.latLng.latitude &&
                    selectionCluster.longitude == distanceFromTo.latLng.longitude) {
                try {
                    return distanceFromTo.element.getDuration().getValue();
                }catch (Exception e){
                    return  -1l;
                }

            }
        }

        return -1l;
    }


    /**
     * @param clusterItems
     * @return WorkOrder sorted in feasible sequence for performing
     */

    public void sequenceWorkOrders(ArrayList<SESPCluster> clusterItems, DownloadCallBack downloadCallBack) {

        clusterItemsSelected = new ArrayList<>();
        new AsyncTask<Object, Object, Object>() {

            @Override
            protected Object doInBackground(Object[] objects) {
                String endTimeP = SESPPreferenceUtil.getPreferenceString("END_TIME");
                String[] endStrings = endTimeP.split(":");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endStrings[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(endStrings[1]));

                endTime.setTime(calendar.getTime().getTime());


                indexSLA = 0;
                indexTr = 0;
                ArrayList<WorkOrder> workOrders = new ArrayList<>();

                clusterWorkOrders = new LinkedHashMap<>();


                for (SESPCluster clusterItem : clusterItems) {

                    if (((SESPCluster) clusterItem).isSelected()) {
                        clusterItemsSelected.add(clusterItem);
                        workOrders.addAll((ArrayList<WorkOrder>) ((SESPCluster) clusterItem).getItems());
                        clusterWorkOrders.put(clusterItem.getPosition(),
                                (ArrayList<WorkOrder>) ((SESPCluster) clusterItem).getItems());
                    }
                }

                fillDistancematrixAmongClusters((ArrayList)clusterItemsSelected);
                choosedWorkOrders = workOrders;
                choosedWorkOrdersonSLA = sortOnSLA(workOrders);
                choosedWorkOrdersTR = sortOnTimeReservationEnd(workOrders);
                mandateWoForTheDay = getMandateWorkOrderForTheDay(workOrders);

                workOrderDateHashMap = new LinkedHashMap<>();

                WorkOrder previousWorkOrder = null;
                WorkOrder currentWorkOrder = null;
                Date etc = new Date();
                etc.setTime(System.currentTimeMillis());


                while (true) {
                    currentWorkOrder = findNextWorkOrder(etc, previousWorkOrder);
                    etc = workOrderDateHashMap.get(currentWorkOrder);
                    if ((currentWorkOrder == null) || etc.compareTo(endTime) >= 0) {
                        break;
                    }
                    previousWorkOrder = currentWorkOrder;
                }

                ArrayList arrayList = new ArrayList<WorkOrder>();
                for (Map.Entry mEntry : workOrderDateHashMap.entrySet()) {

                    WorkOrder workOrder = (WorkOrder) mEntry.getKey();
                    Date date = (Date) mEntry.getValue();
                    workOrder.etc = date;

                    arrayList.add(workOrder);
                }
                return arrayList;
            }

            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(mMapsActivity);
                progressDialog.setTitle("Loading");
                progressDialog.setMessage("Finding Feasible Sequence");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                super.onPreExecute();
            }

            protected void onPostExecute(Object result) {
                downloadCallBack.ComputeCallBack((ArrayList<WorkOrder>) result);
                try {
                    progressDialog.dismiss();
                } catch (Exception e) {

                }
            }

        }.execute();

    }

    private List<WorkOrder> getMandateWorkOrderForTheDay(ArrayList<WorkOrder> workOrders) {

        ArrayList<WorkOrder> mandatary = new ArrayList();
        Date currentWoDateSLA = null;
        Date currentWoDateTR = null;
        Date today = new Date();

        Calendar calToday = Calendar.getInstance();
        calToday.setTime(today);
        Calendar calCompare = Calendar.getInstance();


        for (WorkOrder workOrder : workOrders) {
            if (!workOrder.sla.contains("null")) {
                try {
                    currentWoDateSLA = simpleDateFormat.parse(workOrder.sla);
                    calCompare.setTime(currentWoDateSLA);

                    if (calCompare.get(Calendar.DATE) == calToday.get(Calendar.DATE)) {
                        mandatary.add(workOrder);
                        continue;
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            currentWoDateTR = workOrder.workorderLiteTO.getTimeReservationEnd();
            if (currentWoDateTR != null) {
                calCompare.setTime(currentWoDateTR);
                if (calCompare.get(Calendar.DATE) == calToday.get(Calendar.DATE)) {
                    mandatary.add(workOrder);
                }
            }

        }

        return mandatary;
    }

    private double findDistanceBwn(LatLng latLng, LatLng item) {

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


}

