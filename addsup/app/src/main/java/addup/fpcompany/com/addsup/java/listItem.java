package addup.fpcompany.com.addsup.java;

/**
 * Created by song02 on 2017-12-09.
 */

public class listItem {

    private String idx;
    private String title;
    private String username;
    private String email;
    private String image;
    private String created;
    private String listname;
    private String hit;
    private String spindata;

    public listItem(String idx, String title, String username, String email, String image, String created,
                    String listname, String hit, String spindata) {
        this.idx = idx;
        this.title = title;
        this.username = username;
        this.email = email;
        this.image = image;
        this.created = created;
        this.listname = listname;
        this.hit = hit;
        this.spindata = spindata;
    }

    public String getTitle() {
        return title;
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


    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public String getSpindata(){
        return spindata;
    }

    @Override
    public String toString() {
        return "listItem{" +
                "idx='" + idx + '\'' +
                ", title='" + title + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                ", created='" + created + '\'' +
                ", listname='" + listname + '\'' +
                ", hit='" + hit + '\'' +
                ", spindata='" + spindata + '\'' +
                '}';
    }
}