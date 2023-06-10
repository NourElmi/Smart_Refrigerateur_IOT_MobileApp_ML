package com.example.contactapp.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.contactapp.Model.Market;
import com.example.contactapp.Model.User;
import com.example.contactapp.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;



public class MarketsAdapter  extends RecyclerView.Adapter<MarketsAdapter.MyViewHolder>{
    private List<Market> markets;
    private List<Market> filteredMarketList;
    FirebaseDatabase database;
    private Context context;
    // Provide a suitable constructor (depends on the kind of dataset)
    public MarketsAdapter(List<Market> markets, Context context) {
        this. markets = new ArrayList<Market>() ;
        this. markets.addAll( markets );
        this.context=context;
        this.filteredMarketList = new ArrayList<>(markets);
    }

    // Create new views (invoked by the layout manager)

    @Override
    // -Me-   onCreateViewHolder() crée une vue et la renvoie
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View marketItemLayoutView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.market_item_layout,
                        parent, false);
        MyViewHolder vhMarkets = new MyViewHolder(marketItemLayoutView);

        return vhMarkets;
    }
    // -Me- onBindViewHolder() associe les données au détenteur de la vue pour une position donnée dans RecyclerView.
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.market = markets.get(position);
        holder.Market_Name.setText(markets.get(position).getNomProprietaire());
        holder.Market_loc.setText(markets.get(position).getLocalisation());
        holder.Market_ouv.setText(markets.get(position).getOuver());
        holder.Market_ferm.setText(markets.get(position).getFerm());
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(markets.get(position).getUrlMark());
        Glide.with(context)
                .load(storageReference)
                .into(holder.market_photo);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return markets.size();
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    //   -ME- l'ajout de  implements View.OnClickListener nous permet d'avoir un onClickListner
    //   a l'interieur de MyViewHolder class
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnLongClickListener {
        public Market market;
        User user;
        public TextView Market_Name;
        public TextView Market_loc;
        public TextView Market_ouv;
        public TextView Market_ferm;
        public ImageView market_photo;

        // Context is a reference to the activity that contain the the recycler view
        //we should change the constractor so that he's going to accept an onMealListener
        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            Market_Name = itemLayoutView.findViewById(R.id.Market_Name);
            Market_loc = itemLayoutView.findViewById(R.id.Market_loc);
            Market_ouv = itemLayoutView.findViewById(R.id.Market_ouv);
            Market_ferm = itemLayoutView.findViewById(R.id.Market_fer);
            market_photo = itemLayoutView.findViewById(R.id.market_photo);

            //Now we want to attach that onclickListener to the entire ViewHolder
            // "this" that we have it referes to the interface onclickListener qu'on a implementer
            itemLayoutView.setOnClickListener(this);
            itemLayoutView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View view) {

            Log.i("fvdfvdfvdfv", "fdfvdfv");
            // Handle long click
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                PopupMenu popup = new PopupMenu(context, view, Gravity.END);
                try {
                    Method method = popup.getMenu().getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
                    method.setAccessible(true);
                    method.invoke(popup.getMenu(), true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                popup.inflate(R.menu.popup_menu);
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.call:
                                //handle menu1 click
                                Log.i("kklc", "dwc");
                                Toast.makeText(context, "StartActiviy 1", Toast.LENGTH_SHORT).show();
                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
//                                callIntent.setData(Uri.parse("tel:" + ingred.getTel()));//change the number
//                                Log.i("phoneNbrrrrr", String.valueOf(ingred.getTel()));
//                                context.startActivity(callIntent);
                                return true;
                            case R.id.sms:
//                                Uri uri = Uri.parse("smsto:" + ingred.getTel());
//                                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
//                                intent.putExtra("sms_body", "Hello, I hope you're doing well. I want to ");
//                                context.startActivity(intent);
//                                return true;
                            case R.id.email:
//                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//                                emailIntent.setData(Uri.parse("mailto:" + ingred.getEmailIngredients())); // only email apps should handle this
//                                context.startActivity(emailIntent);
//                                return true;
                            case R.id.whatsapp:
//                                Uri uuri = Uri.parse("smsto:" + ingred.getTel());
//                                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uuri);
//                                sendIntent.setPackage("com.whatsapp");
//                                context.startActivity(sendIntent);
//                                return true;
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



