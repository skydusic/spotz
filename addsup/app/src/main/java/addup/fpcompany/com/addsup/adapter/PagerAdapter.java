package addup.fpcompany.com.addsup.adapter;

import android.graphics.Typeface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-11-27.
 */

public class PagerAdapter extends FragmentStatePagerAdapter implements View.OnClickListener{

    ArrayList<Fragment> dataArr;
    private static Typeface mTypeface;

    public PagerAdapter(FragmentManager fm, ArrayList<Fragment> dataArr) {
        super(fm);
        this.dataArr = dataArr;
    }

    @Override
    public Fragment getItem(int position) {

        return dataArr.get(position);

    }

    @Override
    public int getCount() {
        return dataArr.size();
    }

    @Override
    public void onClick(View v) {

    }
}
