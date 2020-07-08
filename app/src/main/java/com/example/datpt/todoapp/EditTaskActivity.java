package com.example.datpt.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditTaskActivity extends AppCompatActivity {

    EditText titledoes, descdoes, datedoes;
    Button btnSaveTask, btnDelete;
    DatabaseReference reference;
    String keydoes;
    String node;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        titledoes = findViewById(R.id.titledoes);
        descdoes = findViewById(R.id.descdoes);
        datedoes = findViewById(R.id.datedoes);

        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnDelete = findViewById(R.id.btnDelete);

        Intent intent = getIntent();
        node = intent.getStringExtra("node");
        titledoes.setText(intent.getStringExtra("titledoes"));
        descdoes.setText(intent.getStringExtra("descdoes"));
        datedoes.setText(intent.getStringExtra("datedoes"));
        keydoes = intent.getStringExtra("keydoes");

        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child(node).
                        child("Does" + keydoes);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("titledoes").setValue(titledoes.getText().toString());
                        dataSnapshot.getRef().child("descdoes").setValue(descdoes.getText().toString());
                        dataSnapshot.getRef().child("datedoes").setValue(datedoes.getText().toString());
                        dataSnapshot.getRef().child("keydoes").setValue(keydoes);

                        Intent a = new Intent(EditTaskActivity.this, ListTaskActivity.class);
                        a.putExtra("username", node);
                        startActivity(a);
                        Toast.makeText(getApplicationContext(), "Cập nhật thành công!!!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child(node).
                        child("Does" + keydoes);
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent a = new Intent(EditTaskActivity.this, ListTaskActivity.class);
                            a.putExtra("username", node);
                            startActivity(a);
                            Toast.makeText(getApplicationContext(), "Xóa thành công!!!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Xóa thất bại!!!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }
}