package addup.fpcompany.com.addsup;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 10;

    GoogleSignInAccount account;
    static String store_version = "";
    static String device_version = "";
    int device_versionCode;
    static int makeMsg = 0;

    Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.splash_act);

        intent = new Intent(this, MainActivity.class);

        //푸시키 받기, 저장된 값이 있는가

        //앱을 클릭했냐, 푸시키를 클릭했나
        //splash -> 인트로 -> 메인, splash -> 상세이동

        //splash -> 저장된 아이디와 비밀번호 -> 자동로그인 or 로그인 입력화면

        //버전 체크
        //스토어버전
//        store_version = MarketVersionChecker.getMarketVersion(getPackageName());

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

class MarketVersionChecker {

    public static String getMarketVersion(String packageName) {
        try {
            Document doc = Jsoup.connect(
                    "https://play.google.com/store/apps/details?id="
                            + packageName).get();
            Elements Version = doc.select(".content");

            for (Element mElement : Version) {
                if (mElement.attr("itemprop").equals("softwareVersion")) {
                    return mElement.text().trim();
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String getMarketVersionFast(String packageName) {
        String mData = "", mVer = null;

        try {
            URL mUrl = new URL("https://play.google.com/store/apps/details?id="
                    + packageName);
            HttpURLConnection mConnection = (HttpURLConnection) mUrl
                    .openConnection();

            if (mConnection == null)
                return null;

            mConnection.setConnectTimeout(5000);
            mConnection.setUseCaches(false);
            mConnection.setDoOutput(true);

            if (mConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader mReader = new BufferedReader(
                        new InputStreamReader(mConnection.getInputStream()));

                while (true) {
                    String line = mReader.readLine();
                    if (line == null)
                        break;
                    mData += line;
                }

                mReader.close();
            }

            mConnection.disconnect();

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        String startToken = "softwareVersion\">";
        String endToken = "<";
        int index = mData.indexOf(startToken);

        if (index == -1) {
            mVer = null;

        } else {
            mVer = mData.substring(index + startToken.length(), index
                    + startToken.length() + 100);
            mVer = mVer.substring(0, mVer.indexOf(endToken)).trim();
        }

        return mVer;
    }
}

