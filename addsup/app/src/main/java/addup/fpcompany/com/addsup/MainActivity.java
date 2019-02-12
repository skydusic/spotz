package addup.fpcompany.com.addsup;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.Arrays;

import addup.fpcompany.com.addsup.adapter.PagerAdapter;
import addup.fpcompany.com.addsup.frag.adfrag;
import addup.fpcompany.com.addsup.java.favoriteItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener, View.OnTouchListener {

    ViewPager mainTopPager;
    TextView hyperLinkTv;
    int pagerPos = 0;
    ImageView btnOne;
    ImageView btnTwo;
    ImageView btnThree;
    ImageView btnFour;
    ImageView btnFive;
    ImageView btnSix;

    Intent intent;
    private static final String TAG = "MainActivity";

    static ArrayList<favoriteItem> favoriteArr = new ArrayList<>();

    private long backPressedTime = 0;

    //상단 광고
    ArrayList<Fragment> adList = new ArrayList<>();
    ArrayList<String> adUrl = new ArrayList<>();

    static FirebaseAuth mAuth;
    static FirebaseUser mUser;
    static public String mUsername;
    static public String mUsermail;
    static String mPhotoUrl;
    int makeMsg = 0;

    private static final String TAG_EMAIL1 = "skydusic@gmail.com";
    private static final String TAG_EMAIL2 = "drbasketkorea@gmail.com";

    public static String serverUrl = "";
    static ArrayList<String> spinList1 = new ArrayList<>();

    AlertDialog.Builder mDialog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainTopPager = findViewById(R.id.mainTopPager);
        hyperLinkTv = findViewById(R.id.hyperLinkTv);
        btnOne = findViewById(R.id.btnOne);
        btnTwo = findViewById(R.id.btnTwo);
        btnThree = findViewById(R.id.btnthree);
        btnFour = findViewById(R.id.btnFour);
        btnFive = findViewById(R.id.btnFive);
        btnSix = findViewById(R.id.btnSix);


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
        handler.sendEmptyMessageDelayed(0, 3500);

        // 파이어베이스 구글로그인
        if (mUser == null) {
            //로그인 안한 상태
        } else {
            mUsermail = mUser.getEmail();
            nicknameCheck(mUsermail);
            if (mUser.getPhotoUrl() != null) {
                mPhotoUrl = mUser.getPhotoUrl().toString();
            }
        }

        ConnectServer connectServer = new ConnectServer();
        connectServer.execute();

        if (SplashActivity.makeMsg < 1) {
            SplashActivity.makeMsg++;
            if (mUsername != null) {
                Toast.makeText(this, MainActivity.mUsername + "님 환영합니다", Toast.LENGTH_SHORT).show();
            }
        }

        //스피너정보 가져오기
        spinList1.clear();
        getSpinner getSpinner = new getSpinner();
        getSpinner.execute();

        //favorite 가져오기
        if (mUsername != null) {
            getFavorite fav = new getFavorite();
            fav.requestPost(mUsermail);

            //      블랙리스트
            getBlackList getB = new getBlackList();
            getB.requestPost(mUsermail);
        }


//        업데이트 확인
        checkVersion();


//        클릭 리스너
        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
        btnThree.setOnClickListener(this);
        btnFour.setOnClickListener(this);
        btnFive.setOnClickListener(this);
        btnSix.setOnClickListener(this);
        hyperLinkTv.setOnClickListener(this);

    }

    private void nicknameCheck(String email) {
        GetName getName = new GetName();
        getName.requestPost(email);

    }

    private void settingName(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            String num = jsonObj.getString("num_results");
            if(num.equals("1")){
                mUsername = jsonObj.getString("username");
            } else {
                mUsername = mUser.getDisplayName();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void checkVersion() {

        mDialog = new AlertDialog.Builder(this);

        if (SplashActivity.store_version.compareTo(SplashActivity.device_version) > 0) {
            mDialog.setMessage("새로운 버전이 확인되었습니다.")
                    .setCancelable(true)
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {finish();}
                    })
                    .setPositiveButton("업데이트 바로가기",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    Intent marketLaunch = new Intent(
                                            Intent.ACTION_VIEW);
                                    marketLaunch.setData(Uri
                                            .parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                                    startActivity(marketLaunch);
                                    finish();
                                }
                            });
            AlertDialog alert = mDialog.create();
            alert.setTitle("업데이트 확인");
            alert.show();
        }
    }

    private void ADset() {

        getAd getAd = new getAd();
        getAd.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 1414) {
            //인포 액티비티에서 넘어옴
            Toast.makeText(getApplicationContext(), "업로드 완료했습니다", Toast.LENGTH_LONG).show();
        }
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

    @SuppressLint("ClickableVieswAccessi  bility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        Intent urlintent = new Intent(Intent.ACTION_VIEW, Uri.parse(adUrl.get(pagerPos)));
        startActivity(urlintent);

        return false;
    }

    @SuppressLint("StaticFieldLeak")
    class ConnectServer extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... strings) {

            Request request = new Request.Builder().url("http://spotz.co.kr/var/www/html/getImage.php").build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            } catch (IOException e) {
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
//                Log.d("heu", "서버접속 에러(메인) : " + e);
                e.printStackTrace();
            }

            return null;
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            /** 슬라이드에 적용 */
            JSONObject jsonObj = null;
            try {

                jsonObj = new JSONObject(s);

                String[] temp = jsonObj.getString("image").split(",");

                for (int i = 0; i < temp.length; i++) {
                    adList.add(new adfrag(temp[i]));
                }

                String[] temp2 = jsonObj.getString("url").split(",");

                adUrl.addAll(Arrays.asList(temp2));

                /*
                for (int i = 0; i < temp2.length; i++) {
                    adUrl.add(temp2[i]);
                }
                */

            } catch (JSONException e) {
                e.printStackTrace();
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

            } catch (JSONException e) {
                e.printStackTrace();
//                Log.d("heu", "adapter Exception : " + e);
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

    public static class getBlackList {
        //Client 생성
        OkHttpClient client = new OkHttpClient();
        String result;
        ArrayList<String> bList = new ArrayList<>();

        public void requestPost(String username) {

            RequestBody requestBody = new FormBody.Builder().
                    add("username", username).
                    build();
            String url = "http://spotz.co.kr/var/www/html/blackList.php";

            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    Log.d("heu", "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
//                    Log.d("heu", "hitupdate res : " + response.body().string());
                    result = response.body().string();

                    try {
                        JSONObject jsonObj = new JSONObject(result);
                        String[] temp = jsonObj.getString("blacklist").split(",");
                        bList.addAll(Arrays.asList(temp));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(MainActivity.mUser != null) {
                        for (int i = 0; i < bList.size(); i++) {
                            if (mUsermail.equals(bList.get(i))) {
                                FirebaseAuth.getInstance().signOut();
                            }
                        }
                    }

                }
            });
        }
    }

    private class GetName {
        //Client 생성
        OkHttpClient client = new OkHttpClient();
        String result;

        public void requestPost(String email) {

            RequestBody requestBody = new FormBody.Builder().
                    add("email", email).
                    build();
            String url = "http://spotz.co.kr/var/www/html/nameSelect.php";

            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    Log.d("heu", "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
//                    Log.d("heu", "hitupdate res : " + response.body().string());
                    result = response.body().string();
                    settingName(result);
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.btnOne):
                intent = new Intent(MainActivity.this, ClubList.class);
                intent.putExtra("listName", "kbl");
                startActivity(intent);
                break;

            case (R.id.btnTwo):
                intent = new Intent(MainActivity.this, ClubList.class);
                intent.putExtra("listName", "nba");
                startActivity(intent);
                break;

            case (R.id.btnthree):
                intent = new Intent(MainActivity.this, ClubList.class);
                intent.putExtra("listName", "equip");
                startActivity(intent);
                break;

            case (R.id.btnFour):
                intent = new Intent(MainActivity.this, ClubList.class);
                intent.putExtra("listName", "compet");
                startActivity(intent);
                break;

            case (R.id.btnFive):
                intent = new Intent(MainActivity.this, ClubList.class);
                intent.putExtra("listName", "freeboard");
                startActivity(intent);
                break;

            case (R.id.btnSix):
                intent = new Intent(MainActivity.this, ClubList.class);
                intent.putExtra("listName", "qna");
                startActivity(intent);
                break;

            case (R.id.hyperLinkTv):

                Intent urlintent = new Intent(Intent.ACTION_VIEW, Uri.parse(adUrl.get(pagerPos)));
                startActivity(urlintent);

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
                startActivityForResult(intent,10);
                break;

            case (R.id.bottomMember):
                if (mUser == null) {
                    Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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
        handler.sendEmptyMessageDelayed(0, 3500);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
//            Log.d("heu", "State IDLE");
            if (!adUrl.get(pagerPos).equals("")) {
                hyperLinkTv.setVisibility(View.VISIBLE);
                handler.sendEmptyMessageDelayed(0, 3500);
            }

        } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
//            Log.d("heu", "State Draging");
            hyperLinkTv.setVisibility(View.GONE);
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
//        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}

