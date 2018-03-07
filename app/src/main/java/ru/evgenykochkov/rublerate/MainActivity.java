package ru.evgenykochkov.rublerate;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.text.DecimalFormat;

import static ru.evgenykochkov.rublerate.Constants.FILE_DOLLAR;
import static ru.evgenykochkov.rublerate.Constants.FILE_EURO;
import static ru.evgenykochkov.rublerate.Constants.FILE_POUND;
import static ru.evgenykochkov.rublerate.Constants.FILE_RATE_DOLLAR;
import static ru.evgenykochkov.rublerate.Constants.FILE_RATE_EURO;
import static ru.evgenykochkov.rublerate.Constants.FILE_RATE_POUND;

public class MainActivity extends AppCompatActivity implements LoadCurrency.IGetCurrency {

    //ключ для логов
    public static final String LOG_TAG = "BMTH";

    //сохранение, загрузка количества валюты в кошельке
    FileManager file = new FileManager(this);

    TextView allMoneyView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allMoneyView = findViewById(R.id.allMoney);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrency();
    }

    //проверка наличия интернета
    private boolean isNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    //открыть диалог, где задать количество валюты в копилке
    public void fabClicked(View view) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        CurrencyDialogFragment dialog = new CurrencyDialogFragment();
        dialog.setArguments(getMyCurrency());
        dialog.show(fragmentManager, "currencyDialog");
    }

    //сохраняет количество валюты в файл (в диалоге)
    void saveCurrency(String dollar, String euro, String pound) {

        Log.d(LOG_TAG, dollar + " долларов сохранено: " + file.save(FILE_DOLLAR, dollar));
        Log.d(LOG_TAG, euro + " евро сохранено " + file.save(FILE_EURO, euro));
        Log.d(LOG_TAG, pound + " фунтов сохранено " + file.save(FILE_POUND, pound));
    }

    //загружаем валюты из файлов
    Bundle getMyCurrency() {
        Bundle args = new Bundle();

        String dollar = file.load(FILE_DOLLAR);
        String euro = file.load(FILE_EURO);
        String pound = file.load(FILE_POUND);

        if(dollar!=null) {
            args.putString(FILE_DOLLAR, dollar);
        }

        if(dollar!=null) {
            args.putString(FILE_EURO, euro);
        }

        if(dollar!=null) {
            args.putString(FILE_POUND, pound);
        }
        return args;
    }


    //метод интерфейса, количество долларов в рублях
    @Override
    public float getDollarSum(String rate) {
        int iDollar;
        String value = file.load(FILE_DOLLAR);
        if (value!=null) {
            iDollar = Integer.valueOf(value);
        } else {
           iDollar = 0;
        }
        float fRate = Float.valueOf(rate);
        float dollarSum = iDollar*fRate;
        Log.d(LOG_TAG, "долларов в рублях: " + dollarSum);
        return dollarSum;
    }

    //метод интерфейса, количество евро в рублях
    @Override
    public float getEuroSum(String rate) {
        int iEuro;
        String value = file.load(FILE_EURO);
        if (value!=null) {
            iEuro = Integer.valueOf(value);
        } else {
            iEuro = 0;
        }
        float fRate = Float.valueOf(rate);
        float euroSum = iEuro*fRate;
        Log.d(LOG_TAG, "евро в рублях: " + euroSum);
        return euroSum;
    }

    //метод интерфейса, количество фунтов в рублях
    @Override
    public float getPoundSum(String rate) {
        int iPound;
        String value=file.load(FILE_POUND);
        if (value!=null) {
            iPound = Integer.valueOf(value);
        } else {
            iPound = 0;
        }
        float fRate = Float.valueOf(rate);
        float poundSum = iPound*fRate;
        Log.d(LOG_TAG, "фунтов в рублях: " + poundSum);
        return poundSum;
    }


    //метод интерфейса, общая сумма в рублях
    @Override
    public void getAllMoney(float dollar, float euro, float pound) {
        float fAllMoney = dollar + euro + pound;
        String allMoney = new DecimalFormat("#0.00").format(fAllMoney);
        allMoneyView.setText("в копилке " + '\n' + allMoney + '\n' + " рублей");
    }

    //получение данных по курсам валют
    void getCurrency() {
        //сеть доступна
        if (isNetworkConnection()) {
            Log.d(LOG_TAG, "подключен к сети");
            LoadCurrency load = new LoadCurrency(this);
            load.execute();
        //сеть НЕ доступна
        } else {
            float dollar = getDollarSum(file.load(FILE_RATE_DOLLAR));
            float euro = getEuroSum(file.load(FILE_RATE_EURO));
            float pound = getPoundSum(file.load(FILE_RATE_POUND));
            getAllMoney(dollar, euro, pound);
        }
    }
}
