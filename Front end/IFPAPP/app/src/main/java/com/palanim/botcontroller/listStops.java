package com.palanim.botcontroller;

import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class listStops extends AppCompatActivity {
    ListView stops;
    TextView targetView;
    String toDelTarget;
    int SIZE=5;
    TextView box1;
    TextView box2;
    ImageView add;
    HashMap<String, List<Integer>> OGstopsint = new HashMap<String, List<Integer>>();
   // String countryList[] = {"Master Bedroom", "BedRoom2", "Lavatory", "Kitchen", "Store Room", "Balcony", "Verandah", "Dining Hall", "Hall"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_stops);
        targetView=(TextView)findViewById(R.id.target);
        stops = (ListView)findViewById(R.id.stops);
     //   ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , countryList);
     //   stops.setAdapter(arrayAdapter);
        box1 = (TextView) findViewById(R.id.box1);
        box2 = (TextView) findViewById(R.id.box2);
        add = (ImageView) findViewById(R.id.add);
        stops.setOnItemClickListener(this::onItemClick);

        /*
         ******
         *
         *        \/\/\/\/      IMPORTANT NOTICE TO DEV/ READER          \/\/\/\/
         *
         *  Try to read Stops List from internal Storage
         *  File name is     arenaStops
         *
         * This happens every time app is installed.
         * The same file is read and modified again when needed in other activities
         *
         * ******
         */

        String filenameStops = "arenaStops";


        FileInputStream fis1 = null;
        try {
            fis1 = getApplicationContext().openFileInput(filenameStops);
            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis1, StandardCharsets.UTF_8);
            StringBuilder stringBuilder1 = new StringBuilder();
            try (BufferedReader reader1 = new BufferedReader(inputStreamReader)) {
                String line = reader1.readLine();
                while (line != null) {
                    stringBuilder1.append(line).append('\n');
                    line = reader1.readLine();
                }
                Log.i("mine1", "Arena File found" );
            } catch (IOException e) {
                // Error occurred when opening raw file for reading.
            } finally {
                String contents = stringBuilder1.toString();
                Log.i("mineStops", "Arena Stops File found" + contents );

                String value = contents;
                value = value.substring(1, value.length()-1);           //remove curly brackets
                String[] keyValuePairs = value.split(", _");              //split the string to create key-value pairs
                HashMap<String, List<Integer>> map = new HashMap<>();

                for(String pair : keyValuePairs)                        //iterate over the pairs
                {
                    String[] entry = pair.split("=");            //split the pairs to get key and value
                    Log.i("hashu", entry[0] +"---"+ entry[1]);
                    String value1= entry[1];
                    Log.i("lenght", Integer.toString(value1.length()));
                    value1 = value1.substring(1, value1.length()-1);
                    Log.i("coords", value1.toString());//remove curly brackets
                    String[] coords = value1.split(", ");
                    coords[1] = coords[1].replace("{","");
                    coords[1] = coords[1].replace("}","");
                    coords[1] = coords[1].replace("]","");
                    coords[1] = coords[1].replace("[","");
                    Log.i("coords", coords.toString());
                    List<Integer> myList = new ArrayList<Integer>();
                    myList.add(Integer.parseInt(coords[0]));
                    myList.add(Integer.parseInt(coords[1]));
                    String toPut;
                    char comp = '_';
                    if( entry[0].charAt(0) != '_')
                        toPut="_"+ (entry[0].trim());
                    else
                        toPut=entry[0].trim();
                    OGstopsint.put(toPut, myList);          //add them to the hashmap and trim whitespaces
                    Log.i("Read stops as Hash", OGstopsint.toString());
                }


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("mine1", " Arena File not found");



            /*
             ******
             *
             *        \/\/\/\/      IMPORTANT NOTICE TO DEV/ READER          \/\/\/\/
             *
             * ARENA Stops FILE WAS NOT FOUND.
             * File is not written here from scratch
             * File has to be written in setupArena activity
             *
             * This happens only once when APP is installed.
             * The same file is read and modified again when needed in other activities
             *
             * This is an empty file created.
             * Stops can be added/deleted in List Stops arena OPTION ONLY.
             *
             * ******
             */

            Toast.makeText(getApplicationContext(),"Stops File not found. Setup first!",Toast.LENGTH_LONG).show();



        }
        ArrayList<String> NewStops = new ArrayList<>();
        for(String i : OGstopsint.keySet()){
            NewStops.add(i);

        }

        stops = (ListView)findViewById(R.id.stops);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , NewStops);
        stops.setAdapter(arrayAdapter);

        stops.setOnItemClickListener(this::onItemClick);

        };

    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Log.i("Target",      parent.getItemAtPosition(position).toString()   );
        targetView.setText(parent.getItemAtPosition(position).toString());
        toDelTarget =  parent.getItemAtPosition(position).toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void ticked(View v){
        int num1 = Integer.parseInt(box1.getText().toString());
        int xcoo=0;
        int ycoo= 0;
        String name = box2.getText().toString();
        List<Integer> myList = new ArrayList<Integer>();
        if(num1 <= (SIZE*SIZE ) && num1>0){


            int variable = Math.floorDiv(num1, SIZE);
            int remainder = num1 % SIZE;
            if(remainder!=0){
                //OGMatint[variable][num1-(variable*SIZE)-1]=100;
                xcoo=variable;
                ycoo=num1-(variable*SIZE)-1;
            }
            else{
          //      OGMatint[variable-1][num1-((variable-1)*SIZE)-1]=100;
                xcoo=variable-1;
                ycoo=num1-((variable-1)*SIZE)-1;
            }

            for(int i=0; i<SIZE; i++ ){
                for(int j=0; j<SIZE; j++){
               //     Log.i("nums from setup", Integer.toString(OGMatint[i][j]) +" "+ i+" "+ j);
                }
            }
            Toast.makeText(getApplicationContext(),"Stop added",Toast.LENGTH_LONG).show();
        }
        myList.add(ycoo);
        myList.add(xcoo);

        OGstopsint.put("_"+name, myList);
        Toast.makeText(getApplicationContext(),"Stop added",Toast.LENGTH_LONG).show();

        Log.i("Hash added", OGstopsint.toString());



    }

    public void delStop(View v){
        if(!toDelTarget.equals(""))
        OGstopsint.remove(toDelTarget);
    }

public void submit(View v){
    String WriteFileStops = "arenaStops";

    String text = OGstopsint.toString();
    FileOutputStream fos2 = null;
    try {
        fos2 = openFileOutput(WriteFileStops, MODE_PRIVATE);
        fos2.write(text.getBytes());

        Log.i("this", "Stops file Saved to " + getFilesDir() );
    } catch (
            FileNotFoundException ee) {
        ee.printStackTrace();
    } catch (
            IOException ee) {
        ee.printStackTrace();
    } finally {
        if (fos2 != null) {
            try {
                fos2.close();
            } catch (IOException ee) {
                ee.printStackTrace();
            }
        }
    }
}

    }
