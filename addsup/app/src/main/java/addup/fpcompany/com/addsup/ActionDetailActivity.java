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

    /**
     * 마이페이지 목록 -> 각 메뉴별 아이템 보여주기
     */
    TextView detailTv;
    RecyclerView recyclerView;
    MainListAdater adapter;
    Intent intent;
    String url = "";
    String Json = "";
    JSONArray post;
    ArrayList<listItem> listArr = new ArrayList<>();

    String pageName;
    String username;
    listItem item;

    private static final String TAG_RESULTS = "results";
    private static final String TAG_ID = "idx";
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
        username = MainActivity.mUsername;
        detailTv.setText(pageName);

        if (pageName.equals("내 글 보기")) {
            url = "http://spotz.co.kr/var/www/html/getPostOfName.php";
        } else if (pageName.equals("즐겨찾기")) {
            url = "http://spotz.co.kr/var/www/html/favoriteselect.php";
        } else if (pageName.equals("최근 본 글")) {
            url = "http://spotz.co.kr/var/www/html/historyselect.php";
        }

        getPostedList getPostedList = new getPostedList();
        getPostedList.requestPost(url, username);
        handler.sendEmptyMessage(50);

    }

    protected void showList(String json) {

        try {
            JSONObject jsonObj = new JSONObject(json);
            post = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < post.length(); i++) {
                JSONObject c = post.getJSONObject(i);
                listArr.add(new listItem(String.valueOf(c.getInt(TAG_ID)), c.getString(TAG_USERNAME), c.getString(TAG_CONTENTS),
                        c.getString(TAG_IMAGE), ClubList.settingTimes(c.getString(TAG_CREATED)), c.getString("listname"),
                        c.getString("text1"), c.getString("text2"), c.getString("text3"), c.getString("text4"),
                        c.getString("text5"), c.getString("hit"), c.getString("spindata1"), c.getString("spindata2")));
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
                            startActivityForResult(intent, 1000);
                        } else if (pageName.equals("즐겨찾기") || pageName.equals("최근 본 글")) {
                            item = listArr.get(position);
                            intent = new Intent(ActionDetailActivity.this, DetailList.class);
                            intent.putExtra("listname", item.getListname());
                            intent.putExtra("idx", item.getIdx());
                            intent.putExtra("username", item.getUsername());
                            intent.putExtra("image", item.getImage());
                            intent.putExtra("contents", item.getContents());
                            intent.putExtra("spindata1", item.getSpindata1());
                            intent.putExtra("spindata2", item.getSpindata2());
                            intent.putExtra("created", item.getCreated());
                            intent.putExtra("text1", item.getText1());
                            intent.putExtra("text2", item.getText2());
                            intent.putExtra("text3", item.getText3());
                            intent.putExtra("text4", item.getText4());
                            intent.putExtra("text5", item.getText5());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 100) {
            //up

        } else if (requestCode == 200) {
            //super up

        } else if (requestCode == 300) {
            // 수정
            Intent intent = new Intent(ActionDetailActivity.this, insertActivity.class);
            // 0 -> 새글 1 -> 수정
            intent.putExtra("postNum", 1);
            intent.putExtra("idx", item.getIdx());
            intent.putExtra("contents", item.getContents());
            intent.putExtra("username", item.getUsername());
            intent.putExtra("image", item.getImage());
            intent.putExtra("listname", item.getListname());
            intent.putExtra("spindata1", item.getSpindata1());
            intent.putExtra("spindata2", item.getSpindata2());
            intent.putExtra("created", item.getCreated());
            intent.putExtra("text1", item.getText1());
            intent.putExtra("text2", item.getText2());
            intent.putExtra("text3", item.getText3());
            intent.putExtra("text4", item.getText4());
            intent.putExtra("text5", item.getText5());
            startActivityForResult(intent, 2000);

        } else if (requestCode == 400) {
            // 삭제

        }

    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!Json.equals("")) {
                showList(Json);
                Json = "";
                handler.removeMessages(50);
            } else {
                handler.sendEmptyMessageDelayed(50, 200);
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

    class postUpdate {
        //Client 생성
        OkHttpClient client = new OkHttpClient();

        public void requestPost(listItem item) {

            //Request Body에 서버에 보낼 데이터 작성
            RequestBody requestBody = new FormBody.Builder().
                    add("idx", item.getIdx()).
                    add("contents", item.getContents()).
                    add("username", item.getUsername()).
                    add("image", item.getImage()).
                    add("listname", item.getListname()).
                    add("spindata1", item.getSpindata1()).
                    add("spindata2", item.getSpindata2()).
                    add("username", item.getUsername()).
                    add("username", item.getUsername()).
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
        handler.removeMessages(50);
    }
}
