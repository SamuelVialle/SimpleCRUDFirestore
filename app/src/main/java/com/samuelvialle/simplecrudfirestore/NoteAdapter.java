package com.samuelvialle.simplecrudfirestore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvShowTitle);
            content = itemView.findViewById(R.id.tvShowContent);
        }
    }

    
}
