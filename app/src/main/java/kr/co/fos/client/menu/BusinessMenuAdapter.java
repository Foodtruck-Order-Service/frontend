package kr.co.fos.client.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.fos.client.R;

public class BusinessMenuAdapter extends BaseAdapter {
    private ArrayList<Menu> menuList = new ArrayList<Menu>() ;

    public BusinessMenuAdapter() {

    }

    @Override
    public int getCount() {
        return menuList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.menu_business_listview_item, parent, false);
        }

        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.photoView);
        TextView nameTextView = (TextView) convertView.findViewById(R.id.nameView);
        TextView paymentTextView = (TextView) convertView.findViewById(R.id.paymentView);
        ImageButton removeButton = (ImageButton) convertView.findViewById(R.id.removeButton);

        Menu menu = menuList.get(position);

        photoImageView.setImageResource(R.drawable.tteokbokki);
        nameTextView.setText(menu.getName());
        paymentTextView.setText(menu.getAmount() + "원");

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return menuList.get(position) ;
    }

    public void addItem(Menu menu) {
        menuList.add(menu);
    }

    public void removeItem(Menu menu) { menuList.remove(menu); }

    public void removeItemAll() {
        menuList.clear();
    };
}