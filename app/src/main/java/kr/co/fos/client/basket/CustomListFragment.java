package kr.co.fos.client.basket;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.ListFragment;

import kr.co.fos.client.R;

public class CustomListFragment extends ListFragment{
    ListViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Adapter 생성 및 Adapter 지정.
        adapter = new ListViewAdapter();
        setListAdapter(adapter);

        // 첫 번째 아이템 추가.
        adapter.addItem("도", 1, 2);
        adapter.addItem("망", 12, 12);
        adapter.addItem("가", 13, 13);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void addItem(String name, int amount, int count) {
        {
            adapter.addItem(name, amount, count);
        }
    }


}
