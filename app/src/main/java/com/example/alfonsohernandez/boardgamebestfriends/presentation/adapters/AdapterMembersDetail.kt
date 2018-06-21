package com.example.alfonsohernandez.boardgamebestfriends.presentation.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import jp.wasabeef.glide.transformations.CropCircleTransformation

class AdapterMembersDetail : RecyclerView.Adapter<AdapterMembersDetail.ViewHolder>() {

    private val TAG = "AdapterMembersDetail"

    var memberList: ArrayList<User> = arrayListOf()

    var onMemberClickedListener: ((User) -> Unit)? = null
    var onLongClickListener: ((User) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(memberList.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_members_detail, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return memberList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(member: User) {

            val photo = itemView.findViewById<ImageView>(R.id.itemMembersIVphoto)
            val name = itemView.findViewById<TextView>(R.id.itemMemberTVname)

            name.text = member.userName

            if(!member.photo.equals("url")) {
                Glide.with(itemView)
                        .load(member.photo)
                        .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                        .into(photo)
            }else{
                photo.setImageResource(R.drawable.profile)
            }

            itemView.setOnClickListener({
                onMemberClickedListener?.invoke(member)
            })

            itemView.setOnLongClickListener({
                onLongClickListener?.invoke(member)
                true
            })
        }
    }
}