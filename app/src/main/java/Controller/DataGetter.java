package Controller;

import com.marat.smarthouse.Rooms;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Model.Device;
import Model.Room;
import Model.Token;
import Model.SessionData;

public class DataGetter {
    public static void getToken(String login, String password) throws IOException {
        String address = "http://185.188.182.194:8080/user/auth?login=" + login + "&password=" + password;
        try {
            HttpURLConnection connection = (HttpURLConnection)(new URL(address).openConnection());
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();

            if (code == 403)
                throw new IllegalArgumentException("Неверный логин и пароль");

            if (code == 202) {
                BufferedReader response = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String token = response.readLine();
                JSONObject jsonObject = new JSONObject(token);
                Token result = new Token(jsonObject.getString("refreshToken"), null);
                SessionData.setMainToken(result);
                SessionData.setId(jsonObject.getString("id"));
            }
            else throw new IOException("Ошибка подключения");
        } catch (Exception e) {
            throw new IOException("Ошибка подключения");
        }
    }

    public static void registrateUser(String login, String password, String email) throws IOException{
        String address = "http://185.188.182.194:8080/user/register?login=" + login + "&password=" + password + "&email=" + email;
        try {
            HttpURLConnection connection = (HttpURLConnection)(new URL(address).openConnection());
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();

            if (code == 403)
                throw new IllegalArgumentException("Пользователь уже существует");
            else if (code != 202)
                throw new IOException("Ошибка подключения");
        }
        catch (Exception e){
            throw new IOException("Ошибка подключения");
        }
    }

    private static void refresh(long id, String refreshToken) throws Exception{
        String address = "http://185.188.182.194:8080/token/refresh?id=" + id + "&refreshToken=" + refreshToken;
        try {
            HttpURLConnection connection = (HttpURLConnection)(new URL(address).openConnection());
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();

            if (code == 202) {
                BufferedReader response = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String token = response.readLine();
                JSONObject jsonObject = new JSONObject(token);
                Token result = new Token(jsonObject.getString("sessionToken"), null);
                SessionData.setSessionToken(result);
            }
            else
                throw new IOException();
        }
        catch (Exception e){
            throw new IOException("Ошибка подключения");
        }
    }

    private static boolean isValid(long id, String sessionToken) throws Exception{
        String address = "http://185.188.182.194:8080/token/refresh?id=" + id + "&sessionToken=" + sessionToken;
        try {
            HttpURLConnection connection = (HttpURLConnection)(new URL(address).openConnection());
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();

            if (code == 202) {
                BufferedReader response = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String token = response.readLine();
                JSONObject jsonObject = new JSONObject(token);
                Token result = new Token(jsonObject.getString("sessionToken"), null);
                SessionData.setSessionToken(result);
            }
            else
                throw new IOException();
            return true;
        }
        catch (Exception e){
            throw new IOException("Ошибка подключения");
        }
    }

    @Deprecated
    public static void fakeGetToken(String login, String password) throws IOException{
        if (login.equals("marat@marat") && password.equals("aaaaaaaa"))
            SessionData.setMainToken(new Token("asfhasfhahf", null));
        else throw new IOException();
    }

    @Deprecated
    public static boolean hasHome() throws IOException{
        return false;
    }

    public static List<Room> getRooms(long id, String sessionToken) throws IOException{
        String address = "http://185.188.182.194:8080/device/get/all/?id=" + id + "&sessionToken=" + sessionToken;
        try {
            HttpURLConnection connection = (HttpURLConnection)(new URL(address).openConnection());
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();

            if (code == 202) {
                BufferedReader response = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String string = response.readLine();
                JSONObject jsonObject = new JSONObject(string);
            }
            else
                throw new IOException();
        }
        catch (Exception e){
            throw new IOException("Ошибка подключения");
        }
        return null;
    }
    @Deprecated
    public static List<Room> fakeGetRooms() throws IOException{
        ArrayList<Room> list = new ArrayList<>();
        list.add(new Room("Wardrobe", 1, new Device[]{new Device(1, "Temperature"), new Device(2, "Light")}));
        list.add(new Room("Hall", 2, null));
        list.add(new Room("Sleeping1", 3, null));
        list.add(new Room("Toilet",4, null));
        list.add(new Room("Bathroom",5, null));
        list.add(new Room("Kitchen" ,6,null));
        list.add(new Room("Garage",7, null));
        list.add(new Room("Sleeping2",8, null));
        SessionData.setRooms(list);
        return list;
    }

    public static List<Device> getDevices(long id) throws Exception{
        ArrayList<Room> rooms = (ArrayList<Room>)SessionData.getRooms();
        Room room = null;
        for (Room r : rooms)
        {
            if (r.getId() == id) {
                room = r;
                break;
            }
        }
        ArrayList<Device> list = new ArrayList<>();
        list.addAll(Arrays.asList(room.getDevices()));
        return list;
    }
}
