package kos.testmobile;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;



class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

    public static String URLMESSAGE = "could not connect";

    static final String COOKIES_HEADER = "Set-Cookie";
    static java.net.CookieManager msCookieManager = new java.net.CookieManager();
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Boolean doInBackground(String... strs) {
        try {
            System.out.println("in here");
            URL Endpoint = null;

            String url = strs[0];
            String req = strs[1];
           // Endpoint = new URL("http://192.168.21.46:80/" + url);
            Endpoint = new URL("http://192.168.0.25:80/" + url);
            System.out.println(Endpoint.toString());

            HttpURLConnection myConnection = null;

            myConnection = (HttpURLConnection) Endpoint.openConnection();
            myConnection.setRequestMethod("POST");

            if (msCookieManager.getCookieStore().getCookies().size() > 0) {
                // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
                myConnection.setRequestProperty("Cookie",
                        TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
            }
            System.out.println("COOKIES: "+myConnection.getRequestProperty("Cookie"));

            myConnection.setRequestProperty("Content-Type","application/json");

            //req= "{\"username\": \"bob\"}"
            System.out.println(req);
            byte[] outputInBytes = req.getBytes("UTF-8");
            OutputStream os = myConnection.getOutputStream();
            os.write( outputInBytes );
            os.close();


//            if(mCookieManager.getCookieStore().getCookies().size() > 0) {
//                System.out.println("COOKIE HERE");
//                myConnection.setRequestProperty("Cookie",
//                        TextUtils.join(";", mCookieManager.getCookieStore().getCookies()));
//            }

            if (myConnection.getResponseCode() == 200) {
//                Map<String, List<String>> headers = myConnection.getHeaderFields();
//                List<String> cookies = headers.get(MainActivity.COOKIE_HEADER);
//
//                for(String cookie: cookies){
//                    HttpCookie httpCookie = HttpCookie.parse(cookie).get(0);
//                    mCookieManager.getCookieStore().add(null, httpCookie);
//                }
                // Success
                // Further processing here
                System.out.println("Aaaaa");
                Map<String, List<String>> headerFields = myConnection.getHeaderFields();
                List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

                if (cookiesHeader != null) {
                    for (String cookie : cookiesHeader) {
                        msCookieManager.getCookieStore().add(null,HttpCookie.parse(cookie).get(0));
                    }
                }
                //br = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
                //InputStream responseBody = myConnection.getInputStream();
                 //       new InputStreamReader(responseBody, "UTF-8");
                //JsonReader jsonReader = new JsonReader(responseBodyReader);
                //TESTMESSAGE+= responseBodyReader.toString();
                //jsonReader.close();
                String jsonResponse = readInputStreamToString(myConnection);
                URLMESSAGE = jsonResponse;
                System.out.println("2" + URLMESSAGE);

            } else {
                // Error handling code goes here
                URLMESSAGE= "COULD NOT CONNECT";
                System.out.println("2" + URLMESSAGE);
            }

            return false;
        }
        catch (Exception e) {
        }
        return true;
    }


    protected void onPostExecute(Boolean result) {

    }


    private String TAG = "";

    /**
     * @param connection object; note: before calling this function,
     *                   ensure that the connection is already be open, and any writes to
     *                   the connection's output stream should have already been completed.
     * @return String containing the body of the connection response or
     * null if the input stream could not be read correctly
     */
    private String readInputStreamToString(HttpURLConnection connection) {
        String result = null;
        StringBuffer sb = new StringBuffer();
        InputStream is = null;

        try {
            is = new BufferedInputStream(connection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            result = sb.toString();
        } catch (Exception e) {
            Log.i(TAG, "Error reading InputStream");
            result = null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.i(TAG, "Error closing InputStream");
                }
            }
        }

        return result;
    }
}