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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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

    String TAG = "ClubList";
    RecyclerView recyclerView;
    MainListAdater adapter;
    TextView insertBtn;

    String listName;
    TextView listTopName;
    ArrayList<listItem> listItems = new ArrayList<>();

    String myJSON = "";

    private static final String TAG_RESULTS = "results";
    private static final String TAG_ID = "idx";
    private static final String TAG_TITLE = "title";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_CONTENTS = "contents";
    private static final String TAG_CREATED = "created";
    private static final String TAG_IMAGE = "image";

    JSONArray topic = new JSONArray();

    int authNum = 0;
    int postNum = 0;
    String boardUrl;

    HashMap<String, String> postHashmap = new HashMap<>();
    String username = MainActivity.mUsername;

    Spinner spinner1;
    Spinner spinner2;
    ArrayAdapter<String> spinnerAdapter1;
    ArrayAdapter<String> spinnerAdapter2;
    int spinner1Num = 0;

    getPost getPost = new getPost();
    private static Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_list);

        recyclerView = findViewById(R.id.recyclerView);
        listTopName = findViewById(R.id.listTopName);
        insertBtn = findViewById(R.id.insertBtn);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);

        spinnerAdapter1 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, MainActivity.spinList1) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                mTypeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/swaggerttf.ttf");
                setGlobalFont(parent);

                return super.getView(position, convertView, parent);
            }
        };

        //경로 받는다!
        Intent intent = getIntent();
        listName = intent.getStringExtra("listName");

        //Json 확인
        handler.sendEmptyMessage(100);

        boardUrl = "http://spotz.co.kr/var/www/html/clubtable.php";

        //넘어온 경로에 따라 다른리스트를 받는다
        if (listName.equals("clubtable")) {
            listTopName.setText("스포츠 클럽");
        } else if (listName.equals("freelancer")) {
            listTopName.setText("스포츠 프리랜서");
        } else if (listName.equals("competition")) {
            listTopName.setText("스포츠 대회");
        } else if (listName.equals("dongho")) {
            listTopName.setText("스포츠 동호회");
        } else if (listName.equals("review")) {
            listTopName.setText("스포츠 제품 리뷰");
        } else if (listName.equals("employment")) {
            listTopName.setText("스포츠 취업정보");
        }

        /** 메뉴별 화면 만들기! */

        //권한 체크 순서
//        showList() -> checkSelect() -> setRecyclerView();

        authorityChk();

        insertBtn.setOnClickListener(this);

        spinner1.setAdapter(spinnerAdapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getPost.requestPost(boardUrl, MainActivity.spinList1.get(position), "", listName);
                spinner2.setVisibility(View.INVISIBLE);
                spinnerAdapter2 = new ArrayAdapter<>(ClubList.this, R.layout.support_simple_spinner_dropdown_item, MainActivity.spinList2.get(position));
                spinner2.setAdapter(spinnerAdapter2);
                if (MainActivity.spinList2.get(position).size() > 1) {
                    spinner2.setVisibility(View.VISIBLE);
                }
                spinner1Num = position;
                handler.sendEmptyMessage(100);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(this);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        // do whatever
                        Intent intent = new Intent(v.getContext(), DetailList.class);
                        intent.putExtra("listname", listName);
                        intent.putExtra("idx", listItems.get(position).getIdx());
                        intent.putExtra("title", listItems.get(position).getTitle());
                        intent.putExtra("contents", listItems.get(position).getContents());
                        intent.putExtra("created", listItems.get(position).getCreated());
                        intent.putExtra("owner", listItems.get(position).getOwner());
                        intent.putExtra("timetable", listItems.get(position).getTimetable());
                        intent.putExtra("location", listItems.get(position).getLocation());
                        intent.putExtra("traffic", listItems.get(position).getTraffic());
                        intent.putExtra("fee", listItems.get(position).getFee());
                        intent.putExtra("phone", listItems.get(position).getPhone());

                        /** 해야할 일 이미지 -> 리사이클러뷰 */
                        String image = listItems.get(position).getImage();
                        String[] temp = image.split(",");
                        intent.putExtra("image", temp[0]);
                        v.getContext().startActivity(intent);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

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

    public int authorityChk() {
        MainActivity.mUser = MainActivity.mAuth.getInstance().getCurrentUser();
        authNum = 0;
        if (MainActivity.mUser == null) {
            authNum = 0;
        } else if (MainActivity.mUser.getDisplayName() == "skydusic@gmail.com") {
            authNum = 2;
        } else if (MainActivity.mUser != null) {
            authNum = 1;
        }

//        버튼 보이기
        if (authNum == 0) {
            insertBtn.setVisibility(View.INVISIBLE);
        } else if (authNum == 1) {
            insertBtn.setVisibility(View.VISIBLE);
        }
        // 버튼 내용 수정
        if (postNum == 1) {
            insertBtn.setText("내 글 수정");
        }

        return authNum;
    }

    public void checkSelect() {
        // 수정
        try {
            if (username != null) {
                for (int i = 0; i < topic.length(); i++) {
                    JSONObject c = topic.getJSONObject(i);
                    String usedName = c.getString(TAG_USERNAME);
                    if (username.equals(usedName)) {
                        postNum = 1;
                        postHashmap.put(TAG_CONTENTS, c.getString(TAG_CONTENTS));
                        postHashmap.put(TAG_TITLE, c.getString(TAG_TITLE));
                        postHashmap.put(TAG_IMAGE, c.getString(TAG_IMAGE));
                        break;
                    } else {
                        postNum = 0;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!myJSON.equals("")) {
                showList();
                setRecyclerView();
                removeMessages(100);
                myJSON = "";
            } else {
                handler.sendEmptyMessageDelayed(100, 200);
            }
        }
    };

    protected void showList() {
        listItems.clear();
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            topic = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < topic.length(); i++) {
                JSONObject c = topic.getJSONObject(i);
//                시간 설정

                listItems.add(new listItem(String.valueOf(c.getInt(TAG_ID)), c.getString(TAG_USERNAME), c.getString(TAG_TITLE),
                        c.getString(TAG_CONTENTS), c.getString(TAG_IMAGE), settingTimes(c.getString(TAG_CREATED)), listName,
                        c.getString("owner"), c.getString("timetable"), c.getString("location"), c.getString("traffic"),
                        c.getString("fee"), c.getString("phone"), c.getString("hit")));

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("heu", "adapter Exception : " + e);
        }

        // 수정
//        checkSelect();
    }

    //    쓴날짜가 오늘일 때 써진시간으로 보여주는 메소드
    static public String settingTimes(String created) {
        String time;
        ArrayList<String> result = new ArrayList<>();
        String[] temp1 = created.split(" ");
        String[] temp2 = temp1[0].split("-");

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

        return time;
    }

    class getPost {
        OkHttpClient client = new OkHttpClient();
        Request request;

        public void requestPost(String url, String spindata1, String spindata2, String listname) {
            RequestBody requestBody = new FormBody.Builder().
                    add("spindata1", spindata1).
                    add("spindata2", spindata2).
                    add("listname", listname).
                    build();

            request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "Connect Server Error is " + e.toString());

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    myJSON = response.body().string();
                }
            });

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.insertBtn):
                Intent intent = new Intent(this, addup.fpcompany.com.addsup.insertActivity.class);
                intent.putExtra("postNum", postNum);
                intent.putExtra("listname", listName);
                intent.putExtra("username", username);
                /**
                 * if (postNum == 1) {
                 intent.putExtra("title", postHashmap.get(TAG_TITLE));
                 intent.putExtra("contents", postHashmap.get(TAG_CONTENTS));
                 intent.putExtra("image", postHashmap.get(TAG_IMAGE));
                 }*/
                startActivityForResult(intent, 2400);
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
            getPost.requestPost(boardUrl, "전국", "", listName);
            handler.sendEmptyMessageDelayed(24, 300);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeMessages(100);
        handler.removeMessages(200);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(100);
        handler.removeMessages(200);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            getPost.requestPost(boardUrl, "", MainActivity.spinList2.get(spinner1Num).get(position), listName);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
