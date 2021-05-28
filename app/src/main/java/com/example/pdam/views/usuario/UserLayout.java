package com.example.pdam.views.usuario;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pdam.R;
import com.example.pdam.models.User;
import com.example.pdam.providers.AuthProvider;
import com.example.pdam.providers.UserProvider;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.net.URI;

public class UserLayout extends AppCompatActivity {

    private User usuario;
    private AuthProvider mAuthProvider;
    private UserProvider mUserProvider;
    private FirebaseUser fbUser;

    private EditText etNombreCompleto;
    private EditText etTelefono;

    private Button btnModificar;
    private Button btnCancelar;
    private Button btnAplicar;

    private ImageView ivFotoUser;
    private String uFotoURI;
    private ImageButton ibtnFotoChange;

    private StorageReference storageRef;

    private static final String TAG = "DEV";
    private final int REQUEST_CODE_FOTO_SELECT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Perfil");

        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();

        etNombreCompleto = findViewById(R.id.user_et_uNombreCompleto);
        etTelefono = findViewById(R.id.user_et_uTelefono);

        ivFotoUser = findViewById(R.id.user_iv_uFoto);

        btnModificar = findViewById(R.id.user_btn_modificar);
        btnCancelar = findViewById(R.id.user_btn_cancelar);
        btnAplicar = findViewById(R.id.user_btn_aplicar);
        ibtnFotoChange = findViewById(R.id.user_ib_foto_change);

        btnAplicar.setVisibility(View.GONE);
        btnCancelar.setVisibility(View.GONE);
        ibtnFotoChange.setVisibility(View.GONE);

        storageRef = FirebaseStorage.getInstance().getReference("user");

        //eventos

        ibtnFotoChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_FOTO_SELECT);
            }
        });

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNombreCompleto.setEnabled(true);
                etTelefono.setEnabled(true);
                btnAplicar.setVisibility(View.VISIBLE);
                btnCancelar.setVisibility(View.VISIBLE);
                ibtnFotoChange.setVisibility(View.VISIBLE);
                btnModificar.setVisibility(View.GONE);
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

                Log.i(TAG, "UserLayout: obtención de datos exitosa:");
                usuario = new User(snapshot.getValue(User.class));
                Log.i(TAG, "UserLayout: usuario: " + usuario.getuName());

                etNombreCompleto.setText(usuario.getuName());

                if(!usuario.getuTelefono().isEmpty()){
                    etTelefono.setText(usuario.getuTelefono());
                }

                if(uFotoURI == null){
                    if (usuario.getuFotoURI().isEmpty()){
                        ivFotoUser.setImageDrawable(getDrawable(R.drawable.ic_hamster_svg));
                    } else {
                        //ivFotoUser.setImageURI(Uri.parse(usuario.getuFotoURI()));
                        Picasso.get().load(usuario.getuFotoURI()).into(ivFotoUser);
                        uFotoURI = usuario.getuFotoURI();
                    }
                } else {
                    if(!uFotoURI.isEmpty()){
                        ivFotoUser.setImageURI(Uri.parse(uFotoURI));
                    }
                }

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
            if (uFotoURI != null){
                if (!uFotoURI.isEmpty()){
                    Bitmap bitmap = ((BitmapDrawable) ivFotoUser.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] byteArray = baos.toByteArray();
                    final StorageReference curRef = storageRef.child(usuario.getuID()).child("foto").child("fotoProfile");
                    UploadTask up = curRef.putBytes(byteArray);
                    Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            return curRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                User newUser = new User(usuario.getuID(), uName, usuario.getuEmail(), etTelefono.getText().toString().trim(), task.getResult().toString());
                                mUserProvider.setUsuario(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(UserLayout.this, "Los datos se han modificado con exito", Toast.LENGTH_LONG).show();
                                            recargarIntent();
                                        } else {
                                            Toast.makeText(UserLayout.this, "Fallo al guardar datos del usuario", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            } else {
                User newUser = new User(usuario.getuID(), uName, usuario.getuEmail(), etTelefono.getText().toString().trim(), "");
                mUserProvider.setUsuario(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserLayout.this, "Los datos se han modificado con exito", Toast.LENGTH_LONG).show();
                            recargarIntent();
                        } else {
                            Toast.makeText(UserLayout.this, "Fallo al guardar datos del usuario", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }

    /**
     * recarga Perfil del usuario
     */
    private void recargarIntent() {
        Intent intent = new Intent(UserLayout.this, UserLayout.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_CODE_FOTO_SELECT){
                if(data != null){
                    Log.i(TAG, "Img URI: " + data.getData() );
                    uFotoURI = data.getData().toString();
                }
            }
        }
    }
}