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
import android.widget.TextView;

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

import static com.palanim.botcontroller.setupArena.toStringMine;

public class arenaAdv extends AppCompatActivity {

    TextView arena;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arena_adv);
arena=findViewById(R.id.arena);
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
                arena.setText(contents);
                String[] lines = contents.split(System.getProperty("line.separator"));

                for (int i=0; i< lines.length; i++){
                    Log.i("mine1222222333333", "Arena File found" + lines[i] );
                    int[] result = Arrays.stream(lines[i].split(" ")).mapToInt(Integer::parseInt).toArray();
                    for(int j=0; j<result.length; j++){
                    //    OGMatint[i][j]=result[j];
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
             *
             * File is not written from scratch
             *
             * ******
             */





        }
    }

    public void delButton(View v){

        /*
         ******
         *
         *        \/\/\/\/      IMPORTANT NOTICE TO DEV/ READER          \/\/\/\/
         *
         * Replacing arenaMatrix with emptty file
         * This removes all obstacles
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



    }

    public void delStops(View v){

        /*
         ******
         *
         *        \/\/\/\/      IMPORTANT NOTICE TO DEV/ READER          \/\/\/\/
         *
         * Replacing Arena Stops with Empty File
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos2 != null) {
                try {
                    fos2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}