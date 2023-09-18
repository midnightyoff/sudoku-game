package sudokugame;

public class Time {
    private int second;
    private int minute;

    public Time(int minute, int second){
        this.minute = minute;
        this.second = second;
    }

    public Time(String currentTime){
        String[] time = currentTime.split(":");
        minute = Integer.parseInt(time[0]);
        second = Integer.parseInt(time[1]);
    }

    public String getCurrentTime(){
        return minute + ":" + second;
    }

    public void oneSecondPassed(){
        second++;
        if (second == 60){
            minute++;
            second = 0;
            if (minute == 60){
                minute = 0;
            }
        }
    }

    @Override
    public String  toString(){
        String time = "";
        if (minute < 10) time += "0" + minute;
        else time += minute;
        if (second < 10) time += ":0" + second;
        else time += ":" + second;
        return time;
    }
}
