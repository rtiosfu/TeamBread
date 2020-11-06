package com.example.loginscreen.roomcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loginscreen.roomcode.enterRoomCode;
import com.example.loginscreen.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class examEntry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_entry);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

//    public void dispatchTakePictureIntent(View view){
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                       try {
//                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                }catch(ActivityNotFoundException e){
////                    test.setText("Activity not found exception.");
//                }
//
//    }

    public void dispatchTakePictureIntent(View view) {
        TextView test = (TextView)findViewById(R.id.preExamScanMessage);
        test.setText("This function has ran.");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }catch(ActivityNotFoundException e){
                    test.setText("Activity not found exception.");
                }
//                setImage();
            }

    }

    public void setImage(View view){
        File cardScanFile = new File(currentPhotoPath);
        if(cardScanFile.exists()) {
            Bitmap cardScanbmp = BitmapFactory.decodeFile(cardScanFile.getAbsolutePath());
            ImageView cardScan = (ImageView) findViewById(R.id.cameraImagePreview);

            cardScan.setImageBitmap(cardScanbmp);
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

}