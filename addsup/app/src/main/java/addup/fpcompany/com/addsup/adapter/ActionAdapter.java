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
        if (this.mTypeface == null)
            this.mTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/SDSwaggerTTF.ttf");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mypage_actionlist, parent, false);
        setGlobalFont(parent);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
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

    void setGlobalFont(ViewGroup root) {
        for (int i = 0; i <= root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            if (child instanceof TextView) {
                // 폰트 세팅
                ((TextView) child).setTypeface(mTypeface);

                // 자간 조절
                ((TextView) child).setLineSpacing(10, 1);

                // 크기 조절
                ((TextView) child).setTextSize(16);

            } else if (child instanceof ViewGroup)
                setGlobalFont((ViewGroup) child);
        }
    }
}
