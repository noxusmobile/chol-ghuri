package com.example.cholghuri;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MemoriesAdapter extends RecyclerView.Adapter<MemoriesAdapter.ViewHolder> {

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private String userID;
    private String tourID;
    private DatabaseReference databaseReference;

    private List<Upload> uploadList;
    private Context context;

    public MemoriesAdapter(List<Upload> uploadList, Context context , String tourID) {
        this.uploadList = uploadList;
        this.context = context;
        this.tourID = tourID;
    }

    @NonNull
    @Override
    public MemoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.memories_list_layout, viewGroup, false);

        return new MemoriesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MemoriesAdapter.ViewHolder viewHolder, int i) {


        final Upload currentUpload = uploadList.get(i);

        viewHolder.memoriesCaption.setText(currentUpload.getCaption());
        Picasso.with(context).load(currentUpload.getUploadURI()).fit().centerCrop().into(viewHolder.memoriesPic);

        viewHolder.menucard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(context, viewHolder.menucard);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_card, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.delete:
                                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                alert.setTitle("Delete Entry");
                                alert.setMessage("Are you sure you want to delete?");
                                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        databaseReference = firebaseDatabase.getReference().child("UserLIst").child(userID).child("TourList").child(tourID).child("MemoryList");
                                        StorageReference imageRef = firebaseStorage.getReferenceFromUrl(currentUpload.getUploadURI());
                                        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                databaseReference.child(currentUpload.getUploadID()).removeValue();
                                                Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                });
                                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                alert.show();
                                return true;
                            case R.id.update:
                                Intent intent = new Intent(context, UpdateMemories.class);
                                intent.putExtra("tourID", tourID);
                                intent.putExtra("UploadID", currentUpload.getUploadID());
                                intent.putExtra("Caption", currentUpload.getCaption());
                                intent.putExtra("UploadURI", currentUpload.getUploadURI());
                                context.startActivity(intent);
                                return true;
                            default:
                        }
                        return false;
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return uploadList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView memoriesPic;
        public TextView memoriesCaption;
        public ImageView menucard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            memoriesPic = itemView.findViewById(R.id.memoriesPic);
            memoriesCaption = itemView.findViewById(R.id.memoriesCaption);
            menucard = itemView.findViewById(R.id.menucard);

        }
    }
}
