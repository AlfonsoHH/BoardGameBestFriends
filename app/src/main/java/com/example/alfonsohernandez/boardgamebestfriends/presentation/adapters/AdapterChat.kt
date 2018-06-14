package com.example.alfonsohernandez.boardgamebestfriends.presentation.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Message
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import jp.wasabeef.glide.transformations.CropCircleTransformation
import timber.log.Timber

class AdapterChat: RecyclerView.Adapter<AdapterChat.ViewHolder>() {
    var messageList: ArrayList<Message> = arrayListOf()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Timber.e("Pos: $position with ${messageList.get(position)}")
        holder.bindItem(messageList.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_message, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtUser: TextView? = null
        var llOther: LinearLayout? = null
        var txtBody: TextView? = null
        var txtDate: TextView? = null
        var llMe: LinearLayout? = null
        var txtBodyMe: TextView? = null
        var txtDateMe: TextView? = null
        var photo: ImageView? = null

        init {
            txtUser = itemView.findViewById<TextView>(R.id.itemMessageUser)
            llOther = itemView.findViewById<LinearLayout>(R.id.itemMessageLLother)
            txtBody = itemView.findViewById<TextView>(R.id.itemMessageText)
            txtDate = itemView.findViewById<TextView>(R.id.itemMessageDate)
            llMe = itemView.findViewById<LinearLayout>(R.id.itemMessageLLme)
            txtBodyMe = itemView.findViewById<TextView>(R.id.itemMessageTextMe)
            txtDateMe = itemView.findViewById<TextView>(R.id.itemMessageDateMe)
            photo = itemView.findViewById<ImageView>(R.id.itemMessageIVphoto)
        }


        fun bindItem(message: Message) {
            Timber.e("message -> ${message.text}")

            message.user?.let {
                if (it) {
                    llMe?.setVisibility(true)
                    llOther?.setVisibility(false)
                    txtBodyMe?.text = message.text
                    txtDateMe?.text = message.date
                    photo?.setVisibility(false)
                    txtUser?.setVisibility(false)
                } else {
                    txtUser?.text = message.userName
                    txtBody?.text = message.text
                    txtDate?.text = message.date
                    photo?.setVisibility(true)
                    txtUser?.setVisibility(true)
                    llMe?.setVisibility(false)
                    llOther?.setVisibility(true)

                    if (!message.userPhoto.equals("url")) {
                        Glide.with(itemView)
                                .load(message.userPhoto)
                                .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                                .into(photo ?: return)
                    } else {

                    }
                }
            }
        }
    }
}