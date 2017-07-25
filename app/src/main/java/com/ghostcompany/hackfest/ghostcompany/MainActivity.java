package com.ghostcompany.hackfest.ghostcompany;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.ghostcompany.hackfest.ghostcompany.Async.AsyncGetEmpresasProximas;
import com.ghostcompany.hackfest.ghostcompany.models.AdapterEntity;
import com.ghostcompany.hackfest.ghostcompany.models.Attributes;
import com.ghostcompany.hackfest.ghostcompany.models.Empresa;
import com.ghostcompany.hackfest.ghostcompany.models.Entity;
import com.ghostcompany.hackfest.ghostcompany.models.OnGetEmpresaProximaCompletedCallback;
import com.google.android.gms.location.LocationListener;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnGetEmpresaProximaCompletedCallback, LocationListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    private SliderLayout sliderImages;
    private Button btFoto, btnYes, btnNo;
    private TextView tvTitulo, tvCnpj, tvEmpresaTitulo ;
    private static final int TIRAR_FOTO = 1001;
    private ImageView ivSlider;

    private ActionBarDrawerToggle mDrawerToggle;
    public boolean threadsAlive = false;
    private DrawerLayout mDrawerLayout;

    private String strLat = "";
    private String strLng = "";

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent it = getIntent();
        Empresa emp = new Empresa();//(Empresa) it.getSerializableExtra("obj");


        tvEmpresaTitulo = (TextView) findViewById(R.id.tvMainEmpresaTitulo);
        tvEmpresaTitulo.setText("Investigando...");
        //tvEmpresaTitulo.setVisibility(View.INVISIBLE);
        tvCnpj = (TextView) findViewById(R.id.tvMainEmpresaCnpj);
        tvCnpj.setText("Empresas próximas.");
 //       tvCnpj.setVisibility(View.INVISIBLE);

//        sliderImages = (SliderLayout) findViewById(R.id.slider);
        ivSlider = (ImageView) findViewById(R.id.ivSlider);

        btFoto = (Button) findViewById(R.id.btMainEmpresaTirarFoto);
        btFoto.setOnClickListener(new tirarFotoIntent());
        btFoto.setVisibility(View.INVISIBLE);

        btnYes = (Button) findViewById(R.id.button);
        btnYes.setVisibility(View.INVISIBLE);
        btnNo = (Button) findViewById(R.id.button2);
        btnNo.setVisibility(View.INVISIBLE);

//        configurarSliderImages();d

//        setLayouts(emp);

        //AsyncGetEmpresas exec = new AsyncGetEmpresas(MainActivity.this);
        //exec.execute();



    }

    private void configurarSliderImages() {

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();

//        file_maps.put("Camera",R.drawable.add_empresa_foto);


        for(String name : file_maps.keySet()){
            DefaultSliderView imageSlider = new DefaultSliderView(this);
            // initialize a SliderLayout

            imageSlider.image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            sliderImages.addSlider(imageSlider);
        }

        sliderImages.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderImages.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderImages.setDuration(4000);
//        sliderImages.addOnPageChangeListener(this);

    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
       // sliderImages.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    public void setLayouts(Empresa e) {
        //tvTitulo.setText("Empresaa");
        tvEmpresaTitulo.setText("Empresaa");
        tvCnpj.setText("12234567");

    }

    @Override
    public void onGetEmpresaCompleted(String result) throws Exception {
           // get string with company close 30m


        if (!result.contains("No context element found") && !result.equals("")) {
            String distance = getDistance(result);

            //Ocorrencia occ = getTipoOcorrencia(retorno);
            //String title = occ.getTitle();
            /*
            if (distance != null && Double.valueOf(distance) <= 30) {

                setOccurenceCard(occ, distance);

                if (doVoiceAlert) {
                    atual = System.nanoTime();
                    if ((Double.parseDouble(distance) <= 30.0)) {
                        if (first) {
                            anterior = atual;
                            if(threadsAlive) {
                                notificacaoVoz(title, distance);
                            }
                            first = false;
                        } else if (atual - anterior > 30000000000.0f) {
                            if(threadsAlive) {
                                notificacaoVoz(title, distance);
                            }
                            anterior = atual;
                        }
                    }
                }

            } */

            Log.v("DIST", distance);
        } else {
            /*
            txtMensagem.setText("Nenhum alerta");
            TextView textAlertDetailsView = (TextView) findViewById(R.id.alert_details);
            textAlertDetailsView.setText("");
            ImageView iconWeather = (ImageView) findViewById(R.id.alert_img);
            iconWeather.setImageResource(R.drawable.no_alert);
            setarCorDeFundo(R.color.branco);
            setOccurenceCardTextColor(R.color.branco);
            */
        }

    }

    public String getDistance(String result) throws Exception {
        List<Entity> listEntity = AdapterEntity.parseListEntity(result);
        double minDistance = 0;
        double distance = 0;
        boolean isFirst = true;
        for (Entity entity : listEntity) {
            for (Attributes att : entity.getAttributes()) {
                if (att.getName().equalsIgnoreCase("GPSCoord")) {
                    String[] tokensVal = att.getValue().split(",");
                    distance = Util.distance(tokensVal[0].trim(), tokensVal[1].trim(), strLat, strLng, 'M');
                    if (isFirst) {
                        minDistance = distance;
                        isFirst = false;
                    } else {
                        if (minDistance < distance) {
                            minDistance = distance;
                        }
                    }
                }
            }
        }
        return String.valueOf(minDistance);
    }

    @Override
    public void onLocationChanged(Location location) {

        setStrLat(String.valueOf(location.getLatitude()));
        setStrLng(String.valueOf(location.getLongitude()));
        String[] myLatLngTaskParams = {strLat,strLng};
        try {
            AsyncGetEmpresasProximas asyncGetEmpresasProximas = new AsyncGetEmpresasProximas(MainActivity.this);
            asyncGetEmpresasProximas.execute(myLatLngTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class tirarFotoIntent implements View.OnClickListener {
        private File output = null;

        @Override
        public void onClick(View v) {
            Intent itTirarFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//            Nesse IF ele verifica se há algum programa que abra aquela intent.
            if (itTirarFoto.resolveActivity(getPackageManager()) != null){

                File dir = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

                output = new File(dir, "imagem_demo.jpeg");
                itTirarFoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));

                startActivityForResult(itTirarFoto, TIRAR_FOTO);

            }

        }



        /*
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            TextSliderView demoSlider = new TextSliderView(this);
            demoSlider.description("Game of Thrones")
                    .image("http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg")
                    .setOnClickListener(this);
            slider.addSlider(demoSlider);


//        Vai checar se a origem é de "TIRAR_FOTO" e se voltou com sucesso.
            if (requestCode == TIRAR_FOTO && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imgBitmap = (Bitmap) extras.get("data");

                MainActivity.this.sliderImages.addSlider(imgBitmap);
                this.ivThumbnail.setVisibility(View.VISIBLE);
            }
        }
        */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.map_action_settings:
                getMapActivity();
                return true;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getMapActivity(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void setStrLat(String latitudeString) {
        this.strLat = latitudeString;
    }

    public void setStrLng(String longitudeString) {
        this.strLng = longitudeString;
    }

}
