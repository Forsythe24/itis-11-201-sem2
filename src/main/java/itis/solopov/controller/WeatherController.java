package itis.solopov.controller;

import itis.solopov.client.HttpClientImpl;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

@RestController
public class WeatherController {
    private final static String URL_WEATHER = "https://api.openweathermap.org/data/2.5/weather?";
    private final static String KEY = "9bb29b262c154821f0022ca03f091ade";
    private static String cityName;

    @GetMapping("/weather")
    private String weather() {
        String cityWeatherInfoJSON;
        try {
            cityWeatherInfoJSON = getCityWeatherInfoJSON("Kazan");
        } catch (IOException e) {
            return "ERROR: bad connection.";
        }
        try {
            return getParsedWeatherInfo(cityWeatherInfoJSON);
        } catch (JSONException e) {
            return "ERROR: bad data provided. Response from API: " + cityWeatherInfoJSON;
        }
        
    }

    private String getCityWeatherInfoJSON(String city) throws IOException {
        HttpClientImpl client = new HttpClientImpl();
        cityName = city;
        return client.get(URL_WEATHER + "q=" + city + "&appid=" + KEY, new HashMap<>());
    }


    private String getParsedWeatherInfo(String response) throws JSONException {

        JSONObject jsonResponse = new JSONObject(response);

        BigDecimal temperature = jsonResponse.getJSONObject("main").getBigDecimal("temp");

        BigDecimal humidity = jsonResponse.getJSONObject("main").getBigDecimal("humidity");

        String precipitation = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");

        return "Here's your weather forecast for " + capitalize(cityName) + ":" + '\n' + "Temperature: " + temperature.toString() + '\n' + '\n' +
                "Humidity: " + humidity + '\n' + '\n' +
                "Precipitation: " + precipitation +
                "\n";
    }

    private String capitalize(String cityName) {
        String[] pieces = cityName.split(" ");
        StringBuilder sbResult = new StringBuilder();
        for (String piece : pieces) {
            String capitalized = piece.substring(0, 1).toUpperCase() + piece.substring(1);
            sbResult.append(capitalized).append(" ");
        }
        sbResult.deleteCharAt(sbResult.length() - 1);
        return sbResult.toString();
    }
}
