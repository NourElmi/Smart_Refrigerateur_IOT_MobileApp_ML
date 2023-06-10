package com.example.contactapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapp.Model.Contact;
import com.example.contactapp.adapters.MyAdapterAdd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class noshop extends AppCompatActivity implements View.OnClickListener {

    RecyclerView contactsRecycler;
    EditText barreRecherche;
    ImageView backButton;
    ImageView logoutButton;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    SharedPreferences.Editor editor;
    LinkedList<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noshop);
        contactsRecycler = (RecyclerView) findViewById(R.id.list_contacts);
        barreRecherche = (EditText) findViewById(R.id.search);
        logoutButton = (ImageView) findViewById(R.id.logout_button);
        backButton = (ImageView) findViewById(R.id.back_button);


        backButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        contacts = new LinkedList<Contact>();

        getContacts();
        addTextListener();

    }

    public void addTextListener() {
        barreRecherche.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                // query contient text qu'on a ecrit dans barre de recherche
                if (query.toString().isEmpty()) {
                    // if query is vide, all contacts vont s'afficher
                    MyAdapterAdd myAdapter = new MyAdapterAdd(contacts, noshop.this);
                    contactsRecycler.setAdapter(myAdapter);
                } else {
                    // else we are going to show just the contacts that conferms to the query
                    List<Contact> filteredContacts = new ArrayList<>();
                    for (Contact c : contacts) {
                        if (c.getNomContact().toLowerCase().contains(query.toString().toLowerCase())) {
                            filteredContacts.add(c);
                        }
                    }
                    MyAdapterAdd myAdapter = new MyAdapterAdd(filteredContacts, noshop.this);
                    contactsRecycler.setAdapter(myAdapter);
                }
            }

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_button) {
            Intent myintent= new Intent(this, Liste_favoris.class);
            startActivity(myintent);; // gérer l'événement de clic sur la flèche arrière
        } else if (view.getId() == R.id.logout_button) {
            mAuth.signOut();
            Intent intent = new Intent(this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }


    }


    void getContacts() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference docRef = db.collection("Admin").document("admin1@gmail.com").collection("user").document(currentUser.getEmail());
        docRef.collection("FrigoBasProd").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Contact c = new Contact(document.get("nom").toString(), document.get("url").toString());
                                contacts.add(c);
                            }
                            // use this setting to improve performance if you know that changes
                            // in content do not change the layout size of the RecyclerView
                            Log.i("mesConactssss", contacts.toString());

                            contactsRecycler.setHasFixedSize(true);
                            MyAdapterAdd myAdapter = new MyAdapterAdd(contacts, noshop.this);
                            contactsRecycler.setAdapter(myAdapter);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(noshop.this);
                            contactsRecycler.setLayoutManager(layoutManager);

                        } else {
                            Log.d("not ok", "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

}