package com.example.project_akhir_pam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity {
    private EditText title, description;
    private Button btnSave;
    private FirebaseAuth mAuth;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        mAuth = FirebaseAuth.getInstance();
        title = findViewById(R.id.et_title);
        description = findViewById(R.id.et_desc);
        btnSave = findViewById(R.id.btn_save);
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
                    String authorValue = dataSnapshot.child("author").getValue(String.class);
                    String titleValue = dataSnapshot.child("title").getValue(String.class);
                    String dateValue = dataSnapshot.child("date").getValue(String.class);
                    String descValue = dataSnapshot.child("description").getValue(String.class);
                    title.setText(titleValue);
                    description.setText(descValue);
                } else {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UpdateActivity.this, "databse error", Toast.LENGTH_SHORT).show();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm()) {
                    return;
                }
                Map<String, Object> map = new HashMap<>();
                map.put("title", title.getText().toString());
                map.put("description", description.getText().toString());
                map.put("date", getCurrentDate()+"-updated");
                databaseReference.child(id).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(UpdateActivity.this, "Updated", Toast.LENGTH_SHORT).show();
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
        if (TextUtils.isEmpty(description.getText().toString())) {
            description.setError("Required");
            result = false;
        } else {
            description.setError(null);
        }
        return result;
    }

    public void onBackPressed() {
        finish();
    }
}