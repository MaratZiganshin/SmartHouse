package Model;

import java.util.List;

public class Room {
    private String name;
    private String type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;
    private Device[] devices;
    public String getName() {
        return name;
    }

    public Room(String name, String type, long id, Device[] devices) {
        this.name = name;
        this.id = id;
        this.devices = devices;
        this.type = type;
    }

    public Device[] getDevices() {
        return devices;
    }

    public void setDevices(Device[] devices) {
        this.devices = devices;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
