package addup.fpcompany.com.addsup.java;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BaseFragment extends Fragment {
    protected static Typeface mTypeface;

    protected void setGlobalFont(ViewGroup root) {
        if (BaseFragment.mTypeface == null)
            BaseFragment.mTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SDSwaggerTTF.ttf");

        for (int i = 0; i < root.getChildCount(); i++) {

            View child = root.getChildAt(i);

            if (child instanceof TextView)
                ((TextView)child).setTypeface(mTypeface);
            else if (child instanceof ViewGroup)
                setGlobalFont((ViewGroup)child);
        }
    }
}
