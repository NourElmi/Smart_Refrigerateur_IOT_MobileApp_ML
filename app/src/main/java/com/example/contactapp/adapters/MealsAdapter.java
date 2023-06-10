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
import com.example.contactapp.Meals_Details;
import com.example.contactapp.Model.Meal;
import com.example.contactapp.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;



public class MealsAdapter  extends RecyclerView.Adapter<MealsAdapter.MyViewHolder>{
    private List<Meal> meals;
    private List<Meal> filteredMealList;
    FirebaseDatabase database;
    private Context context;
    // Provide a suitable constructor (depends on the kind of dataset)
    public MealsAdapter(List<Meal> meals, Context context) {
        this. meals = new ArrayList<Meal>() ;
        this. meals.addAll( meals );
        this.context=context;
        this.filteredMealList = new ArrayList<>(meals);
    }

    // Create new views (invoked by the layout manager)

    @Override
    // -Me-   onCreateViewHolder() crée une vue et la renvoie
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View mealItemLayoutView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_item_layout,
                        parent, false);
        MyViewHolder vhMeals = new MyViewHolder(mealItemLayoutView);

        return vhMeals;
    }
    // -Me- onBindViewHolder() associe les données au détenteur de la vue pour une position donnée dans RecyclerView.
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        holder.meal = meals.get(position);
        holder.Meal_Name.setText(meals.get(position).getNomMeal());
        holder.meal_Descrip.setText(meals.get(position).getDescMeal());
//        holder.ecoScore.setText(meals.get(position).get());
        holder.meal_Time.setText(meals.get(position).getTimeNeeded());
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(meals.get(position).getImg_url());
        Glide.with(context)
                .load(storageReference)
                .into(holder.meal_photo);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return meals.size();
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    //   -ME- l'ajout de  implements View.OnClickListener nous permet d'avoir un onClickListner
    //   a l'interieur de MyViewHolder class
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnLongClickListener {
        Meal meal;
        public TextView Meal_Name;
        public TextView meal_Descrip;
        public TextView ecoScore;
        public TextView meal_Time;
        public ImageView meal_photo;

        // Context is a reference to the activity that contain the the recycler view
        //we should change the constractor so that he's going to accept an onMealListener
        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            Meal_Name = itemLayoutView.findViewById(R.id.Meal_Name);
            meal_Descrip = itemLayoutView.findViewById(R.id.meal_Descrip);
            ecoScore = itemLayoutView.findViewById(R.id.ecoScore);
            meal_Time = itemLayoutView.findViewById(R.id.meal_Time);
            meal_photo = itemLayoutView.findViewById(R.id.meal_photo);

            //Now we want to attach that onclickListener to the entire ViewHolder
            // "this" that we have it referes to the interface onclickListener qu'on a implementer
            itemLayoutView.setOnClickListener(this);
            itemLayoutView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                Intent myintent = new Intent(context, Meals_Details.class);
                myintent.putExtra("meal", meals.get(pos));
//                Intent myintent = new Intent(context, Detail_meal.class);
//                myintent.putExtra("meal", meals.get(pos));
//                Log.i("ONEMealtttt", String.valueOf(meals.get(pos)));
                context.startActivity(myintent);
            }
        }

        @Override
        public boolean onLongClick(View view) {
           return true;
        }

    }}




