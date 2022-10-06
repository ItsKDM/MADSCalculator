package com.example.kdm.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewHistory extends AppCompatActivity {

    ArrayList<History> historyData;
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    History history;
    ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        firebaseAuth = FirebaseAuth.getInstance();
        historyData = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerviewHistoryData);
        progressDialog = new ProgressDialog(ViewHistory.this);

        databaseReference = FirebaseDatabase.getInstance().getReference("History").child(firebaseAuth.getUid());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(ViewHistory.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(historyData, ViewHistory.this);
        recyclerView.setAdapter(recyclerViewAdapter);
        progressDialog.setMessage("Wait...");
        progressDialog.show();

        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemsnapshot:snapshot.getChildren())
                {
                    history=itemsnapshot.getValue(History.class);
                    historyData.add(history);
                    if(history==null)
                    {
                        Toast.makeText(ViewHistory.this, "No History", Toast.LENGTH_SHORT).show();
                    }
                }
                Toast.makeText(ViewHistory.this, "Size : "+historyData.size(), Toast.LENGTH_SHORT).show();
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerViewAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewHistory.this, "Cancelled : "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}