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
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import addup.fpcompany.com.addsup.adapter.NoticeAdapter;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Notice_Activity extends AppCompatActivity implements View.OnClickListener {

    TextView noticeTv;
    Button noticeIns;
    RecyclerView recyclerView;
    NoticeAdapter adapter;
    ArrayList<noticeItem> noticeArr = new ArrayList<>();

    Intent intent;

    final String TAG = "Notice_Activity";
    String myJSON = "";
    String url = "http://spotz.co.kr/var/www/html/noticetable.php";

    private static final String TAG_IDX = "idx";
    private static final String TAG_RESULTS = "results";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTENTS = "contents";
    private static final String TAG_CREATED = "created";
    private static final String TAG_IMAGE = "image";

    JSONArray topic = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_);

        noticeTv = findViewById(R.id.noticeTv);
        noticeIns = findViewById(R.id.noticeIns);
        recyclerView = findViewById(R.id.recyclerView);

        getPost getPost = new getPost();
        getPost.requestPost(url);

        handler.sendEmptyMessage(100);

        if(MainActivity.mUser.getEmail().equals("skydusic@gmail.com")){
            noticeIns.setVisibility(View.VISIBLE);
            noticeIns.setOnClickListener(this);
        }

    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!myJSON.equals("")) {
                showList();
                setRecyclerView();
                removeMessages(100);
                myJSON = "";
            } else {
                handler.sendEmptyMessageDelayed(100, 200);
            }
        }
    };

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            topic = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < topic.length(); i++) {
                JSONObject c = topic.getJSONObject(i);
                noticeArr.add(new noticeItem(c.getString(TAG_IDX),c.getString(TAG_TITLE),c.getString(TAG_CONTENTS), c.getString(TAG_IMAGE), c.getString(TAG_CREATED)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("heu", "adapter Exception : " + e);
        }
    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NoticeAdapter(getApplicationContext(), noticeArr);
        recyclerView.setAdapter(adapter);
    }

    class getPost {
        OkHttpClient client = new OkHttpClient();
        Request request;

        public void requestPost(String url) {

            RequestBody requestBody = new FormBody.Builder().build();

            request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "Connect Server Error is " + e.toString());

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    myJSON = response.body().string();
                }
            });

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.bottomHome):
                intent = new Intent(Notice_Activity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case (R.id.bottomMember):
                if (MainActivity.mUser == null) {
                    Intent intent = new Intent(Notice_Activity.this, SignInActivity.class);
                    startActivityForResult(intent, 10);
                } else {
                    Intent intent = new Intent(Notice_Activity.this, myPageActivity.class);
                    startActivityForResult(intent, 1000);
                }
                break;
            case (R.id.noticeIns):

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(100);
    }
}
