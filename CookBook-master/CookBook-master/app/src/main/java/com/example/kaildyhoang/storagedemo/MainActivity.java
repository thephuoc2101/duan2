package com.example.kaildyhoang.storagedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button _btnAddUser, _btnShow, _btnAddPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _btnAddUser = (Button) findViewById(R.id.buttonAddUser);
        _btnShow = (Button) findViewById(R.id.buttonShow);
        _btnAddPost = (Button) findViewById(R.id.buttonAddPost);


        _btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(),AddActivity.class));
            }
        });

        _btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(),ShowUserDataActivity.class));
            }
        });

        _btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
