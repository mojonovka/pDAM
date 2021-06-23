package com.example.pdam.views.propiedad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pdam.R;
import com.example.pdam.models.Inmueble;
import com.example.pdam.models.User;
import com.example.pdam.providers.PropiedadProvider;
import com.example.pdam.providers.UserProvider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PropInfo extends AppCompatActivity implements OnMapReadyCallback {

    private String usuarioID;
    private String inmbID;

    private TextView tvInfoNombreDescriptivo;
    private ImageView ivInfoFoto;
    private TextView tvInfoDescripcion;
    private TextView tvInfoDireccion;
    private Button btnInfoContactar;

    private MapView mMapView;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private PropiedadProvider mPropiedadProvider;
    private Inmueble inmbl;
    private User usuario;


    private UserProvider mUserProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prop_info);

        usuarioID = getIntent().getStringExtra("usuarioID");
        inmbID = getIntent().getStringExtra("inmbID");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView = findViewById(R.id.infoMapa);
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);

        inicializarComponentes();
        rellenarPropiedad(inmbID);
        setlisteners();
    }

    private void setlisteners() {
        btnInfoContactar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //iniciar chat

                mUserProvider.getUsuarioById(inmbl.getInmbPropID()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {

                        usuario = new User(snapshot.getValue(User.class));

                        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + usuario.getuEmail()));
                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void inicializarComponentes() {
        mPropiedadProvider = new PropiedadProvider();
        mUserProvider = new UserProvider();

        tvInfoNombreDescriptivo = findViewById(R.id.infoNombreDescriptivo);
        ivInfoFoto = findViewById(R.id.infoFoto);

        tvInfoDescripcion = findViewById(R.id.infoDescripcion);
        tvInfoDireccion = findViewById(R.id.infoDireccion);
        btnInfoContactar = findViewById(R.id.infoBTNContactar);

    }

    private void rellenarPropiedad(String inmbID) {
        mPropiedadProvider.getInmbById(inmbID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                inmbl = new Inmueble(snapshot.getValue(Inmueble.class));

                tvInfoNombreDescriptivo.setText(inmbl.getInmbNombreDesc());

                Picasso.get().load(inmbl.getInmbFotoURI()).into(ivInfoFoto);

                tvInfoDescripcion.setText("Tipo: " + inmbl.getInmbTipo() + "\n" + "Precio: " + inmbl.getInmbPrecio() + "€ / " + inmbl.getInmbPeriodo() + " \n\nDescripción: \n" + inmbl.getInmbDescCompleta());

                tvInfoDireccion.setText("Dirección: " + inmbl.getInmbProvincia() + "\n" + inmbl.getInmbMunicipio() + " : " + inmbl.getInmbCP() + "\n" + inmbl.getInmbDireccion());

                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {

                        LatLng pos = new LatLng(inmbl.getInmbGEOLat(),inmbl.getInmbGEOLng());

                        googleMap.addMarker(new MarkerOptions().position(pos).title("*"));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15.5f), 4000, null);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //googleMap.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("Marker"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + inmbl.getInmbGEOLat() + "," + inmbl.getInmbGEOLng());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    startActivity(mapIntent);
                }
            });
            return;
        }
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null){
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}