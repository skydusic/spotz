package addup.fpcompany.com.addsup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

    TextView detailTv;
    RecyclerView recyclerView;
    MainListAdater adapter;
    Intent intent;
    String url;
    String Json = "";
    JSONArray post;
    ArrayList<listItem> listArr = new ArrayList<>();

    String pageName;
    listItem item;

    private static final String TAG_RESULTS = "results";
    private static final String TAG_ID = "idx";
    private static final String TAG_TITLE = "title";
    private static final String TAG_USERNAME = "username";
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
        detailTv.setText(pageName);

        if (pageName.equals("내 글 보기")) {
            url = "http://spotz.co.kr/var/www/html/getPostOfName.php";
        } else if (pageName.equals("즐겨찾기")) {
            url = "";
        } else if (pageName.equals("최근 본 글")) {
            url = "";
        }

        getData();


    }

    protected void showList(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            post = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < post.length(); i++) {
                JSONObject c = post.getJSONObject(i);
                    listArr.add(new listItem(String.valueOf(c.getInt(TAG_ID)), c.getString(TAG_USERNAME), c.getString(TAG_TITLE), c.getString(TAG_CONTENTS),
                            c.getString(TAG_IMAGE), ClubList.settingTimes(c.getString(TAG_CREATED)), c.getString("listname")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("heu", "adapter Exception : " + e);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("heu", "adapter ETC Excep : " + e);
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

                        if (pageName.equals("내 글 보기")) {
                            item = listArr.get(position);
                            Intent intent = new Intent(ActionDetailActivity.this, myPageOption.class);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
    }

    private void getData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                getPostedList getPostedList = new getPostedList();
                getPostedList.requestPost(url, MainActivity.mUsername);
                handler1.sendEmptyMessage(100);
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!Json.equals("")) {
                showList(Json);
                removeMessages(100);
                Json = "";
            } else {
                handler1.sendEmptyMessageDelayed(100, 200);
            }
        }
    };


    // 올린 글을 찾아온다
    class getPostedList {
        //Client 생성
        OkHttpClient client = new OkHttpClient();

        public void requestPost(String url, String username) {

            //Request Body에 서버에 보낼 데이터 작성
            RequestBody requestBody = new FormBody.Builder().add("username", username).build();

            final Request request = new Request.Builder().url(url).post(requestBody).build();
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

}