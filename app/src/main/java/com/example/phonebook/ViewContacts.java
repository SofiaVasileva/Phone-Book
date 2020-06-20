package com.example.phonebook;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewContacts extends AppCompatActivity {
    public ListView listContact;
    private DataBase helper;
    private Button btnAdd,btnSend;


    protected void Contacts(){
        try {

            final DataBase helper = new DataBase(this);
            helper.create();
            Thread thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        listContact.clearChoices();
                        ArrayList<String> result = helper.AllContact ();
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_listview, R.id.textView, result);
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
            if(helper!=null)
                helper.closeDataBase();
            helper=null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contacts);

        btnAdd=findViewById(R.id.btnAdd);
        btnSend=findViewById(R.id.btnSend);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ViewContacts.this,AddContact.class);
                startActivity(intent);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ViewContacts.this,SendSMS.class);
                startActivity(intent);
            }
        });

        listContact=findViewById(R.id.listContacts);
        listContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String clicked="";
                TextView clickedText=view.findViewById(R.id.textView);
                clicked=clickedText.getText().toString();
                String[] elements=clicked.split("\t");
                String ID=elements[0];
                Intent intent=new Intent(ViewContacts.this, EditContact.class);
                Bundle bundle=new Bundle();
                bundle.putString("ID", ID);
                bundle.putString("Name", elements[1]);
                bundle.putString("Phone", elements[2]);
                bundle.putString("Description", elements[3]);
                bundle.putString("Category", elements[4]);
                intent.putExtras(bundle);
                startActivityForResult(intent, 200, bundle);
                Contacts();
            }

        });
        Contacts();
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
