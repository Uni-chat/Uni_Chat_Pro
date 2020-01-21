package com.leonard.unichat.Messages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.leonard.unichat.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    private Button updateSettingsButton;
    private TextView txtNameOfUsers;
    private EditText edtStatusOfUsers;
    private CircularImageView imgSetProfileImage;
    private String currentUserID, currentUserName, userAsStudent, userAsAdmin;
    private static String userAsType;
    private Uri imageUri, resultUri;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseRefTeacher, databaseRefStudent, databaseRefAdmin;
    private StorageReference userProfileImageRef;
    private static final int IMG_REQUEST_CODE = 1;
    private String users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        retriveUserInfo();

        updateSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateSettings();
            }
        });

        imgSetProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // showFileChooser();

                fileGetter();

            }
        });
    }

  /*  if(extras!=null){
        petrolPriceURL =extras.getString("URLString");

    }*/

    //Unused Method bcoz of Crop Image Library
    /*private void showFileChooser() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMG_REQUEST_CODE);

    }*/


    private void initViews () {

        userAsType = getIntent().getExtras().get("TYPE_USER").toString();

        updateSettingsButton = (Button) findViewById(R.id.updateSettingsButton);
        txtNameOfUsers = (TextView) findViewById(R.id.txtNameOfUsers);
        edtStatusOfUsers = (EditText) findViewById(R.id.edtStatusOfUsers);
        imgSetProfileImage = (CircularImageView) findViewById(R.id.imgSetProfileImage);



        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();
        databaseRefTeacher = FirebaseDatabase.getInstance().getReference().child("Users/Teachers");
        databaseRefStudent = FirebaseDatabase.getInstance().getReference().child("Users/Student");
        databaseRefAdmin = FirebaseDatabase.getInstance().getReference().child("Users/Admin");

        userProfileImageRef = FirebaseStorage.getInstance().getReference().child("Proflie Images");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                resultUri = result.getUri();

                try {

                    // Show selected Image on IMAGE view
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                    imgSetProfileImage.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                final StorageReference filePath = userProfileImageRef.child(currentUserID + ".jpg");

                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                imageUri = uri;

                                final String downloadImageUrl = uri.toString();

                                if (userAsType.equals("Admin")) {

                                    photoUriRef(databaseRefAdmin, downloadImageUrl);

                                }

                                else if (userAsType.equals("Teacher")) {

                                    photoUriRef(databaseRefTeacher, downloadImageUrl);

                                }

                                else if (userAsType.equals("Student")) {

                                    photoUriRef(databaseRefStudent, downloadImageUrl);

                                }
                            }
                        });
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                String err = error.toString();
                Toast.makeText(SettingsActivity.this, err , Toast.LENGTH_SHORT).show();

            }
        }
    }


     /* if (requestCode == IMG_REQUEST_CODE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            imageUri = data.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imgSetProfileImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/




    // Library Crop use to get an image
    private void fileGetter() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);

    }


    private void updateSettings () {

        String setUserName = txtNameOfUsers.getText().toString();
        String setUserMode = edtStatusOfUsers.getText().toString();



        if (TextUtils.isEmpty(setUserMode)) {

            Toast.makeText(this, "Dont have Status", Toast.LENGTH_SHORT).show();

        } else {

           /* HashMap<String, String> prifileMap = new HashMap<>();
            //prifileMap.put("UID", currentUserID);
            //prifileMap.put("NANE ", setUserName);
            prifileMap.put("STATUS ", setUserMode);*/

            if (users.equals("Admin")) {

                updateStatusRef(databaseRefAdmin, setUserMode);

            }

            else if (users.equals("Teacher")) {

                updateStatusRef(databaseRefTeacher, setUserMode);

            }

            else if (users.equals("Student")) {

                updateStatusRef(databaseRefStudent, setUserMode);

            }

        }

    }


    private void retriveUserInfo() {

        if (userAsType.equals("Admin")) {

            users = "Admin";

            retiveDBInfo(databaseRefAdmin);

        }

        else if (userAsType.equals("Teacher")) {

            users = "Teacher";

            retiveDBInfo(databaseRefTeacher);

        }

        else if (userAsType.equals("Student")) {

            users = "Student";

            retiveDBInfo(databaseRefStudent);

        }
    }

    private void retiveDBInfo (DatabaseReference userRefDB) {


        userRefDB.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if ((dataSnapshot.exists())  &&
                        (dataSnapshot.hasChild("NAME") && (dataSnapshot.hasChild("STATUS"))
                                && (dataSnapshot.hasChild("IMG_URL")))) {

                    String retriveUserName = dataSnapshot.child("NAME").getValue().toString();
                    String retriveUserStatus = dataSnapshot.child("STATUS").getValue().toString();
                    String retriveUserImageUrl = dataSnapshot.child("IMG_URL").getValue().toString();

                    txtNameOfUsers.setText(retriveUserName);
                    edtStatusOfUsers.setText(retriveUserStatus);

                    Picasso.get().load(retriveUserImageUrl).into(imgSetProfileImage);

                } else if ((dataSnapshot.exists())  &&
                        (dataSnapshot.hasChild("NAME") && (dataSnapshot.hasChild("STATUS")))) {

                    String retriveUserName = dataSnapshot.child("NAME").getValue().toString();
                    String retriveUserStatus = dataSnapshot.child("STATUS").getValue().toString();

                    txtNameOfUsers.setText(retriveUserName);
                    edtStatusOfUsers.setText(retriveUserStatus);

                } else if ((dataSnapshot.exists())  &&
                        (dataSnapshot.hasChild("NAME") && (dataSnapshot.hasChild("IMG_URL")))) {

                    String retriveUserName = dataSnapshot.child("NAME").getValue().toString();
                    String retriveUserImageUrl = dataSnapshot.child("IMG_URL").getValue().toString();

                    txtNameOfUsers.setText(retriveUserName);
                    edtStatusOfUsers.setText("");
                    Picasso.get().load(retriveUserImageUrl).into(imgSetProfileImage);

                } else if ((dataSnapshot.exists())  && (dataSnapshot.hasChild("NAME"))) {

                    String retriveUserName = dataSnapshot.child("NAME").getValue().toString();
                    //String retriveUserStatus = dataSnapshot.child("STATUS ").getValue().toString();

                    txtNameOfUsers.setText(retriveUserName);
                    edtStatusOfUsers.setText("");

                } else {

                   /* String retriveUserName = dataSnapshot.child("NANE ").getValue().toString();

                    txtNameOfUsers.setText(retriveUserName);
                    edtStatusOfUsers.setText("");*/
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateStatusRef (DatabaseReference statusRefDB, String statusSet) {

        statusRefDB.child(currentUserID).child("STATUS").setValue(statusSet)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(SettingsActivity.this, " Files Updated ", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            String message = task.getException().toString();
                            Toast.makeText(SettingsActivity.this, message , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void photoUriRef (DatabaseReference photoUriDB, String strImgUrl) {

        photoUriDB.child(currentUserID).child("IMG_URL").setValue(strImgUrl)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(SettingsActivity.this, " Image saved in DB ", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            String message = task.getException().toString();
                            Toast.makeText(SettingsActivity.this, message , Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }





}
