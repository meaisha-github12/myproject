package com.example.jkk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class HelpcenterAdapter extends ArrayAdapter<String> {

    private Context context;
    private int resource;

    public HelpcenterAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, null);
        }

        String content = getItem(position);

        if (content != null) {
            TextView contentTextView = view.findViewById(R.id.contentTextView);
            contentTextView.setText(content);
        }

        return view;
    }
}
