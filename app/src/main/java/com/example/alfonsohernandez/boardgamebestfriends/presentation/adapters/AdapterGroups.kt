package com.example.alfonsohernandez.boardgamebestfriends.presentation.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import jp.wasabeef.glide.transformations.CropCircleTransformation

/**
 * Created by alfonsohernandez on 15/03/2018.
 */

class AdapterGroups : RecyclerView.Adapter<AdapterGroups.ViewHolder>() {

    private val TAG = "AdapterGroups"

    var groupList: ArrayList<Group> = arrayListOf()

    var onGroupClickedListener: ((Group) -> Unit)? = null
    var onLongClickListener: ((Group) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(groupList.get(position))
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
            val txtNumMeetings = itemView.findViewById<TextView>(R.id.itemFriendsTVmeetings)
            val foto = itemView.findViewById<ImageView>(R.id.itemFriendsImageView)

            val wholeItem = itemView.findViewById<FrameLayout>(R.id.wholeItem)

            if (group.id.equals("blank")){
                wholeItem.setVisibility(false)
            }else {
                wholeItem.setVisibility(true)

                txtTitle?.text = group.title
                txtDescription?.text = group.subtitle
                txtNumMeetings?.text = group.meetings.toString()

                if (!group.photo.equals("url")) {
                    Glide.with(itemView.context)
                            .load(group.photo)
                            .apply(bitmapTransform(CropCircleTransformation()))
                            .into(foto)
                } else {
                    foto?.setImageDrawable(itemView.resources.getDrawable(R.drawable.group_icon))
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
}