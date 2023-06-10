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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapp.Model.Market;
import com.example.contactapp.adapters.MarketsAdapter;
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

public class Market_list extends AppCompatActivity implements View.OnClickListener {
    BottomNavigationView bottonNavMenu;
    RecyclerView marketsRecycler;
    EditText barreRecherche;
    TextView servicee;

    //    SearchView barreRecherche;
    ImageView back_button;
    ImageView favorisButton;
    ImageView logoutButton;
    FirebaseFirestore db;
    Button Button_add;
    private FirebaseAuth mAuth;


    List<Market> markets;
    List<Market> filteredList;
    MarketsAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_list);


        //        binding = ActivityMainBinding.inflate(getLayoutInflater());
        bottonNavMenu = findViewById(R.id.bottomNavMenu);
        marketsRecycler=(RecyclerView) findViewById(R.id.Market_list);
        barreRecherche= (EditText) findViewById(R.id.search);
        back_button=(ImageView) findViewById(R.id.back_button);

        logoutButton=(ImageView) findViewById(R.id.logout_button);
        bottonNavMenu = findViewById(R.id.bottomNavMenu);
        Button_add = findViewById(R.id.Button_add);

//        fab_add=(FloatingActionButton) findViewById(R.id.fab_add);
//        fab_add.setOnClickListener(this);
//        bottonNavMenuView.setOnClickListener(this);
        Button_add.setOnClickListener((this));
        back_button.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        markets= new ArrayList<Market>();
        getMarkets();
        Log.i("result returned by appeling getUsers(): ", markets.toString());
        addTextListener();

        bottonNavMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Home:
                        Intent myintent = new Intent(Market_list.this, Liste_contacts.class);
                        startActivity(myintent);
                        break;
                    case R.id.Settings:
                        Intent settingsIntent = new Intent(Market_list.this, User_list.class);
                        startActivity(settingsIntent);
                        break;
                    case R.id.Profile:
                        Intent Intent2 = new Intent(Market_list.this, Market_list.class);
                        startActivity(Intent2);
                        break;

                }
                return true;
            }
        });
        bottonNavMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
//                    case R.id.HomeAdmin:
//                        Intent myintent = new Intent(User_list.this, Users_Messages.class);
//                        startActivity(myintent);
//                        break;
                    case R.id.Users:
                        Intent settingsIntent = new Intent(Market_list.this, User_list.class);
                        startActivity(settingsIntent);
                        break;
                    case R.id.Markets:
                        Intent Intent2 = new Intent(Market_list.this, Market_list.class);
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
                    MarketsAdapter myAdapter = new MarketsAdapter(markets, Market_list.this);
                    marketsRecycler.setAdapter(myAdapter);
                } else {
                    // else we are going to show just the meals that conferms to the query
                    List<Market> filteredMarkets = new ArrayList<>();
                    for (Market c : markets) {
                        if (c.getNomProprietaire().toLowerCase().contains(query.toString().toLowerCase()))
//                                ||
//                                c.getPrenomMeal().toLowerCase().contains(query.toString().toLowerCase()) ||
//                                c.getServiceMeal().toLowerCase().contains(query.toString().toLowerCase()))
                        {
                            filteredMarkets.add(c);
                        }
                    }
                    MarketsAdapter myAdapter = new MarketsAdapter(filteredMarkets, Market_list.this);
                    marketsRecycler.setAdapter(myAdapter);
                }
            }
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });
    }


    public void onClick(View view) {
//        if(view.getId()==R.id.fab_add){
//            Intent myintent= new Intent(this, Ajouter_contact.class);
//            startActivity(myintent);
//        }else
        if(view.getId()==R.id.back_button){
            Intent myintent= new Intent(this, User_list.class);
            startActivity(myintent);; // gérer l'événement de clic sur la flèche arrière

        }
        else if(view.getId()==R.id.logout_button){
            mAuth.signOut();
            Intent intent = new Intent(this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
        else if (view.getId()==R.id.Button_add) {
            Intent myintent= new Intent(this, Ajouter_market.class);
            startActivity(myintent);
        }


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
                    myAdapter = new MarketsAdapter(markets, Market_list.this);
                    marketsRecycler.setAdapter(myAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(Market_list.this);
                    marketsRecycler.setLayoutManager(layoutManager);
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