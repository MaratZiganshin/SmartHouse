package Controller;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import JSON.Token;
import Model.SessionData;

public class DataGetter {
    public static void getToken(String login, String password) throws IOException {
        String address = "http://185.188.182.194:8080/auth?login=" + login + "&password=" + password;
        try {
            URLConnection connection = new URL(address).openConnection();
            BufferedReader response = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String token = response.readLine();
            JSONObject jsonObject = new JSONObject();
            Token result = new Token(jsonObject.getString("token"), jsonObject.getString("expTime"));
            SessionData.setMainToken(result);
        } catch (Exception e) {
            throw new IOException("Invalid password or login");
        }
    }

    @Deprecated
    public static void fakeGetToken(String login, String password) throws IOException{
        SessionData.setMainToken(new Token("asfhasfhahf", null));
    }
}
