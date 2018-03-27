package com.example.manicwarden.coursework;
/***
 Frazer J Johnston. Matriculation Number: S1623945
 ***/
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manicwarden.coursework.ParseXML;
import com.example.manicwarden.coursework.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.example.manicwarden.coursework.R.id.findByName;
import static com.example.manicwarden.coursework.R.id.listView;

public class findByRoadname extends AppCompatActivity {
    private ListView listView;
    private EditText roadname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_by_roadname);

        listView = findViewById(R.id.listView);
        Button searchButton = (Button)findViewById(R.id.btnSearch);
        searchButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                System.out.println("***************************onCreate");
                beginTask();
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        roadname = (EditText) findViewById(R.id.searchText);
        roadname.setImeOptions(EditorInfo.IME_ACTION_DONE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapter, View v, int position, long id)
            {

                try {
                    //String item = listView.getItemAtPosition(position).toString();
                    //NextRdv item = (NextRdv) parent.getItemAtPosition(position)

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

                    AlertDialog.Builder builder = new AlertDialog.Builder(findByRoadname.this);
                    builder.setCancelable(true);
                    builder.setTitle("Incident Information");


                    builder.setMessage(alertTitle + "\n" + alertDesc + "\n" + alertStartDate
                            + "\n" + alertEndDate + "\n" + alertDuration + "\n" + alertQuestion);

                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // call the map activity passing the longitude and latitude
                                    Intent intent = new Intent(findByRoadname.this, MapsActivity.class);

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

        if(isEmpty(roadname))
        {
            Toast.makeText(this, "Please enter a roadname.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            parseXML.roadName = roadname.getText().toString();
        }

        parseXML.startParsing(plannedRoadworksURL);
    }

    private boolean isEmpty(EditText editText) {
        // if the string the user entered is empty or whitespace
        if (editText.getText().toString().trim().length() > 0)
        {
            return false;
        }
        return true;
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
