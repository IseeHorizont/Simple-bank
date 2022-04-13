package ru.geekbrains.bank.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class SandPHelper {

    private static final String TOKEN = "134GFKK7OHNVJI96";

    private double amdPrice;
    private double nvidiaPrice;
    private double pfizerPrice;
    private double teslaPrice;
    private double oraclePrice;

    public SandPHelper() {

        JSONObject dataAMD = new JSONObject(getContentFromAPI("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=AMD&interval=60min&apikey=" + TOKEN));
        String keyStock = dataAMD.getJSONObject("Meta Data").get("3. Last Refreshed").toString();
        this.amdPrice = dataAMD.getJSONObject("Time Series (Daily)").getJSONObject(keyStock).getDouble("1. open");

        JSONObject dataNVDA = new JSONObject(getContentFromAPI("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=NVDA&interval=60min&apikey=" + TOKEN));
        this.nvidiaPrice = dataNVDA.getJSONObject("Time Series (Daily)").getJSONObject(keyStock).getDouble("1. open");

        JSONObject dataPFE = new JSONObject(getContentFromAPI("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=PFE&interval=60min&apikey=" + TOKEN));
        this.pfizerPrice = dataPFE.getJSONObject("Time Series (Daily)").getJSONObject(keyStock).getDouble("1. open");

        JSONObject dataTSLA = new JSONObject(getContentFromAPI("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=TSLA&interval=60min&apikey=" + TOKEN));
        this.teslaPrice = dataTSLA.getJSONObject("Time Series (Daily)").getJSONObject(keyStock).getDouble("1. open");

        JSONObject dataORCL = new JSONObject(getContentFromAPI("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=ORCL&interval=60min&apikey=" + TOKEN));
        this.oraclePrice = dataORCL.getJSONObject("Time Series (Daily)").getJSONObject(keyStock).getDouble("1. open");
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

    public double getAmdPrice() {
        return amdPrice;
    }

    public double getNvidiaPrice() {
        return nvidiaPrice;
    }

    public double getPfizerPrice() {
        return pfizerPrice;
    }

    public double getTeslaPrice() {
        return teslaPrice;
    }

    public double getOraclePrice() {
        return oraclePrice;
    }

    @Override
    public String toString() {
        return "SandPHelper{" +
                "amdPrice=" + amdPrice +
                ", nvidiaPrice=" + nvidiaPrice +
                ", pfizerPrice=" + pfizerPrice +
                ", teslaPrice=" + teslaPrice +
                ", oraclePrice=" + oraclePrice +
                '}';
    }

//    public static void main(String[] args) {
//        long startTime = System.currentTimeMillis();
//        SandPHelper sAndP = new SandPHelper();
//        System.out.println(sAndP.toString());
//        System.out.println("end time: " + ((System.currentTimeMillis() - startTime)));
//    }
}
