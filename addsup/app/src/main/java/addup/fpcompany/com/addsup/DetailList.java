package addup.fpcompany.com.addsup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import addup.fpcompany.com.addsup.adapter.CommentAdapter;
import addup.fpcompany.com.addsup.adapter.PagerAdapter;
import addup.fpcompany.com.addsup.frag.DetailFrag;
import addup.fpcompany.com.addsup.java.commentItem;
import addup.fpcompany.com.addsup.java.favoriteItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DetailList extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, View.OnFocusChangeListener {

    Intent intent;

    RelativeLayout mainLayout;
    RelativeLayout commentLay;
    ScrollView detailScrollView;
    TextView titleTv;
    TextView contentsTv;
    TextView commentCount;
    TextView timeTv;
    EditText commentEt;
    Button inputComment;
    Button editpostBT;
    Button delpostBT;
    ViewPager viewPager;
    ImageView favorite;
    ArrayList<String> arr = new ArrayList<>();
    ArrayList<Fragment> fragArr = new ArrayList<>();
    ArrayList<commentItem> listItems = new ArrayList<>();

    RecyclerView recyclerView;

    String listname = "";
    String idx = "";
    String title = "";
    String contents = "";
    String username = "";
    String created = "";
    String image = "";
    String spindata = "";

    String imageurl = "";
    String commentJson = "";

    Boolean favoriteFLAG = false;

    favoriteItem favoriteTemp;

    InputMethodManager imm;

    private static final String TAG_RESULTS = "results";
    private static final String TAG_TITLE = "title";
    private static final String TAG_ID = "idx";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_CONTENTS = "contents";
    private static final String TAG_CREATED = "created";
    private static final String TAG_IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list);

        titleTv = findViewById(R.id.titleTv);
        contentsTv = findViewById(R.id.contents);
        timeTv = findViewById(R.id.timeTv);
        viewPager = findViewById(R.id.viewPager);
        favorite = findViewById(R.id.favorite);
        commentCount = findViewById(R.id.commentCount);
        commentEt = findViewById(R.id.commentEt);
        detailScrollView = findViewById(R.id.detailScrollView);
        editpostBT = findViewById(R.id.editpostBT);
        delpostBT = findViewById(R.id.delpostBT);

        recyclerView = findViewById(R.id.commentRecycle);

        //레이아웃에 클릭 붙이기 -> 레이아웃 클릭하면 키보드 하이드
        mainLayout = findViewById(R.id.mainLayout);
        commentLay = findViewById(R.id.commentLay);

        //댓글버튼
        inputComment = findViewById(R.id.inputComment);

        // 인텐트로 정보 가져옴
        intent = getIntent();
        listname = intent.getStringExtra("listname");
        idx = intent.getStringExtra("idx");
        title = intent.getStringExtra("title");
        contents = intent.getStringExtra("contents");
        username = intent.getStringExtra("username");
        created = intent.getStringExtra("created");
        spindata = intent.getStringExtra("spindata");

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        titleTv.setText(title);
        contentsTv.setText(contents);
        timeTv.setText(created);
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


        //코멘트 가져오기
        resetCommentList();

        favorite.setOnClickListener(this);
        inputComment.setOnClickListener(this);
        mainLayout.setOnClickListener(this);
        commentEt.setOnFocusChangeListener(this);
        editpostBT.setOnClickListener(this);
        delpostBT.setOnClickListener(this);

    }

    private void resetCommentList(){

        if (!commentJson.equals("")){
            commentJson = "";
        }

        getComment getComment = new getComment();
        getComment.requestPost(idx, listname);

        commentHandler.sendEmptyMessage(500);

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
//            Log.d("heu", "플래그 :" + favoriteFLAG);
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

    private void hideKeyboard()
    {
        imm.hideSoftInputFromWindow(commentEt.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();
        switch (v.getId()) {
            case (R.id.mainLayout):
                break;
            case (R.id.inputComment):
                String text = commentEt.getText().toString();
                if(!text.equals("")) {
                    recyclerView.removeAllViewsInLayout();
                    commentInsert CI = new commentInsert();
                    CI.requestPost(listname, commentEt.getText().toString(), idx);
                    commentEt.setText("");
                }
                resetCommentList();
                hideKeyboard();
                break;
            case (R.id.editpostBT):

                Log.d("heu", "디테일 리스트 네임: " + listname);

                // 수정
                Intent editIntent = new Intent(DetailList.this, insertActivity.class);
                // 0 -> 새글 1 -> 수정
                editIntent.putExtra("postNum", 1);
                editIntent.putExtra("idx", idx);
                editIntent.putExtra("title", title);
                editIntent.putExtra("contents", contents);
                editIntent.putExtra("username", username);
                editIntent.putExtra("image", image);
                editIntent.putExtra("listname", listname);
//                intent.putExtra("spindata", );
                editIntent.putExtra("created", created);
                startActivityForResult(editIntent, 3000);

                break;
            case (R.id.delpostBT):

                // 삭제
                postDelete postDelete = new postDelete();
                postDelete.requestPost(idx, listname, username);


                break;
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

    @SuppressLint("HandlerLeak")
    Handler commentHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (!commentJson.equals("")){
                //코멘트 객체화
                setCommentJson(commentJson);

                //코멘트 어댑터 적용
                setCommentlist();
                removeMessages(500);
                commentJson = "";

            } else {
                commentHandler.sendEmptyMessageDelayed(500, 300);
            }
        }
    };

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case (R.id.commentEt):
                detailScrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        detailScrollView.smoothScrollTo(0, commentLay.getTop());
                    }
                }, 100);
                break;
        }
    }

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

    private void setCommentlist() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        CommentAdapter adapter = new CommentAdapter(getApplicationContext(), listItems);
        recyclerView.setAdapter(adapter);

    }

    private void setCommentJson(String commentJson){

        JSONArray comment;
        listItems.clear();
        try {
            JSONObject jsonObj = new JSONObject(commentJson);
            comment = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < comment.length(); i++) {
                JSONObject c = comment.getJSONObject(i);
                listItems.add(new commentItem(String.valueOf(c.get(TAG_ID)), c.getString("listname"),
                        c.getString(TAG_USERNAME), c.getString(TAG_CONTENTS), ClubList.settingTimes(c.getString(TAG_CREATED))));
            }
        } catch (JSONException e){
            e.printStackTrace();
            Log.d("heu", "adapter Exception : " + e);
        }
    }

    class commentInsert {

        RequestBody requestBody;
        OkHttpClient client = new OkHttpClient();

        public void requestPost(final String listname, String contents, String postidx) {

            requestBody = new FormBody.Builder().
                    add("username", MainActivity.mUsername).
                    add("postidx", postidx).
                    add("listname", listname).
                    add("contents", contents).
                    build();
            String url = "http://spotz.co.kr/var/www/html/commentInsert.php";

            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("heu", "Connect Server Error is " + e.toString());

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d("heu", "응답(코멘트) :" +response.toString());
                }
            });
        }
    }

    class getComment {

        OkHttpClient client = new OkHttpClient();
        Request request;

        public void requestPost(String idx, String listname) {
            RequestBody requestBody = new FormBody.Builder().
                    add("postidx", idx).
                    add("listname", listname).
                    build();

            String url = "http://spotz.co.kr/var/www/html/commentSelect.php";
            request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("heu", "Connect Server Error is " + e.toString());

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    commentJson = response.body().string();
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

    String Json;
    class postDelete {
        //Client 생성
        OkHttpClient client = new OkHttpClient();

        public void requestPost(String idx, String listname, String username) {

            String url = "http://spotz.co.kr/var/www/html/deletepost.php";
            //Request Body에 서버에 보낼 데이터 작성
            RequestBody requestBody = new FormBody.Builder().
                    add("idx", idx).
                    add("listname", listname).
                    add("username", username).
                    build();

            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("heu", "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Json = response.body().string();

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
        commentHandler.removeMessages(500);
        handler2.removeMessages(1000);
    }
}
