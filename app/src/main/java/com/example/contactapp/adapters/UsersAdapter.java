package com.example.contactapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.contactapp.Details_User;
import com.example.contactapp.Model.User;
import com.example.contactapp.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;



public class UsersAdapter  extends RecyclerView.Adapter<UsersAdapter.MyViewHolder>{
    private List<User> users;


    private List<User> filteredUserList;
    FirebaseDatabase database;
    private Context context;
    // Provide a suitable constructor (depends on the kind of dataset)
    public UsersAdapter(List<User> users, Context context) {
        this. users = new ArrayList<User>() ;
        this. users.addAll( users );
        this.context=context;
        this.filteredUserList = new ArrayList<>(users);
    }

    // Create new views (invoked by the layout manager)

    @Override
    // -Me-   onCreateViewHolder() crée une vue et la renvoie
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View userItemLayoutView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_layout,
                        parent, false);
        MyViewHolder vhUsers = new MyViewHolder(userItemLayoutView);

        return vhUsers;
    }
    // -Me- onBindViewHolder() associe les données au détenteur de la vue pour une position donnée dans RecyclerView.
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.user = users.get(position);
        holder.User_Name.setText(users.get(position).getNom() +" "+ users.get(position).getPrenom());
        holder.user_Descrip.setText(users.get(position).getTel());
//        holder.ecoScore.setText(meals.get(position).get());
        holder.user_Email.setText(users.get(position).getEmail());
    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(users.get(position).getUrl());
        Glide.with(context)
                .load(storageReference)
                .into(holder.user_photo);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return users.size();
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    //   -ME- l'ajout de  implements View.OnClickListener nous permet d'avoir un onClickListner
    //   a l'interieur de MyViewHolder class
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnLongClickListener {
        User user;
        public TextView User_Name;
        public TextView user_Descrip;
        public TextView ecoScore;
        public TextView user_Email;
        public ImageView user_photo;

        // Context is a reference to the activity that contain the the recycler view
        //we should change the constractor so that he's going to accept an onMealListener
        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            User_Name = itemLayoutView.findViewById(R.id.User_Name);
            user_Descrip = itemLayoutView.findViewById(R.id.user_Descrip);
            user_Email = itemLayoutView.findViewById(R.id.user_Email);
            user_photo = itemLayoutView.findViewById(R.id.user_photo);

            //Now we want to attach that onclickListener to the entire ViewHolder
            // "this" that we have it referes to the interface onclickListener qu'on a implementer
            itemLayoutView.setOnClickListener(this);
            itemLayoutView.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            if(pos != RecyclerView.NO_POSITION) {
                Intent myintent = new Intent(context, Details_User.class);
                myintent.putExtra("user", users.get(pos));
                Log.i("ONEContacttttt", String.valueOf(users.get(pos)));
                context.startActivity(myintent);
            }

        }


        @Override
        public boolean onLongClick(View view) {
            Log.i("fvdfvdfvdfv", "fdfvdfv");
            // Handle long click
            int pos = getAdapterPosition();
            if(pos != RecyclerView.NO_POSITION) {
                PopupMenu popup = new PopupMenu(context, view , Gravity.END);
                try {
                    Method method = popup.getMenu().getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
                    method.setAccessible(true);
                    method.invoke(popup.getMenu(), true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                popup.inflate(R.menu.popup_menu);
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.call:
                                //handle menu1 click
                                Log.i("kklc", "dwc");
                                Toast.makeText(context, "StartActiviy 1", Toast.LENGTH_SHORT).show();
                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                callIntent.setData(Uri.parse("tel:" + user.getTel()));//change the number
                                Log.i("phoneNbrrrrr", String.valueOf(user.getTel()));
                                context.startActivity(callIntent);
                                return true;
                            case R.id.sms:
                                Uri uri = Uri.parse("smsto:"+ user.getTel());
                                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                                intent.putExtra("sms_body", "Hello, I hope you're doing well. I want to ");
                                context.startActivity(intent);
                                return true;
                            case R.id.email:
                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                                emailIntent.setData(Uri.parse("mailto:" + user.getEmail())); // only email apps should handle this
                                context.startActivity(emailIntent);
                                return true;
                            case R.id.whatsapp:
                                Uri uuri = Uri.parse("smsto:"+  user.getTel());
                                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uuri);
                                sendIntent.setPackage("com.whatsapp");
                                context.startActivity(sendIntent);
                                return true;
                            default:
                                return false;
                        }
                    }
                });

            }
            // Return true to indicate the click was handled
            return true;
        }

    }

    }



