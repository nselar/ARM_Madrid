package com.example.arm_madrid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class APIMobilityClient {

    private BufferedReader buffReader;
    HttpURLConnection connection;
    String answer;

    public APIMobilityClient(){

    }

    public String getAnswer(){
        return answer;
    }

    public APIMobilityClient connect(){

        try {

            URL url = new URL("https://openapi.emtmadrid.es/v1/hello/");
            connection = (HttpURLConnection) url.openConnection();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    public APIMobilityClient sendRequest() {

        try {
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    public void disconnect(){

        connection.disconnect();

    }

    public APIMobilityClient readAnswer() {

        try {

            buffReader = new BufferedReader(new InputStreamReader((connection.getInputStream())));

            String output;
            while ((output = buffReader.readLine()) != null) {
                answer += output;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }
}
