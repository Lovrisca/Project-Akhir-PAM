package com.example.project_akhir_pam;

import static com.example.project_akhir_pam.DashboardAdapter.getCurrentDate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddActivity extends AppCompatActivity {
    EditText title, desc;
    ImageView image;
    Button btnSave;
    FirebaseAuth mAuth;
    Uri imageUri;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("news");
        mAuth = FirebaseAuth.getInstance();
        title = findViewById(R.id.et_title);
        desc = findViewById(R.id.et_desc);
        btnSave = findViewById(R.id.btn_save);
        image = findViewById(R.id.upload_image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
                progressDialog.setTitle("Uploading");
                progressDialog.setTitle("Please wait...");
                progressDialog.show();
            }
        });
        progressDialog = new ProgressDialog(this);
    }

    private void addData() {
        if (!validateFormAdd()) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        DatabaseReference uniqueNewsId = databaseReference.push();
        String newsId = uniqueNewsId.getKey();
        map.put("image", "");
        map.put("title", title.getText().toString());
        map.put("author", mAuth.getUid());
        map.put("description", desc.getText().toString());
        map.put("date", getCurrentDate());

        uniqueNewsId.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                uploadImage(newsId);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Could not insert", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
        finish();
    }

    //helper
    private boolean validateFormAdd() {
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

    public void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null) {
            imageUri = data.getData();
            image.setImageURI(imageUri);
        }
    }
    private void uploadImage(String newsId) {
        //memberi nama file
        storageReference = FirebaseStorage.getInstance().getReference("images/" + newsId);
        Log.e("Image path", String.valueOf(imageUri));
        if (imageUri != null) {
            storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //to get donwload uri
                    Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String imgUri = task.getResult().toString();
                                Log.e("image Uri", imgUri);
                            progressDialog.dismiss();
                            Map<String, Object> p = new HashMap<>();
                            p.put("image",imgUri);
                            databaseReference.child(newsId).updateChildren(p);
                            Toast.makeText(getApplicationContext(), "Upload success!", Toast.LENGTH_LONG).show();
                                Log.e("status upload:", "berhasil");
                            finish();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("status upload:", "gagal");
                }
            });
        }else {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Uploaded!", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}