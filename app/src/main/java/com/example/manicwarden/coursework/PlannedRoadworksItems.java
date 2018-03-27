package com.example.manicwarden.coursework;
/***
 Frazer J Johnston. Matriculation Number: S1623945
 ***/
import java.util.Date;


public class PlannedRoadworksItems {
    public String _title = "Empty";
    public String _description = "Empty";
    public String _location = "";
    private int _duration;
    public String _startDate;
    public String _endDate;
    private boolean visible;


    PlannedRoadworksItems(String title, String description, String location, int duration, String startDate, String endDate)
    {
        this._title = title;
        this._description = description;
        this._location = location;
        this._duration = duration;
        this._startDate = startDate;
        this._endDate = endDate;
    }

    public String get_title(){return _title;}
    public void set_title(String title){this._title = title;}

    public String get_description(){return _description;}
    public void set_description(String description){this._description = description;}

    public String get_location(){return _location;}
    public void set_location(String location){this._location = location;}

    public String get_duration (){String duration = Integer.toString(_duration); return duration;}
    public void set_duration(int duration){this._duration = duration;}

    public String get_startDate(){return _startDate;}
    public void set_startDate(String startDate){this._startDate = startDate;}

    public String get_endDate(){return _endDate;}
    public void set_endDate(String endDate){this._endDate = endDate;}

    public boolean isVisible() {
        return visible;
    }

    public void setVisibility(boolean visible) {
        this.visible = visible;
    }

}
