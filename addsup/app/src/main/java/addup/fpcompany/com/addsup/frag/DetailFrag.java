package addup.fpcompany.com.addsup.frag;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import addup.fpcompany.com.addsup.R;

/**
 * Created by song02 on 2018-03-12.
 */

@SuppressLint("ValidFragment")
public class DetailFrag extends Fragment {

    String imgsrc;
    ImageView imgView;

    public DetailFrag(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.detail_lay, container, false);

        imgView = (ImageView) layout.findViewById(R.id.imageV);
        Glide.with(this).load(imgsrc).into(imgView);

        return layout;
    }
}
