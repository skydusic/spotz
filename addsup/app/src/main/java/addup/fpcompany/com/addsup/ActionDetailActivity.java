package addup.fpcompany.com.addsup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import addup.fpcompany.com.addsup.adapter.MainListAdater;
import addup.fpcompany.com.addsup.adapter.RecyclerItemClickListener;
import addup.fpcompany.com.addsup.java.listItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ActionDetailActivity extends AppCompatActivity {

    /**
     * 마이페이지 목록 -> 각 메뉴별 아이템 보여주기
     */
    TextView detailTv;
    RecyclerView recyclerView;
    MainListAdater adapter;
    Intent intent;
    JSONArray post;
    ArrayList<listItem> listArr = new ArrayList<>();
    listItem item;

    String pageName, username, email;
    String url, Json = "";
    int pageOrder = 0;

    postDelete delete = new postDelete();
    getPostedList getPostedList;

    private static final String TAG_RESULTS = "results";
    private static final String TAG_TITLE = "title";
    private static final String TAG_ID = "idx";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_CONTENTS = "contents";
    private static final String TAG_CREATED = "created";
    private static final String TAG_IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_detail);

        detailTv = findViewById(R.id.detailTv);
        recyclerView = findViewById(R.id.recyclerView);
        intent = getIntent();
        pageName = intent.getStringExtra("actionName");
        username = MainActivity.mUsername;
        email = MainActivity.mUsermail;
        detailTv.setText(pageName);

        if (pageName.equals("내 글 보기")) {
            url = "http://spotz.co.kr/var/www/html/getPostOfName.php";
            getPostedList = new getPostedList();
            getPostedList.requestPost(url, username, email, pageOrder);
        } else if (pageName.equals("즐겨찾기")) {
            url = "http://spotz.co.kr/var/www/html/favoriteselect.php";
            getPostedList = new getPostedList();
            getPostedList.requestPost(url, username, email, pageOrder);
        } else if (pageName.equals("최근 본 글")) {
            url = "http://spotz.co.kr/var/www/html/historyselect.php";
            getPostedList = new getPostedList();
            getPostedList.requestPost(url, username, email, pageOrder);
        } else if (pageName.equals("닉네임 변경")) {
            url = "http://spotz.co.kr/var/www/html/nameSelect.php";
            Intent intent = new Intent(ActionDetailActivity.this, nameChange.class);
            intent.putExtra("username", MainActivity.mUsername);
            intent.putExtra("email", MainActivity.mUsermail);
            startActivity(intent);
            this.finish();
        }

        handler.sendEmptyMessage(50);

    }

    protected String showList(String json) {
        listArr.clear();
        try {
            JSONObject jsonObj = new JSONObject(json);
            post = jsonObj.getJSONArray(TAG_RESULTS);
            for (int i = 0; i < post.length(); i++) {
                JSONObject c = post.getJSONObject(i);
                listArr.add(new listItem(String.valueOf(c.getInt(TAG_ID)), c.getString(TAG_TITLE), c.getString(TAG_USERNAME), c.getString(TAG_EMAIL),
                        c.getString(TAG_IMAGE), ClubList.settingTimes(c.getString(TAG_CREATED)), c.getString("listname"),
                        c.getString("hit"), c.getString("spindata")));
            }
            pageOrder++;
        } catch (JSONException e) {
            e.printStackTrace();
//            Log.d("heu", "adapter Exception : " + e);
        } catch (Exception e) {
            e.printStackTrace();
//            Log.d("heu", "adapter ETC Excep : " + e);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MainListAdater(getApplicationContext(), listArr);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(ActionDetailActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        item = listArr.get(position);

                        if (pageName.equals("내 글 보기")) {
                            Intent intent = new Intent(ActionDetailActivity.this, myPageOption.class);
                            intent.putExtra("listname", item.getListname());
                            intent.putExtra("idx", item.getIdx());
                            intent.putExtra("title", item.getTitle());
                            intent.putExtra("username", item.getUsername());
                            intent.putExtra("email", item.getEmail());
                            intent.putExtra("image", item.getImage());
                            intent.putExtra("spindata", item.getSpindata());
                            intent.putExtra("created", item.getCreated());
                            startActivityForResult(intent, 1000);
                        } else if (pageName.equals("즐겨찾기") || pageName.equals("최근 본 글")) {
                            Intent intent = new Intent(ActionDetailActivity.this, DetailList.class);
                            intent.putExtra("listname", item.getListname());
                            intent.putExtra("idx", item.getIdx());
                            intent.putExtra("title", item.getTitle());
                            intent.putExtra("username", item.getUsername());
                            intent.putExtra("email", item.getEmail());
                            intent.putExtra("image", item.getImage());
                            intent.putExtra("spindata", item.getSpindata());
                            intent.putExtra("created", item.getCreated());
                            startActivityForResult(intent, 2000);
                        }
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(-1) && newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    refreshHandler.sendEmptyMessage(3000);
                } else if (!recyclerView.canScrollVertically(1) && newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
//                    Log.i(TAG, "End of list");
                    Json = "";
                    getPostedList.requestPost(url,username,email,pageOrder);
                    addPageHandler.sendEmptyMessageDelayed(4000, 200);
                } else {
                }

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {

                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {

                } else {

                }
            }
        });
        return "finish";
    }

    private void addList(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            post = jsonObj.getJSONArray(TAG_RESULTS);
            for (int i = 0; i < post.length(); i++) {
                JSONObject c = post.getJSONObject(i);
                listArr.add(new listItem(String.valueOf(c.getInt(TAG_ID)), c.getString(TAG_TITLE), c.getString(TAG_USERNAME), c.getString(TAG_EMAIL),
                        c.getString(TAG_IMAGE), ClubList.settingTimes(c.getString(TAG_CREATED)), c.getString("listname"),
                        c.getString("hit"), c.getString("spindata")));
            }
            pageOrder++;
        } catch (JSONException e) {
            e.printStackTrace();
//            Log.d("heu", "adapter Exception : " + e);
        } catch (Exception e) {
            e.printStackTrace();
//            Log.d("heu", "adapter ETC Excep : " + e);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 100) {
            //up

        } else if (resultCode == 200) {
            //super up

        } else if (resultCode == 300) {
            // 수정
            Intent intent = new Intent(ActionDetailActivity.this, insertActivity.class);
            // 0 -> 새글 1 -> 수정
            intent.putExtra("postNum", 1);
            intent.putExtra("idx", item.getIdx());
            intent.putExtra("title", item.getTitle());
            intent.putExtra("username", item.getUsername());
            intent.putExtra("email", item.getEmail());
            intent.putExtra("image", item.getImage());
            intent.putExtra("listname", item.getListname());
            intent.putExtra("spindata", item.getSpindata());
            intent.putExtra("created", item.getCreated());
            startActivityForResult(intent, 3000);

        } else if (resultCode == 400) {
            // 삭제

            String url = "http://spotz.co.kr/var/www/html/deletepost.php";
            delete.requestPost(item);
            Json = "";
            listArr.clear();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getPostedList.requestPost(url, username, email, 0);
            handler.sendEmptyMessage(50);

        } else if (resultCode == 500) {
            // 보러가기
            Intent moveIntent = new Intent(ActionDetailActivity.this, DetailList.class);
            moveIntent.putExtra("idx", item.getIdx());
            moveIntent.putExtra("title", item.getTitle());
            moveIntent.putExtra("username", item.getUsername());
            moveIntent.putExtra("email", item.getEmail());
            moveIntent.putExtra("image", item.getImage());
            moveIntent.putExtra("listname", item.getListname());
            moveIntent.putExtra("spindata", item.getSpindata());
            moveIntent.putExtra("created", item.getCreated());
            startActivity(moveIntent);
        }

        /*if(resultCode == 2400) {
            getPostedList.requestPost(url, username, email);
            handler.sendEmptyMessage(50);
        }*/


    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler.removeMessages(50);
            if (!Json.equals("")) {
                showList(Json);
                Json = "";
            } else {
                handler.sendEmptyMessageDelayed(50, 200);
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler refreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            removeMessages(3000);
            pageOrder = 0;
            recyclerView.removeAllViewsInLayout();
            getPostedList.requestPost(url, username, email, pageOrder);
            handler.sendEmptyMessage(50);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler addPageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            removeMessages(4000);
            if (Json.equals("")) {
                addPageHandler.sendEmptyMessageDelayed(4000, 100);
            } else {
                addList(Json);
            }
        }
    };

    // 올린 글을 찾아온다
    class getPostedList {
        //Client 생성
        OkHttpClient client = new OkHttpClient();

        public void requestPost(String url, String username, String email, int pageOrder) {

            //Request Body에 서버에 보낼 데이터 작성
            RequestBody requestBody = new FormBody.Builder().
                    add("username", username).
                    add("email", email).
                    add("pageorder", String.valueOf(pageOrder)).
                    build();

            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    Log.d("heu", "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Json = response.body().string();
                }
            });
        }
    }

    class postDelete {
        //Client 생성
        OkHttpClient client = new OkHttpClient();

        public void requestPost(listItem item) {

            String url = "http://spotz.co.kr/var/www/html/deletepost.php";
            //Request Body에 서버에 보낼 데이터 작성
            RequestBody requestBody = new FormBody.Builder().
                    add("idx", item.getIdx()).
                    add("listname", item.getListname()).
                    add("username", item.getUsername()).
                    add("email", item.getEmail()).
                    build();

            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    Log.d("heu", "Connect Server Error is " + e.toString());
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
        handler.removeMessages(50);
    }
}
