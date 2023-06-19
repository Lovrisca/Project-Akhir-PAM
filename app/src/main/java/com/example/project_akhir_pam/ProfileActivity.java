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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1000084332129-78nbcqpqe6ihd57j02cn85k4ai12pn9l.apps.googleusercontent.com")
                .requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

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
        String emil = String.valueOf(mAuth.getCurrentUser().getEmail());
        String name = String.valueOf(mAuth.getCurrentUser().getDisplayName());
        Log.e("email:", emil);
        Log.e("display name:",name);
        Log.e("user Id:", userId);

        email.setText(emil);
        if(!name.isEmpty()){
            username.setText(name);
        }

        databaseReferenceCount = FirebaseDatabase.getInstance().getReference().child("news");
        databaseReferenceCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //count articles by user
                int sum = 0;
                for(DataSnapshot ds : snapshot.getChildren()){
                    Map<String,Object> map = (Map<String, Object>) ds.getValue();
                    Object name = map.get("authorId");
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
                    String memberValue = dataSnapshot.child("created").getValue(String.class);
                    username.setText(usernameValue);
                    member.setText(memberValue);
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
        String name = String.valueOf(mAuth.getCurrentUser().getDisplayName());
        Log.e("name:", name);
        if(!name.isEmpty()){
            googleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.e("status", "berhasil logout");
                    mAuth.signOut();
                    newTask();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("status", "GAGAL LOGOUT");
                }
            });
        }
        else{
            mAuth.signOut();
            newTask();
        }
    }
    public void newTask(){
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
    }
}