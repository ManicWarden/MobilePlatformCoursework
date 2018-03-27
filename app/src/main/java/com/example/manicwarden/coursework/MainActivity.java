package com.example.manicwarden.coursework;
/***
 Frazer J Johnston. Matriculation Number: S1623945
 ***/
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{


    //https://developer.android.com/training/basics/network-ops/xml.html#instantiate
    private Button currentIncidents;
    private Button plannedRoadworks;
    private Button byName;
    private Button byDate;
    private Button Exit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentIncidents = (Button)findViewById(R.id.btnCurrentIncidents);
        plannedRoadworks = (Button)findViewById(R.id.btnPlannedRoadworks);
        byName = (Button)findViewById(R.id.btnbyName);
        byDate = (Button)findViewById(R.id.btnbyDate);
        Exit = (Button)findViewById(R.id.btnExit);

        currentIncidents.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                navCurrentIncidents(v);
            }
        });

        plannedRoadworks.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                navPlannedRoadworks(v);
            }
        });

        byName.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                navByName(v);
            }
        });

        byDate.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                navByDate(v);
            }
        });

        Exit.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                System.exit(0);
            }
        });


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

    public void navCurrentIncidents(View view)
    {
        Intent intent = new Intent(this, CurrentIncidents.class);
        startActivity(intent);

    }

    public void navPlannedRoadworks(View view)
    {
        Intent intent = new Intent(this, PlannedRoadworks.class);
        startActivity(intent);
    }

    public void navByName(View view)
    {
        Intent intent = new Intent(this, findByRoadname.class);
        startActivity(intent);
    }

    public void navByDate(View view)
    {
        Intent intent = new Intent(this, findByDate.class);
        startActivity(intent);
    }

}
