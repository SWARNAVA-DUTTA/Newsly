package dutta.swarnava.newsly.api;

import dutta.swarnava.newsly.models.News;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface
{
    @GET("top-headlines")
    Call<News> getNews
            (
                    @Query("country") String country,
                    @Query("apiKey") String apiKey
            );
    @GET("everything")
    Call<News> getNewsSearch
            (
                    @Query("q") String str,
                    @Query("language") String language,
                    @Query("sortBy") String str2,
                    @Query("apiKey") String str3
            );
}
