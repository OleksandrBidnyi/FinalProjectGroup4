package finalProject;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Aleksandr on 26.04.2017.
 */
public class Hotel {
    private long id;
    private String name;
    private String city;
    private List<Room> rooms;

    public Hotel(long id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Room> getRooms() {
        return rooms = RoomDAO.getRoomDAO().getBase().stream()
                .filter(room -> room.getHotel().getId() == this.getId()).collect(Collectors.toList());
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hotel hotel = (Hotel) o;

        if (id != hotel.id) return false;
        if (name != null ? !name.equals(hotel.name) : hotel.name != null) return false;
        return city != null ? city.equals(hotel.city) : hotel.city == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "finalProject.Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
