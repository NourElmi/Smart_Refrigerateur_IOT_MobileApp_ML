package com.example.contactapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.contactapp.Delete;
import com.example.contactapp.Model.Contact;
import com.example.contactapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MyAdapterDelete extends RecyclerView.Adapter<MyAdapterDelete.MyViewHolder> {
    private List<Contact> contacts;
    private List<Contact> filteredList;
    FirebaseDatabase database;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Context context;
    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapterDelete(List<Contact> contacts, Context context) {
        this. contacts = new ArrayList<Contact>() ;
        this. contacts.addAll( contacts );
        this.context=context;
        this.filteredList = new ArrayList<>(contacts);

    }

    // Create new views (invoked by the layout manager)

    @Override
    // -Me-   onCreateViewHolder() crée une vue et la renvoie
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_layout_delete,
                        parent, false);
        MyViewHolder vh = new MyViewHolder(itemLayoutView);

        return vh;
    }




    // -Me- onBindViewHolder() associe les données au détenteur de la vue pour une position donnée dans RecyclerView.
    // Replace the contents of a view (invoked by the layout manager)
    public void onBindViewHolder(MyViewHolder holder, int position) {
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();


        holder.contact = contacts.get(position);
        holder.identification.setText(contacts.get(position).getNomContact());
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(contacts.get(position).getImg_url());
        Glide.with(context)
                .load(storageReference)
                .into(holder.photo);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
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
//            poids =itemLayoutView.findViewById(R.id.poids);
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
            if(pos != RecyclerView.NO_POSITION) {
                //Intent myintent = new Intent(context, Meals_List.class);
                //myintent.putExtra("contact", contacts.get(pos));
////                Intent myintent = new Intent(context, Detail_contact.class);

////                Log.i("ONEContacttttt", String.valueOf(contacts.get(pos)));
//                context.startActivity(myintent);
                Intent Intent1 = new Intent(context, Delete.class);
                Intent1.putExtra("contact", contacts.get(pos));
                context.startActivity(Intent1);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            return true;
        }

    }




}