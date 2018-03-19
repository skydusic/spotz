package addup.fpcompany.com.addsup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import addup.fpcompany.com.addsup.adapter.PagerAdapter;
import addup.fpcompany.com.addsup.frag.DetailFrag;

public class Notice_Detail extends AppCompatActivity implements View.OnClickListener {

    ViewPager viewPager;
    TextView idxTv;
    TextView noticeTv;
    TextView createdTv;
    TextView contentsTV;


    Intent intent;
    String idx = "";
    String title = "";
    String contents = "";
    String created = "";
    String image = "";
    String[] imageArr;
    ArrayList<Fragment> fragArr = new ArrayList<>();

    String url = MainActivity.serverUrl + "notice/";

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
}
