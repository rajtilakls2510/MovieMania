package com.example.moviemania.Adapters;

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

import com.example.moviemania.Activities.CompanyActivity;
import com.example.moviemania.Activities.DetailsActivity;
import com.example.moviemania.Activities.EpisodeDetailsActivity;
import com.example.moviemania.Activities.PeopleDetailsActivity;
import com.example.moviemania.Activities.SeasonDetailsActivity;
import com.example.moviemania.Activities.TVDetailsActivity;
import com.example.moviemania.R;
import com.example.moviemania.Models.SmallCard;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter1 extends RecyclerView.Adapter<RecyclerAdapter1.ViewHolder> {

    ArrayList<SmallCard> films;
    Context context;
    public RecyclerAdapter1(ArrayList<SmallCard> films){
        this.films=films;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.card_item1,null);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SmallCard film=films.get(position);
        holder.title.setText(film.getTitle());
        Picasso.with(context).load(film.getPoster()).into(holder.poster);
        holder.subtitle.setText(film.getSubtitle());
        holder.cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(film.getInfoType().equals("cast")){
                Intent i=new Intent(context, PeopleDetailsActivity.class);
                i.putExtra("ID",film.getID());
                context.startActivity(i);}
                if(film.getInfoType().equals("company")) {
                    Toast.makeText(context, "Clicked: "+film.getID(), Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(context, CompanyActivity.class);
                    i.putExtra("ID",film.getID());
                    context.startActivity(i);
                }
                if(film.getInfoType().equals("movie")){
                    Toast.makeText(context, "ID: "+film.getID(), Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(context, DetailsActivity.class);
                    i.putExtra("ID",film.getID());
                    context.startActivity(i);
                }
                if(film.getInfoType().equals("tvSeason")) {
                    Intent i=new Intent(context, SeasonDetailsActivity.class);
                    i.putExtra("TV_ID",film.getID());
                    i.putExtra("Season",film.getSeason());
                    context.startActivity(i);
                }
                if(film.getInfoType().equals("tvEpisode")) {
                    Intent i=new Intent(context, EpisodeDetailsActivity.class);
                    i.putExtra("TV_ID",film.getID());
                    i.putExtra("Season",film.getSeason());
                    i.putExtra("Episode",film.getEpisode());
                    context.startActivity(i);
                }
                if(film.getInfoType().equals("tv")) {
                    Intent i=new Intent(context, TVDetailsActivity.class);
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

    public class ViewHolder extends  RecyclerView.ViewHolder{

        ImageView poster;
        TextView title,subtitle;
        CardView cv2;
        public ViewHolder(View itemView){
            super(itemView);
            poster=itemView.findViewById(R.id.cardPoster);
            title=itemView.findViewById(R.id.cardTitle);
            subtitle=itemView.findViewById(R.id.cardSubtitle);
            cv2=itemView.findViewById(R.id.cv2);

        }
    }
}
