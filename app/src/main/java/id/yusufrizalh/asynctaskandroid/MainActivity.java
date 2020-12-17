package id.yusufrizalh.asynctaskandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    // 1: global variable
    Button btn_fetch_data;
    ImageView img_avatar = null;
    ProgressDialog progressDialog;  // loading
    Bitmap bmImg = null;
    URL imageUrl = null;
    InputStream inputStream = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 2: mengenali semua komponen
        img_avatar = findViewById(R.id.img_avatar);
        btn_fetch_data = findViewById(R.id.btn_fetch_data);

        // 3: event handling
        btn_fetch_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute("https://i.ibb.co/PxkfQPX/github-avatar.png");
                // myAsyncTask.execute("https://pngimg.com/uploads/computer_pc/computer_pc_PNG7719.png");
            }
        });
    }

    public class MyAsyncTask extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Harap tunggu...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                imageUrl = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                connection.setDoInput(true);
                connection.connect();
                inputStream = connection.getInputStream();  // membaca response dari server

                // menangani gambar dari server
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                bmImg = BitmapFactory.decodeStream(inputStream, null, options);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return bmImg;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (img_avatar != null) {    // avatar berhasil didapatkan
                progressDialog.dismiss();
                img_avatar.setImageBitmap(bitmap);
            } else {    // avatar belum berhasil didapatkan
                progressDialog.show();
            }
        }
    }
}