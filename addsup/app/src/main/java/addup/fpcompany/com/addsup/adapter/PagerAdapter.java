package addup.fpcompany.com.addsup.adapter;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
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
    public android.support.v4.app.Fragment getItem(int position) {

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
