package ru.evgenykochkov.rublerate;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static ru.evgenykochkov.rublerate.MainActivity.LOG_TAG;

/**
 * Created by Жека on 06.03.2018.
 */

public class LoadCurrency extends AsyncTask<Void, Void, Void> {

    public static final String URL_SBR = "http://www.cbr.ru/scripts/XML_daily.asp";

    public static final String KEY_CHAR_CODE = "CharCode";
    public static final String KEY_VALUE = "Value";
    public static final String KEY_NOMINAL = "Nominal";
    public static final String KEY_NAME = "Name";

    private String dollar;
    private String euro;
    private String pound;
    private String date;

    IGetCurrency iGetCurrency;

    LoadCurrency(IGetCurrency iGetCurrency) {
        this.iGetCurrency=iGetCurrency;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        getData();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        float fDollar = iGetCurrency.getDollarSum(dollar);
        float fEuro = iGetCurrency.getEuroSum(euro);
        float fPound = iGetCurrency.getPoundSum(pound);
        iGetCurrency.getAllMoney(fDollar, fEuro, fPound);
    }

    //лезем на сайт ЦБР и забираем дату и курс
    private void getData() {

        try {

            //определяем url
            URL url = new URL (URL_SBR);

            //соединяемся
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();

            //получаем от сервера код ответа
            int responseCode = connect.getResponseCode();
            Log.d(LOG_TAG, "пытаемся");
            //если код ответа хороший, парсим поток
            if(responseCode==HttpURLConnection.HTTP_OK) {
                InputStream input = connect.getInputStream();
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();

                Document dom = db.parse(input);
                Element docElement = dom.getDocumentElement();
                date = docElement.getAttribute("Date");
                Log.d(LOG_TAG, "дата запроса курса валют :  " + date);

                NodeList nodeList = docElement.getElementsByTagName("Valute");
                int count = nodeList.getLength();
                Log.d(LOG_TAG, "валют:  " + count + " штук");
                if (nodeList!=null && count > 0) {
                    for (int i=0; count>i; i++) {
                        Element entry = (Element) nodeList.item(i);

                        String charCode = entry
                                .getElementsByTagName(KEY_CHAR_CODE)
                                .item(0).getFirstChild()
                                .getNodeValue();

                        String value = entry
                                .getElementsByTagName(KEY_VALUE)
                                .item(0).getFirstChild()
                                .getNodeValue();

                        String nominal = entry
                                .getElementsByTagName(KEY_NOMINAL)
                                .item(0).getFirstChild()
                                .getNodeValue();

                        String name = entry
                                .getElementsByTagName(KEY_NAME)
                                .item(0).getFirstChild()
                                .getNodeValue();

                        //Log.d(LOG_TAG, "charCode:  " + charCode);
                        //Log.d(LOG_TAG, "value:  " + value);
                        //Log.d(LOG_TAG, "nominal:  " + nominal);
                        //Log.d(LOG_TAG, "name:  " + name);


                        switch (charCode) {
                            case "USD":
                                value = value.replace(",",".");
                                dollar=value;
                                Log.d(LOG_TAG, "USD:  " + value);
                                break;
                            case "EUR":
                                value = value.replace(",",".");
                                euro=value;
                                Log.d(LOG_TAG, "EUR:  " + value);
                                break;
                            case "GBP":
                                value = value.replace(",",".");
                                pound=value;
                                Log.d(LOG_TAG, "GBP:  " + value);
                                break;
                        }

                    }
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    interface IGetCurrency {
        float getDollarSum(String rate);
        float getEuroSum(String rate);
        float getPoundSum(String rate);
        void getAllMoney(float dollar, float euro, float pound);
    }
}
