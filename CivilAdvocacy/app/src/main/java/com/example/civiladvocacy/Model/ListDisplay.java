package com.example.civiladvocacy.Model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.civiladvocacy.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListDisplay extends RecyclerView.Adapter<ListDisplay.ViewHolder> {

    MainActivity mainActivity;
    List<Official_Gov_Details> officialsList;

    public ListDisplay(MainActivity mainActivity, List<Official_Gov_Details> officialsList) {
        this.mainActivity = mainActivity;
        this.officialsList = officialsList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_activity_list, parent, false);
        view.setOnClickListener(mainActivity);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(officialsList.get(position).getPhotoUrl()!=null && !officialsList.get(position)
                .getPhotoUrl().isEmpty()){

            Picasso.get().load(officialsList.get(position).getPhotoUrl())
                    .error(R.drawable.brokenimage)
                    .into(holder.image);
        }



        holder.post.setText(officialsList.get(position).getPost());
        holder.name.setText(officialsList.get(position).getName()+"("+officialsList.get(position).getParty()+")");

    }

    @Override
    public int getItemCount() {
        return officialsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView post,name;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView2);
            post = itemView.findViewById(R.id.govPost);
            name = itemView.findViewById(R.id.personName);
        }
    }
}
