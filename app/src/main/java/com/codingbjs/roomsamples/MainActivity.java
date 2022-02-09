package com.codingbjs.roomsamples;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;

import com.codingbjs.roomsamples.databinding.ActivityMainBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;

    AppDatabase db;

    ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());


//        db = Room.databaseBuilder(this, AppDatabase.class, "todo_db")
//                .allowMainThreadQueries()
//                .build();

        db = Room.databaseBuilder(this, AppDatabase.class, "todo_db").build();

        executorService = Executors.newFixedThreadPool(4);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mainBinding.resultText.setText(db.todoDao().getAll().toString());
            }
        });


        mainBinding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        db.todoDao().insert(new Todo(mainBinding.todoEditText.getText().toString()));
                        mainBinding.resultText.setText(db.todoDao().getAll().toString());
                    }
                });
            }
        });
    }
}