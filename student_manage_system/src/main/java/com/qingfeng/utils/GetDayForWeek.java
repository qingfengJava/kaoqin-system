package com.qingfeng.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @author 清风学Java
 * @date 2021/11/17
 * @apiNote
 */
public class GetDayForWeek {

    public static String getDateDayForWeek(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayForWeek = 0;
        if(c.get(Calendar.DAY_OF_WEEK) == 1){
            dayForWeek = 7;
        }else{
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        String getDayForWeek = "";
        switch (dayForWeek){
            case 1: getDayForWeek =  "星期一"; break;
            case 2: getDayForWeek =  "星期二"; break;
            case 3: getDayForWeek =  "星期三"; break;
            case 4: getDayForWeek =  "星期四"; break;
            case 5: getDayForWeek =  "星期五"; break;
            case 6: getDayForWeek =  "星期六"; break;
            case 7: getDayForWeek =  "星期天"; break;
        }
        return getDayForWeek;
    }
}
