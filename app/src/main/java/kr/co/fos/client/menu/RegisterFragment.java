package kr.co.fos.client.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.foodtruck.Foodtruck;
import kr.co.fos.client.foodtruck.FoodtruckMainActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterFragment extends Fragment {
    FoodtruckMainActivity activity;

    Retrofit client;
    HttpInterface service;

    EditText menuNameEdit;
    EditText amountEdit;
    EditText cookingTimeEdit;
    EditText explanationEdit;
    TextView photoNameText;
    private final int PICK_IMAGE = 1111;
    private static final String TAG = "TestImageCropActivity";

    File imageFile;

    Bitmap bitmapImg;
    LinearLayout listLayout;

    ImageButton addButton;
    Button registerButton;
    Button photoButton;

    Foodtruck foodtruck;

    int count = 1;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        activity = (FoodtruckMainActivity)getActivity();
    }

    @Override
    public void onDetach(){
        super.onDetach();

        activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetrofitInit();

        getParentFragmentManager().setFragmentResultListener("menu", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                foodtruck = (Foodtruck) bundle.getSerializable("foodtruck");
            }
        });

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.menu_register_fragment, container, false);

        menuNameEdit = (EditText) rootView.findViewById(R.id.menuNameEdit);
        amountEdit = (EditText) rootView.findViewById(R.id.amountEdit);
        cookingTimeEdit = (EditText) rootView.findViewById(R.id.cookingTimeEdit);
        explanationEdit = (EditText) rootView.findViewById(R.id.explanationEdit);
        photoNameText = (TextView) rootView.findViewById(R.id.foodtruckPhotoNameView);

        listLayout = (LinearLayout) rootView.findViewById(R.id.listLayout);

        addButton = (ImageButton) rootView.findViewById(R.id.addButton);
        registerButton = (Button) rootView.findViewById(R.id.submitButton);
        photoButton = (Button) rootView.findViewById(R.id.photoButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOptionView();
                count++;
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuRegister();
            }
        });
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentImage = new Intent(Intent.ACTION_PICK);
                intentImage.setType("image/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intentImage.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                startActivityForResult(intentImage,PICK_IMAGE);
            }
        });

        return rootView;
    }

    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                // .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }

    // 메뉴 등록
    public void menuRegister() {
        Menu menu = new Menu();
        MultipartBody.Part image = insertPhoto();
        menu.setFoodtruckNo(foodtruck.getNo());

        String name = menuNameEdit.getText().toString();
        String amount = amountEdit.getText().toString();
        String cookingTime = cookingTimeEdit.getText().toString();
        String explanation = explanationEdit.getText().toString();

        if (!name.equals("") && !amount.equals("") && !cookingTime.equals("") && !explanation.equals("")) {
            menu.setName(name);
            menu.setAmount(amount);
            menu.setCookingTime(cookingTime);
            menu.setContent(explanation);

            // OptionList
            List<Option> options = new ArrayList<Option>();
            for (int i = 1; i <= listLayout.getChildCount(); i++) {
                Option option = new Option();
                EditText optionName = listLayout.findViewWithTag("optionName:" + i);
                if (optionName != null) {
                    option.setOptionName(optionName.getText().toString());

                    LinearLayout valueList =  listLayout.findViewWithTag("optionValueList:" + i);
                    // OptionValueList
                    if (valueList != null) {
                        List<OptionValue> optionValues = new ArrayList<OptionValue>();
                        for (int j = 1; j <= valueList.getChildCount(); j++) {
                            OptionValue optionValue = new OptionValue();

                            EditText value = valueList.findViewWithTag("optionValue:" + j);
                            EditText addAmount = valueList.findViewWithTag("optionAmount:" + j);
                            if (value != null && addAmount!= null) {
                                optionValue.setOptionValue(value.getText().toString());
                                optionValue.setAddAmount(addAmount.getText().toString());

                                optionValues.add(optionValue);
                            }
                        }

                        option.setOptionValues(optionValues);
                        options.add(option);
                    }
                }
            }

            menu.setOptions(options);

            Call<ResponseBody> call = service.menuRegister(menu, image);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Gson gson = new Gson();
                        boolean result = gson.fromJson(response.body().string(), Boolean.class);

                        if (result) {
                            activity.onMenuFragmentChange(1);
                        }

                        System.out.println(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getActivity().getBaseContext(),"연결 실패",Toast.LENGTH_SHORT).show();
                }
            });
        } else {
           System.out.println("입력 오류");
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");

        switch (requestCode) {
            case PICK_IMAGE: {
                Log.d(TAG, "PICK_FROM_ALBUM");
                String uri = getRealPathFromURI(data.getData());
                imageFile = new File(uri);
                try{
                    InputStream in = getContext().getContentResolver().openInputStream(data.getData());
                    bitmapImg = BitmapFactory.decodeStream(in);
                    in.close();
                }catch(Exception e){
                    e.printStackTrace();
                }

                photoNameText.setText(imageFile.getName());
            }
        }
    }
    //uri로 부터 절대경로 추출 메소드
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContext().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    //MultipartBody.Part 형태로 사진파일 변경
    private MultipartBody.Part insertPhoto() {
        //create a file to write bitmap data
        File f = new File(activity.getApplicationContext().getCacheDir(), photoNameText.getText().toString());
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmapImg.compress(Bitmap.CompressFormat.PNG, 0 , bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", f.getName(), reqFile);

        return body;
    }

    public void addOptionView() {
        // 레이아웃(최상위) 생성
        LinearLayout newOptionLayout = new LinearLayout(getContext());
        newOptionLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams newOptionLayoutP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        newOptionLayoutP.bottomMargin = 20;
        newOptionLayout.setLayoutParams(newOptionLayoutP);

        //=====
        // 옵션 name 레이아웃 생성
        LinearLayout newOptionNameLayout = new LinearLayout(getContext());
        newOptionNameLayout.setOrientation(LinearLayout.HORIZONTAL);
        newOptionNameLayout.setTag("option:" + count);

        LinearLayout.LayoutParams newOptionNameLayoutP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        newOptionNameLayoutP.bottomMargin = 20;
        newOptionNameLayout.setLayoutParams(newOptionNameLayoutP);

        // 옵션 명 TextView 생성
        TextView optionNameView = new TextView(getContext());
        optionNameView.setText("옵션 이름 " + count);
        optionNameView.setTextSize(20);
        optionNameView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.fontdungle);
        optionNameView.setTypeface(typeface);
        optionNameView.setTextColor( ContextCompat.getColor(getContext(), R.color.colorText));

        LinearLayout.LayoutParams optionNameViewLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 3);
        optionNameViewLp.leftMargin = 15;
        optionNameViewLp.bottomMargin = 20;
        optionNameView.setLayoutParams(optionNameViewLp);
        // (option + count)레이아웃에 추가
        newOptionNameLayout.addView(optionNameView);

        // 옵션 명 Edit 생성
        EditText optionNameEdit = new EditText(getContext());
        optionNameEdit.setBackgroundResource(R.drawable.text_border);
        optionNameEdit.setTag("optionName:" + count);
        optionNameEdit.setEms(10);
        optionNameEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        optionNameEdit.setTextSize(20);
        optionNameEdit.setTypeface(typeface);
        optionNameEdit.setTextColor( ContextCompat.getColor(getContext(), R.color.colorText));
        LinearLayout.LayoutParams optionValueEditLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 2);
        optionValueEditLp.rightMargin = 10;
        optionValueEditLp.bottomMargin = 20;
        optionNameEdit.setLayoutParams(optionValueEditLp);
        // (option + count)레이아웃에 추가
        newOptionNameLayout.addView(optionNameEdit);
        newOptionLayout.addView(newOptionNameLayout);
        // =====

        // 옵션 value 레이아웃 생성
        LinearLayout newOptionValueLayout = new LinearLayout(getContext());
        newOptionValueLayout.setOrientation(LinearLayout.VERTICAL);
        newOptionValueLayout.setTag("optionValueList:" + count);

        LinearLayout.LayoutParams newOptionValueLayoutP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        newOptionValueLayoutP.bottomMargin = 20;
        newOptionValueLayout.setLayoutParams(newOptionValueLayoutP);
        newOptionLayout.addView(newOptionValueLayout);

        addOptionValueView(newOptionValueLayout);

        // Value add Button 생성
        Button newAddValueButton = new Button(getContext());
        newAddValueButton.setText("값 추가");
        newAddValueButton.setTextSize(34);
        newAddValueButton.setTypeface(typeface);
        newAddValueButton.setBackgroundResource(R.drawable.orange_button);
        newAddValueButton.setTextColor( ContextCompat.getColor(getContext(), R.color.colorWhite));
        LinearLayout.LayoutParams newAddValueButtonLayoutP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        newAddValueButtonLayoutP.rightMargin = 10;
        newAddValueButtonLayoutP.bottomMargin = 20;
        newAddValueButtonLayoutP.gravity = 5;
        newAddValueButton.setLayoutParams(newAddValueButtonLayoutP);

        newAddValueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOptionValueView(newOptionValueLayout);
            }
        });

        newOptionLayout.addView(newAddValueButton);

        listLayout.addView(newOptionLayout);
    }

    public void addOptionValueView(LinearLayout valueList) {
        // 레이아웃(최상위) View 생성
        LinearLayout newOptionValueLayout = new LinearLayout(getContext());
        newOptionValueLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams newOptionValueLayoutP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        newOptionValueLayoutP.bottomMargin = 20;
        newOptionValueLayout.setLayoutParams(newOptionValueLayoutP);

        // 옵션 값 View 생성
        TextView optionValueView = new TextView(getContext());
        optionValueView.setText("옵션 값 " + (valueList.getChildCount() + 1));
        optionValueView.setTextSize(20);
        optionValueView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.fontdungle);
        optionValueView.setTypeface(typeface);
        optionValueView.setTextColor( ContextCompat.getColor(getContext(), R.color.colorText));
        LinearLayout.LayoutParams optionValueLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 4);
        //optionValueLp.leftMargin = 15;
        optionValueView.setLayoutParams(optionValueLp);
        //부모 뷰에 추가
        newOptionValueLayout.addView(optionValueView);

        // 옵션 값 Edit 생성
        EditText optionValueEdit = new EditText(getContext());
        optionValueEdit.setBackgroundResource(R.drawable.text_border);
        optionValueEdit.setTag("optionValue:" + (valueList.getChildCount() + 1));
        optionValueEdit.setEms(10);
        optionValueEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        optionValueEdit.setTextSize(20);
        optionValueEdit.setTypeface(typeface);
        optionValueEdit.setTextColor( ContextCompat.getColor(getContext(), R.color.colorText));
        LinearLayout.LayoutParams optionValueEditLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 4);
        optionValueEdit.setLayoutParams(optionValueEditLp);
        //부모 뷰에 추가
        newOptionValueLayout.addView(optionValueEdit);

        // 추가 금액 View 생성
        TextView optionAmountView = new TextView(getContext());
        optionAmountView.setText("추가금액 " + (valueList.getChildCount() + 1));
        optionAmountView.setTextSize(20);
        optionAmountView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        optionAmountView.setTypeface(typeface);
        optionAmountView.setTextColor( ContextCompat.getColor(getContext(), R.color.colorText));
        LinearLayout.LayoutParams optionAmountViewLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 4);
        optionAmountView.setLayoutParams(optionAmountViewLp);
        //부모 뷰에 추가
        newOptionValueLayout.addView(optionAmountView);

        // 추가금액 Edit 생성
        EditText optionAmountEdit = new EditText(getContext());
        optionAmountEdit.setBackgroundResource(R.drawable.text_border);
        optionAmountEdit.setTag("optionAmount:" + (valueList.getChildCount() + 1));
        optionAmountEdit.setEms(10);
        optionAmountEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
        optionAmountEdit.setTextSize(20);
        optionAmountEdit.setTypeface(typeface);
        optionAmountEdit.setTextColor( ContextCompat.getColor(getContext(), R.color.colorText));
        optionAmountEdit.setKeyListener(DigitsKeyListener.getInstance("0123456789"));

        LinearLayout.LayoutParams optionAmountEditLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 4);
        optionAmountEdit.setLayoutParams(optionAmountEditLp);
        //부모 뷰에 추가
        newOptionValueLayout.addView(optionAmountEdit);

        valueList.addView(newOptionValueLayout);
    }
}

