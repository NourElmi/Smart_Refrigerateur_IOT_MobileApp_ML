package com.example.contactapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.contactapp.Model.Contact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class Detail_contact extends AppCompatActivity implements View.OnClickListener {

    Contact contact;
    TextView identification;
    TextView service;
    TextView email_contact;
    TextView Tel;
    ImageView backButton;
    ImageView favoriteButton;
    ImageView photo;
    ImageView call;
    ImageView Icon_sms;
    ImageView Icon_email;
    ImageView Icon_whatsApp;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_contact);
        contact= (Contact) getIntent().getSerializableExtra("contact");



        identification=(TextView) findViewById(R.id.identification_contact);
        service=(TextView) findViewById(R.id.service_contact);
        email_contact=(TextView) findViewById(R.id.email_contact);
        Tel=(TextView) findViewById(R.id.Tel_contact);
        photo=(ImageView) findViewById(R.id.photo_contact);
        call=(ImageView) findViewById(R.id.call);
        Icon_sms=(ImageView) findViewById(R.id.sms);
        Icon_email=(ImageView) findViewById(R.id.email);
        Icon_whatsApp=(ImageView) findViewById(R.id.whatsapp);
        backButton = (ImageView) findViewById(R.id.back_button);
        favoriteButton = (ImageView) findViewById(R.id.favorite_button);


//        identification.setText(contact.getNomContact()+" "+ contact.getPrenomContact());
//        service.setText(contact.getServiceContact());
//        email_contact.setText(contact.getEmailContact());
//        Tel.setText(String.valueOf(contact.getTel()));
        call.setOnClickListener(this);
        Icon_sms.setOnClickListener(this);
        Icon_email.setOnClickListener(this);
        Icon_whatsApp.setOnClickListener(this);
        backButton.setOnClickListener(this);
        favoriteButton.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();

        // Reference to an image file in Cloud Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(contact.getImg_url());
        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        Glide.with(this /* context */)
                .load(storageReference)
                .into(photo);
    }
    private void insertContactINFavoris(String nomContact, String urlImgContact) {
        Map<String, String> contactAttributs =new HashMap<>();
        contactAttributs.put("nom", nomContact.trim() );
//        contactAttributs.put("prenom", prenomContact.trim() );
//        contactAttributs.put("Tel", telContact.trim() );
//        contactAttributs.put("email", emailContact.trim() );
//        contactAttributs.put("service", serviceContact.trim() );
        contactAttributs.put("url", "gs://contactapp-b7ba8.appspot.com/photos/nour1234@gmail.com.jpeg" );

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        CollectionReference docRef = db.collection("user").document(currentUser.getEmail()).collection("ShoppingList");
        docRef.document(nomContact).set(contactAttributs);
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.favorite_button){
            String nomContact = contact.getNomContact();
//            String prenomContact = contact.getPrenomContact();
//            String emailContact = contact.getEmailContact();
//            String serviceContact =contact.getServiceContact();
//            String telContact = contact.getTel();
            String urlImgContact = contact.getImg_url();
            insertContactINFavoris(nomContact, urlImgContact );
            Intent myintent= new Intent(this, Liste_favoris.class);
            startActivity(myintent);

        }else if(view.getId()==R.id.back_button){
            onBackPressed(); // gérer l'événement de clic sur la flèche arrière
        } else if(view.getId()==R.id.call)
        {
//            Intent callIntent = new Intent(Intent.ACTION_DIAL);
//            callIntent.setData(Uri.parse("tel:" + contact.getTel()));//change the number
//           Log.i("phoneNbrrrrr", String.valueOf(contact.getTel()));
//            startActivity(callIntent);

        }else if(view.getId()==R.id.sms){
//            Uri uri = Uri.parse("smsto:"+ contact.getTel());
//            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
//            intent.putExtra("sms_body", "Hello, I hope you're doing well. I want to ");
//            startActivity(intent);
        }else if(view.getId()==R.id.email)
        {
//            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//            emailIntent.setData(Uri.parse("mailto:" + contact.getEmailContact())); // only email apps should handle this
//            startActivity(emailIntent);

        }else if(view.getId()==R.id.whatsapp){
//            Uri uri = Uri.parse("smsto:"+  contact.getTel());
//            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
//            sendIntent.setPackage("com.whatsapp");
//            startActivity(sendIntent);

        }



    }


}