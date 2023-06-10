package com.example.contactapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.contactapp.Model.Ingredients;
import com.example.contactapp.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class InredAdapter extends RecyclerView.Adapter<InredAdapter.MyViewHolder>{
    private List<Ingredients> ingreds;
    private List<Ingredients> filteredIngredientsList;
    FirebaseDatabase database;
    private Context context;
    public InredAdapter(List<Ingredients> ingreds, Context context) {
        this. ingreds = new ArrayList<Ingredients>() ;
        this. ingreds.addAll( ingreds );
        this.context=context;
        this.filteredIngredientsList = new ArrayList<>(ingreds);
    }

    // Create new views (invoked by the layout manager)

    @Override
    // -Me-   onCreateViewHolder() crée une vue et la renvoie
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ingredItemLayoutView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.ingeredent_item_layout,
                        parent, false);
        MyViewHolder vhIngredientss = new MyViewHolder(ingredItemLayoutView);

        return vhIngredientss;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.ingred = ingreds.get(position);
        holder.ingredName.setText(ingreds.get(position).getNomIngred());
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(ingreds.get(position).getUrl_img_Ingred());
        Glide.with(context)
                .load(storageReference)
                .into(holder.ingred_photo);
    }

    @Override
    public int getItemCount() {
        return ingreds.size();
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    //   -ME- l'ajout de  implements View.OnClickListener nous permet d'avoir un onClickListner
    //   a l'interieur de MyViewHolder class
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnLongClickListener {
        Ingredients ingred;
        public TextView ingredName;
        public ImageView ingred_photo;

        // Context is a reference to the activity that contain the the recycler view
        //we should change the constractor so that he's going to accept an onIngredientsListener
        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            ingredName = itemLayoutView.findViewById(R.id.ingredName);
            ingred_photo = itemLayoutView.findViewById(R.id.ingred_photo);

            //Now we want to attach that onclickListener to the entire ViewHolder
            // "this" that we have it referes to the interface onclickListener qu'on a implementer
            itemLayoutView.setOnClickListener(this);
            itemLayoutView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
//                Intent myintent = new Intent(context, Ingredientss_Details.class);
//                myintent.putExtra("ingred", ingreds.get(pos));
////                Intent myintent = new Intent(context, Detail_ingred.class);
////                myintent.putExtra("ingred", ingreds.get(pos));
////                Log.i("ONEIngredientstttt", String.valueOf(ingreds.get(pos)));
//                context.startActivity(myintent);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            return true;
        }

    }}




