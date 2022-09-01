package com.example.wanderoute;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// Hier wird die RecyclerView beschrieben, anhand dessen die Routen in der View dargestellt werden.
public class RouteRecyclerAdapter extends RecyclerView.Adapter<RouteRecyclerAdapter.RouteViewHolder> {

    private List<Route> routes = new ArrayList<>();
    private OnRouteClickListener listener;

    // Beschreibt eine einzige Route und stellt den ClickListener Zurverfügung.
    public class RouteViewHolder extends RecyclerView.ViewHolder {

        private TextView bez;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            bez = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onRouteClick(routes.get(position));
                    }
                }
            });
        }
    }

    // Beim anklicken dieses Listeners, öffnet sich die Route zum bearbeiten.
    public interface OnRouteClickListener {
        void onRouteClick(Route route);
    }
    public void setOnRouteClickListener(OnRouteClickListener listener) {
        this.listener = listener;
    }

    // Der RecyclerView wird hier Inflated.
    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new RouteViewHolder(itemView);
    }

    // Der Name der Route wird im Holder dargestellt.
    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        Route currentRoute = routes.get(position);
        holder.bez.setText(currentRoute.getBez());
    }

    // Größe der Liste der Route-Objekte wird berechnet
    @Override
    public int getItemCount() {
        return routes.size();
    }

    // Liste der Route-Objekte wird gesetzt, falls sich etwas ändert.
    public void setRoutes(List<Route> routes) {
        this.routes = routes;
        notifyDataSetChanged();
    }
}
