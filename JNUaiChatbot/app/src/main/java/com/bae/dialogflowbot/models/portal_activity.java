package com.bae.dialogflowbot.models;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bae.dialogflowbot.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;



public class portal_activity extends AppCompatActivity {

    //포털사이트
    private Button website;

    //웹 크롤링
    private String URL = "https://www.jnu.ac.kr/WebApp/web/HOM/COM/Board/board.aspx?boardID=5"; //전남대학교 홈페이지 공지사항 URL
    private TextView crawlingex;
    private Button crawlingbtn;
    private String htmlContentInStringFormat="";

    int cnt=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal_activity);

        //포털사이트
        website = findViewById(R.id.web);
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://portal.jnu.ac.kr/"));
                startActivity(intent); //액티비티 이동
            }
        });

        //웹 크롤링
        crawlingex = (TextView)findViewById(R.id.crawlingex);
        crawlingex.setMovementMethod(new ScrollingMovementMethod());

        crawlingbtn = (Button)findViewById(R.id.crawlingbtn);
        crawlingbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                System.out.println((cnt+1)+"번째 크롤링");
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();
                cnt++;
            }
        });

        crawlingbtn.performClick();
    }

    //웹 크롤링
    private class JsoupAsyncTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params){
            try{
                Document doc = Jsoup.connect(URL).get();

                //test
                Elements titles = doc.select(".board_list_table_wrap table.board_list td.title");

                for(Element e: titles){
                    System.out.println("title: "+ e.text() +"\n https://www.jnu.ac.kr/" + e.select("a").attr("href"));

                    htmlContentInStringFormat += e.text().trim() + "\n https://www.jnu.ac.kr/" + e.select("a").attr("href") + "\n\n";
                }

            } catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            crawlingex.setText(htmlContentInStringFormat);
        }
    }
}
