package com.example.appdevy3t3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import java.util.List;

public class DogAdapter extends RecyclerView.Adapter<DogAdapter.DogViewHolder> {

    Context context;
    List<DogModel> dogList;

    public DogAdapter(Context context, List<DogModel> dogList) {
        this.context = context;
        this.dogList = dogList;
    }

    @NonNull
    @Override
    public DogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_dog_model, parent, false);
        return new DogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogViewHolder holder, int position) {
        DogModel dog = dogList.get(position);
        holder.tv_dogName.setText(dog.getDog_name());
//        Picasso.get().load(dog.getImageUrl()).into(holder.img_dog);
        Glide.with(context).load(dog.getImageUrl()).into(holder.img_dog);

    }

    @Override
    public int getItemCount() {
        return dogList.size();
    }

    public static class DogViewHolder extends RecyclerView.ViewHolder {
        TextView tv_dogName;
        ImageView img_dog;

        public DogViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_dogName = itemView.findViewById(R.id.dog_name);
            img_dog = itemView.findViewById(R.id.dog_image);
        }
    }
}
