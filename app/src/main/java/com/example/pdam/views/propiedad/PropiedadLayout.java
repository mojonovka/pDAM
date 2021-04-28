package com.example.pdam.views.propiedad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pdam.R;
import com.example.pdam.models.Propiedad;
import com.example.pdam.models.User;
import com.example.pdam.providers.AuthProvider;
import com.example.pdam.providers.PropiedadProvider;
import com.example.pdam.providers.UserProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class PropiedadLayout extends AppCompatActivity {

    private Propiedad inmbl;
    private AuthProvider mAuthProvider;
    private PropiedadProvider mPropiedadProvider;
    private FirebaseUser fbUser;

    private EditText etPropNombreDescriptivo;
    private EditText etPropTipo;
    private EditText etPropPrecio;
    private EditText etPropPeriodo;
    private EditText etPropProvincia;
    private EditText etPropMunicipio;
    private EditText etPropCP;
    private EditText etPropDireccion;

    private String inmblID;
    private String propID;

    private static final String TAG = "DEV";

    private enum MODE{
        CREATE, UPDATE, ALQUILER, UNDEFINED
    }

    MODE mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propiedad_layout);
        Log.i(TAG, "PropiedadLayout: init");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initComponents();

        switch (getIntent().getStringExtra("mode")){
            case "CREATE":
                mode = MODE.CREATE;
                getSupportActionBar().setTitle("Propiedad nueva");
                break;
            case "UPDATE":
                mode = MODE.UPDATE;
                getSupportActionBar().setTitle("Propiedad modificación");
                inmblID = getIntent().getStringExtra("inmbID");
                rellenarPropiedad(inmblID);
                break;

            case "ALQUILER":
                mode = MODE.ALQUILER;
                getSupportActionBar().setTitle("Propiedad, datos detallados");
                inmblID = getIntent().getStringExtra("inmbID");
                rellenarPropiedad(inmblID);
                deshabilitarComponentes();
                break;

            default:
                mode = MODE.UNDEFINED;
        }

    }

    private void deshabilitarComponentes() {
        etPropNombreDescriptivo.setEnabled(false);
        etPropTipo.setEnabled(false);
        etPropPrecio.setEnabled(false);
        etPropPeriodo.setEnabled(false);
        etPropProvincia.setEnabled(false);
        etPropMunicipio.setEnabled(false);
        etPropCP.setEnabled(false);
        etPropDireccion.setEnabled(false);
    }

    private void rellenarPropiedad(String inmbID) {
        mPropiedadProvider.getPropiedadById(inmbID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                inmbl = new Propiedad(snapshot.getValue(Propiedad.class));
                etPropNombreDescriptivo.setText(inmbl.getnombreDescriptivo());
                etPropTipo.setText(inmbl.getTipo());
                etPropPrecio.setText(inmbl.getPrecio());
                etPropPeriodo.setText(inmbl.getPeriodo());
                etPropProvincia.setText(inmbl.getProvincia());
                etPropMunicipio.setText(inmbl.getMunicipio());
                etPropCP.setText(inmbl.getCp());
                etPropDireccion.setText(inmbl.getDireccion());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fbUser = mAuthProvider.getUsuario();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (mode){
            case CREATE:
                getMenuInflater().inflate(R.menu.menu_prop_create, menu);
                break;
            case UPDATE:
                getMenuInflater().inflate(R.menu.menu_prop_update, menu);
                break;
            case ALQUILER:
                getMenuInflater().inflate(R.menu.menu_prop_alquiler, menu);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.menu_prop_crear:
                crearPropiedad();
                return true;

            case R.id.menu_prop_update:
                actualizarPropiedad();
                return true;

            case R.id.menu_prop_delete:
                eliminarPropiedad();
                return true;

            case R.id.menu_prop_alqiler:
                alqularPropiedad();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void alqularPropiedad() {

        String propietarioID = inmbl.getId_usuario();

        UserProvider userProvider = new UserProvider();

        DatabaseReference mdaDatabaseReference = userProvider.getUsuarioById(propietarioID);

        mdaDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = new User(snapshot.getValue(User.class));
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + user.getuEmail()));
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void eliminarPropiedad() {
        mPropiedadProvider.eliminarPropiedad(getIntent().getStringExtra("inmbID")).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.i(TAG, "PropiedadLayout: propiedad se ha eleminado con exito");
                    Toast.makeText(PropiedadLayout.this,"Propiedad se ha eleminado con exito", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    private void crearPropiedad() {

        inmbl = setPropiedad();
        if (inmbl != null){
            mPropiedadProvider.setPropiedad(inmbl).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Log.i(TAG, "PropiedadLayout: propiedad se ha creado co exito");
                        Toast.makeText(PropiedadLayout.this,"Propiedad se ha guardado con exito", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Log.i(TAG, "PropiedadLayout: fallo al guardar la propiedad");
                        Toast.makeText(PropiedadLayout.this,"fallo al guardar la propiedad", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void actualizarPropiedad() {
        inmbl = setPropiedad();
        inmbl.setpID(getIntent().getStringExtra("inmbID"));
        mPropiedadProvider.updatePropiedad(inmbl).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(PropiedadLayout.this, "Propiedad actualizada", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    private Propiedad setPropiedad(){

        Boolean isDatosCorrectos = true;
        Propiedad propiedad = null;

        String propNombreDescriptivo = etPropNombreDescriptivo.getText().toString().trim();
        if (propNombreDescriptivo.isEmpty()){
            etPropNombreDescriptivo.setError("no puede ser vacío");
            isDatosCorrectos = false;
        } else if (propNombreDescriptivo.length() < 3){
            etPropNombreDescriptivo.setError("debe tener al menos 3 caracteres");
            isDatosCorrectos = false;
        }

        String propTipo = etPropTipo.getText().toString().trim();
        if (propTipo.isEmpty()){
            etPropTipo.setError("no puede ser vacío");
            isDatosCorrectos = false;
        } else if (propTipo.length() < 3){
            etPropTipo.setError("debe tener al menos 3 caracteres");
            isDatosCorrectos = false;
        }

        String propPeriodo = etPropPeriodo.getText().toString().trim();
        if (propPeriodo.isEmpty()){
            etPropPeriodo.setError("no puede ser vacío");
            isDatosCorrectos = false;
        } else if (propPeriodo.length() < 3){
            etPropPeriodo.setError("debe tener al menos 3 caracteres");
            isDatosCorrectos = false;
        }

        String propPrecio = etPropPrecio.getText().toString().trim();
        Log.i(TAG, "PropiedadLayout: propPrecio " + propPrecio);
        if (!propPrecio.matches("[1-9]{1}[0-9]*")){
            etPropPrecio.setError("debe ser un numero natural positivo");
            isDatosCorrectos = false;
        }

        String propProvincia = etPropProvincia.getText().toString().trim();
        if (propProvincia.isEmpty()){
            etPropProvincia.setError("no puede ser vacío");
            isDatosCorrectos = false;
        } else if (propProvincia.length() < 3){
            etPropProvincia.setError("debe tener al menos 3 caracteres");
            isDatosCorrectos = false;
        }

        String propMunicipio = etPropMunicipio.getText().toString().trim();
        if (propMunicipio.isEmpty()){
            etPropMunicipio.setError("no puede ser vacío");
            isDatosCorrectos = false;
        } else if (propMunicipio.length() < 3){
            etPropMunicipio.setError("debe tener al menos 3 caracteres");
            isDatosCorrectos = false;
        }

        String propCP = etPropCP.getText().toString().trim();
        Log.i(TAG, "PropiedadLayout: propPrecio " + propPrecio);
        if (!propCP.matches("[0-9]{5}")){
            etPropCP.setError("debe 5 naturales, ej. 04740");
            isDatosCorrectos = false;
        }

        String propDireccion = etPropDireccion.getText().toString().trim();
        if (propDireccion.isEmpty()){
            etPropDireccion.setError("no puede ser vacío");
            isDatosCorrectos = false;
        } else if (propDireccion.length() < 3){
            etPropDireccion.setError("debe tener al menos 3 caracteres");
            isDatosCorrectos = false;
        }

        if (isDatosCorrectos){
            propiedad = new Propiedad(
                    fbUser.getUid(),
                    propNombreDescriptivo,
                    propTipo,
                    propPeriodo,
                    propPrecio,
                    propProvincia,
                    propCP,
                    propMunicipio,
                    propDireccion
            );
        }

        return propiedad;
    }

    private void initComponents() {

        mAuthProvider = new AuthProvider();
        mPropiedadProvider = new PropiedadProvider();

        etPropNombreDescriptivo = findViewById(R.id.plEtPropNombreDescriptivo);
        etPropTipo = findViewById(R.id.plEtPropTipo);
        etPropPrecio = findViewById(R.id.plEtPropPrecio);
        etPropPeriodo = findViewById(R.id.plEtPropPeriodo);
        etPropProvincia = findViewById(R.id.plEtPropProvincia);
        etPropMunicipio = findViewById(R.id.plEtPropMunicipio);
        etPropCP = findViewById(R.id.plEtPropCP);
        etPropDireccion = findViewById(R.id.plEtPropDireccion);
    }
}