package com.samuelvialle.simplecrudfirestore;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private ShowAllNotesActivity activity;
    private List<NoteModel> notesList;

    public NoteAdapter() {
    }

    public NoteAdapter(ShowAllNotesActivity activity, List<NoteModel> notesList) {
        this.activity = activity;
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.title.setText(notesList.get(position).getTitle());
        holder.content.setText(notesList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView title, content;
        ImageView btnEdit, btnDelete;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvShowTitle);
            content = itemView.findViewById(R.id.tvShowContent);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateNote(getAdapterPosition());
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAlertDialog(getAdapterPosition());
                }
            });
        }
    }

    private void showAlertDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteNote(position);
            }
        });
        builder.setNegativeButton("CANCEl", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(activity, "Delete canceled", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setTitle("Delete confirmation");
        builder.setMessage("Are you sure to delete the note ?");
        builder.create();
        builder.show();

    }

    private void updateNote(int position) {
        NoteModel note = notesList.get(position);
        // Créer un bundle pour envoyer les infos sur la page de modification
        Bundle bundle = new Bundle();
        bundle.putString("uId", note.getId());
        bundle.putString("uTitle", note.getTitle());
        bundle.putString("uContent", note.getContent());

        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    private void deleteNote(int position) {
        NoteModel note = notesList.get(position);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Notes").document(note.getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            notifyRemoved(position);
                            Toast.makeText(activity, "Data deleted!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void notifyRemoved(int position){
        notesList.remove(position);
        notifyItemRemoved(position);
        activity.readDataFromFirestore();
    }


}
