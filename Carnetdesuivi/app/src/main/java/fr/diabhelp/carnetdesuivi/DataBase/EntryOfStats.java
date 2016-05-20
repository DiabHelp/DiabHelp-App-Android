package fr.diabhelp.carnetdesuivi.DataBase;

/**
 * Created by naqued on 23/03/16.
 */
public class EntryOfStats {

    private Integer launch;
    private Integer diner;
    private Integer encas;
    private Integer sleep;
    private Integer wakeup;
    private Integer night;
    private Integer workout;
    private Integer hypogly;
    private Integer hypergly;
    private Integer atwork;
    private Integer athome;
    private Integer alcohol;
    private Integer period;
    private Integer breakfast;
    private String  beg_date;
    private String  end_date;

    public String getBeg_date() {
        return beg_date;
    }

    public void setBeg_date(String beg_date) {
        this.beg_date = beg_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
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

    public Integer getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(Integer breakfast) {
        this.breakfast = breakfast;
    }
}
