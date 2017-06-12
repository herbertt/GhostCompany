package com.ghostcompany.hackfest.ghostcompany;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.ghostcompany.hackfest.ghostcompany.models.Empresa;

import java.io.File;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    private SliderLayout sliderImages;
    private Button btFoto;
    private TextView tvTitulo, tvCnpj, tvEmpresaTitulo ;
    private static final int TIRAR_FOTO = 1001;
    private ImageView ivSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent it = getIntent();
        Empresa emp = (Empresa) it.getSerializableExtra("obj");

        tvTitulo = (TextView) findViewById(R.id.tvMainEmpresaTitulo);
        tvCnpj = (TextView) findViewById(R.id.tvMainEmpresaCnpj);

//        sliderImages = (SliderLayout) findViewById(R.id.slider);
        ivSlider = (ImageView) findViewById(R.id.ivSlider);

        btFoto = (Button) findViewById(R.id.btMainEmpresaTirarFoto);
        btFoto.setOnClickListener(new tirarFotoIntent());

//        configurarSliderImages();

        setLayouts(emp);

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
        sliderImages.stopAutoCycle();
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
        tvTitulo.setText(e.getTitle());
        tvEmpresaTitulo.setText(e.getTitle());
        tvCnpj.setText(e.getEmpresaCode());
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
}
