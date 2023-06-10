package com.example.contactapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Ajouter_market extends AppCompatActivity implements View.OnClickListener{

    Button btt_add_Contact_img;
    Button btt_add_contact;
    ImageView contact_photo;
    EditText urlImg_Contact;
    EditText nom;
    EditText localisation;
    EditText ferm;
    EditText ouvert;
    Button add;
    EditText nom_Contact;
    EditText prenom_Contact;
    EditText email_Contact;
    EditText service_Contact;
    EditText pass;
    ImageView backButton;
    FirebaseFirestore db;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_market);


        btt_add_contact=(Button) findViewById(R.id.btt_add_contact);
        nom=(EditText) findViewById(R.id.nom);
        localisation=(EditText) findViewById(R.id.localisation);
        ferm=(EditText) findViewById(R.id.ferm);
        ouvert=(EditText) findViewById(R.id.ouver);

       /* nom_Contact=(EditText) findViewById(R.id.nom_Contact);
        prenom_Contact=(EditText) findViewById(R.id.prenom_Contact);
        tel_Contact=(EditText) findViewById(R.id.tel_Contact);
        email_Contact=(EditText) findViewById(R.id.email_contact);
        service_Contact=(EditText) findViewById(R.id.service_contact);
        pass = (EditText) findViewById(R.id.pass);*/
        backButton = (ImageView) findViewById(R.id.back_button);


        btt_add_contact.setOnClickListener(this);
        backButton.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
    }

    void insertNewContact(String loc,String nom,String ouver,String ferm){
        Map<String, String> contactAttributs =new HashMap<>();
        /*contactAttributs.put("nom", nomContact.trim() );
        contactAttributs.put("prenom", prenomContact.trim() );
        contactAttributs.put("Tel", telContact.trim() );
        contactAttributs.put("email", emailContact.trim() );
        contactAttributs.put("password", Pass.trim() );
        contactAttributs.put("url", "gs://projiot-3c780.appspot.com/users/user3.jpeg" );
        contactAttributs.put("dateNaiss", date.trim() );*/
        contactAttributs.put("nomProprietaire", nom.trim() );
        contactAttributs.put("localisation", loc.trim() );
        contactAttributs.put("Ouver", ouver.trim() );
        contactAttributs.put("Ferm", ferm.trim() );
        contactAttributs.put("urlMark", "gs://projiot-3c780.appspot.com/Markets/bim3.jpeg" );

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        CollectionReference docRef = db.collection("Admin").document("admin1@gmail.com").collection("superMarPart");
        docRef.document(nom).set(contactAttributs);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btt_add_contact) {
            /*String nom = nom_Contact.getText().toString();
            String prenom = prenom_Contact.getText().toString();
            String email = email_Contact.getText().toString();
            String dateNaiss = service_Contact.getText().toString();
            String Tel = tel_Contact.getText().toString();

            String password = pass.getText().toString();*/
            String n =nom.getText().toString();
            String loc =localisation.getText().toString();
            String o =ouvert.getText().toString();
            String f =ferm.getText().toString();
            insertNewContact(loc,n,o,f);
            Intent myintent= new Intent(this, Market_list.class);
            startActivity(myintent);
        }else if(view.getId()==R.id.back_button){
            onBackPressed(); // gérer l'événement de clic sur la flèche arrière
        }

    }
}