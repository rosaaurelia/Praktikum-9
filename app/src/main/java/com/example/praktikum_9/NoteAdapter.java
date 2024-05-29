package com.example.praktikum_9;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private Context context;
    private List<Note> noteList;
    private DatabaseReference databaseReference;

    public NoteAdapter(Context context, List<Note> noteList)
    {
        this.context = context;
        this.noteList = noteList;
        this.databaseReference = FirebaseDatabase.getInstance("https://pamfirebase-1bd9a-default-rtdb.firebaseio.com/").getReference("notes");
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc;
        Button btnEdit, btnDel;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDel = itemView.findViewById(R.id.btn_delete);
            tvDesc = itemView.findViewById(R.id.et_description);
        }
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.tvTitle.setText(note.getTitle());
//        holder.tvDesc.setText(note.getDescription());

        holder.btnEdit.setOnClickListener(v-> {
            Intent intent = new Intent(context, UpdateDialog.class);
            intent.putExtra("noteId", note.getID());
            intent.putExtra("title", note.getTitle());
            intent.putExtra("desc", note.getDescription());
            context.startActivity(intent);
        });

        holder.btnDel.setOnClickListener(v->
        {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Note")
                    .setMessage("Confirm to delete this note")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        databaseReference.child(uid).child(note.getID()).removeValue().addOnSuccessListener(aVoid -> {
                            Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show();
                            if (noteList.size() > 0 && position < noteList.size()) {
                                noteList.remove(position);
                            }
                        }).addOnFailureListener(e -> {
                            Toast.makeText(context, "Failed to delete Note", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }


}
