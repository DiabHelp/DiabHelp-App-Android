package fr.diabhelp.carnetdesuivi.BDD.Ressource;

/**
 * Created by naqued on 21/11/15.
 */
public class EntryOfCDS {
    // Notez que l'identifiant est un long

        public Integer launch;;
    public Integer diner;;
    public Integer encas;
    public Integer sleep;
    public Integer wakeup;
    public Integer night;
    public Integer workout;
    public Integer hypogly;
    public Integer hypergly;
    public Integer atwork;
    public Integer athome;
    public Integer alcohol;
    public Integer period;
    public Integer breakfast;
private String idUser;
    private String dateCreation;
    private String dateEdition;
    private String title;
    private String place;
    private Double glucide;
    private String activity;
    private String activityType;
    private String notes;
    private Double fast_insu;
    private Double slow_insu;
    private Double hba1c;
    private String hour;
    private Double glycemy;
    public EntryOfCDS(){}

    public EntryOfCDS(String dateCreation) { // String dateCreation
        this.dateCreation = dateCreation;
        this.dateEdition = null;
        this.title = null;
        this.place = null;
        this.glucide = null;
        this.activity = null;
        this.activityType = null;
        this.notes = null;
        this.fast_insu = null;
        this.slow_insu = null;
        this.hba1c = null;
        this.hour = null;
        this.glycemy = null;

        this.launch = null;
        this.diner = null;
        this.encas = null;
        this.sleep = null;
        this.wakeup = null;
        this.night = null;
        this.workout = null;
        this.hypogly = null;
        this.hypergly = null;
        this.atwork = null;
        this.athome = null;
        this.alcohol = null;
        this.period = null;
        this.breakfast = null;
    }

    public String getTitle() { return this.title; }

    public void setTitle(String _title) { this.title = _title;}

    public String getPlace() { return this.place; }

    public void setPlace(String _place) { this.place = _place;}

    public Double getGlucide() { return this.glucide; }

    public void setGlucide(String _glucide) {

        if (!_glucide.isEmpty())
            this.glucide = Double.parseDouble(_glucide);
        else
            this.glucide = 0.00;

    }

    public String getActivity() { return this.activity; }

    public void setActivity(String _activity) { this.activity = _activity; }

    public String getActivityType() { return this.activityType; }

    public void setActivityType(String _activityType) { this.activityType = _activityType; }

    public String getNotes() { return this.notes; }

    public void setNotes(String _notes) { this.notes = _notes; }

    public String getDateCreation() { return this.dateCreation; }

    public void setDateCreation(String _date) { this.dateCreation = _date; }

    public String getDateEdition(){return this.dateEdition; }

    public void setDateEdition(String dateEd)
    {
        this.dateEdition = dateEd;
    }

    public Double getFast_insu() { return this.fast_insu; }

    public void setFast_insu(Double _insu) { this.fast_insu = _insu; }

    public Double getSlow_insu() { return this.slow_insu; }

    public void setSlow_insu(Double _insu) { this.slow_insu = _insu; }

    public Double getHba1c() { return this.hba1c; }

    public void setHba1c(Double _hba1c) { this.hba1c = _hba1c; }

    public String getHour () { return this.hour; }

    public void setHour (String _hour) { this.hour = _hour; }

    public Double getglycemy() { return this.glycemy; }

    public String getIdUser() {return this.idUser;}

    public void setIdUser(String idUser) {this.idUser = idUser;}

    public Integer getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(Integer breakfast) {
        this.breakfast = breakfast;
    }

    public Integer getLaunch() {
        return launch;
    }

    public void setLaunch(Integer launch) {
        this.launch = launch;
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

    public Integer getSleep() {
        return sleep;
    }

    public void setSleep(Integer sleep) {
        this.sleep = sleep;
    }

    public Integer getWakeup() {
        return wakeup;
    }

    public void setWakeup(Integer wakeup) {
        this.wakeup = wakeup;
    }

    public Integer getNight() {
        return night;
    }

    public void setNight(Integer night) {
        this.night = night;
    }

    public Integer getWorkout() {
        return workout;
    }

    public void setWorkout(Integer workout) {
        this.workout = workout;
    }

    public Integer getHypogly() {
        return hypogly;
    }

    public void setHypogly(Integer hypogly) {
        this.hypogly = hypogly;
    }

    public Integer getHypergly() {
        return hypergly;
    }

    public void setHypergly(Integer hypergly) {
        this.hypergly = hypergly;
    }

    public Integer getAtwork() {
        return atwork;
    }

    public void setAtwork(Integer atwork) {
        this.atwork = atwork;
    }

    public Integer getAthome() {
        return athome;
    }

    public void setAthome(Integer athome) {
        this.athome = athome;
    }

    public Integer getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(Integer alcohol) {
        this.alcohol = alcohol;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public void setGlucide(Double _glucide ) { this.glucide = _glucide; }

    public void setSlow_insu(String lentus) {

        if (!lentus.isEmpty())
            this.slow_insu = Double.parseDouble(lentus);
        else
            this.slow_insu = 0.00;

    }

    public void setHba1c(String hba1c) {

        if (!hba1c.isEmpty())
            this.hba1c = Double.parseDouble(hba1c);
        else
            this.hba1c = 0.00;

    }

    public void setFast_insu(String _fast_insu) {

        if (!_fast_insu.isEmpty())
            this.fast_insu = Double.parseDouble(_fast_insu);
        else
            this.fast_insu = 0.00;

    }

    public void setglycemy(String _glycemy) {

        if (!_glycemy.isEmpty())
            this.glycemy = Double.parseDouble(_glycemy);
        else
            this.glycemy = 0.00;

    }

    public void setglycemy(Double _glycemy) { this.glycemy = _glycemy; }

    private int    getMonthint(String month)
    {
        if (month.contains("Janvier"))
            return (1);
        else if (month.contains("Fevrier"))
            return (2);
        else if (month.contains("Mars"))
            return (3);
        else if (month.contains("Avril"))
            return (4);
        else if (month.contains("Mai"))
            return (5);
        else if (month.contains("Juin"))
            return (6);
        else if (month.contains("Juillet"))
            return (7);
        else if (month.contains("Aout"))
            return (8);
        else if (month.contains("Septembre"))
            return (9);
        else if (month.contains("Octobre"))
            return (10);
        else if (month.contains("Novembre"))
            return (11);
        else if (month.contains("Decembre"))
            return (12);
        return (-1);
    }

    @Override
    public String toString() {
        String str = "idUser = " + idUser + "\n" +
                "title = " + title + "\n" +
                "place = " + place + "\n" +
                "glucide = " + glucide + "\n" +
                "activity = " + activity + "\n" +
                "activityType = " + activityType + "\n" +
                "notes = " + notes + "\n" +
                "dateCreation = " + dateCreation + "\n" +
                "fastInsu = " + fast_insu + "\n" +
                "slowInsu = " + slow_insu + "\n" +
                "hba1c = " + hba1c + "\n" +
                "hour = " + hour + "\n" +
                "glycemy = " + glycemy + "\n" +
                "dateEdition = " + dateEdition + "\n";
        return (str);
    }

public enum TimeDay{
        BEFORE(0),
        AFTER(1),
        INSULINE(2);

        private final int value;

        private TimeDay(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}