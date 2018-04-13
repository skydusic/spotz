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
    String text1;
    String text2;
    String text3;
    String text4;
    String text5;
    String hit;
    String spindata1;
    String spindata2;

    public listItem(String idx, String username, String contents, String image, String created,
                    String listname, String text1, String text2, String text3, String text4,
                    String text5, String hit, String spindata1, String spindata2) {
        this.idx = idx;
        this.username = username;
        this.contents = contents;
        this.image = image;
        this.created = created;
        this.listname = listname;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.text4 = text4;
        this.text5 = text5;
        this.hit = hit;
        this.spindata1 = spindata1;
        this.spindata2 = spindata2;
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

    public String getText1() {
        return text1;
    }

    public String getText2() {
        return text2;
    }

    public String getText3() {
        return text3;
    }

    public String getText4() {
        return text4;
    }

    public String getText5() {
        return text5;
    }

    public String getSpindata1(){
        return spindata1;
    }
    public String getSpindata2(){
        return spindata2;
    }
}