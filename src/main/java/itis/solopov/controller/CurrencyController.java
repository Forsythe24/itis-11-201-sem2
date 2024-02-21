package itis.solopov.controller;


import itis.solopov.client.HttpClient;
import itis.solopov.client.HttpClientImpl;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CurrencyController {

    private final static String URL_CURRENCY = "https://www.cbr-xml-daily.ru/daily_json.js";
    private final static String RUBLE_SIGN = "&#8381";
    private final static String BREAK = "<br>";
    @GetMapping("/currency")
    public String currency() {
        String currencyJSON;
        try {
            currencyJSON = getCurrencyJSON();
        } catch (IOException e) {
            return "ERROR: bad connection.";
        }
        try {
            return parseCurrencyJSON(currencyJSON);
        } catch (JSONException e) {
            return "ERROR: bad data provided. Response from API: " + currencyJSON;
        }
    }
    private String getCurrencyJSON() throws IOException {

        HttpClientImpl client = new HttpClientImpl();
        return client.get(URL_CURRENCY, new HashMap<>());
    }

    private String parseCurrencyJSON(String currencyJSON) throws JSONException {
        StringBuilder sbResult = new StringBuilder();
        JSONObject json = new JSONObject(currencyJSON);

        JSONObject allCurrencyInfo = json.getJSONObject("Valute");

        String[] allCurrencyNames = JSONObject.getNames(allCurrencyInfo);

        for (String name : allCurrencyNames) {
            BigDecimal currencyValue = allCurrencyInfo.getJSONObject(name).getBigDecimal("Value");
            sbResult.append(name).append(": ").append(currencyValue).append(" ").append(RUBLE_SIGN).append(BREAK);
        }

        return sbResult.toString();
    }





}
