package kr.co.fos.client.basket;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import kr.co.fos.client.R;

public class InquiryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basket_inquiry);
        CustomListFragment customListFrgmt = (CustomListFragment) getSupportFragmentManager().findFragmentById(R.id.customlistfragment);
        //customListFrgmt.addItem("New Box", "New Account Box Black 36dp") ;
    }
    //로그아웃
    public void logout() {
        
    }

    //장바구니 조회
    public void basketInquiry() {

    }

    //장바구니 삭제
    public void basketDelete() {

    }
}
