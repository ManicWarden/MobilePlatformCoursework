package com.example.manicwarden.coursework;
/***
 Frazer J Johnston. Matriculation Number: S1623945
 ***/
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.example.manicwarden.coursework.Global.isConnectedtoInternet;



public class ParseXML {

    private ArrayList<HashMap<String, String>> itemList = new ArrayList<>();
    private HashMap<String,String> item = new HashMap<>();
    private SimpleAdapter adapter;
    private Context _context;

    public Date targetDate;
    public String roadName;
    public ArrayList<PlannedRoadworksItems> plannedRoadworksItemList = new ArrayList<PlannedRoadworksItems>();
    public ArrayList<CurrentIncidentsItems> currentIncidentsItemList = new ArrayList<CurrentIncidentsItems>();
    DateFormat format = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);

    public ParseXML(Context context)
    {
        this._context = context;
    }

    public void startParsing(String url)
    {

        // if user is connected to a network
        /*if(isConnectedtoInternet(_context))
        {*/
            new parseXMLTask().execute(url);
        /*}
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(_context);
            builder.setCancelable(false);
            builder.setTitle("No Internet Connection");

            builder.setMessage("No network connection detected. Please connect to the internet to continue.");

            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }*/

    }

    private class parseXMLTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {

            System.out.println("***************************doInBackground1");
            String title = "";
            String Description = "";
            String location = "";
            int duration = 0;
            String startDate = "";
            String endDate = "";
            String StartDate;
            String EndDate;


            try
            {
                System.out.println("***************************doInBackgroundTRY2");
                //URL url = new URL(urls[0]);
                //URLConnection urlConnection = url.openConnection();
                //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                // setting the InputStream stream to a HttpURLConnection based on the CurrentIncidents url
                InputStream stream = downloadURL(urls[0]);


                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = parserFactory.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

                // setting the input of the parser to the stream connected to the CurrentIncidents Traffic Scotland URL
                parser.setInput(stream, null );
                String tag = "", text = "";
                int event = parser.getEventType();

                // until the end of the XML Document
                while (event != XmlPullParser.END_DOCUMENT)
                {
                    System.out.println("**********************while (event != XmlPullParser.END_DOCUMENT3");
                    tag = parser.getName();
                    switch(event)
                    {
                        case XmlPullParser.START_TAG:
                            System.out.println("**********************XmlPullParser.START_TAG4");
                            if(tag.equals("item"))
                            {
                                item = new HashMap<>();
                            }
                            break;
                        case XmlPullParser.TEXT:
                            System.out.println("**********************XmlPullParser.TEXT4");
                            text=parser.getText();
                            break;
                        case XmlPullParser.END_TAG:
                            System.out.println("**********************XmlPullParser.TEXT4");

                                // if the user is activating the parser from the PlannedRoadworks activity
                                // the data will be stored in the PlannedRoadworksItems
                                switch (tag) {
                                    case "title":
                                        System.out.println("**********************title5");
                                        //plannedRoadworksItems.set_title(text);
                                        item.put("Title", text);
                                        title = text;
                                        break;
                                    case "description":
                                        System.out.println("**********************description5");


                                        //plannedRoadworksItems.set_description(text);

                                        item.put("Description", text);
                                        Description = text;
                                        if(text.contains("Start Date: "))
                                        {

                                            startDate = getDate(text, "Start Date: ");
                                        }
                                        if(text.contains("End Date: "))
                                        {
                                            endDate = getDate(text, "End Date: ");
                                        }

                                        item.put("startDate", startDate);
                                        item.put("endDate", endDate);
                                        duration = getDuration(startDate, endDate);
                                        /********* setting values not working****************/
                                        //plannedRoadworksItems.set_startDate(startDate);
                                        //plannedRoadworksItems.set_endDate(endDate);
                                        break;
                                    case "georss:point":
                                        System.out.println("**********************description5");
                                        item.put("Location", text);
                                        location = text;
                                        //plannedRoadworksItems.set_location(text);
                                        break;
                                    case "item":
                                        System.out.println("**********************description5");
                                        if (item != null) {
                                            System.out.println("**********************description5");
                                            itemList.add(item);
                                            // add the created object to the object list
                                            if ( _context instanceof CurrentIncidents )
                                            {
                                                CurrentIncidentsItems currentIncidentsItems = new CurrentIncidentsItems(title,Description, location, duration);
                                                currentIncidentsItemList.add(currentIncidentsItems);
                                            }
                                            else
                                            {
                                                PlannedRoadworksItems plannedRoadworksItems = new PlannedRoadworksItems(title, Description, location, duration, startDate, endDate);
                                                plannedRoadworksItemList.add(plannedRoadworksItems);
                                            }

                                        }
                                        break;
                                }

                            break;
                    }
                    System.out.println("**********************parser.next()6");
                    event = parser.next();
                }



                /*
                while((InputLine = bufferedReader.readLine()) != null)
                {

                }*/
            }
            catch(Exception e)
            {
                System.out.println("**********************doInBackgroundException2");
            }
            return "";
        }
        private InputStream downloadURL(String urlString) throws IOException
        {
            System.out.println("***************************downloadURL");
            java.net.URL url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            // time in milliseconds
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            return conn.getInputStream();

        }

        @Override
        protected void onPostExecute(String result)
        {


            System.out.println("***************************onPostExecute7");
            System.out.println("**********************runOnUiThread7");

            /*adapter = new SimpleAdapter(_context, itemList,
                    R.layout.listview_rows,
                    new String[]{"Title", "Description"},
                    new int[]{R.id.title, R.id.description});*/



            // if the activity that called this class was CurrentIncidents
            if ( _context instanceof CurrentIncidents )
            {

                ListView listView = (ListView) ((CurrentIncidents)_context).findViewById(R.id.listView);
                if(itemList.isEmpty())
                {
                    adapter = new SimpleAdapter(_context, itemList,
                    R.layout.listview_rows,
                    new String[]{"Title", "Description"},
                    new int[]{R.id.title, R.id.description});

                    item.put("Title", "There are no current incidents");
                    itemList.add(item);
                    listView.setAdapter(adapter);

                }
                else
                {
                    ArrayAdapter adapter = new CurrentIncidentsAdapter((CurrentIncidents)_context,R.layout.listview_rows, currentIncidentsItemList);
                    listView.setAdapter(adapter);
                }


            }
            // if the activity that called this class was PlannedRoadworks
            else if ( _context instanceof PlannedRoadworks )
            {

                ListView listView = (ListView) ((PlannedRoadworks)_context).findViewById(R.id.listView);
                ArrayAdapter adapter = new PlannedRoadworksAdapter((PlannedRoadworks)_context,R.layout.listview_rows, plannedRoadworksItemList);
                listView.setAdapter(adapter);

            }
            else if( _context instanceof findByDate)
            {

                checkDates(plannedRoadworksItemList);
            }
            else if(_context instanceof findByRoadname)
            {

                checkTitles(plannedRoadworksItemList);
            }

        }
    }

    private void checkDates(ArrayList<PlannedRoadworksItems> list)
    {

        ArrayList<PlannedRoadworksItems> dateList = new ArrayList<>();
        HashMap<String,String> dateItem = new HashMap<>();
        System.out.println("***************  checkDates() Start");
        String startDate = "";
        Date StartDate = null;
        String endDate = "";
        Date EndDate = null;

        for(int i = 0; i <  list.size(); i ++)
        {
            //https://stackoverflow.com/questions/883060/how-can-i-determine-if-a-date-is-between-two-dates-in-java
            // loop through the list of hashmaps and get every start
            // date and end date
            startDate = list.get(i).get_startDate();
            try
            {
                StartDate = format.parse(startDate);
            }
            catch (ParseException e)
            {
                System.out.println("*************** Date Parse Exception " + e);
            }

            endDate = list.get(i).get_endDate();

            try
            {
                EndDate = format.parse(endDate);
            }
            catch (ParseException e)
            {
                System.out.println("*************** Date Parse Exception " + e);
            }

            // if the strings have parsed into dates properly
            if(StartDate != null && EndDate != null)
            {
                // if the target date entered by the user is between the start date and
                // the end date of the incident or roadwork in question
                if(targetDate.before(EndDate) && targetDate.after(StartDate))
                {
                    // add the current item to the datelist
                    dateList.add(list.get(i));

                }
            }
        }
        System.out.println("***************  checkDates() after for");
        ListView listView = (ListView) ((findByDate)_context).findViewById(R.id.listView);
        // if there are no incidents or roadworks that will occur in the targetdate
        // entered by the user

        if(dateList.isEmpty())
        {
            itemList.clear();


            item.put("Title", "There are no planned roadworks on the date specified: "+ targetDate);
            itemList.add(item);

            adapter = new SimpleAdapter(_context, itemList,
                    R.layout.listview_rows,
                    new String[]{"Title"},
                    new int[]{R.id.title});
            listView.setAdapter(adapter);
        }
        else
        {
            ArrayAdapter adapter = new PlannedRoadworksAdapter(_context,R.layout.listview_rows, dateList);
            listView.setAdapter(adapter);
        }



        System.out.println("***************************** after setting adapter");
    }

    private void checkTitles(ArrayList<PlannedRoadworksItems> list)
    {
        ArrayList<PlannedRoadworksItems> roadList = new ArrayList<>();
        HashMap<String,String> titleItem = new HashMap<>();
        String Title = "";
        for(int i = 0; i <  list.size(); i ++)
        {

            Title = list.get(i).get_title();

            if(Title.toLowerCase().contains(roadName.toLowerCase()))
            {
                roadList.add(list.get(i));
            }
        }
        ListView listView = (ListView) ((findByRoadname)_context).findViewById(R.id.listView);
        if(roadList.isEmpty())
        {

            itemList.clear();


            item.put("Title", "There are no planned roadworks on the road specified: " + roadName);
            itemList.add(item);

            SimpleAdapter adapter = new SimpleAdapter(_context, itemList,
                    R.layout.listview_rows,
                    new String[]{"Title"},
                    new int[]{R.id.title});
            listView.setAdapter(adapter);
        }
        else
        {
            ArrayAdapter adapter = new PlannedRoadworksAdapter(_context,R.layout.listview_rows, roadList);
            listView.setAdapter(adapter);
        }

        System.out.println("***************************** after setting adapter");
    }
    private String getDate(String text, String start)
    {
        // get date from xml text depending on start, either begins at the the Start Date and ends at
        // the first " -" or begins at the End Date and ends at the second " -"
        // leaving a string like "Monday, 19 March 2018 -"
        String penultimateDate = "";
        String finalDate = "";

        try
        {
            // splits the string into 3 pieces
            // dates[0] contains the Start Date: tag to the 00:00 of said start date before the first <br />
            // dates[1] contains the End Date: tag to the 00:00 of said end date before the second <br /> and after the first <br />
            // dates[2] contains the text after the second <br />
            String[] dates = text.split("<br />");
            if(start.contains("Start"))
            {
                // removes the start tag 'Start Date: ' or 'End Date: ' and the time the item begins
                penultimateDate = dates[0].substring(dates[0].indexOf(start), dates[0].indexOf(" -"));
                // removes the day and comma from the remainder of the string
                finalDate = penultimateDate.substring(penultimateDate.indexOf(",") + 2);
            }
            else if(start.contains("End"))
            {
                // removes the start tag 'Start Date: ' or 'End Date: ' and the time the item begins
                penultimateDate = dates[1].substring(dates[1].indexOf(start), dates[1].indexOf(" -"));
                // removes the day and comma from the remainder of the string
                finalDate = penultimateDate.substring(penultimateDate.indexOf(",") + 2);
            }

        }
        catch (Error e)
        {
            System.out.print("date parsing error " + e);
            e.printStackTrace();
        }


        return finalDate;

    }

    private int getDuration(String startDate, String endDate)
    {
        // find the duration of events by changing the dates into date formats and
        // finding the difference
        int duration = 0;

        Date StartDate = new Date();
        Date EndDate= new Date();
        try
        {
            StartDate = format.parse(startDate);
        }
        catch (ParseException e)
        {
            System.out.println("*************** Date Parse Exception " + e);
        }


        try
        {
            EndDate = format.parse(endDate);
        }
        catch (ParseException e)
        {
            System.out.println("*************** Date Parse Exception " + e);
        }


        duration = (int)( (EndDate.getTime() - StartDate.getTime())
                                      / (1000 * 60 * 60 * 24) );

        duration = duration + 1;

        return duration;
    }

}