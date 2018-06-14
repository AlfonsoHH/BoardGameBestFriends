package com.example.alfonsohernandez.boardgamebestfriends.presentation.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region

object DialogFactory {

    fun buildConfirmDialog(context: Context, msg: String, listener: Runnable?): Dialog {

        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setMessage(msg)

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.groupsYes), { dialogInterface, i ->
            alertDialog.dismiss()
            listener?.run()
        })

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.groupsNo), { dialogInterface, i -> alertDialog.dismiss() })

        return alertDialog
    }

    fun buildConfirmDialogTitle(context: Context, title: String, msg: String, listener: Runnable?): Dialog {

        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle(title)
        alertDialog.setMessage(msg)

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.groupsYes), { dialogInterface, i ->
            alertDialog.dismiss()
            listener?.run()
        })

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.groupsNo), { dialogInterface, i -> alertDialog.dismiss() })

        return alertDialog
    }

    //SINGLE INPUT DIALOG

    var callbackInput: DialogInputCallback? = null

    interface DialogInputCallback{
        fun getDialogInput(input: String)
    }

    fun buildInputDialog(context: Context, title: String): Dialog {

        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle(title)

        val input = EditText(context)
        input.inputType = InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS
        alertDialog.setView(input)

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.groupsYes),object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog?.dismiss()
                callbackInput?.getDialogInput(input.text.toString())
            }
        })

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.groupsNo), { dialogInterface, i -> alertDialog.dismiss() })

        return alertDialog
    }

    //CHOOSE REGION

    var callbackCity: CitySelectedCallback? = null

    interface CitySelectedCallback {
        fun onCitySelectedChoosed(city: String)
    }

    fun buildChooseRegionDialog(context: Context, regionList: ArrayList<Region>): Dialog{
        val spinnerArrayAdapterCity: ArrayAdapter<String>
        val countryList = getCountryList(regionList)
        val cityList = getCityList(regionList, countryList.get(0))

        val builder = AlertDialog.Builder(context)
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.dialog_region,null)
        builder.setView(view)

        val spinnerCountry: Spinner = view.findViewById(R.id.dialogRegionScountry)
        val spinnerCity: Spinner = view.findViewById(R.id.dialogRegionScity)

        val spinnerArrayAdapterCountry = ArrayAdapter(context, android.R.layout.simple_spinner_item, countryList)
        spinnerArrayAdapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCountry.setAdapter(spinnerArrayAdapterCountry)

        spinnerArrayAdapterCity = ArrayAdapter(context, android.R.layout.simple_spinner_item, cityList)
        spinnerArrayAdapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCity.setAdapter(spinnerArrayAdapterCity)

        spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                spinnerArrayAdapterCity.clear()
                spinnerArrayAdapterCity.addAll(getCityList(regionList, spinnerCountry.selectedItem.toString()))
            }
            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        builder.setTitle(context.getString(R.string.loginRegionDialogTitle))
        builder.setPositiveButton(context.getString(R.string.loginDialogPositive),object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog?.dismiss()
                callbackCity?.onCitySelectedChoosed(spinnerCity.selectedItem.toString())
            }
        })
        builder.setNegativeButton(context.getString(R.string.loginDialogNegative),object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog?.dismiss()
            }
        })
        val dialog: Dialog = builder.create()
        return dialog
    }

    private fun getCountryList(regionList: ArrayList<Region>): ArrayList<String> {
        val countryList = arrayListOf<String>()
        for(region in regionList){
            var exist = false
            for(country in countryList) {
                if (country.equals(region.country))
                    exist = true
            }
            if(!exist)
                countryList.add(region.country)
        }
        return countryList
    }

    private fun getCityList(regionList: ArrayList<Region>, countryName: String): ArrayList<String> {
        val cityList = arrayListOf<String>()
        for(region in regionList){
            if(region.country.equals(countryName))
                cityList.add(region.city)
        }
        return cityList
    }

}