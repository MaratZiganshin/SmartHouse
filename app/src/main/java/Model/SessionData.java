package Model;


import java.util.List;

public class SessionData {
    private static Token mainToken;
    private static String id;

    public static List<Room> getRooms() {
        return rooms;
    }

    public static void setRooms(List<Room> rooms) {
        SessionData.rooms = rooms;
    }

    private static List<Room> rooms;
    private static List<Device> devices;

    public static Token getSessionToken() {
        return sessionToken;
    }

    public static void setSessionToken(Token sessionToken) {
        SessionData.sessionToken = sessionToken;
    }

    public static String error;

    private static Token sessionToken = null;

    public static Token getMainToken() {
        return mainToken;
    }

    public static void setMainToken(Token _mainToken) {
        mainToken = _mainToken;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        SessionData.id = id;
    }

    public static List<Device> getDevices() {
        return devices;
    }

    private static Device currentDevice;
    private static Room currentRoom;

    public static void setDevices(List<Device> devices) {
        SessionData.devices = devices;
    }

    public static Device getCurrentDevice() {
        return currentDevice;
    }

    public static void setCurrentDevice(Device currentDevice) {
        SessionData.currentDevice = currentDevice;
    }

    public static Room getCurrentRoom() {
        return currentRoom;
    }

    public static void setCurrentRoom(Room currentRoom) {
        SessionData.currentRoom = currentRoom;
    }
}
