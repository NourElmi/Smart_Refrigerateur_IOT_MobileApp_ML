package com.example.contactapp;

import android.content.Intent;
import android.net.Uri;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Details_User extends AppCompatActivity implements View.OnClickListener {
    User user;
    BottomNavigationView bottonNavMenu;
    TextView identification;
    TextView dateNaiss;
    TextView email_user;
    TextView password;
    TextView Tel;
    ImageView backButton;
    ImageView favoriteButton;
    ImageView photo;
    ImageView call;
    ImageView Icon_sms;
    ImageView Icon_email;
    ImageView Icon_whatsApp;
    FirebaseFirestore db;
    Button delete;
    Button modif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_user);
        user= (User) getIntent().getSerializableExtra("user");

        identification=(TextView) findViewById(R.id.identification_user);
        dateNaiss=(TextView) findViewById(R.id.service_user);
        email_user=(TextView) findViewById(R.id.email_user);
        Tel=(TextView) findViewById(R.id.Tel_user);
        photo=(ImageView) findViewById(R.id.photo_user);
        call=(ImageView) findViewById(R.id.call);
        Icon_sms=(ImageView) findViewById(R.id.sms);
        Icon_email=(ImageView) findViewById(R.id.email);
        Icon_whatsApp=(ImageView) findViewById(R.id.whatsapp);
        bottonNavMenu = findViewById(R.id.bottomNavMenu);
        delete = findViewById(R.id.Button_Delete);
        modif=findViewById(R.id.Button_modif);

        identification.setText(user.getNom()+" "+ user.getPrenom());
        email_user.setText(user.getEmail());
        dateNaiss.setText(user.getDateNaiss());
//        password.setText(user.getPassword());
        Tel.setText(String.valueOf(user.getTel()));
        delete.setOnClickListener(this);
        modif.setOnClickListener(this);
        call.setOnClickListener((View.OnClickListener) this);
        Icon_sms.setOnClickListener((View.OnClickListener)this);
        Icon_email.setOnClickListener((View.OnClickListener)this);
        Icon_whatsApp.setOnClickListener((View.OnClickListener)this);

        db = FirebaseFirestore.getInstance();

        // Reference to an image file in Cloud Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(user.getUrl());
        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        Glide.with(this /* context */)
                .load(storageReference)
                .into(photo);

        bottonNavMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
//                    case R.id.HomeAdmin:
//                        Intent myintent = new Intent(Details_User.this, Users_Messages.class);
//                        startActivity(myintent);
//                        break;
                    case R.id.Users:
                        Intent settingsIntent = new Intent(Details_User.this, User_list.class);
                        startActivity(settingsIntent);
                        break;
                    case R.id.Markets:
                        Intent Intent2 = new Intent(Details_User.this, Market_list.class);
                        startActivity(Intent2);
                        break;

                }
                return true;
            }
        });
    }


    @Override
    public void onClick(View view) {
//        if (view.getId() == R.id.favorite_button) {
////            String nomUser = user.getNom();
////            String prenomUser = user.getPrenom();
////            String dateNaiss = user.getDateNaiss();
////            String emailUser = user.getEmail();
////            String password = user.getPassword();
////            String telUser = user.getTel();
////            String urlImgUser = user.getUrl();
////            insertUserINFavoris(nomUser, prenomUser, emailUser, serviceUser, telUser, urlImgUser);
////            Intent myintent = new Intent(this, Liste_favoris.class);
////            startActivity(myintent);
//
//        } else if (view.getId() == R.id.back_button) {
//            onBackPressed(); // gérer l'événement de clic sur la flèche arrière
//        } else
        if (view.getId() == R.id.call) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + user.getTel()));//change the number
            Log.i("phoneNbrrrrr", String.valueOf(user.getTel()));
            startActivity(callIntent);

        } else if (view.getId() == R.id.sms) {
            Uri uri = Uri.parse("smsto:" + user.getTel());
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            intent.putExtra("sms_body", "Hello, I hope you're doing well. I want to ");
            startActivity(intent);
        } else if (view.getId() == R.id.email) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + user.getEmail())); // only email apps should handle this
            startActivity(emailIntent);

        } else if (view.getId() == R.id.whatsapp) {
            Uri uri = Uri.parse("smsto:" + user.getTel());
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);

        }
        else if (view.getId() == R.id.Button_Delete) {

            deleteNoteFromFirebase();

        }
        else if (view.getId() == R.id.Button_modif) {
            Intent myintent= new Intent(this, modifieruser.class);


            myintent.putExtra("user", user);

            startActivity(myintent);


        }

    }
    private void deleteNoteFromFirebase() {
        DocumentReference documentReference;

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        documentReference = db.collection("Admin").document("admin1@gmail.com");
        documentReference.collection("user").document(user.getEmail()).delete();
       /* String doc= contact.getNomContact();
        produit p=new produit();
        p.setNom(contact.getNomContact());
        p.setUrl(contact.getImg_url());// gérer l'événement de clic sur la flèche arrière
            documentReference.collection("ShoppingList").document(doc).delete();*/

        Intent myintent= new Intent(this, User_list.class);
        startActivity(myintent);

    }
}