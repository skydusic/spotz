package addup.fpcompany.com.addsup.java;

/**
 * Created by song02 on 2018-03-26.
 */

public class favoriteItem {

    String idx;
    String listname;
    String postidx;

    public favoriteItem(String idx, String listname, String postidx) {
        this.idx = idx;
        this.listname = listname;
        this.postidx = postidx;
    }

    public String getIdx(){
        return idx;
    }

    public String getPostidx() {
        return postidx;
    }

    public String getListname() {
        return listname;
    }
}
