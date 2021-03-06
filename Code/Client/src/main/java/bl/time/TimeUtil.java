package bl.time;

import blservice.time.TimeService;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ZhangYF on 2016/8/29.
 */
public class TimeUtil {

    private static int allTimeLength = 240;


    public static int TimeSliceNum = 48;


    public static int getCurrentHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public static int getCurrentMin() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    /**
     * 获取当前时间片
     * @return 当前为第几个时间片，如果不在交易时间段，若在开市前或者中午返回-1，
     * 若在当天交易结束后则返回-2
     */
    public static int getCurrentIime(){
        TimeService time = new TimeBlImpl();
        Calendar calendar = null;
        try {
            calendar = time.getCurrentTime();
        } catch (IOException e) {
            e.printStackTrace();
            calendar = Calendar.getInstance();
            return timeToNode(calendar);
        }
        return timeToNode(calendar);
    }

    /**
     * 将calender中的时间转换为对应时间片
     * @param calendar
     * @return calendar时间为第几个时间片，如果不在交易时间段，若在开市前或者中午返回-1，
     * 若在当天交易结束后则返回-2
     */
    public static int timeToNode(Calendar calendar){
        int timeNum = 48;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        if(hour>=9 && hour<=11){
            if(hour==9 && minute<30){
                return -1;
            }else if(hour==11 && minute>30){
                return 25;
            }else{
                return ((hour-9)*60+minute-30)/timeNumTOLength(timeNum)+1;
            }
        }else if(hour == 12){
            return 25;
        }
        if(hour>=13 && hour <15){
            return ((hour-13)*60+minute)/timeNumTOLength(timeNum)+timeNum/2+1;
        }else if(hour==15 && minute ==0){
            return 49;
        }else if(hour>=15){
            return -2;
        }else{
            return -1;
        }
    }

    /**
     * 将时间片数量转换为时间片长度，单位：分钟
     * @param timeNum 时间片数量
     * @return 时间片长度
     */
    private static int timeNumTOLength(int timeNum){
        return allTimeLength/timeNum;
    }

    /**
     * 将时间片长度转换为时间片总数，
     * @param timeLength 时间片长度，单位：分钟
     * @return 时间片数量
     */
    public static int timeLengthToNum(int timeLength){
        return allTimeLength/timeLength;
    }

    /**
     * 将时间片转化为对应时间
     * @param timeNode 时间片
     * @return yyyy-MM-dd HH:mm:ss格式时间 如果超出范围返回null
     */
    public static String timeNodeToDate(int timeNode){
        if(timeNode>TimeSliceNum){
            return null;
        }

        Calendar calendar=Calendar.getInstance();
        int timeLength = allTimeLength/TimeSliceNum;
        if(timeNode<=(TimeSliceNum/2)){
            calendar.set(Calendar.HOUR_OF_DAY,9);
            calendar.set(Calendar.MINUTE,30);
            calendar.add(Calendar.MINUTE,timeLength*(timeNode-1));
        }else{
            calendar.set(Calendar.HOUR_OF_DAY,13);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,1);
            calendar.add(Calendar.MINUTE,timeLength*(timeNode - TimeSliceNum/2 -1));
        }
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = calendar.getTime();
        String time = df.format(date);

        return time;
    }

    public static Date getLastworkDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        if (calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
            calendar.add(Calendar.DATE,-2);
        }else if (calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY){
            calendar.add(Calendar.DATE,-1);
        }

        return calendar.getTime();
    }

    public static String getLastworkDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        if (calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
            calendar.add(Calendar.DATE,-2);
        }else if (calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY){
            calendar.add(Calendar.DATE,-1);
        }

        return dateFormat.format(calendar.getTime());
    }

    /**
     * 判断时间是否股票处于交易时间之内，采用24小时制
     * 交易时间 9:30 - 11: 30, 13:00 - 15:00
     * @param hour 小时
     * @param minute 分钟
     * @return
     */
    public static boolean isAtTradeTime(int hour, int minute) {
        int start1 = 9 * 60 + 30, end1 = 11 * 60 + 30;
        int start2 = 13 * 60, end2 = 15 * 60;
        int time = hour * 60 + minute;
        return (time >= start1 && time <= end1) ||
                (time >= start2 && time <= end2);
    }

    public static boolean isAfterTradeTime(int hour, int minute) {
        return !isLessThan(hour, minute, 15, 0);
    }

    /**
     * 验证时间是否有效
     * @param hour 小时
     * @param minute 分钟
     * @return boolean
     */
    public static boolean isTimeValid(int hour, int minute) {
        return hour >= 0 && hour < 24 && minute >= 0 && minute <= 59;
    }

    /**
     * 比较两个时间的大小
     * @param hour1 时间1的小时
     * @param minute1 时间1的分钟
     * @param hour2 时间2的小时
     * @param minute2 时间2的分钟
     * @return
     */
    public static boolean isLessThan(int hour1, int minute1, int hour2, int minute2) {
        int time1 = hour1 * 60 + minute1;
        int time2 = hour2 * 60 + minute2;
        return time1 < time2;
    }
}
