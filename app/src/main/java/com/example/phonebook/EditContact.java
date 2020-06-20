package com.example.phonebook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class EditContact extends AppCompatActivity {
    private EditText Name,Phone,Description,Category;
    private Button btnEdit, btnDelete, btnShow;
    private DataBase myHelper;
    String ID="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

         Name=findViewById(R.id.Name);
         Phone=findViewById(R.id.Phone);
         Description=findViewById(R.id.Description);
         Category=findViewById(R.id.Category);
         btnEdit=findViewById(R.id.btnEdit);
         btnDelete=findViewById(R.id.btnDelete);
         btnShow=findViewById(R.id.btnShow);

       Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            ID=bundle.getString("ID");
            Name.setText(bundle.getString("Name"));
            Phone.setText(bundle.getString("Phone"));
            Description.setText(bundle.getString("Description"));
            Category.setText(bundle.getString("Category"));
        }

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(EditContact.this,ViewContacts.class);
                startActivity(intent);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    myHelper=new DataBase(EditContact.this);

                    String name=Name.getText().toString();
                    String phone=Phone.getText().toString();
                    String description=Description.getText().toString();
                    String category=Category.getText().toString();

                    if(name==""||phone==""||description==""||category==""){
                        throw new Exception("Please, enter all data!");
                    }

                    myHelper.update(ID, name, phone, description,category);
                    Toast.makeText(getApplicationContext(), " Successful updated", Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Exception: "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }finally {
                    if(myHelper!=null)
                        myHelper.closeDataBase();
                    myHelper=null;
                }
                finishActivity(300);
                Intent i=new Intent(EditContact.this, ViewContacts.class);
                startActivity(i);
            }
        });

    btnDelete.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              try {
                  myHelper=new DataBase(EditContact.this);
                  myHelper.delete(ID);
                  Toast.makeText(getApplicationContext(), "Successful deleted", Toast.LENGTH_LONG).show();
              }catch (Exception e){
                  Toast.makeText(getApplicationContext(), "Exception: "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
              }finally {
                  if(myHelper!=null)
                      myHelper.closeDataBase();
                  myHelper=null;
              }
              finishActivity(300);
              Intent i=new Intent(EditContact.this, ViewContacts.class);
              startActivity(i);
          }
      });
    }
}
