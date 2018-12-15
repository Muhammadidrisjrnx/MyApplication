package com.example.rplrus021.myapplication;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    private ViewPager viewPager;
    private LinearLayout linearLayout;
    private TextView[] textViews;
    private Button button_back, button_next;
    private int state_page = 0;
    private slide_adapter slide_adapter;
    private ArrayList<question>questionArrayList;
    private question question;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.view_pager_intro);
        radioGroup = (RadioGroup)findViewById(R.id.radio);
        button_back = (Button) findViewById(R.id.button_back);
        button_back.setText("");
        button_next = (Button) findViewById(R.id.button_next);
        button_next.setText("Next");
        new load_data().execute();

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state_page = getItem(+1);
                viewPager.setCurrentItem(state_page);

            }
        });
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state_page = getItem(-1);
                viewPager.setCurrentItem(state_page);
            }
        });

    }

    ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            state_page = i;
            if (i == 0){
                button_back.setVisibility(View.INVISIBLE);
                button_back.setEnabled(false);
                button_next.setVisibility(View.VISIBLE);
                button_next.setEnabled(true);
            }else if (i == questionArrayList.size()-1){
                button_back.setText("back");
                button_back.setVisibility(View.VISIBLE);
                button_back.setEnabled(true);
                button_next.setVisibility(View.VISIBLE);
                button_next.setEnabled(true);
                button_next.setText("Finish");
            }else {
                button_back.setText("back");
                button_back.setVisibility(View.VISIBLE);
                button_back.setEnabled(true);
                button_next.setVisibility(View.VISIBLE);
                button_next.setEnabled(true);
                button_next.setText("next");
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private int getItem(int i){
        return viewPager.getCurrentItem()+i;
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
                if (jsonObject != null) {
                    JSONArray hasiljson = jsonObject.getJSONArray("Result");
                    questionArrayList = new ArrayList<question>();
                    for (int i = 0; i < hasiljson.length(); i++) {
                        question = new question();
                        question.setId(hasiljson.getJSONObject(i).getInt("id_question"));
                        question.setQuestion(hasiljson.getJSONObject(i).getString("pertanyaan"));
                        questionArrayList.add(question);
                    }
                    slide_adapter = new slide_adapter(MainActivity.this,questionArrayList);
                    viewPager.setAdapter(slide_adapter);
                    viewPager.addOnPageChangeListener(listener);
                } else {
                    Log.d("hasil json", "onPostExecute: " + "json object null");
                }
            } catch (Exception e) {
                Log.d("errorku ", "onPostExecute: " + e.toString());
            }
        }
    }

}
