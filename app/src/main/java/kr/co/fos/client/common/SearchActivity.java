package kr.co.fos.client.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.foodtruck.SearchResultActivity;

public class SearchActivity extends Fragment implements View.OnClickListener{

    Button loginBtn;
    Intent intent;
    Boolean loginCheck;

    public SearchActivity() {
        // Required empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.search, container, false);
        loginBtn = rootView.findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        loginCheck = SharedPreference.getAttribute(getContext(), "no") != null;
        if (loginCheck) {
            loginBtn.setText("로그아웃");
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.loginBtn:    // 로그인 버튼

                if (SharedPreference.getAttribute(getContext(), "no") != null) {
                    if(SharedPreference.getAttribute(getContext(), "type").equals("M")){
                        SharedPreference.removeAttribute(getContext(), "no");
                        SharedPreference.removeAttribute(getContext(), "type");
                        loginBtn.setText("로그인");
                    } else if(SharedPreference.getAttribute(getContext(), "type").equals("B")){
                        SharedPreference.removeAttribute(getContext(), "no");
                        SharedPreference.removeAttribute(getContext(), "type");
                        loginBtn.setText("로그인");
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        MainViewActivity mainViewActivity = new MainViewActivity();
                        transaction.replace(R.id.fragment_container, mainViewActivity);
                        transaction.commit();
                    }
                } else {
                    intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }
}
