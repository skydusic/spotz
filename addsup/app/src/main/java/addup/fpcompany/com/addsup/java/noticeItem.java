package addup.fpcompany.com.addsup.java;


public class noticeItem {

    private String idx, title, contents, image, created, hit;

    public noticeItem(String idx, String title, String contents, String image, String created, String hit) {
        this.idx = idx;
        this.title = title;
        this.contents = contents;
        this.image = image;
        this.created = created;
        this.hit = hit;
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

    public String getHit() {
        return hit;
    }
}
