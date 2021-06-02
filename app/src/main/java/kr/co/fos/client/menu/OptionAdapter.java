package kr.co.fos.client.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kr.co.fos.client.R;

public class OptionAdapter extends BaseAdapter {
    private ArrayList<Option> optionList = new ArrayList<Option>();

    public OptionAdapter() {

    }

    @Override
    public int getCount() {
        return optionList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.option_listview_item, parent, false);
        }

        TextView optionNameView = (TextView) convertView.findViewById(R.id.optionNameView) ;
        ListView optionValueView = (ListView) convertView.findViewById(R.id.optionValueListView);

        Option option = optionList.get(position);

        OptionValueAdapter adapter = new OptionValueAdapter();
        optionValueView.setAdapter(adapter);

        optionNameView.setText(option.getOptionName());

        for (OptionValue item  : option.getOptionValues()) {
            adapter.addItem(item);
        }
        optionValueView.setAdapter(adapter);

        int numberOfItems = adapter.getCount();
        int totalItemsHeight = 0;
        for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
            View item = adapter.getView(itemPos, null, optionValueView);
            float px = 500 * (optionValueView.getResources().getDisplayMetrics().density);
            item.measure(View.MeasureSpec.makeMeasureSpec((int) px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalItemsHeight += item.getMeasuredHeight();
        }
        int totalDividersHeight = optionValueView.getDividerHeight() *
                (numberOfItems - 1);
        // Get padding
        int totalPadding = optionValueView.getPaddingTop() + optionValueView.getPaddingBottom();

        // Set list height.
        ViewGroup.LayoutParams params = optionValueView.getLayoutParams();
        params.height = totalItemsHeight + totalDividersHeight + totalPadding;
        optionValueView.setLayoutParams(params);
        optionValueView.requestLayout();

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return optionList.get(position);
    }

    public void addItem(Option option) {
        optionList.add(option);
    }

    public void removeItemAll() {
        optionList.clear();
    };
}