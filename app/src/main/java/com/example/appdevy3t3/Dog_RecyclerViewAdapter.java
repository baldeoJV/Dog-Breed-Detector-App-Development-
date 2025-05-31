package com.example.appdevy3t3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Dog_RecyclerViewAdapter extends RecyclerView.Adapter<Dog_RecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<DogModel> dogModels;

    public Dog_RecyclerViewAdapter(Context context, ArrayList<DogModel> dogModels) {
        this.context = context;
        this.dogModels = dogModels;
    }

    @NonNull
    @Override
    public Dog_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_dog_model, parent, false);

        return new Dog_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Dog_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.imageView.setImageResource(dogModels.get(position).getImage());
        holder.textViewName.setText(dogModels.get(position).getDogName());
    }

    @Override
    public int getItemCount() {
        return dogModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.dog_image);
            textViewName = itemView.findViewById(R.id.dog_name);
        }
    }
}
