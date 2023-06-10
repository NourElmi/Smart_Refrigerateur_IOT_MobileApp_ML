package com.example.contactapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.contactapp.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class    Edit extends AppCompatActivity implements View.OnClickListener{

    User user;
    Button btt_add_Contact_img;
    Button btt_add_contact;
    ImageView contact_photo;
    EditText urlImg_Contact;
    EditText tel_Contact;
    EditText nom_Contact;
    EditText prenom_Contact;
    EditText email_Contact;
    EditText service_Contact;
    TextView dateNaiss;
    EditText pass;
    TextView Tel;
    ImageView backButton;
    FirebaseFirestore db;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        user= (User) getIntent().getSerializableExtra("user");


        btt_add_contact=(Button) findViewById(R.id.btt_add_contact);
        contact_photo=(ImageView) findViewById(R.id.contact_photo);
        dateNaiss=(TextView) findViewById(R.id.service_contact);
        nom_Contact=(EditText) findViewById(R.id.nom_Contact);
        prenom_Contact=(EditText) findViewById(R.id.prenom_Contact);
        tel_Contact=(EditText) findViewById(R.id.tel_Contact);
        email_Contact=(EditText) findViewById(R.id.email_contact);
        service_Contact=(EditText) findViewById(R.id.service_contact);
        pass = (EditText) findViewById(R.id.pass);
        backButton = (ImageView) findViewById(R.id.back_button);



        btt_add_contact.setOnClickListener(this);
        backButton.setOnClickListener(this);
        nom_Contact.setText(user.getNom());
        prenom_Contact.setText(user.getPrenom());
        tel_Contact.setText(user.getTel());
        email_Contact.setText(user.getEmail());
        dateNaiss.setText(user.getDateNaiss());
        pass.setText(user.getPassword());
//        password.setText(user.getPassword());


        db = FirebaseFirestore.getInstance();
    }

    void insertNewContact(String date ,String nomContact, String prenomContact, String Pass, String telContact){
        Map<String, String> contactAttributs =new HashMap<>();
        contactAttributs.put("nom", nomContact.trim() );
        contactAttributs.put("prenom", prenomContact.trim() );
        contactAttributs.put("Tel", telContact.trim() );
        contactAttributs.put("email", user.getEmail() );
        contactAttributs.put("password", Pass.trim() );
        contactAttributs.put("url", "gs://projiot-3c780.appspot.com/users/user3.jpeg" );
        contactAttributs.put("dateNaiss", date.trim() );

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference docRef = db.collection("Admin").document("admin1@gmail.com").collection("user").document(currentUser.getEmail());
        docRef.set(contactAttributs);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btt_add_contact) {
            String nom = nom_Contact.getText().toString();
            String prenom = prenom_Contact.getText().toString();

            String dateNaiss = service_Contact.getText().toString();
            String Tel = tel_Contact.getText().toString();

            String password = pass.getText().toString();
            insertNewContact(dateNaiss,nom, prenom,password,Tel);
            Intent myintent= new Intent(this, Profile.class);
            startActivity(myintent);
        }else if(view.getId()==R.id.back_button){
            Intent myintent= new Intent(this, Profile.class);
            startActivity(myintent);
            // gérer l'événement de clic sur la flèche arrière
        }


    }
}