package com.example.pdam.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pdam.R;
import com.example.pdam.models.Inmueble;
import com.example.pdam.models.User;
import com.example.pdam.providers.AuthProvider;
import com.example.pdam.providers.PropiedadAdapterPPA;
import com.example.pdam.providers.PropiedadProvider;
import com.example.pdam.providers.UserProvider;
import com.example.pdam.views.identificacion.AuthMainActivity;
import com.example.pdam.views.propiedad.PropInfo;
import com.example.pdam.views.propiedad.PropSeachAttr;
import com.example.pdam.views.propiedad.PropiedadesActivity;
import com.example.pdam.views.usuario.UserLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PPAActivity extends AppCompatActivity implements PropiedadAdapterPPA.ListItemClick{

    private TextView tvUserName;
    private Button btnOpcion;

    private FloatingActionButton floatBTN;
    private User usuario;
    private AuthProvider mAuthProvider;
    private UserProvider mUserProvider;
    private FirebaseUser fbUser;

    private ArrayList<Inmueble> listaPropiedades;
    //private PropiedadAdapter propiedadAdapter;
    private PropiedadAdapterPPA propiedadAdapter;
    private RecyclerView rvPropiedades;
    private PropiedadProvider propiedadProvider;
    private LinearLayoutManager llManager;

    private SharedPreferences sharedPreferences;

    private static final String TAG = "DEV";

    /**
     * parametros de busqueda
     *
     */

    private String saProvincia;
    private String saMunicipio;
    private String saPeriodo;
    private final int REQUEST_CODE_SEACH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppa);

        Log.i(TAG, "-----------------------------------------------------------------");
        Log.i(TAG, "PPAActivity: onCreate()");

        Log.i(TAG, "PPAActivity: savedInstanceState=" + savedInstanceState);

        if(savedInstanceState != null){
            Log.i(TAG, "PPAActivity: !=null ");
            saProvincia = savedInstanceState.getString("saProvincia");
            saMunicipio = savedInstanceState.getString("saMunicipio");
            saPeriodo = savedInstanceState.getString("saPeriodo");
        } else {
            Log.i(TAG, "PPAActivity: null ");
            saProvincia = "";
            saMunicipio = "";
            saPeriodo = "";
        }



        inicializarComponentes(savedInstanceState);
        setEventListeners();
        rellenarPropiedades();

    }

    private void inicializarComponentes(Bundle savedInstanceState) {

        floatBTN = findViewById(R.id.fbtn_prop_seach_save);
        tvUserName = findViewById(R.id.tvUserName);

        btnOpcion = findViewById(R.id.btnOpcion);
        btnOpcion.setText("IDENTIFICARSE");

        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();

        propiedadProvider = new PropiedadProvider();
        listaPropiedades = new ArrayList<>();

        rvPropiedades = findViewById(R.id.propiedadesLista);
        llManager = new LinearLayoutManager(this);
        rvPropiedades.setLayoutManager(llManager);
        propiedadAdapter = new PropiedadAdapterPPA(listaPropiedades, PPAActivity.this);
        rvPropiedades.setAdapter(propiedadAdapter);

        sharedPreferences = getSharedPreferences("attrSeach", Context.MODE_PRIVATE);

        saProvincia = sharedPreferences.getString("saProvincia", "");
        saMunicipio = sharedPreferences.getString("saMunicipio", "");
        saPeriodo = sharedPreferences.getString("saPeriodo","");

    }

    private void setEventListeners() {

        btnOpcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i(TAG, "PPAActivity: inicio de identifiación");
                Intent intent = new Intent(PPAActivity.this, AuthMainActivity.class);
                //Log.i(TAG, "PPAActivity: rederect a AuthMainActivity");
                startActivity(intent);
            }
        });

        /**
         * se abre pantalla de los ajustes de busqueda
         */
        floatBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PPAActivity.this, PropSeachAttr.class);
                intent.putExtra("saProvincia", saProvincia);
                intent.putExtra("saMunicipio", saMunicipio);
                intent.putExtra("saPeriodo", saPeriodo);
                startActivityForResult(intent, REQUEST_CODE_SEACH);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        fbUser = mAuthProvider.getUsuario();

        if (fbUser != null) {
            getDatosDelUsuario(fbUser.getUid());
            btnOpcion.setVisibility(View.INVISIBLE);
        } else {
            tvUserName.setText("bienvenido");
            btnOpcion.setVisibility(View.VISIBLE);
        }
    }

    public void getDatosDelUsuario(String uID) {

        mUserProvider.getUsuarioById(uID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.i(TAG, "PPAActivity: obtención de datos exitosa:");

                try {
                    usuario = new User(snapshot.getValue(User.class));
                } catch (Exception e){
                    e.printStackTrace();
                    Log.i(TAG, "PPAActivity: bad user data");
                    mAuthProvider.cerarSecion();
                } finally {

                }
                //fallo manejar mejor la auth del usuario
                Log.i(TAG, "PPAActivity: usuario: " + usuario.getuName());

                tvUserName.setText(usuario.getuName());
                Log.i(TAG, "PPAActivity: onStart: usuario: " + usuario.getuName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i(TAG, "PPAActivity: obtención de datos ha fallado:");
                tvUserName.setText("algo va mal");
                mAuthProvider.cerarSecion();
                usuario = null;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(fbUser != null){
            getMenuInflater().inflate(R.menu.menu_items, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.menu_verPerfil:
                intent = new Intent(PPAActivity.this, UserLayout.class);
                startActivity(intent);
                return true;

            case R.id.menu_propiedades:
                intent = new Intent(PPAActivity.this, PropiedadesActivity.class);
                intent.putExtra("usuario_id", fbUser.getUid());
                startActivity(intent);
                return true;

            case R.id.menu_cerrarSesion:
                mAuthProvider.cerarSecion();
                intent = new Intent(PPAActivity.this, PPAActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_about:
                intent = new Intent(PPAActivity.this, PPAAbout.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void rellenarPropiedades() {

        Log.i(TAG, "PropiedadesActivity: rellenarPropiedades()");

        propiedadProvider.getPropiedadesDataBaseReference()
                .orderByChild("inmbTimeStamp").limitToLast(25)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // se puede optimizar actualizando solo un unico elemento, no recarcando dota la lista
                        //Log.i(TAG, "PropiedadesActivity: lista " + listaPropiedades.size());
                        listaPropiedades.clear();
                        //Log.i(TAG, "PropiedadesActivity: lista.clear() " + listaPropiedades.size());
                        for(DataSnapshot objDataSnapshot : snapshot.getChildren()){
                            Inmueble prop = objDataSnapshot.getValue(Inmueble.class);

                            if(isGoodForSeach(prop)){
                                listaPropiedades.add(prop);
                            }

                        }

                        Log.i(TAG, "PropiedadesActivity: lista.add() " + listaPropiedades.size());
                        propiedadAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        /**
         * .orderByChild("inmbID")
         * .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
         */
    }

    private boolean isGoodForSeach(Inmueble prop) {

        boolean isGFS = true;
        //Log.i(TAG, "PropiedadesActivity: isGoodForSeach " + prop.toString());
        //Log.i(TAG, "PropiedadesActivity: saProvincia " + saProvincia);

        if (!prop.getInmbDisp()){
            isGFS = false;
        }

        if(!saProvincia.isEmpty()){
            //Log.i(TAG, "PropiedadesActivity: saProvincia != \"\" ");
            if (!prop.getInmbProvincia().equalsIgnoreCase(saProvincia)){
                //Log.i(TAG, "PropiedadesActivity: !prop.getProvincia().equalsIgnoreCase(saProvincia)");
                isGFS = false;
            }
        }

        if(!saMunicipio.isEmpty()){
            if (!prop.getInmbMunicipio().equalsIgnoreCase(saMunicipio)){
                isGFS = false;
            }
        }

        if(!saPeriodo.isEmpty()){
            if (!prop.getInmbPeriodo().equalsIgnoreCase(saPeriodo)){
                isGFS = false;
            }
        }

        Log.i(TAG, "PropiedadesActivity: isGoodForSeach " + isGFS);

        return isGFS;
    }

    /**
     * evento sobre inmueble clickado de la lista
     * @param clickedItem
     */
    @Override
    public void onListItemClick(int clickedItem) {

        if(fbUser != null){
            Intent intent = new Intent(PPAActivity.this, PropInfo.class);
            intent.putExtra("usuarioID", fbUser.getUid());
            intent.putExtra("inmbID", listaPropiedades.get(clickedItem).getInmbID());
            startActivity(intent);
        } else {
            Snackbar.make(rvPropiedades, "nesesitas identificarse", Snackbar.LENGTH_LONG)
                    .setAction("action", null).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_CODE_SEACH){
                saProvincia = data.getStringExtra("saProvincia");
                saMunicipio = data.getStringExtra("saMunicipio");
                saPeriodo = data.getStringExtra("saPeriodo");
                Log.i(TAG, "PPAActivity: saProvincia = " + saProvincia);
                Log.i(TAG, "PPAActivity: saMunicipio = " + saMunicipio);
                Log.i(TAG, "PPAActivity: saPeriodo = " + saPeriodo);

                rellenarPropiedades();

            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "PropiedadesActivity: onSaveInstanceState() " + "(" + saProvincia + ") (" + saMunicipio + ") (" + saPeriodo + ")");
        outState.putString("saProvincia", saProvincia);
        outState.putString("saMunicipio", saMunicipio);
        outState.putString("saPeriodo", saPeriodo);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "PropiedadesActivity: onSaveInstanceState()");
        saProvincia = savedInstanceState.getString("saProvincia");
        saMunicipio = savedInstanceState.getString("saMunicipio");
        saPeriodo = savedInstanceState.getString("saPeriodo");
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("saProvincia", saProvincia);
        editor.putString("saMunicipio", saMunicipio);
        editor.putString("saPeriodo", saPeriodo);
        editor.commit();
    }
}