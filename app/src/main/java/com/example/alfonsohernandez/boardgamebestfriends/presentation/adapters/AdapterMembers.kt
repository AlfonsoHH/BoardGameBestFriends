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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User

class AdapterMembers : RecyclerView.Adapter<AdapterMembers.ViewHolder>() {

    private val TAG = "AdapterMembers"

    var memberList: ArrayList<User> = arrayListOf()

    var onMemberClickedListener: ((User) -> Unit)? = null
    var onLongClickListener: ((User) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(memberList!!.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_members, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return memberList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(member: User) {

            val photo = itemView.findViewById<ImageView>(R.id.itemMembersIVphoto)

            Log.d(TAG,member.photo)
            if(member.service.equals("email")){
                if(!member.photo.equals("url")) {
                    val decodedString = Base64.decode(member.photo, Base64.DEFAULT)
                    val imagenJug = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    Bitmap.createScaledBitmap(imagenJug, 180, 180, false)
                    val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(itemView.getResources(), imagenJug)
                    roundedBitmapDrawable.isCircular = true

                    photo?.setImageDrawable(roundedBitmapDrawable)
                }
            }else{
                Glide.with(itemView).asBitmap().load(member.photo).into(object : SimpleTarget<Bitmap>(120,120) {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val roundedDrawable = RoundedBitmapDrawableFactory.create(itemView.resources, resource)
                        roundedDrawable.isCircular = true
                        photo?.setImageDrawable(roundedDrawable)
                    }
                })
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