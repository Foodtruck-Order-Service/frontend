package kr.co.fos.client.menu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import kr.co.fos.client.R;
import kr.co.fos.client.foodtruck.FoodtruckMainActivity;

public class OptionViewAdapter extends BaseAdapter {
    Activity activity;
    RegisterFragment fragment;
    LinearLayout container;

    private ArrayList<Option> optionList = new ArrayList<Option>();

    public OptionViewAdapter() {
        super();
    }

    public OptionViewAdapter(Activity activity, RegisterFragment fragment) {
        super();
        this.activity = activity;
        this.fragment = fragment;
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
            convertView = inflater.inflate(R.layout.menu_register_listview_item, parent, false);
        }

        container = (LinearLayout) convertView.findViewById(R.id.listLayout);

        EditText optionNameEdit = (EditText) convertView.findViewById(R.id.optionNameEdit);
        EditText optionValueEdit = (EditText) convertView.findViewById(R.id.optionValueEdit);
        EditText optionAmountEdit = (EditText) convertView.findViewById(R.id.optionAmountEdit);
        Button addOptionValueButton = (Button) convertView.findViewById(R.id.addOptionValueButton);

        addOptionValueView();

        if (optionList.size() > 0) {

        }

        addOptionValueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOptionValueView();
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
        return optionList.get(position);
    }

    public void addItem(Option option) {
        optionList.add(option);
    }

    public void removeItemAll() {
        optionList.clear();
    };

    public void addOptionValueView() {
        // ?????? ??? View ??????
        TextView optionValueView = new TextView(fragment.getContext());
        optionValueView.setText("?????? ??? " + (getCount() + 1));
        optionValueView.setTextSize(20);
        optionValueView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        optionValueView.setTextColor(Color.BLACK);

        LinearLayout.LayoutParams optionValueLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30);
        optionValueLayout.weight = 3;
        optionValueLayout.leftMargin = 15;
        optionValueLayout.bottomMargin = 40;
        optionValueView.setLayoutParams(optionValueLayout);
        //?????? ?????? ??????
        container.addView(optionValueView);

        // ?????? ??? Edit ??????
        EditText optionValueEdit = new EditText(fragment.getContext());
        optionValueEdit.setBackgroundResource(R.drawable.text_border);
        optionValueEdit.setEms(10);
        optionValueEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        optionValueEdit.setTextSize(22);

        LinearLayout.LayoutParams optionValueEditLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30);
        optionValueEditLp.weight = 3;
        optionValueEditLp.bottomMargin = 20;
        optionValueEdit.setLayoutParams(optionValueEditLp);
        //?????? ?????? ??????
        container.addView(optionValueEdit);

        // ?????? ?????? View ??????
        TextView optionAmountView = new TextView(fragment.getContext());
        optionAmountView.setText("???????????? " + (getCount() + 1));
        optionAmountView.setTextSize(20);
        optionAmountView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        optionAmountView.setTextColor(Color.BLACK);

        LinearLayout.LayoutParams optionAmountViewLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30);
        optionAmountViewLayout.weight = 3;
        optionAmountViewLayout.bottomMargin = 40;
        optionAmountView.setLayoutParams(optionAmountViewLayout);
        //?????? ?????? ??????
        container.addView(optionAmountView);

        // ???????????? Edit ??????
        EditText optionAmountEdit = new EditText(fragment.getContext());
        optionAmountEdit.setBackgroundResource(R.drawable.text_border);
        optionAmountEdit.setEms(10);
        optionAmountEdit.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
        optionAmountEdit.setTextSize(22);

        LinearLayout.LayoutParams optionAmountEditLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30);
        optionAmountEditLp.weight = 3;
        optionAmountEditLp.bottomMargin = 20;
        optionAmountEdit.setLayoutParams(optionAmountEditLp);
        //?????? ?????? ??????
        container.addView(optionAmountEdit);
    }
}
