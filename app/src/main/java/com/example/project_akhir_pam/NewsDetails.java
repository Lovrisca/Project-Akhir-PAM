package com.example.project_akhir_pam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class NewsDetails extends AppCompatActivity {
    TextView title, date, author, description;
    ImageView image;
    DatabaseReference databaseReference, databaseReferenceId;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        image = findViewById(R.id.tv_picture);
        title = findViewById(R.id.tv_title);
        author = findViewById(R.id.tv_author);
        date = findViewById(R.id.tv_date);
        description = findViewById(R.id.tv_details);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("news");
        databaseReferenceId = FirebaseDatabase.getInstance().getReference().child("users");

        //get selected news id from adapter
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Log.e("id gambar:", id);

        progressDialog = new ProgressDialog(NewsDetails.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DatabaseReference newsRef = databaseReference.child(id);
        newsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.e("id news", id);
                    String nameValue = dataSnapshot.child("authorName").getValue(String.class);
                    String titleValue = dataSnapshot.child("title").getValue(String.class);
                    String dateValue = dataSnapshot.child("date").getValue(String.class);
                    String descValue = dataSnapshot.child("description").getValue(String.class);
                    String imageValue = dataSnapshot.child("imageName").getValue(String.class);

                    title.setText(titleValue);
                    author.setText(nameValue);
                    date.setText(dateValue);
                    description.setText(descValue);
                    //put image in news details
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
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(NewsDetails.this, "databse error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
        finish();
    }
}
