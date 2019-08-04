package com.example.moviemania;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {


    ArrayList<Films> films;
    Context context;
    public RecyclerAdapter(ArrayList<Films> films) {
        this.films=films;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.movie_item,null);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Films film=films.get(position);
        holder.title.setText(film.getTitle());
        Picasso.with(context).load(film.getPoster()).into(holder.poster);
        holder.rating.setText(film.getRating());
        holder.releaseDate.setText(film.getReleaseDate());
        holder.genres.setText(film.getGenres());
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(film.getInfoType().equals("movie")){
                Intent intent=new Intent(context,DetailsActivity.class);
                intent.putExtra("ID",film.getID());
                context.startActivity(intent);}
                if(film.getInfoType().equals("tv")){
                    Intent intent=new Intent(context,TVDetailsActivity.class);
                    intent.putExtra("ID",film.getID());
                    context.startActivity(intent);
                }
                if(film.getInfoType().equals("people")){
                    Intent i=new Intent(context,PeopleDetailsActivity.class);
                    i.putExtra("ID",film.getID());
                    context.startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return films.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView poster;
        TextView title,rating,releaseDate,genres;
        CardView cv;
        public ViewHolder(View itemView){
            super(itemView);
            title=itemView.findViewById(R.id.Title);
            poster=itemView.findViewById(R.id.Poster);
            rating=itemView.findViewById(R.id.Rating);
            releaseDate=itemView.findViewById(R.id.ReleaseDate);
            genres=itemView.findViewById(R.id.Genres);
            cv=itemView.findViewById(R.id.cv1);
        }

    }

}
