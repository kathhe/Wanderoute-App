package com.example.wanderoute;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// Hier wird die RecyclerView beschriebem, anhand dessen die Poi in der View dargestellt werden.
public class PoiRecyclerAdapter extends RecyclerView.Adapter<PoiRecyclerAdapter.PoiViewHolder> {

    private List<Poi> pois = new ArrayList<>();
    private PoiRecyclerAdapter.OnPoiClickListener listener;


    // Beschreibt eine einzige Poi und stellt den ClickListener Zurverfügung.
    public class PoiViewHolder extends RecyclerView.ViewHolder {
        private TextView ort;

        public PoiViewHolder(@NonNull View itemView) {
            super(itemView);
            ort = itemView.findViewById(R.id.poiOrt);


            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onPoiClick(pois.get(position));
                    }
                }
            });
        }
    }

    // Beim anklicken dieses Listeners, öffnet sich die Poi zum bearbeiten.
    public interface OnPoiClickListener {
        void onPoiClick(Poi poi);
    }
    public void setOnPoiClickListener(PoiRecyclerAdapter.OnPoiClickListener listener) {
        this.listener = listener;
    }

    // Der RecyclerView wird hier Inflated.
    @NonNull
    @Override
    public PoiRecyclerAdapter.PoiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_poi_item, parent, false);
        return new PoiRecyclerAdapter.PoiViewHolder(itemView);
    }

    // Der Name der Poi wird im Holder dargestellt.
    @Override
    public void onBindViewHolder(@NonNull PoiRecyclerAdapter.PoiViewHolder holder, int position) {
        Poi currentPoi = pois.get(position);
        holder.ort.setText(currentPoi.getOrt());
    }

    // Größe der Liste der Poi-Objekte wird berechnet
    @Override
    public int getItemCount() {
        return pois.size();
    }

    // Liste der Poi-Objekte wird gesetzt, falls sich etwas ändert.
    public void setPois(List<Poi> pois) {
        this.pois = pois;
        notifyDataSetChanged();
    }
}
