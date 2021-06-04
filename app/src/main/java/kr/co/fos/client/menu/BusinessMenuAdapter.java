package kr.co.fos.client.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.util.ArrayList;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.PopupActivity;
import kr.co.fos.client.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

public class BusinessMenuAdapter extends BaseAdapter {
    private ArrayList<Menu> menuList = new ArrayList<Menu>();
    Context context;
    Activity activity;
    Fragment fragment;

    public BusinessMenuAdapter(Activity activity, Fragment fragment) {
        super();
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return menuList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        context = parent.getContext();

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

        BusinessMenuAdapter adapterMe = this;

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PopupActivity.class);
                intent.putExtra("data", "정말로 삭제하겠습니까?");
                activity.getIntent().putExtra("menu", menu);

                fragment.startActivityForResult(intent, 2);
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