package itis.solopov.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.json.JSONObject;

public class HttpClientImpl implements HttpClient {
    private final static String TOKEN = "Bearer 171ac2eab31b3cdd76a6ff129ce3e15106d600c8db508dc655b395be5f7a24bc";

    @Override
    public String get(String strURL, Map<String, Object> params) {
        try {
            strURL += convertMapIntoParamsStr(params);
            URL url = new URL(strURL);
            HttpURLConnection getConnection = (HttpURLConnection) url.openConnection();

            setUpConnection(getConnection, "GET");

            String response = readResponse(getConnection);
            getConnection.disconnect();

            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String convertMapIntoParamsStr(Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            StringBuilder sbParams = new StringBuilder();
            for (String key : map.keySet()) {
                if (key == null) {
                    throw new IllegalArgumentException("Keys must not be null in map.");
                }
                sbParams.append(key).append("=").append(map.get(key)).append("&");
            }
            return sbParams.toString();
        }
        return "";
    }

    @Override
    public String post(String strURL, Map<String, Object> params) {
        try {
            URL url = new URL(strURL);
            JSONObject json = new JSONObject(params);
            HttpURLConnection postConnection = (HttpURLConnection) url.openConnection();

            setUpConnection(postConnection, "POST");

            String jsonInput = json.toString();
            System.out.println(jsonInput);

            writeToConn(postConnection, jsonInput);

            System.out.println(postConnection.getResponseCode());

            String response = readResponse(postConnection);
            postConnection.disconnect();
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String delete(String strURL, Map<String, Object> params) {
        try {
            URL url = new URL(strURL);
            JSONObject json = new JSONObject(params);
            HttpURLConnection deleteConnection = (HttpURLConnection) url.openConnection();

            setUpConnection(deleteConnection, "DELETE");

            String jsonInput = json.toString();

            writeToConn(deleteConnection, jsonInput);

            System.out.println(deleteConnection.getResponseCode());

            String response = readResponse(deleteConnection);
            deleteConnection.disconnect();
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeToConn(HttpURLConnection connection, String jsonInput) {
        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String put(String URL, Map<String, Object> params) {
        try {
            URL url = new URL(URL);
            JSONObject json = new JSONObject(params);
            HttpURLConnection putConnection = (HttpURLConnection) url.openConnection();

            setUpConnection(putConnection, "PUT");

            String jsonInput = json.toString();
            System.out.println(jsonInput);
            writeToConn(putConnection, jsonInput);

            System.out.println(putConnection.getResponseCode());

            String response = readResponse(putConnection);
            putConnection.disconnect();
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String readResponse(HttpURLConnection connection) throws IOException {
        if (connection != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder content = new StringBuilder();
                String input;
                while ((input = reader.readLine()) != null) {
                    content.append(input);
                }
                return content.toString();
            }
        }
        return null;
    }

    private static void setUpConnection(HttpURLConnection connection, String method) throws IOException {
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", TOKEN);
        connection.setDoOutput(true);

        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
    }
}
