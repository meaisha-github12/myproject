package com.example.jkk.ui.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jkk.LoginActivity;
import com.example.jkk.NavigationActivity;
import com.example.jkk.R;
import com.example.jkk.RegisterationActivity;
import com.example.jkk.Upload;
import com.example.jkk.UserItem;
import com.example.jkk.UserMenu;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.Current;

import java.util.Currency;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<Upload> mUploads;

    public UserAdapter(Context context, List<Upload> uploads) {
        this.context = context;
        this.mUploads = uploads;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
//        Upload uploadCurrent = mUploads.get(position);

        Upload upload = mUploads.get(position);
        holder.text.setText(upload.getFileName());
        String imageUrl = upload.getImageUrl();
        String text = upload.getFileName();
//        Log.d("Title", text);

        if (imageUrl != null) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(holder.image);
        }
        // TODO:
//      pass text variable to intent, so you can load food item by .whereEqualTo() function to load item per category
//      and use FoodItemModel as Upload class is used in home page to load all category.
        holder.card.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(context, UserMenu.class);
                mainActivity.putExtra("category",upload.getFileName()); // "EXTRA_TEXT" is a key to retrieve the value in UserMenu activity
                context.startActivity(mainActivity);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView image;
        CardView card;
        Button btn;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text_view_name);
            image = itemView.findViewById(R.id.image_view_upload);
            card = itemView.findViewById(R.id.fooditem);
            btn = itemView.findViewById(R.id.Menu);
        }
    }
}
