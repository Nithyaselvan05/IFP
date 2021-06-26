package com.palanim.botcontroller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class setupArena extends AppCompatActivity {
    TextView optionsText;
    ImageView okay;
    TextView box1;
    TextView box2;
    ImageView add;


    int SIZE=5;
    int[][] OGMatint =    {    {  0,  0,  0,  0,  0},
            {  0,  0,  0,  0,  0},
            {  0,  0,  0,  0,  0},
            {  0,  0,  0,  0,  0},
            {  0,  0,  0,  0,  0},
    };
    int a=0;
    HashMap<String, List<Integer>> OGstopsint = new HashMap<String, List<Integer>>();

    public static String toStringMine (int[][] M) {
        String separator = " ";
        StringBuffer result = new StringBuffer();
        String mark1 ="\"";
        String openb = "{";
        String closeb = "}";



        // iterate over the first dimension
        for (int i = 0; i < M.length; i++) {
            // iterate over the second dimension
          //  result.append(openb);
            for(int j = 0; j < M[i].length; j++){
              //  result.append(mark1);
                result.append(M[i][j]);
             //   result.append(mark1);
                result.append(separator);
            }
            // remove the last separator
            result.setLength(result.length() - separator.length());
            // add a line break.
           // result.append(closeb);
            result.append("\n");
        }
        return result.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_arena);
        optionsText = (TextView) findViewById(R.id.optionView);
        box1 = (TextView) findViewById(R.id.box1);
        box2 = (TextView) findViewById(R.id.box2);
        add = (ImageView) findViewById(R.id.add);


        /*
         ******
         *
         *        \/\/\/\/      IMPORTANT NOTICE TO DEV/ READER          \/\/\/\/
         *
         * TRY TO OPEN ArenaMatrix File from internal Storage
         *
         *
         * This happens every time app is run.
         * The same file is read and modified again when needed in other activities
         *
         * ******
         */
        String filename = "arenaMatrix";


        FileInputStream fis = null;
        try {
            fis = getApplicationContext().openFileInput(filename);
            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                while (line != null) {
                    Log.i("LINES", line.toString() );
                    stringBuilder.append(line).append('\n');
                    line = reader.readLine();
                }
                Log.i("mine1", "Arena File found" );
            } catch (IOException e) {
                // Error occurred when opening raw file for reading.
            } finally {

                String contents = stringBuilder.toString();
                Log.i("mine1222222", "Arena File found" +contents );

                String[] lines = contents.split(System.getProperty("line.separator"));

                for (int i=0; i< lines.length; i++){
                    Log.i("mine1222222333333", "Arena File found" + lines[i] );
                    int[] result = Arrays.stream(lines[i].split(" ")).mapToInt(Integer::parseInt).toArray();
                    for(int j=0; j<result.length; j++){
                        OGMatint[i][j]=result[j];
                    }
                }
                /*
                for(int i=0; i<SIZE; i++ ){
                    for(int j=0; j<SIZE; j++){
                        Log.i("nums", Integer.toString(OGMatint[i][j]) + i+j);
                    }
                }*/



            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("mine1", " Arena File not found");



            /*
            ******
            *
            *        \/\/\/\/      IMPORTANT NOTICE TO DEV/ READER          \/\/\/\/
            *
            * ARENA FILE WAS NOT FOUND.
            * Hence Writing the ARENA FILE FROM SCRATCH.
            *
            * This happens only once when APP is installed.
            * The same file is read and modified again when needed in other activities
            *
            * ******
            */



            String WriteFile = "arenaMatrix";
         //   String[] response ={"a","b","c"};
            int[][] maze = {
                    {  0,  0,  0,  0,  0},
                    {  0,  0,  0,  0,  0},
                    {  0,  0,  0,  0,  0},
                    {  0,  0,  0,  0,  0},
                    {  0,  0,  0,  0,  0},
            };

            String text = toStringMine(maze);
            Log.i("this", text );
            FileOutputStream fos = null;
            try {
                fos = openFileOutput(WriteFile, MODE_PRIVATE);
                fos.write(text.getBytes());

                Log.i("this", "Saved to " + getFilesDir() );
            } catch (FileNotFoundException ee) {
                e.printStackTrace();
            } catch (IOException ee) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException ee) {
                        e.printStackTrace();
                    }
                }
            }



        }

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
             * Hence Writing the ARENA Stops FILE FROM SCRATCH.
             *
             * This happens only once when APP is installed.
             * The same file is read and modified again when needed in other activities
             *
             * This is an empty file created.
             * Stops can be added/deleted in Setup arena OPTION ONLY.
             *
             * ******
             */


            String WriteFileStops = "arenaStops";
            HashMap<String, List<Integer>> mazeStops = new HashMap<String, List<Integer>>();
            List<Integer> myList = new ArrayList<Integer>();
            myList.add(5);
            myList.add(7);
            mazeStops.put("_England", myList);
            List<Integer> myList1 = new ArrayList<Integer>();
            myList1.add(51);
            myList1.add(71);
            mazeStops.put("_USA", myList1);
            List<Integer> myList2 = new ArrayList<Integer>();
            myList2.add(53);
            myList2.add(73);
            mazeStops.put("_Dummy", myList);
            //  String response1 =null;
            String text = mazeStops.toString();
            FileOutputStream fos2 = null;
            try {
                fos2 = openFileOutput(WriteFileStops, MODE_PRIVATE);
                fos2.write(text.getBytes());

                Log.i("this", "Stops file Saved to " + getFilesDir() );
            } catch (FileNotFoundException ee) {
                e.printStackTrace();
            } catch (IOException ee) {
                e.printStackTrace();
            } finally {
                if (fos2 != null) {
                    try {
                        fos2.close();
                    } catch (IOException ee) {
                        e.printStackTrace();
                    }
                }
            }



        }

        /*

        ------------------- FILE WRITE and READ OVER ------------------

        Two Files:
                    1. arenaMatrix
                    2. arenaStops
        Other Files not in this activity:
                    1. robotName

         */




    }
    /*
     ******
     *
     *        \/\/\/\/      IMPORTANT NOTICE TO DEV/ READER          \/\/\/\/
     *
     * variable a is used to set which option was set to modify changes to arenaMatrix or arenaStops
     *
     * ******
     */


    public void dt(View v){

        a=1;
        optionsText.setText("Deprecated Tags");
        add.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.tags));
        box2.setEnabled(false);

    }
    public void obs(View v){

        a=2;
        optionsText.setText("Obstacles");
        add.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.obstacles));
        box2.setEnabled(true);

    }
    public void stops(View v){

        a=10;
        optionsText.setText("Stops");
        add.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.stops));
        box2.setEnabled(true);
    }
    public void liststps(View v){

        a=4;
        optionsText.setText("List of Stops");
       // add.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.stops));
       box2.setEnabled(true);
            Intent intent = new Intent(this, listStops.class);

            startActivity(intent);



    }


    /*
     ******
     *
     *        \/\/\/\/      IMPORTANT NOTICE TO DEV/ READER          \/\/\/\/
     *
     * Based on variable a, when add tick button is clicked appropriate change is effected in a duplicate matrix.
     * The duplicates ones are dupMatrix and dupStops
     *
     * We do this because we want to to avoid opening and reading internal files every time we make a change. That is time consuming.
     *
     * When Save Changes is clicked, the duplicate matrix is written over the permanent internal files.
     *
     * Below --ticked-- function does this adding work for us.
     *
     *
     *
     * ******
     */

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void ticked(View v){
        if(a==0){
            Toast.makeText(getApplicationContext(),"Select an option!",Toast.LENGTH_LONG).show();
        }
        else if(a==1){   /////*******Deprecated Tags
            Log.i("Hi", "hi");
                int num1 = Integer.parseInt(box1.getText().toString());
                if(num1 <= (SIZE*SIZE ) && num1>0){


                    int variable = Math.floorDiv(num1, SIZE);
                    int remainder = num1 % SIZE;
                    if(remainder!=0){
                        OGMatint[variable][num1-(variable*SIZE)-1]=100;
                    }
                    else{
                        OGMatint[variable-1][num1-((variable-1)*SIZE)-1]=100;
                    }

                   for(int i=0; i<SIZE; i++ ){
                        for(int j=0; j<SIZE; j++){
                            Log.i("nums from setup", Integer.toString(OGMatint[i][j]) +" "+ i+" "+ j);
                        }
                    }
                    Toast.makeText(getApplicationContext(),"Deprecated Tag added",Toast.LENGTH_LONG).show();
                }


        }
        else if(a==2){   /////*******Obstacles

            int num1 = Integer.parseInt(box1.getText().toString());
            int num2 = Integer.parseInt(box2.getText().toString());
            if(num1<= (SIZE*SIZE) && num1>0 && num2<= (SIZE *SIZE ) && num2>0){
                int obs=0;
                if(num2-num1<SIZE){
                    obs=num1+1;
                }
                else if(num2-num1 >= SIZE){
                    obs=num2-5;
                }
                Log.i("obstacle", Integer.toString(obs));

                int variable = Math.floorDiv(obs, SIZE);
                int remainder = obs % SIZE;
                if(remainder!=0){
                    OGMatint[variable][obs-(variable*SIZE)-1]=100;
                }
                else{
                    OGMatint[variable-1][obs-((variable-1)*SIZE)-1]=100;
                }
            }
            for(int i=0; i<SIZE; i++ ){
                for(int j=0; j<SIZE; j++){
                    Log.i("nums from setup", Integer.toString(OGMatint[i][j]) +" "+ i+" "+ j);
                }
            }
            Toast.makeText(getApplicationContext(),"Obstacle added!",Toast.LENGTH_LONG).show();

        }
        else if(a==3){   /////*******stops
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
               //     OGMatint[variable-1][num1-((variable-1)*SIZE)-1]=100;
                    xcoo=variable-1;
                    ycoo=num1-((variable-1)*SIZE)-1;
                }

                for(int i=0; i<SIZE; i++ ){
                    for(int j=0; j<SIZE; j++){
                        Log.i("nums from setup", Integer.toString(OGMatint[i][j]) +" "+ i+" "+ j);
                    }
                }
                Toast.makeText(getApplicationContext(),"Stop added",Toast.LENGTH_LONG).show();
            }

            myList.add(xcoo);
            myList.add(ycoo);
            OGstopsint.put("_"+name, myList);
            Toast.makeText(getApplicationContext(),"Stop added",Toast.LENGTH_LONG).show();

            Log.i("Hash added", OGstopsint.toString());
        }

    }

    public void submit(View v){


        /*
         ******
         *
         *        \/\/\/\/      IMPORTANT NOTICE TO DEV/ READER          \/\/\/\/
         *
         * Saving the final changes made as arenaStops
         *
         * ******
         */


        String WriteFileStops = "arenaStops";

        String text = OGstopsint.toString();
        FileOutputStream fos2 = null;
        try {
            fos2 = openFileOutput(WriteFileStops, MODE_PRIVATE);
            fos2.write(text.getBytes());

            Log.i("this", "Stops file Saved to " + getFilesDir() );
        } catch (FileNotFoundException ee) {
            ee.printStackTrace();
        } catch (IOException ee) {
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


        /*
         ******
         *
         *        \/\/\/\/      IMPORTANT NOTICE TO DEV/ READER          \/\/\/\/
         *
         *Saving the changes made to final document as arenaMatrix
         *
         * ******
         */



        String WriteFile = "arenaMatrix";

        String texttoWrite = toStringMine(OGMatint);
        Log.i("thisssssss", texttoWrite );
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(WriteFile, MODE_PRIVATE);
            fos.write(texttoWrite.getBytes());

            Log.i("this", "Saved to " + getFilesDir() );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ee) {
            ee.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
            }
        }
        Toast.makeText(getApplicationContext(),"Changes Saved",Toast.LENGTH_LONG).show();
    }




    }
