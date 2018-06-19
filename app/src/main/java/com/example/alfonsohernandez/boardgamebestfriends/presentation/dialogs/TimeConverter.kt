package com.example.alfonsohernandez.boardgamebestfriends.presentation.dialogs

import android.content.Context
import com.example.alfonsohernandez.boardgamebestfriends.R
import java.text.SimpleDateFormat

class TimeConverter (private val context: Context) {

    fun convertHoursToString(openMor: String, closeMor: String, aftDif: Boolean, openAft: String, closeAft: String): String{
        if (!openMor.equals("") && !closeMor.equals("") && (openMor.substring(0,2).toInt() < closeMor.substring(0,2).toInt() || (openMor.substring(0,2).toInt() == closeMor.substring(0,2).toInt() && openMor.substring(3,5).toInt() < closeMor.substring(3,5).toInt()))) {
            return convertHours(openMor, closeMor, aftDif, openAft, closeAft)
        } else {
            return ""
        }
    }

    fun convertDaysToString(mon: Boolean, tue: Boolean, wed: Boolean, thu: Boolean, fri: Boolean, sat: Boolean, sun: Boolean): String {
        if (mon || tue || wed || thu || fri || sat || sun) {
            return convertDays(generateDayList(mon, tue, wed, thu, fri, sat, sun))
        } else {
            return ""
        }
    }

    fun convertDays(days: java.util.ArrayList<Boolean>): String{
        var daysString = ""
        for(i in 0..6){
            if(days.get(i)) {
                if(i == 0){
                    if(days.get(i + 1))
                        daysString = daysString + getDayString(i) + "-"
                    else
                        daysString = daysString + getDayString(i) + " "
                }else if(i == 6){
                    daysString = daysString + getDayString(i)
                }else {
                    if (!days.get(i - 1) && !days.get(i + 1))
                        daysString = daysString + getDayString(i) + " "
                    if (!days.get(i - 1) && days.get(i + 1))
                        daysString = daysString + getDayString(i) + "-"
                    if (days.get(i - 1) && !days.get(i + 1))
                        daysString = daysString + getDayString(i) + " "
                }
            }
        }
        return daysString
    }

    fun generateDayList(mon: Boolean,
                        tue: Boolean,
                        wed: Boolean,
                        thu: Boolean,
                        fri: Boolean,
                        sat: Boolean,
                        sun: Boolean): java.util.ArrayList<Boolean> {

        var listDaysWeek = arrayListOf<Boolean>()

        listDaysWeek.add(mon)
        listDaysWeek.add(tue)
        listDaysWeek.add(wed)
        listDaysWeek.add(thu)
        listDaysWeek.add(fri)
        listDaysWeek.add(sat)
        listDaysWeek.add(sun)

        return listDaysWeek
    }

    fun convertHours(openMor: String, closeMor: String, aftDif: Boolean, openAft: String, closeAft: String): String{
        var hours = openMor + "-" + closeMor
        if(aftDif)
            hours = hours + " " + openAft + "-" + closeAft
        return hours
    }

    fun getDayString(day: Int): String{
        when(day){
            0 -> return context.getString(R.string.openingHoursMon)
            1 -> return context.getString(R.string.openingHoursTue)
            2 -> return context.getString(R.string.openingHoursWed)
            3 -> return context.getString(R.string.openingHoursThu)
            4 -> return context.getString(R.string.openingHoursFri)
            5 -> return context.getString(R.string.openingHoursSat)
            6 -> return context.getString(R.string.openingHoursSun)
        }
        return ""
    }

}