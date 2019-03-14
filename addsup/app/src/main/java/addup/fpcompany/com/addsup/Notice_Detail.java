package addup.fpcompany.com.addsup;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import addup.fpcompany.com.addsup.adapter.PagerAdapter;
import addup.fpcompany.com.addsup.frag.DetailFrag;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Notice_Detail extends AppCompatActivity implements View.OnClickListener {

    ViewPager viewPager;
    TextView idxTv, noticeTv, createdTv, contentsTV;
    Intent intent;
    String listname, idx, title, contents, created, image = "";
    String url = MainActivity.serverUrl + "notice/";
    String[] imageArr;
    ArrayList<Fragment> fragArr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        viewPager = findViewById(R.id.viewPager);
        idxTv = findViewById(R.id.idxTv);
        noticeTv = findViewById(R.id.noticeTv);
        createdTv = findViewById(R.id.createdTv);
        contentsTV = findViewById(R.id.contentsTv);

        intent = getIntent();
        listname = intent.getStringExtra("listname");
        idx = intent.getStringExtra("idx");
        title = intent.getStringExtra("title");
        contents = intent.getStringExtra("contents");
        created = intent.getStringExtra("created");
        image = intent.getStringExtra("image");

        idxTv.setText(idx);
        noticeTv.setText(title);
        createdTv.setText(created);
        contentsTV.setText(contents);

        setView();
        hitUpdate hitUpdate = new hitUpdate();
        hitUpdate.requestPost(listname);
    }

    private void setView() {
        if(image.equals("")){
            contentsTV.setVisibility(View.VISIBLE);
        } else {
            viewPager.setVisibility(View.VISIBLE);
            imageArr = image.split(",");
            for (int i = 0; i < imageArr.length; i++) {
                fragArr.add(new DetailFrag(url + "/" + String.valueOf(idx) + "/" + imageArr[i]));
            }
            PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), fragArr);
            viewPager.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {

    }

    class hitUpdate {
        //Client 생성
        OkHttpClient client = new OkHttpClient();

        public void requestPost(String listname) {
            //Request Body에 서버에 보낼 데이터 작성
            RequestBody requestBody = new FormBody.Builder().
                    add("listname", listname).
                    add("idx", idx).
                    build();
            String url = "http://spotz.co.kr/var/www/html/hitupdate.php";

            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    Log.d("heu", "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
//                    Log.d("heu", "hitupdate res : " + response.body().string());
                }
            });
        }
    }

}
