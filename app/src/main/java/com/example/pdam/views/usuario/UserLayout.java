package com.example.pdam.views.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pdam.R;
import com.example.pdam.models.User;
import com.example.pdam.providers.AuthProvider;
import com.example.pdam.providers.UserProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class UserLayout extends AppCompatActivity {

    private User usuario;
    private AuthProvider mAuthProvider;
    private UserProvider mUserProvider;
    private FirebaseUser fbUser;

    private EditText etNombreCompleto;
    private Button btnModificar;
    private Button btnCancelar;
    private Button btnAplicar;

    private LinearLayout llModificar;
    private LinearLayout llAplicarCancelar;

    private static final String TAG = "DEV";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Perfil");

        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();

        etNombreCompleto = findViewById(R.id.ulEtNombreCompleto);

        btnModificar = findViewById(R.id.ulBtnModificar);
        btnCancelar = findViewById(R.id.ulBtnCancelar);
        btnAplicar = findViewById(R.id.ulBtnAplicar);

        llModificar = findViewById(R.id.ulLlModoficar);
        llAplicarCancelar = findViewById(R.id.ulLlAplicarCancelar);

        llAplicarCancelar.setVisibility(View.INVISIBLE);

        //eventos
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNombreCompleto.setEnabled(true);
                llModificar.setVisibility(View.INVISIBLE);
                llAplicarCancelar.setVisibility(View.VISIBLE);
            }
        });

        btnAplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDatosDelUsuario();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recargarIntent();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        fbUser = mAuthProvider.getUsuario();

        if (fbUser != null) {
            getDatosDelUsuario(fbUser.getUid());
        } else {
            etNombreCompleto.setText("no debe aparecer");
        }
    }

    /**
     * obtiene datos del usuario
     * @param uID
     */
    private void getDatosDelUsuario(String uID) {

        mUserProvider.getUsuarioById(uID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.i(TAG, "PPAActivity: obtención de datos exitosa:");
                usuario = new User(snapshot.getValue(User.class));
                Log.i(TAG, "PPAActivity: usuario: " + usuario.getuName());

                etNombreCompleto.setText(usuario.getuName());
                Log.i(TAG, "PPAActivity: onStart: usuario: " + usuario.getuName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i(TAG, "PPAActivity: obtención de datos ha fallado:");
                etNombreCompleto.setText("algo va mal");
                usuario = null;
            }
        });
    }

    /**
     * guarda datos del usuario en la base de datos
     */
    private void setDatosDelUsuario() {
        String uName = etNombreCompleto.getText().toString();
        if (uName.isEmpty()){
            Toast.makeText(UserLayout.this,"Nomre no debe ser vacío",Toast.LENGTH_LONG).show();
        } else {
            User newUser = new User(usuario.getuID(), uName, usuario.getuEmail());
            mUserProvider.setUsuario(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(UserLayout.this,"Los datos se han modificado con exito",Toast.LENGTH_LONG).show();
                        recargarIntent();
                    } else {
                        Toast.makeText(UserLayout.this,"Fallo al guardar datos del usuario",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    /**
     * recarga Perfil del usuario
     */
    private void recargarIntent() {
        Intent intent = new Intent(UserLayout.this, UserLayout.class);
        startActivity(intent);
    }

}