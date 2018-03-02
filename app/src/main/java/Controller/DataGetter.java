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
                throw new IllegalArgumentException();

            if (code == 202) {
                BufferedReader response = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String token = response.readLine();
                JSONObject jsonObject = new JSONObject(token);
                Token result = new Token(jsonObject.getString("refreshToken"), null);
                SessionData.setMainToken(result);
                SessionData.setId(jsonObject.getString("id"));
            }
            else throw new IOException("Ошибка подключения");
        }
        catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Неверный логин и пароль");
        }
        catch (Exception e) {
            throw new IOException("Ошибка подключения");
        }
    }

    public static void registrateUser(String email, String login, String password, String homeId, String homePass) throws IOException{
        String address = "http://185.188.182.194:8080/user/register?login=" + login + "&password=" + password + "&email=" + email+ "&homeId=" + homeId+ "&homePass=" + homePass;
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

    public static void refresh(long id, String refreshToken) throws Exception{
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
                Token result = new Token(jsonObject.getString("token"), null);
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
        return true;
    }

    public static List<Room> getRooms(long id, String sessionToken, boolean canBeRefreshed) throws IOException{
        String address = "http://185.188.182.194:8080/device/get/all/?id=" + id + "&sessionToken=" + sessionToken;
        try {
            HttpURLConnection connection = (HttpURLConnection)(new URL(address).openConnection());
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();

            if (code == 202) {
                BufferedReader response = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String json = response.readLine();
                JSONArray array = new JSONArray(json);
                ArrayList<Room> list = new ArrayList<>();
                for (int i = 0; i < array.length(); i++){
                    JSONObject object = array.getJSONObject(i);
                    String name = object.getString("name");
                    String type = object.getString("type");
                    long Id = object.getLong("id");
                    JSONArray devicesJson= object.getJSONArray("devices");
                    Device[] devices = new Device[devicesJson.length()];
                    for (int j = 0; j < devicesJson.length(); j++){
                        Device device = new Device(devicesJson.getJSONObject(j).getLong("id"), ""/*devicesJson.getJSONObject(j).getString("name")*/,devicesJson.getJSONObject(j).getString("type"), devicesJson.getJSONObject(j).getString("state"), Long.parseLong(devicesJson.getJSONObject(j).getString("roomId")));
                        devices[j] = device;
                    }
                    Room room = new Room(name, type, Id, devices);
                    list.add(room);
                }
                SessionData.setRooms(list);
                return list;
            }
            else if (code == 403 && canBeRefreshed){
                refresh(id, SessionData.getMainToken().getToken());
                return getRooms(id, SessionData.getSessionToken().getToken(), false);
            }
            else
                throw new IOException();
        }
        catch (Exception e){
            throw new IOException("Ошибка подключения");
        }
    }

    public static void setCommand(long id, String sessionToken, long deviceId, String command, boolean canBeRefreshed) throws Exception{
        String address = "http://185.188.182.194:8080/device/set/?id=" + id + "&sessionToken=" + sessionToken + "&deviceId=" + deviceId +"&command=" + command;
        try {
            HttpURLConnection connection = (HttpURLConnection)(new URL(address).openConnection());
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();

            if (code == 202) {

            }
            else if (code == 403 && canBeRefreshed){
                refresh(id, SessionData.getMainToken().getToken());
                setCommand(id, SessionData.getSessionToken().getToken(), deviceId, command, false);
            }
            else
                throw new IOException();
        }
        catch (Exception e){
            throw new IOException("Ошибка подключения");
        }
    }

    public static boolean addRoom(long id, String sessionToken, String name, String type, boolean canBeRefreshed) throws Exception{
        String address = "http://185.188.182.194:8080/rooms/add/?id=" + id + "&sessionToken=" + sessionToken + "&name=" + name + "&type=" + type;
        try {
            HttpURLConnection connection = (HttpURLConnection)(new URL(address).openConnection());
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();

            if (code == 202) {
                return true;
            }
            else if (code == 403 && canBeRefreshed){
                refresh(id, SessionData.getMainToken().getToken());
                return addRoom(id, sessionToken, name, type, false);
            }
            else
                throw new IOException();
        }
        catch (Exception e){
            throw new IOException("Ошибка подключения");
        }
    }

    public static void deleteRoom(long id, String sessionToken, long roomId, boolean canBeRefreshed) throws Exception{
        String address = "http://185.188.182.194:8080/rooms/delete/?id=" + id + "&sessionToken=" + sessionToken + "&room=" + roomId;
        try {
            HttpURLConnection connection = (HttpURLConnection)(new URL(address).openConnection());
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();

            if (code == 202) {
                return;
            }
            else if (code == 403 && canBeRefreshed){
                refresh(id, SessionData.getMainToken().getToken());
                deleteRoom(id, sessionToken, roomId, false);
            }
            else
                throw new IOException();
        }
        catch (Exception e){
            throw new IOException("Ошибка подключения");
        }
    }

    public static void renameRoom(long id, String sessionToken, long roomId, String newName, String type,  boolean canBeRefreshed) throws Exception{
        String address = "http://185.188.182.194:8080/rooms/rename/?id=" + id + "&sessionToken=" + sessionToken + "&room=" + roomId + "&name=" + newName + "&type=" + type;
        try {
            HttpURLConnection connection = (HttpURLConnection)(new URL(address).openConnection());
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();

            if (code == 202) {
                return;
            }
            else if (code == 403 && canBeRefreshed){
                refresh(id, SessionData.getMainToken().getToken());
                renameRoom(id, sessionToken, roomId, newName, type, false);
            }
            else
                throw new IOException();
        }
        catch (Exception e){
            throw new IOException("Ошибка подключения");
        }
    }

    public static void setRoom(long id, String sessionToken, long deviceId, long room, boolean canBeRefreshed) throws Exception{
        String address = "http://185.188.182.194:8080/device/setRoom/?id=" + id + "&sessionToken=" + sessionToken + "&deviceId=" + deviceId + "&room=" + room;
        try {
            HttpURLConnection connection = (HttpURLConnection)(new URL(address).openConnection());
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();

            if (code == 202) {
                return;
            }
            else if (code == 403 && canBeRefreshed){
                refresh(id, SessionData.getMainToken().getToken());
                setRoom(id, sessionToken, deviceId, room,false);
            }
            else
                throw new IOException();
        }
        catch (Exception e){
            throw new IOException("Ошибка подключения");
        }
    }

    public static void renameDevice(long id, String sessionToken, long deviceId, long room, String newName, boolean canBeRefreshed) throws Exception{
        String address = "http://185.188.182.194:8080/device/rename/?id=" + id + "&sessionToken=" + sessionToken + "&deviceId=" + deviceId + "&room=" + room + "&name=" + newName;
        try {
            HttpURLConnection connection = (HttpURLConnection)(new URL(address).openConnection());
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();

            if (code == 202) {
                return;
            }
            else if (code == 403 && canBeRefreshed){
                refresh(id, SessionData.getMainToken().getToken());
                renameDevice(id, sessionToken, deviceId, room, newName,false);
            }
            else
                throw new IOException();
        }
        catch (Exception e){
            throw new IOException("Ошибка подключения");
        }
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
