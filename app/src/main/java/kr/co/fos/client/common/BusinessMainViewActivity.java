package kr.co.fos.client.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.foodtruck.BusinessStartActivity;
import kr.co.fos.client.foodtruck.FoodtruckMainActivity;
import kr.co.fos.client.foodtruck.SearchResultActivity;
import kr.co.fos.client.member.MyInfoActivity;
import kr.co.fos.client.menu.InfoActivity;
import kr.co.fos.client.order.BusinessInquiryActivity;
import kr.co.fos.client.sales.InquiryAcivity;

public class BusinessMainViewActivity extends Fragment implements View.OnClickListener{
    Button startSalesBtn;
    Button foodtruckManageBtn;
    Button orderManageBtn;
    Button salesManageBtn;
    Button infoBtn;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.business_main_fragment, container, false);
        startSalesBtn = rootView.findViewById(R.id.startSalesBtn);
        foodtruckManageBtn = rootView.findViewById(R.id.foodtruckManageBtn);
        orderManageBtn = rootView.findViewById(R.id.orderManageBtn);
        salesManageBtn = rootView.findViewById(R.id.salesManageBtn);
        infoBtn = rootView.findViewById(R.id.infoBtn);

        startSalesBtn.setOnClickListener(this);
        foodtruckManageBtn.setOnClickListener(this);
        orderManageBtn.setOnClickListener(this);
        salesManageBtn.setOnClickListener(this);
        infoBtn.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.startSalesBtn:    // 영업 시작 버튼
                intent = new Intent(getContext(), BusinessStartActivity.class);
                startActivity(intent);
                break;
            case R.id.foodtruckManageBtn:    // 푸드트럭 관리 버튼
                // 나중에 추가해야함
              //  intent = new Intent(getContext(), FoodtruckMainActivity.class);
              //  startActivity(intent);
                break;
            case R.id.orderManageBtn:    // 주문 관리 버튼
                intent = new Intent(getContext(), BusinessInquiryActivity.class);
                startActivity(intent);
                break;
            case R.id.salesManageBtn:    // 매출 관리 버튼
                intent = new Intent(getContext(), InquiryAcivity.class);
                startActivity(intent);
                break;
            case R.id.infoBtn:    // 내 정보 버튼
                if(SharedPreference.getAttribute(getContext(), "no") != null) {
                    intent = new Intent(getContext(), MyInfoActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getContext(),LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
        //검색

    }
}
