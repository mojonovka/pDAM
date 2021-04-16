package com.example.pdam.views.propiedad;

import android.content.Intent;
import android.os.Bundle;

import com.example.pdam.models.Propiedad;
import com.example.pdam.providers.PropiedadAdapter;
import com.example.pdam.providers.PropiedadProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pdam.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PropiedadesActivity extends AppCompatActivity implements PropiedadAdapter.ListItemClick{

    private RecyclerView rvPropiedades;
    private ArrayList<Propiedad> listaPropiedades;
    private PropiedadAdapter propiedadAdapter;
    private PropiedadProvider propiedadProvider;
    private LinearLayoutManager llManager;
    private String usuario_id;

    private Toolbar toolbar;
    private FloatingActionButton floatBTN;
    private static final String TAG = "DEV";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propiedades);

        Log.i(TAG, "PropiedadesActivity: init");
        Log.i(TAG, "PropiedadesActivity: onCreate");

        /*
        if(savedInstanceState != null){
            usuario_id = savedInstanceState.getString("savedUserId");
        } else {
            usuario_id = getIntent().getStringExtra("usuario_id");
        }
        */

        inicializarComponentes();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setEventListeners();
        rellenarPropiedades();
    }

    private void inicializarComponentes() {

        Log.i(TAG, "PropiedadesActivity: inicializarComponentes()");

        toolbar = findViewById(R.id.toolbar);
        floatBTN = findViewById(R.id.propiedadesFloatBtn);

        propiedadProvider = new PropiedadProvider();
        listaPropiedades = new ArrayList<>();

        rvPropiedades = findViewById(R.id.propiedadesLista);
        llManager = new LinearLayoutManager(this);
        rvPropiedades.setLayoutManager(llManager);
        propiedadAdapter = new PropiedadAdapter(listaPropiedades, PropiedadesActivity.this);
        rvPropiedades.setAdapter(propiedadAdapter);
    }

    private void setEventListeners() {

        Log.i(TAG, "PropiedadesActivity: setEventListeners()");

        floatBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                 *         .setAction("Action", null).show();
                 */
                Intent intent = new Intent(PropiedadesActivity.this, PropiedadLayout.class);
                intent.putExtra("mode", "create");
                startActivity(intent);
            }
        });
    }

    private void rellenarPropiedades() {

        Log.i(TAG, "PropiedadesActivity: rellenarPropiedades()");

        propiedadProvider.getPropiedadesDataBaseReference()
                .orderByChild("id_usuario")
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // se puede optimizar actualizando solo un unico elemento, no recarcando dota la lista
                //Log.i(TAG, "PropiedadesActivity: lista " + listaPropiedades.size());
                listaPropiedades.clear();
                //Log.i(TAG, "PropiedadesActivity: lista.clear() " + listaPropiedades.size());
                for(DataSnapshot objDataSnapshot : snapshot.getChildren()){
                    Propiedad prop = objDataSnapshot.getValue(Propiedad.class);
                    listaPropiedades.add(prop);
                }
                Log.i(TAG, "PropiedadesActivity: lista.add() " + listaPropiedades.size());
                propiedadAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onListItemClick(int clickedItem) {
        Toast.makeText(PropiedadesActivity.this, "Selected item" + clickedItem, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(PropiedadesActivity.this, PropiedadLayout.class);
        intent.putExtra("mode", "update");
        intent.putExtra("pID",listaPropiedades.get(clickedItem).getpID());
        startActivity(intent);
    }
}