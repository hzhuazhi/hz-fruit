package com.hz.fruit.master.core.controller;


import java.util.Calendar;
import java.util.Date;

/**
 * @Description TODO
 * @Author yoko
 * @Date 2020/5/9 19:43
 * @Version 1.0
 */
public class Temp {
    public static void main(String[] args) {



    }

    /**
     * 判断时间是否在时间段内
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        //设置当前时间
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        //设置开始时间
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        //设置结束时间
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        //处于开始时间之后，和结束时间之前的判断
        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

//    public static int getGroupNumByMoney(String balance, String groupMinMoney, int groupNum){
//        int num = 0;
//        double balance_db = Double.parseDouble(balance);
//        double groupMinMoney_db = Double.parseDouble(groupMinMoney);
//
//        double res = balance_db/groupMinMoney_db;
//        int res_num = (int) Math.floor(res);
//        if (res_num <= 0){
//            num = 0;
//        }else{
//            if (res_num <= groupNum){
//                num = 0;
//            }else{
//                num = res_num - groupNum;
//            }
//        }
//        return num;
//    }



}
