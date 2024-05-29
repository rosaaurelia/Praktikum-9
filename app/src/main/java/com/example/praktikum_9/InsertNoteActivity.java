package com.example.praktikum_9;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InsertNoteActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tvEmail;
//    private TextView tvUid;
    private Button btnKeluar, btnTambah, btnLihat;
    private FirebaseAuth mAuth;
    private NoteAdapter noteAdapter;
//    private EditText etTitle;
//    private EditText etDesc;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ListView listViewNotes;
    private List<Note> noteList = new ArrayList<>();
    private ArrayAdapter<Note> adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_note);

        tvEmail = findViewById(R.id.tv_email);
        btnKeluar = findViewById(R.id.btn_keluar);
        btnTambah = findViewById(R.id.btn_add);
        btnLihat = findViewById(R.id.btn_show);
        recyclerView = findViewById(R.id.recycler_view);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("notes").child(mAuth.getCurrentUser().getUid());

        btnKeluar.setOnClickListener(this);
        btnTambah.setOnClickListener(this);
        btnLihat.setOnClickListener(this);

        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(this, noteList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(noteAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            tvEmail.setText(currentUser.getEmail());
        }
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_keluar) {
            logOut();
        } else if (view.getId() == R.id.btn_add) {
            Intent intent = new Intent(InsertNoteActivity.this, UpdateDialog.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_show) {
            listNotes();
        }
    }

    private void listNotes() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noteList.clear();
                for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                    Note note = noteSnapshot.getValue(Note.class);
                    note.setID(noteSnapshot.getKey());
                    noteList.add(note);
                }
                noteAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(InsertNoteActivity.this, "Failed to load notes", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void logOut(){
        mAuth.signOut();
        Intent intent = new Intent(InsertNoteActivity.this,
                MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//makesure user cant go back
        startActivity(intent);
    }

}