package kr.co.fos.client.foodtruck;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

import kr.co.fos.client.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    int mYear, mMonth, mDay, mHour, mMinute;

    Button btnchangetime;

    Button btnchangetime2;
    TimePickerDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodtruck_register);
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

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnchangetime:    // 아이디 중복확인 버튼
                dialog = new TimePickerDialog(RegisterActivity.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar, mTimeSetListener, mHour, mMinute, false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();

                break;
            case R.id.btnchangetime2:    // 이메일 체크 버튼
                dialog = new TimePickerDialog(RegisterActivity.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar, mTimeSetListener2, mHour, mMinute, false);
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
                    btnchangetime.setText(String.format("%d : %d", mHour, mMinute));
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
                    btnchangetime2.setText(String.format("%d : %d", mHour, mMinute));
                    //텍스트뷰의 값을 업데이트


                }

            };



    // 사진 등록
    public void photoRegister() {

    }

    // 회원 등록
    public void memberRegister() {

    }
}
