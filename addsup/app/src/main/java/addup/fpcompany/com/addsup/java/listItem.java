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
    String sports;
    String location;
    String phone;
    String etc;
    String hit;

    public listItem(String idx, String username, String contents,
                    String image, String created, String listname, String corperation,
                    String sports, String location, String phone, String etc, String hit) {
        this.idx = idx;
        this.username = username;
        this.contents = contents;
        this.image = image;
        this.created = created;
        this.listname = listname;
        this.corperation = corperation;
        this.sports = sports;
        this.location = location;
        this.phone = phone;
        this.etc = etc;
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

    public String getLocation() {
        return location;
    }

    public String getCorperation() {
        return corperation;
    }

    public String getSports() {
        return sports;
    }

    public String getEtc() {
        return etc;
    }

    public String getPhone() {
        return phone;
    }
}
