package com.dianaramirez.rima;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.incidentViewHolder>{
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the incidents in a list
    private List<Incident> incidentList;

    //getting the context and incident list with constructor
    public IncidentAdapter(Context mCtx, List<Incident> incidentList) {
        this.mCtx = mCtx;
        this.incidentList = incidentList;
    }

    @Override
    public incidentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cards_layout, null);
        return new incidentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(incidentViewHolder holder, int position) {
        //getting the incident of the specified position
        Incident incident = incidentList.get(position);
        String tipoString = "";
        switch (String.valueOf(incident.getType())) {
            case ("1"):
                tipoString = "Agua";
                break;
            case ("2"):
                tipoString = "Alcantarillado";
                break;
            case ("3"):
                tipoString = "Electricidad";
                break;
            case ("4"):
                tipoString = "Telefonía";
                break;
            case ("5"):
                tipoString = "Vías";
                break;
            case ("6"):
                tipoString = "Ríos";
                break;
            case ("7"):
                tipoString = "Laderas";
                break;
        }

        //binding the data with the viewholder views
        holder.textViewTitle.setText(incident.getTitle()+" ("+tipoString+")");
        holder.idText.setText(""+incident.getId());
        holder.textViewDescription.setText(incident.getDescription());
        holder.textViewDistance.setText(String.valueOf(incident.getDistance()));
        holder.textViewDate.setText(String.valueOf(incident.getDate()));
        holder.type_incident.setText(String.valueOf(incident.getType()));
        holder.textEditable.setText(String.valueOf(incident.getEditable()));

        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(incident.getImage()));

    }


    @Override
    public int getItemCount() {
        return incidentList.size();
    }


    class incidentViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textViewTitle, textViewDescription, textViewDistance, textViewDate, idText, type_incident, textEditable;
        ImageView imageView;

        public incidentViewHolder(View itemView) {
            super(itemView);
            cardView= itemView.findViewById(R.id.card);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            idText = itemView.findViewById(R.id.id);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewDistance = itemView.findViewById(R.id.textViewDistance);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            imageView = itemView.findViewById(R.id.imageView);
            type_incident = itemView.findViewById(R.id.type_incident);
            textEditable = itemView.findViewById(R.id.editable);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(textEditable.getText().toString().equals("true")) {
                        String title = textViewTitle.getText().toString().substring(0,textViewTitle.getText().toString().indexOf("(")-1);
                        Intent intent = new Intent(mCtx, EditIncident.class);
                        intent.putExtra("title", title);
                        intent.putExtra("description", textViewDescription.getText().toString());
                        intent.putExtra("id", idText.getText().toString());
                        intent.putExtra("type_incident", type_incident.getText().toString());
                        mCtx.startActivity(intent);
                    }
                }
            });
        }
    }

}
