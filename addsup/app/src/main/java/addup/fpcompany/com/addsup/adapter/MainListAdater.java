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
            mTypeface = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/swaggerttf.ttf");
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        listItem = itemArr.get(position);
        holder.titleHolder.setText(listItem.getTitle());
        holder.timeHolder.setText(listItem.getCreated());
        holder.location.setText("주소: "+listItem.getLocation());
        holder.phone.setText("연락처: "+listItem.getPhone());
        holder.hit.setText("조회수: "+listItem.getHit());

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
        TextView titleHolder, timeHolder, location, phone, hit;
        ImageView imageViewHolder;

        public ViewHolder(View itemView) {
            super(itemView);
            titleHolder = itemView.findViewById(R.id.title);
            timeHolder = itemView.findViewById(R.id.created);
            imageViewHolder = itemView.findViewById(R.id.imageView);
            location = itemView.findViewById(R.id.location);
            phone = itemView.findViewById(R.id.phone);
            hit = itemView.findViewById(R.id.hit);

            titleHolder.setTypeface(mTypeface);
            timeHolder.setTypeface(mTypeface);
            location.setTypeface(mTypeface);
            phone.setTypeface(mTypeface);
            hit.setTypeface(mTypeface);
        }
    }
}

