package com.fmi.master_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Notes extends AppCompatActivity {

    //Initialize variable
    DrawerLayout drawerLayout;
    ImageView btMenu;
    RecyclerView recyclerView;

    //Notes
    ListView listView1;
    EditText inputText1;
    Button btnAdd, btnUpdate;

    ArrayList<String> foods = new ArrayList<String>();
    ArrayAdapter<String> myAdapter1;

    //Database variables
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Integer indexVal;
    String item;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        listView1 = (ListView) findViewById(R.id.listView1);
        btnAdd = (Button) findViewById(R.id.button1);
        btnUpdate = (Button) findViewById(R.id.button2);
        inputText1 = (EditText) findViewById(R.id.editText);

        //Setup listview
        //foods.add("Ham");
        //foods.add("Ham");

        myAdapter1 = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, foods);
        listView1.setAdapter(myAdapter1);

        //Initialize database & reference
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Data");
        
        //Create method
        getValue();

        //Add items
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String stringVal = inputText1.getText().toString().trim();

                //Initialize unique key
                String sKey = databaseReference.push().getKey();
                if (sKey != null){
                    databaseReference.child(sKey).child("value").setValue(stringVal);
                    inputText1.setText("");
                }


                //foods.add(stringVal);
                //myAdapter1.notifyDataSetChanged();

                //inputText1.setText("");

            }
        });

        //Select item
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item = adapterView.getItemAtPosition(i).toString() + " has been selected.";
                indexVal = i;
                Toast.makeText(Notes.this, item, Toast.LENGTH_SHORT).show();
            }
        });

        //Update item
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stringVal = inputText1.getText().toString();
                foods.set(indexVal,stringVal);
                myAdapter1.notifyDataSetChanged();
            }
        });

        //Delete item
        listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                item = adapterView.getItemAtPosition(i).toString() + " has been deleted.";
                Toast.makeText(Notes.this, item, Toast.LENGTH_SHORT).show();

                foods.remove(i);
                myAdapter1.notifyDataSetChanged();
                return true;
            }
        });


        //Assign variable
        drawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.bt_menu);
        recyclerView = findViewById(R.id.recycler_view);

        //Set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Set adapter
        recyclerView.setAdapter(new MainAdapter(this, MainActivity.arrayList));

        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Open drawer
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void getValue() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foods.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    //Get children
                    String sValue = dataSnapshot.child("value").getValue(String.class);
                    //Add value to array list
                    foods.add(sValue);
                }
                listView1.setAdapter(myAdapter1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Display toast
                Toast.makeText(getApplicationContext(),
                        error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Close drawer
        MainActivity.closeDrawer(drawerLayout);
    }
}