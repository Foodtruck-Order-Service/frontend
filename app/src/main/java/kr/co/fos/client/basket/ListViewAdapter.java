package kr.co.fos.client.basket;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kr.co.fos.client.PopupActivity;
import kr.co.fos.client.R;
import kr.co.fos.client.member.UpdateActivity;
import kr.co.fos.client.menu.Option;

public class ListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();


    // ListViewAdapter의 생성자
    public ListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView nameText = (TextView) convertView.findViewById(R.id.nameText);
        Button closeBtn = (Button) convertView.findViewById(R.id.closeBtn);
        TextView optionText = (TextView) convertView.findViewById(R.id.optionText);
        TextView optionValueText = (TextView) convertView.findViewById(R.id.optionValueText);
        TextView amountText = (TextView) convertView.findViewById(R.id.amountText);
        Button minusBtn = (Button) convertView.findViewById(R.id.minusBtn);
        TextView countText = (TextView) convertView.findViewById(R.id.countText);
        Button plusBtn = (Button) convertView.findViewById(R.id.plusBtn);


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);
        nameText.setText(listViewItem.getName());
        // optionText.setText((listViewItem.getOptions()).get);
        amountText.setText("가격" + listViewItem.getAmount());
        countText.setText(String.valueOf(listViewItem.getCount()));



        closeBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                listViewItemList.remove(position);
                
            }
        });

        plusBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                countText.setText(String.valueOf(Integer.valueOf(countText.getText().toString()) + 1));
            }
        });
        minusBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                countText.setText(String.valueOf(Integer.valueOf(countText.getText().toString()) - 1));
            }
        });

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
        return listViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    //, List<Option> options,추가해야함
    public void addItem(String name, int amount, int count) {
        ListViewItem item = new ListViewItem();
        item.setName(name);

        item.setAmount(amount);
        //item.setOptions(options);
        item.setCount(count);

        listViewItemList.add(item);
    }


}
