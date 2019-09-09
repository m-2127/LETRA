///*
//package com.bitrebels.letra.services;
//
//import com.bitrebels.letra.repository.HolidayRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.Calendar;
//
//
//@Service
//public class LeaveTracker {
//
//    @Autowired
//    HolidayRepo holidayRepo;
//
////    the workDays in below functions are multiplied by 7 considering the
////    number of work hours per day
//
//    public long currentProgress(long completedHours){
//
//        long workDays =0 ;
//
//        LocalDate currentDate = LocalDate.now();
//
//        Calendar currentCal = Calendar.getInstance();
//        currentCal.clear();
//        currentCal.set(currentDate.getYear(), currentDate.getMonthValue()-1, currentDate.getDayOfMonth());
//
//        int day_of_week = currentCal.get(Calendar.DAY_OF_WEEK);
//        if(day_of_week>1 && day_of_week<7){
//            workDays = day_of_week-1;
//        }
//
//
//        return workDays*7 + completedHours;
//    }
//
//    public long requiredOrRemainingWork(LocalDate startDate){
//
//        LocalDate currentDate = LocalDate.now();
//
//        long workDays=0;
//
//        Calendar startCal = Calendar.getInstance();
//        startCal.clear();
//        startCal.set(startDate.getYear(), startDate.getMonthValue()-1, startDate.getDayOfMonth());
//
//        Calendar currentCal = Calendar.getInstance();
//        startCal.clear();
//        startCal.set(currentDate.getYear(), currentDate.getMonthValue()-1, currentDate.getDayOfMonth());
//
//        if (startCal.getTimeInMillis() == currentCal.getTimeInMillis()) {
//            return 0;
//        }
//
//        if (startCal.getTimeInMillis() > currentCal.getTimeInMillis()) {
//            startCal.set(currentDate.getYear(), currentDate.getMonthValue()-1, currentDate.getDayOfMonth());
//            currentCal.set(startDate.getYear(), startDate.getMonthValue()-1, startDate.getDayOfMonth());
//        }
//
//        do {
//            //excluding start date
//            startCal.add(Calendar.DAY_OF_MONTH, 1);
//            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
//                ++workDays;
//            }
//        } while (startCal.getTimeInMillis() < currentCal.getTimeInMillis()); //excluding end date
//
//        workDays = workDays - holidayRepo.countByDateBetween(startDate, currentDate);
//
//        return workDays*7;
//    }
//}
//*/

