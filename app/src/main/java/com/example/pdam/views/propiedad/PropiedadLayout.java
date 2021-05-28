package com.example.pdam.views.propiedad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.pdam.R;
import com.example.pdam.models.Inmueble;
import com.example.pdam.models.User;
import com.example.pdam.providers.AuthProvider;
import com.example.pdam.providers.PropiedadProvider;
import com.example.pdam.views.usuario.UserLayout;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PropiedadLayout extends AppCompatActivity {

    private Inmueble inmbl;
    private AuthProvider mAuthProvider;
    private PropiedadProvider mPropiedadProvider;
    private FirebaseUser fbUser;

    private ImageView ivPropFoto;
    private Button btnSetImg;

    private EditText etPropNombreDescriptivo;
    private EditText etPropDescripcionComp;
    private EditText etPropTipo;
    private EditText etPropPeriodo;
    private EditText etPropPrecio;
    private EditText etPropProvincia;
    private EditText etPropMunicipio;
    private EditText etPropCP;
    private double inmbGEOLat;
    private double inmbGEOLng;
    private EditText etPropDireccion;
    private Switch swPropDisp;

    private String propFotoURI;
    private StorageReference storageRef;

    private String inmblID;

    private static final String TAG = "DEV";
    private final int REQUEST_CODE_FOTO_SELECT = 11;

    private enum MODE {
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
        setEventListeners();

        switch (getIntent().getStringExtra("mode")) {
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
                inmbl = new Inmueble(snapshot.getValue(Inmueble.class));

                etPropNombreDescriptivo.setText(inmbl.getInmbNombreDesc());
                etPropDescripcionComp.setText(inmbl.getInmbDescCompleta());
                etPropTipo.setText(inmbl.getInmbTipo());
                etPropPeriodo.setText(inmbl.getInmbPeriodo());
                etPropPrecio.setText(inmbl.getInmbPrecio());
                etPropProvincia.setText(inmbl.getInmbProvincia());
                etPropMunicipio.setText(inmbl.getInmbMunicipio());
                etPropCP.setText(inmbl.getInmbCP());
                etPropDireccion.setText(inmbl.getInmbDireccion());
                swPropDisp.setChecked(inmbl.getInmbDisp());
                propFotoURI = inmbl.getInmbFotoURI();
                Picasso.get().load(propFotoURI).into(ivPropFoto);
                inmbGEOLat = inmbl.getInmbGEOLat();
                inmbGEOLng = inmbl.getInmbGEOLng();
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
        switch (mode) {
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

        switch (item.getItemId()) {

            case R.id.menu_prop_crear:
                crearPropiedad();
                return true;

            case R.id.menu_prop_update:
                actualizarPropiedad();
                return true;

            case R.id.menu_prop_delete:
                //eliminarPropiedad();
                return true;

            case R.id.menu_prop_alqiler:
                //alqularPropiedad();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
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
*/
    private void crearPropiedad() {

        inmbl = setPropiedad();

        if (inmbl != null) {
            /* se prepara foto para subir */
            Bitmap bitmap = ((BitmapDrawable) ivPropFoto.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArray = baos.toByteArray();
            /* se obtiene identificador unico para la inmueble */
            DatabaseReference pushedPropRef = mPropiedadProvider.getPropiedadesDataBaseReference().push();
            inmbl.setInmbID(pushedPropRef.getKey());

            final StorageReference curRef = storageRef.child(inmbl.getInmbID()).child("foto").child("fotoInmb");
            UploadTask up = curRef.putBytes(byteArray);
            Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    return curRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        inmbl.setInmbFotoURI(task.getResult().toString());
                        mPropiedadProvider.setInmb(inmbl).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.i(TAG, "PropiedadLayout: propiedad se ha creado co exito");
                                    Toast.makeText(PropiedadLayout.this, "Propiedad se ha guardado con exito", Toast.LENGTH_LONG).show();
                                    Log.i(TAG, "PropiedadLayout: paso a la lista de propiedades");
                                    finish();
                                } else {
                                    Log.i(TAG, "PropiedadLayout: fallo al guardar la propiedad");
                                    Toast.makeText(PropiedadLayout.this, "fallo al guardar la propiedad", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            });
        }
    }


    private void actualizarPropiedad() {
        inmbl = setPropiedad();
        if(inmbl != null){
            inmbl.setInmbID(getIntent().getStringExtra("inmbID"));
            /**
             * subir imagetn
             */
            Bitmap bitmap = ((BitmapDrawable) ivPropFoto.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArray = baos.toByteArray();

            final StorageReference curRef = storageRef.child(inmbl.getInmbID()).child("foto").child("fotoInmb");
            UploadTask up = curRef.putBytes(byteArray);
            Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    return curRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        inmbl.setInmbFotoURI(task.getResult().toString());
                        mPropiedadProvider.updatePropiedad(inmbl).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(PropiedadLayout.this, "Propiedad actualizada", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Log.i(TAG, "PropiedadLayout: fallo al actualizar la propiedad");
                                    Toast.makeText(PropiedadLayout.this, "fallo al guardar la propiedad", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            });
        }
    }



    private Inmueble setPropiedad() {

        Boolean isDatosCorrectos = true;
        Inmueble propiedad = null;

        if (propFotoURI.isEmpty()) {
            Toast.makeText(this, "Hace falta selecciónar una imagen", Toast.LENGTH_SHORT).show();
            isDatosCorrectos = false;
        }

        String propNombreDescriptivo = etPropNombreDescriptivo.getText().toString().trim();
        if (propNombreDescriptivo.isEmpty()) {
            etPropNombreDescriptivo.setError("no puede ser vacío");
            isDatosCorrectos = false;
        } else if (propNombreDescriptivo.length() < 3) {
            etPropNombreDescriptivo.setError("debe tener al menos 3 caracteres");
            isDatosCorrectos = false;
        }

        String propDescripcionCompleta = etPropDescripcionComp.getText().toString().trim();
        if (propDescripcionCompleta.isEmpty()) {
            etPropDescripcionComp.setError("no puede ser vacío");
            isDatosCorrectos = false;
        } else if (propDescripcionCompleta.length() < 3) {
            etPropDescripcionComp.setError("debe tener al menos 3 caracteres");
            isDatosCorrectos = false;
        }

        String propTipo = etPropTipo.getText().toString().trim();
        if (propTipo.isEmpty()) {
            etPropTipo.setError("no puede ser vacío");
            isDatosCorrectos = false;
        } else if (propTipo.length() < 3) {
            etPropTipo.setError("debe tener al menos 3 caracteres");
            isDatosCorrectos = false;
        }

        String propPeriodo = etPropPeriodo.getText().toString().trim();
        if (propPeriodo.isEmpty()) {
            etPropPeriodo.setError("no puede ser vacío");
            isDatosCorrectos = false;
        } else if (propPeriodo.length() < 3) {
            etPropPeriodo.setError("debe tener al menos 3 caracteres");
            isDatosCorrectos = false;
        }

        String propPrecio = etPropPrecio.getText().toString().trim();
        //Log.i(TAG, "PropiedadLayout: propPrecio " + propPrecio);
        if (!propPrecio.matches("[1-9]{1}[0-9]*")) {
            etPropPrecio.setError("debe ser un numero natural positivo");
            isDatosCorrectos = false;
        }

        String propProvincia = etPropProvincia.getText().toString().trim();
        if (propProvincia.isEmpty()) {
            etPropProvincia.setError("no puede ser vacío");
            isDatosCorrectos = false;
        } else if (propProvincia.length() < 3) {
            etPropProvincia.setError("debe tener al menos 3 caracteres");
            isDatosCorrectos = false;
        }

        String propMunicipio = etPropMunicipio.getText().toString().trim();
        if (propMunicipio.isEmpty()) {
            etPropMunicipio.setError("no puede ser vacío");
            isDatosCorrectos = false;
        } else if (propMunicipio.length() < 3) {
            etPropMunicipio.setError("debe tener al menos 3 caracteres");
            isDatosCorrectos = false;
        }

        String propCP = etPropCP.getText().toString().trim();
        Log.i(TAG, "PropiedadLayout: propPrecio " + propPrecio);
        if (!propCP.matches("[0-9]{5}")) {
            etPropCP.setError("debe 5 naturales, ej. 04740");
            isDatosCorrectos = false;
        }

        String propDireccion = etPropDireccion.getText().toString().trim();
        if (propDireccion.isEmpty()) {
            etPropDireccion.setError("no puede ser vacío");
            isDatosCorrectos = false;
        } else if (propDireccion.length() < 3) {
            etPropDireccion.setError("debe tener al menos 3 caracteres");
            isDatosCorrectos = false;
        }

        if (isDatosCorrectos) {

            try {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                String dir = "España, " + propProvincia + ", " + propMunicipio + ", " + propCP + ", " + propDireccion;
                Log.i(TAG, "inmbGEO dir: " + dir);
                List<Address> geoResults = geocoder.getFromLocationName(dir, 1);

                if (geoResults.size() > 0) {
                    Address addr = geoResults.get(0);
                    LatLng inmbGEO = new LatLng(addr.getLatitude(), addr.getLongitude());

                    Log.i(TAG, "inmbGEO: " + inmbGEO.toString());

                    propiedad = new Inmueble(
                            "",
                            fbUser.getUid(),
                            propNombreDescriptivo,
                            propDescripcionCompleta,
                            propTipo,
                            propPeriodo,
                            propPrecio,
                            propProvincia,
                            propMunicipio,
                            propDireccion,
                            propCP,
                            addr.getLatitude(),
                            addr.getLongitude(),
                            propFotoURI,
                            swPropDisp.isChecked(),
                            Calendar.getInstance().getTimeInMillis() * (-1)

                    );

                }

            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println(ex.toString());
                propiedad = null;
            }

        }

        return propiedad;
    }

    private void initComponents() {

        mAuthProvider = new AuthProvider();
        mPropiedadProvider = new PropiedadProvider();

        etPropNombreDescriptivo = findViewById(R.id.inmb_et_inmbNombreDesc);
        etPropDescripcionComp = findViewById(R.id.inmb_et_inmbDescCompleta);
        etPropTipo = findViewById(R.id.inmb_et_inmbTipo);
        etPropPeriodo = findViewById(R.id.inmb_et_inmbPeriodo);
        etPropPrecio = findViewById(R.id.inmb_et_inmbPrecio);
        etPropProvincia = findViewById(R.id.inmb_et_inmbProvincia);
        etPropMunicipio = findViewById(R.id.inmb_et_inmbMunicipio);
        etPropCP = findViewById(R.id.inmb_et_inmbCP);
        etPropDireccion = findViewById(R.id.inmb_et_inmbDireccion);
        swPropDisp = findViewById(R.id.inmb_sw_inmbDisp);
        ivPropFoto = findViewById(R.id.inmb_iv_uFoto);

        btnSetImg = findViewById(R.id.inmb_btn_SelectImg);
        propFotoURI = "";

        storageRef = FirebaseStorage.getInstance().getReference("inmueble");
    }

    private void setEventListeners() {
        btnSetImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_FOTO_SELECT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_FOTO_SELECT) {
                if (data != null) {
                    Log.i(TAG, "Img URI: " + data.getData());
                    propFotoURI = data.getData().toString();
                    ivPropFoto.setImageURI(Uri.parse(propFotoURI));
                }
            }
        }
    }
}