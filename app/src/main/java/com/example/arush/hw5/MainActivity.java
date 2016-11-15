package com.example.arush.hw5;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
{
    private static final String DEBUG_TAG = "HttpExample";
    private TextView t1;
    private Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1=(TextView) findViewById(R.id.textView2);
      //  b1=(Button) findViewById(R.id.button2);
        String stringUrl = "https://www.iiitd.ac.in/about";
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            new DownloadWebpageTask().execute(stringUrl);
        }
        else
        {
            t1.setText("No network connection available.");
        }
    }


    private class DownloadWebpageTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                return downloadUrl(urls[0]);
            }
            catch (IOException e)
            {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            t1.setText(result);
        }
    }

    private String downloadUrl(String myurl) throws IOException
    {

        InputStream is = null;
        int len = 500;
        try
        {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            String contentAsString = readIt(is, len);
            return contentAsString;

        }
        finally
        {
            if (is != null)
            {
                is.close();
            }
        }
    }

    public String readIt(InputStream stream, int len)
            throws IOException, UnsupportedEncodingException
    {
//        Reader reader = null;
//        reader = new InputStreamReader(stream, "UTF-8");
//        char[] buffer = new char[len];
//        reader.read(buffer);
//        return new String(buffer);
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = stream.read(buffer)) != -1)
        {
            result.write(buffer, 0, length);
            Log.d("myTag", buffer.toString());
        }
        Log.d("myTag",result.toString("UTF-8"));
        String r = result.toString("UTF-8").replace("\r", "");
        Log.d("myTag",r);
        int maxLogSize = 1000;
        for(int i = 0; i <= r.length() / maxLogSize; i++)
        {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > r.length() ? r.length() : end;
            Log.d("myTag", r.substring(start, end));
        }

        Log.d("myTag", String.valueOf(result.toString("UTF-8").length()));
        return result.toString("UTF-8");
    }
}
