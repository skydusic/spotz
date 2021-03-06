package addup.fpcompany.com.addsup.frag;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import addup.fpcompany.com.addsup.R;
import addup.fpcompany.com.addsup.adapter.MainListAdater;
import addup.fpcompany.com.addsup.java.listItem;

/**
 * Created by song02 on 2018-03-23.
 */

public class listFrag extends Fragment {

    ArrayList<listItem> listItems = new ArrayList<>();
    MainListAdater adapter = new MainListAdater(getActivity(), listItems);
    RecyclerView recyclerView;

    public listFrag() {
    }

    //기본 생성자 + 파라미터(인자)가 있는 생성자
    @SuppressLint("ValidFragment")
    public listFrag(ArrayList<listItem> listItems) {
        this.listItems = listItems;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_recyclerview, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        setRecyclerView();
        return view;
    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MainListAdater(getActivity(), listItems);
        recyclerView.setAdapter(adapter);
    }
}
