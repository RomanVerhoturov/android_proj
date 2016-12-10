package istu.edu.irnitu.IOPackage;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * NewFitGid
 * Created by Александр on 14.06.2016.
 * Contact on luck.alex13@gmail.com
 * © Aleksandr Novikov 2016
 */
public class InternetController {
    private static  String LOG_TAG = "LOG_TAG";
    public static boolean hasConnection(Context appContext) {
        boolean connection = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) { // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        connection = true;
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // connected to the mobile provider's data plan
                        connection = true;
                    }
                } else {
                    // not connected to the internet
                    connection = false;
                }
            }
        }catch (Exception ex){
            Log.d(LOG_TAG, "InternetController: has NO Connection() " + ex);
            ex.printStackTrace();
            connection = false;
        }
        return connection;
    }
}
