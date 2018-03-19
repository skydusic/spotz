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
import addup.fpcompany.com.addsup.noticeItem;

/**
 * Created by song02 on 2018-03-17.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    Context context;
    ArrayList<noticeItem> itemArr;
    noticeItem listItem;
    String url = MainActivity.serverUrl + "notice/";

    public NoticeAdapter(Context context, ArrayList<noticeItem> itemArr) {
        this.context = context;
        this.itemArr = itemArr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoticeAdapter.ViewHolder holder, int position) {
        listItem = itemArr.get(position);
        holder.idHolder.setText(listItem.getIdx());
        holder.titleHolder.setText(listItem.getTitle());
        holder.timeHolder.setText(listItem.getCreated());
        if (!listItem.getImage().equals("")) {
            String[] temp = listItem.getImage().split(",");
            Glide.with(context).load(url + listItem.getIdx() + "/" + temp[0]).into(holder.imageViewHolder);
        }
    }

    @Override
    public int getItemCount() {
        return itemArr.size();
    }

    /*@Override
    public void onClick(View v) {
        Log.d("heu", "id : " + v.getId());
        int pos = 0;
        Intent intent = new Intent(v.getContext(), Notice_Detail.class);
        intent.putExtra("listname", "공지사항");
        intent.putExtra("idx", listItem.getIdx());
        intent.putExtra("title", listItem.getTitle());
        intent.putExtra("contents", listItem.getContents());
        intent.putExtra("created", listItem.getCreated());
        intent.putExtra("image", listItem.getImage());
        v.getContext().startActivity(intent);
    }*/

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
