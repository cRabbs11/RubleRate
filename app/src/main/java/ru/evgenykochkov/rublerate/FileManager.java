package ru.evgenykochkov.rublerate;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import static ru.evgenykochkov.rublerate.MainActivity.LOG_TAG;

/**
 * Created by Жека on 04.03.2018.
 */

public class FileManager {

    private Context context;

    FileManager(Context context) {
        this.context = context;
    }
    //читает значение из файла
    String load(String filename) {
        String value=null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    context.openFileInput(filename)));
            value = br.readLine();
            br.close();
            Log.d(LOG_TAG, "из файла: " + filename + " прочитали значение: " + value + ".");
        } catch (Throwable t) {
            Log.d(LOG_TAG, "файла " + filename +  " нету");
        }
        return value;
    }

    //сохраняет значение в файл
    boolean save(String filename, String value) {
        try {
            FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(value.getBytes());
            outputStream.close();
            Log.d(LOG_TAG, "в файл: " + filename + " сохранено значение: " + String.valueOf(value));
            return true;
        } catch (Throwable t) {
            Log.d(LOG_TAG, "сохранение не получилось ");
            return false;
        }
    }
}
