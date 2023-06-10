package com.example.contactapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.contactapp.Model.Contact;
import com.example.contactapp.Model.produit;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Delete extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    Contact contact;
    Button deletebutton;
    Button exitbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        contact = (Contact) getIntent().getSerializableExtra("contact");
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        deletebutton = (Button) findViewById(R.id.buttonDelete);
        exitbutton = (Button) findViewById(R.id.buttonCancel);
        exitbutton.setOnClickListener((v)->back());
        deletebutton.setOnClickListener((v)-> deleteNoteFromFirebase() );


    }

    private void back() {
        Toast.makeText(Delete.this, "Produit not deleted", Toast.LENGTH_SHORT).show();
        Intent myintent= new Intent(this, Liste_favoris.class);
        startActivity(myintent);
    }

    private void deleteNoteFromFirebase() {
        DocumentReference documentReference;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        documentReference = db.collection("Admin").document("admin1@gmail.com").collection("user").document(currentUser.getEmail());
        String doc= contact.getNomContact();
        produit p=new produit();
        p.setNom(contact.getNomContact());
        p.setUrl(contact.getImg_url());// gérer l'événement de clic sur la flèche arrière
        documentReference.collection("ShoppingList").document(doc).delete();
        Toast.makeText(Delete.this, "Produit Deleted", Toast.LENGTH_SHORT).show();

        Intent myintent= new Intent(this, Liste_favoris.class);
        startActivity(myintent);

    }



}