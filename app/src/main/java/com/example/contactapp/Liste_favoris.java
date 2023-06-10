package com.example.contactapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapp.Model.Contact;
import com.example.contactapp.Model.Market;
import com.example.contactapp.adapters.MarketHorizAdapter;
import com.example.contactapp.adapters.MyAdapterDelete;
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

public class Liste_favoris extends AppCompatActivity implements View.OnClickListener {

    RecyclerView contactsRecycler;
    RecyclerView marketsRecycler;
    EditText barreRecherche;
    ImageView backButton;
    ImageView logoutButton;
    Button addshop;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    SharedPreferences.Editor editor;

    MarketHorizAdapter myAdapter;
    LinkedList<Contact> contacts;
    List<Market> markets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_favoris);
        contactsRecycler = (RecyclerView) findViewById(R.id.list_contacts);
        marketsRecycler=(RecyclerView) findViewById(R.id.list_magasin);

        barreRecherche = (EditText) findViewById(R.id.search);
        logoutButton = (ImageView) findViewById(R.id.logout_button);
        backButton = (ImageView) findViewById(R.id.back_button);
        addshop = (Button) findViewById(R.id.addshop);

        backButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        addshop.setOnClickListener(this);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        contacts = new LinkedList<Contact>();
        markets= new ArrayList<Market>();

        getMarkets();
        getContacts();
        addTextListener();

    }
    public void addTextListener() {
        barreRecherche.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                // query contient text qu'on a ecrit dans barre de recherche
                if (query.toString().isEmpty()) {
                    // if query is vide, all contacts vont s'afficher
                    MyAdapterDelete myAdapter = new MyAdapterDelete(contacts, Liste_favoris.this);
                    contactsRecycler.setAdapter(myAdapter);
                } else {
                    // else we are going to show just the contacts that conferms to the query
                    List<Contact> filteredContacts = new ArrayList<>();
                    for (Contact c : contacts) {
                        if (c.getNomContact().toLowerCase().contains(query.toString().toLowerCase())) {
                            filteredContacts.add(c);
                        }
                    }
                    MyAdapterDelete myAdapter = new MyAdapterDelete(filteredContacts, Liste_favoris.this);
                    contactsRecycler.setAdapter(myAdapter);
                }
            }
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });
    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.back_button){
            Intent myintent= new Intent(this, Liste_contacts.class);
            startActivity(myintent);; // gérer l'événement de clic sur la flèche arrière
        } else if (view.getId() == R.id.logout_button) {
            mAuth.signOut();
            Intent intent = new Intent(this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if (view.getId() == R.id.addshop){
            Intent myintent= new Intent(this, noshop.class);
            startActivity(myintent);
        }


    }


    void getContacts() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference docRef = db.collection("Admin").document("admin1@gmail.com").collection("user").document(currentUser.getEmail());
        docRef.collection("ShoppingList").get()
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
                            MyAdapterDelete myAdapter = new MyAdapterDelete(contacts, Liste_favoris.this);
                            contactsRecycler.setAdapter(myAdapter);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(Liste_favoris.this);
                            contactsRecycler.setLayoutManager(layoutManager);

                        } else {
                            Log.d("not ok", "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    void getMarkets() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference docRef = db.collection("Admin").document("admin1@gmail.com");
        docRef.collection("superMarPart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    markets.clear(); // Clear the meals list before populating it
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String Ouver = document.getString("Ouver");
                        String Ferm = document.getString("Ferm");
                        String nomProprietaire = document.getString("nomProprietaire");
                        String localisation = document.getString("localisation");
                        String urlMark = document.getString("urlMark");

                        Log.i("nomsdfghjkhgfUser", nomProprietaire+"  "+urlMark);
                        // Check if any required field is null
                        if (nomProprietaire != null && urlMark != null) {
                            Market c = new Market(nomProprietaire, urlMark, localisation, Ouver, Ferm);
                            Log.i("mesfeeveUsers", c.toString());
                            markets.add(c);
                        }
                    }

                    // Perform the required operations on the meals list here,
                    // such as setting the adapter and updating the RecyclerView

                    Log.i("mesUsers", markets.toString());
                    marketsRecycler.setHasFixedSize(true);
                    myAdapter = new MarketHorizAdapter(markets, Liste_favoris.this);
                    marketsRecycler.setAdapter(myAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(Liste_favoris.this, LinearLayoutManager.HORIZONTAL, false);
                    marketsRecycler.setLayoutManager(layoutManager);

                } else {
                    Log.d("not ok", "Error getting documents: ", task.getException());
                }
            }
        });
    }

}