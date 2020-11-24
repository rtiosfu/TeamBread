////////////////////////////////////////////
//TEAM BREAD
//examEntry.java
//PROGRAMMERS:Ryan
//KNOWN BUGS: Picture requires the submit button to be clicked before it is displayed.
//V3 CHANGES: None yet.
////////////////////////////////////////////

package com.example.loginscreen.roomcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginscreen.roomcode.enterRoomCode;
import com.example.loginscreen.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//Dummy pre-exam entry page. Will lead to an actual exam page and will involve photo checking in version 2.
public class examEntry extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_entry);

    }

    //Code to signal to mediastore that we wish to take an image.
    static final int REQUEST_IMAGE_CAPTURE = 1;



    //sends the signal to take the photo with default camera app
    public void dispatchTakePictureIntent(View view) {
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
                    Toast.makeText(getApplicationContext(), "Error opening camera. Please try again.", Toast.LENGTH_SHORT).show();
                }
//                setImage();
            }

    }

    public void onSubmitClick(View view){
        Intent intent = new Intent(this, com.example.loginscreen.roomcode.examRoomDisplay.class);
        startActivity(intent);
    }


    //Displays the last photo that the camera has taken on the screen.
    public void setImage(View view){
        File cardScanFile = new File(currentPhotoPath);
        if(cardScanFile.exists()) {
            Bitmap cardScanbmp = BitmapFactory.decodeFile(cardScanFile.getAbsolutePath());
            ImageView cardScan = (ImageView) findViewById(R.id.cameraImagePreview);

            cardScan.setImageBitmap(cardScanbmp);
        }
    }

    String currentPhotoPath;

    //helper to the photo function that saves the file to the picture directory.
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