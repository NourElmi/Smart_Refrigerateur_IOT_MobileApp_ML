package com.example.contactapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.contactapp.Model.Ingredients;
import com.example.contactapp.Model.Meal;
import com.example.contactapp.adapters.InredAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Meals_Details extends AppCompatActivity implements View.OnClickListener {

    //    ActivityMainBinding binding;

    Meal meal;
    BottomNavigationView bottonNavMenu;
    RecyclerView ingredsRecycler;
    EditText barreRecherche;
    ImageView image_Meal;

    TextView Meal_Name;
    TextView ecoScore;
    TextView nbrPersonne;
    TextView meal_Time;
    ImageView back_button;
    Button fav_meal_button;
    ImageView logoutButton;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;

    List<Ingredients> meals;
    List<Ingredients> filteredList;
    InredAdapter myAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_meals_details);
            meal= (Meal) getIntent().getSerializableExtra("meal");
//        binding = ActivityMainBinding.inflate(getLayoutInflater());

            ingredsRecycler=(RecyclerView) findViewById(R.id.list_Ingredients_Meal);
            barreRecherche= (EditText) findViewById(R.id.search);
            fav_meal_button=(Button) findViewById(R.id.fav_meal_button);
            image_Meal=(ImageView) findViewById(R.id.image_Meal);
            Meal_Name= (TextView) findViewById(R.id.Meal_Name);
            ecoScore= (TextView) findViewById(R.id.ecoScore);
            nbrPersonne= (TextView) findViewById(R.id.nbrPersonne);
            meal_Time= (TextView) findViewById(R.id.meal_Time);
            bottonNavMenu = findViewById(R.id.bottomNavMenu);

            fav_meal_button.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        Meal_Name.setText(meal.getNomMeal());
        nbrPersonne.setText(meal.getNbrPersonne());
        meal_Time.setText(meal.getTimeNeeded());
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(meal.getImg_url());
        Glide.with(this /* context */)
                    .load(storageReference)
                    .into(image_Meal);

        meals= new ArrayList<Ingredients>();
        getIngredientss(meal);
        addTextListener();

        bottonNavMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Home:
                        Intent myintent = new Intent(Meals_Details.this, Liste_contacts.class);
                        startActivity(myintent);
                        break;
                    case R.id.Settings:
                        Intent settingsIntent = new Intent(Meals_Details.this, settings.class);
                        startActivity(settingsIntent);
                        break;
                    case R.id.Profile:
                        Intent Intent2 = new Intent(Meals_Details.this, Profile.class);
                        startActivity(Intent2);
                        break;

                }
                return true;
            }
        });


    }
    public void addTextListener() {
        barreRecherche.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                // query contient text qu'on a ecrit dans barre de recherche
                if (query.toString().isEmpty()) {
                    // if query is vide, all meals vont s'afficher
                    InredAdapter myAdapter = new InredAdapter(meals, Meals_Details.this);
                    ingredsRecycler.setAdapter(myAdapter);
                } else {
                    // else we are going to show just the meals that conferms to the query
                    List<Ingredients> filteredIngredientss = new ArrayList<>();
                    for (Ingredients c : meals) {
                        if (c.getNomIngred().toLowerCase().contains(query.toString().toLowerCase()))
//                                ||
//                                c.getPrenomIngredients().toLowerCase().contains(query.toString().toLowerCase()) ||
//                                c.getServiceIngredients().toLowerCase().contains(query.toString().toLowerCase()))
                        {
                            filteredIngredientss.add(c);
                        }
                    }
                    InredAdapter myAdapter = new InredAdapter(filteredIngredientss, Meals_Details.this);
                    ingredsRecycler.setAdapter(myAdapter);
                }
            }
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });
    }

    @Override
    public void onClick(View view) {
//        if(view.getId()==R.id.fab_add){
//            Intent myintent= new Intent(this, Ajouter_contact.class);
//            startActivity(myintent);
//        }else
        if(view.getId()==R.id.favoris_button){
            Intent myintent= new Intent(this, Liste_favoris.class);
            startActivity(myintent);
        }


    }


    //   void getIngredientss(){
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        DocumentReference docRef = db.collection("user").document(currentUser.getEmail());
//        docRef.collection("contact").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Ingredients c= new Ingredients(document.get("nom").toString(),document.get("prenom").toString(),document.get("service").toString(),document.get("email").toString(),document.get("Tel").toString(),document.get("url").toString());
//                                meals.add(c);
//                            }
//                            // use this setting to improve performance if you know that changes
//                            // in content do not change the layout size of the RecyclerView
//                            Log.i("mesConactssss", meals.toString());
//                            ingredsRecycler.setHasFixedSize(true);
//                            myAdapter = new InredAdapter(meals, Liste_meals.this);
//                            ingredsRecycler.setAdapter(myAdapter);
//                            LinearLayoutManager layoutManager = new LinearLayoutManager(Liste_meals.this);
//                            ingredsRecycler.setLayoutManager(layoutManager);
//
//                        } else {
//                            Log.d("not ok", "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//    }
    void getIngredientss(Meal meal) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.i("mesfeeveIngrdsfghjkhgfedientss", meal.getNomMeal());
        DocumentReference docRef = db.collection("Admin").document("admin1@gmail.com").collection("Meals").document(meal.getNomMeal());
        docRef.collection("ingredient").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    meals.clear(); // Clear the meals list before populating it
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String ingredName = document.getString("ingredName");
                        String url_img_Ingred = document.getString("url_img_Ingred");
                        Log.i("nomsdfghjkhgfIngredients", ingredName+"  "+url_img_Ingred);
                        // Check if any required field is null
                        if (ingredName != null && url_img_Ingred != null ) {
                            Ingredients c = new Ingredients(ingredName, url_img_Ingred);
                            Log.i("mesfeeveIngredientss", c.toString());
                            meals.add(c);
                        }
                    }

                    // Perform the required operations on the meals list here,
                    // such as setting the adapter and updating the RecyclerView

                    Log.i("mesIngredientss", meals.toString());
                    ingredsRecycler.setHasFixedSize(true);
                    myAdapter = new InredAdapter(meals, Meals_Details.this);
                    ingredsRecycler.setAdapter(myAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(Meals_Details.this);
                    ingredsRecycler.setLayoutManager(layoutManager);
                    ingredsRecycler.setLayoutManager(new GridLayoutManager(Meals_Details.this, 2));


                } else {
                    Log.d("not ok", "Error getting documents: ", task.getException());
                }
            }
        });
    }



}



