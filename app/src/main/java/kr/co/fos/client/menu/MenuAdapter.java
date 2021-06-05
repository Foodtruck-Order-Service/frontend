package kr.co.fos.client.menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;

import kr.co.fos.client.R;

public class MenuAdapter extends BaseAdapter {
    private ArrayList<Menu> menuList = new ArrayList<Menu>() ;

    public MenuAdapter() {

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
            convertView = inflater.inflate(R.layout.menu_listview_item, parent, false);
        }

        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.photoView);
        TextView nameTextView = (TextView) convertView.findViewById(R.id.nameView);
        TextView paymentTextView = (TextView) convertView.findViewById(R.id.paymentView);

        Menu menu = menuList.get(position);
        Glide.with(context).load("http://192.168.35.135:8080/menu/photo/" + menu.getNo()).into(photoImageView);

        nameTextView.setText(menu.getName());
        paymentTextView.setText(menu.getAmount() + "원");

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

    public void removeItemAll() {
        menuList.clear();
    };
}