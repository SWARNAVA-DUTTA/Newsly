package dutta.swarnava.newsly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
//import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import dutta.swarnava.newsly.api.ApiClient;
import dutta.swarnava.newsly.api.ApiInterface;
import dutta.swarnava.newsly.models.Article;
import dutta.swarnava.newsly.models.News;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    SwipeRefreshLayout layout;
    TextView headline_text;
    AlertDialog.Builder builder;
    RelativeLayout error_layout;
    ImageView errorImage;
    TextView errorTitle,errorMessage;
    Button btnRetry;
    private RecyclerView.LayoutManager layoutManager;
    RecyclerView recView;
   private List<Article> articles = new ArrayList<>();
   private Adapter adapter;
   private String TAG=MainActivity.class.getSimpleName();
    public static final String API_KEY="29b32c86a4ac4a87873d6a4088647320";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        headline_text=(TextView)findViewById(R.id.headline_text);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
//        navigationView.setCheckedItem(R.id.India);
        recView=(RecyclerView)findViewById(R.id.recView);
        layoutManager=new LinearLayoutManager(MainActivity.this);
        recView.setLayoutManager(layoutManager);
        recView.setItemAnimator(new DefaultItemAnimator());
        recView.setNestedScrollingEnabled(false);
        headline_text.setText("Top Headlines");
        builder = new AlertDialog.Builder(this);
        layout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        layout.setColorSchemeResources(R.color.refreshcolor);
        layout.setOnRefreshListener(this);
        error_layout=findViewById(R.id.errorLayout);
        errorImage=(ImageView) findViewById(R.id.errorImage);
        errorTitle=(TextView)findViewById(R.id.errorTitle);
        errorMessage=(TextView)findViewById(R.id.errorMessage);
        btnRetry=(Button)findViewById(R.id.btnRetry);

// refresh complete
        layout.setRefreshing(false);
//        layout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);
        setupToolbar();
        menuItemClick();
        onLoadingSwipeRefresh("", "in");
    }



    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.about) {
            builder.setMessage("Newsly - India's own News App\n\nRead latest top headlines of any country from your favourite sources\n\nVersion: 1.0\nDeveloped by Swarnava Dutta")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                           dialog.cancel();
                        }
                    });
//                    .setNegativeButton("Change", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            //  Action for 'NO' Button
//                            dialog.cancel();
//                            edt_ph.requestFocus();
//                        }
//                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle(("About"));
            alert.setIcon(R.mipmap.ic_launcher_round);
            alert.show();
        }
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        MenuItem findItem = menu.findItem(R.id.search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search Latest News...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String str) {
                LoadJson(str,"");
                return false;
            }

            public boolean onQueryTextSubmit(String str) {
                if (str.length() > 2) {
                    LoadJson(str,"");
                } else {
                    Toast.makeText(MainActivity.this, "Type more than two letters!", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
        findItem.getIcon().setVisible(false, false);
        return true;
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Newsly");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);//Implement Hamburg Icon
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);//It takes the response of the Hamburger icon
        toggle.syncState();//It verifies whether DrawerLayout is Open or Close state

    }
    private void menuItemClick() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.share_this_app:
                            Intent intent2 = new Intent("android.intent.action.SEND");
                            intent2.setType("text/plan");
                            intent2.putExtra("android.intent.extra.SUBJECT", "Newsly");
                            StringBuilder sb = new StringBuilder();
                            sb.append("☼☼☼ *Newsly - India's own News App* ☼☼☼");
                            sb.append("\n\nRead latest top headlines of any country from your favourite sources");
                            sb.append("\n\n ▼ Download the app now : \n https://drive.google.com/uc?id=19Q9uWs-i0_8Z7DA3Evr3m5KGDwYVtWkU&export=download");
                            intent2.putExtra("android.intent.extra.TEXT", sb.toString());
                            startActivity(Intent.createChooser(intent2, "Share with :"));

                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    public void LoadJson(final String str,final String country)
    {
        error_layout.setVisibility(View.GONE);
        layout.setRefreshing(true);
        ApiInterface apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
//        String country=Utils.getCountry();
        String language=Utils.getLanguage();
        Call<News> call;
        call=apiInterface.getNews(country,API_KEY);
        if (str.length() > 0) {
            call = apiInterface.getNewsSearch(str, language,"publishedAt", API_KEY);
        } else {
            call = apiInterface.getNews(country, API_KEY);
        }
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response)
            {
                if(response.isSuccessful() && response.body().getArticle()!=null)
                {
                    if(!articles.isEmpty())
                    {
                        articles.clear();
                    }
                    articles=response.body().getArticle();
                    adapter=new Adapter(articles,MainActivity.this);
                    recView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    initListener();
                    headline_text.setVisibility(View.VISIBLE);
                    recView.setVisibility(View.VISIBLE);
                    layout.setRefreshing(false);

                }
                else
                {
                    headline_text.setVisibility(View.INVISIBLE);
                    layout.setRefreshing(false);
                    String error_code;
                    switch(response.code())
                    {
                        case 404:
                            error_code="ERROR 404 Not Found";
                            break;
                        case 500:
                            error_code="ERROR 500 Server Broken";
                            break;
                        default:
                            error_code="Unknown Error";
                            MainActivity.this.showErrorMessage(R.drawable.error,"No Result", "Please try again later\n"+error_code);
                    }
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                    headline_text.setVisibility(View.GONE);
                    layout.setRefreshing(false);
                MainActivity.this.headline_text.setVisibility(View.GONE);
                MainActivity.this.layout.setRefreshing(false);
                StringBuilder sb = new StringBuilder();
                sb.append("Network failure, Please Try Again\n");
//                sb.append(t.toString());
                MainActivity.this.showErrorMessage(R.drawable.error,"Oops..", sb.toString());
            }
        });
    }
    public void initListener() {
        this.adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            public void onItemClick(View view, int i) {
                ImageView imageView = (ImageView) view.findViewById(R.id.img);
                Intent intent = new Intent(MainActivity.this, NewsDetail.class);
                Article article = (Article) MainActivity.this.articles.get(i);
                intent.putExtra("url", article.getUrl());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("img", article.getUrlToImage());
                intent.putExtra("date", article.getPublishedAt());
                intent.putExtra("source", article.getSource().getName());
                intent.putExtra("author", article.getAuthor());
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(MainActivity.this, (View)imageView, ViewCompat.getTransitionName(imageView));

                if (Build.VERSION.SDK_INT >= 16) {
                    MainActivity.this.startActivity(intent, options.toBundle());
                } else {
                    MainActivity.this.startActivity(intent);
                }
            }
        });
    }

    public void onLoadingSwipeRefresh(final String str, final String country) {
        this.layout.post(new Runnable() {
            public void run() {
                MainActivity.this.LoadJson(str,country);
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void showErrorMessage(int i,String str, String str2) {
        if (this.error_layout.getVisibility() ==View.GONE) {
            this.error_layout.setVisibility(View.VISIBLE);
            recView.setVisibility(View.GONE);
            headline_text.setVisibility(View.GONE);

        }
        this.errorImage.setImageResource(i);
        this.errorTitle.setText(str);
        this.errorMessage.setText(str2);
        this.btnRetry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.onLoadingSwipeRefresh("","in");
            }
        });
    }

    @Override
    public void onRefresh() {
        LoadJson("","in");
    }
}