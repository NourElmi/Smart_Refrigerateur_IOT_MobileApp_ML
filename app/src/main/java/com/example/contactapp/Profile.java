package com.example.contactapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.contactapp.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Profile extends AppCompatActivity implements View.OnClickListener  {

    BottomNavigationView bottonNavMenu;

    FirebaseFirestore db;

    User user;
    TextView identification;
    TextView password;

    TextView email;

    TextView date;
    TextView Tel;

    ImageView photo;
    Context context;
    Button edit;



    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = Profile.this;

        identification=(TextView) findViewById(R.id.textView);
        password=(TextView) findViewById(R.id.textView1);
        email=(TextView) findViewById(R.id.textView2);
        date=(TextView) findViewById(R.id.textView7);
        Tel=(TextView) findViewById(R.id.textView4);
        photo=(ImageView) findViewById(R.id.photo_user);
        bottonNavMenu = findViewById(R.id.bottomNavMenu);
        edit = findViewById(R.id.button2);

        edit.setOnClickListener(this);


        db = FirebaseFirestore.getInstance();


        mAuth = FirebaseAuth.getInstance();

        bottonNavMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Home:
                        Intent Intent1 = new Intent(Profile.this, Liste_contacts.class);
                        startActivity(Intent1);
                        break;
                    case R.id.Settings:
                        Intent settingsIntent = new Intent(Profile.this, settings.class);
                        startActivity(settingsIntent);
                        break;
                    case R.id.Profile:
                        Intent Intent2 = new Intent(Profile.this, Profile.class);
                        startActivity(Intent2);
                        break;

                }
                return true;
            }
        });
        user =new User();
        getUser();


      /*  identification.setText(user.getNom());
        password.setText(user.getEmail());
        email.setText(user.getEmail());
        date.setText(String.valueOf(user.getDateNaiss()));
        Tel.setText(String.valueOf(user.getTel()));*/

        // Reference to an image file in Cloud Storage
        //StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(user.getUrl());
        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        //Glide.with(this /* context */)
        //   .load(storageReference)
        //   .into(photo);


    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.button2)
        {
            Intent myintent= new Intent(this,   Edit.class);
            myintent.putExtra("user", user);
            startActivity(myintent);
        }
    }

    public void getUser(){

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference docRef = db.collection("Admin").document("admin1@gmail.com").collection("user").document(String.valueOf(currentUser.getEmail()));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String dateNaiss =  document.get("dateNaiss").toString();
                        String mail = (String) document.get("email");
                        String nom = (String) document.get("nom");
                        String pass = document.getString("password");
                        String prenom = document.getString("prenom");
                        String tel = document.getString("Tel");
                        String url = document.getString("url");

                        Log.i("User Values", dateNaiss + " " + email + " " + nom + " " + password + " " + prenom + " " + prenom);

                        if (dateNaiss != null && email != null && nom != null && password != null && prenom != null && url != null) {

                            user.setNom(nom);
                            user.setPrenom(prenom);
                            user.setUrl(url);
                            user.setPassword(pass);
                            user.setTel(tel);
                            user.setDateNaiss(dateNaiss);
                            user.setEmail(mail);
                            Log.i("User Object", user.getNom());
                            identification.setText(user.getNom()+" "+user.getPrenom());
                            password.setText(user.getPassword());
                            email.setText(user.getEmail());
                            date.setText(String.valueOf(user.getDateNaiss()));
                            Tel.setText(String.valueOf(user.getTel()));

                            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(user.getUrl());
                            Glide.with(context)
                                    .load(storageReference)
                                    .into(photo);

                        }
                    } else {
                        Log.d("User Document", "Document does not exist");
                    }
                } else {
                    Log.d("User Document", "Error getting document: ", task.getException());
                }
            }
        });
    }
}