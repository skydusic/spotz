package addup.fpcompany.com.addsup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class infoActivity extends AppCompatActivity implements View.OnClickListener{

    Button insertBtn;
    EditText titleET;
    EditText contentsET;

    String TAG = "heu";
    String myJSON = "";
    String url = "";
    String username = "익명";

    Intent intent;

    LinearLayout infoLay;
    LinearLayout reportLay;

    TextView reportTitleET;
    TextView reportContentsET;
    TextView reportET;

    String listname = "";
    String idx = "";
    String title = "";
    String contents = "";
    String writer = "";
    String repoter = "";
    String created = "";
    String image = "";
    String flag = "info";
    String report = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        insertBtn = findViewById(R.id.insertBtn);
        titleET = findViewById(R.id.titleET);
        contentsET = findViewById(R.id.contentsET);
        infoLay = findViewById(R.id.infoLay);
        reportLay = findViewById(R.id.reportLay);
        reportTitleET = findViewById(R.id.reportTitleET);
        reportContentsET = findViewById(R.id.reportContentsET);
        reportET = findViewById(R.id.reportEt);

        Intent infoIntent = getIntent();
        if(infoIntent.getStringExtra("flag") != null){
            flag = infoIntent.getStringExtra("flag");
        }
        listname = infoIntent.getStringExtra("listname");
        idx = infoIntent.getStringExtra("idx");
        title = infoIntent.getStringExtra("title");
        contents = infoIntent.getStringExtra("contents");
        writer = infoIntent.getStringExtra("writer");
        repoter = infoIntent.getStringExtra("repoter");
        created = infoIntent.getStringExtra("created");
        image = infoIntent.getStringExtra("image");

        // 인포액티비티 설정부
        if (flag.equals("report")){
            infoLay.setVisibility(View.INVISIBLE);
            reportLay.setVisibility(View.VISIBLE);
            reportTitleET.setText(title);
            reportContentsET.setText(contents);
            url = "http://spotz.co.kr/var/www/html/reportinsert.php";
        } else if(flag.equals("info")){
            infoLay.setVisibility(View.VISIBLE);
            reportLay.setVisibility(View.INVISIBLE);
            url = "http://spotz.co.kr/var/www/html/infoinsert.php";
        }

            insertBtn.setOnClickListener(this);

        if(MainActivity.mUsername != null)
            username = MainActivity.mUsername;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.insertBtn):
                if (flag.equals("report")){
                    title = reportTitleET.getText().toString().trim();
                    contents = reportContentsET.getText().toString().trim();
                    report = reportET.getText().toString().trim();
                } else if(flag.equals("info")){
                    title = titleET.getText().toString().trim();
                    contents = contentsET.getText().toString().trim();
                }

                Log.d("heu", "Title : " + title);
                Log.d("heu", "contents : " + contents);
                Log.d("heu", "report : " + report);

                if(contents.length() > 300) {
                    Toast.makeText( infoActivity.this, "글자 제한을 초과했습니다. (" + String.valueOf(contents.length()) + " / 300자)", Toast.LENGTH_LONG).show();
                } else {
                    writeInfo writeInfo = new writeInfo();
                    writeInfo.requestPost(url, title, contents, username, report);
                    finish();
                }
                break;

            case (R.id.bottomHome):
                intent = new Intent(infoActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case (R.id.bottomNotice):
                intent = new Intent(infoActivity.this, Notice_Activity.class);
                startActivity(intent);
                break;

            case (R.id.bottomInfo):
                intent = new Intent(infoActivity.this, infoActivity.class);
                startActivity(intent);
                break;

            case (R.id.bottomMember):
                if (MainActivity.mUser == null) {
                    Intent intent = new Intent(infoActivity.this, SignInActivity.class);
                    startActivityForResult(intent, 10);
                } else {
                    Intent intent = new Intent(infoActivity.this, myPageActivity.class);
                    startActivityForResult(intent, 1000);
                }
                break;
        }
    }

    class writeInfo {
        OkHttpClient client = new OkHttpClient();

        public void requestPost(String url, String title , String contents, String username, String report) {


            Log.d("heu", "URL : " + url);

            RequestBody requestBody = new FormBody.Builder().
                    add("idx", idx).
                    add("listname", listname).
                    add("title", title).
                    add("contents", contents).
                    add("username", username).
                    add("report", report).
                    add("writer", writer).
                    add("created", created).
                    add("image", image).
                    build();

            Request request = new Request.Builder().url(url).post(requestBody).build();
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
}
