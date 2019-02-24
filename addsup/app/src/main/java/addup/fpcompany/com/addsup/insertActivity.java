package addup.fpcompany.com.addsup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import addup.fpcompany.com.addsup.adapter.PagerAdapter;
import addup.fpcompany.com.addsup.frag.DetailFrag;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class insertActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static String TAG = "heu";

    EditText titleEt;
    EditText contentsET;

    ImageView imgFind, imView1, imView2, imView3, imView4, imView5, imView6, imView7, imView8, imView9, imView10;
    HorizontalScrollView horScrollView;
    Spinner spinner;
    TextView buttonInsert;
    ArrayAdapter<String> spinnerAdapter1;
    int spinnerNumber1 = 0;
    int spinnerNumber2 = 0;

    private final int GALLERY_CODE = 1112;

    String idx, listname, image, imageurl = "";
    String title, serverUri, imagePath, contents, spindata, email;
    String url = MainActivity.serverUrl;
    int postNum;

    private static final String TAG_RESULTS = "results";
    private static final String TAG_CONTENTS = "contents";

    ArrayList<String> filenameList = new ArrayList<>();
    ArrayList<String> pathList = new ArrayList<>();
    ArrayList<Uri> uriList = new ArrayList<>();
    ArrayList<String> imageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        horScrollView = findViewById(R.id.horScrollView);
        imView1 = findViewById(R.id.imView1);
        imView2 = findViewById(R.id.imView2);
        imView3 = findViewById(R.id.imView3);
        imView4 = findViewById(R.id.imView4);
        imView5 = findViewById(R.id.imView5);
        imView6 = findViewById(R.id.imView6);
        imView7 = findViewById(R.id.imView7);
        imView8 = findViewById(R.id.imView8);
        imView9 = findViewById(R.id.imView9);
        imView10 = findViewById(R.id.imView10);
        imgFind = findViewById(R.id.imgFind);
        titleEt = findViewById(R.id.titleET);
        contentsET = findViewById(R.id.contentsET);
        spinner = findViewById(R.id.insspinner1);
        buttonInsert = findViewById(R.id.button_main_insert);

        Intent clubInt = getIntent();
        idx = clubInt.getStringExtra("idx");
        title = clubInt.getStringExtra("title");
        postNum = clubInt.getIntExtra("postNum", 0);
        contents = clubInt.getStringExtra("contents");
        listname = clubInt.getStringExtra("listname");

        spinnerAdapter1 = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, MainActivity.spinList1);

        if (postNum == 0) {
            // 새로쓰기
            serverUri = "http://spotz.co.kr/var/www/html/freeboardIns.php";
        } else if (postNum == 1) {
            // 수정
            serverUri = "http://spotz.co.kr/var/www/html/freeboardUpdate.php";

            titleEt.setText(clubInt.getStringExtra("title"));
            listname = clubInt.getStringExtra("listname");
            spindata = clubInt.getStringExtra("spindata");
            email = clubInt.getStringExtra("email");

            //getcontents
            GetContents getContents = new GetContents();
            getContents.requestPost(idx);

            /** 이미지 테스트 해봐야 함 */
            image = clubInt.getStringExtra("image");

            imageurl = MainActivity.serverUrl + "userImageFolder/" + listname + "/" + email + "/";
            if (!image.equals("")) {
                setImageView(image);
            } else {
                horScrollView.setVisibility(View.GONE);
            }
        }

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                String title = titleEt.getText().toString().trim();
                String contents = contentsET.getText().toString().trim();

                if (contents.length() > 900) {
                    Toast.makeText(insertActivity.this, "내용글자 제한을 초과했습니다. (" + String.valueOf(contents.length()) + " / 900자)", Toast.LENGTH_LONG).show();
                } else if (title.length() > 100) {
                    Toast.makeText(insertActivity.this, "제목글자 제한을 초과했습니다. (" + String.valueOf(contents.length()) + " / 100자)", Toast.LENGTH_LONG).show();
                } else {
                    if (!contents.equals("")) {
                        InsertData task = new InsertData();
                        ConnectServer connectServer = new ConnectServer();
                        for (int i = 0; i < uriList.size(); i++) {
                            connectServer.requestPost(url, MainActivity.mUsername, MainActivity.mUsermail, listname, uriList.get(i), filenameList.get(i), pathList.get(i));
                        }
                        String imageAddress1 = "";
                        for (int i = 0; i < filenameList.size(); i++) {
                            imageAddress1 += filenameList.get(i);
                            if (filenameList.size() - 1 > i) {
                                imageAddress1 += ",";
                            }
                        }
                        task.request(title, contents, serverUri, MainActivity.mUsername, MainActivity.mUsermail, imageAddress1,
                                listname, spindata);
                        setResult(2400, returnIntent);
                        finish();
                    } else {
                        Toast.makeText(insertActivity.this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();

                    }
                }


            }
        });

        spinner.setAdapter(spinnerAdapter1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spindata = MainActivity.spinList1.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imgFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*DelFolder delFolder = new DelFolder();
                delFolder.requestPost(MainActivity.mUsername, listName);*/
                moveGallery();
            }
        });
        checkDangerousPermissions();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(postNum == 1 || ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // editpost + image reset
            for (int i = 0; i < imageList.size(); i++) {
                DeleteImage deleteImage = new DeleteImage();
                deleteImage.requestPost(imageList.get(i));
            }
        }

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            //Reset List
            imageSettingReset();
            //Receiving data
            List<Image> images = ImagePicker.getImages(data);
            //Setting Image
            for (int i = 0; i < images.size(); i++) {
                Uri tempUri = Uri.fromFile(new File(images.get(i).getPath()));
                filenameList.add(images.get(i).getName());
                pathList.add(images.get(i).getPath());
                uriList.add(tempUri);
                imageSet(images.get(i).getPath(), i);
                Log.d("heu", "NAME : " + images.get(i).getName());
                Log.d("heu", "PATH : " + images.get(i).getPath());
            }

            horScrollView.setVisibility(View.VISIBLE);
        }
    }

    private void imageSet(String path, int i) {
        switch (i) {
            case 0:
                Glide.with(this).load(path).into(imView1);
                imView1.setVisibility(View.VISIBLE);
                break;
            case 1:
                Glide.with(this).load(path).into(imView2);
                imView2.setVisibility(View.VISIBLE);
                break;
            case 2:
                Glide.with(this).load(path).into(imView3);
                imView3.setVisibility(View.VISIBLE);
                break;
            case 3:
                Glide.with(this).load(path).into(imView4);
                imView4.setVisibility(View.VISIBLE);
                break;
            case 4:
                Glide.with(this).load(path).into(imView5);
                imView5.setVisibility(View.VISIBLE);
                break;
            case 5:
                Glide.with(this).load(path).into(imView6);
                imView6.setVisibility(View.VISIBLE);
                break;
            case 6:
                Glide.with(this).load(path).into(imView7);
                imView7.setVisibility(View.VISIBLE);
                break;
            case 7:
                Glide.with(this).load(path).into(imView7);
                imView8.setVisibility(View.VISIBLE);
                break;
            case 8:
                Glide.with(this).load(path).into(imView7);
                imView9.setVisibility(View.VISIBLE);
                break;
            case 9:
                Glide.with(this).load(path).into(imView7);
                imView10.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void imageSettingReset() {
        horScrollView.setVisibility(View.INVISIBLE);
        imView1.setVisibility(View.GONE);
        imView2.setVisibility(View.GONE);
        imView3.setVisibility(View.GONE);
        imView4.setVisibility(View.GONE);
        imView5.setVisibility(View.GONE);
        imView6.setVisibility(View.GONE);
        imView7.setVisibility(View.GONE);
        imView8.setVisibility(View.GONE);
        imView9.setVisibility(View.GONE);
        imView10.setVisibility(View.GONE);
        filenameList.clear();
        uriList.clear();
    }

    private void setImageView(String image) {
        String[] temp = image.split(",");
        for (String aTemp : temp) {
            imageList.add(imageurl + aTemp);
        }
        for (int i = 0; i < imageList.size(); i++) {
            imageSet(imageList.get(i), i);
        }
        horScrollView.setVisibility(View.VISIBLE);
    }

    private Bitmap resize(Bitmap bm) {
        Configuration config = getResources().getConfiguration();
        if(bm == null){
            return bm;
        } else {
            if (bm.getWidth() < bm.getHeight()) {
                if (config.smallestScreenWidthDp >= 1024)
                    bm = Bitmap.createScaledBitmap(bm, 1920, 1080, true);
                else
                    bm = Bitmap.createScaledBitmap(bm, 720, 1280, true);
            } else {
                if (config.smallestScreenWidthDp >= 1024)
                    bm = Bitmap.createScaledBitmap(bm, 1080, 1920, true);
                else
                    bm = Bitmap.createScaledBitmap(bm, 1280, 720, true);
            }
        }

//        bm = Bitmap.createScaledBitmap(bm, bm.getWidth()/3, bm.getHeight()/3, true);
        return bm;
    }

    private String fileToBitmap(String path) {

        File sourceFile = new File(path);

        String strNewFolder = "CacheFolder" + File.separator;
        String strFileName = sourceFile.getName();

        String strCurPath = sourceFile.getPath().substring(0, sourceFile.getPath().lastIndexOf("/")) + File.separator;
        String strNewPath = Environment.getExternalStorageDirectory().getPath() + File.separator + strNewFolder;

        File folder = new File(strNewPath);
        File fNewFileDir = new File(strNewPath + strFileName);

        if (!folder.exists()) {
            folder.mkdir();
        }
        String from = strCurPath + strFileName;
        String to = strNewPath + strFileName;

        try {
            filecopy(from, to);
        } catch (FileNotFoundException e) {
//            Log.d(TAG, strFileName + "File not exist");
        } catch (Exception e) {
            e.printStackTrace();
        }


        imagePath = fNewFileDir.getPath(); // path 경로

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
        File souceFile = new File(imagePath);

        try {
            OutputStream outputStream = new FileOutputStream(souceFile);
            resize(bitmap).compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return fNewFileDir.getAbsolutePath();

    }

    private static void filecopy(String from, String to) throws Exception {
        FileChannel in = null;
        FileChannel out = null;
        try {
            in = new FileInputStream(from).getChannel();
            out = new FileOutputStream(to).getChannel();
            in.transferTo(0, in.size(), out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) out.close();
            if (in != null) in.close();
        }
    }

    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;

        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
        } else {
//            Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "외장메모리 권한이 필요합니다", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap src, float degree) {

        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(0);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index = 0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }

    void moveGallery() {
        /*Intent intent2 = new Intent(Intent.ACTION_PICK);
        intent2.setType("image/*");
        intent2.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent2.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(Intent.createChooser(intent2, "사진을 선택하세요"), GALLERY_CODE);*/

        ImagePicker.create(this)
                .returnMode(ReturnMode.NONE) // set whether pick and / or camera action should return immediate result or not.
                .folderMode(false) // folder mode (false by default)
                .toolbarFolderTitle("폴더") // folder selection title
                .toolbarImageTitle("사진 선택") // image selection title
                .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                .includeVideo(false) // Show video on image picker
                .multi() // multi mode (default mode)
                .limit(10) // max images can be selected (99 by default)
                .showCamera(false) // show camera or not (true by default)
                .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                //.origin(images) // original selected images, used in multi mode
                //.exclude(images) // exclude anything that in image.getPath()
                //.excludeFiles(files) // same as exclude but using ArrayList<File>
                //.theme(R.style.CustomImagePickerTheme) // must inherit ef_BaseTheme. please refer to sample
                //.enableLog(false) // disabling log
                //.imageLoader(new GrayscaleImageLoder()) // custom image loader, must be serializeable
                .start(); // start image picker activity with request code

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerNumber2 = position;
//        Log.d("heu", "spin Number : " + position);
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @SuppressLint("HandlerLeak")
    Handler refreshPostHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (myJSON.equals("")) {
                refreshPostHandler.sendEmptyMessageDelayed(300, 200);
            } else {
                refreshPostHandler.removeMessages(300);
                try {
                    JSONObject jsonObj = new JSONObject(myJSON);
                    JSONArray post = jsonObj.getJSONArray(TAG_RESULTS);
                    for (int i = 0; i < post.length(); i++) {
                        JSONObject c = post.getJSONObject(i);
                        contents = c.getString(TAG_CONTENTS);
                    }
                    contentsET.setText(contents);
                } catch (JSONException e) {
                    e.printStackTrace();
//            Log.d("heu", "adapter Exception : " + e);
                }
            }
        }
    };

    class InsertData {
        ProgressDialog progressDialog;
        RequestBody requestBody;
        OkHttpClient client = new OkHttpClient();

        protected void request(String title, String contents, String serverURL, String username, String email,
                               String image, String listname, String spindata) {

            if (postNum == 1) {
                // 수정일 경우 idx 첨부!
                requestBody = new FormBody.Builder().
                        add("idx", idx).
                        add("title", title).
                        add("contents", contents).
                        add("username", username).
                        add("email", email).
                        add("image", image).
                        add("listname", listname).
                        add("spindata", spindata).
                        build();
            } else {
                requestBody = new FormBody.Builder().
                        add("title", title).
                        add("contents", contents).
                        add("username", username).
                        add("email", email).
                        add("image", image).
                        add("listname", listname).
                        add("spindata", spindata).
                        build();
            }

            Request request = new Request.Builder().url(serverURL).post(requestBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    Log.d(TAG, "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
//                    Log.d(TAG, "Response Body is " + response.body().string());
                }
            });

        }

    }

    class GetContents {
        OkHttpClient client = new OkHttpClient();
        Request request;

        public void requestPost(String idx) {

            String url = "http://spotz.co.kr/var/www/html/refreshPost.php";
            RequestBody requestBody = new FormBody.Builder().
                    add("idx", idx).
                    add("listname", listname).
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
                    refreshPostHandler.sendEmptyMessage(300);
                }
            });

        }
    }

    class ConnectServer {
        //Client 생성
        OkHttpClient client = new OkHttpClient();

        //POST 방식
        public void requestPost(String url, String username, String email, String listname, Uri uri,String filename, String path) {
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

            url += "/imagesave.php";

            File file = new File(path);

            //Request Body에 서버에 보낼 데이터 작성
//            RequestBody requestBody = new FormBody.Builder().add("userId", id).add("userPassword", password).build();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("uploaded_file", filename, RequestBody.create(MEDIA_TYPE_PNG, file))
                    .addFormDataPart("username", username)
                    .addFormDataPart("email", email)
                    .addFormDataPart("listname", listname)
                    .build();

            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    Log.d(TAG, "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
//                    Log.d(TAG, "Response Body is " + response.body().string());
                }
            });
        }
    }

    /**
     * 욕설 필터링 기능
     * 서버에서 욕목록을 받아옴
     */
    String myJSON = "";

    class getStringFinderText {
        OkHttpClient client = new OkHttpClient();
        Request request;

        public void requestPost() {
            RequestBody requestBody = new FormBody.Builder().
                    build();

            request = new Request.Builder().url(url + "getStringFinderText/").post(requestBody).build();
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

    class DeleteImage {
        //Client 생성
        OkHttpClient client = new OkHttpClient();

        public void requestPost(String imagepath) {
            String url = "http://spotz.co.kr/var/www/html/deleteImage.php";
            //Request Body에 서버에 보낼 데이터 작성

            File file = new File(imagepath);
            String[] path = file.getPath().split("/html");

            RequestBody requestBody = new FormBody.Builder().
                    add("imagepath", path[1]).
                    build();

            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    Log.d("heu", "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        refreshPostHandler.removeMessages(300);
    }
}
