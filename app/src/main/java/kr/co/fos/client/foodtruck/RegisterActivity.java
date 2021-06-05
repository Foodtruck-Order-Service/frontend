package kr.co.fos.client.foodtruck;

import android.app.TimePickerDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.GregorianCalendar;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.common.LoginActivity;
import kr.co.fos.client.common.MainActivity;
import kr.co.fos.client.member.Member;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    Retrofit client;
    HttpInterface service;

    int mYear, mMonth, mDay, mHour, mMinute;

    Button btnchangetime;
    Button btnchangetime2;

    TimePickerDialog dialog;

    EditText businessEdit;
    EditText foodtruckNameEdit;
    Spinner categorySpinner;

    EditText contentEdit;
    TextView photoNameView;
    File imageFile;
    Button photo_btn;
    Button submit_btn;
    private final int PICK_IMAGE = 1111;
    private static final String TAG = "TestImageCropActivity";
    Bitmap bitmapImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodtruck_register);

        setRetrofitInit();

        // EditText
        businessEdit = (EditText)findViewById(R.id.businessEdit);
        foodtruckNameEdit = (EditText)findViewById(R.id.foodtruckNameEdit);
        contentEdit = (EditText)findViewById(R.id.contentEdit);

        photoNameView = (TextView)findViewById(R.id.foodtruckPhotoNameView);

        // Button
        photo_btn = (Button)findViewById(R.id.photoButton);
        submit_btn = (Button)findViewById(R.id.submitButton);

        // Spinner
        categorySpinner = (Spinner)findViewById(R.id.spinner_category);
        ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        categorySpinner.setAdapter(categoryAdapter);

        btnchangetime = findViewById(R.id.btnchangetime);
        btnchangetime2 = findViewById(R.id.btnchangetime2);
        btnchangetime.setOnClickListener(this);
        btnchangetime2.setOnClickListener(this);
        Calendar cal = new GregorianCalendar();

        mYear = cal.get(Calendar.YEAR);

        mMonth = cal.get(Calendar.MONTH);

        mDay = cal.get(Calendar.DAY_OF_MONTH);

        mHour = cal.get(Calendar.HOUR_OF_DAY);

        mMinute = cal.get(Calendar.MINUTE);

        photo_btn.setOnClickListener(this);
        submit_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photoButton:     // 사진 등록 버튼
                System.out.println("사진 등록 버튼 클릭함");
                Intent intentImage = new Intent(Intent.ACTION_PICK);
                intentImage.setType("image/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intentImage.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                startActivityForResult(intentImage,PICK_IMAGE);

                break;
            case R.id.submitButton:    // 회원가입 버튼
                memberRegister();

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnchangetime:    // 시작시간 버튼

                dialog = new TimePickerDialog(RegisterActivity.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar, mTimeSetListener, mHour, mMinute, false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();

                break;

            case R.id.btnchangetime2:    // 종료시간 버튼
                dialog = new TimePickerDialog(RegisterActivity.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar, mTimeSetListener2, mHour, mMinute, false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();

                break;
        }
    }

    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                // .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }

    //시간 대화상자 리스너 부분
    TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    // TODO Auto-generated method stub
                    //사용자가 입력한 값을 가져온뒤
                    mHour = hourOfDay;
                    mMinute = minute;
                    btnchangetime.setText(String.format("%d:%d", mHour, mMinute));
                    //텍스트뷰의 값을 업데이트


                }

            };

    //시간 대화상자 리스너 부분
    TimePickerDialog.OnTimeSetListener mTimeSetListener2 =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    // TODO Auto-generated method stub
                    //사용자가 입력한 값을 가져온뒤
                    mHour = hourOfDay;
                    mMinute = minute;
                    btnchangetime2.setText(String.format("%d:%d", mHour, mMinute));
                    //텍스트뷰의 값을 업데이트


                }

            };

    // 사진 등록
    public void photoRegister() {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");

        switch (requestCode) {
            case PICK_IMAGE: {
                Log.d(TAG, "PICK_FROM_ALBUM");
                String uri = getRealPathFromURI(data.getData());
                imageFile = new File(uri);
                try{
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    bitmapImg = BitmapFactory.decodeStream(in);
                    in.close();
                }catch(Exception e){
                    e.printStackTrace();
                }

                photoNameView.setText(imageFile.getName());
            }
        }
    }
    //uri로 부터 절대경로 추출 메소드
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    //MultipartBody.Part 형태로 사진파일 변경
    private MultipartBody.Part insertPhoto() {
        //create a file to write bitmap data
        File f = new File(getApplicationContext().getCacheDir(), photoNameView.getText().toString());
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmapImg.compress(Bitmap.CompressFormat.PNG, 0 , bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", f.getName(), reqFile);

        return body;
    }

    // 회원 등록
    public void memberRegister() {
        Member member = (Member) getIntent().getSerializableExtra("info");
        Foodtruck foodtruck = new Foodtruck();
        MultipartBody.Part image = insertPhoto();
        foodtruck.setBrn(businessEdit.getText().toString());
        foodtruck.setName(foodtruckNameEdit.getText().toString());
        foodtruck.setCategory(categorySpinner.getSelectedItem().toString());
        foodtruck.setStartTime(btnchangetime.getText().toString());
        foodtruck.setEndTime(btnchangetime2.getText().toString());
        foodtruck.setContent(contentEdit.getText().toString());

        member.setFoodtruck(foodtruck);

        Call<ResponseBody> call = service.memberBusinessRegister(member, image);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    boolean result = gson.fromJson(response.body().string(), Boolean.class);
                    Toast.makeText(getBaseContext(),"회원 등록 :" + result,Toast.LENGTH_SHORT).show();
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
}
