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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manicwarden.coursework.ParseXML;
import com.example.manicwarden.coursework.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class findByDate extends AppCompatActivity {

    private ListView listView;
    private EditText day;
    private EditText month;
    private EditText year;
    DateFormat format = new SimpleDateFormat("d MM yyyy", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_by_date);

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
        day = (EditText) findViewById(R.id.dateDay);
        month = (EditText) findViewById(R.id.dateMonth);
        year = (EditText) findViewById(R.id.dateYear);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapter, View v, int position, long id)
            {

                //String item = listView.getItemAtPosition(position).toString();
                //NextRdv item = (NextRdv) parent.getItemAtPosition(position)
                try {
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(findByDate.this);
                    builder.setCancelable(true);
                    builder.setTitle("Incident Information");


                    builder.setMessage(alertTitle + "\n" + alertDesc + "\n" + alertStartDate
                            + "\n" + alertEndDate + "\n" + alertDuration + "\n" + alertQuestion);

                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // call the map activity passing the longitude and latitude
                                    Intent intent = new Intent(findByDate.this, MapsActivity.class);

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
        String stringDate = day.getText().toString() + " " +
                month.getText().toString() + " " +
                year.getText().toString();

        // if the date entered is valid
        try
        {
            format.setLenient(false);
            Date date = format.parse(stringDate);
            parseXML.targetDate = date;
        }
        // if date entered is not valid
        catch(ParseException e)
        {
            Toast.makeText(this, "Please enter a valid date", Toast.LENGTH_LONG).show();
            day.setText("");
            month.setText("");
            year.setText("");
            return;
        }

        //parseXML.targetDate =
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

    // to find if a text view has a value
    /*private boolean isEmpty(EditText editText) {
        if (editText.getText().toString().trim().length() > 0)
        {
            try { // if value is a number
                int num = Integer.parseInt(editText.getText().toString());
                return true;
            }
            // if value is not a number
            catch (NumberFormatException e) {
                return false;
            }

        }
        return true;
    }*/

}
