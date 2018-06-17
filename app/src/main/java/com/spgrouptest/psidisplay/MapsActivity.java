package com.spgrouptest.psidisplay;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.spgrouptest.psidisplay.internal.GetPsiCommand;
import com.spgrouptest.psidisplay.internal.PsiListener;
import com.spgrouptest.psidisplay.internal.PsiParser;

import java.util.Iterator;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = MapsActivity.class.getName();
    private Context context;
    private Activity activity;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        activity = this;
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final PsiParser psiParser = new PsiParser(this.getApplicationContext());

        PsiListener psiListener = new PsiListener() {
            @Override
            public void onSuccess() {
                final Map<String, String> east = psiParser.getEast();
                final Map<String, String> west = psiParser.getWest();
                final Map<String, String> north = psiParser.getNorth();
                final Map<String, String> south = psiParser.getSouth();
                final Map<String, String> central = psiParser.getCentral();

                Log.d(TAG, "east: " + Double.parseDouble(east.get(GetPsiCommand.RESP_PARAM_LATITUDE)) + ", " + Double.parseDouble(east.get(GetPsiCommand.RESP_PARAM_LONGITUDE)));
                Log.d(TAG, "west: " + Double.parseDouble(west.get(GetPsiCommand.RESP_PARAM_LATITUDE)) + ", " + Double.parseDouble(west.get(GetPsiCommand.RESP_PARAM_LONGITUDE)));
                Log.d(TAG, "north: " + Double.parseDouble(north.get(GetPsiCommand.RESP_PARAM_LATITUDE)) + ", " + Double.parseDouble(north.get(GetPsiCommand.RESP_PARAM_LONGITUDE)));
                Log.d(TAG, "south: " + Double.parseDouble(south.get(GetPsiCommand.RESP_PARAM_LATITUDE)) + ", " + Double.parseDouble(south.get(GetPsiCommand.RESP_PARAM_LONGITUDE)));
                Log.d(TAG, "central: " + Double.parseDouble(central.get(GetPsiCommand.RESP_PARAM_LATITUDE)) + ", " + Double.parseDouble(central.get(GetPsiCommand.RESP_PARAM_LONGITUDE)));

                LatLng eastSg = new LatLng(Double.parseDouble(east.get(GetPsiCommand.RESP_PARAM_LATITUDE)), Double.parseDouble(east.get(GetPsiCommand.RESP_PARAM_LONGITUDE)));
                LatLng westSg = new LatLng(Double.parseDouble(west.get(GetPsiCommand.RESP_PARAM_LATITUDE)), Double.parseDouble(west.get(GetPsiCommand.RESP_PARAM_LONGITUDE)));
                LatLng northSg = new LatLng(Double.parseDouble(north.get(GetPsiCommand.RESP_PARAM_LATITUDE)), Double.parseDouble(north.get(GetPsiCommand.RESP_PARAM_LONGITUDE)));
                LatLng southSg = new LatLng(Double.parseDouble(south.get(GetPsiCommand.RESP_PARAM_LATITUDE)), Double.parseDouble(south.get(GetPsiCommand.RESP_PARAM_LONGITUDE)));
                LatLng centralSg = new LatLng(Double.parseDouble(central.get(GetPsiCommand.RESP_PARAM_LATITUDE)), Double.parseDouble(central.get(GetPsiCommand.RESP_PARAM_LONGITUDE)));

                Marker eastMarker = mMap.addMarker(new MarkerOptions().position(eastSg).title("East Singapore"));
                eastMarker.setTag("east");
                Marker westMarker = mMap.addMarker(new MarkerOptions().position(westSg).title("West Singapore"));
                westMarker.setTag("west");
                Marker northMarker = mMap.addMarker(new MarkerOptions().position(northSg).title("North Singapore"));
                northMarker.setTag("north");
                Marker southMarker = mMap.addMarker(new MarkerOptions().position(southSg).title("South Singapore"));
                southMarker.setTag("south");
                Marker centralMarker = mMap.addMarker(new MarkerOptions().position(centralSg).title("Central Singapore"));
                centralMarker.setTag("central");

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        String readings = "";

                        switch (marker.getTag().toString()) {
                            case "east":
                                readings = concatString(east);
                                break;
                            case "west":
                                readings = concatString(west);
                                break;
                            case "north":
                                readings = concatString(north);
                                break;
                            case "south":
                                readings = concatString(south);
                                break;
                            case "central":
                                readings = concatString(central);
                                break;
                            default:
                                Log.d(TAG, "Unknown marker");
                                break;
                        }
                        readings = readings + "Status: " + psiParser.getStatus();
                        showAlertDialog(activity, "PSI Info", readings);
                        return false;
                    }
                });

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centralSg,10));
            }

            @Override
            public void onError(String errorMessage) {
                ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
                ComponentName topActivity = activityManager.getRunningTasks(1).get(0).topActivity;
                showAlertDialog(activity, "Error", errorMessage);
            }
        };
        psiParser.processPsi(psiListener);
    }

    private void showAlertDialog(Activity activity, String title, String message) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private String concatString(Map<String, String> map) {
        String readings = "";
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            if (key == GetPsiCommand.RESP_PARAM_LATITUDE || key == GetPsiCommand.RESP_PARAM_LONGITUDE) {
                continue;
            }
            String value = map.get(key);
            readings = readings + key + ": " + value + "\n";
        }
        Log.d(TAG, readings);
        return readings;
    }
}
