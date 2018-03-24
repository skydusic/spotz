package addup.fpcompany.com.addsup.java;

/**
 * Created by song02 on 2017-12-09.
 */

public class listItem {

    String idx;
    String username;
    String title;
    String contents;
    String image;
    String created;
    String listname;

    public listItem(String idx, String username, String title, String contents, String image, String created, String listname) {
        this.idx = idx;
        this.username = username;
        this.title = title;
        this.contents = contents;
        this.image = image;
        this.created = created;
        this.listname = listname;
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

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public String getImage() {
        return image;
    }
}
