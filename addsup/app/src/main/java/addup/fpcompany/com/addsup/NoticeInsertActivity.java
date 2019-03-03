package addup.fpcompany.com.addsup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NoticeInsertActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "heu";
    String myJSON = "";

    EditText titleEt;
    EditText contentsEt;
    TextView insertBtn;
    String url = "";
    RadioButton commonNotice, topNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_insert);

        titleEt = findViewById(R.id.titleEt);
        contentsEt = findViewById(R.id.contentsEt);
        insertBtn = findViewById(R.id.insertBtn);
        commonNotice = findViewById(R.id.commonNotice);
        topNotice = findViewById(R.id.topNotice);

        RadioButton.OnClickListener optionOnClickListener
                = new RadioButton.OnClickListener() {

            public void onClick(View v) {

                if (commonNotice.isChecked()) {
                    url = "http://spotz.co.kr/var/www/html/noticeinsert.php";
                } else if (topNotice.isChecked()) {
                    url = "http://spotz.co.kr/var/www/html/topnoticeinsert.php";
                }

                Toast.makeText(
                        NoticeInsertActivity.this,
                        "URL : " + url,
                        Toast.LENGTH_LONG).show();

            }
        };

        insertBtn.setOnClickListener(this);
        commonNotice.setOnClickListener(optionOnClickListener);
        topNotice.setOnClickListener(optionOnClickListener);
    }

    @Override
    public void onClick(View v) {
        if(!commonNotice.isChecked() && !topNotice.isChecked()){
            Toast.makeText(NoticeInsertActivity.this, "라디오버튼을 체크하세요!",Toast.LENGTH_LONG).show();
        } else {

            String title = titleEt.getText().toString().trim();
            String contents = contentsEt.getText().toString().trim();
            String image = "";

            writeNot writeNot = new writeNot();
            writeNot.requestPost(url, title, contents, image);
            finish();
        }
    }

    class writeNot {
        OkHttpClient client = new OkHttpClient();

        public void requestPost(String url, String title, String contents, String image) {

            RequestBody requestBody = new FormBody.Builder().
                    add("title", title).
                    add("contents", contents).
                    add("image", image).
                    build();

            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    Log.d(TAG, "Connect Server Error is " + e.toString());

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    myJSON = response.body().string();
                }
            });

        }
    }

}
