package com.example.a2018obarnett.owen_final;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a2018obarnett.owen_final.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {


    static final int REQUEST_IMAGE_CAPTURE = 1;
    public String sideText= "";
    private Button top;
    private Button done;
    private Button side;
    private boolean isTop = false;
    private Boolean isSide = false;
    //Sets up camera
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    //Called once camera takes picture
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //Checks if right image and get the bitmap
        //Sends the bitmap to store
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            storeImage(imageBitmap, sideText);
        }
    }
    //Changes the bitmap into a string and opens a file based on the name passed to write the string to
    private void storeImage(Bitmap test, String name) {
        try {
            String FILENAME = name;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            test.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String string = Base64.encodeToString(b, Base64.DEFAULT);
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(string.getBytes());
            fos.close();
        }
        catch(Exception e)
        {
        }
        //Displays the image on the screen as the icon
        if(sideText=="top")
        {
            Bitmap display = readImage("top");
            ImageView mImageView = (ImageView) findViewById(R.id.top_view);
            mImageView.setImageBitmap(display);
        }
        else
        {
            Bitmap display = readImage("side");
            ImageView mImageView = (ImageView) findViewById(R.id.side_view);
            mImageView.setImageBitmap(display);
        }
    }


    //Takes a file name, reads the file and decodes the string into a bitmap, which it passes back
    private Bitmap readImage(String name) {
        try {
            FileInputStream fos = openFileInput(name);
            StringBuffer fileContent = new StringBuffer("");
            byte[] buffer = new byte[1024];
            int n;
            while ((n = fos.read(buffer)) != -1)
            {
                fileContent.append(new String(buffer, 0, n));
            }
            String encodedString = fileContent.toString();
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;

        } catch (Exception e) {
            return null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        done=(Button)findViewById(R.id.done);
        setContentView(R.layout.activity_main);
        try {
            FileInputStream fos = openFileInput("size");
            BufferedReader br = new BufferedReader(new InputStreamReader(fos));
            String yourText = br.readLine();
            br.close();
            if(yourText != null) {
                EditText temp = (EditText) findViewById(R.id.size);
                temp.setText(yourText);
            }
        }
        catch(Exception e){

        }
        top=(Button)findViewById(R.id.top_button);
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sideText = "top";
                dispatchTakePictureIntent();
                isTop = true;
                if(isSide == true)
                {
                   done.setEnabled(true);
                }
            }
        });

        side=(Button)findViewById(R.id.side_button);
        side.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sideText = "side";
                dispatchTakePictureIntent();
                isSide = true;
                if(isTop == true)
                {
                    done.setEnabled(true);
                }
            }
        });
        done=(Button)findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    EditText temp = (EditText)findViewById(R.id.size);
                    FileOutputStream fos = openFileOutput("size", Context.MODE_PRIVATE);
                    fos.write(temp.getText().toString().getBytes());
                    String test = new String(temp.getText().toString().getBytes());
                    temp.setText(test);
                    fos.close();
                }
                catch (Exception e){
                }

            }
        });



    }
}
