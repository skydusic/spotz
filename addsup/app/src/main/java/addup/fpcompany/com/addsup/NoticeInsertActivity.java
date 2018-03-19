package addup.fpcompany.com.addsup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    String TAG = "NoticeInsertActivity";
    String myJSON = "";

    EditText titleEt;
    EditText contentsEt;
    TextView insertBtn;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_insert);

        titleEt = findViewById(R.id.titleET);
        contentsEt = findViewById(R.id.contentsET);
        insertBtn = findViewById(R.id.insertBtn);
        insertBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        writeNot writeNot = new writeNot();
        writeNot.requestPost(url);
    }

    class writeNot {
        OkHttpClient client = new OkHttpClient();
        Request request;

        public void requestPost(String url) {

            RequestBody requestBody = new FormBody.Builder().
                    add("title", titleEt.getText().toString()).
                    add("contents", contentsEt.getText().toString()).
                    build();

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

}
