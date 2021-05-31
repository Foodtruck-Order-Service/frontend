package kr.co.fos.client.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.foodtruck.LocationActivity;
import kr.co.fos.client.member.MyInfoActivity;

public class MainViewActivity extends Fragment implements View.OnClickListener {
    Button foodtruckLocationBtn;
    Button bookmarkBtn;
    Button infoBtn;
    Button orderBtn;
    Button basketBtn;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_fragment, container, false);
        foodtruckLocationBtn = rootView.findViewById(R.id.foodtruckLocationBtn);
        bookmarkBtn = rootView.findViewById(R.id.bookmarkBtn);
        infoBtn = rootView.findViewById(R.id.infoBtn);
        orderBtn = rootView.findViewById(R.id.orderBtn);
        basketBtn = rootView.findViewById(R.id.basketBtn);
        foodtruckLocationBtn.setOnClickListener(this);
        bookmarkBtn.setOnClickListener(this);
        infoBtn.setOnClickListener(this);
        orderBtn.setOnClickListener(this);
        basketBtn.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.foodtruckLocationBtn:    // 내 주변 푸드트럭 검색 버튼
                intent = new Intent(getContext(), LocationActivity.class);
                startActivity(intent);
                break;
            case R.id.bookmarkBtn:    // 즐겨찾기 버튼
                intent = new Intent(getContext(), kr.co.fos.client.bookmark.InquiryActivity.class);
                startActivity(intent);
                break;
            case R.id.infoBtn:    // 내 정보 버튼
                if (SharedPreference.getAttribute(getContext(), "no") != null) {
                    intent = new Intent(getContext(), MyInfoActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.orderBtn:    // 주문 버튼
                intent = new Intent(getContext(), kr.co.fos.client.order.InquiryActivity.class);
                startActivity(intent);
                break;
            case R.id.basketBtn:    // 장바구니 버튼
                intent = new Intent(getContext(), kr.co.fos.client.basket.InquiryActivity.class);
                startActivity(intent);
                break;
        }
    }
}
