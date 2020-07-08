package com.example.datpt.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.datpt.todoapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnLogin, btnSignup;
    EditText edtUsername, edtPassword;
    DatabaseReference reference;
    ArrayList<User> list;
    User userCurrent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);


        list = new ArrayList<User>();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userCurrent = new User(edtUsername.getText().toString(), edtPassword.getText().toString());
                reference = FirebaseDatabase.getInstance().getReference().child("accounts");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (checkLogin(userCurrent, dataSnapshot)) {
                            Intent intent = new Intent(MainActivity.this, ListTaskActivity.class);
                            intent.putExtra("username", userCurrent.getUsername());
                            startActivity(intent);

                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Tài khoản hoặc mật khẩu không đúng!!!",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean checkLogin(User u, DataSnapshot dataSnapshot) {

        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
            u = dataSnapshot1.getValue(User.class);
            if (u.equals(userCurrent)) {
                return true;
            }
        }
        return false;

    }
}