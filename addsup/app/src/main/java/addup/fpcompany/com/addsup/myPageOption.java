package addup.fpcompany.com.addsup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class myPageOption extends Activity implements View.OnClickListener {

    TextView upBtn;
    TextView superUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page_option);

        upBtn = findViewById(R.id.upBtn);
        superUpBtn = findViewById(R.id.superUpBtn);

        upBtn.setOnClickListener(this);
        superUpBtn.setOnClickListener(this);

    }

    Intent intent = new Intent();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.upBtn):
                setResult(100, intent);
                break;
            case (R.id.superUpBtn):
                setResult(200, intent);
                break;
        }
        finish();
    }
}
