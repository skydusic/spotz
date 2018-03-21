package addup.fpcompany.com.addsup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import addup.fpcompany.com.addsup.adapter.MyPageOptionAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class myPageActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    TextView userId;
    TextView logoutBtn;
    ProgressBar progressBar;
    ListView postList;

    String listName;
    String getUserData = "http://spotz.co.kr/var/www/html/getPostOfName.php";
    String Json = "";

    ArrayList<listItem> listArr = new ArrayList<>();
    JSONArray post;

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
        setContentView(R.layout.activity_my_page);

        userId = findViewById(R.id.userId);
        logoutBtn = findViewById(R.id.logoutBtn);
        progressBar = findViewById(R.id.progressBar);
        postList = findViewById(R.id.postList);

        handler.sendEmptyMessage(100);
        progressBar.setVisibility(View.VISIBLE);

        getUserData();

        logoutBtn.setOnClickListener(this);
    }


    private void getUserData() {
        // 네트워크 코드이므로 쓰레드에서 실행
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPostedList getPostedList = new getPostedList();
                getPostedList.requestPost(getUserData, MainActivity.mUsername);
                /** showList(); 부르는 핸들러 */
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
                Log.d("heu", "Json : " + Json);
                Log.d("heu", "마이페이지 제이슨!");
                showList(Json);
                removeMessages(100);
                Json = "";
            } else {
                Log.d("heu", "마이페이지 엘스!");
                handler1.sendEmptyMessageDelayed(100, 200);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.logoutBtn):
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();
                finish();

                break;
            case (R.id.bottomHome):
                Intent intent = new Intent(myPageActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        if (currentUser == null) {

        } else {
            MainActivity.mUsername = MainActivity.mUser.getDisplayName();
            if (currentUser.getPhotoUrl() != null) {
                MainActivity.mPhotoUrl = currentUser.getPhotoUrl().toString();
            }

            userId.setText(MainActivity.mUsername);
            handler.removeMessages(100);
            progressBar.setVisibility(View.GONE);
        }
    }

    protected void showList(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            post = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < post.length(); i++) {
                JSONObject c = post.getJSONObject(i);
                try {
                    listArr.add(new listItem(String.valueOf(c.getInt(TAG_ID)), c.getString(TAG_USERNAME), c.getString(TAG_TITLE), c.getString(TAG_CONTENTS),
                            c.getString(TAG_IMAGE), ClubList.settingTimes(c.getString(TAG_CREATED)), c.getString("listname")));
                } catch (JSONException e){
                    listArr.add(new listItem(String.valueOf(c.getInt(TAG_ID)), c.getString(TAG_USERNAME), c.getString(TAG_TITLE), c.getString(TAG_CONTENTS),
                            "basic_image.png", c.getString(TAG_CREATED), c.getString("listname")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("heu", "adapter Exception : " + e);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("heu", "adapter ETC Excep : " + e);
        }

        MyPageOptionAdapter adapter = new MyPageOptionAdapter(this, listArr);
        postList.setAdapter(adapter);
        postList.setOnItemClickListener(this);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
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

    String listname = "";
    String idx = "";

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        listname = listArr.get(position).getListname();
        idx = listArr.get(position).getIdx();

        Intent intent = new Intent(getApplicationContext(), myPageOption.class);
        startActivityForResult(intent, 1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
/*
*       up = 100
*       superup = 200
* */
        if(resultCode == 100){
            upUpdate upUpdate = new upUpdate();
            upUpdate.requestPost();
        } else if (resultCode == 200){

        }

    }

    // 업 버튼을 누르면 작동
    class upUpdate {
        //Client 생성
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
                    Log.d("heu", "Connect Server Error is " + e.toString());
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {

                }
            });
        }
    }

    // 올린 글을 찾아온다
    class getPostedList {
        //Client 생성
        OkHttpClient client = new OkHttpClient();

        public void requestPost(String url, String username) {

            /*** 마이페이지에서 누를때 변경되는 로직 필요 ***/

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(100);
    }
}
