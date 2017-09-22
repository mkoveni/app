package apps.scg.craton.scgsurveyapp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import apps.scg.craton.scgsurveyapp.dao.api.ResponseApi;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/09/18
 */

public class SyncReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if(isOnline(context)) try {
            new ResponseApi().syncResponses();
        } catch (DataException e) {
            e.printStackTrace();
        }
    }

    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }
}
