package com.example.alfonsohernandez.boardgamebestfriends.presentation.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game

class AdapterGames : RecyclerView.Adapter<AdapterGames.ViewHolder>() {
    var gameList: ArrayList<Game> = arrayListOf()

    var onGameClickedListener: ((Game) -> Unit)? = null
    var onLongClickListener: ((Game) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(gameList?.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_games, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return gameList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(game: Game) {

            val txtTitle = itemView.findViewById<TextView>(R.id.itemGamesTVtitle)
            val foto = itemView.findViewById<ImageView>(R.id.itemGamesImageView)

            txtTitle?.text = game.title

            Glide.with(itemView)
                    .load(game.photo)
                    .into(foto)

            itemView.setOnClickListener({
                onGameClickedListener?.invoke(game)
            })

            itemView.setOnLongClickListener {
                onLongClickListener?.invoke(game)
                true
            }

        }
    }
}