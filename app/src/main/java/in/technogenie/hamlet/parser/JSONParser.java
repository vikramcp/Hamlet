package in.technogenie.hamlet.parser;

import android.support.annotation.NonNull;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class JSONParser {

    private static final String MEMBERS_URL = "https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=17Wrov7FQV6vIYh4ndsZRQINstmflbRA9s8WkPgNbbe4&sheet=Sheets1";
    private static final String EVENTS_URL = "https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=10bHphTkpeCWSvPRBrwTDdROQJ2lb5ulr2ZNmNCSynd4&sheet=Sheet1";

    public static final String TAG = "JSONParser";

    private static final String KEY_USER_ID = "user_id";

    private static Response response;

 /*   public static JSONObject getDataFromwweb() {

    }*/

    private String downloadUrl(String urlString) throws IOException {
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int responseCode = conn.getResponseCode();
            is = conn.getInputStream();
            String contentAsString = convertStreamToString(is);
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static JSONObject getDataFromWeb() {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(MEMBERS_URL).build();
            response = client.newCall(request).execute();

            Log.d("JSON Data 1 :", "" + response.body().string());

           // result=getJSONUrl(url);  //<< get json string from server
            JSONObject jsonObject = new JSONObject(response.body().string());

            Log.d("JSON Object 2 :", "" + jsonObject);
            return jsonObject;

        } catch (@NonNull IOException | JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "" + e);
        } catch (Exception e) {
            Log.e(TAG, "" + e);
        }
        return null;
    }

    public static JSONArray getMemberDataArrayFromWeb() {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(MEMBERS_URL)
                    .build();
            response = client.newCall(request).execute();

            //Log.d("JSON Data :", "" + response.body().string());

            String stringToParse  = response.body().string();
            System.out.println("JSON String Data 1 :" + stringToParse);

            // result=getJSONUrl(url);  //<< get json string from server
            JSONObject jsonObject = new JSONObject(stringToParse);

           // Log.d("JSON Object :", "" + jsonObject);

            JSONArray array = jsonObject.getJSONArray(Keys.KEY_CONTACTS);
          //  Log.d("JSON Array :", "" + array);

            return array;

        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONArray getEventsDataArrayFromWeb() {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(EVENTS_URL)
                    .build();
            response = client.newCall(request).execute();

            //Log.d("JSON Data :", "" + response.body().string());

            String stringToParse = response.body().string();
            System.out.println("JSON String Data 1 :" + stringToParse);

            // result=getJSONUrl(url);  //<< get json string from server
            JSONObject jsonObject = new JSONObject(stringToParse);

            // Log.d("JSON Object :", "" + jsonObject);

            JSONArray array = jsonObject.getJSONArray(Keys.EVENT_CONTACTS);
            Log.d("JSON Array :", "" + array);

            return array;

        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject getDataById(int userId) {

        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormEncodingBuilder()
                    .add(KEY_USER_ID, Integer.toString(userId))
                    .build();

            Request request = new Request.Builder()
                    .url(MEMBERS_URL)
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());

        } catch (IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }
}
