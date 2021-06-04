package kr.co.fos.client.payment;

import android.app.Application;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.iamport.sdk.domain.core.Iamport;

import kr.co.fos.client.R;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Iamport.INSTANCE.create(this);
    }
}
