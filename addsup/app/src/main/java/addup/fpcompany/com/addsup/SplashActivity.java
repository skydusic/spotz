package addup.fpcompany.com.addsup;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static addup.fpcompany.com.addsup.SplashActivity.store_version;
import static addup.fpcompany.com.addsup.SplashActivity.store_versionCode;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 10;

    GoogleSignInAccount account;
    static String store_version = "";
    static int store_versionCode;
    static String device_version = "";
    static int device_versionCode;
    static int makeMsg = 0;
    static String pac;

    Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.splash_act);

        intent = new Intent(this, MainActivity.class);
        pac = getPackageName();
        //푸시키 받기, 저장된 값이 있는가

        //앱을 클릭했냐, 푸시키를 클릭했나
        //splash -> 인트로 -> 메인, splash -> 상세이동

        //splash -> 저장된 아이디와 비밀번호 -> 자동로그인 or 로그인 입력화면

        //버전 체크
        //스토어버전
        GetVersion getVersion = new GetVersion();
        getVersion.requestPost();


        //설치된 앱버전
        try {
            device_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            device_versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        startMain();
    }

    private void startMain() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 450);
    }

}

/*
@SuppressLint("StaticFieldLeak")
class ConnectServer extends AsyncTask<String, Void, String> {

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
}*/

class GetVersion {
    //Client 생성
    OkHttpClient client = new OkHttpClient();
    String result;
    String url = "http://spotz.co.kr/var/www/html/version_check.php";
    String ver = "version";
    String verCode = "code";

    public void requestPost() {

        RequestBody requestBody = new FormBody.Builder().build();

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
                    store_version = new JSONObject(result).getString(ver);
                    store_versionCode = Integer.parseInt(new JSONObject(result).getString(verCode));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}