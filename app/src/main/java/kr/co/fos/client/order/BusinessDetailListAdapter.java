package kr.co.fos.client.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.basket.ListViewItem;
import kr.co.fos.client.menu.Menu;
import kr.co.fos.client.menu.Option;
import kr.co.fos.client.menu.OptionValue;

public class BusinessDetailListAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<BusinessDetailListViewItem> businessDetailListViewItemList = new ArrayList<BusinessDetailListViewItem>();
    BaseAdapter baseAdapter = this;

    // ListViewAdapter의 생성자
    public BusinessDetailListAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return businessDetailListViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.order_business_detail_listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView nameText = (TextView) convertView.findViewById(R.id.nameText);
        TextView countText = (TextView) convertView.findViewById(R.id.countText);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        BusinessDetailListViewItem businessDetailListViewItem = businessDetailListViewItemList.get(position);
        nameText.setText(businessDetailListViewItem.getName());
        countText.setText(String.valueOf(businessDetailListViewItem.getCount()));
        Toast.makeText(context, businessDetailListViewItem.toString(), Toast.LENGTH_SHORT).show();
        List<String> list = new ArrayList<>();
        Gson gson = new Gson();

        if (businessDetailListViewItem.getMenuDetail() != null) {
            for (int i = 0; i < businessDetailListViewItem.getMenuDetail().size(); i++) {
                MenuDetail menuDetail = businessDetailListViewItem.getMenuDetail().get(i);
                list.add(menuDetail.getOptionName() + " : " + menuDetail.getOptionValue());
            }
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list);
            ListView listView = (ListView) convertView.findViewById(R.id.listview1);


            int numberOfItems = itemsAdapter.getCount();
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = itemsAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int) px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Get padding
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();

            listView.setAdapter(itemsAdapter);
        }



        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return businessDetailListViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    //, List<Option> options,추가해야함
    public void addItem(BusinessDetailListViewItem item) {
        businessDetailListViewItemList.add(item);
    }

    public ArrayList<BusinessDetailListViewItem> inquiryAllItem() {
        return businessDetailListViewItemList;
    }

}
