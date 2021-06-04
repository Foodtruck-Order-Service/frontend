package kr.co.fos.client.basket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.common.MainActivity;
import kr.co.fos.client.foodtruck.SearchResultActivity;
import kr.co.fos.client.menu.Menu;
import kr.co.fos.client.menu.Option;
import kr.co.fos.client.menu.OptionValue;
import kr.co.fos.client.order.ChoiceActivity;


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


        Set<Menu> basketSet = SharedPreference.getBasketList();
        Iterator<Menu> iterBasket = basketSet.iterator();
        List<ListViewItem> listViewItemList = new ArrayList<>();
        while(iterBasket.hasNext()){
            Menu menu = iterBasket.next();
            ListViewItem listViewItem = new ListViewItem();
            listViewItem.setNo(menu.getNo());
            listViewItem.setFoodtruckNo(menu.getFoodtruckNo());
            listViewItem.setName(menu.getName());

            int OptionValueAmount = 0;
            if(menu.getOptions() != null) {
                for (int i = 0; i < menu.getOptions().size(); i++) {
                    Option option = menu.getOptions().get(i);
                    for (int j = 0; j < option.getOptionValues().size(); j++) {
                        OptionValue optionValue = option.getOptionValues().get(j);
                        OptionValueAmount += Integer.valueOf(optionValue.getAddAmount());
                    }
                }
            }
            listViewItem.setAmount(String.valueOf(Integer.valueOf(menu.getAmount()) + OptionValueAmount));
            listViewItem.setTotalAmount(String.valueOf(Integer.valueOf(menu.getAmount()) + OptionValueAmount));
            listViewItem.setCount(1);
            listViewItem.setHash(menu.hashCode());
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
                System.out.print(customListFrgmt.inquiryAllItem());
                if(!customListFrgmt.inquiryAllItem().isEmpty()) {
                    intent = new Intent(getApplicationContext(), ChoiceActivity.class);
                    intent.putExtra("order", customListFrgmt.inquiryAllItem());
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "장바구니에 메뉴가 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
