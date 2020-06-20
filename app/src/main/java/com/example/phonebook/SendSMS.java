package com.example.phonebook;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class SendSMS extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private Button btnSend, btnBack;
    private EditText Phone, Message;

    boolean isValidMobile(String number, String expression) throws PatternSyntaxException {
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();

    }


    private void checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, getString(R.string.permission_not_granted));
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            enableSmsButton();
        }
    }

    public void smsSendMessage() {
        try {
            Phone = findViewById(R.id.Phone);
            String number = Phone.getText().toString();
            Message = findViewById(R.id.Message);
            String message = Message.getText().toString();
            if(message==""){
                throw new Exception("Please, enter message!");
            }
            if(number=="" && !isValidMobile(number,"\"([\\\\d]){2,}[\\\\.\\\\-]?\"")){
                throw new Exception("Please,enter corect phone number!");
            }
            SmsManager smsManager=SmsManager.getDefault();
            if(smsManager==null){
                Toast.makeText(getApplicationContext(), "No Service!", Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(getApplicationContext(), SendSMS.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
            checkForSmsPermission();
            smsManager.sendTextMessage(number, null, message, pendingIntent, null);
            Toast.makeText(getApplicationContext(), "Message sent successfully!", Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Exception: "+e.getLocalizedMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (permissions[0].equalsIgnoreCase(Manifest.permission.SEND_SMS)
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableSmsButton();
                } else {
                    Log.d(TAG, getString(R.string.failure_permission));
                    Toast.makeText(getApplicationContext(), "Accept permission!", Toast.LENGTH_LONG).show();
                    disableSmsButton();
                }
            }
        }
    }

    private void disableSmsButton() {
        Toast.makeText(getApplicationContext(), "Accept permission!", Toast.LENGTH_LONG).show();
       btnSend =  findViewById(R.id.btnSend);
        btnSend.setVisibility(View.INVISIBLE);
       btnBack =  findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SendSMS.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void enableSmsButton() {
        btnSend =  findViewById(R.id.btnSend);
        btnSend.setVisibility(View.VISIBLE);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsSendMessage();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_s_m_s);

        checkForSmsPermission();

    }
}



