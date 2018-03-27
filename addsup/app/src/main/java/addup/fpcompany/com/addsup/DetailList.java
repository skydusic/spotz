package addup.fpcompany.com.addsup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import addup.fpcompany.com.addsup.adapter.PagerAdapter;
import addup.fpcompany.com.addsup.frag.DetailFrag;
import addup.fpcompany.com.addsup.java.favoriteItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DetailList extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    Intent intent;

    TextView detailNameTv;
    TextView midDetailTv1;
    TextView midDetailTv2;
    TextView midDetailTv3;
    TextView midDetailTv4;
    TextView contentsTv;
    ViewPager viewPager;
    ImageView favorite;
    ArrayList<String> arr = new ArrayList<>();
    ArrayList<Fragment> fragArr = new ArrayList<>();

    String idx = "";
    String title = "";
    String contents = "";
    String created = "";
    String image = "";
    String listname = "";

    String url = MainActivity.serverUrl + "userImageFolder/";
    Boolean favoriteFLAG = false;
    int favoritePos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list);

        detailNameTv = findViewById(R.id.detailNameTv);
        midDetailTv1 = findViewById(R.id.midDetailTv1);
        midDetailTv2 = findViewById(R.id.midDetailTv2);
        midDetailTv3 = findViewById(R.id.midDetailTv3);
        midDetailTv4 = findViewById(R.id.midDetailTv4);
        contentsTv = findViewById(R.id.contents);
        viewPager = findViewById(R.id.viewPager);
        favorite = findViewById(R.id.favorite);

        // 인텐트로 정보 가져옴
        intent = getIntent();
        idx = intent.getStringExtra("idx");
        title = intent.getStringExtra("title");
        contents = intent.getStringExtra("contents");
        created = intent.getStringExtra("created");
        listname = intent.getStringExtra("listname");
        detailNameTv.setText(idx + " : " + title);
        contentsTv.setText(contents);
        image = intent.getStringExtra("image");

        // 이미지 세팅
        setRecyclerView();
        // 이미지 페이지 넘기는 핸들러
        handler.sendEmptyMessageDelayed(0, 2000);
        hitUpdate hitUpdate = new hitUpdate();
        hitUpdate.requestPost();

        //즐겨찾기 플래그
        favoriteSet();

        favorite.setOnClickListener(this);
    }

    private void favImageSet() {
        for (int i = 0; i < MainActivity.favoriteArr.size(); i++) {
            favoriteItem favoriteTemp = MainActivity.favoriteArr.get(i);
            Log.d("heu", "플래그 : " + favoriteFLAG);
            if (favoriteTemp.getListname().equals(listname) && favoriteTemp.getPostidx().equals(idx)) {

                favoritePos = Integer.parseInt(favoriteTemp.getIdx());

                favorite.setImageResource(R.drawable.yellowstar);
                favoriteFLAG = true;
                break;
            } else {
                favorite.setImageResource(R.drawable.blackstar);
                favoriteFLAG = false;
            }
        }

        Log.d("heu", "포스 : " + favoritePos);

    }

    private void favoriteSet() {
        MainActivity.getFavorite fav = new MainActivity.getFavorite();
        MainActivity.favoriteArr.clear();
        fav.requestPost(MainActivity.mUsername);
        handler2.sendEmptyMessageDelayed(1000, 100);
    }

    private void setRecyclerView() {
        if (!image.equals("")) {
            String[] temp = image.split(",");
            for (int i = 0; i < temp.length; i++) {
                arr.add(url + temp[i]);
            }
        }

        for (int i = 0; i < arr.size(); i++) {
            fragArr.add(new DetailFrag(arr.get(i)));
        }
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), fragArr);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(DetailList.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.bottomHome):
                Intent intent = new Intent(DetailList.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case (R.id.bottomMember):
                if (MainActivity.mUser == null) {
                    Intent intent1 = new Intent(DetailList.this, SignInActivity.class);
                    finish();
                    startActivityForResult(intent1, 10);
                } else {
                    Intent intent2 = new Intent(DetailList.this, myPageActivity.class);
                    finish();
                    startActivityForResult(intent2, 1000);
                }
                break;
            case (R.id.favorite):

                if(favoriteFLAG) {
                    Log.d("heu", "딜리트");
                    favoriteDelete delete = new favoriteDelete();
                    delete.requestPost(String.valueOf(favoritePos));

                } else {
                    Log.d("heu", "인서트 ");
                    favoriteInsert insert = new favoriteInsert();
                    insert.requestPost(listname, idx);
                }
                favoriteSet();

                break;
        }
    }

    int pagerPos = 0;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        pagerPos = position;
        handler.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            handler.sendEmptyMessageDelayed(0, 2000);

        } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
            handler.removeMessages(0);

        } else if (state == ViewPager.SCROLL_STATE_SETTLING) {

        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler.removeMessages(0);
            pagerPos++;

            if (pagerPos >= arr.size()) {
                pagerPos = 0;
                viewPager.setCurrentItem(pagerPos, false);
            } else {
                viewPager.setCurrentItem(pagerPos);
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (MainActivity.favoriteArr != null) {
                favImageSet();
                removeMessages(1000);
            } else {
                handler2.sendEmptyMessageDelayed(1000, 200);
            }

        }
    };

    class hitUpdate {
        OkHttpClient client = new OkHttpClient();

        public void requestPost() {

            //Request Body에 서버에 보낼 데이터 작성
            RequestBody requestBody = new FormBody.Builder().
                    add("listname", listname).
                    add("idx", idx).
                    build();
            String url = "http://spotz.co.kr/var/www/html/hitupdate.php";

            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("heu", "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d("heu", "hitupdate res : " + response.body().string());
                }
            });
        }
    }

    class favoriteInsert {
        OkHttpClient client = new OkHttpClient();

        public void requestPost(String listname, String postidx) {

            //Request Body에 서버에 보낼 데이터 작성
            RequestBody requestBody = new FormBody.Builder().
                    add("username", MainActivity.mUsername).
                    add("listname", listname).
                    add("postidx", postidx).
                    build();
            String url = "http://spotz.co.kr/var/www/html/favoriteinsert.php";

            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("heu", "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d("heu", "favorite res : " + response.body().string());
                    favoriteSet();
                }
            });
        }
    }

    class favoriteDelete {
        OkHttpClient client = new OkHttpClient();

        public void requestPost(String idx) {

            RequestBody requestBody = new FormBody.Builder().
                    add("idx", idx).
                    build();
            String url = "http://spotz.co.kr/var/www/html/favoritedelete.php";

            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("heu", "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d("heu", "hitupdate res : " + response.body().string());
                    favoriteSet();
                }
            });
        }
    }

    class historyInsert {
        OkHttpClient client = new OkHttpClient();

        public void requestPost(String listname, String postidx) {

            //Request Body에 서버에 보낼 데이터 작성
            RequestBody requestBody = new FormBody.Builder().
                    add("username", MainActivity.mUsername).
                    add("listname", listname).
                    add("postidx", postidx).
                    build();
            String url = "http://spotz.co.kr/var/www/html/historyinsert.php";

            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("heu", "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d("heu", "favorite res : " + response.body().string());
                }
            });
        }
    }

    class historyDelete {
        OkHttpClient client = new OkHttpClient();

        public void requestPost(String idx) {

            RequestBody requestBody = new FormBody.Builder().
                    add("idx", idx).
                    build();
            String url = "http://spotz.co.kr/var/www/html/historydelete.php";

            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("heu", "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d("heu", "hitupdate res : " + response.body().string());
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
        handler2.removeMessages(1000);
    }
}