package addup.fpcompany.com.addsup;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class myPageOption extends Activity implements View.OnClickListener {

    TextView editpost;
    TextView delpost;
    TextView movepost;
    Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 상단 바 없애는 코드임
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_page_option);
        mTypeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/dohyeonttf.ttf");

        editpost = findViewById(R.id.editpost);
        delpost = findViewById(R.id.delpost);
        movepost = findViewById(R.id.movepost);

        editpost.setTypeface(mTypeface);
        delpost.setTypeface(mTypeface);
        movepost.setTypeface(mTypeface);

        editpost.setOnClickListener(this);
        delpost.setOnClickListener(this);
        movepost.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.editpost):
                setResult(300);
                break;
            case (R.id.delpost):
                setResult(400);
                break;
            case (R.id.movepost):
                setResult(500);
                break;
        }
        finish();
    }
}
