package addup.fpcompany.com.addsup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    String url = "http://spotz.co.kr/var/www/html/infoInsert.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        insertBtn = findViewById(R.id.insertBtn);
        titleET = findViewById(R.id.titleET);
        contentsET = findViewById(R.id.contentsET);
        insertBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.insertBtn):
                writeInfo writeInfo = new writeInfo();
                writeInfo.requestPost(url);
                break;
        }
    }

    class writeInfo {
        OkHttpClient client = new OkHttpClient();

        public void requestPost(String url) {

            String title = titleET.getText().toString().trim();
            String contents = contentsET.getText().toString().trim();

            RequestBody requestBody = new FormBody.Builder().
                    add("title", title).
                    add("contents", contents).
                    add("username", MainActivity.mUsername).
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
