package addup.fpcompany.com.addsup.adapter;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import addup.fpcompany.com.addsup.MainActivity;
import addup.fpcompany.com.addsup.R;
import addup.fpcompany.com.addsup.java.commentItem;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    Context context;
    ArrayList<commentItem> listArr;
    commentItem commentItem;
    private static Typeface mTypeface;
    private static final String TAG_EMAIL1 = "skydusic@gmail.com";
    private static final String TAG_EMAIL2 = "drbasketkorea@gmail.com";
    private OnItemClickListener mListener;

    public CommentAdapter(Context context, ArrayList<commentItem> itemArr, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.listArr = itemArr;
        this.mListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        if (mTypeface == null)
            mTypeface = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/dohyeonttf.ttf");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        commentItem = listArr.get(position);

        holder.usernameTv.setText(commentItem.getUsername());
        holder.contentsTv.setText(commentItem.getContents());
        holder.commentCreatedTv.setText(commentItem.getCreated());

        if (MainActivity.mUsermail != null) {
            if (listArr.get(position).getEmail().equals(MainActivity.mUsermail) || MainActivity.mUsermail.equals(TAG_EMAIL1) || MainActivity.mUsermail.equals(TAG_EMAIL2)) {
                holder.editIv.setVisibility(View.VISIBLE);
                holder.deleteIv.setVisibility(View.VISIBLE);
                holder.editIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemClick(v, position);
                    }
                });
                holder.deleteIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemClick(v, position);
                    }
                });
            }
        }

    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    @Override
    public int getItemCount() {
        return listArr.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTv, contentsTv, commentCreatedTv;
        ImageView editIv, deleteIv;

        public ViewHolder(View itemView) {
            super(itemView);
            usernameTv = itemView.findViewById(R.id.usernameTv);
            contentsTv = itemView.findViewById(R.id.contentsTv);
            commentCreatedTv = itemView.findViewById(R.id.commentCreatedTv);
            editIv = itemView.findViewById(R.id.editIv);
            deleteIv = itemView.findViewById(R.id.deleteIv);

            usernameTv.setTypeface(mTypeface);
            contentsTv.setTypeface(mTypeface);
            commentCreatedTv.setTypeface(mTypeface);
        }
    }
}
