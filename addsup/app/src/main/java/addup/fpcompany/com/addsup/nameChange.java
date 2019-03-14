package addup.fpcompany.com.addsup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class nameChange extends AppCompatActivity implements View.OnClickListener{

    String email, username = "";
    TextView emailTv;
    EditText usernameEt;
    Button confirmBt;
    Intent intent;
    String Json;

    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_change);

        emailTv = findViewById(R.id.emailTv);
        usernameEt = findViewById(R.id.usernameEt);
        confirmBt = findViewById(R.id.confirmBt);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        intent = getIntent();
        emailTv.setText(intent.getStringExtra("email"));
        usernameEt.setText(intent.getStringExtra("username"));

        confirmBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();
        switch (v.getId()) {
            case (R.id.confirmBt):
                username = usernameEt.getText().toString();
                email = emailTv.getText().toString();
                usernameEt.clearFocus();
                MainActivity.mUsername = username;
                MainActivity.mUsermail = email;
                ChangeUsername changeUsername = new ChangeUsername();
                changeUsername.requestPost(email, username);
                break;
        }
    }

    @SuppressLint("ShowToast")
    private void result(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            String result = jsonObj.getString("result");
            if(result.equals("성공")){
                Intent mainIntent = new Intent(nameChange.this, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Toast.makeText(nameChange.this,"완료되었습니다", Toast.LENGTH_SHORT).show();
                startActivity(mainIntent);
                this.finish();
            } else if (result.equals("실패")) {
                Toast.makeText(nameChange.this,"동일한 닉네임이 있습니다", Toast.LENGTH_SHORT).show();
                usernameEt.setText("");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void hideKeyboard() {
        imm.hideSoftInputFromWindow(usernameEt.getWindowToken(), 0);
    }

    class ChangeUsername {
        //Client 생성
        OkHttpClient client = new OkHttpClient();

        public void requestPost(final String email, final String username) {

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
                    Json = response.body().string();
                    handler.sendEmptyMessage(0);
                }
            });
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            result(Json);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
    }
}
