package addup.fpcompany.com.addsup.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import addup.fpcompany.com.addsup.MainActivity;
import addup.fpcompany.com.addsup.R;
import addup.fpcompany.com.addsup.java.listItem;

/**
 * Created by Administrator on 2017-12-05.
 */


public class MainListAdater extends RecyclerView.Adapter<MainListAdater.ViewHolder> {


    Context context;
    ArrayList<listItem> itemArr;
    listItem listItem;
    String url = MainActivity.serverUrl + "userImageFolder/";
    private static Typeface mTypeface;

    public MainListAdater(Context context, ArrayList<listItem> itemArr) {
        this.context = context;
        this.itemArr = itemArr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/dohyeonttf.ttf");
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        listItem = itemArr.get(position);
        holder.text1.setText(listItem.getText1());
        holder.text2.setText(listItem.getText2());
        holder.text3.setText(listItem.getText3());
        holder.text4.setText(listItem.getText4());
        holder.text5.setText(listItem.getText5());
        holder.timeHolder.setText(listItem.getCreated());
        holder.hit.setText(listItem.getHit());

        if (!listItem.getImage().equals("")) {
            String[] temp = listItem.getImage().split(",");
            if (!temp.equals("")) {
                Glide.with(context).load(url + temp[0]).into(holder.imageViewHolder);
            } else {

            }
        } else {
            Glide.with(context).load(url + "basic_image.png").into(holder.imageViewHolder);
        }
    }

    @Override
    public int getItemCount() {
        return itemArr.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1,text2,text3,text4,text5,timeHolder, hit;
        ImageView imageViewHolder;

        public ViewHolder(View itemView) {
            super(itemView);
            timeHolder = itemView.findViewById(R.id.created);
            imageViewHolder = itemView.findViewById(R.id.imageView);
            hit = itemView.findViewById(R.id.hit);
            text1 = itemView.findViewById(R.id.text1);
            text2 = itemView.findViewById(R.id.text2);
            text3 = itemView.findViewById(R.id.text3);
            text4 = itemView.findViewById(R.id.text4);
            text5 = itemView.findViewById(R.id.text5);

            timeHolder.setTypeface(mTypeface);
            hit.setTypeface(mTypeface);
            text1.setTypeface(mTypeface);
            text2.setTypeface(mTypeface);
            text3.setTypeface(mTypeface);
            text4.setTypeface(mTypeface);
            text5.setTypeface(mTypeface);
        }
    }
}

