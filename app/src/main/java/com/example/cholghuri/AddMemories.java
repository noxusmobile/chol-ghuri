package com.example.cholghuri;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.IDN;
import java.net.URI;

public class AddMemories extends AppCompatActivity {

    private ImageView memoriesPic;
    private EditText memoriesCaption;
    private Button memoriesSave,memoriesTakePicture;
    private ProgressBar memoriesProgressBar;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private Uri imageUri;
    private DatabaseReference reference;
    private String tourID,userID,Id;
    private StorageTask uploadTask;


    private StorageReference mStore,storageReference;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memories);


        mStore = FirebaseStorage.getInstance().getReference();
        getDataFromIntent();
        intialize();

        onclick();
    }

    private void getDataFromIntent() {


            Intent intent = getIntent();
            tourID = intent.getStringExtra("tourID");

        }

    private void onclick() {

        memoriesPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        memoriesSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(AddMemories.this, "Upload In Progress", Toast.LENGTH_SHORT).show();
                } else {
                    UploadImageWithTitle();
                }
            }
        });

       /* memoriesTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent,CAMERA_REQUEST_CODE);
            }
        });*/


    }

    private void intialize() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();


        storageReference = FirebaseStorage.getInstance().getReference("Memories/"+userID);
        reference = FirebaseDatabase.getInstance().getReference().child("UserLIst").child(userID).child("TourList").child(tourID).child("MemoryList");


        //memoriesTakePicture=findViewById(R.id.memoriesTakePicture);
        memoriesPic = findViewById(R.id.memoriesPic);
        memoriesCaption=findViewById(R.id.memoriesCaption);
        memoriesSave=findViewById(R.id.memoriesSave);
        memoriesProgressBar = findViewById(R.id.proessbar);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
           Picasso.with(this).load(imageUri).placeholder(R.mipmap.ic_launcher_round).into(memoriesPic);
        }
    }

    /*
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){

            Uri uri = data.getData();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child("UserLIst").child(userID).child("TourList").child(tourID).child("Memories");
            String Id = databaseReference.push().getKey();
            StorageReference filepath = mStore.child("Memories").child(uri.getLastPathSegment()).child(Id);

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(AddMemories.this, "uploaded", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
*/

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void UploadImageWithTitle() {
        final String Title = memoriesCaption.getText().toString();

        if(Title.equals("")){
            Toast.makeText(this, "Enter Title Field", Toast.LENGTH_SHORT).show();
        }else if(imageUri !=null){

            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    memoriesProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(AddMemories.this, "Upload successfull", Toast.LENGTH_SHORT).show();

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    Upload upload = new Upload(downloadUrl.toString(), Title);
                                    String uploadId = reference.push().getKey();
                                    upload.setUploadID(uploadId);
                                    reference.child(uploadId).setValue(upload);

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(AddMemories.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            memoriesProgressBar.setProgress((int) progress);

                        }
                    });

        }else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

}



