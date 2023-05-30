package com.example.project_akhir_pam;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    EditText email, password;
    Button btnLogin;
    TextView registerNow;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        registerNow = findViewById(R.id.register_now);

        mAuth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(this);

        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                login(email.getText().toString(), password.getText().toString());
                break;
        }
    }

    public void login(String email,String password){
        if (!validateForm()){
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed - in user 's information
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(LoginActivity.this, user.toString(), Toast.LENGTH_SHORT).show();
                    updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }
    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError("Required");
            result = false;
        } else {
            email.setError(null);
        }
        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("Required");
            result = false;
        } else {
            password.setError(null);
        }
        return result;
    }
    public void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, AddActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(LoginActivity.this,"Log In First", Toast.LENGTH_SHORT).show();
        }
    }
}