package com.example.ipark.Models;


import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ipark.Location.PlacesPOJO;
import com.example.ipark.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<Vehicle>history;
    private List<PlacesPOJO.CustomA> stLstStores;
    private List<StoreModel> models;

    public static int a = 0;

    public RecyclerViewAdapter(List<Vehicle>history_list){
        history = history_list;
        a+=1;
        if(a>1)
            a=1;
    }

    public RecyclerViewAdapter(List<PlacesPOJO.CustomA> stores, List<StoreModel> storeModels) {
        stLstStores = stores;
        models = storeModels;
        a-=1;
        if(a<0)
            a=0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(a==0) {
                final View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.store_list_row, parent, false);
                return new MyViewHolder(view);
            }

            else{
                final View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.historyholder,parent,false);
                return new historyHolder(view);
            }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(a==0){
            ((MyViewHolder)holder).setData(stLstStores.get(holder.getAdapterPosition()), (MyViewHolder)holder, models.get(holder.getAdapterPosition()));
        }

        else if(a==1){
            ((historyHolder)holder).setHistory((historyHolder)holder,history.get(holder.getAdapterPosition()));
        }
    }


    @Override
    public int getItemCount() {
        if(a==0)
            return Math.min(7, stLstStores.size());
        else
            return history.size();
    }


    public class historyHolder extends RecyclerView.ViewHolder{
        TextView Owner_name,Owner_number;
        ImageView Car_bike;

        public historyHolder(final View itemView){
            super(itemView);
            this.Owner_name = itemView.findViewById(R.id.owner_name);
            this.Owner_number = itemView.findViewById(R.id.owner_number);
            this.Car_bike = itemView.findViewById(R.id.bike_car);
        }

        public void setHistory(historyHolder holder, Vehicle object){
            holder.Owner_name.setText(object.getE());
            holder.Owner_number.setText(object.getA());

            String t = object.getF();

            if(t!=null && t.contains("CAR"))
                holder.Car_bike.setImageResource(R.drawable.car);
            else
                holder.Car_bike.setImageResource(R.drawable.bike);
        }
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtStoreName;
        TextView txtStoreAddr;
        //TextView txtStoreDist;
        StoreModel model;
        CardView cardView;


        public MyViewHolder(final View itemView) {
            super(itemView);

            //this.txtStoreDist = (TextView) itemView.findViewById(R.id.txtStoreDist);
            this.txtStoreName = (TextView) itemView.findViewById(R.id.txtStoreName);
            this.txtStoreAddr = (TextView) itemView.findViewById(R.id.txtStoreAddr);
            this.cardView = (CardView)itemView.findViewById(R.id.card_vieww);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int x = getAdapterPosition();

                    String lat = models.get(x).lat;
                    String lon = models.get(x).lon;

                    //Toast.makeText(view.getContext(), lat, Toast.LENGTH_SHORT).show();
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("geo:"+lon+","+lat+"?q="+lat+","+lon));
                        intent.setComponent(new ComponentName(
                                "com.google.android.apps.maps",
                                "com.google.android.maps.MapsActivity"));
                        itemView.getContext().startActivity(intent);
                    } catch (ActivityNotFoundException e) {

                        try {
                            itemView.getContext().startActivity(new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=com.google.android.apps.maps")));
                        } catch (ActivityNotFoundException anfe) {
                            itemView.getContext().startActivity(new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.apps.maps")));
                        }
                        e.printStackTrace();
                    }
                }
            });
        }


        public void setData(PlacesPOJO.CustomA  info, MyViewHolder holder, StoreModel storeModel) {


            this.model = storeModel;

            //holder.txtStoreDist.setText(model.distance + "\n" + model.duration);
            holder.txtStoreName.setText(info.name);
            holder.txtStoreAddr.setText(info.vicinity);

        }
    }
}


