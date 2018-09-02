package addup.fpcompany.com.addsup.java;

/**
 * Created by song02 on 2017-12-09.
 */

public class listItem {

    private String idx;
    private String title;
    private String username;
    private String contents;
    private String image;
    private String created;
    private String listname;
    private String hit;
    private String spindata;

    public listItem(String idx, String title, String username, String contents, String image, String created,
                    String listname, String hit, String spindata) {
        this.idx = idx;
        this.title = title;
        this.username = username;
        this.contents = contents;
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

    public String getContents() {
        return contents;
    }

    public String getImage() {
        return image;
    }

    public String getSpindata(){
        return spindata;
    }
}