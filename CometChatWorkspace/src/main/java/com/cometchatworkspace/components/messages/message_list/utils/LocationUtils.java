package com.cometchatworkspace.components.messages.message_list.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import com.cometchatworkspace.R;
import com.cometchatworkspace.components.messages.common.extensions.ExtensionResponseListener;
import com.cometchatworkspace.resources.constants.UIKitConstants;
import com.cometchatworkspace.resources.utils.Utils;

public class LocationUtils {
    private Context context;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location;
    private final long MIN_TIME = 1000;
    private final long MIN_DIST = 5;
    private FusedLocationProviderClient fusedLocationProviderClient;
    double LATITUDE = 0;
    double LONGITUDE = 0;

    public void initialize(Context ct, ExtensionResponseListener responseListener) {
        context = ct;
        try {
            initLocation();
            boolean gpsprovider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!gpsprovider) {
                turnOnLocation();
            } else {
                getLocation(responseListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getLocation(ExtensionResponseListener responseListener) {

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location_) {
                if (location_ != null) {
                    double lon = location_.getLongitude();
                    double lat = location_.getLatitude();

                    JSONObject customData = new JSONObject();
                    try {
                        customData.put("latitude", lat);
                        customData.put("longitude", lon);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, context.getString(R.string.unable_to_get_location), Toast.LENGTH_LONG).show();
                    }

                    initAlert(customData, responseListener);
                } else if (location != null) {
                    double lon = location.getLongitude();
                    double lat = location.getLatitude();

                    JSONObject customData = new JSONObject();
                    try {
                        customData.put("latitude", lat);
                        customData.put("longitude", lon);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    initAlert(customData, responseListener);
                } else {
                    Toast.makeText(context, context.getString(R.string.unable_to_get_location), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initAlert(JSONObject customData,ExtensionResponseListener responseListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.map_share_layout,null);
        builder.setView(view);
        try {
            LATITUDE = customData.getDouble("latitude");
            LONGITUDE = customData.getDouble("longitude");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView address = view.findViewById(R.id.address);
        address.setText("Address: "+ Utils.getAddress(context,LATITUDE,LONGITUDE));
        ImageView mapView = view.findViewById(R.id.map_vw);
        String mapUrl = UIKitConstants.MapUrl.MAPS_URL +LATITUDE+","+LONGITUDE+"&key="+
                UIKitConstants.MapUrl.MAP_ACCESS_KEY;
        Glide.with(context)
                .load(mapUrl)
                .placeholder(R.drawable.default_map)
                .into(mapView);

        builder.setPositiveButton(context.getString(R.string.share), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                responseListener.OnResponseSuccess(customData);
            }
        }).setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    private void turnOnLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.turn_on_gps));
        builder.setPositiveButton(context.getString(R.string.on), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity)context).startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), UIKitConstants.RequestCode.LOCATION);
            }
        }).setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    private void initLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location l) {
                location = l;
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
