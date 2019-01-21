package addup.fpcompany.com.addsup.java;

public class commentItem {

    private String idx;
    private String listname;
    private String username;
    private String email;
    private String contents;
    private String created;

    public commentItem(String idx, String listname, String username, String email, String contents, String created) {
        this.idx = idx;
        this.listname = listname;
        this.username = username;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public String getContents() {
        return contents;
    }

    public String getCreated() {
        return created;
    }

}
