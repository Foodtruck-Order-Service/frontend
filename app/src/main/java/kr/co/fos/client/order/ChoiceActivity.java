package kr.co.fos.client.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.basket.ListViewItem;
import kr.co.fos.client.menu.Option;
import kr.co.fos.client.menu.OptionValue;
import kr.co.fos.client.payment.PaymentActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChoiceActivity extends AppCompatActivity implements View.OnClickListener{
    Retrofit client;
    HttpInterface service;
    Intent intent;
    RadioButton paymentCard;
    RadioButton paymentCash;
    RadioGroup paymentRadio;
    TextView totalAmountText;
    Button paymentBtn;
    ArrayList<ListViewItem> listViewItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_choice);
        paymentCard = findViewById(R.id.paymentCard);
        paymentCash = findViewById(R.id.paymentCash);
        totalAmountText = findViewById(R.id.totalAmountText);
        paymentBtn = findViewById(R.id.paymentBtn);
        paymentBtn.setOnClickListener(this);

        intent = getIntent();
        listViewItems = (ArrayList<ListViewItem>) intent.getSerializableExtra("order");
        int totalAmount = 0;
        for(int i = 0; i < listViewItems.size(); i ++){
            ListViewItem listViewItem = listViewItems.get(i);
            totalAmount += Integer.valueOf(listViewItem.getTotalAmount());
        }
        totalAmountText.setText(String.valueOf(totalAmount));

        setRetrofitInit();
    }
    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.paymentBtn:    // ????????? ??????
                if (paymentCard.isChecked()) {
                    intent = new Intent(getApplicationContext(), PaymentActivity.class);

                    Order order = orderParsing();
                    order.setPaymentType("??????");
                    intent.putExtra("order", order);
                    startActivity(intent);
                    //??????
                } else if (paymentCash.isChecked()) {
                    Order order = orderParsing();
                    order.setPaymentType("??????");
                    orderRegister(order);
                    break;
                }  else{
                    Toast.makeText(getApplicationContext(), "?????? ????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();

                 }
                
        }
      

    }

    public Order orderParsing() {
        Order order = new Order();
        //????????????
        order.setMemberNo(Integer.valueOf(SharedPreference.getAttribute(getApplicationContext(),"no")));
        //?????? ??????
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);
        order.setOrderTime(getTime);
        //??? ??????
        int totalAmount = 0;
        for(int i = 0; i < listViewItems.size(); i ++){
            ListViewItem listViewItem = listViewItems.get(i);
            totalAmount += Integer.valueOf(listViewItem.getTotalAmount());
        }
        order.setTotalAmount(String.valueOf(totalAmount));
        order.setStatus("W");
        order.setFoodtruckNo(listViewItems.get(0).getFoodtruckNo());
        List<OrderInfo> orderInfoList = new ArrayList<>();
        for(int i = 0 ; i < listViewItems.size(); i++){
            ListViewItem listViewItem = listViewItems.get(i);
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setMenuName(listViewItem.getName());
            orderInfo.setCount(listViewItem.getCount());
            orderInfo.setAmount(listViewItem.getAmount());

            List<MenuDetail> menuDetailList = new ArrayList<>();
            for(int j = 0; j < listViewItem.getOptions().size(); j ++){
                List<Option> optionList = listViewItem.getOptions();
                Option option = optionList.get(j);
                for(int c = 0; c < option.getOptionValues().size() ; c++){
                    List<OptionValue> optionValueList = option.getOptionValues();
                    OptionValue optionValue = optionValueList.get(c);
                    MenuDetail menuDetail = new MenuDetail();
                    menuDetail.setOptionName(option.getOptionName());
                    menuDetail.setOptionValue(optionValue.getOptionValue());
                    menuDetail.setAddAmount(optionValue.getAddAmount());
                    menuDetailList.add(menuDetail);
                }
            }
            orderInfo.setMenuDetails(menuDetailList);
            orderInfoList.add(orderInfo);
        }
        order.setOrderInfos(orderInfoList);
        return order;
    }
    public void logout(){

    }

    // ?????? ??????
    public void orderRegister(Order order) {
        Call<ResponseBody> call = service.payment(order);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                intent = new Intent(getApplicationContext(), NumberActivity.class);
                int orderNo = 0;
                try {
                    orderNo = Integer.valueOf(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(orderNo);
                intent.putExtra("orderNo", orderNo);
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "???????????? ????????? ????????????.", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
