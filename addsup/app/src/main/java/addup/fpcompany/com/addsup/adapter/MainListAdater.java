package addup.fpcompany.com.addsup.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import addup.fpcompany.com.addsup.R;
import addup.fpcompany.com.addsup.java.listItem;

/**
 * Created by Administrator on 2017-12-05.
 */


public class MainListAdater extends RecyclerView.Adapter<MainListAdater.ViewHolder> {


    Context context;
    ArrayList<listItem> itemArr;
    listItem listItem;
    private static Typeface mTypeface;

    public MainListAdater(Context context, ArrayList<listItem> itemArr) {
        this.context = context;
        this.itemArr = itemArr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        if (mTypeface == null)
            mTypeface = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/dohyeonttf.ttf");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        listItem = itemArr.get(position);

        holder.title.setText(listItem.getTitle());
        holder.timeHolder.setText(listItem.getCreated());
        holder.hit.setText(listItem.getHit());


        if(!listItem.getImage().equals("")){
            holder.imageViewHolder.setVisibility(View.VISIBLE);
        }

        /*listItem = itemArr.get(position);

        String url = MainActivity.serverUrl + "userImageFolder/" + listItem.getListname() + "/" + listItem.getUsername() + "/";

        if (!listItem.getImage().equals("")) {
            String[] temp = listItem.getImage().split(",");
            if (!temp.equals("")) {
                Glide.with(context).load(url + temp[0]).into(holder.imageViewHolder);
            } else {
                Glide.with(context).load(R.drawable.emptyimg).into(holder.imageViewHolder);
            }
        } else {
            Glide.with(context).load(R.drawable.emptyimg).into(holder.imageViewHolder);
        }*/
    }

    @Override
    public int getItemCount() {
        return itemArr.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,timeHolder, hit;
        ImageView imageViewHolder;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.listTitle);
            timeHolder = itemView.findViewById(R.id.created);
            hit = itemView.findViewById(R.id.hit);
            imageViewHolder = itemView.findViewById(R.id.areImage);

            title.setTypeface(mTypeface);
            timeHolder.setTypeface(mTypeface);
            hit.setTypeface(mTypeface);
        }
    }
}

