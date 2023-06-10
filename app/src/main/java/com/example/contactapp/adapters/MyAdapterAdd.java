package com.example.contactapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.contactapp.Liste_favoris;
import com.example.contactapp.Model.Contact;
import com.example.contactapp.Model.produit;
import com.example.contactapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MyAdapterAdd  extends RecyclerView.Adapter<MyAdapterAdd.MyViewHolder> {
    private List<Contact> contacts;
    private List<Contact> filteredList;
    FirebaseDatabase database;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Context context;
    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapterAdd(List<Contact> contacts, Context context) {
        this. contacts = new ArrayList<Contact>() ;
        this. contacts.addAll( contacts );
        this.context=context;
        this.filteredList = new ArrayList<>(contacts);

    }

    // Create new views (invoked by the layout manager)


    // -Me-   onCreateViewHolder() crée une vue et la renvoie
    @NonNull
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_layout_add,
                        parent, false);
        MyViewHolder vh = new MyViewHolder(itemLayoutView);

        return vh;
    }





    // -Me- onBindViewHolder() associe les données au détenteur de la vue pour une position donnée dans RecyclerView.
    // Replace the contents of a view (invoked by the layout manager)

    public void onBindViewHolder(MyViewHolder holder, int position) {
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        //Recuperer Poids
        final String path = "weight" + (position + 1); // Génère le chemin dynamiquement

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(path)) {
                    String weight = dataSnapshot.child(path).getValue().toString();
                    float weightValue = Float.parseFloat(weight);
                    int weightInteger = Math.round(weightValue);
                    if (weightInteger > 1)
                    {
                        int weightIntegerCalibration = weightInteger - 1;
                        holder.poids.setText(weightIntegerCalibration + " g");

                    }else {
                        holder.poids.setText(weightInteger + " g");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Erreur de lecture de la base de données: " + databaseError.getMessage());
            }
        });

        holder.contact = contacts.get(position);
        holder.identification.setText(contacts.get(position).getNomContact());
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(contacts.get(position).getImg_url());
        Glide.with(context)
                .load(storageReference)
                .into(holder.photo);
    }

    // Return the size of your dataset (invoked by the layout manager)

    public int getItemCount() {
        return contacts.size();
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    //   -ME- l'ajout de  implements View.OnClickListener nous permet d'avoir un onClickListner
    //   a l'interieur de MyViewHolder class
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnLongClickListener{
        Contact contact;
        public TextView identification;

        public TextView poids;
        //        public TextView service;
        public ImageView photo;
        public TextView ingredName;
        public ImageView ingred_photo;

        // Context is a reference to the activity that contain the the recycler view
        //we should change the constractor so that he's going to accept an onContactListener
        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            identification =itemLayoutView.findViewById(R.id.identification);
            poids =itemLayoutView.findViewById(R.id.poids);
//            service =itemLayoutView.findViewById(R.id.service);
            photo= itemLayoutView.findViewById(R.id.contact_photo);

            //Now we want to attach that onclickListener to the entire ViewHolder
            // "this" that we have it referes to the interface onclickListener qu'on a implementer
            itemLayoutView.setOnClickListener(this);
            itemLayoutView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                DocumentReference documentReference;
                db = FirebaseFirestore.getInstance();
                mAuth = FirebaseAuth.getInstance();

                FirebaseUser currentUser = mAuth.getCurrentUser();

                documentReference = db.collection("Admin").document("admin1@gmail.com").collection("user").document(currentUser.getEmail());
                String doc = contacts.get(pos).getNomContact();

                // Vérifier si le document existe déjà
                documentReference.collection("ShoppingList").document(doc).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (!document.exists()) {
                                        // L'élément n'existe pas encore, nous pouvons l'ajouter
                                        produit p = new produit();
                                        p.setNom(contacts.get(pos).getNomContact());
                                        p.setUrl(contacts.get(pos).getImg_url());

                                        documentReference.collection("ShoppingList").document(doc).set(p)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(context, "Produit ajouter a votre shopping liste", Toast.LENGTH_SHORT).show();
                                                            Intent intent1 = new Intent(context, Liste_favoris.class);
                                                            context.startActivity(intent1);
                                                        } else {
                                                            // Gérer l'échec de l'ajout du document
                                                        }
                                                    }
                                                });
                                    } else {

                                        Toast.makeText(context, "Produit deja exist dans votre shopping liste", Toast.LENGTH_SHORT).show();
                                        Intent intent1 = new Intent(context, Liste_favoris.class);
                                        context.startActivity(intent1);
                                    }
                                } else {
                                    // Gérer l'échec de la récupération du document existant
                                }
                            }
                        });
            }

        }

        @Override
        public boolean onLongClick(View view) {

            return true;
        }

    }




}
