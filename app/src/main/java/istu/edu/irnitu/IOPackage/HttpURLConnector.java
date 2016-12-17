package istu.edu.irnitu.IOPackage;

import android.text.TextUtils;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;


/**
 * NewFitGid
 * Created by Александр on 30.05.2016.
 * Contact on luck.alex13@gmail.com
 * © Aleksandr Novikov 2016
 */
public class HttpURLConnector {
    private URL url;
    private final String LOG_TAG = "LOG_TAG_CONN";
    private static final String COOKIES_HEADER = "Set-Cookie";
    public static final int TIMEOUT_300 = 300000;//300
    public static final int TIMEOUT_60 = 60000;//60
    public static final int TIMEOUT_120 = 120000;//120

    private CookieManager cookieManager;
    private Map<String, List<String>> headerFields;
    private List<String> cookiesHeader;
    private OutputStreamWriter outputStreamWriter;
    private InputStream inputStream;
    private BufferedReader bufferedReader;
    private StringBuffer response, request;
    private HttpURLConnection connection;
    private Document jsoupDocument;
    private String[] accountData;

    private String errMessage;

    public HttpURLConnector(URL url) {
        this.url = url;
        connection = null;
        jsoupDocument = null;
    }
    public String simplePostRequest(String requestParams) {
        String body = null;
        response = new StringBuffer();
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Accept-Language", "UTF-8");
            connection.setRequestProperty("User-Agent", "CodeJava Agent");
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setReadTimeout(HttpURLConnector.TIMEOUT_120);
            connection.setConnectTimeout(HttpURLConnector.TIMEOUT_120);
            connection.connect();

            outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
            outputStreamWriter.write(requestParams);
            outputStreamWriter.flush();
            outputStreamWriter.close();
            int responseCode = connection.getResponseCode();
            Log.d(LOG_TAG, "Sending 'POST' request to URL : " + url);
            Log.d(LOG_TAG, "Post parameters : " + requestParams);
            Log.d(LOG_TAG, "Response Code : " + responseCode);

            inputStream = connection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();
            inputStream.close();

            jsoupDocument = Jsoup.parse(response.toString());
            body = jsoupDocument.body().toString();


            cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);
            headerFields = connection.getHeaderFields();
            cookiesHeader = headerFields.get(COOKIES_HEADER);
//
            if (cookiesHeader != null) {
                Log.d(LOG_TAG, "cookiesHeader != null ");
                for (String cookies : cookiesHeader) {
                    Log.d(LOG_TAG, "cookie " + cookies);

                }
            } else {
                Log.d(LOG_TAG, "cookiesHeader = null ");

            }

        } catch (UnknownHostException uhe) {
            errMessage = "votingConnection UnknownHostException " + uhe.toString();
            body = errMessage;
            Log.d(LOG_TAG, errMessage);
            uhe.printStackTrace();

        } catch (IOException e) {
            // writing exception to log
            errMessage = "votingConnection IOException " + e.toString();
            body = errMessage;
            Log.d(LOG_TAG, errMessage);
            e.printStackTrace();
        } finally {
            if (connection != null) {
                Log.d(LOG_TAG, "votingConnection connection.disconnect() ");
                connection.disconnect();
            }
        }
        return body;
    }
    public String simpleGetRequest() {
        String result = null;
        response = new StringBuffer();
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Accept-Language", "UTF-8");
            connection.setRequestProperty("User-Agent", "CodeJava Agent");
            connection.setRequestMethod("GET");

            connection.setDoInput(true);
            connection.setReadTimeout(HttpURLConnector.TIMEOUT_120);
            connection.setConnectTimeout(HttpURLConnector.TIMEOUT_120);
            connection.connect();

            int responseCode = connection.getResponseCode();
            Log.d(LOG_TAG, "Sending 'GET' request to URL : " + url);
            Log.d(LOG_TAG, "Response Code : " + responseCode);

            inputStream = connection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();
            inputStream.close();
            result = response.toString();
            jsoupDocument = Jsoup.parse(response.toString());
        } catch (UnknownHostException uhe) {
            errMessage = "simpleGetRequest UnknownHostException " + uhe.toString();
            response.append(errMessage);
            Log.d(LOG_TAG, errMessage);
            uhe.printStackTrace();

        } catch (IOException e) {
            // writing exception to log
            errMessage = "simpleGetRequest IOException " + e.toString();
            response.append(errMessage);
            Log.d(LOG_TAG, errMessage);
            e.printStackTrace();
        } finally {
            if (connection != null) {
                Log.d(LOG_TAG, "simpleGetRequest connection.disconnect() ");
                connection.disconnect();
            }
        }
        return result;
    }
    public Document getJsoupDocument() {
        return jsoupDocument;
    }
}
