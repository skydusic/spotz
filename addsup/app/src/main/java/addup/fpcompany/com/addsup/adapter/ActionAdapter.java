package addup.fpcompany.com.addsup.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    public ActionAdapter(Context context, ArrayList<String> itemArr) {
        this.context = context;
        this.itemArr = itemArr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mypage_actionlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("heu", "액션 어댑터 itemarr : " + itemArr.toString());
        String temp = itemArr.get(position).toString();
        holder.titleHolder.setText(temp);
    }

    @Override
    public int getItemCount() {
        return itemArr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleHolder;

        public ViewHolder(View itemView) {
            super(itemView);
            titleHolder = itemView.findViewById(R.id.titleTv);

        }
    }
}
