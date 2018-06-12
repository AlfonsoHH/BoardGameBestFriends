package com.example.alfonsohernandez.boardgamebestfriends.presentation.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.LinearLayout
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object OpenHoursDialog {

    fun createDialog(context: Context){

        val builder = AlertDialog.Builder(context)
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.dialog_opening_hours,null)
        builder.setView(view)

        val mon1: CheckBox = view.findViewById(R.id.openingHoursCmon1)
        val mon2: CheckBox = view.findViewById(R.id.openingHoursCmon2)
        val mon3: CheckBox = view.findViewById(R.id.openingHoursCmon3)
        val tue1: CheckBox = view.findViewById(R.id.openingHoursCtue1)
        val tue2: CheckBox = view.findViewById(R.id.openingHoursCtue2)
        val tue3: CheckBox = view.findViewById(R.id.openingHoursCtue3)
        val wed1: CheckBox = view.findViewById(R.id.openingHoursCwed1)
        val wed2: CheckBox = view.findViewById(R.id.openingHoursCwed2)
        val wed3: CheckBox = view.findViewById(R.id.openingHoursCwed3)
        val thu1: CheckBox = view.findViewById(R.id.openingHoursCthu1)
        val thu2: CheckBox = view.findViewById(R.id.openingHoursCthu2)
        val thu3: CheckBox = view.findViewById(R.id.openingHoursCthu3)
        val fri1: CheckBox = view.findViewById(R.id.openingHoursCfri1)
        val fri2: CheckBox = view.findViewById(R.id.openingHoursCfri2)
        val fri3: CheckBox = view.findViewById(R.id.openingHoursCfri3)
        val sat1: CheckBox = view.findViewById(R.id.openingHoursCsat1)
        val sat2: CheckBox = view.findViewById(R.id.openingHoursCsat2)
        val sat3: CheckBox = view.findViewById(R.id.openingHoursCsat3)
        val sun1: CheckBox = view.findViewById(R.id.openingHoursCsun1)
        val sun2: CheckBox = view.findViewById(R.id.openingHoursCsun2)
        val sun3: CheckBox = view.findViewById(R.id.openingHoursCsun3)

        val aft1: CheckBox = view.findViewById(R.id.openingHoursCdifAft1)
        val aft2: CheckBox = view.findViewById(R.id.openingHoursCdifAft2)
        val aft3: CheckBox = view.findViewById(R.id.openingHoursCdifAft3)

        val aft1LL: LinearLayout = view.findViewById(R.id.openingHoursLLaft1)
        val aft2LL: LinearLayout = view.findViewById(R.id.openingHoursLLaft2)
        val aft3LL: LinearLayout = view.findViewById(R.id.openingHoursLLaft3)

        val openMor1: EditText = view.findViewById(R.id.openingHoursETopenMor1)
        val openMor2: EditText = view.findViewById(R.id.openingHoursETopenMor2)
        val openMor3: EditText = view.findViewById(R.id.openingHoursETopenMor3)
        val openAft1: EditText = view.findViewById(R.id.openingHoursETopenAft1)
        val openAft2: EditText = view.findViewById(R.id.openingHoursETopenAft2)
        val openAft3: EditText = view.findViewById(R.id.openingHoursETopenAft3)
        val closeMor1: EditText = view.findViewById(R.id.openingHoursETcloseMor1)
        val closeMor2: EditText = view.findViewById(R.id.openingHoursETcloseMor2)
        val closeMor3: EditText = view.findViewById(R.id.openingHoursETcloseMor3)
        val closeAft1: EditText = view.findViewById(R.id.openingHoursETcloseAft1)
        val closeAft2: EditText = view.findViewById(R.id.openingHoursETcloseAft2)
        val closeAft3: EditText = view.findViewById(R.id.openingHoursETcloseAft3)

        aft1.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(aft1.isChecked){
                    aft1LL.setVisibility(true)
                }else{
                    aft1LL.setVisibility(false)
                }
            }
        })

        aft2.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(aft2.isChecked){
                    aft2LL.setVisibility(true)
                }else{
                    aft2LL.setVisibility(false)
                }
            }
        })

        aft3.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(aft3.isChecked){
                    aft3LL.setVisibility(true)
                }else{
                    aft3LL.setVisibility(false)
                }
            }
        })

        val cal = Calendar.getInstance()

        val sdf = SimpleDateFormat("HH:mm")

        val timeSetListener1 = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            openMor1.setText(sdf.format(cal.time))
        }

        openMor1.setOnClickListener { TimePickerDialog(context, timeSetListener1, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show() }

        val timeSetListener2 = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            openMor2.setText(sdf.format(cal.time))
        }

        openMor2.setOnClickListener { TimePickerDialog(context, timeSetListener2, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show() }

        val timeSetListener3 = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            openMor3.setText(sdf.format(cal.time))
        }

        openMor3.setOnClickListener { TimePickerDialog(context, timeSetListener3, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show() }

        val timeSetListener4 = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            openAft1.setText(sdf.format(cal.time))
        }

        openAft1.setOnClickListener { TimePickerDialog(context, timeSetListener4, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show() }

        val timeSetListener5 = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            openAft2.setText(sdf.format(cal.time))
        }

        openAft2.setOnClickListener { TimePickerDialog(context, timeSetListener5, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show() }

        val timeSetListener6 = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            openAft3.setText(sdf.format(cal.time))
        }

        openAft3.setOnClickListener { TimePickerDialog(context, timeSetListener6, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show() }

        val timeSetListener7 = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            closeMor1.setText(sdf.format(cal.time))
        }

        closeMor1.setOnClickListener { TimePickerDialog(context, timeSetListener7, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show() }

        val timeSetListener8 = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            closeMor2.setText(sdf.format(cal.time))
        }

        closeMor2.setOnClickListener { TimePickerDialog(context, timeSetListener8, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show() }

        val timeSetListener9 = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            closeMor3.setText(sdf.format(cal.time))
        }

        closeMor3.setOnClickListener { TimePickerDialog(context, timeSetListener9, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show() }

        val timeSetListener10 = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            closeAft1.setText(sdf.format(cal.time))
        }

        closeAft1.setOnClickListener { TimePickerDialog(context, timeSetListener10, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show() }

        val timeSetListener11 = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            closeAft2.setText(sdf.format(cal.time))
        }

        closeAft2.setOnClickListener { TimePickerDialog(context, timeSetListener11, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show() }

        val timeSetListener12 = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            closeAft3.setText(sdf.format(cal.time))
        }

        closeAft3.setOnClickListener { TimePickerDialog(context, timeSetListener12, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show() }


        builder.setPositiveButton(context.getString(R.string.openingHoursAccept),object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                val listOpeningHours = arrayListOf<String>()
                listOpeningHours.add(TimeConverter(context).convertDaysToString(mon1.isChecked,tue1.isChecked,wed1.isChecked,thu1.isChecked,fri1.isChecked,sat1.isChecked,sun1.isChecked))
                listOpeningHours.add(TimeConverter(context).convertHoursToString(openMor1.text.toString(),closeMor1.text.toString(),aft1.isChecked,openAft1.text.toString(),closeAft1.text.toString()))
                listOpeningHours.add(TimeConverter(context).convertDaysToString(mon2.isChecked,tue2.isChecked,wed2.isChecked,thu2.isChecked,fri2.isChecked,sat2.isChecked,sun2.isChecked))
                listOpeningHours.add(TimeConverter(context).convertHoursToString(openMor2.text.toString(),closeMor2.text.toString(),aft2.isChecked,openAft2.text.toString(),closeAft2.text.toString()))
                listOpeningHours.add(TimeConverter(context).convertDaysToString(mon3.isChecked,tue3.isChecked,wed3.isChecked,thu3.isChecked,fri3.isChecked,sat3.isChecked,sun3.isChecked))
                listOpeningHours.add(TimeConverter(context).convertHoursToString(openMor3.text.toString(),closeMor3.text.toString(),aft3.isChecked,openAft3.text.toString(),closeAft3.text.toString()))
                callbackOpeningHours?.onOpeningHoursChoosed(listOpeningHours)
            }
        })

        builder.setNegativeButton(context.getString(R.string.openingHoursCancel),object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog?.dismiss()
            }
        })



        val dialog: Dialog = builder.create()
        dialog.show()
    }



    var callbackOpeningHours: OpeningHoursCallback? = null

    interface OpeningHoursCallback {
        fun onOpeningHoursChoosed(openingHours: ArrayList<String>)
    }

}