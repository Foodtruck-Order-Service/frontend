package kr.co.fos.client.foodtruck;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.common.MainActivity;
import kr.co.fos.client.menu.Menu;
import kr.co.fos.client.menu.MenuAdapter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateFragment  extends Fragment implements View.OnClickListener {
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

    Button photo_btn;
    Button submit_btn;

    Foodtruck foodtruck;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetrofitInit();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.foodtruck_manage_fragment, container, false);

        Bundle bundle = getArguments();

        foodtruck = (Foodtruck) bundle.getSerializable("foodtruck");

        setRetrofitInit();

        // EditText
        businessEdit = (EditText) rootView.findViewById(R.id.businessEdit);
        foodtruckNameEdit = (EditText) rootView.findViewById(R.id.foodtruckNameEdit);
        contentEdit = (EditText) rootView.findViewById(R.id.contentEdit);

        photoNameView = (TextView) rootView.findViewById(R.id.foodtruckPhotoNameView);

        // Button
        photo_btn = (Button) rootView.findViewById(R.id.photoButton);
        submit_btn = (Button) rootView.findViewById(R.id.submitButton);

        // Spinner
        categorySpinner = (Spinner) rootView.findViewById(R.id.spinner_category);
        ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.category, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        categorySpinner.setAdapter(categoryAdapter);

        btnchangetime = rootView.findViewById(R.id.btnchangetime);
        btnchangetime2 = rootView.findViewById(R.id.btnchangetime2);
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

        // 푸드트럭 값 입력
        businessEdit.setText(foodtruck.getBrn());
        foodtruckNameEdit.setText(foodtruck.getName());
        contentEdit.setText(foodtruck.getContent());

        btnchangetime.setText(foodtruck.getStartTime());
        btnchangetime2.setText(foodtruck.getEndTime());

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photoButton:     // 사진 등록 버튼
                Toast.makeText(this.getActivity().getApplicationContext(),"사진 등록", Toast.LENGTH_SHORT).show();
                break;
            case R.id.submitButton:    // 수정하기 버튼
                foodtruckUpdate();

                Intent intent = new Intent(this.getActivity(), MainActivity.class);
                startActivity(intent);
                this.getActivity().finish();
                break;
            case R.id.btnchangetime:    // 시작시간 버튼

                dialog = new TimePickerDialog(this.getActivity(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar, mTimeSetListener, mHour, mMinute, false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();

                break;

            case R.id.btnchangetime2:    // 종료시간 버튼
                dialog = new TimePickerDialog(this.getActivity(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar, mTimeSetListener2, mHour, mMinute, false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();

                break;
        }
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

    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                // .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }

    // 푸드트럭 수정
    public void foodtruckUpdate() {
        foodtruck.setBrn(businessEdit.getText().toString());
        foodtruck.setName(foodtruckNameEdit.getText().toString());
        foodtruck.setCategory(categorySpinner.getSelectedItem().toString());
        foodtruck.setStartTime(btnchangetime.getText().toString());
        foodtruck.setEndTime(btnchangetime2.getText().toString());
        foodtruck.setContent(contentEdit.getText().toString());

        Call<ResponseBody> call = service.foodtruckUpdate(foodtruck.getNo(), foodtruck);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    boolean result = gson.fromJson(response.body().toString(), Boolean.class);
                    Toast.makeText(getActivity().getBaseContext(),"수정 완료!!",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity().getBaseContext(),"연결 실패",Toast.LENGTH_SHORT).show();
            }
        });
    }
}