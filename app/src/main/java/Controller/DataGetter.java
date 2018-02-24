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
                        Device device = new Device(devicesJson.getJSONObject(j).getLong("id"), ""/*devicesJson.getJSONObject(j).getString("name")*/,devicesJson.getJSONObject(j).getString("type"), devicesJson.getJSONObject(j).getString("state"));
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
    /*@Deprecated
    public static List<Room> fakeGetRooms() throws Exception{
        String json = "[{\"devices\":[{\"id\":2,\"type\":\"Temperature\",\"roomId\":-1,\"state\":\"\"},{\"id\":3,\"type\":\"Light\",\"roomId\":-1,\"state\":\"\"},{\"id\":4,\"type\":\"Outlet\",\"roomId\":-1,\"state\":\"\"},{\"id\":5,\"type\":\"qq\",\"roomId\":-1,\"state\":\"\"},{\"id\":6,\"type\":\"qq\",\"roomId\":-1,\"state\":\"\"},{\"id\":7,\"type\":\"qq\",\"roomId\":-1,\"state\":\"\"},{\"id\":8,\"type\":\"NewTypeDevice\",\"roomId\":-1,\"state\":\"\"},{\"id\":9,\"type\":\"NewTypeDevice\",\"roomId\":-1,\"state\":\"\"},{\"id\":10,\"type\":\"NewTypeDevice\",\"roomId\":-1,\"state\":\"\"},{\"id\":11,\"type\":\"NewTypeDevice\",\"roomId\":-1,\"state\":\"\"},{\"id\":12,\"type\":\"NewTypeDevice\",\"roomId\":-1,\"state\":\"\"},{\"id\":15,\"type\":\"lol\",\"roomId\":-1,\"state\":\"\"},{\"id\":20,\"type\":\"lol\",\"roomId\":-1,\"state\":\"\"},{\"id\":21,\"type\":\"lol\",\"roomId\":-1,\"state\":\"\"}],\"name\":\"Unknown\",\"id\":-1},{\"devices\":[{\"id\":1,\"type\":\"lamp\",\"roomId\":1,\"state\":\"\"},{\"id\":13,\"type\":\"NewTypeDevice\",\"roomId\":1,\"state\":\"\"},{\"id\":16,\"type\":\"lol\",\"roomId\":1,\"state\":\"\"}],\"name\":\"My first Test Room\",\"id\":1},{\"devices\":[{\"id\":14,\"type\":\"NewTypeDevice\",\"roomId\":2,\"state\":\"\"},{\"id\":17,\"type\":\"lol\",\"roomId\":2,\"state\":\"\"}],\"name\":\"My second Test Room\",\"id\":2},{\"devices\":[],\"name\":\"System.Windows.Forms.TextBox, Text: WOW\",\"id\":3},{\"devices\":[],\"name\":\"System.Windows.Forms.TextBox, Text: WOW\",\"id\":4},{\"devices\":[],\"name\":\"WOOOOOOOOOOW\",\"id\":5},{\"devices\":[],\"name\":\"WOOOOOOOOOOW\",\"id\":6},{\"devices\":[],\"name\":\"WOOOOOOOOOOW\",\"id\":7},{\"devices\":[],\"name\":\"nOW\",\"id\":8},{\"devices\":[],\"name\":\"nOW\",\"id\":9},{\"devices\":[],\"name\":\"nOW\",\"id\":10},{\"devices\":[],\"name\":\"nOWh\",\"id\":11},{\"devices\":[],\"name\":\"nOWh\",\"id\":12},{\"devices\":[],\"name\":\"nOWh\",\"id\":13}]";
        JSONArray array = new JSONArray(json);
        ArrayList<Room> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++){
            JSONObject object = array.getJSONObject(i);
            String name = object.getString("name");
            long id = object.getLong("id");
            JSONArray devicesJson= object.getJSONArray("devices");
            Device[] devices = new Device[devicesJson.length()];
            for (int j = 0; j < devicesJson.length(); j++){
                Device device = new Device(devicesJson.getJSONObject(j).getLong("id"), devicesJson.getJSONObject(j).getString("type"), devicesJson.getJSONObject(j).getString("state"));
                devices[j] = device;
            }
            Room room = new Room(name, id, devices);
            list.add(room);
        }
        /*list.add(new Room("Wardrobe", 1, new Device[]{new Device(1, "Temperature"), new Device(2, "Light")}));
        list.add(new Room("Hall", 2, null));
        list.add(new Room("Sleeping1", 3, null));
        list.add(new Room("Toilet",4, null));
        list.add(new Room("Bathroom",5, null));
        list.add(new Room("Kitchen" ,6,null));
        list.add(new Room("Garage",7, null));
        list.add(new Room("Sleeping2",8, null));
        SessionData.setRooms(list);
        return list;
    }*/

    public static Device setCommand(long id, String sessionToken, long deviceId, String command, boolean canBeRefreshed) throws Exception{
        String address = "http://185.188.182.194:8080/device/set/?id=" + id + "&sessionToken=" + sessionToken + "&deviceId=" + deviceId +"&command=" + command;
        try {
            HttpURLConnection connection = (HttpURLConnection)(new URL(address).openConnection());
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();

            if (code == 202) {
                BufferedReader response = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String json = response.readLine();
                JSONObject jsonObject = new JSONObject(json);
                return new Device(jsonObject.getLong("id"),jsonObject.getString("name"),jsonObject.getString("type"), jsonObject.getString("state"));
            }
            else if (code == 403 && canBeRefreshed){
                refresh(id, SessionData.getMainToken().getToken());
                return setCommand(id, SessionData.getSessionToken().getToken(), deviceId, command, false);
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

    public static void renameRoom(long id, String sessionToken, long roomId, String newName, boolean canBeRefreshed) throws Exception{
        String address = "http://185.188.182.194:8080/rooms/rename/?id=" + id + "&sessionToken=" + sessionToken + "&room=" + roomId + "&name=" + newName;
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
                renameRoom(id, sessionToken, roomId, newName,false);
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
