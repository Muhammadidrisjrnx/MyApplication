package com.example.rplrus021.myapplication;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public ArrayList<question>questionsArrayList;
    private question question;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        new load_data().execute();
    }
    @SuppressLint("StaticFieldLeak")
    public class load_data extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;

            try {
                String url = config_url.url2 + "question.php";
                System.out.println("url " + url);
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream inputStream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        inputStream, "iso-8859-1"
                ), 8);
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                inputStream.close();
                String json = stringBuilder.toString();
                jsonObject = new JSONObject(json);
            } catch (Exception e) {
                jsonObject = null;
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Log.d("hasil json ", "onPostExecute: " + jsonObject.toString());
            try {
                if (jsonObject !=null) {
                    JSONArray hasiljson = jsonObject.getJSONArray("Result");
                    questionsArrayList = new ArrayList<question>();
                    for (int i = 0; i < hasiljson.length(); i++) {
                        question = new question();
                        question.setId(hasiljson.getJSONObject(i).getInt("id_question"));
                        question.setQuestion(hasiljson.getJSONObject(i).getString("pertanyaan"));
                        questionsArrayList.add(question);
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    adapter adapter = new adapter(MainActivity.this, questionsArrayList);
                    recyclerView.setAdapter(adapter);
                }else {
                    Log.d("TAG", "onPostExecute: "+"json object null");
                }
            } catch (Exception e) {
                Log.d("errorku ", "onPostExecute: " + e.toString());
            }
        }
    }

}
