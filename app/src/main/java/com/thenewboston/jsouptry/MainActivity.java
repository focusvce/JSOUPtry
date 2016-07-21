package com.thenewboston.jsouptry;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Connection connection;
    Document document;
    ImageView imageView;
    EditText editText;
    Button button;
    String url = "http://stackoverflow.com/questions/10168066/how-to-print-out-all-the-elements-of-a-list-in-java";
    String imgurl = null;
    TextView textView,descr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        editText = (EditText) findViewById(R.id.url);
        button = (Button) findViewById(R.id.submit);
        textView = (TextView) findViewById(R.id.title);
        descr = (TextView) findViewById(R.id.desc);
        imageView = (ImageView) findViewById(R.id.image);
       // Glide.with(this).load(imgurl).into(imageView);

    }
    public String getUrl(String urll){
        connection = Jsoup.connect(urll).userAgent("Mozilla");
        try {
            document = connection.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements metaOgImage = document.select("meta[property=og:image]");
        if (metaOgImage.size() != 0 && metaOgImage != null) {
            Toast.makeText(this, Integer.toString(metaOgImage.size()), Toast.LENGTH_SHORT).show();
            for (int i = 0; i < metaOgImage.size(); i++) {
                if (metaOgImage.get(i).attr("content") != "") {
                    imgurl = metaOgImage.get(i).attr("content");
                    Toast.makeText(this,imgurl,Toast.LENGTH_SHORT).show();
                    break;
                } else
                    continue;
            }
        } else if (imgurl == "" || imgurl == null) {
            Elements pngs = document.select("img");
            Toast.makeText(this, Integer.toString(pngs.size())+"else", Toast.LENGTH_LONG).show();
            for (int i = 0; i < pngs.size(); i++) {
                if (pngs.get(i).attr("src") != "") {
                    imgurl = pngs.get(i).attr("src");
                    break;
                } else
                    continue;
            }
        }
        Log.e("ADARSH",imgurl);
        Elements metaOgDescription = document.select("meta[property=og:description]");
        String desc = null;
        if(metaOgDescription.size()!=0)
            desc = metaOgDescription.get(0).attr("content");
        if(desc!=null)
            descr.setText(desc);
        String title = document.title();
        textView.setText(title);
        return imgurl;
    }
    public void clicked(View v){
        url=editText.getText().toString();
        imgurl=getUrl(url);
        imageView = (ImageView) findViewById(R.id.image);
        TextView t = (TextView) findViewById(R.id.imgurl);
        t.setText(imgurl);
        //Glide.with(this).load(imgurl).into(imageView);
       // if(imageView.getDrawable()==null) {
            //Toast.makeText(this, "IMAGE VIEW NULL", Toast.LENGTH_SHORT).show();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(imgurl).getContent());
                imageView.setImageBitmap(bitmap);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
       // }

    }
}
