package com.shubham1172.piedpiper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LEDActivity extends AppCompatActivity {

    private ListView listView;
    private ProgressBar progressBar;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led);
        progressBar = (ProgressBar)findViewById(R.id.progress_led);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Sensor").child("Lighting");
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int values[] = new int[3];
                int count = 0;
                for(DataSnapshot value: dataSnapshot.getChildren()){
                    values[count++] = Integer.parseInt(value.getValue().toString());
                }
                populateItems(values);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);
    }

    private void populateItems(int values[]){
        //led names
        String[] list = new String[3];
        for(int i=1;i<=3;i++){
            list[i-1] = "LED"+i;
        }
        //led values
        databaseReference.removeEventListener(valueEventListener);
        listView = (ListView) findViewById(R.id.led_list);
        listView.setAdapter(new LEDAdapter(this, list, values));
        progressBar.setVisibility(ProgressBar.GONE);
    }
}
