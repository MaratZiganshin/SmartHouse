package Model;

import java.util.List;

public class Room {
    private String Name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;
    private Device[] devices;
    public String getName() {
        return Name;
    }

    public Room(String name, long id, Device[] devices) {
        Name = name;
        this.id = id;
        this.devices = devices;
    }

    public Device[] getDevices() {
        return devices;
    }

    public void setDevices(Device[] devices) {
        this.devices = devices;
    }
}
