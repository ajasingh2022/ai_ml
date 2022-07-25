package com.capgemini.sesp.ast.android.module.tsp;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.tsp.TspUtil.mMapsActivity;
import static com.capgemini.sesp.ast.android.module.tsp.TspUtil.polyline;

/** Task For parsing JSON Object

 */
public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

    private ProgressDialog progressDialog;
    // Parsing the data in non-ui thread

    @Override
    protected void onPreExecute() {

        progressDialog = new ProgressDialog(mMapsActivity);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Making View");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try{
            jObject = new JSONObject(jsonData[0]);
            DirectionJSONParser parser = new DirectionJSONParser();

            // Starts parsing data
            routes = parser.parse(jObject);
        }catch(Exception e){
            e.printStackTrace();
        }
        return routes;
    }

    // Executes in UI thread, after the parsing process
    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;
        MarkerOptions markerOptions = new MarkerOptions();

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

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(6);
                lineOptions.color(Color.argb(0xFF, 0, 0, 1));
            }

            // Drawing polyline in the Google Map for the i-th route
            polyline = TspUtil.mMap.addPolyline(lineOptions);
            mMapsActivity.polyline = polyline;
        }catch (Exception e){
            Toast.makeText(mMapsActivity,"Service Not Available",Toast.LENGTH_SHORT).show();
        }
        TspUtil.progressDialog.dismiss();
    }
}