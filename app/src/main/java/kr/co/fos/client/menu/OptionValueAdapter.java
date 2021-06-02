package kr.co.fos.client.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kr.co.fos.client.R;

public class OptionValueAdapter extends BaseAdapter {
    private ArrayList<OptionValue> optionValueList = new ArrayList<OptionValue>();

    public OptionValueAdapter() {

    }

    @Override
    public int getCount() {
        return optionValueList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.option_value_list_item, parent, false);
        }

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        TextView valueView = (TextView) convertView.findViewById(R.id.valueView);
        TextView amountView = (TextView) convertView.findViewById(R.id.amountView);

        OptionValue optionValue = optionValueList.get(position);

        valueView.setText(optionValue.getOptionValue());
        amountView.setText("+" + optionValue.getAddAmount());

        checkBox.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (checkBox.isChecked()) {
                  optionValueList.get(position).setChecked(true);
              } else {
                  optionValueList.get(position).setChecked(false);
              }
            }
        }) ;

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return optionValueList.get(position);
    }

    public void addItem(OptionValue optionValue) {
        optionValueList.add(optionValue);
    }

    public void removeItemAll() {
        optionValueList.clear();
    };
}
