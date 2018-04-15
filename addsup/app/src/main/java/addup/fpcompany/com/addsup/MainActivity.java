package addup.fpcompany.com.addsup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import addup.fpcompany.com.addsup.adapter.PagerAdapter;
import addup.fpcompany.com.addsup.frag.adfrag;
import addup.fpcompany.com.addsup.java.favoriteItem;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    ViewPager mainTopPager;
    int pagerPos = 0;
    ImageView clubBtn;
    ImageView freeBtn;
    ImageView competition;
    ImageView dongho;
    ImageView review;
    ImageView employment;

    Intent intent;
    private static final String TAG = "MainActivity";

    static ArrayList<favoriteItem> favoriteArr = new ArrayList<>();

    private long backPressedTime = 0;

    //상단 광고
    ArrayList<Fragment> adList = new ArrayList<>();

    static FirebaseAuth mAuth;
    static FirebaseUser mUser;
    static String mUsername;
    static String mPhotoUrl;
    int makeMsg = 0;

    public static String serverUrl = "";
    static ArrayList<String> spinList1 = new ArrayList<>();
    static ArrayList<ArrayList<String>> spinList2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainTopPager = findViewById(R.id.mainTopPager);
        clubBtn = findViewById(R.id.clubBtn);
        freeBtn = findViewById(R.id.freeBtn);
        competition = findViewById(R.id.competition);
        dongho = findViewById(R.id.dongho);
        review = findViewById(R.id.review);
        employment = findViewById(R.id.employment);


//        메뉴 로그인
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        // 파이어베이스 notifications
        String firebaseToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d("heu", "Refreshed token : " + firebaseToken);

        // 파이어베이스 admob
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        intent = new Intent(this, addup.fpcompany.com.addsup.ClubList.class);

        // 광고 이미지 주소 받아오기, 프래그먼트 설정
        ADset();

        handler.sendEmptyMessageDelayed(0, 2000);
        // 파이어베이스 구글로그인
        if (mUser == null) {
            //로그인 안한 상태

        } else {
            mUsername = mUser.getEmail();
            if (mUser.getPhotoUrl() != null) {
                mPhotoUrl = mUser.getPhotoUrl().toString();
            }
        }

        ConnectServer connectServer = new ConnectServer();
        connectServer.execute();

        if (SplashActivity.makeMsg < 1) {
            if (mUsername != null) {
                Toast.makeText(this, MainActivity.mUsername + "님 환영합니다", Toast.LENGTH_SHORT).show();
            }
        }
        SplashActivity.makeMsg++;

        //스피너정보 가져오기
        spinList1.clear();
        spinList2.clear();
        getSpinner getSpinner = new getSpinner();
        getSpinner.execute();

        //favorite 가져오기
        if(mUsername != null) {
            getFavorite fav = new getFavorite();
            fav.requestPost(mUsername);
        }

//        클릭 리스너
        clubBtn.setOnClickListener(this);
        freeBtn.setOnClickListener(this);
        competition.setOnClickListener(this);
        dongho.setOnClickListener(this);
        review.setOnClickListener(this);
        employment.setOnClickListener(this);
    }

    private void ADset() {

        getAd getAd = new getAd();
        getAd.execute();
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler.removeMessages(0);
            pagerPos++;

            if (pagerPos >= adList.size()) {
                pagerPos = 0;
                mainTopPager.setCurrentItem(pagerPos, false);
            } else {
                mainTopPager.setCurrentItem(pagerPos);
            }
        }
    };

    @SuppressLint("StaticFieldLeak")
    class ConnectServer extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... strings) {

            Request request = new Request.Builder().url("http://spotz.co.kr/var/www/html/getImage.php").build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            } catch (IOException e) {
                Log.d("heu", "서버접속 에러(메인) : " + e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            serverUrl = s;
        }
    }

    class getAd extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... strings) {

            Request request = new Request.Builder().url("http://spotz.co.kr/var/www/html/getadsrc.php").build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            } catch (IOException e) {
                Log.d("heu", "서버접속 에러(메인) : " + e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String[] temp = s.split(",");
            for (int i = 0; i < temp.length; i++) {
                adList.add(new adfrag(temp[i]));
            }
            final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), adList);
            mainTopPager.setAdapter(adapter);
            mainTopPager.addOnPageChangeListener(MainActivity.this);

        }
    }

    class getSpinner extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... strings) {

            Request request = new Request.Builder().url("http://spotz.co.kr/var/www/html/getspinner.php").build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            } catch (IOException e) {
                Log.d("heu", "서버접속 에러(메인) : " + e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String json = s;
            try {
                JSONObject jsonObj = new JSONObject(json);
                String[] temp = jsonObj.getString("spindata").split(",");
                for (int i = 0; i < temp.length; i++) {
                    spinList1.add(temp[i]);
                }

                for (int i = 0; i < temp.length; i++) {
                    String[] temp2 = jsonObj.getString("spindata" + String.valueOf(i)).split(",");

                    ArrayList<String> tempArr = new ArrayList<>();
                    for (int j = 0; j < temp2.length; j++) {
                        tempArr.add(temp2[j]);
                    }
                    spinList2.add(tempArr);
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("heu", "adapter Exception : " + e);
            }

        }
    }

    static class getFavorite {
        String result;
        OkHttpClient client = new OkHttpClient();

        public void requestPost(String username) {

            String url = "http://spotz.co.kr/var/www/html/favoritelist.php";

            RequestBody requestBody = new FormBody.Builder().
                    add("username", username).
                    build();

            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "Connect Server Error is " + e.toString());

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    result = response.body().string();
                    try {
                        JSONObject jsonObj = new JSONObject(result);
                        JSONArray post = jsonObj.getJSONArray("results");

                        for (int i = 0; i < post.length(); i++) {
                            JSONObject c = post.getJSONObject(i);
                            favoriteArr.add(new favoriteItem(c.getString("idx"), c.getString("listname"), c.getString("postidx")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.clubBtn):
                intent = new Intent(MainActivity.this, ClubList.class);
                intent.putExtra("listName", "clubtable");
                startActivity(intent);
                break;

            case (R.id.freeBtn):
                intent = new Intent(MainActivity.this, ClubList.class);
                intent.putExtra("listName", "freelancer");
                startActivity(intent);
                break;

            case (R.id.competition):
                intent = new Intent(MainActivity.this, ClubList.class);
                intent.putExtra("listName", "competition");
                startActivity(intent);
                break;

            case (R.id.dongho):
                intent = new Intent(MainActivity.this, ClubList.class);
                intent.putExtra("listName", "dongho");
                startActivity(intent);
                break;

            case (R.id.review):
                intent = new Intent(MainActivity.this, ClubList.class);
                intent.putExtra("listName", "review");
                startActivity(intent);
                break;

            case (R.id.employment):
                intent = new Intent(MainActivity.this, ClubList.class);
                intent.putExtra("listName", "employment");
                startActivity(intent);
                break;

            /*case (R.id.bottomHome):
                intent = new Intent(MainActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;*/

            case (R.id.bottomNotice):
                intent = new Intent(MainActivity.this, Notice_Activity.class);
                startActivity(intent);
                break;

            case (R.id.bottomInfo):
                intent = new Intent(MainActivity.this, infoActivity.class);
                startActivity(intent);
                break;

            case (R.id.bottomMember):
                if (mUser == null) {
                    Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                    startActivityForResult(intent, 10);
                } else {
                    Intent intent = new Intent(MainActivity.this, myPageActivity.class);
                    startActivityForResult(intent, 1000);
                }
                break;

        }
    }

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

    @Override
    public void onBackPressed() {

        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        // 메뉴가 열렸으면 닫기!
        long FINISH_INTERVAL_TIME = 2000;
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "앱을 종료하시려면 취소키를 한번 더 눌러주세요", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeMessages(0);
        handler.removeMessages(1);
    }


    @Override
    public void onResume() {
        super.onResume();
        handler.sendEmptyMessage(0);
        handler.sendEmptyMessage(1);
    }

    @Override
    protected void onDestroy() {
        makeMsg = 0;
        handler.removeMessages(0);
        handler.removeMessages(1);
        super.onDestroy();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}

