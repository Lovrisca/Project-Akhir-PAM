package com.example.project_akhir_pam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewsDetails extends AppCompatActivity {
    TextView title, date, author, description;
    ImageView back;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        title = findViewById(R.id.tv_title);
        author = findViewById(R.id.tv_author);
        date = findViewById(R.id.tv_date);
        description = findViewById(R.id.tv_details);
        back = findViewById(R.id.btn_back);
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
                    author.setText(authorValue);
                    date.setText(dateValue);
                    description.setText(descValue);
                } else {
                    Toast.makeText(NewsDetails.this, "Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(NewsDetails.this, "databse error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onBackPressed() {
        finish();
    }
}
