package com.example.contactapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapp.Model.Contact;
import com.example.contactapp.Model.Meal;
import com.example.contactapp.adapters.MealsAdapter;
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

import java.util.ArrayList;
import java.util.List;

public class Meals_List extends AppCompatActivity implements View.OnClickListener {

    //    ActivityMainBinding binding;
    BottomNavigationView bottonNavMenu;
    RecyclerView mealsRecycler;
    EditText barreRecherche;
    TextView servicee;

    //    SearchView barreRecherche;
    ImageView back_button;
    ImageView favorisButton;
    ImageView logoutButton;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    Contact contact;

    List<Meal> meals;
    List<Meal> filteredList;
    MealsAdapter myAdapter;
     @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_meals_list);
         contact= (Contact) getIntent().getSerializableExtra("contact");
//        binding = ActivityMainBinding.inflate(getLayoutInflater());

        mealsRecycler=(RecyclerView) findViewById(R.id.list_meals);
        barreRecherche= (EditText) findViewById(R.id.search);
        bottonNavMenu = findViewById(R.id.bottomNavMenu);
//        fab_add=(FloatingActionButton) findViewById(R.id.fab_add);
//        fab_add.setOnClickListener(this);
//        bottonNavMenuView.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        meals= new ArrayList<Meal>();
        getMeals();
        Log.i("result returned by appeling getMeals(): ", meals.toString());
        addTextListener();

            bottonNavMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.Home:
                            Intent myintent = new Intent(Meals_List.this, Liste_contacts.class);
                            startActivity(myintent);
                            break;
                        case R.id.Settings:
                            Intent settingsIntent = new Intent(Meals_List.this, settings.class);
                            startActivity(settingsIntent);
                            break;
                        case R.id.Profile:
                            Intent Intent2 = new Intent(Meals_List.this, Profile.class);
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
                    MealsAdapter myAdapter = new MealsAdapter(meals, Meals_List.this);
                    mealsRecycler.setAdapter(myAdapter);
                } else {
                    // else we are going to show just the meals that conferms to the query
                    List<Meal> filteredMeals = new ArrayList<>();
                    for (Meal c : meals) {
                        if (c.getNomMeal().toLowerCase().contains(query.toString().toLowerCase()))
//                                ||
//                                c.getPrenomMeal().toLowerCase().contains(query.toString().toLowerCase()) ||
//                                c.getServiceMeal().toLowerCase().contains(query.toString().toLowerCase()))
                        {
                            filteredMeals.add(c);
                        }
                    }
                    MealsAdapter myAdapter = new MealsAdapter(filteredMeals, Meals_List.this);
                    mealsRecycler.setAdapter(myAdapter);
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
//       if(view.getId()==R.id.favoris_button){
//            Intent myintent= new Intent(this, Liste_favoris.class);
//            startActivity(myintent);
//        }
//

    }


    //   void getMeals(){
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        DocumentReference docRef = db.collection("user").document(currentUser.getEmail());
//        docRef.collection("contact").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Meal c= new Meal(document.get("nom").toString(),document.get("prenom").toString(),document.get("service").toString(),document.get("email").toString(),document.get("Tel").toString(),document.get("url").toString());
//                                meals.add(c);
//                            }
//                            // use this setting to improve performance if you know that changes
//                            // in content do not change the layout size of the RecyclerView
//                            Log.i("mesConactssss", meals.toString());
//                            mealsRecycler.setHasFixedSize(true);
//                            myAdapter = new MealsAdapter(meals, Liste_meals.this);
//                            mealsRecycler.setAdapter(myAdapter);
//                            LinearLayoutManager layoutManager = new LinearLayoutManager(Liste_meals.this);
//                            mealsRecycler.setLayoutManager(layoutManager);
//
//                        } else {
//                            Log.d("not ok", "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//    }
    void getMeals() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference docRef = db.collection("Admin").document("admin1@gmail.com");
        docRef.collection("Meals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    meals.clear(); // Clear the meals list before populating it
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String nomMeal = document.getString("nomMeal");
                        String descMeal = document.getString("descMeal");
                        String img_url = document.getString("img_url");
                        String timeNeeded = document.getString("timeNeeded");
                        String mealRate = document.getString("mealRate");
                        String nbrPersonne = document.getString("nbrPersonne");
                        Log.i("nomsdfghjkhgfMeal", nomMeal+"  "+descMeal+"  "+timeNeeded+"  "+img_url+"  "+mealRate+"  "+nbrPersonne);
                        // Check if any required field is null
                        if (nomMeal != null && descMeal != null && img_url != null && timeNeeded != null && mealRate != null && nbrPersonne != null) {
                            Meal c = new Meal(nomMeal, descMeal,timeNeeded, mealRate, nbrPersonne, img_url);
                            Log.i("mesfeeveMeals", c.toString());
                            meals.add(c);
                        }
                    }

                    // Perform the required operations on the meals list here,
                    // such as setting the adapter and updating the RecyclerView

                    Log.i("mesMeals", meals.toString());
                    mealsRecycler.setHasFixedSize(true);
                    myAdapter = new MealsAdapter(meals, Meals_List.this);
                    mealsRecycler.setAdapter(myAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(Meals_List.this);
                    mealsRecycler.setLayoutManager(layoutManager);
                } else {
                    Log.d("not ok", "Error getting documents: ", task.getException());
                }
            }
        });
    }


//    void getMeals() {
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        DocumentReference docRef = db.collection("Admin").document("admin1@gmail.com");
//        docRef.collection("Meals").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Meal c= new Meal(document.get("nomMeal").toString(),document.get("descMeal").toString() ,document.get("img_url").toString(),document.get("timeNeeded").toString() ,document.get("mealRate").toString(),document.get("nbrPersonne").toString());
//                                meals.add(c);
//                            }
//                            // use this setting to improve performance if you know that changes
//                            // in content do not change the layout size of the RecyclerView
//                            Log.i("mesMeals", meals.toString());
//                            mealsRecycler.setHasFixedSize(true);
//                            myAdapter = new MealsAdapter(meals, Meals_List.this);
//                            mealsRecycler.setAdapter(myAdapter);
//                            LinearLayoutManager layoutManager = new LinearLayoutManager(Meals_List.this);
//                            mealsRecycler.setLayoutManager(new GridLayoutManager(Meals_List.this, 2));
//                        } else {
//                            Log.d("not ok", "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//    }



}



