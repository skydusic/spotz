package addup.fpcompany.com.addsup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class nameChange extends AppCompatActivity implements View.OnClickListener{

    String email, username = "";
    TextView emailTv;
    EditText usernameEt;
    Button confirmBt;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_change);

        emailTv = findViewById(R.id.emailTv);
        usernameEt = findViewById(R.id.usernameEt);
        confirmBt = findViewById(R.id.confirmBt);

        intent = getIntent();
        emailTv.setText(intent.getStringExtra("email"));
        usernameEt.setText(intent.getStringExtra("username"));


        confirmBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.confirmBt):
                username = usernameEt.getText().toString();
                email = emailTv.getText().toString();
                ChangeUsername changeUsername = new ChangeUsername();
                changeUsername.requestPost(email, username);

                Intent mainIntent = new Intent(nameChange.this, MainActivity.class);
                MainActivity.mUsername = username;
                MainActivity.mUsermail = email;
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);

                break;
        }
    }


    class ChangeUsername {
        //Client 생성
        OkHttpClient client = new OkHttpClient();

        public void requestPost(String email, String username) {

            String url = "http://spotz.co.kr/var/www/html/nameUpdate.php";
            //Request Body에 서버에 보낼 데이터 작성
            RequestBody requestBody = new FormBody.Builder().
                    add("email", email).
                    add("username", username).
                    build();

            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    Log.d("heu", "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
//                    Json = response.body().string();

                }
            });
        }
    }
}
