package com.ghostcompany.hackfest.ghostcompany;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set view as the XML defined
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapEmpresas);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        configurarMapa();

//        TODO enquanto tiver empresas,
        adicionarMarcadores();

        mMap.setMinZoomPreference(15.0f);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerClickListener(new OnMarkerListenerShowEmpresa());
        mMap.setOnInfoWindowClickListener(new OnInfoWindowListenerShowEmpresa());

    }

    private void adicionarMarcadores() {
//        TODO definir que quando tiver empresas, vai setar um ponteiro naquela posição
        LatLng empresa = new LatLng(-7.1218496, -34.8427769);
        mMap.addMarker(new MarkerOptions().position(empresa).title("Marcador teste"))
                .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_marker));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(empresa));
    }

    private void configurarMapa(){
        mMap.setMinZoomPreference(15.0f);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }


    private class OnMarkerListenerShowEmpresa implements GoogleMap.OnMarkerClickListener{

//        Setar o que acontecerá quando clicar num marcador (padrão: abrir um textbox)
        @Override
        public boolean onMarkerClick(Marker marker) {
            Toast.makeText(getApplicationContext(), "Click no marcador", Toast.LENGTH_SHORT).show();

            return false;
        }
    }

    private class OnInfoWindowListenerShowEmpresa implements GoogleMap.OnInfoWindowClickListener {
        @Override
        public void onInfoWindowClick(Marker marker) {
            Intent it = new Intent(MapsActivity.this, MainActivity.class);
            startActivity(it);
        }
    }
}
