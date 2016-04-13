package com.polarcape.plugins.geolocation;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.provider.Settings;


public class PCGeolocation extends CordovaPlugin {
    
    private CallbackContext callbackContextPlugin;
    private JSONObject translations;
    public PCGeolocation() {
    }
    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        translations = args.getJSONObject(0);
        if (action.equals("getLocation")) {
            Context context = cordova.getActivity();
            JSONObject result = getCurrentLocation(context);
            PluginResult pluginResult;
            if(result!=null){
                pluginResult = new PluginResult(PluginResult.Status.OK, result);
                pluginResult.setKeepCallback(true);
                callbackContext.sendPluginResult(pluginResult);
            }else{
                pluginResult = new PluginResult(PluginResult.Status.ERROR, "null");
                pluginResult.setKeepCallback(true);
                showSettingsAlert(context);
            }
            
            return true;
        }
        return false;
    }
    
    private JSONObject getCurrentLocation(Context context) {
        try {
            JSONObject returnObject = null;
            GPSTracker gps = new GPSTracker(context);
            if(gps.canGetLocation()){
                returnObject = new JSONObject();
                returnObject.put("lat", gps.getLatitude());
                returnObject.put("lng", gps.getLongitude());
                returnObject.put("error",false);
            }
            return returnObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            System.out.println("REQUEST CODE: " + requestCode + " ; RESULT CODE: " +resultCode);
            Context ctx = cordova.getActivity();
            GPSTracker gps = new GPSTracker(ctx);
            PluginResult pluginResult = null;
            if(gps.canGetLocation()){
                JSONObject returnObject = new JSONObject();
                returnObject.put("lat", gps.getLatitude());
                returnObject.put("lng", gps.getLongitude());
                returnObject.put("error",false);
                pluginResult = new PluginResult(PluginResult.Status.OK, returnObject);
            }else{
                JSONObject returnObject = new JSONObject();
                returnObject.put("error",true);
                returnObject.put("reason","opened Settings, but not activated location services");
                pluginResult = new PluginResult(PluginResult.Status.OK, returnObject);
            }
            callbackContextPlugin.sendPluginResult(pluginResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * @throws JSONException
     * */
    public void showSettingsAlert(Context context) throws JSONException{
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        
        // Setting Dialog Title
        alertDialog.setTitle(translations.getString("title"));
        
        // Setting Dialog Message
        alertDialog.setMessage(translations.getString("message"));
        
        // On pressing Settings button
        alertDialog.setPositiveButton(translations.getString("ok"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                startGPSIntent();
            }
        });
        
        // on pressing cancel button
        alertDialog.setNegativeButton(translations.getString("cancel"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    dialog.cancel();
                    JSONObject returnObject = new JSONObject();
                    returnObject.put("error",true);
                    returnObject.put("reason","NOT opened Settings, but not activated location services");
                    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, returnObject);
                    callbackContextPlugin.sendPluginResult(pluginResult);	
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
        // Showing Alert Message
        alertDialog.show();
    }
    private void startGPSIntent() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        this.cordova.startActivityForResult(this,intent,0);
        
    }
}
