package ru.evgenykochkov.rublerate;

import android.util.Log;

import java.text.SimpleDateFormat;

/**
 * Created by Жека on 25.02.2018.
 */

public class Data {

    public String getData() {
        String data;
        long date = System.currentTimeMillis();
        SimpleDateFormat dd = new SimpleDateFormat("dd");
        SimpleDateFormat M = new SimpleDateFormat("MM");
        SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
        String sdd = dd.format(date);
        String sM = M.format(date);
        String syyyy = yyyy.format(date);
        data = sdd + "." + sM + "." + syyyy;
        Log.d(MainActivity.LOG_TAG, "сегодня: " + data);
        return data;
    }
}
