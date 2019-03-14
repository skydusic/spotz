package addup.fpcompany.com.addsup.adapter;

import android.content.Context;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import addup.fpcompany.com.addsup.MainActivity;
import addup.fpcompany.com.addsup.R;
import addup.fpcompany.com.addsup.java.noticeItem;

/**
 * Created by song02 on 2018-03-17.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    Context context;
    ArrayList<noticeItem> itemArr;
    noticeItem listItem;
    String url = MainActivity.serverUrl + "notice/";
    private static Typeface mTypeface;

    public NoticeAdapter(Context context, ArrayList<noticeItem> itemArr) {
        this.context = context;
        this.itemArr = itemArr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notice_item, parent, false);
        if (mTypeface == null)
            mTypeface = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/dohyeonttf.ttf");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoticeAdapter.ViewHolder holder, int position) {
        listItem = itemArr.get(position);
        holder.idxTv.setText(listItem.getIdx());
        holder.title.setText(listItem.getTitle());
        holder.created.setText(listItem.getCreated());

        /*if (!listItem.getImage().equals("")) {
            String[] temp = listItem.getImage().split(",");
            Glide.with(context).load(url + listItem.getIdx() + "/" + temp[0]).into(holder.imageViewHolder);
        } else {
            holder.imageViewHolder.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        return itemArr.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView idxTv, title, created;

        public ViewHolder(View itemView) {
            super(itemView);
            idxTv = itemView.findViewById(R.id.idxTv);
            title = itemView.findViewById(R.id.titleTv);
            created = itemView.findViewById(R.id.created);
            idxTv.setTypeface(mTypeface);
            title.setTypeface(mTypeface);
            created.setTypeface(mTypeface);
        }
    }
}
