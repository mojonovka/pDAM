package com.example.pdam.providers;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pdam.R;
import com.example.pdam.models.Inmueble;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PropiedadAdapter extends RecyclerView.Adapter<PropiedadAdapter.ViewHolder> {

    ArrayList<Inmueble> listaPropiedades;
    final private ListItemClick mOnClickListener;

    public interface ListItemClick {
        void onListItemClick(int clickedItem);
    }

    public PropiedadAdapter(ArrayList<Inmueble> listaPropiedades, ListItemClick listener) {
        this.listaPropiedades = listaPropiedades;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public PropiedadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_prop_row_card, parent, false);
        return new PropiedadAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropiedadAdapter.ViewHolder holder, int position) {
        holder.tvPropNombreDescriptivo.setText(listaPropiedades.get(position).getInmbNombreDesc());
        holder.tvPropMunicipio.setText(listaPropiedades.get(position).getInmbMunicipio());
        holder.tvPropPeriodo.setText(" ");
        holder.tvPropPrecio.setText(listaPropiedades.get(position).getInmbPeriodo() + " / " + listaPropiedades.get(position).getInmbPrecio() + "€");
        Picasso.get().load(listaPropiedades.get(position).getInmbFotoURI()).into(holder.ivPropFoto);

        if (listaPropiedades.get(position).getInmbDisp()) {
            holder.fabDisponible.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
        } else {
            holder.fabDisponible.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        }

        holder.fabDisponible.setEnabled(false);

    }

    @Override
    public int getItemCount() {
        return listaPropiedades.size();
    }

    /**
     * inner class
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvPropNombreDescriptivo, tvPropMunicipio, tvPropPeriodo, tvPropPrecio;
        private ImageView ivPropFoto;
        private FloatingActionButton fabDisponible;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPropNombreDescriptivo = itemView.findViewById(R.id.propRow_nombre_descriptivo);
            tvPropMunicipio = itemView.findViewById(R.id.propRow_municipio);
            tvPropPeriodo = itemView.findViewById(R.id.propRow_periodo);
            tvPropPrecio = itemView.findViewById(R.id.propRow_precio);
            ivPropFoto = itemView.findViewById(R.id.propRow_img);
            fabDisponible = itemView.findViewById(R.id.propRow_FAB);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int clickedItem = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedItem);
        }
    }
}
