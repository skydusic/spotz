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

    TextView contentsTv;
    ViewPager viewPager;
    ImageView favorite;
    ArrayList<String> arr = new ArrayList<>();
    ArrayList<Fragment> fragArr = new ArrayList<>();

    TextView text1menu;
    TextView text2menu;
    TextView text3menu;
    TextView text4menu;
    TextView text5menu;
    TextView text1Tv;
    TextView text2Tv;
    TextView text3Tv;
    TextView text4Tv;
    TextView text5Tv;

    String listname = "";
    String idx = "";
    String contents = "";
    String username = "";
    String created = "";
    String image = "";

    String text1 = "";
    String text2 = "";
    String text3 = "";
    String text4 = "";
    String text5 = "";

    String imageurl;

    Boolean favoriteFLAG = false;

    favoriteItem favoriteTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list);

        contentsTv = findViewById(R.id.contents);
        viewPager = findViewById(R.id.viewPager);
        favorite = findViewById(R.id.favorite);

        text1menu = findViewById(R.id.text1menu);
        text2menu = findViewById(R.id.text2menu);
        text3menu = findViewById(R.id.text3menu);
        text4menu = findViewById(R.id.text4menu);
        text5menu = findViewById(R.id.text5menu);
        text1Tv = findViewById(R.id.text1Tv);
        text2Tv = findViewById(R.id.text2Tv);
        text3Tv = findViewById(R.id.text3Tv);
        text4Tv = findViewById(R.id.text4Tv);
        text5Tv = findViewById(R.id.text5Tv);

        // 인텐트로 정보 가져옴
        intent = getIntent();
        listname = intent.getStringExtra("listname");
        idx = intent.getStringExtra("idx");
        contents = intent.getStringExtra("contents");
        username = intent.getStringExtra("username");
        created = intent.getStringExtra("created");
        text1 = intent.getStringExtra("text1");
        text2 = intent.getStringExtra("text2");
        text3 = intent.getStringExtra("text3");
        text4 = intent.getStringExtra("text4");
        text5 = intent.getStringExtra("text5");

        //메뉴 텍스트 삽입
        if (listname.equals("clubtable")) {
            text1menu.setText("업체명");
            text2menu.setText("종목");
            text3menu.setText("위치");
            text4menu.setText("연락처");
            text5menu.setText("세부 안내사항");
        } else if (listname.equals("freelancer")) {
            text1menu.setText("대표명");
            text2menu.setText("종목");
            text3menu.setText("레슨장소");
            text4menu.setText("연락처");
            text5menu.setText("세부 안내사항");
        } else if (listname.equals("competition")) {
            text1menu.setText("대회명");
            text2menu.setText("종목");
            text3menu.setText("위치");
            text4menu.setText("연락처");
            text5menu.setText("세부 안내사항");
        } else if (listname.equals("dongho")) {
            text1menu.setText("동호회명");
            text2menu.setText("종목");
            text3menu.setText("위치");
            text4menu.setText("시간");
            text5menu.setText("연락처");
        } else if (listname.equals("review")) {
            text1menu.setText("제품명");
            text2menu.setText("구입처");
            text3menu.setText("가격");
            text4menu.setText("평점");
            text5menu.setText("세부 안내사항");
        } else if (listname.equals("employment")) {
            text1menu.setText("회사명");
            text2menu.setText("회사위치");
            text3menu.setText("연봉 / 시급");
            text4menu.setText("면접일정");
            text5menu.setText("세부 안내사항");
        }

        text1Tv.setText(text1);
        text2Tv.setText(text2);
        text3Tv.setText(text3);
        text4Tv.setText(text4);
        text5Tv.setText(text5);

        contentsTv.setText(contents);
        image = intent.getStringExtra("image");
        imageurl = MainActivity.serverUrl + "userImageFolder/" + listname + "/" + username + "/";

        // 이미지 세팅
        setRecyclerView();
        // 이미지 페이지 넘기는 핸들러
        handler.sendEmptyMessageDelayed(0, 2000);

        hitUpdate hitUpdate = new hitUpdate();
        hitUpdate.requestPost();

        //히스토리 기록
        historyInsert insert = new historyInsert();
        insert.requestPost(listname, idx);

        //즐겨찾기 플래그
        favImageSet();

        favorite.setOnClickListener(this);

    }

    private void favImageSet() {
        for (int i = 0; i < MainActivity.favoriteArr.size(); i++) {
            favoriteItem favoriteTemp = MainActivity.favoriteArr.get(i);
            if (favoriteTemp.getListname().equals(listname) && favoriteTemp.getPostidx().equals(idx)) {
                favoriteFLAG = true;
                favorite.setImageResource(R.drawable.yellowstar);
                break;
            } else {
                favoriteFLAG = false;
                favorite.setImageResource(R.drawable.blackstar);

            }
            Log.d("heu", "플래그 :" + favoriteFLAG);
        }

    }

    private void favoriteSet() {
        MainActivity.getFavorite fav = new MainActivity.getFavorite();
        MainActivity.favoriteArr.clear();
        fav.requestPost(MainActivity.mUsername);
    }

    private void setRecyclerView() {
        if (!image.equals("")) {
            String[] temp = image.split(",");
            for (String aTemp : temp) {
                arr.add(imageurl + aTemp);
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
                if (favoriteFLAG) {
                    favorite.setImageResource(R.drawable.blackstar);
                    favoriteDelete delete = new favoriteDelete();
                    delete.requestPost(idx);
                    favoriteFLAG = false;
                } else {
                    favorite.setImageResource(R.drawable.yellowstar);
                    favoriteInsert insert = new favoriteInsert();
                    insert.requestPost(listname, idx);
                    favoriteFLAG = true;
                }
                MainActivity.getFavorite fav = new MainActivity.getFavorite();
                fav.requestPost(MainActivity.mUsername);
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
//            Log.d("heu", "State IDLE");
            handler.sendEmptyMessageDelayed(0, 2000);

        } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
//            Log.d("heu", "State Draging");
            handler.removeMessages(0);

        } else if (state == ViewPager.SCROLL_STATE_SETTLING) {

//            Log.d("heu", "State Settling");
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
            removeMessages(1000);
            if (MainActivity.favoriteArr != null) {
                favImageSet();
            } else {
                handler2.sendEmptyMessageDelayed(1000, 300);
            }

        }
    };

    class hitUpdate {
        //Client 생성
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
//                    Log.d("heu", "hitupdate res : " + response.body().string());
                }
            });
        }
    }

    class favoriteInsert {
        //Client 생성
        OkHttpClient client = new OkHttpClient();

        public void requestPost(final String listname, final String postidx) {

//            배열에 아이템 추가
            MainActivity.favoriteArr.add(new favoriteItem(username, listname, postidx));

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
//                    Log.d("heu", "favorite res : " + response.body().string());
//                    favoriteSet();
                }
            });
        }
    }

    class favoriteDelete {
        //Client 생성
        OkHttpClient client = new OkHttpClient();

        public void requestPost(String idx) {

            //Request Body에 서버에 보낼 데이터 작성
            RequestBody requestBody = new FormBody.Builder().
                    add("username", MainActivity.mUsername).
                    add("listname", listname).
                    add("postidx", idx).
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
//                    Log.d("heu", "hitupdate res : " + response.body().string());
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
//                    Log.d("heu", "history res : " + response.body().string());
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
