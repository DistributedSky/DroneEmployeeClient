package com.droneemployee.client;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by simon on 18.06.16.
 */
public class UrlPostTest {
    private static final String TAG = "UrlPostTest";

    @org.junit.Test
    public void test() throws IOException {
        String urlSpec = "http://localhost:7453";
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                    ": with" +
                    urlSpec);
            }
            System.out.println(TAG + ": " + "Connection OK");


        } finally {
            connection.disconnect();
        }
    }
}
