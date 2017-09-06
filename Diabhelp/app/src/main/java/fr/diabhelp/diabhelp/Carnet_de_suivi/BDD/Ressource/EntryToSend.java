package fr.diabhelp.diabhelp.Carnet_de_suivi.BDD.Ressource;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import fr.diabhelp.diabhelp.Carnet_de_suivi.Utils.DateUtils;

/**
 * Created by Sumbers on 10/08/2016.
 */
public class EntryToSend {

    public Integer lunch;
    public Integer diner;
    public Integer encas;
    public Integer sleep;
    public Integer wakeup;
    public Integer night;
    public Integer workout;
    public Integer hypogly;
    public Integer hypergly;
    public Integer work;
    public Integer athome;
    public Integer alcohol;
    public Integer period;
    public Integer breakfast;
    private Integer id;
    private Long dateCreation;
    private Long dateEdition;
    private String title;
    private String place;
    private Double glucide;
    private String activity;
    private String activityType;
    private String notes;
    private Double fastInsu;
    private Double slowInsu;
    private Double hba1c;
    private String hour;
    private Double glycemy;

    public EntryToSend(Integer id) {
        this.id = id;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public Integer getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(Integer alcohol) {
        this.alcohol = alcohol;
    }

    public Integer getAthome() {
        return athome;
    }

    public void setAthome(Integer athome) {
        this.athome = athome;
    }

    public Integer getWork() {
        return work;
    }

    public void setWork(Integer work) {
        this.work = work;
    }

    public Integer getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(Integer breakfast) {
        this.breakfast = breakfast;
    }

    public Long getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATECREATION_DB_FORMAT);
        try {
            this.dateCreation = sdf.parse(dateCreation).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Long getDateEdition() {
        return dateEdition;
    }

    public void setDateEdition(String dateEdition) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATEEDITION_DB_FORMAT);
        try {
            this.dateEdition = sdf.parse(dateEdition).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Integer getDiner() {
        return diner;
    }

    public void setDiner(Integer diner) {
        this.diner = diner;
    }

    public Integer getEncas() {
        return encas;
    }

    public void setEncas(Integer encas) {
        this.encas = encas;
    }

    public Double getFastInsu() {
        return fastInsu;
    }

    public void setFastInsu(Double fastInsu) {
        this.fastInsu = fastInsu;
    }

    public Double getGlucide() {
        return glucide;
    }

    public void setGlucide(Double glucide) {
        this.glucide = glucide;
    }

    public Double getGlycemy() {
        return glycemy;
    }

    public void setGlycemy(Double glycemy) {
        this.glycemy = glycemy;
    }

    public Double getHba1c() {
        return hba1c;
    }

    public void setHba1c(Double hba1c) {
        this.hba1c = hba1c;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Integer getHypergly() {
        return hypergly;
    }

    public void setHypergly(Integer hypergly) {
        this.hypergly = hypergly;
    }

    public Integer getHypogly() {
        return hypogly;
    }

    public void setHypogly(Integer hypogly) {
        this.hypogly = hypogly;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLaunch() {
        return lunch;
    }

    public void setLaunch(Integer launch) {
        this.lunch = launch;
    }

    public Integer getNight() {
        return night;
    }

    public void setNight(Integer night) {
        this.night = night;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getSleep() {
        return sleep;
    }

    public void setSleep(Integer sleep) {
        this.sleep = sleep;
    }

    public Double getSlowInsu() {
        return slowInsu;
    }

    public void setSlowInsu(Double slowInsu) {
        this.slowInsu = slowInsu;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getWakeup() {
        return wakeup;
    }

    public void setWakeup(Integer wakeup) {
        this.wakeup = wakeup;
    }

    public Integer getWorkout() {
        return workout;
    }

    public void setWorkout(Integer workout) {
        this.workout = workout;
    }
}
