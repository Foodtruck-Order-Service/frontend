package kr.co.fos.client.payment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.iamport.sdk.data.sdk.IamPortRequest;
import com.iamport.sdk.data.sdk.IamPortResponse;
import com.iamport.sdk.data.sdk.PG;
import com.iamport.sdk.data.sdk.PayMethod;
import com.iamport.sdk.domain.core.Iamport;

import java.io.IOException;
import java.util.Date;

import kotlin.Unit;
import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.basket.InquiryActivity;
import kr.co.fos.client.order.NumberActivity;
import kr.co.fos.client.order.Order;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentActivity extends AppCompatActivity {
    Retrofit client;
    HttpInterface service;
    Intent intent;
    Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        setRetrofitInit();
        Iamport.INSTANCE.init(this);
        intent = getIntent();
        order = (Order) intent.getSerializableExtra("order");
        String name = "";
        for(int i = 0; i <order.getOrderInfos().size(); i++){
            name += order.getOrderInfos().get(i).getMenuName();
            name += ", ";
        }
        name = name.substring(0, name.length()-2);

        IamPortRequest request = IamPortRequest.builder()
                .pg(PG.kcp.makePgRawName(""))
                .pay_method(PayMethod.card)
                .name(name + "를 결제합니다.")
                .merchant_uid("mid_" + (new Date()).getTime())
                .amount(order.getTotalAmount())
                .buyer_name("FOS").build();

        Iamport.INSTANCE.payment("imp32083772", request,
                iamPortApprove -> {
                    // (Optional) CHAI 최종 결제전 콜백 함수.
                    return Unit.INSTANCE;
                }, iamPortResponse -> {
                    if(iamPortResponse.getImp_success()) {
                        order.setMerchantUid(iamPortResponse.getMerchant_uid());
                        orderRegister(order);
                        String responseText = iamPortResponse.toString();
                        Log.d("IAMPORT_SAMPLE", responseText);
                        Toast.makeText(this, responseText, Toast.LENGTH_LONG).show();
                    } else {
                        intent = new Intent(getApplicationContext(), InquiryActivity.class);
                        startActivity(intent);
                    }
                    return Unit.INSTANCE;
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Iamport.INSTANCE.close();
    }

    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }

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
                Toast.makeText(getApplicationContext(), "시스템에 문제가 있습니다.", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
