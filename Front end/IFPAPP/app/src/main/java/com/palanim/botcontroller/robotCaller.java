package com.palanim.botcontroller;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class robotCaller extends AppCompatActivity {

    ListView stops;
    TextView targetView;
    String finalTarget;
    String ESPRESPONSE = " " ;
    int ESPRes = 0;
    int RFIDReadFlag = 0;
    String arenaOP = null;
    int SIZE=5;
    int[][] OGMatint =    {    {  0,  0,  0,  0,  0},
            {  0,  0,  0,  0,  0},
            {  0,  0,  0,  0,  0},
            {  0,  0,  0,  0,  0},
            {  0,  0,  0,  0,  0},
    };
    int a=0;
    HashMap<String, List<Integer>> OGstopsint = new HashMap<String, List<Integer>>();
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_caller);


        targetView=(TextView)findViewById(R.id.target);



        /*

        --------------------DOCUMENTATION TEXT-------------------------
            \/\/\/\/ READING ARENA MATRIX FROM INTERNAL FILE STORAGE \/\/\/\/

            File name is arenaMatrix

            If file is not found, Click Setup Arena First

            File is not setup here. Only read.

        */

        /*
         ******
         *
         *        \/\/\/\/      IMPORTANT NOTICE TO DEV/ READER          \/\/\/\/
         *
         * TRY TO OPEN ArenaMatrix File from internal Storage
         *
         *

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



        }


        /*

        ****** DEVELOPER NOTICE
        Code that follows will read arenaStops and store it as a hashmap on global variable


         */

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

    static class Node implements Comparable {
        public AStar.Node parent;
        public int x, y;
        public double g;
        public double h;
        Node(AStar.Node parent, int xpos, int ypos, double g, double h) {
            this.parent = parent;
            this.x = xpos;
            this.y = ypos;
            this.g = g;
            this.h = h;
        }
        // Compare by f value (g + h)
        @Override
        public int compareTo(Object o) {
            AStar.Node that = (AStar.Node) o;
            return (int)((this.g + this.h) - (that.g + that.h));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Log.i("Target",      parent.getItemAtPosition(position).toString()   );
        targetView.setText(parent.getItemAtPosition(position).toString());

        finalTarget =  parent.getItemAtPosition(position).toString();
    }

    /*
    -----------DOCUMENTATION------------

    \/\/\/\/ MAIN FUCNTION that finds path when clicked \/\/\/\/




     */


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void GO(View v){


        /*****
         *
         * --------------------------- DOCUMENTATION to DEV -----------------------------
         *
         *  Workflow of request ack model communication between ESP 8266 and Android App using REST API
         *
         *
         *
         *  1. Android Sends "NEW" request
         *  2. ESP keeps reading data from server.
         *  3. If ESP finds "NEW" from android, it sends back first two RFID Tags
         *  4. this data is collected by android and new path is calculated and PUT
         *  5. ESP waits for new response and on receiving data sends it to Arduino
         *  6. In case wrong Tag is encountered, ESP sends that data to server with title "lost"
         *  7. Android App waits for such a query and recalculates path and sends.
         *  8. This process continues until final Tag is reached.
         *  9. with that, one connection ends and all Titles and contents on the server are reset so that
         *      ESp and Arduino understand which stage of the process they're in
         *
         *
         *
         * *************** ___ Naming conventions for communication ____ *************************
         *
         *
         *  android initiate request: AndroidSays: content =               NEW
         *
         *  ESP RFID response   ESPSays                                   +1368583524 (exmaple, must start with + character
         *
         *  android path                                                  1lffr (starts with number)
         *
         *  ESP for wrong tag                                             l2783657265 (starts with l and tags)
         *
         *  android NEW path                                              N1lffr (Starts with N)
         *
         *  ESP End path                                                  END
         *
         *  Android end connection                                        END
         *
         *
         *
         *
         *
         *
         *
         *
         *
         */

        /*STEP 1

                Init communication by sending NEW


         */
        RequestQueue queue = Volley.newRequestQueue(this);

        String url ="https://fast-crag-18702.herokuapp.com";
        final String[] urlpot = {url + "/AndroidSays/?title=AndroidSays&content=" + "NEW"};
        // String urlpot=url;
        final StringRequest[] putRequest = {new StringRequest(Request.Method.PUT, urlpot[0],
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("ResponseNEW", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("title", "AndroidSays");
                params.put("content", "NEW");

                return params;
            }

        }};

        queue.add(putRequest[0]);



   //    while(ESPRes == 0){



          /*  JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,  url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.i("from WHILE", response.toString());
                   String InitRes = "";
                    //Log.i("RESPONSE", response.toString());
                    try {
                        JSONObject myResp = response.getJSONObject(1);
                        InitRes = myResp.getString("content");


                        Log.i("From Callback", InitRes);
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
               /*    String WriteFile = "ESP_Response_file";
                 String text = InitRes;
              Log.i("this", text );
                    FileOutputStream fos = null;
                    try {
                        fos = openFileOutput(WriteFile, MODE_PRIVATE);
                        fos.write(text.getBytes());

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

*/
            /*
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("NOTHING NOTHING", "nothing");
                }
            });
            queue.add(request);

            String filename = "ESP_Response_file";


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
                    Log.i("mine1", "ESP File found" );
                } catch (IOException e) {
                    // Error occurred when opening raw file for reading.
                } finally {

                    String contents = stringBuilder.toString();
                    Log.i("From esp resp", contents);
                    if(contents.charAt(0)== 'R')
                    ESPRes=1;

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.i("mine1", " ESP RESPONSE FILE not found");




            }
*/
        /*
        static public void onResume(){
            super.onResume();
            getWhatESPsays getWhatESPsays2 = new getWhatESPsays(robotCaller.this);
            getWhatESPsays2.getESPData(new getWhatESPsays.VolleyCallback(){

                @Override
                public void onError(String message) {

                }

                @Override
                public void onSuccess(String result) {

                    Log.i("FROMSuper", result);
                }
            });
        }

*/
        Log.i("Before", "delay");
        ProgressDialog dialog = ProgressDialog.show(this, "Get a cup of Coffee", "While we're trying to contact your robot...",
                true);
        dialog.show();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                getWhatESPsays getWhatESPsays1 = new getWhatESPsays(robotCaller.this);
                getWhatESPsays1.getESPData(new getWhatESPsays.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Log.i("FROM CALLBACKROBOT", message);




                    }


                    @Override
                    public void onResponse(String response) {

                        Log.i("FROMSUCCESS", response);
                        //   ESPRESPONSE = response;

                        ESPRes =1;
                        RFIDReadFlag =1;

                        int xend =0;
                        int yend =0;
                        int xstart =0;
                        int ystart= 0;
                        List<Integer> myList12 = new ArrayList<Integer>();
                        myList12=OGstopsint.get(finalTarget);
                        xend=myList12.get(0);
                        yend=myList12.get(1);
                        AStar as = new AStar(OGMatint, xstart, ystart, false);
                        List<AStar.Node> path = as.findPathTo(xend, yend);
                        String finalpath ="";
                        List<List<Integer>> PathWInt = new ArrayList<>();
                        if (path != null) {
                            for(int ii=0; ii<path.size(); ii++ ){
                                finalpath += ("[" + path.get(ii).x + ", " + path.get(ii).y + "] ");
                                Class a = path.get(ii).getClass();
                                System.out.println("Class of Object obj is : " + a.getName());
                                List<Integer> IndCod = new ArrayList<>();
                                IndCod.add(path.get(ii).x);
                                IndCod.add(path.get(ii).y);
                                PathWInt.add(IndCod);
                            }

                        }

                        Log.i("path",finalpath);
                        String dirsFinal= returnDirs(PathWInt, xstart, ystart);
                        Log.i("Dirs", dirsFinal.toString());


                        //  String urlpot = url + "/MC%20vs%20MC/?title=MC%20vsMC%20&content=" + dirsFinal ;
                        urlpot[0] = url + "/AndroidSays/?title=AndroidSays&content=" + dirsFinal ;
                        // String urlpot=url;
                        // StringRequest putRequest = new StringRequest(Request.Method.PUT, urlpot,
                        putRequest[0] = new StringRequest(Request.Method.PUT, urlpot[0],
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response) {
                                        // response
                                        Log.d("Response", response);
                                    }
                                },
                                new Response.ErrorListener()
                                {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // error
                                        Log.d("Error.Response", error.toString());
                                    }
                                }
                        ) {

                            @Override
                            protected Map<String, String> getParams()
                            {
                                Map<String, String>  params = new HashMap<String, String> ();
                                params.put("title", "AndroidSays" );
                                params.put("content", "1"+dirsFinal);


                                return params;
                            }

                        };

                        queue.add(putRequest[0]);








                        String WriteFile = "ESP_Response_file";
                        String text = response;
                        Log.i("this", text );
                        FileOutputStream fos = null;
                        try {
                            fos = openFileOutput(WriteFile, MODE_PRIVATE);
                            fos.write(text.getBytes());

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

                        Log.i("After", "delay");
                        dialog.dismiss();









                    }
                });

            }
        }, 60000);



            //queue.add(request);

            Log.i("OUTSIDE", ESPRESPONSE);

            if(ESPRESPONSE.toString().charAt(0)!='+'){
                Log.i("OUTSIDE", ESPRESPONSE);

            }


//        }


        Log.i("Icame", "outside");
        String filename = "ESP_Response_file";


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
               Log.i("From esp resp out", contents);


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("mine1", " ESP RESPONSE FILE not found");




        }

        /****
         *
         *  Above we have waited for a minute to recieve response from ESP
         *  Now we'll keep reading data from server every 10 seconds.
         *  Normally when ESP doesn't lose path it'll post OK
         *  If it loses path, it will post last two RFIDs
         *  Android Keeps Track of that.
         *
         */
        final Handler mHandler = new Handler();

        Runnable mRun = new Runnable() {

            @Override
            public void run() {
                 Log.i("This executes every", "10 secs");
                CheckTenSeconds checkTenSeconds = new CheckTenSeconds(robotCaller.this);
                checkTenSeconds.getESPDataTenS(new getWhatESPsays.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Log.i("FROM CALLBACKROBOT", message);





                    }


                    @Override
                    public void onResponse(String response) {
                        Log.i("END", response);
                        if(response.equals("END") ){
                            ESPRes =2;
                            Log.i("END1", response);
                            return;
                        }

                        Log.i("FROMSUCCESS", response);
                        //   ESPRESPONSE = response;



                        int xend =0;
                        int yend =0;
                        int xstart =0;
                        int ystart= 0;
                        List<Integer> myList12 = new ArrayList<Integer>();
                        myList12=OGstopsint.get(finalTarget);
                        xend=myList12.get(0);
                        yend=myList12.get(1);
                        AStar as = new AStar(OGMatint, xstart, ystart, false);
                        List<AStar.Node> path = as.findPathTo(xend, yend);
                        String finalpath ="";
                        List<List<Integer>> PathWInt = new ArrayList<>();
                        if (path != null) {
                            for(int ii=0; ii<path.size(); ii++ ){
                                finalpath += ("[" + path.get(ii).x + ", " + path.get(ii).y + "] ");
                                Class a = path.get(ii).getClass();
                                System.out.println("Class of Object obj is : " + a.getName());
                                List<Integer> IndCod = new ArrayList<>();
                                IndCod.add(path.get(ii).x);
                                IndCod.add(path.get(ii).y);
                                PathWInt.add(IndCod);
                            }

                        }

                        Log.i("path",finalpath);
                        String dirsFinal= returnDirs(PathWInt, xstart, ystart);
                        Log.i("Dirs", dirsFinal.toString());


                        //  String urlpot = url + "/MC%20vs%20MC/?title=MC%20vsMC%20&content=" + dirsFinal ;
                        urlpot[0] = url + "/AndroidSays/?title=AndroidSays&content=" + dirsFinal ;
                        // String urlpot=url;
                        // StringRequest putRequest = new StringRequest(Request.Method.PUT, urlpot,
                        putRequest[0] = new StringRequest(Request.Method.PUT, urlpot[0],
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response) {
                                        // response
                                        Log.d("Response", response);
                                    }
                                },
                                new Response.ErrorListener()
                                {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // error
                                        Log.d("Error.Response", error.toString());
                                    }
                                }
                        ) {

                            @Override
                            protected Map<String, String> getParams()
                            {
                                Map<String, String>  params = new HashMap<String, String> ();
                                params.put("title", "AndroidSays" );
                                params.put("content", "1"+dirsFinal);


                                return params;
                            }

                        };

                        queue.add(putRequest[0]);








                        String WriteFile = "ESP_Response_file";
                        String text = response;
                        Log.i("this", text );
                        FileOutputStream fos = null;
                        try {
                            fos = openFileOutput(WriteFile, MODE_PRIVATE);
                            fos.write(text.getBytes());

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

                        Log.i("After", "delay");
                        dialog.dismiss();









                    }
                });
                if(ESPRes==2)
                    return;
                 mHandler.postDelayed(this, 10000);
            }
        };

        if(ESPRes!=2)
            mRun.run();
        if(ESPRes==2){
            urlpot[0] = url + "/AndroidSays/?title=AndroidSays&content=" + "END";
            // String urlpot=url;
            // StringRequest putRequest = new StringRequest(Request.Method.PUT, urlpot,
            putRequest[0] = new StringRequest(Request.Method.PUT, urlpot[0],
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response", response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.toString());
                        }
                    }
            ) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("title", "AndroidSays");
                    params.put("content", "END");


                    return params;
                }

            };

            queue.add(putRequest[0]);

        }

        /*


        ---------------------DOCUMENTATION/ NOTICE-----------------------

        Below is the code for GET AND PUT METHODS from server.

        We get information from the robot and post the path whenever requested by user or alternate
        path requested by Robot.






        -------------------------------------------------------------------
         */



        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,  url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("RESPONSE11111", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("NOTHING NOTHING", "nothing");
            }
        });
        queue.add(request);

/*
        String urlpot = url + "/Alif1";
        StringRequest putRequest = new StringRequest(Request.Method.PUT, urlpot,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String> ();
                params.put("title", "BRAga" );
                params.put("content", "NITHYA101");

                return params;
            }

        };

        queue.add(putRequest);
*/
        int xend =0;
        int yend =0;
        int xstart =0;
        int ystart= 0;
        List<Integer> myList12 = new ArrayList<Integer>();
        myList12=OGstopsint.get(finalTarget);
        xend=myList12.get(0);
        yend=myList12.get(1);
        AStar as = new AStar(OGMatint, xstart, ystart, false);
        List<AStar.Node> path = as.findPathTo(xend, yend);
        String finalpath ="";
        List<List<Integer>> PathWInt = new ArrayList<>();
        if (path != null) {
            for(int ii=0; ii<path.size(); ii++ ){
                finalpath += ("[" + path.get(ii).x + ", " + path.get(ii).y + "] ");
                Class a = path.get(ii).getClass();
                System.out.println("Class of Object obj is : " + a.getName());
                List<Integer> IndCod = new ArrayList<>();
                IndCod.add(path.get(ii).x);
                IndCod.add(path.get(ii).y);
                PathWInt.add(IndCod);
            }

        }

        Log.i("path",finalpath);
String dirsFinal= returnDirs(PathWInt, xstart, ystart);
Log.i("Dirs", dirsFinal.toString());

/*
      //  String urlpot = url + "/MC%20vs%20MC/?title=MC%20vsMC%20&content=" + dirsFinal ;
       urlpot[0] = url + "/AndroidSays/?title=AndroidSays&content=" + dirsFinal ;
    // String urlpot=url;
       // StringRequest putRequest = new StringRequest(Request.Method.PUT, urlpot,
        putRequest[0] = new StringRequest(Request.Method.PUT, urlpot[0],
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String> ();
                params.put("title", "AndroidSays" );
                params.put("content", "1"+dirsFinal);


                return params;
            }

        };

        queue.add(putRequest[0]);


*/

    }
/*
    -----------DOCUMENTATION / NOTICE TO DEV/READER------------
    return dirs takes a coordinate list and returns only the directions that a robot
    needs to take in order to achieve the target



 */
    public String returnDirs( List<List<Integer>> PathWInt, int xstart, int ystart){
       String finalDirs = "";
       // finalDirs.add('l');

        int prevx= xstart;
        int prevy= ystart;
        for(List<Integer> temp: PathWInt){
            int x = temp.get(0);
            int y = temp.get(1);
            if(x-prevx==1){
                finalDirs+="l";
            }
            else if(prevx-x==1){
                finalDirs+="r";
            }
            else if(y-prevy==1){
                finalDirs+="f";
            }
            else if(prevy-y==1){
                finalDirs+="b";
            }
            prevx=x;
            prevy=y;
        }


        return finalDirs;
    }






    }




/*
 ******
 *--------------------------DOCUMENTATION TEXT -------------------
 *        \/\/\/\/      IMPORTANT NOTICE TO DEV/ READER          \/\/\/\/
 *
 *
 * Below Function is the A* Algorithm that calculates path from target!
 * roosettacode.org has credits in developing this code
 *
 * ******
 */

class AStar {
    private final List<Node> open;
    private final List<Node> closed;
    private final List<Node> path;
    private final int[][] maze;
    private Node now;
    private final int xstart;
    private final int ystart;
    private int xend, yend;
    private final boolean diag;

    // Node class for convienience
    static class Node implements Comparable {
        public Node parent;
        public int x, y;
        public double g;
        public double h;
        Node(Node parent, int xpos, int ypos, double g, double h) {
            this.parent = parent;
            this.x = xpos;
            this.y = ypos;
            this.g = g;
            this.h = h;
        }
        // Compare by f value (g + h)
        @Override
        public int compareTo(Object o) {
            Node that = (Node) o;
            return (int)((this.g + this.h) - (that.g + that.h));
        }
    }

    AStar(int[][] maze, int xstart, int ystart, boolean diag) {
        this.open = new ArrayList<>();
        this.closed = new ArrayList<>();
        this.path = new ArrayList<>();
        this.maze = maze;
        this.now = new Node(null, xstart, ystart, 0, 0);
        this.xstart = xstart;
        this.ystart = ystart;
        this.diag = diag;
    }
    /*
     ** Finds path to xend/yend or returns null
     **
     ** @param (int) xend coordinates of the target position
     ** @param (int) yend
     ** @return (List<Node> | null) the path
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Node> findPathTo(int xend, int yend) {
        this.xend = xend;
        this.yend = yend;
        this.closed.add(this.now);
        addNeigborsToOpenList();
        while (this.now.x != this.xend || this.now.y != this.yend) {
            if (this.open.isEmpty()) { // Nothing to examine
                return null;
            }
            this.now = this.open.get(0); // get first node (lowest f score)
            this.open.remove(0); // remove it
            this.closed.add(this.now); // and add to the closed
            addNeigborsToOpenList();
        }
        this.path.add(0, this.now);
        while (this.now.x != this.xstart || this.now.y != this.ystart) {
            this.now = this.now.parent;
            this.path.add(0, this.now);
        }
        return this.path;
    }
    /*
     ** Looks in a given List<> for a node
     **
     ** @return (bool) NeightborInListFound
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private static boolean findNeighborInList(List<Node> array, Node node) {
        return array.stream().anyMatch((n) -> (n.x == node.x && n.y == node.y));
    }
    /*
     ** Calulate distance between this.now and xend/yend
     **
     ** @return (int) distance
     */
    private double distance(int dx, int dy) {
        if (this.diag) { // if diagonal movement is alloweed
            return Math.hypot(this.now.x + dx - this.xend, this.now.y + dy - this.yend); // return hypothenuse
        } else {
            return Math.abs(this.now.x + dx - this.xend) + Math.abs(this.now.y + dy - this.yend); // else return "Manhattan distance"
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addNeigborsToOpenList() {
        Node node;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (!this.diag && x != 0 && y != 0) {
                    continue; // skip if diagonal movement is not allowed
                }
                node = new Node(this.now, this.now.x + x, this.now.y + y, this.now.g, this.distance(x, y));
                if ((x != 0 || y != 0) // not this.now
                        && this.now.x + x >= 0 && this.now.x + x < this.maze[0].length // check maze boundaries
                        && this.now.y + y >= 0 && this.now.y + y < this.maze.length
                        && this.maze[this.now.y + y][this.now.x + x] != -1 // check if square is walkable
                        && !findNeighborInList(this.open, node) && !findNeighborInList(this.closed, node)) { // if not already done
                    node.g = node.parent.g + 1.; // Horizontal/vertical cost = 1.0
                    node.g += maze[this.now.y + y][this.now.x + x]; // add movement cost for this square

                    // diagonal cost = sqrt(hor_cost² + vert_cost²)
                    // in this example the cost would be 12.2 instead of 11
                        /*
                        if (diag && x != 0 && y != 0) {
                            node.g += .4;	// Diagonal movement cost = 1.4
                        }
                        */
                    this.open.add(node);
                }
            }
        }
        Collections.sort(this.open);
    }
/*
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void main(String[] args) {
        // -1 = blocked
        // 0+ = additional movement cost
        int[][] maze = {
                {  0,  0,  0,  0,  0,  0,  0,  0},
                {  0,  0,  0,  0,  0,  0,  0,  0},
                {  0,  0,  0,100,100,100,  0,  0},
                {  0,  0,  0,  0,  0,100,  0,  0},
                {  0,  0,100,  0,  0,100,  0,  0},
                {  0,  0,100,  0,  0,100,  0,  0},
                {  0,  0,100,100,100,100,  0,  0},
                {  0,  0,  0,  0,  0,  0,  0,  0},
        };
        AStar as = new AStar(maze, 0, 0, true);
        List<Node> path = as.findPathTo(7, 7);
        if (path != null) {
            path.forEach((n) -> {
                System.out.print("[" + n.x + ", " + n.y + "] ");
                maze[n.y][n.x] = -1;
            });
            System.out.printf("\nTotal cost: %.02f\n", path.get(path.size() - 1).g);

            for (int[] maze_row : maze) {
                for (int maze_entry : maze_row) {
                    switch (maze_entry) {
                        case 0:
                            System.out.print("_");
                            break;
                        case -1:
                            System.out.print("*");
                            break;
                        default:
                            System.out.print("#");
                    }
                }
                System.out.println();
            }
        }
    } */
}
