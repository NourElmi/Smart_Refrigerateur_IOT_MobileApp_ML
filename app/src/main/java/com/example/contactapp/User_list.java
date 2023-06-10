package com.example.contactapp;

import android.annotation.SuppressLint;
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

import com.example.contactapp.Model.User;
import com.example.contactapp.adapters.UsersAdapter;
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

public class User_list extends AppCompatActivity implements View.OnClickListener {

    //    ActivityMainBinding binding;
    BottomNavigationView bottonNavMenu;
    RecyclerView usersRecycler;
    EditText barreRecherche;
    TextView servicee;

    //    SearchView barreRecherche;
    ImageView back_button;
    ImageView favorisButton;
    ImageView logoutButton;
    Button Button_add;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;

    List<User> users;
    List<User> filteredList;
    UsersAdapter myAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

//        binding = ActivityMainBinding.inflate(getLayoutInflater());

        usersRecycler=(RecyclerView) findViewById(R.id.list_users);
        barreRecherche= (EditText) findViewById(R.id.search);
        back_button=(ImageView) findViewById(R.id.back_button);
        logoutButton=(ImageView) findViewById(R.id.logout_button);
        bottonNavMenu = findViewById(R.id.bottomNavMenu);
        Button_add=findViewById(R.id.Button_add);
//        fab_add=(FloatingActionButton) findViewById(R.id.fab_add);
//        fab_add.setOnClickListener(this);
        Button_add.setOnClickListener(this);


        logoutButton.setOnClickListener(this);
//        bottonNavMenuView.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        users= new ArrayList<User>();
        getUsers();
        Log.i("result returned by appeling getUsers(): ", users.toString());
        addTextListener();

        bottonNavMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
//                    case R.id.HomeAdmin:
//                        Intent myintent = new Intent(User_list.this, Users_Messages.class);
//                        startActivity(myintent);
//                        break;
                    case R.id.Users:
                        Intent settingsIntent = new Intent(User_list.this, User_list.class);
                        startActivity(settingsIntent);
                        break;
                    case R.id.Markets:
                        Intent Intent2 = new Intent(User_list.this, Market_list.class);
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
                    UsersAdapter myAdapter = new UsersAdapter(users, User_list.this);
                    usersRecycler.setAdapter(myAdapter);
                } else {
                    // else we are going to show just the meals that conferms to the query
                    List<User> filteredUsers = new ArrayList<>();
                    for (User c : users) {
                        if (c.getNom().toLowerCase().contains(query.toString().toLowerCase()))
//                                ||
//                                c.getPrenomMeal().toLowerCase().contains(query.toString().toLowerCase()) ||
//                                c.getServiceMeal().toLowerCase().contains(query.toString().toLowerCase()))
                        {
                            filteredUsers.add(c);
                        }
                    }
                    UsersAdapter myAdapter = new UsersAdapter(filteredUsers, User_list.this);
                    usersRecycler.setAdapter(myAdapter);
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
        if(view.getId()==R.id.logout_button){
            mAuth.signOut();
            Intent intent = new Intent(this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (view.getId()==R.id.Button_add) {
            Intent myintent= new Intent(this, Ajouter_contact.class);
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
    void getUsers() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference docRef = db.collection("Admin").document("admin1@gmail.com");
        docRef.collection("user").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    users.clear(); // Clear the meals list before populating it
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String dateNaiss = document.getString("dateNaiss");
                        String email = document.getString("email");
                        String nom = document.getString("nom");
                        String password = document.getString("password");
                        String prenom = document.getString("prenom");
                        String Tel = document.getString("Tel");
                        String url = document.getString("url");

                        Log.i("nomsdfghjkhgfUser", dateNaiss+"  "+email+"  "+nom+"  "+password+"  "+prenom+"  "+prenom );
                        // Check if any required field is null
                        if (dateNaiss != null && email != null && nom != null && password != null && prenom != null  && url != null ) {
                            User c = new User(dateNaiss, email,nom, password, prenom, Tel, url);
                            Log.i("mesfeeveUsers", c.toString());
                            users.add(c);
                        }
                    }

                    // Perform the required operations on the meals list here,
                    // such as setting the adapter and updating the RecyclerView

                    Log.i("mesUsers", users.toString());
                    usersRecycler.setHasFixedSize(true);
                    myAdapter = new UsersAdapter(users, User_list.this);
                    usersRecycler.setAdapter(myAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(User_list.this);
                    usersRecycler.setLayoutManager(layoutManager);
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