package com.example.weatherapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.R;
import com.example.weatherapp.api.ApiRequest;
import com.example.weatherapp.api.RetrofitInit;
import com.example.weatherapp.model.Example;
import com.example.weatherapp.model.Main;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    EditText edtSearch;
    TextView txtWeather,txtCity,txtHumid,txtWind,txtDescribe;
    ImageButton btnChoose;
    String api ="3649d1353b5115a7d34c9539377a7daa";
    String icon ="";
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Initialize();

        // Set Detail
        detail();

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityname = edtSearch.getText().toString().trim();
                if(cityname.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter a valid city", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Call api use retrofit
                Call<Example> call = RetrofitInit.getInstance().getWeather(cityname,api);
                call.enqueue(new Callback<Example>() {
                    @Override
                    public void onResponse(Call<Example> call, Response<Example> response) {
                        edtSearch.getText().clear();
                        if(response.code() == 404){
                            Toast.makeText(MainActivity.this, "Please enter a valid city", Toast.LENGTH_SHORT).show();
                        }else if (!(response.isSuccessful())){
                            Toast.makeText(MainActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                        }
                        Example example = response.body();
                        assert example != null;
                        Main main = example.getMain();
                        Double temp = main.getTemp();
                        Integer temperature =(int)(temp-273.15);
                        icon = example.getWeather().get(0).getIcon();
                        String img = "http://openweathermap.org/img/wn/"+icon+"@2x.png";
                        txtWeather.setText(String.format("%s%s", String.valueOf(temperature), (char) 0x00B0));
                        Picasso.with(getApplicationContext())
                                .load(img)
                                .placeholder(R.mipmap.ic_launcher)
                                .into(imageView);
                        txtCity.setText(example.getName());
                        txtHumid.setText(String.format("Humidity: %s %%", String.valueOf(main.getHumidity())));
                        txtWind.setText(String.format("Wind: %s km/h", example.getWind().getSpeed()));
                        String myString = String.valueOf(example.getWeather().get(0).getDescription());
                        String upperString = myString.substring(0, 1).toUpperCase() + myString.substring(1).toLowerCase();
                        txtDescribe.setText(upperString);
                        if(icon.contains("d")){
                            linearLayout.setBackgroundResource(R.drawable.day);
                        }else if(icon.contains("n")){
                            linearLayout.setBackgroundResource(R.drawable.night);
                        }
                    }

                    @Override
                    public void onFailure(Call<Example> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void detail() {
       linearLayout.setBackgroundResource(R.drawable.day);
    }

    private void Initialize() {
        edtSearch = findViewById(R.id.edtSearch);
        txtWeather = findViewById(R.id.txtWeather);
        imageView = findViewById(R.id.imgWeather);
        btnChoose = findViewById(R.id.btnChoose);
        txtCity = findViewById(R.id.txtCity);
        txtHumid = findViewById(R.id.txtHumid);
        txtWind = findViewById(R.id.txtWind);
        txtDescribe = findViewById(R.id.txtDescribe);
        linearLayout = findViewById(R.id.linearLayout);
    }
}