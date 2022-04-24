package com.srp.startone;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "personal_notification";
    public static final int NOTIFICATION_ID = 101;

    EditText phonenumber, message;
    Button book;
    // creating a variable for
    // our Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our
    // Database Reference for Firebase.
    DatabaseReference databaseReference;

    // variable for Text view.
    private TextView retrieveTV;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // below line is used to get the instance
        // of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get
        // reference for our database.
        databaseReference = firebaseDatabase.getReference("Data");

        // initializing our object class variable.
        retrieveTV = findViewById(R.id.idTVRetrieveData);

        // calling method
        // for getting data
        getdata();
    }
    public void createNotificationchannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Personal Notifications";
            String description = "Include all notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
    public void headnote(){
        createNotificationchannel();
            Intent landingingintent = new Intent(this,MainActivity.class);
            landingingintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent landingPendingIntent = PendingIntent.getActivity(this,0,landingingintent,PendingIntent.FLAG_ONE_SHOT);

            Intent yesIntent = new Intent(this,MainActivity.class);
            yesIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent yesPendingIntent = PendingIntent.getActivity(this,0,yesIntent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_sms_notification);
        builder.setContentTitle("simple Notification");
        builder.setContentText("this is notification");
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setAutoCancel(true);
        builder.setContentIntent(landingPendingIntent);
        builder.addAction(R.drawable.ic_sms_notification,"yes",yesPendingIntent);
        //builder.addAction(R.drawable.ic_sms_notification,"no",noPendingIntent);
        Toast.makeText(getApplicationContext(), "action required", Toast.LENGTH_LONG).show();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());
    }

    private void getdata() {
        // calling add value event listener method
        // for getting the values from database.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // this method is call to get the realtime
                // updates in the data.
                // this method is called when the data is
                // changed in our Firebase console.
                // below line is for getting the data from
                // snapshot of our database.
                String value = snapshot.getValue(String.class);
                // after getting the value we are setting
                // our value to our text view in below line.
                retrieveTV.setText(value);
                assert value != null;
                if (value.equals("296")) {
                    retrieveTV.setText("96");
                    headnote();
                    book = findViewById(R.id.button);
                    phonenumber = findViewById(R.id.editText);
                    message = findViewById(R.id.editText2);
                    book.setOnClickListener(view -> {
                        String number = phonenumber.getText().toString();
                        String msg = message.getText().toString();
                        try {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(number, null, msg, null, null);
                            Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Some fields are Empty", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(MainActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();

            }
        });
    }
}