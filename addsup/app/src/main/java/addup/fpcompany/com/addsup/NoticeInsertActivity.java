package addup.fpcompany.com.addsup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
    String url = "http://spotz.co.kr/var/www/html/noticeinsert.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_insert);

        titleEt = findViewById(R.id.titleEt);
        contentsEt = findViewById(R.id.contentsEt);
        insertBtn = findViewById(R.id.insertBtn);
        insertBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String title = titleEt.getText().toString().trim();
        String contents = contentsEt.getText().toString().trim();

        writeNot writeNot = new writeNot();
        writeNot.requestPost(url, title, contents);
        finish();
    }

    class writeNot {
        OkHttpClient client = new OkHttpClient();

        public void requestPost(String url, String title, String contents) {

            RequestBody requestBody = new FormBody.Builder().
                    add("title", title).
                    add("contents", contents).
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
