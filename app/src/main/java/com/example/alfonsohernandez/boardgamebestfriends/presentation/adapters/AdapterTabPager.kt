package com.example.alfonsohernandez.boardgamebestfriends.presentation.adapters

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.groups.GroupsFragment
import com.example.alfonsohernandez.boardgamebestfriends.presentation.meetings.MeetingsFragment
import com.example.alfonsohernandez.boardgamebestfriends.presentation.places.PlacesFragment
import com.example.alfonsohernandez.boardgamebestfriends.presentation.profile.ProfileFragment
import com.example.alfonsohernandez.boardgamebestfriends.presentation.tab.TabPresenter

class AdapterTabPager(fm: FragmentManager, private var tabCount: Int, presenter: TabPresenter, kind: String) :
        FragmentPagerAdapter(fm) {

    private val TAG = "AdapterTabPager"

    var bundle = Bundle()
    var kindNow = kind

    var  presenter: TabPresenter? = null

    init {
        this.presenter = presenter
    }

    override fun getItem(position: Int): Fragment? {

        bundle.putString("kind",kindNow)
        when (position) {
            0 -> {
                presenter?.firebaseEvent("Meetings pageview",TAG)
                val fragment = MeetingsFragment()
                fragment.arguments = bundle
                return fragment
            }
            1 -> {
                presenter?.firebaseEvent("Group pageview",TAG)
                val fragment = GroupsFragment()
                fragment.arguments = bundle
                return fragment
            }
            2 -> {
                presenter?.firebaseEvent("Places pageview",TAG)
                val fragment = PlacesFragment()
                fragment.arguments = bundle
                return fragment
            }
            3 -> {
                presenter?.firebaseEvent("Profile pageview",TAG)
                val fragment = ProfileFragment()
                fragment.arguments = bundle
                return fragment
            }
            else -> return null
        }
    }

    override fun getCount(): Int {
        return tabCount
    }
}