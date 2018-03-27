package com.example.manicwarden.coursework;
/***
 Frazer J Johnston. Matriculation Number: S1623945
 ***/
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class CurrentIncidentsPage extends AppCompatActivity {

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("***************************onCreate");
        super.onCreate(savedInstanceState);

           listView = (ListView) findViewById(R.id.listView);

        setContentView(R.layout.activity_current_incidents_page);
        Button startButton = (Button)findViewById(R.id.btnStart);
        startButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                System.out.println("***************************onCreate");
                beginTask();
            }
        });

        Toast.makeText(this, "Current Incidents Page",Toast.LENGTH_SHORT).show();
    }

    private String currentIncidentsURL ="https://trafficscotland.org/rss/feeds/currentincidents.aspx";

    private ArrayList<HashMap<String, String>> itemList = new ArrayList<>();
    private HashMap<String,String> item = new HashMap<>();

    private void beginTask()
    {
        System.out.println("***************************onCreate");

        itemList.clear();
        listView.setAdapter(null);
        new parseXMLTask().execute(currentIncidentsURL);
    }

    private class parseXMLTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            System.out.println("***************************doInBackground1");
            String InputLine = "";


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
                            switch(tag)
                            {
                                case"title":
                                    System.out.println("**********************title5");
                                    item.put("title", text);
                                    break;
                                case "description":
                                    System.out.println("**********************description5");
                                    item.put("descrption", text);
                                    break;
                                case "georss:point":
                                    System.out.println("**********************description5");
                                    item.put("georss:point",text);
                                    break;
                                case "item":
                                    System.out.println("**********************description5");
                                    if(item != null)
                                    {
                                        System.out.println("**********************description5");
                                        itemList.add(item);
                                    }
                                    break;
                            }
                            break;
                    }
                    System.out.println("**********************parser.next()6");
                    event = parser.next();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("**********************runOnUiThread7");

                        ListAdapter adapter = new SimpleAdapter(CurrentIncidentsPage.this, itemList,
                                R.layout.listview_rows,
                                new String[]{"Title", "Description", "Location"},
                                new int[]{R.id.title, R.id.description, R.id.location});
                        System.out.println("**********************" + adapter.toString());
                        System.out.println("**********************After creating adapter");
                        listView.setAdapter(adapter);
                        System.out.println("**********************After setting adapter");
                    }
                });

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
            setContentView(R.layout.activity_current_incidents);

        }
    }

}
