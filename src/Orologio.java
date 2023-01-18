import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

public class Orologio implements Runnable {

        Thread t1 = null;
        int hours = 0, minutes = 0, seconds = 0;
        String timeString = "";

        public void DigitalWatch() {
            t1 = new Thread(this);
            t1.start();
        }

        public void run() {
            try {
                while (true) {

                    LocalDateTime currentTime = LocalDateTime.now();
                    Month month = currentTime.getMonth();
                    int day = currentTime.getDayOfMonth();
                    int year = currentTime.getYear();
                    DayOfWeek dayOfWeek = currentTime.getDayOfWeek();

                    Calendar cal = Calendar.getInstance();
                    hours = cal.get(Calendar.HOUR_OF_DAY);
                    if (hours > 12)
                        hours -= 12;
                    minutes = cal.get(Calendar.MINUTE);
                    seconds = cal.get(Calendar.SECOND);

                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss");
                    Date date = cal.getTime();
                    timeString = formatter.format(date);

                    printTime();

                    t1.sleep(10);
                    printTime();// interval given in milliseconds
                }
            } catch (Exception e) {
            }
        }

        public void printTime() {
            System.out.println(timeString);
        }



}