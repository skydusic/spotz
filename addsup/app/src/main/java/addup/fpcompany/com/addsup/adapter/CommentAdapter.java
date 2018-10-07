package addup.fpcompany.com.addsup.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import addup.fpcompany.com.addsup.R;
import addup.fpcompany.com.addsup.java.commentItem;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    Context context;
    ArrayList<commentItem> listArr;
    commentItem commentItem;
    private static Typeface mTypeface;

    public CommentAdapter(Context context, ArrayList<commentItem> itemArr) {
        this.context = context;
        this.listArr = itemArr;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        if (mTypeface == null)
            mTypeface = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/dohyeonttf.ttf");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        commentItem = listArr.get(position);

        holder.usernameTv.setText(commentItem.getUsername());
        holder.contentsTv.setText(commentItem.getContents());
        holder.commentCreatedTv.setText(commentItem.getCreated());
    }

    @Override
    public int getItemCount() {
        return listArr.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTv, contentsTv, commentCreatedTv;
        public ViewHolder(View itemView) {
            super(itemView);
            usernameTv = itemView.findViewById(R.id.usernameTv);
            contentsTv = itemView.findViewById(R.id.contentsTv);
            commentCreatedTv = itemView.findViewById(R.id.commentCreatedTv);

            usernameTv.setTypeface(mTypeface);
            contentsTv.setTypeface(mTypeface);
            commentCreatedTv.setTypeface(mTypeface);
        }
    }
}
