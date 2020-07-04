package com.example.ipark.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ipark.R;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Listmodel> implements View.OnClickListener{
    ArrayList<Listmodel>data = new ArrayList<>();
    Context context;
    int resource;
    private int lastPosition = -1;

    private static class ViewHolder{
        TextView author;
        TextView library;
    }

    public CustomAdapter(ArrayList<Listmodel>data, int resource, Context context){
        super(context, R.layout.list_items, data);
        this.data = data;
        this.context = context;
        this.resource = resource;
    }


    @Override
    public void onClick(View v) {
        int pos = (Integer)v.getTag();
        Object object = getItem(pos);
        Listmodel model = (Listmodel)object;

        if(pos==R.id.list_view_listner){
            Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Listmodel object = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if(convertView==null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.list_items,parent,false);
            viewHolder.author = (TextView)convertView.findViewById(R.id.author);
            viewHolder.library = (TextView)convertView.findViewById(R.id.library_name);
            result = convertView;

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
            result = convertView;
        }

        lastPosition = position;
        viewHolder.author.setText(object.getN());
        viewHolder.library.setText(object.getL());
        return convertView;
    }
}
