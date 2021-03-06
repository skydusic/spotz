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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;

import addup.fpcompany.com.addsup.adapter.ActionAdapter;
import addup.fpcompany.com.addsup.adapter.RecyclerItemClickListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class myPageActivity extends AppCompatActivity implements View.OnClickListener {

    TextView userId;
    TextView logoutBtn;
    ProgressBar progressBar;
    RecyclerView actionList;
    ActionAdapter adapter;
    ArrayList<String> actionArr = new ArrayList<>();
    String listname = "";
    String idx = "";

    private static final String TAG_EMAIL1 = "skydusic@gmail.com";
    private static final String TAG_EMAIL2 = "drbasketkorea@gmail.com";
    private static final String TAG_ADMIN = "관리자";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        userId = findViewById(R.id.userId);
        logoutBtn = findViewById(R.id.logoutBtn);
        progressBar = findViewById(R.id.progressBar);
        actionList = findViewById(R.id.actionList);

        handler.sendEmptyMessage(100);
        progressBar.setVisibility(View.VISIBLE);

        getActionList();

//        블랙리스트
        if (MainActivity.mUsermail != null) {
            MainActivity.getBlackList getB = new MainActivity.getBlackList();
            getB.requestPost(MainActivity.mUsermail);
        }

        actionList.addOnItemTouchListener(new RecyclerItemClickListener(this, actionList, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(myPageActivity.this, ActionDetailActivity.class);
                intent.putExtra("actionName", actionArr.get(position));
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        logoutBtn.setOnClickListener(this);
    }

    private void getActionList() {

        actionArr.add("내 글 보기");
//        actionArr.add("즐겨찾기");
        actionArr.add("최근 본 글");
        actionArr.add("닉네임 변경");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        actionList.setLayoutManager(layoutManager);
        adapter = new ActionAdapter(getApplicationContext(), actionArr);
        actionList.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.logoutBtn):
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(myPageActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                MainActivity.mUser = null;
                MainActivity.mUsername = "";
                MainActivity.mUsermail = "";
                this.finish();
                break;
            case (R.id.bottomHome):
                intent = new Intent(myPageActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case (R.id.bottomNotice):
                intent = new Intent(myPageActivity.this, Notice_Activity.class);
                startActivity(intent);
                break;
            case (R.id.bottomInfo):
                intent = new Intent(myPageActivity.this, infoActivity.class);
                startActivity(intent);
                break;
            case (R.id.bottomMember):
                /*Intent intent1 = new Intent(myPageActivity.this, myPageActivity.class);
                startActivity(intent1);*/
                break;
        }
    }

    private void set() {
        FirebaseUser currentUser = MainActivity.mAuth.getCurrentUser();
        MainActivity.mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            if (currentUser.getPhotoUrl() != null) {
                MainActivity.mPhotoUrl = currentUser.getPhotoUrl().toString();
            }
            if(currentUser.getEmail().equals(TAG_EMAIL1) || currentUser.getEmail().equals(TAG_EMAIL2)){
                MainActivity.mUsername = TAG_ADMIN;
                userId.setText(TAG_ADMIN);
            } else {
                MainActivity.mUsername = currentUser.getDisplayName();
                userId.setText(MainActivity.mUsername);
            }
            MainActivity.mUsermail = currentUser.getEmail();
            handler.removeMessages(100);
            progressBar.setVisibility(View.GONE);
        }
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    handler.sendEmptyMessageDelayed(100, 300);
                    set();
                }
            });
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
         *       up = 100
         *       superup = 200
         * */
        if (resultCode == 100) {
            upUpdate upUpdate = new upUpdate();
            upUpdate.requestPost();
        } else if (resultCode == 200) {

        }

    }

    class upUpdate {
        OkHttpClient client = new OkHttpClient();

        public void requestPost() {

            //Request Body에 서버에 보낼 데이터 작성
            RequestBody requestBody = new FormBody.Builder().
                    add("listname", listname).
                    add("idx", idx).
                    build();
            String url = "http://spotz.co.kr/var/www/html/uptable.php";

            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    Log.d("heu", "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
//                    Log.d("heu", "upupdate res : " + response.body().string());
                }
            });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(100);
    }
}
