package com.app.development.stackoverflowapi.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.development.stackoverflowapi.R;

public class WebViewActivity extends AppCompatActivity {

    WebView webView;
    String url;
    String currentLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = findViewById(R.id.web);

        Intent intent = getIntent();
        url = intent.getStringExtra("URL");
        currentLink=url;

        if (url != null) {

            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(url);
            Log.e("link", url);
            webView.getSettings().setJavaScriptEnabled(true);

        }else {
            Toast.makeText(this, "Not a web link to open", Toast.LENGTH_SHORT).show();
        }

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try{
                    currentLink=url;
                }catch (Exception e){
                    currentLink="https://stackoverflow.com/";
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.share_menu: {
                callShareQuestionFunction();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void callShareQuestionFunction() {
        Intent shareQuestionIntent = new Intent(Intent.ACTION_SEND);
        String message = "Hey!! I am using Stack Overflow app(which you can download from Google Play Store) for reading the questions of stack overflow. I am sharing the link of one question which I found helpful for myself.";
        shareQuestionIntent.setType("text/plain");
        shareQuestionIntent.putExtra(Intent.EXTRA_TEXT, currentLink+"\n" + message);
        startActivity(Intent.createChooser(shareQuestionIntent, "Share question using.."));
    }
}