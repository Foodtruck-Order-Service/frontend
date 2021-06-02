package kr.co.fos.client.basket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.menu.Menu;
import kr.co.fos.client.menu.Option;
import kr.co.fos.client.menu.OptionValue;


public class InquiryActivity extends AppCompatActivity implements View.OnClickListener {
    Button paymentBtn;
    Intent intent;
    CustomListFragment customListFrgmt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basket_inquiry);

        paymentBtn = findViewById(R.id.paymentBtn);
        paymentBtn.setOnClickListener(this);

        customListFrgmt = (CustomListFragment) getSupportFragmentManager().findFragmentById(R.id.customlistfragment);

        ListViewItem list1 = new ListViewItem();
        //필수
        //주문한 회원 번호
        //푸드트럭 번호
        //푸드트럭 이름
        //주문한 메뉴 번호
        //메뉴 명

        // 총 가격(메뉴의 옵션 까지의 가격)
        // 전체 총 가격

        // 선택
        // List<옵션>
     /*   list1.setNo(1);
        list1.setName("청년상회");
        list1.setAmount(3000);
        List<Option> optionList = new ArrayList<>();
        Option option = new Option();
        option.setOptionName("크기");
        optionList.add(option);
        List<OptionValue> optionValueList = new ArrayList<>();
        OptionValue optionValue1 = new OptionValue();
        optionValue1.setOptionValue("톨 사이즈");
        optionValueList.add(optionValue1);
        OptionValue optionValue2 = new OptionValue();
        optionValue2.setOptionValue("작은 사이즈");
        optionValueList.add(optionValue2);
        option.setOptionValues(optionValueList);
        list1.setOptions(optionList);


        ListViewItem list2 = new ListViewItem();
        list2.setNo(2);
        list2.setName("도스마스");
        list2.setAmount(5000);
        list2.setCount(2);
        list2.setOptions(optionList);
        List<ListViewItem> listArray = new ArrayList<>();
        listArray.add(list1);
        listArray.add(list2);*/

        Set<Menu> basketSet = SharedPreference.getBasketList();
        Iterator<Menu> iterBasket = basketSet.iterator();
        List<ListViewItem> listViewItemList = new ArrayList<>();
        while(iterBasket.hasNext()){
            Menu menu = iterBasket.next();
            ListViewItem listViewItem = new ListViewItem();
            listViewItem.setNo(menu.getNo());
            listViewItem.setName(menu.getName());
            listViewItem.setAmount(menu.getAmount());
            listViewItem.setCount(1);
            listViewItem.setOptions(menu.getOptions());
            listViewItemList.add(listViewItem);
        }
       /* Gson gson = new Gson();
        String json = gson.toJson(listArray);
        List<ListViewItem> jsonArray = gson.fromJson(json,new TypeToken<List<ListViewItem>>(){}.getType());*/

        for(int i = 0; i < listViewItemList.size(); i++) {
            customListFrgmt.addItem(listViewItemList.get(i));
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.paymentBtn:
                Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                pament();
                break;
        }
    }

    public void pament(){
        Gson gson = new Gson();
        String json = gson.toJson(customListFrgmt.inquiryAllItem());
        Log.i(this.getClass().getName(),json);
        System.out.print("asd : " +  json);
        //결제 api

    }
}
