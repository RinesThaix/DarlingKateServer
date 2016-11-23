package ru.luvas.dk.server.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author RinesThaix
 */
public class PostExecutor {

    public static String executeGet(String urlGet) {
        try {
            URL url = new URL(urlGet);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null)
                    sb.append(line);
                return sb.toString();
            }
        } catch (Exception ex) {
            Logger.warn(ex, "Could not execute get-query!");
            return null;
        }
    }

    public static String executeGetGZIP(String urlGet) {
        try {
            URL url = new URL(urlGet);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(url.openStream()), "UTF-8"))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null)
                    sb.append(line);
                return sb.toString();
            }
        } catch (Exception ex) {
            Logger.warn(ex, "Could not execute get-query!");
            return null;
        }
    }

    public static InputStream execute(String urlGet) {
        try {
            String[] spl = urlGet.split("\\?");
            String urlClean = spl[0];
            URL url = new URL(urlClean);
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection) con;
            http.setRequestMethod("POST");
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            if(spl.length > 1) {
                String params = spl[1];
                http.setDoOutput(true);
                byte[] out = params.getBytes("UTF-8");
                int length = out.length;
                http.setFixedLengthStreamingMode(length);
                http.connect();
                try (OutputStream os = http.getOutputStream()) {
                    os.write(out);
                }
            }
            return http.getInputStream();
        } catch (Exception ex) {
            Logger.warn(ex, "Could not execute post-query '%s'!", urlGet);
            return null;
        }
    }

    public static String executeAndGet(String urlGet) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(execute(urlGet), "UTF-8"))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while((line = reader.readLine()) != null)
                sb.append(line);
            return sb.toString();
        } catch (Exception ex) {
            Logger.warn(ex, "Could not execute and get post-query!");
            return null;
        }
    }

    public static String encode(String toEncode, Object... params) {
        return encode(String.format(toEncode, params));
    }

    public static String encode(String toEncode) {
        try {
            return URLEncoder.encode(toEncode, "UTF-8");
        } catch (Exception ex) {
            Logger.warn("Could not encode given text!");
            return toEncode;
        }
    }

}
