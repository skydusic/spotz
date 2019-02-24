package addup.fpcompany.com.addsup.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import addup.fpcompany.com.addsup.R;
import addup.fpcompany.com.addsup.java.commentItem;

public class CommentListAdapter extends BaseAdapter {

    private ArrayList<commentItem> listViewItemsList = new ArrayList<>();
    private static Typeface mTypeface;

    public CommentListAdapter() {

    }

    @Override
    public int getCount() {
        return listViewItemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        final Context context = parent.getContext();

        if (mTypeface == null)
            mTypeface = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/dohyeonttf.ttf");

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.comment_item, parent, false);
        }

        TextView usernameTv = convertView.findViewById(R.id.usernameTv);
        TextView contentsTv = convertView.findViewById(R.id.contentsTv);
        TextView commentCreatedTv = convertView.findViewById(R.id.commentCreatedTv);
        ImageView editIv = convertView.findViewById(R.id.editIv);
        ImageView deleteIv = convertView.findViewById(R.id.deleteIv);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        commentItem temp = listViewItemsList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        usernameTv.setTypeface(mTypeface);
        contentsTv.setTypeface(mTypeface);
        commentCreatedTv.setTypeface(mTypeface);

        usernameTv.setText(temp.getUsername());
        contentsTv.setText(temp.getContents());
        commentCreatedTv.setText(temp.getCreated());

        return convertView;
    }

    public void addItem(commentItem item) {
        commentItem temp = new commentItem(item.getIdx(), item.getListname(), item.getUsername(), item.getEmail(), item.getUsername(), item.getCreated());
        listViewItemsList.add(temp);
    }

}
