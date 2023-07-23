package com.example.save_food.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.save_food.R;

import java.util.ArrayList;

public class RecyclerApdapter extends RecyclerView.Adapter<RecyclerApdapter.ViewHolder> {

    private final itemClickListener itemClickListener;
    private final ArrayList<Uri> uriArrayList;
    private final Context context;
    CountOfImagesWhenRemoved countOfImagesWhenRemoved;
    public RecyclerApdapter(ArrayList<Uri> uriArrayList, Context context, CountOfImagesWhenRemoved countOfImagesWhenRemoved, itemClickListener itemClickListener) {
        this.uriArrayList = uriArrayList;
        this.context = context;
        this.countOfImagesWhenRemoved = countOfImagesWhenRemoved;
        this.itemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public RecyclerApdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater =  LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_single_image, parent, false);

        return new ViewHolder(view, countOfImagesWhenRemoved, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerApdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
     //   holder.imageView.setImageURI(uriArrayList.get(position));
        Glide.with(context).load(uriArrayList.get(position)).into(holder.imageView);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uriArrayList.remove(uriArrayList.get(position));
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
                countOfImagesWhenRemoved.clicked(uriArrayList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    public interface CountOfImagesWhenRemoved {
        void clicked(int getSize);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView, delete;
        itemClickListener itemClickListener;

        CountOfImagesWhenRemoved countOfImagesWhenRemoved;
        public ViewHolder(@NonNull View itemView, CountOfImagesWhenRemoved countOfImagesWhenRemoved, itemClickListener itemClickListener) {
            super(itemView);
            this.countOfImagesWhenRemoved = countOfImagesWhenRemoved;
            imageView = itemView.findViewById(R.id.image);
            delete = itemView.findViewById(R.id.delete);
            this.itemClickListener = itemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                if(itemClickListener != null){
                    itemClickListener.itemClick(getAdapterPosition());
                }
        }
    }

    public interface itemClickListener{
        void itemClick(int position);
    }

}

