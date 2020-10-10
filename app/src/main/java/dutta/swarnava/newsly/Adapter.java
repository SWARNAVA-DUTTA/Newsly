package dutta.swarnava.newsly;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import dutta.swarnava.newsly.models.Article;
import java.util.List;

import dutta.swarnava.newsly.models.Article;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>
{
    private List<Article> articles;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public Adapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holders, int position)
    {
        final MyViewHolder holder=holders;
        Article article=this.articles.get(position);
        RequestOptions requestOptions=new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.getDiskCacheStrategy();
        requestOptions.centerCrop();

        Glide.with(context)
                .load(article.getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);

        holder.title.setText(article.getTitle());
        holder.desc.setText(article.getDescription());
        holder.source.setText(article.getSource().getName());
        TextView textView = holder.time;
        StringBuilder sb = new StringBuilder();
        sb.append(" • ");
        sb.append(Utils.DateToTimeFormat(article.getPublishedAt()));
        textView.setText(sb.toString());
        holder.published_at.setText(Utils.DateFormat(article.getPublishedAt()));
        holder.author.setText(article.getAuthor());
    }

    @Override
    public int getItemCount() {
        return this.articles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView title,desc,author,published_at,source,time;
        ImageView imageView;
        ProgressBar progressBar;
        OnItemClickListener onItemClickListener;
        public MyViewHolder(@NonNull View itemView,OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            title=(TextView)itemView.findViewById(R.id.title);
            desc=(TextView)itemView.findViewById(R.id.desc);
            author=(TextView)itemView.findViewById(R.id.author);
            published_at=(TextView)itemView.findViewById(R.id.publishedAt);
            source=(TextView)itemView.findViewById(R.id.source);
            time=(TextView)itemView.findViewById(R.id.time);
            imageView=(ImageView)itemView.findViewById(R.id.img);
            progressBar=(ProgressBar)itemView.findViewById(R.id.loading_progress);
            this.onItemClickListener=onItemClickListener;
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(view,getAdapterPosition());

        }
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener
    {
        void onItemClick(View view,int position);
    }
}
