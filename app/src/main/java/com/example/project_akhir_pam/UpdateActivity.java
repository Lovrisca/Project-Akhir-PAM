package com.example.project_akhir_pam;

import static com.example.project_akhir_pam.DashboardAdapter.getCurrentDate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity {
    private EditText title, description;
    private ImageView image;
    private Button btnSave;
    private FirebaseAuth mAuth;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    Uri imageUri;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        mAuth = FirebaseAuth.getInstance();
        title = findViewById(R.id.et_title);
        description = findViewById(R.id.et_desc);
        btnSave = findViewById(R.id.btn_save);
        image = findViewById(R.id.upload_image);

        progressDialog = new ProgressDialog(UpdateActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("news");

        //get selected news id from adapter
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        DatabaseReference newsRef = databaseReference.child(id);
        newsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.e("data di intent read", id);
                    String titleValue = dataSnapshot.child("title").getValue(String.class);
                    String descValue = dataSnapshot.child("description").getValue(String.class);
                    String imageValue = dataSnapshot.child("imageName").getValue(String.class);

                    //put image in update
                    storageReference = FirebaseStorage.getInstance().getReference("images/"+imageValue);
                    try {
                        File local = File.createTempFile("tempfile", ".jpg");
                        storageReference.getFile(local).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(local.getAbsolutePath());
                                image.setImageBitmap(bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Log.e("status download image","gagal");
                            }
                        });
                    }catch (IOException e){
                    }
                    title.setText(titleValue);
                    description.setText(descValue);
                } else {
                }
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UpdateActivity.this, "databse error", Toast.LENGTH_SHORT).show();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateFormUpdate()) {
                    return;
                }
                progressDialog = new ProgressDialog(UpdateActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                Map<String, Object> map = new HashMap<>();
                map.put("title", title.getText().toString());
                map.put("description", description.getText().toString());
                map.put("date",getCurrentDate()+" - edited");
                databaseReference.child(id).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                uploadImage(id);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UpdateActivity.this, "Update fails", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    //helper
    private boolean validateFormUpdate() {
        boolean result = true;
        if (TextUtils.isEmpty(title.getText().toString())) {
            title.setError("Required");
            result = false;
        } else {
            title.setError(null);
        }
        if (TextUtils.isEmpty(description.getText().toString())) {
            description.setError("Required");
            result = false;
        } else {
            description.setError(null);
        }
        return result;
    }

    public void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null) {
            imageUri = data.getData();
            image.setImageURI(imageUri);
        }
    }

    private void uploadImage(String newsId) {
        //memberi nama file
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String filename = formatter.format(now);

        storageReference = FirebaseStorage.getInstance().getReference("images/" + filename);
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
                            Map<String, Object> p = new HashMap<>();
                            p.put("image",imgUri);
                            p.put("imageName",filename);
                            databaseReference.child(newsId).updateChildren(p);
                            Toast.makeText(getApplicationContext(), "Update success!", Toast.LENGTH_SHORT).show();
                            Log.e("status upload:", "berhasil");
                            progressDialog.dismiss();
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
    public void onBackPressed() {
        finish();
    }
}