package kr.co.fos.client.review;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.bookmark.Bookmark;
import kr.co.fos.client.foodtruck.Foodtruck;
import kr.co.fos.client.foodtruck.FoodtruckMainActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterFragment extends Fragment {
    FoodtruckMainActivity foodtruckMainActivity;

    Retrofit client;
    HttpInterface service;

    RatingBar ratingBar;

    Button photoRegisterBtn;
    Button reviewRegisterBtn;

    EditText reviewContentEditText;
    EditText photoEditText;
    private final int PICK_IMAGE = 1111;
    private static final String TAG = "TestImageCropActivity";

    File imageFile;

    Foodtruck foodtruck;

    Bitmap bitmapImg;
    Review review;
    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                // .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        foodtruckMainActivity = (FoodtruckMainActivity)getActivity();
    }

    public void onDetch(){
        super.onDetach();

        foodtruckMainActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetrofitInit();
        review = new Review();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.review_register_fragment, container, false);

        foodtruck = (Foodtruck) getActivity().getIntent().getSerializableExtra("foodtruck");

        ratingBar = (RatingBar)rootView.findViewById(R.id.ratingBar);
        System.out.println("ratingbar : " + ratingBar.getRating());

        reviewContentEditText = (EditText)rootView.findViewById(R.id.reviewContentEditText);
        photoEditText = (EditText)rootView.findViewById(R.id.photoEditText);

        reviewRegisterBtn = (Button)rootView.findViewById(R.id.reviewRegisterButton);
        photoRegisterBtn = (Button)rootView.findViewById(R.id.photoRegisterbtn);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                System.out.println(rating);
            }
        });

        //리뷰 등록 부분
        reviewRegisterBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String memberNo = SharedPreference.getAttribute(foodtruckMainActivity.getApplicationContext(),"no");
                review.setGrade(String.valueOf(ratingBar.getRating()));
                review.setContent(reviewContentEditText.getText().toString());
                //세션에서 멤버번호 가져오기
                review.setMemberNo(Integer.parseInt(memberNo));

                //intent에서 푸드트럭 가져오기
                review.setFoodtruckNo(foodtruck.getNo());

                reviewRegister(review);

                foodtruckMainActivity.onFragmentChange(1);
            }
        });

        photoRegisterBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                System.out.println("사진 등록 버튼 클릭함");
                Intent intentImage = new Intent(Intent.ACTION_PICK);
                intentImage.setType("image/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intentImage.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                startActivityForResult(intentImage,PICK_IMAGE);
            }
        });

        return rootView;
    }

    //리뷰 등록
    public void reviewRegister(Review review) {
        //사진파일 가져오기
        MultipartBody.Part image = insertPhoto();

        Call<ResponseBody> call = service.reviewRegister(review,image);
        System.out.println(image.body());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println("" + response.body().string());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(foodtruckMainActivity.getBaseContext(),"연결 실패",Toast.LENGTH_SHORT).show();
            }
        });
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
                    InputStream in = getContext().getContentResolver().openInputStream(data.getData());
                    bitmapImg = BitmapFactory.decodeStream(in);
                    in.close();
                }catch(Exception e){
                    e.printStackTrace();
                }

                photoEditText.setText(imageFile.getName());
            }
        }
    }
    //uri로 부터 절대경로 추출 메소드
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContext().getContentResolver().query(contentURI, null, null, null, null);
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
        File f = new File(foodtruckMainActivity.getApplicationContext().getCacheDir(), photoEditText.getText().toString());
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
}
