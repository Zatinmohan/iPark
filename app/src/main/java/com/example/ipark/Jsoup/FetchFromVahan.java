package com.example.ipark.Jsoup;

import android.os.AsyncTask;

import com.example.ipark.Interfaces.AsyncResponse;
import com.example.ipark.Models.Vehicle;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class FetchFromVahan extends AsyncTask<String, Void, Vehicle>
{
    private final String BASE_URL = "https://parivahan.gov.in";
    private final String VEHICLE_URL="/rcdlstatus/vahan/rcDlHome.xhtml";


    /*private final String BASE_URL = "https://vahan.nic.in";
    private final String VEHICLE_URL="/nrservices/faces/user/searchstatus.xhtml";*/


    private Vehicle vehicle;
    private int statusCode;
    private AsyncResponse response = null;
    boolean flag=false;

    private final int SOCKET_TIMEOUT = 408;
    private final int CAPTCHA_FAILED = 999;
    private final int TECHNICAL_DIFFICULTY = 888;
    private String getAbsoluteURL(String url)
    {
        return BASE_URL + url;
    }
    public FetchFromVahan(AsyncResponse response)
    {
        this.response = response;
    }

    @Override
    protected Vehicle doInBackground(String... params)
    {
        try
        {
            Connection connection = Jsoup.connect(getAbsoluteURL(VEHICLE_URL))
                    .userAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.101 Safari/537.36")
                    .referrer(getAbsoluteURL(BASE_URL))
                    //.data("javax.faces.partial.ajax", "true")
                    .data("javax.faces.source", GetCaptcha.formNumber)
                    .data("javax.faces.partial.execute", "@all")
                    //.data("javax.faces.partial.render", "form_rcdl:pnl_show form_rcdl:pg_show form_rcdl:rcdl_pnl")
                    .data(GetCaptcha.formNumber, GetCaptcha.formNumber)
                    .data("form_rcdl", "form_rcdl")
                    .data("form_rcdl:tf_reg_no1", params[0])
                    .data("form_rcdl:tf_reg_no2", params[1])
                    .data("form_rcdl:j_idt32:CaptchaID", params[2])
                    .data("javax.faces.ViewState", GetCaptcha.viewState)
                    .timeout(10000)
                    .cookies(GetCaptcha.cookies);

            Document document = connection.get();
            statusCode = connection.response().statusCode();

            Elements body = document.select("body#mymain");
            Elements form = body.select("form#form_rcdl");
            Elements table = form.select("table");


            if(table.size()!=0)
            {
                Elements rows = table.select("tr");

                String[] results = new String[13];
                int k=0;
                for(int i=0;i<rows.size();i++)
                {
                    Element row = rows.get(i);

                    Elements cols = row.select("td");

                    for(int j=1;j<cols.size();j+=2)
                    {
                        results[k++]=cols.get(j).text().toString().trim();
                    }
                }

                vehicle = new Vehicle(results[0],results[1],results[2],results[3],results[4],results[5],results[6],results[7],results[8],results[9],results[10],results[11]);
            }
            else
            {
                /*Elements error_div=document.select("div#form_rcdl:j_idt16");
                Elements error_div2=error_div.select("div.ui-messages-error ui-corner-all");*/
                if(Jsoup.parse(document.text()).body().getElementsByClass("ui-messages-error-detail").size() != 0)
                    statusCode = CAPTCHA_FAILED;

                //vehicle = null;
            }
        }
        catch (SocketTimeoutException e)
        {
            statusCode = SOCKET_TIMEOUT;
        }
        catch (IOException e) {e.printStackTrace();}
        catch (ArrayIndexOutOfBoundsException e){ statusCode = TECHNICAL_DIFFICULTY; }

        return vehicle;
    }

    @Override
    protected void onPostExecute(Vehicle vehicle)
    {
        response.processFinish(vehicle, statusCode);
    }
}