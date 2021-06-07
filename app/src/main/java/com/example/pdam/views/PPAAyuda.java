package com.example.pdam.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.os.Bundle;
import android.text.Spanned;
import android.widget.TextView;

import com.example.pdam.R;

public class PPAAyuda extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppa_ayuda);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvAyuda = findViewById(R.id.ayuda_tv_contenido);
        Spanned spanned = HtmlCompat.fromHtml(contenido, HtmlCompat.FROM_HTML_MODE_COMPACT);
        tvAyuda.setText(spanned);

    }

    private TextView tvAyuda;
    private String contenido ="" +
            "<p>" +
            "<b>Tipos de usuarios: </b> la aplicación podrán utilizar dos tipos de usuarios, autorizados y no autorizados. " +
            "Los ultimos tienes limitaciones a la hora de utilizar la funcionalidad " +
            "de la aplicación completa." +
            "</p><br/>" +
            "<p>" +
            "<b>Usuarios Autorizados: </b>son aquellos usuarios que se han registrado en la aplicación " +
            "y han entrado en ella utilizando sus credenciales." +
            "</p><br/>" +
            "<p>" +
            "<b>Usuarios no Autorizados:  </b>son aquellos usuarios que no han entrado en la aplicación " +
            "utilizando sus credenciales, aunque disponen de ellos." +
            "</p><br/>" +
            "<p>" +
            "<b>Crear cuenta/obtener credenciales:  </b>para realizar esta operación hay que pinchar " +
            "en el boton `identificarse` y elegir `crear una cuenta nueva` y " +
            "rellenar los datos que se piden correctamente." +
            "</p><br/>" +
            "<p>" +
            "<b>Lista de inmuebles disponibles para alquiler:  </b>En la pantalla principal " +
            "de la aplicación se despliega una lista de inmuebles, los cuales, los propietarios " +
            "dejaron disponibles para ser alquilados. Esta lista está disponible tanto " +
            "para ambos tipos de usuarios." +
            "</p><br/>" +
            "<p>" +
            "<b>Filtros de búsqueda:  </b>En la pantalla principal de la aplicación está situado botón " +
            "para cambiar filtros de búsqueda que se aplican a los inmuebles. " +
            "Utilizar estos filtros pueden ambos tipos de usuarios." +
            "</p><br/>" +
            "<p>" +
            "<b>Inquilinos:  </b>son usuarios autorizados que pretenden alquilar un inmueble." +
            "</p><br/>" +
            "<p>" +
            "<b>Propietarios: </b>son usuarios autorizados de la aplicación que pretenden dejar " +
            "en alquiler su inmueble, previamente registrado." +
            "<br/><br/>La misma persona dependiendo de sus necesidades puede interactuar como inquilino o como propietario.\n" +
            "</p><br/>" +
            "<p>" +
            "<b>Inmuebles: </b>El usuario puede añadir cualquier número de inmuebles. Su consulta " +
            "está disponible en el punto del menú `Inmuebles`. Desde allí podrá consultar " +
            "su estado, añadir nuevos y eliminar o modificar ya existentes." +
            "<br />Cuando el inmueble ya está alquilado su propietario debe modificar " +
            "su propiedad `disponibilidad` lo que impide que este inmueble aparezca " +
            "en la lista de inmuebles para alquiler." +
            "</p><br/>" +
            "" +
            "" +
            "";
}