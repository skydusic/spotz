package addup.fpcompany.com.addsup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class DetailList extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    Intent intent;

    TextView detailNameTv;
    TextView midDetailTv1;
    TextView midDetailTv2;
    TextView midDetailTv3;
    TextView midDetailTv4;
    TextView contentsTv;
    ViewPager viewPager;
    ArrayList<String> arr = new ArrayList<>();
    ArrayList<Fragment> fragArr = new ArrayList<>();

    String idx = "";
    String title = "";
    String contents = "";
    String created = "";
    String image = "";
    String listname = "";

    String url = MainActivity.serverUrl + "userImageFolder/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list);

        detailNameTv = findViewById(R.id.detailNameTv);
        midDetailTv1 = findViewById(R.id.midDetailTv1);
        midDetailTv2 = findViewById(R.id.midDetailTv2);
        midDetailTv3 = findViewById(R.id.midDetailTv3);
        midDetailTv4 = findViewById(R.id.midDetailTv4);
        contentsTv = findViewById(R.id.contents);
        viewPager = findViewById(R.id.viewPager);


        // 인텐트로 정보 가져옴
        intent = getIntent();
        idx = intent.getStringExtra("idx");
        title = intent.getStringExtra("title");
        contents = intent.getStringExtra("contents");
        created = intent.getStringExtra("created");
        listname = intent.getStringExtra("listname");
        detailNameTv.setText(idx + " : " + title);
        contentsTv.setText(contents);

        image = intent.getStringExtra("image");

        // 이미지 세팅
        setRecyclerView();
        // 이미지 페이지 넘기는 핸들러
        handler.sendEmptyMessageDelayed(0, 2000);

        hitUpdate hitUpdate = new hitUpdate();
        hitUpdate.requestPost();

    }

    private void setRecyclerView() {
        if (!image.equals("")) {
            String[] temp = image.split(",");
            for (int i = 0; i < temp.length; i++) {
                arr.add(url + temp[i]);
            }
        }

        for (int i = 0; i < arr.size(); i++) {
            fragArr.add(new DetailFrag(arr.get(i)));
        }
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), fragArr);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(DetailList.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.bottomHome):
                Intent intent = new Intent(DetailList.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case (R.id.bottomMember):
                if (MainActivity.mUser == null) {
                    Intent intent1 = new Intent(DetailList.this, SignInActivity.class);
                    finish();
                    startActivityForResult(intent1, 10);
                } else {
                    Intent intent2 = new Intent(DetailList.this, myPageActivity.class);
                    finish();
                    startActivityForResult(intent2, 1000);
                }
                break;
        }
    }


    int pagerPos = 0;
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        pagerPos = position;
        handler.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
//            Log.d("heu", "State IDLE");
            handler.sendEmptyMessageDelayed(0, 2000);

        } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
//            Log.d("heu", "State Draging");
            handler.removeMessages(0);

        } else if (state == ViewPager.SCROLL_STATE_SETTLING) {

//            Log.d("heu", "State Settling");
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler.removeMessages(0);
            pagerPos++;

            if (pagerPos >= arr.size()) {
                pagerPos = 0;
                viewPager.setCurrentItem(pagerPos, false);
            } else {
                viewPager.setCurrentItem(pagerPos);
            }
        }
    };

    class hitUpdate {
        //Client 생성
        OkHttpClient client = new OkHttpClient();

        public void requestPost() {

            //Request Body에 서버에 보낼 데이터 작성
            RequestBody requestBody = new FormBody.Builder().
                    add("listname", listname).
                    add("idx", idx).
                    build();
            String url = "http://spotz.co.kr/var/www/html/hitupdate.php";

            Log.d("heu", "리스트네임 : " + listname);

            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("heu", "Connect Server Error is " + e.toString());
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d("heu", "hitupdate res : " + response.body().string());
                }
            });
        }
    }

}
