package com.example.project_akhir_pam;

import static com.example.project_akhir_pam.DashboardAdapter.getCurrentDate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    RecyclerView rc;
    DashboardAdapter adapter;
    ImageView add, home, profile;
    Boolean isProfileRunning = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setTitle("Search");

        rc = (RecyclerView)findViewById(R.id.recycle_news);
        rc.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Note> options = new FirebaseRecyclerOptions.Builder<Note>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("news"), Note.class)
                        .build();

        adapter = new DashboardAdapter(options);
        rc.setAdapter(adapter);

        add = findViewById(R.id.btn_add);
        home = findViewById(R.id.btn_home);
        profile = findViewById(R.id.btn_profile);

        //to add activity Intent
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddActivity.class));
                finish();
            }
        });
        //to profile activity Intent
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isProfileRunning = true;
                startActivity(new Intent(getApplicationContext(),profile.class));
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isProfileRunning) {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.search_menu,menu);

        MenuItem item = menu.findItem(R.id.search);

        SearchView searchView=(SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processsearch(s);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void processsearch(String s)
    {
        FirebaseRecyclerOptions<Note> options =
                new FirebaseRecyclerOptions.Builder<Note>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("news").orderByChild("title").startAt(s).endAt(s+"\uf8ff"), Note.class)
                        .build();
        adapter=new DashboardAdapter(options);
        adapter.startListening();
        rc.setAdapter(adapter);
    }
}