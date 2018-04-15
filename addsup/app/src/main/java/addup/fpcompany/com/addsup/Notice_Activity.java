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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import addup.fpcompany.com.addsup.adapter.NoticeAdapter;
import addup.fpcompany.com.addsup.adapter.RecyclerItemClickListener;
import addup.fpcompany.com.addsup.java.noticeItem;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Notice_Activity extends AppCompatActivity implements View.OnClickListener, RecyclerView.OnItemTouchListener {

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
    getPost getPost = new getPost();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_);

        noticeTv = findViewById(R.id.noticeTv);
        noticeIns = findViewById(R.id.noticeIns);
        recyclerView = findViewById(R.id.recyclerView);

        getPost.requestPost(url);
        handler.sendEmptyMessage(100);

        if (MainActivity.mUser != null && MainActivity.mUser.getEmail().equals("skydusic@gmail.com")) {
            noticeIns.setVisibility(View.VISIBLE);
            noticeIns.setOnClickListener(this);
        }

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        // do whatever

                        Intent intent = new Intent(v.getContext(), Notice_Detail.class);
                        intent.putExtra("listname", "공지사항");
                        intent.putExtra("idx", noticeArr.get(position).getIdx());
                        intent.putExtra("title", noticeArr.get(position).getTitle());
                        intent.putExtra("contents", noticeArr.get(position).getContents());
                        intent.putExtra("created", noticeArr.get(position).getCreated());
                        intent.putExtra("image", noticeArr.get(position).getImage());
                        v.getContext().startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );


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
        noticeArr.clear();
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            topic = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < topic.length(); i++) {
                JSONObject c = topic.getJSONObject(i);
                noticeArr.add(new noticeItem(c.getString(TAG_IDX), c.getString(TAG_TITLE), c.getString(TAG_CONTENTS), c.getString(TAG_IMAGE), ClubList.settingTimes(c.getString(TAG_CREATED))));
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

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    class getPost {
        OkHttpClient client = new OkHttpClient();
        Request request;

        void requestPost(String url) {

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

            case (R.id.bottomNotice):
                intent = new Intent(Notice_Activity.this, Notice_Activity.class);
                startActivity(intent);
                break;

            case (R.id.bottomInfo):
                intent = new Intent(Notice_Activity.this, infoActivity.class);
                startActivity(intent);
                break;

            case (R.id.noticeIns):
                Intent intent = new Intent(Notice_Activity.this, NoticeInsertActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(100);
    }

    @Override
    public void onResume() {
        super.onResume();

        myJSON = "";
        getPost.requestPost(url);
        handler.sendEmptyMessage(100);
    }
}
