package com.example.pdam.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pdam.R;
import com.example.pdam.models.User;
import com.example.pdam.providers.AuthProvider;
import com.example.pdam.providers.UserProvider;
import com.example.pdam.views.identificacion.AuthMainActivity;
import com.example.pdam.views.propiedad.PropiedadesActivity;
import com.example.pdam.views.usuario.UserLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class PPAActivity extends AppCompatActivity {

    private TextView tvUserName;
    private Button btnOpcion;
    private User usuario;
    private AuthProvider mAuthProvider;
    private UserProvider mUserProvider;
    private FirebaseUser fbUser;

    private static final String TAG = "DEV";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppa);

        Log.i(TAG, "-----------------------------------------------------------------");
        Log.i(TAG, "-----------------------------------------------------------------");
        Log.i(TAG, "-----------------------------------------------------------------");
        Log.i(TAG, "PPAActivity: init");

        tvUserName = findViewById(R.id.tvUserName);

        btnOpcion = findViewById(R.id.btnOpcion);
        btnOpcion.setText("IDENTIFICARSE");

        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();

        btnOpcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "PPAActivity: inicio de identifiación");
                Intent intent = new Intent(PPAActivity.this, AuthMainActivity.class);
                Log.i(TAG, "PPAActivity: rederect a AuthMainActivity");
                startActivity(intent);
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
        //invalidateOptionsMenu();
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
                    //mAuthProvider.cerarSecion();
                } finally {

                }

                Log.i(TAG, "PPAActivity: usuario: " + usuario.getuName());

                tvUserName.setText(usuario.getuName());
                Log.i(TAG, "PPAActivity: onStart: usuario: " + usuario.getuName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i(TAG, "PPAActivity: obtención de datos ha fallado:");
                tvUserName.setText("algo va mal");
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}