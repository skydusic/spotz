package addup.fpcompany.com.addsup.java;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BaseActivity extends AppCompatActivity {

    private static Typeface mTypeface;

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (BaseActivity.mTypeface == null)
            BaseActivity.mTypeface = Typeface.createFromAsset(getAssets(), "fonts/SDSwaggerTTF.ttf");

        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        setGlobalFont(root);
    }

    void setGlobalFont(ViewGroup root) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            if (child instanceof TextView) {
                // 폰트 세팅
                ((TextView) child).setTypeface(mTypeface);

                // 자간 조절
                ((TextView) child).setLineSpacing(10, 1);

                // 크기 조절
                ((TextView) child).setTextSize(16);

            } else if (child instanceof ViewGroup)
                setGlobalFont((ViewGroup) child);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

}
