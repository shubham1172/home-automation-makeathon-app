package com.shubham1172.piedpiper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by shubham1172 on 10/10/17.
 */

public class LEDAdapter extends ArrayAdapter<String> {

    private static String TAG_LED_ADAPTER = "LED_ADAPTER";

    private Context context;
    private String[] values;
    private int[] seekValues;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public LEDAdapter(Context context, String[] values, final int[] seekValues) {
        super(context, R.layout.led_item, values);
        this.context = context;
        this.values = values;
        this.seekValues = seekValues;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Sensor").child("Lighting");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG_LED_ADAPTER, "Child changed");
                int count = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    seekValues[count] = Integer.parseInt(child.getValue().toString());
                    count++;
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.led_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.led_name);
        SeekBar seekBar = (SeekBar) rowView.findViewById(R.id.led_seek);
        seekBar.setProgress(seekValues[position]);
        seekBar.setMax(100);
        textView.setText(values[position]);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                databaseReference.child(Integer.toString(position + 1)).setValue(seekBar.getProgress());
            }
        });
        return rowView;
    }

}
