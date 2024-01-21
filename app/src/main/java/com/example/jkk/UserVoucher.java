package com.example.jkk;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jkk.R;
import com.example.jkk.Upload;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.Current;

import java.util.Currency;
import java.util.List;

public class UserVoucher extends RecyclerView.Adapter<com.example.jkk.UserVoucher.UserViewHolder> {

    private Context context;
    private List<Upload> mUploads;

    public UserVoucher(Context context, List<Upload> uploads) {
        this.context = context;
        this.mUploads = uploads;
    }


    @NonNull
    @Override
    public com.example.jkk.UserVoucher.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_user_item, parent, false);
        return new com.example.jkk.UserVoucher.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.jkk.UserVoucher.UserViewHolder holder, int position) {
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
//            Picasso.get()
//        .load(imageUrl)
//        .noPlaceholder()
//        .fit()
//        .centerCrop()
//        .into(holder.image);
        }

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView image;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text_view_name);
            image = itemView.findViewById(R.id.image_view_upload);
        }
    }
}
