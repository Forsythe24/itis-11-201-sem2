package itis.solopov.client;

import java.util.Map;

public interface HttpClient {
    String get(String URL, Map<String, Object> params);
    String post(String URL, Map<String, Object> params);
    String put(String URL, Map<String, Object> params);
    String delete(String URL, Map<String, Object> params);
}
