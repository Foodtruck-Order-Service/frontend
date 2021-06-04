package kr.co.fos.client.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BusinessListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<BusinessListViewItem> businessListViewItemList = new ArrayList<BusinessListViewItem>();
    BaseAdapter baseAdapter = this;
    Retrofit client;
    HttpInterface service;
    Context context;
    // ListViewAdapter의 생성자
    public BusinessListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return businessListViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        context = parent.getContext();
        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.order_business_listview_item, parent, false);
        }
        setRetrofitInit();
        TextView nameText = (TextView) convertView.findViewById(R.id.nameText);
        TextView receptionNoText = (TextView) convertView.findViewById(R.id.receptionNoText);
        TextView orderTimeText = (TextView) convertView.findViewById(R.id.orderTimeText);
        TextView paymentText = (TextView) convertView.findViewById(R.id.paymentText);
        TextView totalAmountText = (TextView) convertView.findViewById(R.id.totalAmountText);
        TextView statusText = (TextView) convertView.findViewById(R.id.statusText);
        Button cancleBtn = (Button) convertView.findViewById(R.id.cancleBtn);


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        BusinessListViewItem businessListViewItem = businessListViewItemList.get(position);
        nameText.setText(businessListViewItem.getName());
        receptionNoText.setText("접수 번호 : " + businessListViewItem.getReceptionNo());

        SimpleDateFormat dateTimeFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dateTime = dateTimeFm.parse(businessListViewItem.getOrderTime());
            SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFm.format(dateTime);
            orderTimeText.setText("주문 날짜 : " + date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        paymentText.setText("결제 종류 : " + businessListViewItem.getPaymentType());
        totalAmountText.setText("총 가격 : " + businessListViewItem.getTotalAmount());
        switch(businessListViewItem.getStatus()){
            case "W" :
                statusText.setText("주문 상태 : 주문 대기");
                break;
            case "R" :
                statusText.setText("주문 상태 : 준비중");
                break;
            case "P" :
                statusText.setText("주문 상태 : 픽업 대기");
                break;
            case "C" :
                statusText.setText("주문 상태 : 주문 완료");
                break;
            case "I" :
                statusText.setText("주문 상태 : 주문 취소 대기");
                break;
            case "N" :
                statusText.setText("주문 상태 : 주문 취소");
                break;
        };
        if(statusText.getText().equals("주문 상태 : 주문 취소")){
            cancleBtn.setEnabled(false);
        }

        cancleBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                orderCancle(businessListViewItem.getNo(), pos);
            }
        });


        return convertView;
    }


    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return businessListViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(BusinessListViewItem item) {

        businessListViewItemList.add(item);
    }

    //주문 삭제
    public void orderCancle(int no,int position){
        Call<ResponseBody> call = service.orderCancel(no);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                BusinessListViewItem businessListViewItem =  businessListViewItemList.get(position);
                businessListViewItem.setStatus("N");
                businessListViewItemList.set(position,businessListViewItem);
                //결제 취소 들어가야함
                baseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "시스템에 문제가 있습니다.", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
