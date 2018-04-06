package addup.fpcompany.com.addsup.java;

/**
 * Created by song02 on 2017-12-09.
 */

public class listItem {

    String idx;
    String username;
    String contents;
    String image;
    String created;
    String listname;
    String corperation;
    String timetable;
    String location;
    String traffic;
    String fee;
    String phone;
    String hit;

    public listItem(String idx, String username, String contents,
                    String image, String created, String listname, String corperation,
                    String timetable, String location, String traffic, String fee,
                    String phone, String hit) {
        this.idx = idx;
        this.username = username;
        this.contents = contents;
        this.image = image;
        this.created = created;
        this.listname = listname;
        this.corperation = corperation;
        this.timetable = timetable;
        this.location = location;
        this.traffic = traffic;
        this.fee = fee;
        this.phone = phone;
        this.hit = hit;
    }

    public String getHit() {
        return hit;
    }

    public String getListname() {
        return listname;
    }

    public String getCreated() {
        return created;
    }

    public String getIdx() {
        return idx;
    }

    public String getUsername() {
        return username;
    }

    public String getContents() {
        return contents;
    }

    public String getImage() {
        return image;
    }

    public String getcorperation() {
        return corperation;
    }

    public String getTimetable() {
        return timetable;
    }

    public String getLocation() {
        return location;
    }

    public String getTraffic() {
        return traffic;
    }

    public String getFee() {
        return fee;
    }

    public String getPhone() {
        return phone;
    }
}
