package addup.fpcompany.com.addsup.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import addup.fpcompany.com.addsup.R;

/**
 * Created by song02 on 2018-03-23.
 */

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ViewHolder> {

    Context context;
    ArrayList<String> itemArr = new ArrayList<>();
    private static Typeface mTypeface;

    public ActionAdapter(Context context, ArrayList<String> itemArr) {
        this.context = context;
        this.itemArr = itemArr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mypage_actionlist, parent, false);
        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/swaggerttf.ttf");
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String temp = itemArr.get(position).toString();
        holder.coperation.setText(temp);
    }

    @Override
    public int getItemCount() {
        return itemArr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView coperation;

        public ViewHolder(View itemView) {
            super(itemView);
            coperation = itemView.findViewById(R.id.corperTv);
            coperation.setTypeface(mTypeface);
        }
    }
}
