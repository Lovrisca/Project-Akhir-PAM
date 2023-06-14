package com.example.project_akhir_pam;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NewsDetails extends AppCompatActivity {
    TextView title, date, author, description;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        Note note = new Note();

        title = (TextView) findViewById(R.id.tv_title);
        author = (TextView) findViewById(R.id.tv_author);
        date = (TextView) findViewById(R.id.tv_date);
        description = (TextView) findViewById(R.id.tv_details);
        back = (ImageView) findViewById(R.id.btn_back);

        title.setText(note.getTitle());
        author.setText(note.getAuthor());
        date.setText(note.getDate());
        description.setText(note.getDescription());
    }
}
