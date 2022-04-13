package ru.geekbrains.bank.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ExchangeRates {

    private final String TOKEN = "BHZWGZDY1J3MGJPH";
    private double usdRate;
    private double eurRate;
    private double cadRate;
    private double gbpRate;

    public ExchangeRates() {
        JSONObject dataUsd = new JSONObject(getContentFromAPI("https://www.alphavantage.co/query?function=FX_INTRADAY&from_symbol=USD&to_symbol=RUB&interval=60min&apikey=" + TOKEN));
        String keyUsd = dataUsd.getJSONObject("Meta Data").getString("4. Last Refreshed");
        this.usdRate = dataUsd.getJSONObject("Time Series FX (60min)").getJSONObject(keyUsd).getDouble("1. open");

        JSONObject dataEur = new JSONObject(getContentFromAPI("https://www.alphavantage.co/query?function=FX_INTRADAY&from_symbol=EUR&to_symbol=RUB&interval=60min&apikey=" + TOKEN));
        this.eurRate = dataEur.getJSONObject("Time Series FX (60min)").getJSONObject(keyUsd).getDouble("1. open");

        JSONObject dataCad = new JSONObject(getContentFromAPI("https://www.alphavantage.co/query?function=FX_INTRADAY&from_symbol=CAD&to_symbol=RUB&interval=60min&apikey=" + TOKEN));
        this.cadRate = dataCad.getJSONObject("Time Series FX (60min)").getJSONObject(keyUsd).getDouble("1. open");

        JSONObject dataGbp = new JSONObject(getContentFromAPI("https://www.alphavantage.co/query?function=FX_INTRADAY&from_symbol=GBP&to_symbol=RUB&interval=60min&apikey=" + TOKEN));
        this.gbpRate = dataGbp.getJSONObject("Time Series FX (60min)").getJSONObject(keyUsd).getDouble("1. open");
    }

    public static String getContentFromAPI(String urlAddress){
        StringBuilder content = new StringBuilder();
        try{
            URL url = new URL(urlAddress);
            URLConnection urlConn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String line;
            while((line = reader.readLine()) != null){
                content.append(line + "\n");
            }
            reader.close();
        }catch (Exception ex){
            System.out.println("Ошибка");
        }
        return content.toString();
    }

    public double getUsdRate() {
        return usdRate;
    }

    public double getEurRate() {
        return eurRate;
    }

    public double getCadRate() {
        return cadRate;
    }

    public double getGbpRate() {
        return gbpRate;
    }

    @Override
    public String toString() {
        return "ExchangeRates: {" +
                "usdRate=" + usdRate +
                ", eurRate=" + eurRate +
                ", cadRate=" + cadRate +
                ", gbpRate=" + gbpRate +
                '}';
    }

//    public static void main(String[] args) {
//        long startTime = System.currentTimeMillis();
//        ExchangeRates currencies = new ExchangeRates();
//        System.out.println("start time: " + startTime);
//        System.out.println(currencies.toString());
//        System.out.println("end time: " + ((System.currentTimeMillis() - startTime)));
//    }
}
