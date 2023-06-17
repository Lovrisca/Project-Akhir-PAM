package com.example.project_akhir_pam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    TextView username, email, member, article;
    Button logout;
    ImageView home, add;
    DatabaseReference databaseReference, databaseReferenceCount;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.tv_username);
        email = findViewById(R.id.email1);
        member = findViewById(R.id.member1);
        article = findViewById(R.id.article1);
        logout = findViewById(R.id.btn_logout);
        home = findViewById(R.id.btn_home);
        add = findViewById(R.id.btn_add);

        //get user id
        String userId = String.valueOf(mAuth.getCurrentUser().getUid());
        Log.e("data profile user:", userId);

        databaseReferenceCount = FirebaseDatabase.getInstance().getReference().child("news");
        databaseReferenceCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //count articles by user
                int sum = 0;
                for(DataSnapshot ds : snapshot.getChildren()){
                    Map<String,Object> map = (Map<String, Object>) ds.getValue();
                    Object name = map.get("author");
                    //int pValue = Integer.parseInt(String.valueOf(name));
                    if(userId.matches(name.toString())){
                        sum += 1;
                    }
                }
                article.setText(String.valueOf(sum));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        DatabaseReference userdata = databaseReference.child(userId);
        userdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String usernameValue = dataSnapshot.child("username").getValue(String.class);
                    String emailValue = dataSnapshot.child("email").getValue(String.class);
                    String memberValue = dataSnapshot.child("created").getValue(String.class);
                    //String articleValue = dataSnapshot.child("username").getValue(String.class);
                    username.setText(usernameValue);
                    email.setText(emailValue);
                    member.setText(memberValue);
                    //article.setText(articleValue);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddActivity.class));
                finish();
            }
        });
    }
    public void logOut() {
        mAuth.signOut();
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
    }
}