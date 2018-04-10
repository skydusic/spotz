package addup.fpcompany.com.addsup;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

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

    EditText contentsET;
    TextView text1Tv;
    TextView text2Tv;
    TextView text3Tv;
    TextView text4Tv;
    TextView text5Tv;
    EditText text1;
    EditText text2;
    EditText text3;
    EditText text4;
    EditText text5;

    ImageView imgFind;
    ImageView imView1;
    ImageView imView2;
    ImageView imView3;
    ImageView imView4;
    ImageView imView5;
    ImageView imView6;
    ImageView imView7;
    HorizontalScrollView horScrollView;
    Spinner spinner1;
    Spinner spinner2;
    TextView buttonInsert;
    ArrayAdapter<String> spinnerAdapter1;
    ArrayAdapter<String> spinnerAdapter2;
    int spinnerNumber1 = -1;
    int spinnerNumber2 = -1;

    private final int GALLERY_CODE = 1112;

    String serverUri;
    String imagePath;
    String imageAddress1 = "";
    String listName;
    String contents;
    String image;
    public static final String url = MainActivity.serverUrl;
    int postNum;

    ArrayList<String> filenameList = new ArrayList<>();
    ArrayList<String> imagePathArr = new ArrayList<>();
    ArrayList<Uri> uriList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        horScrollView = findViewById(R.id.horScrollView);
        text1Tv = findViewById(R.id.text1Tv);
        text2Tv = findViewById(R.id.text2Tv);
        text3Tv = findViewById(R.id.text3Tv);
        text4Tv = findViewById(R.id.text4Tv);
        text5Tv = findViewById(R.id.text5Tv);
        imView1 = findViewById(R.id.imView1);
        imView2 = findViewById(R.id.imView2);
        imView3 = findViewById(R.id.imView3);
        imView4 = findViewById(R.id.imView4);
        imView5 = findViewById(R.id.imView5);
        imView6 = findViewById(R.id.imView6);
        imView7 = findViewById(R.id.imView7);
        imgFind = findViewById(R.id.imgFind);
        contentsET = findViewById(R.id.contentsET);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        buttonInsert = findViewById(R.id.button_main_insert);

        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.text5);

        Intent clubInt = getIntent();
        postNum = clubInt.getIntExtra("postNum", -1);
        contents = clubInt.getStringExtra("contents");
        listName = clubInt.getStringExtra("listname");
        image = clubInt.getStringExtra("image");

        spinnerAdapter1 = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, MainActivity.spinList1);

        if (listName.equals("clubtable")) {
            text1Tv.setText("업체명");
            text2Tv.setText("종목");
            text3Tv.setText("위치");
            text4Tv.setText("연락처");
            text5Tv.setText("세부 안내사항");
        } else if (listName.equals("freelancer")) {
            text1Tv.setText("대표명");
            text2Tv.setText("종목");
            text3Tv.setText("레슨장소");
            text4Tv.setText("연락처");
            text5Tv.setText("세부 안내사항");
        } else if (listName.equals("competition")) {
            text1Tv.setText("대회명");
            text2Tv.setText("종목");
            text3Tv.setText("위치");
            text4Tv.setText("연락처");
            text5Tv.setText("세부 안내사항");
        } else if (listName.equals("dongho")) {
            text1Tv.setText("동호회명");
            text2Tv.setText("종목");
            text3Tv.setText("위치");
            text4Tv.setText("시간");
            text5Tv.setText("연락처");
        } else if (listName.equals("review")) {
            text1Tv.setText("제품명");
            text2Tv.setText("구입처");
            text3Tv.setText("가격");
            text4Tv.setText("평점");
            text5Tv.setText("세부 안내사항");
        } else if (listName.equals("employment")) {
            text1Tv.setText("회사명");
            text2Tv.setText("회사위치");
            text3Tv.setText("연봉 / 시급");
            text4Tv.setText("면접일정");
            text5Tv.setText("세부 안내사항");
        }

        if (postNum == 0) {
            serverUri = "http://spotz.co.kr/var/www/html/clubinsert.php";
        } else if (postNum == 1) {
            serverUri = "http://spotz.co.kr/var/www/html/clubupdate.php";
        }

        /**  글 수정 */
        /*if (title != null) {
            titleET.setText(title);
            contentsET.setText(contents);

            String tempUrl = url + "userImageFoler/";
            String[] temp = image.split(",");

            for (int i = 0; i < temp.length; i++) {
                switch (i) {
                    case 0:
                        Glide.with(this).load(tempUrl + temp[0]).into(imView1);
                        imView1.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        Glide.with(this).load(tempUrl + temp[1]).into(imView2);
                        imView2.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        Glide.with(this).load(tempUrl + temp[2]).into(imView3);
                        imView3.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        Glide.with(this).load(tempUrl + temp[3]).into(imView4);
                        imView4.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        Glide.with(this).load(tempUrl + temp[4]).into(imView5);
                        imView5.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        Glide.with(this).load(tempUrl + temp[5]).into(imView6);
                        imView6.setVisibility(View.VISIBLE);
                        break;
                    case 6:
                        Glide.with(this).load(tempUrl + temp[6]).into(imView7);
                        imView7.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }*/

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contents = contentsET.getText().toString().trim();
                String tex1 = text1.getText().toString();
                String tex2 = text2.getText().toString();
                String tex3 = text3.getText().toString();
                String tex4 = text4.getText().toString();
                String tex5 = text5.getText().toString();

                if (contents.length() > 300) {
                    Toast.makeText(insertActivity.this, "글자 제한을 초과했습니다. (" + String.valueOf(contents.length()) + " / 300자)", Toast.LENGTH_LONG).show();
                } else {
                    /** 해야할 일  */
                    if (!contents.equals("")) {
                        InsertData task = new InsertData();
                        ConnectServer connectServer = new ConnectServer();
                        for (int i = 0; i < imagePathArr.size(); i++) {
                            connectServer.requestPost(url, MainActivity.mUsername, listName, uriList.get(i));
                        }
                        for (int i = 0; i < filenameList.size(); i++) {
                            imageAddress1 += filenameList.get(i).toString();
                            if (filenameList.size() > i) {
                                imageAddress1 += ",";
                            }
                        }
                        task.request(contents, serverUri, MainActivity.mUsername, imageAddress1,
                                listName, MainActivity.spinList1.get(spinnerNumber1), MainActivity.spinList2.get(spinnerNumber1).get(spinnerNumber2),
                                tex1, tex2, tex3, tex4, tex5);
                        setResult(2400);
                        finish();
                    } else {
                        Toast.makeText(insertActivity.this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        spinner1.setAdapter(spinnerAdapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerNumber1 = position;
                spinner2.setVisibility(View.INVISIBLE);
                spinnerAdapter2 = new ArrayAdapter<>(insertActivity.this, R.layout.support_simple_spinner_dropdown_item, MainActivity.spinList2.get(position));
                spinner2.setAdapter(spinnerAdapter2);
                if (MainActivity.spinList2.get(position).size() > 1) {
                    spinner2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner2.setOnItemSelectedListener(this);

        imgFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DelFolder delFolder = new DelFolder();
                delFolder.requestPost(MainActivity.mUsername, listName);
                show();
            }
        });
        checkDangerousPermissions();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY_CODE:

                    imView1.setImageResource(0);
                    imView2.setImageResource(0);
                    imView3.setImageResource(0);

                    //ClipData 또는 Uri를 가져온다
                    Uri uri = data.getData();
                    ClipData clipData = data.getClipData();

                    if (clipData != null) {
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            if (i == 7) {
                                break;
                            }
                            Uri urione = clipData.getItemAt(i).getUri();
                            File sourceFile = new File(getRealPathFromURI(urione));
                            filenameList.add(sourceFile.getName().toString());
                            imagePathArr.add(getRealPathFromURI(urione));
                            uriList.add(urione);
                            switch (i) {
                                case 0:
                                    Glide.with(this).load(urione).into(imView1);
                                    imView1.setVisibility(View.VISIBLE);
                                    break;
                                case 1:
                                    Glide.with(this).load(urione).into(imView2);
                                    imView2.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    Glide.with(this).load(urione).into(imView3);
                                    imView3.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    Glide.with(this).load(urione).into(imView4);
                                    imView4.setVisibility(View.VISIBLE);
                                    break;
                                case 4:
                                    Glide.with(this).load(urione).into(imView5);
                                    imView5.setVisibility(View.VISIBLE);
                                    break;
                                case 5:
                                    Glide.with(this).load(urione).into(imView6);
                                    imView6.setVisibility(View.VISIBLE);
                                    break;
                                case 6:
                                    Glide.with(this).load(urione).into(imView7);
                                    imView7.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }
                        horScrollView.setVisibility(View.VISIBLE);
                    } else if (uri != null) {
                        imView1.setImageURI(uri);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private Bitmap resize(Bitmap bm) {
        Configuration config = getResources().getConfiguration();
        if (bm.getWidth() < bm.getHeight()) {
            if (config.smallestScreenWidthDp >= 1024)
                bm = Bitmap.createScaledBitmap(bm, 615, 1024, true);
            else
                bm = Bitmap.createScaledBitmap(bm, 360, 600, true);
        } else {
            if (config.smallestScreenWidthDp >= 1024)
                bm = Bitmap.createScaledBitmap(bm, 1024, 615, true);
            else
                bm = Bitmap.createScaledBitmap(bm, 600, 360, true);
        }

//        bm = Bitmap.createScaledBitmap(bm, bm.getWidth()/3, bm.getHeight()/3, true);
        return bm;
    }

    private String fileToBitmap(Uri imgUri) {

        File sourceFile = new File(getRealPathFromURI(imgUri));

        String strNewFolder = "CacheFolder" + File.separator;
        String strFileName = sourceFile.getName().toString();

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
            Log.d(TAG, strFileName + "File not exist");
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
            resize(rotate(bitmap, exifDegree)).compress(Bitmap.CompressFormat.PNG, 100, outputStream);
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
            Log.d(TAG, "파일이동 E : " + e);
            e.printStackTrace();
        } finally {
            if (out != null) out.close();
            if (in != null) in.close();
        }
    }

    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
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
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();

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
        matrix.postRotate(degree);
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

    void show() {
        Intent intent2 = new Intent(Intent.ACTION_PICK);
        intent2.setType("image/*");
        intent2.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent2.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(Intent.createChooser(intent2, "사진을 선택하세요"), GALLERY_CODE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerNumber2 = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class InsertData {
        ProgressDialog progressDialog;
        OkHttpClient client = new OkHttpClient();

        protected void request(String contents, String serverURL, String username,
                               String image, String listname, String spindata1, String spindata2,
                               String text1, String text2, String text3, String text4, String text5) {

            Log.d("heu", "이미지 : " + checkImg(image));

            RequestBody requestBody = new FormBody.Builder().
                    add("contents", contents).
                    add("username", username).
                    add("image", checkImg(image)).
                    add("listname", listname).
                    add("spindata1", spindata1).
                    add("spindata2", spindata2).
                    add("text1", text1).
                    add("text2", text2).
                    add("text3", text3).
                    add("text4", text4).
                    add("text5", text5).
                    build();

            Request request = new Request.Builder().url(serverURL).post(requestBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, "Response Body is " + response.body().string());
                }
            });

        }

        private String checkImg(String image) {
//            이미지 주소에 테이블 이름 붙이는 메소드
            String add = "";
            if (!image.equals("")) {
                if (listName.equals("clubtable")) {
                    add += "clubtable/";
                } else if (listName.equals("freelancer")) {
                    add += "freelancer/";
                }
                add += MainActivity.mUsername + '/';
            } else {
                return " ";
            }
            return add + image;
        }
    }

    class ConnectServer {
        //Client 생성
        OkHttpClient client = new OkHttpClient();

        //GET 방식
        public void requestGet(String url, String searchKey) {
            //URL에 포함할 Query문 작성 Name&Value
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            urlBuilder.addEncodedQueryParameter("searchKey", searchKey);
            String requestUrl = urlBuilder.build().toString();

            //Query문이 들어간 URL을 토대로 Request 생성
            Request request = new Request.Builder().url(requestUrl).build();

            //만들어진 Request를 서버로 요청할 Client 생성
            //Callback을 통해 비동기 방식으로 통신을 하여 서버로부터 받은 응답을 어떻게 처리 할 지 정의함
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, "Response Body is " + response.body().string());
                }
            });
        }

        //POST 방식
        public void requestPost(String url, String username, String listname, Uri uri) {
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

            url += "/imagesave.php";

            File file = new File(fileToBitmap(uri));
            String filename = file.getName().toString();

            //Request Body에 서버에 보낼 데이터 작성
//            RequestBody requestBody = new FormBody.Builder().add("userId", id).add("userPassword", password).build();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("uploaded_file", filename, RequestBody.create(MEDIA_TYPE_PNG, file))
                    .addFormDataPart("username", username)
                    .addFormDataPart("listname", listname)
                    .build();

            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, "Response Body is " + response.body().string());
                }
            });
        }
    }

    private class DelFolder {
        OkHttpClient client = new OkHttpClient();

        public void requestPost(String username, String listname) {
            String url = "http://spotz.co.kr/var/www/html/delfolder.php";
            RequestBody requestBody = new FormBody.Builder().
                    add("username", username).
                    add("listname", listname).build();

            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, "Response Body is " + response.body().string());
                }
            });
        }
    }
}
