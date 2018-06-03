package com.example.alfonsohernandez.boardgamebestfriends.presentation.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.RecyclerView
import android.transition.Transition
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import java.text.SimpleDateFormat

/**
 * Created by alfonsohernandez on 15/03/2018.
 */

class AdapterMeetings: RecyclerView.Adapter<AdapterMeetings.ViewHolder>() {

    private val TAG = "AdapterMeetings"

    var meetingsList: ArrayList<Meeting> = arrayListOf()

    var onMeetingClickedListener: ((Meeting) -> Unit)? = null
    var onLongClickListener: ((Meeting) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(meetingsList!!.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_meetings, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return meetingsList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(meeting: Meeting) {

            val txtTitle = itemView.findViewById<TextView>(R.id.itemMeetingsTVtitle)
            val txtDescription = itemView.findViewById<TextView>(R.id.itemMeetingsTVdescription)
            val txtHour = itemView.findViewById<TextView>(R.id.itemMeetingsTVhour)
            val txtDate = itemView.findViewById<TextView>(R.id.itemMeetingsTVdate)
            val txtVacants = itemView.findViewById<TextView>(R.id.itemMeetingsTVvacants)
            val photoGame = itemView.findViewById<ImageView>(R.id.itemMeetingsImageViewGame)
            val photoPlace = itemView.findViewById<ImageView>(R.id.itemMeetingsImageViewPlace)

            txtTitle?.text = meeting.title
            txtDescription?.text = meeting.description
            txtHour?.text = meeting.date.substring(0,meeting.date.lastIndexOf("_"))
            txtDate?.text = meeting.date.substring(meeting.date.lastIndexOf("_")+1,meeting.date.length)
            txtVacants?.text = meeting.vacants.toString() + " Vacants"

            if(meeting.label!=null) {

                val hostingLL = itemView.findViewById<LinearLayout>(R.id.itemMeetingsLLhosting)
                val openTV = itemView.findViewById<TextView>(R.id.itemMeetingsTVopen)
                val groupTV = itemView.findViewById<TextView>(R.id.itemMeetingsTVgroup)
                val hostingTV = itemView.findViewById<TextView>(R.id.itemMeetingsTVhosting)
                val assistingTV = itemView.findViewById<TextView>(R.id.itemMeetingsTVassisting)
                val hostingAssistingTV = itemView.findViewById<TextView>(R.id.itemMeetingsTVhostingAssisting)
                val adminTV = itemView.findViewById<TextView>(R.id.itemMeetingsTVadmin)
                val adminHostTV = itemView.findViewById<TextView>(R.id.itemMeetingsTVadminHost)
                val adminPlayingTV = itemView.findViewById<TextView>(R.id.itemMeetingsTVadminPlaying)
                val adminHostPlayingTV = itemView.findViewById<TextView>(R.id.itemMeetingsTVadminHostPlaying)

                hostingLL.setVisibility(true)

                if (meeting.label.equals("Open"))
                    openTV.setVisibility(true)
                if (meeting.label.equals("Group"))
                    groupTV.setVisibility(true)
                if (meeting.label.equals("Host"))
                    hostingTV.setVisibility(true)
                if (meeting.label.equals("Playing"))
                    assistingTV.setVisibility(true)
                if (meeting.label.equals("Admin"))
                    adminTV.setVisibility(true)
                if (meeting.label.equals("Admin Host"))
                    adminHostTV.setVisibility(true)
                if (meeting.label.equals("Admin Playing"))
                    adminPlayingTV.setVisibility(true)
                if (meeting.label.equals("Playing Host"))
                    hostingAssistingTV.setVisibility(true)
                if (meeting.label.equals("Admin Playing Host"))
                    adminHostPlayingTV.setVisibility(true)
            }

            Glide.with(itemView).asBitmap().load(meeting.gamePhoto).into(object : SimpleTarget<Bitmap>(120,120) {
                override fun onResourceReady(resource: Bitmap, transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?) {
                    val roundedDrawable = RoundedBitmapDrawableFactory.create(itemView!!.resources, resource)
                    roundedDrawable.isCircular = true
                    photoGame.setImageDrawable(roundedDrawable)
                }
            })

            if(!meeting.placePhoto.equals("url")) {
                val decodedString2 = Base64.decode(meeting.placePhoto, Base64.DEFAULT)
                val imagenJug2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.size)
                Bitmap.createScaledBitmap(imagenJug2, 90, 90, false)
                val roundedBitmapDrawable2 = RoundedBitmapDrawableFactory.create(itemView.getResources(), imagenJug2)
                roundedBitmapDrawable2.isCircular = true

                photoPlace?.setImageDrawable(roundedBitmapDrawable2)
            }

            itemView.setOnClickListener({
                onMeetingClickedListener?.invoke(meeting)
            })

            itemView.setOnLongClickListener({
                onLongClickListener?.invoke(meeting)
                true
            })
        }
    }
}