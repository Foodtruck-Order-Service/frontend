package kr.co.fos.client.review;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.bookmark.Bookmark;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class registerActivity extends AppCompatActivity {
    Retrofit client;
    HttpInterface service;

    RatingBar ratingBar;

    TextView idTextView;
    TextView gradeTextView;
    TextView contentTextView;
    TextView registDateTextView;

    Button logoutBtn;
    Button menuBtn;
    Button introduceBtn;
    Button reviewBtn;
    Button photoRegisterBtn;
    Button reviewRegisterBtn;

    Switch bookmarkSwitch;

    EditText reviewContentEditText;
    EditText photoEditText;
    private final int PICK_IMAGE = 1111;
    private static final int CROP_FROM_CAMERA = 2;
    private static final String TAG = "TestImageCropActivity";

    private Uri mImageCaptureUri;
    private AlertDialog mDialog;

    File imageFile;

    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                // .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_register);
        setRetrofitInit();

        final Intent getIntent = getIntent();
        final String foodtruckNo = getIntent.getStringExtra("foodtruckNo");

        ratingBar = (RatingBar)findViewById(R.id.ratingBar);


        reviewContentEditText = (EditText)findViewById(R.id.reviewContentEditText);
        photoEditText = (EditText)findViewById(R.id.photoEditText);

        logoutBtn = (Button)findViewById(R.id.logoutButton);
        menuBtn = (Button)findViewById(R.id.menuButton);
        introduceBtn = (Button)findViewById(R.id.introduceButton);
        reviewBtn = (Button)findViewById(R.id.reviewButton);
        reviewRegisterBtn = (Button)findViewById(R.id.reviewRegisterButton);
        photoRegisterBtn = (Button)findViewById(R.id.photoRegisterbtn);
        bookmarkSwitch = (Switch)findViewById(R.id.bookmarkSwitch);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                System.out.println(rating);
            }
        });

        bookmarkInquiry("2");

        logoutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), kr.co.fos.client.common.MainActivity.class);
                startActivity(intent);
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), kr.co.fos.client.menu.InquiryActivity.class);
                startActivity(intent);
            }
        });

        introduceBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), kr.co.fos.client.foodtruck.DetailInquiryActivity.class);
                startActivity(intent);
            }
        });

        reviewBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), kr.co.fos.client.review.InquiryActivity.class);
                startActivity(intent);
            }
        });

        //리뷰 등록 부분
        reviewRegisterBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Review review = new Review();

                review.setGrade(String.valueOf(ratingBar.getRating()));
                review.setContent(reviewContentEditText.getText().toString());
                //세션에서 멤버번호 가져오기
                review.setMemberNo(2);

                //intent에서 푸드트럭 가져오기
                review.setFoodtruckNo(2);


                System.out.println(review);
                reviewRegister(review);

                Intent intent = new Intent(getApplicationContext(), kr.co.fos.client.review.InquiryActivity.class);
                startActivity(intent);
            }
        });

        photoRegisterBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                System.out.println("사진 등록 버튼 클릭함");
                Intent intentImage = new Intent(Intent.ACTION_PICK);
                System.out.println(intentImage.getDataString());
                intentImage.setType("image/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intentImage.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                startActivityForResult(intentImage,PICK_IMAGE);
            }
        });

        bookmarkSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    //intent에서 받은 푸드트럭 번호
                    bookmarkRegister("2");
                }else if (!isChecked){
                    bookmarkDelete("2");
                }
            }
        });
    }

    //즐겨찾기 조회
    public void bookmarkInquiry(String foodtruckNo) {
        //sharedPreferences안의 로그인 정보속 회원 번호를 저장한다.
        SharedPreferences pref =getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("memberNo","2" );
        editor.commit();

        //sharedPreferences안의 로그인 정보속 회원 번호를 가져온다.
        SharedPreferences prefImport = getSharedPreferences("test", MODE_PRIVATE);
        String memberNo = prefImport.getString("memberNo","");

        //bookmark객체에 회원번호, 푸드트럭 번호 저장
        Bookmark bookmark = new Bookmark();
        bookmark.setMemberNo(memberNo);
        bookmark.setFoodtruckNo(foodtruckNo);
        System.out.println("즐겨찾기 조회 bookmarkINquiry");
        Call<ResponseBody> call = service.bookmarkInquiry(Integer.parseInt(memberNo), Integer.parseInt(foodtruckNo));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println("즐겨찾기 등록 성공");

                    if(response.body().string() != "" ){
                        bookmarkSwitch.setChecked(true);
                    } else {
                        bookmarkSwitch.setChecked(false);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(),"연결 실패",Toast.LENGTH_SHORT).show();

            }
        });

    }

    //즐겨찾기 등록
    public void bookmarkRegister(String foodtruckNo) {
        //sharedPreferences안의 로그인 정보속 회원 번호를 저장한다.
        SharedPreferences pref =getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("memberNo","2" );
        editor.commit();

        //sharedPreferences안의 로그인 정보속 회원 번호를 가져온다.
        SharedPreferences prefImport = getSharedPreferences("test", MODE_PRIVATE);
        String memberNo = prefImport.getString("memberNo","");

        //bookmark객체에 회원번호, 푸드트럭 번호 저장
        Bookmark bookmark = new Bookmark();
        bookmark.setMemberNo(memberNo);
        bookmark.setFoodtruckNo(foodtruckNo);

        Call<ResponseBody> call = service.bookmarkRegister(bookmark);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println("즐겨찾기 등록 성공");
                    System.out.println(response.body().string());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(),"연결 실패",Toast.LENGTH_SHORT).show();

            }
        });

    }

    //즐겨찾기 삭제
    public void bookmarkDelete(String foodtruckNo) {
        //sharedPreferences안의 로그인 정보속 회원 번호를 저장한다.
        SharedPreferences pref =getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("memberNo","2" );
        editor.commit();

        //sharedPreferences안의 로그인 정보속 회원 번호를 가져온다.
        SharedPreferences prefImport = getSharedPreferences("test", MODE_PRIVATE);
        String memberNo = prefImport.getString("memberNo","");

        //bookmark객체에 회원번호, 푸드트럭 번호 저장
        Bookmark bookmark = new Bookmark();
        bookmark.setMemberNo(memberNo);
        bookmark.setFoodtruckNo(foodtruckNo);

        Call<ResponseBody> call = service.bookmarkDelete(bookmark);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println("즐겨찾기 삭제 성공");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(),"연결 실패",Toast.LENGTH_SHORT).show();
            }
        });

    }

    //로그아웃
    public void logout() {
        SharedPreferences pref = getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("auth","false" );
        Intent intent = new Intent(getApplicationContext(), kr.co.fos.client.common.MainActivity.class);
    }



    //사진 등록
    public void photoRegister() {

    }

    //리뷰 등록
    public void reviewRegister(Review review) {
        Call<ResponseBody> call = service.reviewRegister(review);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println("등록 성공!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(),"연결 실패",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case PICK_IMAGE: {
                Log.d(TAG, "PICK_FROM_ALBUM");
                System.out.println("PICK_IMAGE로 들어옴");
                // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.
                // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.
                mImageCaptureUri = data.getData();
                File original_file = getImageFile(mImageCaptureUri);
                imageFile = getImageFile(mImageCaptureUri);

                System.out.println(original_file.getName());
                photoEditText.setText(original_file.getName());
                /*mImageCaptureUri = createSaveCropFile();
                File cpoy_file = new File(mImageCaptureUri.getPath());

                // SD카드에 저장된 파일을 이미지 Crop을 위해 복사한다.
                copyFile(original_file, cpoy_file);*/
            }
            case CROP_FROM_CAMERA:
            {
                Log.w(TAG, "CROP_FROM_CAMERA");

                // Crop 된 이미지를 넘겨 받습니다.
                Log.w(TAG, "mImageCaptureUri = " + mImageCaptureUri);

                String full_path = mImageCaptureUri.getPath();
                String photo_path = full_path.substring(4, full_path.length());

                Log.w(TAG, "비트맵 Image path = "+photo_path);

                Bitmap photo = BitmapFactory.decodeFile(photo_path);
                mPhotoImageView.setImageBitmap(photo);

                break;
            }

        }
    }
    private File getImageFile(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        if (uri == null) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        Cursor mCursor = getContentResolver().query(uri, projection, null, null,
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if(mCursor == null || mCursor.getCount() < 1) {
            return null; // no cursor or no record
        }
        int column_index = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        mCursor.moveToFirst();

        String path = mCursor.getString(column_index);

        if (mCursor !=null ) {
            mCursor.close();
            mCursor = null;
        }

        return new File(path);
    }

    private Uri createSaveCropFile(){
        Uri uri;
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
        return uri;
    }

    public static boolean copyFile(File srcFile, File destFile) {
        boolean result = false;
        try {
            InputStream in = new FileInputStream(srcFile);
            try {
                result = copyToFile(in, destFile);
            } finally  {
                in.close();
            }
        } catch (IOException e) {
            result = false;
        }
        return result;
    }
    private static boolean copyToFile(InputStream inputStream, File destFile) {
        try {
            OutputStream out = new FileOutputStream(destFile);
            try {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) >= 0) {
                    out.write(buffer, 0, bytesRead);
                }
            } finally {
                out.close();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    private ImageView mPhotoImageView;
}
