package com.example.phonebook;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.jar.Attributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class AddContact extends AppCompatActivity {

    private EditText Name,Phone,Description,Category;
    private Button btnAdd,btnShow;
    private DataBase myHelper;
    public ListView listContact;

    protected void Contacts(){
        try {

            final DataBase myHelper = new DataBase(this);
            myHelper.create();
            Thread thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        listContact.clearChoices();
                        ArrayList<String> results = myHelper.AllContact ();
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_listview, R.id.textView, results);
                        synchronized (listContact){
                            listContact.setAdapter(arrayAdapter);
                        }

                    }catch (Exception e){

                    }
                }
            });
            thread.start();
        }catch (Exception e) {
            Toast.makeText(this, "Exception: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
        finally {
            if(myHelper!=null)
                myHelper.closeDataBase();
            myHelper=null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        Name=findViewById(R.id.Name);
        Phone=findViewById(R.id.Phone);
        Description=findViewById(R.id.Description);
        Category=findViewById(R.id.Category);
        btnAdd=findViewById(R.id.btnAdd);
        btnShow=findViewById(R.id.btnShow);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    myHelper=new DataBase(AddContact.this);
                    String name=Name.getText().toString();
                    String phone=Phone.getText().toString();
                    String description=Description.getText().toString();
                    String category=Category.getText().toString();
                    if(name==""||phone==""||description==""||category==""){
                        throw new Exception("Please, enter all data!");
                    }

                    myHelper.insert( name, phone, description,category);
                    Toast.makeText(getApplicationContext(), "Successful Added", Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Exception: "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }finally {
                    if(myHelper!=null)
                        myHelper.closeDataBase();
                    myHelper=null;
                }
               Contacts();

            }
        });

        Contacts();

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AddContact.this,ViewContacts.class);
                startActivity(intent);
            }
        });
    }

    @Override
    @CallSuper
    protected void onActivityResult(int RequestCode, int resultCode, Intent data){
        super.onActivityResult(RequestCode, resultCode, data);
        try{
           Contacts();
        }catch (Exception e){

        }
    }
}
