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

class AdapterChat: RecyclerView.Adapter<AdapterChat.ViewHolder>() {
    var messageList: ArrayList<Message> = arrayListOf()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(messageList.get(position))
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
        fun bindItems(message: Message) {

            val txtUser = itemView.findViewById<TextView>(R.id.itemMessageUser)
            val llOther = itemView.findViewById<LinearLayout>(R.id.itemMessageLLother)
            val txtBody = itemView.findViewById<TextView>(R.id.itemMessageText)
            val txtDate = itemView.findViewById<TextView>(R.id.itemMessageDate)
            val llMe = itemView.findViewById<LinearLayout>(R.id.itemMessageLLme)
            val txtBodyMe = itemView.findViewById<TextView>(R.id.itemMessageTextMe)
            val txtDateMe = itemView.findViewById<TextView>(R.id.itemMessageDateMe)
            val photo = itemView.findViewById<ImageView>(R.id.itemMessageIVphoto)

            message.user?.let {
                if (it) {
                    llMe.setVisibility(true)
                    llOther.setVisibility(false)
                    txtBodyMe.text = message.text
                    txtDateMe?.text = message.date
                    photo.setVisibility(false)
                    txtUser.setVisibility(false)
                } else {
                    txtUser?.text = message.userName
                    txtBody?.text = message.text
                    txtDate?.text = message.date

                    if (!message.userPhoto.equals("url")) {
                        Glide.with(itemView)
                                .load(message.userPhoto)
                                .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                                .into(photo)
                    }
                }
            }
        }
    }
}