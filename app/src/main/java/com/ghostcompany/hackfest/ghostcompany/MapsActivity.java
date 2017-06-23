package com.ghostcompany.hackfest.ghostcompany;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ghostcompany.hackfest.ghostcompany.Async.AsyncGetEmpresas;
import com.ghostcompany.hackfest.ghostcompany.models.AdapterEntity;
import com.ghostcompany.hackfest.ghostcompany.models.Empresa;
import com.ghostcompany.hackfest.ghostcompany.models.Entity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    private Empresa empresa;
    private GoogleMap mMap;
    private HashMap<String, String> markers; // marcadores das empresas
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // Pegar o mapa para ser exibido
        mapFragment.getMapAsync(this);
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
//        DoSomethingThread
        // Enquanto tiver empresas, fará:
        adicionarMarcadores();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerClickListener(new OnMarkerListenerShowEmpresa());
        mMap.setOnInfoWindowClickListener(new OnInfoWindowListenerShowEmpresa());

    }

//	Calcular a distância entre dois pontos (lat, long) em K, M ou N
    private double calcularDistanciaEmpresasPerto(String latitude1, String longitude1,
                            String latitude2, String longitude2, char unit) {

        double lat1 = Double.valueOf(latitude1).doubleValue();
        double lon1 = Double.valueOf(longitude1).doubleValue();
        double lat2 = Double.valueOf(latitude2).doubleValue();
        double lon2 = Double.valueOf(longitude2).doubleValue();
        double dist = 0.0;
        double R = 6372.8; // In kilometers



        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2)
                * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));

        dist = R * c;

        if (unit == 'K') {
            dist = dist * 1.609344;

        } else if (unit == 'N') {
            dist = dist * 0.8684;

        } else if (unit == 'M') {
            dist = dist * 1000.0;
        }
        return (dist);
    }

    private void configurarMapa() {
//        mMap.setMinZoomPreference(15.0f);
        Log.i("ZOOM", String.valueOf(mMap.getMinZoomLevel()) );


        this.mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

    }

    private void adicionarMarcadores(/*List<Empresa> empresas*/){
        int i = 0;

        LatLng[] testeLL = {new LatLng(-7.1218496, -34.8427769), new LatLng(-7.1203619,-34.8458601),
                new LatLng(-7.120312, -34.871368), new LatLng(-7.120120,-34.840012),
                new LatLng(-7.123470, -34.842252), new LatLng(-7.110312, -34.853392)};

        for(LatLng poslatlng : testeLL) {
            Empresa emp = new Empresa();
            emp.setEmpresaCode(i);
            emp.setTitle("Empresa "+i);
            emp.setLat(String.valueOf(poslatlng.latitude));
            emp.setLng(String.valueOf(poslatlng.longitude));
            MapsActivity.this.empresa = emp;
            i++;

            mMap.addMarker(new MarkerOptions().position(poslatlng).title("Empresa "+i))
                    .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_marker));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(poslatlng));
        }



        /*try {
//            AsyncGetEmpresas asyncGetOcurrences = new AsyncGetEmpresas(MapsActivity.this);
//            asyncGetOcurrences.execute();

            // Pegar empresas da nuvem
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED){
                Log.i("RESPONSE", "Não tem permissão necessária");
                return;
             }
//                List<Empresa> emps = getTodasEmpresas();
            List<Empresa> emps = new ArrayList<>();
            try {
                AsyncGetEmpresas asyncGetOcurrences = new AsyncGetEmpresas(MapsActivity.this);
                asyncGetOcurrences.execute();
                 emps = asyncGetOcurrences.getAllOccurrences();
                for (Empresa e: emps ) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(e.getLat()),Double.parseDouble(e.getLng()))).title(e.getTitle()));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            for (Empresa e: emps ) {
                LatLng pinLatLng = new LatLng(Double.parseDouble(e.getLat()),Double.parseDouble(e.getLng()));
                mMap.addMarker(new MarkerOptions()
                        .position(pinLatLng)
                        .title(e.getTitle()))
                        .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_marker));

                mMap.moveCamera(CameraUpdateFactory.newLatLng(pinLatLng));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/



    }

    @Override
    public void onMapLongClick(LatLng latLng) {

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
/*

    public class DoSomethingThread extends Thread {

        private static final String TAG = "DoSomethingThread";


        @Override
        public void run() {
            Log.v(TAG, "doing work in Random Number Thread");
            while (true) {
                try {
                    publishProgress(getTodasEmpresas());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Pega os resultados do método e vai
        private void publishProgress(List<Empresa> param) {
            Log.v(TAG, "reporting back from the consumer message Thread");
            final List<Empresa> resultado = param;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {

                        for (Empresa e: resultado) {
                            LatLng pinLatLng = new LatLng(Double.parseDouble(e.getLat()),Double.parseDouble(e.getLng()));
                            mMap.addMarker(new MarkerOptions()
                                    .position(pinLatLng)
                                    .title(e.getTitle()))
                                    .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_marker));

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(pinLatLng));
                        }

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        System.out.println(e.getStackTrace());
                    }
                }
            });
        }
    }
*/

    public List<Empresa> getTodasEmpresas() throws Exception {


        String result = "";
        String line = "";
        Gson gson = new Gson();


        String uri = "http://130.206.119.206:1026/v1/queryContext";
        String getAll = "{\"entities\": [{\"type\": \"Empresa\",\"isPattern\": \"true\",\"id\": \".*\"}]}";
        OkHttpClient client = new OkHttpClient();
        try
        {
            RequestBody body = RequestBody.create(JSON, getAll);
            Request request = new Request.Builder()
                    .url(uri)
                    .post(body)
                    .addHeader("Accept","application/json")
                    .build();

            int executeCount = 0;
            Response response;

            do
            {
                response = client.newCall(request).execute();
                if(response!=null) Log.i("RESPONSE", response.toString());
                executeCount++;
            }
            while(response.code() == 408 && executeCount < 5);

            result = response.body().string();

        }
        catch(IOException e)
        {
//            Log.e("RESPONSE", e.printStackTrace());
            e.printStackTrace();
        }

        List<Entity> contextElement = AdapterEntity.parseListEntity(result);
        List<Empresa> empresas = new ArrayList<Empresa>();
        for (Entity entity : contextElement) {
            empresas.add(AdapterEntity.toEmpresa(entity));
            Log.i("RESPONSE",AdapterEntity.toEmpresa(entity).toString());
        }

        // TODO Auto-generated method stub
        return empresas;
    }

}
