package addup.fpcompany.com.addsup.java;


public class noticeItem {

    String idx;
    String title;
    String contents;
    String image;
    String created;

    public noticeItem(String idx, String title, String contents, String image, String created) {
        this.idx = idx;
        this.title = title;
        this.contents = contents;
        this.image = image;
        this.created = created;
    }

    public String getIdx() {
        return idx;
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

    public String getCreated() {
        return created;
    }
}
