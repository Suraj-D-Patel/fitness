package patel.d.suraj.fitness;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by suraj.
 */

public class Session {

    String sessionname;
    String StartTime;
    String EndTime;
    String Date;
    String username;
    ArrayList<String> lat;
    ArrayList<String> lng;
    double meters;
    long millis;
    int calorie;

    public Session(){}

    public Session(String sessionname, String startTime, String endTime, String date, String username, ArrayList<String> lat, ArrayList<String> lng, double meters, long millis, int calorie) {
        this.sessionname = sessionname;
        StartTime = startTime;
        EndTime = endTime;
        Date = date;
        this.username = username;
        this.lat = lat;
        this.lng = lng;
        this.meters = meters;
        this.millis = millis;
        this.calorie = calorie;
    }


    public String getSessionname() {
        return sessionname;
    }

    public void setSessionname(String sessionname) {
        this.sessionname = sessionname;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getLat() {
        return lat;
    }

    public void setLat(ArrayList<String> lat) {
        this.lat = lat;
    }

    public ArrayList<String> getLng() {
        return lng;
    }

    public void setLng(ArrayList<String> lng) {
        this.lng = lng;
    }

    public double getMeters() {
        return meters;
    }

    public void setMeters(int meters) {
        this.meters = meters;
    }

    public long getMillis() {
        return millis;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }
}