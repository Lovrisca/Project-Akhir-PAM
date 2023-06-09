package com.example.project_akhir_pam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AddActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnLogout;
    private EditText title, author, desc;
    private
    Button btnSave;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mAuth = FirebaseAuth.getInstance();
        btnLogout = findViewById(R.id.btn_logout);
        //databaseReference = DatabaseReference.getInstance().getReference();
        title = findViewById(R.id.add_title);
        author = findViewById(R.id.add_author);
        desc = findViewById(R.id.add_desc);
        btnLogout.setOnClickListener(this);
        btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
    }
    public void logOut(){
        mAuth.signOut();
        Intent intent = new Intent(AddActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//makesure user cant go back
        startActivity(intent);
    }

    public void uploadImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_IMAGE_REQUEST);
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(title.getText().toString())) {
            title.setError("Required");
            result = false;
        } else {
            title.setError(null);
        }
        if (TextUtils.isEmpty(author.getText().toString())) {
            author.setError("Required");
            result = false;
        } else {
            author.setError(null);
        }
        if(TextUtils.isEmpty(desc.getText().toString())) {
            desc.setError("Required");
            result = false;
        } else {
            desc.setError(null);
        }
        return result;
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            imageUri = data.getData();
//            uploadToFirebase();
//        }
//    }
//    private void uploadToFirebase() {
//        if (imageUri != null && !validateForm() ) {
//            //DatabaseReference fileRef = storageReference.child("images/" + UUID.randomUUID().toString());
//            //fileRef.putFile(imageUri)
//                    //.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        //public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(AddActivity.this, "Unggah berhasil", Toast.LENGTH_SHORT).show();
//                            // Dapatkan URL unduhan gambar dari taskSnapshot
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(AddActivity.this, "Unggah gagal", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }
//    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
//                uploadToFirebase();
            case R.id.btn_logout:
                logOut();
                break;
        }
    }
}