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
import com.google.firebase.database.FirebaseDatabase;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddActivity extends AppCompatActivity{
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
        title = findViewById(R.id.add_title);
        author = findViewById(R.id.add_author);
        desc = findViewById(R.id.add_desc);
        btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
            }
        });
    }

    private void addData() {
        if (!validateForm()) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("title", title.getText().toString());
        map.put("author",mAuth.getUid());
        map.put("description", desc.getText().toString());
        map.put("date", getCurrentDate());
        FirebaseDatabase.getInstance().getReference().child("news").push()
                .setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        title.setText("");
                        desc.setText("");
                        Toast.makeText(getApplicationContext(), "Inserted Successfully", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Could not insert", Toast.LENGTH_LONG).show();
                    }
                });
    }

    //helper
    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(title.getText().toString())) {
            title.setError("Required");
            result = false;
        } else {
            title.setError(null);
        }
        if (TextUtils.isEmpty(desc.getText().toString())) {
            desc.setError("Required");
            result = false;
        } else {
            desc.setError(null);
        }
        return result;
    }
}

//    public void uploadImage(View view) {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_IMAGE_REQUEST);
//    }
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