package com.shubham1172.piedpiper;

import android.gesture.Prediction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MonitorActivity extends AppCompatActivity {

    private TextView LEDs[], temp, door, smoke; //member variables
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;

    private boolean started = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Sensor");

        progressBar = (ProgressBar)findViewById(R.id.monitor_progress_bar);
        LEDs = new TextView[3];
        LEDs[0] = (TextView)findViewById(R.id.led1);
        LEDs[1] = (TextView)findViewById(R.id.led2);
        LEDs[2] = (TextView)findViewById(R.id.led3);

        temp = (TextView)findViewById(R.id.temperature);
        door = (TextView)findViewById(R.id.door_status);
        smoke = (TextView)findViewById(R.id.smoke_status);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(started==false){
                    progressBar.setVisibility(ProgressBar.GONE);
                    started = true;
                }
                int count = -1;
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    count++;
                    Log.d("TAG", child.getKey().toString());
                    Log.d("TAG", child.getValue().toString());
                    if(count==0){   //AC
                        continue;
                    }
                    if(count==1){   //LED
                        String raw = child.getValue().toString();
                        raw = raw.substring(1, raw.length()-1);
                        String led[] = raw.split(",");
                        LEDs[0].setText(led[1]);
                        LEDs[1].setText(led[2]);
                        LEDs[2].setText(led[3]);
                    }else if(count==2){ //Door
                        if(child.getValue().toString().equals("1")){
                            door.setText("Closed");
                        }else{
                            door.setText("Open");
                        }
                    }else if(count==3){ ///Smoke
                        if(child.getValue().toString().equals("0"))
                            smoke.setText("Safe");
                        else
                            smoke.setText("Danger");
                    }else{  //Temperature
                        temp.setText(child.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
