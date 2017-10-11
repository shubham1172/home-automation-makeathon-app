package com.shubham1172.piedpiper;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class HomeScreen extends AppCompatActivity {

    private final static String TAG_HOME = "HOMESCREEN";
    private final static int RC_SPEECH = 1;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        welcomeText = (TextView)findViewById(R.id.welcome_text);
        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String welcome = welcomeText.getText()+"<b>"+ username.substring(0, username.indexOf(" " ))+"</b>!";
        welcomeText.setText(Html.fromHtml(welcome));
        //Alert manager
    }

    public void speakClick(View v){
        Log.d(TAG_HOME, "Clicked!");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        startActivityForResult(intent, RC_SPEECH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SPEECH){
            if(resultCode==RESULT_OK){
                ArrayList dataList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                uploadQuery(dataList.get(0).toString());
                Toast.makeText(this, "Queried!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void signOut(View v){
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    public void actionClick(View v){
        switch (v.getId()){
            case R.id.home_led:
                //led view
                startActivity(new Intent(this, LEDActivity.class));
                break;
            case R.id.home_ac:
                //ac view
                startActivity(new Intent(this, ACActivity.class));
                break;
            case R.id.home_monitor:
                //monitor view
                startActivity(new Intent(this, MonitorActivity.class));
                break;
        }
    }

    private void uploadQuery(String message){
        mDatabaseReference.child("Query").push().setValue(message);
    }
}
