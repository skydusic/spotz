package addup.fpcompany.com.addsup;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class myPageOption extends Activity implements View.OnClickListener {

    TextView upBtn;
    TextView superUpBtn;
    TextView editpost;
    TextView delpost;
    Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 상단 바 없애는 코드임
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_page_option);
//        mTypeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/dohyeonttf.ttf");

        upBtn = findViewById(R.id.upBtn);
        superUpBtn = findViewById(R.id.superUpBtn);
        editpost = findViewById(R.id.editpost);
        delpost = findViewById(R.id.delpost);





        upBtn.setOnClickListener(this);
        superUpBtn.setOnClickListener(this);
        editpost.setOnClickListener(this);
        delpost.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.upBtn):
                setResult(100);
                break;
            case (R.id.superUpBtn):
                setResult(200);
                break;
            case (R.id.editpost):
                setResult(300);
                break;
            case (R.id.delpost):
                setResult(400);
                break;
        }
        finish();
    }
}
