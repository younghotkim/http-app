package com.javaex.http;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListAsyncTask listAsyncTask = new ListAsyncTask();

        listAsyncTask.execute();



    }

    //InnerClass

    public class ListAsyncTask extends AsyncTask<Void, Integer, List<GuestbookVo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<GuestbookVo> doInBackground(Void... voids) {

            List<GuestbookVo> guestbookList = null;

            try {


                URL url = new URL("http://192.168.0.108:8088/mysite5/api/guestbook/list");  //url 생성

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();  //url 연결
                conn.setConnectTimeout(10000); // 10초 동안 기다린 후 응답이 없으면 종료
                conn.setRequestMethod("POST"); // 요청방식 POST
                conn.setRequestProperty("Content-Type", "application/json"); //요청시 데이터 형식 json
                conn.setRequestProperty("Accept", "application/json"); //응답시 데이터 형식 json
                conn.setDoOutput(true); //OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
                conn.setDoInput(true); //InputStream으로 서버로 부터 응답을 받겠다는 옵션.

                int resCode = conn.getResponseCode(); // 응답코드 200이 정상

                if (resCode == 200) { //정상이면

                    Log.d("javaStudy", "" + resCode);



                    //Stream 을 통해 통신한다
                    //데이타 형식은 json으로 한다.

                    InputStream is = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is,"UTF-8");
                    BufferedReader br = new BufferedReader(isr);

                    String jsonData = "";

                    while (true) {

                        String line = br.readLine();

                        if(line == null) {

                            break;

                        }

                        jsonData = jsonData + line;

                    }

                    Log.d("javaStudy","" + jsonData);

                    Gson gson = new Gson();
                    guestbookList = gson.fromJson(jsonData, new TypeToken<List<GuestbookVo>>(){}.getType());

                    //Log.d("javaStudy","size---->" + guestbookList.size());
                    //Log.d("javaStudy","size---->" + guestbookList.get(0).getName());


                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return guestbookList;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<GuestbookVo> guestbookList) {
            super.onPostExecute(guestbookList);

            Log.d("javaStudy","onPostExecute() size---->" + guestbookList.size());
            Log.d("javaStudy","onPostExecute() size---->" + guestbookList.get(0).getName());


        }
    };






}