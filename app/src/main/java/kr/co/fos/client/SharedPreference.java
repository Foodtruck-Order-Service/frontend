package kr.co.fos.client;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import kr.co.fos.client.menu.Menu;

public class SharedPreference {
    private static Set<Menu> basketList;

    static {
        if (basketList == null) {
            basketList = new LinkedHashSet<Menu>();
        }
    }

    //값 저장
    public static void setAttribute(Context context, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    //값 읽기
    public static String getAttribute(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(key, null);
    }

    //데이터 삭제
    public static void removeAttribute(Context context, String key) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }

    //모든 데이터삭제
    public static void removeAllAttribute(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public static Set<Menu> getBasketList() {

        return basketList;
    }

    public static void addBasketList(Menu menu) {
        basketList.add(menu);
    }
}


