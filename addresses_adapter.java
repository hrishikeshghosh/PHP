package com.example.ecommerce_03;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.ecommerce_03.delivery_activity.SELECT_ADDRESS;
import static com.example.ecommerce_03.my_account_fragment.MANAGE_ADDRESS;
import static com.example.ecommerce_03.my_adresses_activity.refresh_item;

public class addresses_adapter extends RecyclerView.Adapter<addresses_adapter.ViewHolder> {

    private List<addresses_model> addresses_modelList;
    private int MODE;
    private int pre_selected_position;
    private boolean refresh = false;
    private Dialog loading_dialog;

    public addresses_adapter(List<addresses_model> addresses_modelList, int MODE, Dialog loading_dialog) {
        this.addresses_modelList = addresses_modelList;
        this.MODE = MODE;
        pre_selected_position = db_querries.selected_address;
        this.loading_dialog = loading_dialog;

    }

    @NonNull
    @Override
    public addresses_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.addresses_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull addresses_adapter.ViewHolder viewHolder, int position) {
        String city = addresses_modelList.get(position).getCity();
        String locality = addresses_modelList.get(position).getLocality();
        String flat_no = addresses_modelList.get(position).getFlat_no();
        String pincode = addresses_modelList.get(position).getPincode();
        String landmark = addresses_modelList.get(position).getLandmark();
        String name = addresses_modelList.get(position).getName();
        String mobile_no = addresses_modelList.get(position).getMobile_no();
        String alternate_mobile_no = addresses_modelList.get(position).getAlternate_mobile_no();
        String state = addresses_modelList.get(position).getState();
        boolean selected = addresses_modelList.get(position).getSelected();


        viewHolder.setdata(name, city, pincode, selected, position, mobile_no, alternate_mobile_no, flat_no, locality, state, landmark);


    }

    @Override
    public int getItemCount() {
        return addresses_modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView fullname;
        private TextView address;
        private TextView pincode;
        private ImageView icon;
        private LinearLayout option_container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullname = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            pincode = itemView.findViewById(R.id.pincode);
            icon = itemView.findViewById(R.id.icon_view);
            option_container = itemView.findViewById(R.id.option_container);
        }

        private void setdata(String username, String city, String user_pincode, final boolean selected, final int position, String mobile_no, String alternate_mobile_no, String flat_no, String locality, String state, String landmark) {

            if (alternate_mobile_no.equals("")) {
                fullname.setText(username + " - " + mobile_no);
            } else {
                fullname.setText(username + " - " + mobile_no + " or " + alternate_mobile_no);
            }

            if (landmark.equals("")) {
                address.setText(flat_no + " " + locality + " " + city + " " + state);
            } else {
                address.setText(flat_no + " " + locality + " " + landmark + " " + city + " " + state);
            }


            pincode.setText(user_pincode);

            if (MODE == SELECT_ADDRESS) {

                icon.setImageResource(R.drawable.tick);
                if (selected) {
                    icon.setVisibility(View.VISIBLE);
                    pre_selected_position = position;
                } else {
                    icon.setVisibility(View.GONE);
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pre_selected_position != position) {
                            addresses_modelList.get(position).setSelected(true);
                            addresses_modelList.get(pre_selected_position).setSelected(false);
                            refresh_item(pre_selected_position, position);
                            pre_selected_position = position;
                            db_querries.selected_address = position;
                        }
                    }
                });

            } else if (MODE == MANAGE_ADDRESS) {

                option_container.setVisibility(View.GONE);
                option_container.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//////edit address

                        Intent add_address_intent = new Intent(itemView.getContext(), add_address_activity.class);
                        add_address_intent.putExtra("INTENT", "update_address");
                        add_address_intent.putExtra("INDEX", position);
                        itemView.getContext().startActivity(add_address_intent);
                        refresh = false;
                    }
                });
                option_container.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//////remove address
                        loading_dialog.show();
                        Map<String, Object> addresses = new HashMap<>();
                        int x = 0;
                        int selected = -1;
                        for (int i = 0; i < addresses_modelList.size(); i++) {

                            if (i != position) {
                                x++;
                                addresses.put("city" + x, addresses_modelList.get(i).getCity());
                                addresses.put("locality" + x, addresses_modelList.get(i).getLocality());
                                addresses.put("flat_no" + x, addresses_modelList.get(i).getFlat_no());
                                addresses.put("pincode" + x, addresses_modelList.get(i).getPincode());
                                addresses.put("landmark" + x, addresses_modelList.get(i).getLandmark());
                                addresses.put("name" + x, addresses_modelList.get(i).getName());
                                addresses.put("mobile_no" + x, addresses_modelList.get(i).getMobile_no());
                                addresses.put("alternate_mobile_no" + x, addresses_modelList.get(i).getAlternate_mobile_no());
                                addresses.put("state" + x, addresses_modelList.get(i).getState());

                                if (addresses_modelList.get(position).getSelected()) {
                                    if (position - 1 >= 0) {
                                        if (x == position) {
                                            addresses.put("selected" + x, true);
                                            selected = x;
                                        } else {
                                            addresses.put("selected" + x, addresses_modelList.get(i).getSelected());
                                        }
                                    } else {
                                        if (x == 1) {
                                            addresses.put("selected" + x, true);
                                            selected = x;
                                        } else {
                                            addresses.put("selected" + x, addresses_modelList.get(i).getSelected());
                                        }
                                    }
                                } else {
                                    addresses.put("selected" + x, addresses_modelList.get(i).getSelected());
                                    if (addresses_modelList.get(i).getSelected()) {
                                        selected = x;
                                    }
                                }
                            }

                        }
                        addresses.put("list_size", x);


                        final int finalSelected = selected;
                        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid())
                                .collection("USER_DATA").document("MY_ADDRESS").set(addresses)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            db_querries.addresses_modelList.remove(position);
                                            if (finalSelected != -1) {
                                                db_querries.selected_address = finalSelected - 1;
                                                db_querries.addresses_modelList.get(finalSelected - 1).setSelected(true);
                                            }else if(db_querries.addresses_modelList.size()==0){
                                                db_querries.selected_address=-1;
                                            }
                                            notifyDataSetChanged();

                                        } else {
                                            String error = task.getException().getMessage();
                                            Toast.makeText(itemView.getContext(), error, Toast.LENGTH_LONG).show();
                                        }
                                        loading_dialog.dismiss();
                                    }
                                });

                        refresh = false;
                    }
                });
                icon.setImageResource(R.drawable.vertical_dots);
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        option_container.setVisibility(View.VISIBLE);
                        if (refresh) {
                            refresh_item(pre_selected_position, pre_selected_position);
                        } else {
                            refresh = true;
                        }

                        pre_selected_position = position;
                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh_item(pre_selected_position, pre_selected_position);
                        pre_selected_position = -1;
                    }
                });

            }

        }
    }
}
