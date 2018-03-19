package addup.fpcompany.com.addsup;

/**
 * Created by song02 on 2017-12-09.
 */

public class listItem {

    String idx;
    String username;
    String title;
    String contents;
    String image1;
    String image2;
    String image3;
    String created;
    String listname;

    public listItem(String idx, String username, String title, String contents, String image1,
                    String image2, String image3, String created, String listname) {
        this.idx = idx;
        this.username = username;
        this.title = title;
        this.contents = contents;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
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

    public String getImage1() {
        return image1;
    }

    public String getImage2() {
        return image2;
    }

    public String getImage3() {
        return image3;
    }

}
