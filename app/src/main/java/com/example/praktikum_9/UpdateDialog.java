package com.example.praktikum_9;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateDialog extends AppCompatActivity {
    private EditText etTitle, etDesc;
    private Button btnSubmit, btnDel;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String noteID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_dialog);

        etDesc = findViewById(R.id.et_description);
        etTitle = findViewById(R.id.et_title);
        btnDel = findViewById(R.id.btn_del);
        btnSubmit = findViewById(R.id.btn_save);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance("https://pamfirebase-1bd9a-default-rtdb.firebaseio.com/").getReference("notes").child(mAuth.getCurrentUser().getUid());

        if (getIntent().hasExtra("noteId")){
            noteID = getIntent().getStringExtra("noteId");
            etTitle.setText(getIntent().getStringExtra("title"));
            etDesc.setText(getIntent().getStringExtra("desc"));
            btnSubmit.setText("Update");
        } else {
            noteID = null;
        }

        btnDel.setOnClickListener(v->{
            etTitle.setText("");
            etDesc.setText("");
        });

        btnSubmit.setOnClickListener(v->{
            if (validateForm()){
                submitData();
            }
        });
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(etTitle.getText().toString())) {
            etTitle.setError("Required");
            result = false;
        } else {
            etTitle.setError(null);
        }
        if (TextUtils.isEmpty(etDesc.getText().toString())) {
            etDesc.setError("Required");
            result = false;
        } else {
            etDesc.setError(null);
        }
        return result;
    }

    private void submitData() {
        String title = etTitle.getText().toString();
        String desc = etDesc.getText().toString();
        Note note = new Note(title, desc);
        if (noteID == null) {
            databaseReference.push().setValue(note) // Hapus uid dari sini
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(UpdateDialog.this, "Note added", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(UpdateDialog.this, "Failed to add note", Toast.LENGTH_SHORT).show());
        } else {
            databaseReference.child(noteID).setValue(note) // Hapus uid dari sini
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(UpdateDialog.this, "Note updated", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(UpdateDialog.this, "Failed to update note", Toast.LENGTH_SHORT).show());
        }
    }
}