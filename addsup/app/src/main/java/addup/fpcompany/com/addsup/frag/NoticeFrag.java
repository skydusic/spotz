package addup.fpcompany.com.addsup.frag;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import addup.fpcompany.com.addsup.R;

/**
 * Created by song02 on 2018-03-18.
 */

@SuppressLint("ValidFragment")
public class NoticeFrag extends Fragment {

    String imgsrc;
    ImageView imgView;

    public NoticeFrag(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.detail_lay, container, false);
        imgView = layout.findViewById(R.id.imageV);
        imgView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Glide.with(this).load(imgsrc).into(imgView);
        return layout;
    }
}
