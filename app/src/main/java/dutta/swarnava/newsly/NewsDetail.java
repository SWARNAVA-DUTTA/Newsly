package dutta.swarnava.newsly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class NewsDetail extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{
    private AppBarLayout appBarLayout;
//    private TextView appbar_subtitle;
//    private TextView appbar_title;
    private TextView date;
    private FrameLayout date_behavior;
    private ImageView imageView;
    private boolean isHideToolbarView = false;
    private String mAuthor;
    private String mDate;
    private String mImg;
    private String mSource;
    private String mTitle;
    private String mUrl;
    private TextView time;
    private TextView title;
    private LinearLayout titleAppbar;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar( toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar)).setTitle("");
         appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
         appBarLayout.addOnOffsetChangedListener(this);
         date_behavior = (FrameLayout) findViewById(R.id.date_behavior);
         imageView = (ImageView) findViewById(R.id.backdrop);
//         titleAppbar = (LinearLayout) findViewById(R.id.title_appbar);
//         appbar_title = (TextView) findViewById(R.id.title_on_appbar);
//         appbar_subtitle = (TextView) findViewById(R.id.subtitle_on_appbar);
         date = (TextView) findViewById(R.id.date);
         time = (TextView) findViewById(R.id.time);
         title = (TextView) findViewById(R.id.title);
        Intent intent = getIntent();
         mUrl = intent.getStringExtra("url");
         mImg = intent.getStringExtra("img");
//         mTitle = intent.getStringExtra(SettingsJsonConstants.PROMPT_TITLE_KEY);
         mDate = intent.getStringExtra("date");
         mSource = intent.getStringExtra("source");
         mAuthor = intent.getStringExtra("author");
         mTitle=intent.getStringExtra("title");
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error((Drawable) Utils.getRandomDrawbleColor());
        Glide.with((FragmentActivity) this).load( mImg)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into( imageView);
        imageView.setAlpha((float) 0.5);
        imageView.setColorFilter(Color.argb(1, 255, 255, 255));
//         appbar_title.setText( mSource);
//         appbar_subtitle.setText( mUrl);
         date.setText(Utils.DateFormat( mDate));
         title.setText(mTitle);
         String author=null;
        if ( mAuthor != null || mAuthor !="") {
            mAuthor=" \u2022 "+mAuthor;
        } else {
            author = "";
        }
        time.setText(mSource + author + " \u2022 "+Utils.DateToTimeFormat( mDate));
        initWebView( mUrl);
    }
    private void initWebView(String str) {
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(str);
    }
@Override
public void onBackPressed() {
//    if (this.mInterstitialAd.isLoaded()) {
//        this.mInterstitialAd.show();
//    } else {
//        Log.d("TAG", "The interstitial wasn't loaded yet.");
//    }
//    this.mInterstitialAd.setAdListener(new AdListener() {
//        public void onAdClosed() {
//            new Handler().postDelayed(new Runnable() {
//                public void run() {
//                    NewsDetailActivity.this.supportFinishAfterTransition();
//                }
//            }, 200);
//        }
//    });
    super.onBackPressed();
    supportFinishAfterTransition();
}
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout2, int i) {
        float abs = ((float) Math.abs(i)) / ((float) appBarLayout2.getTotalScrollRange());
        if (abs == 1.0f && isHideToolbarView) {
            date_behavior.setVisibility(View.VISIBLE);
//            titleAppbar.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        } else if (abs < 1.0f && !isHideToolbarView) {
            date_behavior.setVisibility(View.GONE);
//            titleAppbar.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.view_web) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(this.mUrl));
            startActivity(intent);
            return true;
        }
        if (itemId == R.id.share) {
            try {
                Intent intent2 = new Intent("android.intent.action.SEND");
                intent2.setType("text/plan");
                intent2.putExtra("android.intent.extra.SUBJECT", this.mSource);
                StringBuilder sb = new StringBuilder();
                sb.append(this.mTitle);
                sb.append("\n");
                sb.append(this.mUrl);
                sb.append("\n\nShared from Newsly\nDownload the app here : \n url");
                intent2.putExtra("android.intent.extra.TEXT", sb.toString());
                startActivity(Intent.createChooser(intent2, "Share with :"));
            } catch (Exception unused) {
                Toast.makeText(this, "Sorry, \nCannot be shared", Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }
}