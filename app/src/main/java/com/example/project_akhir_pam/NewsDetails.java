package com.example.project_akhir_pam;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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
//
//        title = (TextView) findViewById(R.id.tv_title);
//        author = (TextView) findViewById(R.id.tv_author);
//        date = (TextView) findViewById(R.id.tv_date);
//        description = (TextView) findViewById(R.id.tv_details);
//        back = (ImageView) findViewById(R.id.btn_back);
//
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//
//        Bundle bundle = getIntent().getExtras();
//        CharSequence getId = bundle.getCharSequence("id");
//
//        DatabaseReference newsRef = databaseReference.child("news").child(getId.toString());
//        newsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    String authorValue = dataSnapshot.child("author").getValue(String.class);
//                    String titleValue = dataSnapshot.child("title").getValue(String.class);
//                    String dateValue = dataSnapshot.child("date").getValue(String.class);
//                    String descValue = dataSnapshot.child("description").getValue(String.class);
//
//                    title.setText(titleValue);
//                    author.setText(authorValue);
//                    date.setText(dateValue);
//                    description.setText(descValue);
//                } else {
//                    // The node with the given getId doesn't exist
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Handle the error case if the database operation is canceled
//            }
//        });
    }
}
