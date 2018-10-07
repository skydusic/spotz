package addup.fpcompany.com.addsup.java;

public class commentItem {

    String idx;
    String listname;
    String username;
    String contents;
    String created;

    public commentItem(String idx, String listname, String username, String contents, String created) {
        this.idx = idx;
        this.listname = listname;
        this.username = username;
        this.contents = contents;
        this.created = created;
    }

    public String getIdx() {
        return idx;
    }

    public String getListname() {
        return listname;
    }

    public String getUsername() {
        return username;
    }

    public String getContents() {
        return contents;
    }

    public String getCreated() {
        return created;
    }

}
