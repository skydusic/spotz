package addup.fpcompany.com.addsup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    TextView contentsTv;
    ViewPager viewPager;
    ImageView favorite;
    ArrayList<String> arr = new ArrayList<>();
    ArrayList<Fragment> fragArr = new ArrayList<>();

    RelativeLayout detailLayout1;
    RelativeLayout detailLayout2;

    TextView ownerTv;
    TextView timetableTv;
    TextView locationTv;
    TextView trafficTv;
    TextView feeTv;
    TextView phoneTv;

    String idx = "";
    String title = "";
    String contents = "";
    String created = "";
    String image = "";
    String listname = "";
    String owner = "";
    String timetable = "";
    String location = "";
    String traffic = "";
    String fee = "";
    String phone = "";

    String url = MainActivity.serverUrl + "userImageFolder/";

    Boolean favoriteFLAG = false;

    favoriteItem favoriteTemp;
    int favoritePos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list);

        detailNameTv = findViewById(R.id.detailNameTv);
        midDetailTv1 = findViewById(R.id.midDetailTv1);
        midDetailTv2 = findViewById(R.id.midDetailTv2);
        contentsTv = findViewById(R.id.contents);
        viewPager = findViewById(R.id.viewPager);
        favorite = findViewById(R.id.favorite);

        detailLayout1 = findViewById(R.id.detailLayout1);
        detailLayout2 = findViewById(R.id.detailLayout2);

        ownerTv = findViewById(R.id.ownerTv);
        timetableTv = findViewById(R.id.timetableTv);
        locationTv = findViewById(R.id.locationTv);
        trafficTv = findViewById(R.id.trafficTv);
        feeTv = findViewById(R.id.feeTv);
        phoneTv = findViewById(R.id.phoneTv);

        // 인텐트로 정보 가져옴
        intent = getIntent();
        idx = intent.getStringExtra("idx");
        title = intent.getStringExtra("title");
        contents = intent.getStringExtra("contents");
        created = intent.getStringExtra("created");
        listname = intent.getStringExtra("listname");
        owner = intent.getStringExtra("owner");
        timetable = intent.getStringExtra("timetable");
        location = intent.getStringExtra("location");
        traffic = intent.getStringExtra("traffic");
        fee = intent.getStringExtra("fee");
        phone = intent.getStringExtra("phone");

        detailNameTv.setText(title);
        contentsTv.setText(contents);
        ownerTv.setText(owner);
        timetableTv.setText(timetable);
        locationTv.setText(location);
        trafficTv.setText(traffic);
        feeTv.setText(fee);
        phoneTv.setText(phone);

        image = intent.getStringExtra("image");

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

        Log.d("heu", "ARR size : " + MainActivity.favoriteArr.size());
        for (int i = 0; i < MainActivity.favoriteArr.size(); i++) {
            favoriteTemp = MainActivity.favoriteArr.get(i);
            Log.d("heu", "리스트네임 : " + listname + ", 템프 : " + favoriteTemp.getListname());
            Log.d("heu", "인덱스 : " + idx + ", 템프 : " + favoriteTemp.getPostidx());
            if (favoriteTemp.getListname().equals(listname) && favoriteTemp.getPostidx().equals(idx)) {
                favoriteFLAG = true;
                favorite.setImageResource(R.drawable.yellowstar);
                favoritePos = Integer.parseInt(favoriteTemp.getIdx());
            } else {
                favorite.setImageResource(R.drawable.blackstar);

            }
        }

        favorite.setOnClickListener(this);
        midDetailTv1.setOnClickListener(this);
        midDetailTv2.setOnClickListener(this);

        /*MainActivity.getFavorite favorite = new MainActivity.getFavorite();
        favorite.requestPost(MainActivity.mUsername);*/

    }

    private void favImageSet() {
        for (int i = 0; i < MainActivity.favoriteArr.size(); i++) {
            favoriteItem favoriteTemp = MainActivity.favoriteArr.get(i);
            if (favoriteTemp.getListname().equals(listname) && favoriteTemp.getPostidx().equals(idx)) {
                favoriteFLAG = true;
                favorite.setImageResource(R.drawable.yellowstar);
                favoritePos = Integer.parseInt(favoriteTemp.getIdx());
                break;
            } else {
                favoriteFLAG = false;
                favorite.setImageResource(R.drawable.blackstar);
            }
        }

        favorite.setOnClickListener(this);
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
                if (favoriteFLAG) {
                    favorite.setImageResource(R.drawable.blackstar);
                    favoriteDelete delete = new favoriteDelete();
                    delete.requestPost(String.valueOf(favoritePos));

                    favoriteFLAG = !favoriteFLAG;
                    MainActivity.getFavorite fav = new MainActivity.getFavorite();
                    fav.requestPost(MainActivity.mUsername);

                } else {
                    favorite.setImageResource(R.drawable.yellowstar);
                    favoriteInsert insert = new favoriteInsert();
                    insert.requestPost(listname, idx);
                }
                break;
            case (R.id.midDetailTv1):
                detailLayout1.setVisibility(View.VISIBLE);
                detailLayout2.setVisibility(View.INVISIBLE);
                midDetailTv1.setBackgroundColor(Color.parseColor("#e0e0e0"));
                midDetailTv2.setBackgroundColor(Color.parseColor("#ffffff"));
                break;

            case (R.id.midDetailTv2):
                detailLayout2.setVisibility(View.VISIBLE);
                detailLayout1.setVisibility(View.INVISIBLE);
                midDetailTv1.setBackgroundColor(Color.parseColor("#ffffff"));
                midDetailTv2.setBackgroundColor(Color.parseColor("#e0e0e0"));
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
            if (MainActivity.favoriteArr != null) {
                favImageSet();
                removeMessages(1000);
            } else {
                handler2.sendEmptyMessageDelayed(1000, 200);
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
//                    Log.d("heu", "favorite res : " + response.body().string());
                    favoriteSet();
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
