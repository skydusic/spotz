package addup.fpcompany.com.addsup.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import addup.fpcompany.com.addsup.MainActivity;
import addup.fpcompany.com.addsup.R;
import addup.fpcompany.com.addsup.java.listItem;

/**
 * Created by song02 on 2018-03-13.
 */

public class MyPageOptionAdapter extends ArrayAdapter {

    LayoutInflater lnf;
    ArrayList<listItem> itemArr;
    Context context;
    String url = MainActivity.serverUrl + "userImageFolder/";
    private static Typeface mTypeface;

    public MyPageOptionAdapter(Activity context, ArrayList<listItem> itemArr) {
        super(context, R.layout.list_item, itemArr);
        this.itemArr = itemArr;
        this.context = context;
        lnf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        //몇개의 줄을 만들것인가 결정해 줌
        return itemArr.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return itemArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        RowDataViewHolder viewHolder;
        if (convertView == null) {
            convertView = lnf.inflate(R.layout.list_item, parent, false);
            viewHolder = new RowDataViewHolder();
            viewHolder.titleHolder = convertView.findViewById(R.id.title);
            viewHolder.descHolder = convertView.findViewById(R.id.contents);
            viewHolder.timeHolder = convertView.findViewById(R.id.created);
            viewHolder.imageViewHolder = convertView.findViewById(R.id.imageView);
            viewHolder.titleHolder.setText(itemArr.get(position).getTitle());
            viewHolder.timeHolder.setText(itemArr.get(position).getCreated());
            String[] temp = itemArr.get(position).getImage().split(",");
            Glide.with(context).load(url + temp[0]).into(viewHolder.imageViewHolder);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RowDataViewHolder) convertView.getTag();
        }

        return convertView;
    }

    public class RowDataViewHolder {
        public TextView titleHolder;
        public TextView descHolder;
        public TextView timeHolder;
        public ImageView imageViewHolder;
    }
}
