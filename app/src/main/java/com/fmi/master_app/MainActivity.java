package com.fmi.master_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Initialize variable
    DrawerLayout drawerLayout;
    ImageView btMenu;
    RecyclerView recyclerView;
    static ArrayList<String> arrayList = new ArrayList<>();
    MainAdapter adapter;

    //Camera variables
    ImageView imageView;
    Button btnOpen;

    private String currentPhotoPath;

    public static void closeDrawer(DrawerLayout drawerLayout) {
        //Check condition
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            //When drawer is open, close it
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Camera variables
        imageView = findViewById(R.id.image_view);
        btnOpen = findViewById(R.id.bt_open);


        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open camera
                String fileName = "photo";
                File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                try {
                    File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);

                    currentPhotoPath = imageFile.getAbsolutePath();

                    Uri imageUri = FileProvider.getUriForFile(MainActivity.this,"com.fmi.master_app", imageFile);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
        });
        

        //Assign variable
        drawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.bt_menu);
        recyclerView = findViewById(R.id.recycler_view);

        //Clear array list
        arrayList.clear();

        //Add menu item in array list
        arrayList.add("Camera");
        arrayList.add("Notes");
        arrayList.add("Maps");
        arrayList.add("Logout");

        //Initialize adapter
        adapter = new MainAdapter(this,arrayList);

        //Set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Set adapter
        recyclerView.setAdapter(adapter);

        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open drawer
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Close drawer
        closeDrawer(drawerLayout);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
           Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);

           ImageView imageView = findViewById(R.id.image_view);
           imageView.setImageBitmap(bitmap);
        }
    }

    //Share button
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.share){
            ApplicationInfo api = getApplicationContext().getApplicationInfo();
            String apkpath = api.sourceDir;
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/vnd.android.package-archive");
            intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(new File(apkpath)));
            startActivity(Intent.createChooser(intent,"ShareVia"));
        }
        return true;
    }
}