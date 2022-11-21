package com.samuelvialle.simplecrudfirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private EditText etNoteTitle, etNoteContent;
    private Button btnSaveNote, btnShowNote;

    private FirebaseFirestore db;

    private void initUI(){
        etNoteTitle = findViewById(R.id.etNoteTitle);
        etNoteContent = findViewById(R.id.etNoteContent);
        btnSaveNote = findViewById(R.id.btnSaveNote);
        btnShowNote = findViewById(R.id.btnShowAllNotes);
    }

    private void initFirebaseTools(){
        db = FirebaseFirestore.getInstance();
    }

    private void clicSaveNote(){
        btnSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Récupération des données duformulaire
                String id = UUID.randomUUID().toString();
                String title = etNoteTitle.getText().toString();
                String content = etNoteContent.getText().toString();

                // Apple de la méthode pour la creation en base
                createDocumentInFirestore(id, title, content);
            }
        });
    }

    private void clicShowAllNotes(){
        btnShowNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Show all notes", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ShowAllNotesActivity.class));
            }
        });
    }

    private void createDocumentInFirestore(String id, String title, String content){
        if(!title.isEmpty() && !content.isEmpty()){
            // Création d'un tableau qui contoient les data à envoyer sur Firestore
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("title", title);
            map.put("content", content);

            db.collection("Notes").document(id).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this, "Note saved !", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Failed " + e, Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Toast.makeText(this, "Empty fields are not allow", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initFirebaseTools();
        clicSaveNote();
        clicShowAllNotes();
    }
}