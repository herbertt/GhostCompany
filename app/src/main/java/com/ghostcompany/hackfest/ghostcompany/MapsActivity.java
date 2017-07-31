package com.ghostcompany.hackfest.ghostcompany;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ghostcompany.hackfest.ghostcompany.Async.AsyncGetEmpresas;
import com.ghostcompany.hackfest.ghostcompany.Async.AsyncGetInform;
import com.ghostcompany.hackfest.ghostcompany.models.Empresa;
import com.ghostcompany.hackfest.ghostcompany.models.Informe;
import com.ghostcompany.hackfest.ghostcompany.models.OnGetEmpresaCompletedCallback;
import com.ghostcompany.hackfest.ghostcompany.models.OnGetEmpresaInfoCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, OnGetEmpresaInfoCallback,
        OnGetEmpresaCompletedCallback{
    private Empresa empresa;
    private GoogleMap mMap;
    private HashMap<String, String> markers; // marcadores das empresas
    private List<Informe> listInfos = new ArrayList<Informe>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // Pegar o mapa para ser exibido
        mapFragment.getMapAsync(this);

        try {
            AsyncGetInform asyncGetInform = new AsyncGetInform(MapsActivity.this);
            asyncGetInform.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        markers = new HashMap<String,String>();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        configurarMapa();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerClickListener(new OnMarkerListenerShowEmpresa());
        // mMap.setOnInfoWindowClickListener(new OnInfoWindowListenerShowEmpresa());


        try {
            AsyncGetEmpresas asyncGetEmpresas = new AsyncGetEmpresas(MapsActivity.this);
            asyncGetEmpresas.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }


    private void configurarMapa() {
//        mMap.setMinZoomPreference(15.0f);
        Log.i("ZOOM", String.valueOf(mMap.getMinZoomLevel()) );

        LatLng currentLatLng = new LatLng(-7.11532, -34.861);
        this.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 6));

        this.mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

    }


    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onGetEmpresaInfoCompleted(List<Informe> infos) {

        listInfos = infos;

    }


    private class OnMarkerListenerShowEmpresa implements GoogleMap.OnMarkerClickListener{
        @Override
        public boolean onMarkerClick(Marker marker) {
//            Toast.makeText(getApplicationContext(), "Click no marcador", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private class OnInfoWindowListenerShowEmpresa implements GoogleMap.OnInfoWindowClickListener{
        @Override
        public void onInfoWindowClick(Marker marker) {
            Intent it = new Intent(MapsActivity.this, MainActivity.class);
            it.putExtra("obj", MapsActivity.this.empresa);
            startActivity(it);
        }
    }


    @Override
    public void onGetEmpresaCompleted(List<Empresa> empresas) {

        if(empresas!=null) {
            for (Empresa empresa : empresas) {

                LatLng latLng = new LatLng(Double.parseDouble(empresa.getLat()), Double.parseDouble(empresa.getLng()));
                Marker marker = this.addMarker(empresa.getTitle(), latLng, empresa.getEmpresaCode());
                markers.put(marker.getId(), String.valueOf(empresa.getIdEmpresa()));
            }

            //  List<Marcador> list = new ArrayList<>();;
            //   Iterator<Empresa> it = null;
            //  it = occurrences.iterator();
          /*  while (it.hasNext()) {
                Empresa o = it.next();
                Marcador m = new Marcador(new LatLng(Double.parseDouble(o.getLat()),Double.parseDouble(o.getLng())),
                        OCCURRENCES[o.getEmpresaCode().intValue()],
                        o.getEmpresaCode().intValue(),o.getIdEmpresa());
                mClusterManager.addItem(m);
            }*/
            // mClusterManager.addItems(list);
            //mClusterManager.setRenderer(new OwnIconRendered(this, this.googleMap, mClusterManager));

        }

    }

    public Marker addMarker(String title,LatLng latLng, int cnpj){
        String[] res =  getSnippetInfo(String.valueOf(cnpj));
        Bitmap bitmap;
        if(Integer.parseInt(res[0])>50){
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.marker_alert);
        }else {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_map_marker);
        }
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title+"  CNPJ: "+cnpj).icon(BitmapDescriptorFactory.fromBitmap(bitmap));

        markerOptions.snippet(res[0]+"% Sim "+res[1]+"% Não");
        return mMap.addMarker(markerOptions);
    }

    public String[] getSnippetInfo(String cnpj){
        int total = 0;
        int yes = 0;
        int no = 0;

        for (Informe info: listInfos) {
            if (info.getCnpj().equals(cnpj)){
                total++;
                if (info.getYesNoInfo().equals("1")){
                    yes++;
                }else if(info.getYesNoInfo().equals("0")){
                    no++;
                }
            }
        }
        String percYes = String.valueOf((yes*100)/total);
        String percNo = String.valueOf((no*100)/total);
        String[] parts = new String[2];
        parts[0] = percYes;
        parts[1] = percNo;

        return parts;
    }


}