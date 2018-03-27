package com.example.manicwarden.coursework;
/***
 Frazer J Johnston. Matriculation Number: S1623945
 ***/
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class CurrentIncidents extends AppCompatActivity
{

    private ListView listView;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("***************************onCreate");
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_current_incidents_page);

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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapter, View v, int position, long id)
            {

                //String item = listView.getItemAtPosition(position).toString();
                //NextRdv item = (NextRdv) parent.getItemAtPosition(position)
                try {
                    TextView location = (TextView) v.findViewById(R.id.location);
                    TextView description = (TextView) v.findViewById(R.id.description);
                    TextView title = (TextView) v.findViewById(R.id.title);
                    TextView duration = (TextView) v.findViewById(R.id.Duration);

                    final String Description = description.getText().toString();
                    final String Title = title.getText().toString();
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
                    String alertDuration = "Duration: " + Duration + days;
                    String alertQuestion = "Do you want to see this item on the map?";

                    AlertDialog.Builder builder = new AlertDialog.Builder(CurrentIncidents.this);
                    builder.setCancelable(true);
                    builder.setTitle("Incident Information");


                    builder.setMessage(alertTitle + "\n" + alertDesc + "\n" + alertDuration + "\n" + alertQuestion);

                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // call the map activity passing the longitude and latitude
                                    Intent intent = new Intent(CurrentIncidents.this, MapsActivity.class);

                                    intent.putExtra("Longitude", longitude);
                                    intent.putExtra("Latitude", latitude);
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

    private String currentIncidentsURL ="http://trafficscotland.org/rss/feeds/currentincidents.aspx";

    private ArrayList<HashMap<String, String>> itemList = new ArrayList<>();

    private void beginTask()
    {
        System.out.println("***************************onCreate");
        itemList.clear();
        listView.setAdapter(null);
        ParseXML parseXML = new ParseXML(this);
        parseXML.startParsing(currentIncidentsURL);

        //new parseXMLTask().execute(currentIncidentsURL);
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


}


