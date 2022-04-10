package ru.geekbrains.bank;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class DowJonesHelper {
    private static final String TOKEN = "O6GTTRBH5ZIGA0AB";

    private double applePrice;
    private double microsoftPrice;
    private double jpmorganPrice;
    private double cocaColaPrice;
    private double mcDonaldsPrice;

    public DowJonesHelper() {
        JSONObject dataAAPL = new JSONObject(getContentFromAPI("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=AAPL&interval=60min&apikey=" + TOKEN));
        String keyStock = dataAAPL.getJSONObject("Meta Data").get("3. Last Refreshed").toString();
        this.applePrice = dataAAPL.getJSONObject("Time Series (Daily)").getJSONObject(keyStock).getDouble("1. open");

        JSONObject dataMSFT = new JSONObject(getContentFromAPI("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=MSFT&interval=60min&apikey=" + TOKEN));
        this.microsoftPrice = dataMSFT.getJSONObject("Time Series (Daily)").getJSONObject(keyStock).getDouble("1. open");

        JSONObject dataJPMorgan = new JSONObject(getContentFromAPI("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=JPM&interval=60min&apikey=" + TOKEN));
        this.jpmorganPrice = dataJPMorgan.getJSONObject("Time Series (Daily)").getJSONObject(keyStock).getDouble("1. open");

        JSONObject dataKO = new JSONObject(getContentFromAPI("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=KO&interval=60min&apikey=" + TOKEN));
        this.cocaColaPrice = dataKO.getJSONObject("Time Series (Daily)").getJSONObject(keyStock).getDouble("1. open");

        JSONObject dataMCD = new JSONObject(getContentFromAPI("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=MCD&interval=60min&apikey=" + TOKEN));
        this.mcDonaldsPrice = dataMCD.getJSONObject("Time Series (Daily)").getJSONObject(keyStock).getDouble("1. open");
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

    public double getApplePrice() {
        return applePrice;
    }

    public double getMicrosoftPrice() {
        return microsoftPrice;
    }

    public double getJpmorganPrice() {
        return jpmorganPrice;
    }

    public double getCocaColaPrice() {
        return cocaColaPrice;
    }

    public double getMcDonaldsPrice() {
        return mcDonaldsPrice;
    }

    @Override
    public String toString() {
        return "DowJonesHelper: \n" +
                "applePrice = " + applePrice + "\n" +
                "microsoftPrice = " + microsoftPrice + "\n" +
                "jpmorganPrice = " + jpmorganPrice + "\n" +
                "cocaColaPrice = " + cocaColaPrice + "\n" +
                "mcDonaldsPrice = " + mcDonaldsPrice;
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        DowJonesHelper dowJ = new DowJonesHelper();
        System.out.println("start time: " + startTime);
        System.out.println(dowJ.toString());
        System.out.println("end time: " + ((System.currentTimeMillis() - startTime)));
    }
}
