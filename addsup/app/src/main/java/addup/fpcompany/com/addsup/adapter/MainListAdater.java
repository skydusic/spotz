package addup.fpcompany.com.addsup.adapter;

import android.content.Context;
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
import addup.fpcompany.com.addsup.listItem;

/**
 * Created by Administrator on 2017-12-05.
 */


public class MainListAdater extends RecyclerView.Adapter<MainListAdater.ViewHolder> {


    Context context;
    ArrayList<listItem> itemArr;
    listItem listItem;
    String url = MainActivity.serverUrl + "userImageFolder/";

    public MainListAdater(Context context, ArrayList<listItem> itemArr) {
        this.context = context;
        this.itemArr = itemArr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        listItem = itemArr.get(position);
        holder.idHolder.setText(listItem.getIdx());
        holder.titleHolder.setText(listItem.getTitle());
        holder.timeHolder.setText(listItem.getCreated());
        if (!listItem.getImage().equals("")) {
            String[] temp = listItem.getImage().split(",");
            Glide.with(context).load(url + temp[0]).into(holder.imageViewHolder);
        }
    }

    @Override
    public int getItemCount() {
        return itemArr.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView idHolder, titleHolder, timeHolder;
        ImageView imageViewHolder;

        public ViewHolder(View itemView) {
            super(itemView);
            idHolder = itemView.findViewById(R.id.idx);
            titleHolder = itemView.findViewById(R.id.title);
            timeHolder = itemView.findViewById(R.id.created);
            imageViewHolder = itemView.findViewById(R.id.imageView);

        }
    }
}

