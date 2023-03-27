package com.example.aston3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        EditText editText = (EditText) findViewById(R.id.editText);
        Button btnGlide = (Button) findViewById(R.id.btnGlide);
        Button btnNoGlide = (Button) findViewById(R.id.btnNoGlide);


        Toast toast = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG);

        btnGlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = editText.getText().toString();
                Glide.with(imageView)
                        .load(url)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e,
                                                        Object model,
                                                        Target<Drawable> target,
                                                        boolean isFirstResource) {
                                toast.show();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource,
                                                           Object model,
                                                           Target<Drawable> target,
                                                           DataSource dataSource,
                                                           boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(imageView);
            }
        });
        btnNoGlide.setOnClickListener(new View.OnClickListener() {
            final Bitmap[] mBitmap = {null};

            @Override
            public void onClick(View view) {
                String url = editText.getText().toString();
                Runnable runnable = () -> mBitmap[0] = getBitmap(url);
                Thread thread = new Thread(runnable);
                thread.start();
                Runnable runnable1 = () -> imageView.setImageBitmap(mBitmap[0]);
                new Handler().postDelayed(runnable1, 1000);
            }
        });

    }
    private Bitmap getBitmap(String urlString) {
        Bitmap bitmap;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            return bitmap;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}