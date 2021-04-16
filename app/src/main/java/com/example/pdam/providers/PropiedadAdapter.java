package com.example.pdam.providers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pdam.R;
import com.example.pdam.models.Propiedad;

import java.util.ArrayList;
import java.util.List;

public class PropiedadAdapter extends RecyclerView.Adapter<PropiedadAdapter.ViewHolder> {

    ArrayList<Propiedad> listaPropiedades;
    final private ListItemClick mOnClickListener;

    public interface ListItemClick{
        void onListItemClick(int clickedItem);
    }

    public PropiedadAdapter(ArrayList<Propiedad> listaPropiedades, ListItemClick listener){
        this.listaPropiedades = listaPropiedades;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public PropiedadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_prop_row, parent, false);
        return new PropiedadAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropiedadAdapter.ViewHolder holder, int position) {
        holder.tvPropNombreDescriptivo.setText(listaPropiedades.get(position).getnombreDescriptivo());
        holder.tvPropMunicipio.setText(listaPropiedades.get(position).getMunicipio());
        holder.tvPropPeriodo.setText(listaPropiedades.get(position).getPeriodo());
        holder.tvPropPrecio.setText(listaPropiedades.get(position).getPrecio());
    }

    @Override
    public int getItemCount() {
        try {
            return listaPropiedades.size();
        } catch (Exception ex){
            return 0;
        }

    }

    /**
     * inner class
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvPropNombreDescriptivo, tvPropMunicipio, tvPropPeriodo, tvPropPrecio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPropNombreDescriptivo = itemView.findViewById(R.id.propRow_nombre_descriptivo);
            tvPropMunicipio = itemView.findViewById(R.id.propRow_municipio);
            tvPropPeriodo = itemView.findViewById(R.id.propRow_periodo);
            tvPropPrecio = itemView.findViewById(R.id.propRow_precio);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int clickedItem = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedItem);
        }
    }
}
