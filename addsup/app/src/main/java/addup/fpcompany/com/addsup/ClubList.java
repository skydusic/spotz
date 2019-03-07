package addup.fpcompany.com.addsup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import addup.fpcompany.com.addsup.adapter.MainListAdater;
import addup.fpcompany.com.addsup.adapter.RecyclerItemClickListener;
import addup.fpcompany.com.addsup.java.listItem;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ClubList extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    RecyclerView recyclerView;
    MainListAdater adapter;
    TextView insertBtn, listTopName;
    ProgressBar progressBar2;
    Spinner spinner1;

    ArrayList<listItem> listItems = new ArrayList<>();
    ArrayList<String> noticeContentsArr = new ArrayList<>();
    ArrayAdapter<String> spinnerAdapter1;

    String myJSON, topNotice, listName, boardUrl = "";
    String username = MainActivity.mUsername;
    int noticeCounter, spinner1Num, pageOrder = 0;

    JSONArray topic = new JSONArray();
    getPost getPost = new getPost();
    GetTopNotice getTopNotice = new GetTopNotice();

    private static final String TAG_IDX = "idx";
    private static final String TAG_RESULTS = "results";
    private static final String TAG_FLAG = "endpage";
    private static final String TAG_TITLE = "title";
    private static final String TAG_LISTNAME = "listname";
    private static final String TAG_ID = "idx";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_CONTENTS = "contents";
    private static final String TAG_CREATED = "created";
    private static final String TAG_SPIN = "spindata";
    private static final String TAG_HIT = "hit";
    private static final String TAG_IMAGE = "image";
    private static final String All = "전체";
    private static final String TAG_ADMIN = "관리자";
    private static Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_list);

        recyclerView = findViewById(R.id.recyclerView);
        listTopName = findViewById(R.id.listTopName);
        insertBtn = findViewById(R.id.insertBtn);
        spinner1 = findViewById(R.id.spinner1);
        progressBar2 = findViewById(R.id.progressBar2);

        spinnerAdapter1 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, MainActivity.spinList1) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                mTypeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/dohyeonttf.ttf");
                setGlobalFont(parent);
                return super.getView(position, convertView, parent);
            }
        };

        //경로 받는다!
        Intent intent = getIntent();
        listName = intent.getStringExtra("listName");

        //Json 확인
//        handler.sendEmptyMessage(100);

        boardUrl = "http://spotz.co.kr/var/www/html/freeboard.php";

        //넘어온 경로에 따라 다른리스트를 받는다
        if (listName.equals("freeboard")) {
            listTopName.setText("자유게시판");
        } else if (listName.equals("nba")) {
            listTopName.setText("NBA");
        } else if (listName.equals("kbl")) {
            listTopName.setText("KBL");
        } else if (listName.equals("qna")) {
            listTopName.setText("질문게시판");
        } else if (listName.equals("equip")) {
            listTopName.setText("용품게시판");
        } else if (listName.equals("compet")) {
            listTopName.setText("대회게시판");
        }

        //권한 체크 순서
//        showList() -> checkSelect() -> setRecyclerView();
//        authorityChk();


        spinner1.setAdapter(spinnerAdapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                progressBar2.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                getTopNotice.requestPost();
                spinner1Num = position;
                getPost.requestPost(boardUrl, MainActivity.spinList1.get(position), listName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        if (position < noticeCounter) {
                            Intent intent = new Intent(v.getContext(), Notice_Detail.class);
                            intent.putExtra("listname", "topNotice");
                            intent.putExtra("idx", listItems.get(position).getIdx());
                            intent.putExtra("title", listItems.get(position).getTitle());
                            intent.putExtra("contents", noticeContentsArr.get(position));
                            intent.putExtra("created", listItems.get(position).getCreated());
                            intent.putExtra("image", listItems.get(position).getImage());
                            v.getContext().startActivity(intent);
                        } else {
                            Intent intent = new Intent(v.getContext(), DetailList.class);
                            intent.putExtra("listname", listName);
                            intent.putExtra("idx", listItems.get(position).getIdx());
                            intent.putExtra("username", listItems.get(position).getUsername());
                            intent.putExtra("email", listItems.get(position).getEmail());
                            intent.putExtra("title", listItems.get(position).getTitle());
                            intent.putExtra("created", listItems.get(position).getCreated());
                            intent.putExtra("spindata", listItems.get(position).getSpindata());
                            String image = listItems.get(position).getImage();
                            intent.putExtra("image", image);
                            v.getContext().startActivity(intent);
                        }
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(-1) && newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    progressBar2.setVisibility(View.VISIBLE);
                    recyclerView.removeAllViewsInLayout();
                    refreshHandler.sendEmptyMessage(3000);
                } else if (!recyclerView.canScrollVertically(1) && newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
//                    Log.i(TAG, "End of list");
                    myJSON = "";
                    GetAddPost getAddPost = new GetAddPost();
                    getAddPost.requestPost(boardUrl, All, listName, pageOrder);

                }
                /*if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {

                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {

                } else {

                }*/
            }
        });

        insertBtn.setOnClickListener(this);

    }

    void setGlobalFont(ViewGroup root) {
        for (int i = 0; i < root.getChildCount(); i++) {
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

    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MainListAdater(getApplicationContext(), listItems);
        recyclerView.setAdapter(adapter);

    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            removeMessages(100);
            if (myJSON != null && !myJSON.equals("")) {
                showList();
                setRecyclerView();
                myJSON = "";
                progressBar2.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                handler.sendEmptyMessageDelayed(100, 100);
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler noticeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            removeMessages(200);
            if (!topNotice.equals("")) {
                setNotice(topNotice);
                topNotice = "";
                handler.sendEmptyMessage(100);
            } else {
                noticeHandler.sendEmptyMessageDelayed(200, 100);
            }
        }
    };


    @SuppressLint("HandlerLeak")
    Handler refreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            removeMessages(3000);
            getTopNotice.requestPost();
            pageOrder = 0;
            getPost.requestPost(boardUrl, All, listName);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler addPageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            removeMessages(4000);
            if (myJSON.equals("")) {
                addPageHandler.sendEmptyMessageDelayed(4000, 100);
            } else {
                addList(myJSON);
            }
        }
    };

    protected void setNotice(String notice) {
        listItems.clear();
        noticeCounter = 0;
        try {
            JSONObject jsonObj = new JSONObject(notice);
            topic = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < topic.length(); i++) {
                JSONObject c = topic.getJSONObject(i);
                listItems.add(new listItem(c.getString(TAG_IDX), c.getString(TAG_TITLE), TAG_ADMIN, "", c.getString(TAG_IMAGE), "",
                        ClubList.settingTimes(c.getString(TAG_CREATED)), c.getString(TAG_HIT), ""));
                noticeContentsArr.add(c.getString(TAG_CONTENTS));
                noticeCounter++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
//            Log.d("heu", "adapter Exception : " + e);
        }
    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            topic = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < topic.length(); i++) {
                JSONObject c = topic.getJSONObject(i);

                listItems.add(new listItem(String.valueOf(c.getInt(TAG_ID)), c.getString(TAG_TITLE), c.getString(TAG_USERNAME), c.getString(TAG_EMAIL),
                        c.getString(TAG_IMAGE), c.getString(TAG_CREATED), c.getString(TAG_LISTNAME),
                        c.getString(TAG_HIT), c.getString(TAG_SPIN)));

            }
            pageOrder++;

        } catch (JSONException e) {
            e.printStackTrace();
//            Log.d("heu", "adapter Exception : " + e);
        }

        // 수정
//        checkSelect();
    }

    private void addList(String json) {

        try {
            JSONObject jsonObj = new JSONObject(json);
            topic = jsonObj.getJSONArray(TAG_RESULTS);
            String flag = jsonObj.getString(TAG_FLAG);
            if (!flag.equals(TAG_FLAG)) {
                for (int i = 0; i < topic.length(); i++) {
                    JSONObject c = topic.getJSONObject(i);
//                시간 설정
                    listItems.add(new listItem(String.valueOf(c.getInt(TAG_ID)), c.getString(TAG_TITLE), c.getString(TAG_USERNAME), c.getString(TAG_EMAIL),
                            c.getString(TAG_IMAGE), c.getString(TAG_CREATED), c.getString(TAG_LISTNAME),
                            c.getString(TAG_HIT), c.getString(TAG_SPIN)));
                }
                adapter.notifyItemInserted(0);
                pageOrder++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
//            Log.d("heu", "adapter Exception : " + e);
        }
    }

    //    쓴날짜가 오늘일 때 써진시간으로 보여주는 메소드
    static public String settingTimes(String created) {
        String time;
        ArrayList<String> result = new ArrayList<>();
        String[] temp1 = created.split(" ");
        String[] temp2 = temp1[0].split("-");


        if (created.length() > 10) {

            for (int k = 0; k < temp2.length; k++) {
                result.add(temp2[k]);
            }
            String[] temp3 = temp1[1].split(":");
            for (int j = 0; j < temp3.length; j++) {
                result.add(temp3[j]);
            }

            //                yyyy-MM-dd HH:mm:ss
            String curtime = new SimpleDateFormat("dd").format(new Date(System.currentTimeMillis()));
            if (result.get(2).equals(curtime)) {
                time = result.get(3) + " : " + result.get(4);
            } else {
                time = result.get(1) + "/" + result.get(2);
            }
        } else {
            return created;
        }

        return time;
    }

    class GetTopNotice {
        OkHttpClient client = new OkHttpClient();
        Request request;

        public void requestPost() {
            String url = "http://spotz.co.kr/var/www/html/topnoticeselect.php";
            RequestBody requestBody = new FormBody.Builder().
                    build();

            request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    Log.d("heu", "Connect Server Error is " + e.toString());

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    topNotice = response.body().string();
                    noticeHandler.sendEmptyMessage(200);
                }
            });

        }
    }

    class getPost {
        OkHttpClient client = new OkHttpClient();
        Request request;

        public void requestPost(String url, String spindata, String listname) {
            RequestBody requestBody = new FormBody.Builder().
                    add("spindata", spindata).
                    add("listname", listname).
                    add("pageOrder", "0").
                    build();

            request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    Log.d("heu", "Connect Server Error is " + e.toString());

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    myJSON = response.body().string();
                }
            });

        }
    }

    class GetAddPost {
        OkHttpClient client = new OkHttpClient();
        Request request;

        public void requestPost(String url, String spindata, String listname, final int pageOrder) {
            RequestBody requestBody = new FormBody.Builder().
                    add("spindata", spindata).
                    add("listname", listname).
                    add("pageorder", String.valueOf(pageOrder)).
                    build();

            request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    Log.d("heu", "Connect Server Error is " + e.toString());

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    myJSON = response.body().string();
                    addPageHandler.sendEmptyMessage(4000);
                }
            });

        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case (R.id.insertBtn):
                if (MainActivity.mUser == null) {
                    intent = new Intent(this, SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivityForResult(intent, 10);
                } else {
                    intent = new Intent(this, addup.fpcompany.com.addsup.insertActivity.class);
                    intent.putExtra("listname", listName);
                    intent.putExtra("username", username);
                    startActivityForResult(intent, 2000);
                }
                break;
            case (R.id.bottomHome):
                Intent intent3 = new Intent(ClubList.this, MainActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent3);
                break;

            case (R.id.bottomNotice):
                intent = new Intent(ClubList.this, Notice_Activity.class);
                startActivity(intent);
                break;

            case (R.id.bottomInfo):
                intent = new Intent(ClubList.this, infoActivity.class);
                intent.putExtra("flag", "info");
                startActivity(intent);
                break;

            case (R.id.bottomMember):
                if (MainActivity.mUser == null) {
                    Intent intent1 = new Intent(ClubList.this, SignInActivity.class);
                    finish();
                    startActivityForResult(intent1, 10);
                } else {
                    Intent intent2 = new Intent(ClubList.this, myPageActivity.class);
                    finish();
                    startActivityForResult(intent2, 1000);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2400) {
            getPost.requestPost(boardUrl, All, listName);
            handler.sendEmptyMessageDelayed(100, 100);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeMessages(100);
        handler.removeMessages(200);
        handler.removeMessages(3000);
        handler.removeMessages(4000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(100);
        handler.removeMessages(200);
        handler.removeMessages(3000);
        handler.removeMessages(4000);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
