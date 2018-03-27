package com.example.manicwarden.coursework;
/***
 Frazer J Johnston. Matriculation Number: S1623945
 ***/
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manicwarden.coursework.R;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class PlannedRoadworks extends AppCompatActivity
{
    private ListView listView;
    private SimpleAdapter adapter;
    /*private String latitude;
    private String longitude;*/
    GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("***************************onCreate");
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_planned_roadworks);

        listView = findViewById(R.id.listView);
        Button startButton = (Button)findViewById(R.id.btnStart);
        startButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                System.out.println("***************************onCreate");
                beginTask();

            }
        });


        //gestureDetector = new GestureDetector(this, new GestureListener());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapter, View v, int position, long id)
            {

                    //String item = listView.getItemAtPosition(position).toString();
                    //NextRdv item = (NextRdv) parent.getItemAtPosition(position)
                    try {
                        // the view passed to the onItemClick method is used to specify what item to target
                        // therefore the textViews are chosen with v. to specify what item data we want
                        TextView location = (TextView) v.findViewById(R.id.location);
                        TextView description = (TextView) v.findViewById(R.id.description);
                        TextView title = (TextView) v.findViewById(R.id.title);
                        TextView startDate = (TextView) v.findViewById(R.id.StartDate);
                        TextView endDate = (TextView) v.findViewById(R.id.EndDate);
                        TextView duration = (TextView) v.findViewById(R.id.Duration);

                        final String Description = description.getText().toString();
                        final String Title = title.getText().toString();
                        final String StartDate = startDate.getText().toString();
                        final String EndDate = endDate.getText().toString();
                        final String Duration = duration.getText().toString();


                        final String item = location.getText().toString();
                        final String[] items = item.split(" ");
                        final String latitude = items[0];
                        final String longitude = items[1];

                        String days = " Days";
                        if (Integer.parseInt(Duration) == 1) {
                            days = " Day";
                        }

                        String alertTitle = "Title: " + Title;
                        String alertDesc = "Description: " + Description;
                        String alertStartDate = "Start Date: " + StartDate;
                        String alertEndDate = "End Date: " + EndDate;
                        String alertDuration = "Duration: " + Duration + days;
                        String alertQuestion = "Do you want to see this item on the map?";

                        AlertDialog.Builder builder = new AlertDialog.Builder(PlannedRoadworks.this);
                        builder.setCancelable(true);
                        builder.setTitle("Incident Information");


                        builder.setMessage(alertTitle + "\n" + alertDesc + "\n" + alertStartDate
                                + "\n" + alertEndDate + "\n" + alertDuration + "\n" + alertQuestion);

                        builder.setPositiveButton("Confirm",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // call the map activity passing the longitude and latitude
                                        Intent intent = new Intent(PlannedRoadworks.this, MapsActivity.class);

                                        intent.putExtra("Longitude", longitude);
                                        intent.putExtra("Latitude", latitude);
                                        finish();
                                        startActivity(intent);

                                    }
                                });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    catch (Exception e)
                    {
                        System.out.println("************************onItemClickException " + e);
                        Toast.makeText(getApplicationContext(), "The item selected cannot be selected, please try another", Toast.LENGTH_LONG).show();
                    }


            }

        });

    }


    private String plannedRoadworksURL ="http://www.trafficscotland.org/rss/feeds/plannedroadworks.aspx";

    private ArrayList<HashMap<String, String>> itemList = new ArrayList<>();


    private void beginTask()
    {
        System.out.println("***************************onCreate");
        itemList.clear();
        listView.setAdapter(null);
        ParseXML parseXML = new ParseXML(this);
        parseXML.startParsing(plannedRoadworksURL);
    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        //respond to menu item selection
        switch (item.getItemId()) {
            case R.id.Home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.CurrentIncidents:
                startActivity(new Intent(this, CurrentIncidents.class));
                return true;
            case R.id.PlannedRoadworks:
                startActivity(new Intent(this, PlannedRoadworks.class));
                return true;
            case R.id.findByDate:
                startActivity(new Intent(this, findByDate.class));
                return true;
            case R.id.findByName:
                startActivity(new Intent(this, findByRoadname.class));
                return true;
            case R.id.Exit:
                System.exit(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        // for single clicks
        return gestureDetector.onTouchEvent(e);
    }
/*
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e)
        {
            // to get the x and y position of the click and round it from a float to an int
            //int x = Math.round(e.getX());
           // int y = Math.round(e.getY());

            //int position = listView.pointToPosition(x,y);
            int position = listView.pointToPosition((int) e.getX(), (int) e.getY());
            if (position < 0) {
                return true;
            }
            TextView textView = (TextView) findViewById(R.id.location);

            final String item = textView.getText().toString();
            final String[] items = item.split(" ");
            final String latitude = items[0];
            final String longitude = items[1];


            AlertDialog.Builder builder = new AlertDialog.Builder(PlannedRoadworks.this);
            builder.setCancelable(true);
            builder.setTitle("Title");
            builder.setMessage("Do you want to see this item on the map?");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // call the map activity passing the longitude and latitude
                            Intent intent = new Intent(PlannedRoadworks.this, MapsActivity.class);

                            intent.putExtra("Longitude", longitude);
                            intent.putExtra("Latitude", latitude);
                            finish();
                            startActivity(intent);

                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        }
    }*/
}



