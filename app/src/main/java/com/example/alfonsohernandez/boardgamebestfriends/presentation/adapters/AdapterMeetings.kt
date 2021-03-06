package com.example.alfonsohernandez.boardgamebestfriends.presentation.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import jp.wasabeef.glide.transformations.CropCircleTransformation

/**
 * Created by alfonsohernandez on 15/03/2018.
 */

class AdapterMeetings : RecyclerView.Adapter<AdapterMeetings.ViewHolder>() {

    private val TAG = "AdapterMeetings"

    var meetingsList: ArrayList<Meeting> = arrayListOf()

    var onMeetingClickedListener: ((Meeting) -> Unit)? = null
    var onLongClickListener: ((Meeting) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(meetingsList.get(position))
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

            val wholeItem = itemView.findViewById<FrameLayout>(R.id.wholeItem)

            if (meeting.id.equals("blank")){
                wholeItem.setVisibility(false)
            }else {
                wholeItem.setVisibility(true)

                txtTitle?.text = meeting.title
                txtDescription?.text = meeting.description
                txtHour?.text = meeting.date.substring(0, meeting.date.lastIndexOf("_"))
                txtDate?.text = meeting.date.substring(meeting.date.lastIndexOf("_") + 1, meeting.date.length)
                txtVacants?.text = meeting.vacants.toString() + " Vacants"

                if (meeting.label != null) {

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

                    if (meeting.label.equals("Open")) {
                        openTV.setVisibility(true)
                        groupTV.setVisibility(false)
                        hostingTV.setVisibility(false)
                        assistingTV.setVisibility(false)
                        adminTV.setVisibility(false)
                        adminHostTV.setVisibility(false)
                        adminPlayingTV.setVisibility(false)
                        hostingAssistingTV.setVisibility(false)
                        adminHostPlayingTV.setVisibility(false)
                    }
                    if (meeting.label.equals("Group")) {
                        openTV.setVisibility(false)
                        groupTV.setVisibility(true)
                        hostingTV.setVisibility(false)
                        assistingTV.setVisibility(false)
                        adminTV.setVisibility(false)
                        adminHostTV.setVisibility(false)
                        adminPlayingTV.setVisibility(false)
                        hostingAssistingTV.setVisibility(false)
                        adminHostPlayingTV.setVisibility(false)
                    }
                    if (meeting.label.equals("Host")) {
                        openTV.setVisibility(false)
                        groupTV.setVisibility(false)
                        hostingTV.setVisibility(true)
                        assistingTV.setVisibility(false)
                        adminTV.setVisibility(false)
                        adminHostTV.setVisibility(false)
                        adminPlayingTV.setVisibility(false)
                        hostingAssistingTV.setVisibility(false)
                        adminHostPlayingTV.setVisibility(false)
                    }
                    if (meeting.label.equals("Playing")) {
                        openTV.setVisibility(false)
                        groupTV.setVisibility(false)
                        hostingTV.setVisibility(false)
                        assistingTV.setVisibility(true)
                        adminTV.setVisibility(false)
                        adminHostTV.setVisibility(false)
                        adminPlayingTV.setVisibility(false)
                        hostingAssistingTV.setVisibility(false)
                        adminHostPlayingTV.setVisibility(false)
                    }
                    if (meeting.label.equals("Admin")) {
                        openTV.setVisibility(false)
                        groupTV.setVisibility(false)
                        hostingTV.setVisibility(false)
                        assistingTV.setVisibility(false)
                        adminTV.setVisibility(true)
                        adminHostTV.setVisibility(false)
                        adminPlayingTV.setVisibility(false)
                        hostingAssistingTV.setVisibility(false)
                        adminHostPlayingTV.setVisibility(false)
                    }
                    if (meeting.label.equals("Admin Host")) {
                        openTV.setVisibility(false)
                        groupTV.setVisibility(false)
                        hostingTV.setVisibility(false)
                        assistingTV.setVisibility(false)
                        adminTV.setVisibility(false)
                        adminHostTV.setVisibility(true)
                        adminPlayingTV.setVisibility(false)
                        hostingAssistingTV.setVisibility(false)
                        adminHostPlayingTV.setVisibility(false)
                    }
                    if (meeting.label.equals("Admin Playing")) {
                        openTV.setVisibility(false)
                        groupTV.setVisibility(false)
                        hostingTV.setVisibility(false)
                        assistingTV.setVisibility(false)
                        adminTV.setVisibility(false)
                        adminHostTV.setVisibility(false)
                        adminPlayingTV.setVisibility(true)
                        hostingAssistingTV.setVisibility(false)
                        adminHostPlayingTV.setVisibility(false)
                    }
                    if (meeting.label.equals("Playing Host")) {
                        openTV.setVisibility(false)
                        groupTV.setVisibility(false)
                        hostingTV.setVisibility(false)
                        assistingTV.setVisibility(false)
                        adminTV.setVisibility(false)
                        adminHostTV.setVisibility(false)
                        adminPlayingTV.setVisibility(false)
                        hostingAssistingTV.setVisibility(true)
                        adminHostPlayingTV.setVisibility(false)
                    }
                    if (meeting.label.equals("Admin Playing Host")) {
                        openTV.setVisibility(false)
                        groupTV.setVisibility(false)
                        hostingTV.setVisibility(false)
                        assistingTV.setVisibility(false)
                        adminTV.setVisibility(false)
                        adminHostTV.setVisibility(false)
                        adminPlayingTV.setVisibility(false)
                        hostingAssistingTV.setVisibility(false)
                        adminHostPlayingTV.setVisibility(true)
                    }
                }

                Glide.with(itemView)
                        .load(meeting.gamePhoto)
                        .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                        .into(photoGame)

                if (!meeting.placePhoto.equals("url")) {
                    Glide.with(itemView)
                            .load(meeting.placePhoto)
                            .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                            .into(photoPlace)
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
}