package com.example.alfonsohernandez.boardgamebestfriends.presentation.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group

/**
 * Created by alfonsohernandez on 15/03/2018.
 */

class AdapterGroups : RecyclerView.Adapter<AdapterGroups.ViewHolder>() {

    private val TAG = "AdapterGroups"

    var groupList: ArrayList<Group> = arrayListOf()

    var onGroupClickedListener: ((Group) -> Unit)? = null
    var onLongClickListener: ((Group) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(groupList!!.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_groups, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(group: Group) {

            val txtTitle = itemView.findViewById<TextView>(R.id.itemFriendsTVtitle)
            val txtDescription = itemView.findViewById<TextView>(R.id.itemFriendsTVdescription)
            val foto = itemView.findViewById<ImageView>(R.id.itemFriendsImageView)

            txtTitle?.text = group.title
            txtDescription?.text = group.subtitle

            if(!group.photo.equals("url")) {
                val decodedString = Base64.decode(group.photo, Base64.DEFAULT)
                val imagenJug = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                Bitmap.createScaledBitmap(imagenJug, 90, 90, false)
                val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(itemView.getResources(), imagenJug)
                roundedBitmapDrawable.isCircular = true

                foto?.setImageDrawable(roundedBitmapDrawable)
            }

            itemView.setOnClickListener({
                onGroupClickedListener?.invoke(group)
            })

            itemView.setOnLongClickListener({
                onLongClickListener?.invoke(group)
                true
            })
        }
    }
}