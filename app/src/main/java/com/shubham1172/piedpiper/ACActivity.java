package com.shubham1172.piedpiper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ACActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private final int OFF = 0;
    private final int ON = 1;
    private final int LOW = 3;
    private final int HIGH = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Sensor").child("AC");
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.on_ac:
                //on
                databaseReference.push().setValue(ON);
                break;
            case R.id.off_ac:
                //off
                databaseReference.push().setValue(OFF);
                break;
            case R.id.high_ac:
                //increase temperature
                databaseReference.push().setValue(HIGH);
                break;
            case R.id.low_ac:
                //decrease temperature
                databaseReference.push().setValue(LOW);
                break;
        }
    }
}
