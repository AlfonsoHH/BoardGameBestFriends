package com.example.alfonsohernandez.boardgamebestfriends.presentation.games

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import com.example.alfonsohernandez.boardgamebestfriends.presentation.adapters.AdapterGames
import com.example.alfonsohernandez.boardgamebestfriends.presentation.dialogs.DialogFactory
import com.example.alfonsohernandez.boardgamebestfriends.presentation.gamedetail.GameDetailActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.utils.NotificationFilter
import com.google.firebase.messaging.RemoteMessage
import kotlinx.android.synthetic.main.activity_games.*
import javax.inject.Inject

class GamesActivity : AppCompatActivity(),
        GamesContract.View,
        SearchView.OnQueryTextListener,
        SwipeRefreshLayout.OnRefreshListener,
        DialogFactory.DialogInputCallback{

    @Inject
    lateinit var presenter: GamesPresenter

    var adapter = AdapterGames()
    var kind: String = ""
    var kindAdding: String = ""

    lateinit var progress: ProgressDialog

    private val TAG = "GamesActivity"

    lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games)

        val extras = intent.extras
        extras?.let {
            searchGames.setQuery(it.getString("search", ""), true)
            kind = it.getString("kind", "")
            kindAdding = it.getString("kindAdding", "")
        }

        injectDependencies()
        setupRecycler()
        presenter.setView(this, kind)

        setSupportActionBar(gamesToolbar)

        presenter.getUserProfile()?.let { user ->
            if (kind.contains("buddy-") && !kind.contains(user.id)) {
                fab.setVisibility(false)
                supportActionBar?.setTitle(getString(R.string.gamesToolbarTitleMember))
            } else if (kind.contains("buddy-") && kind.contains(user.id)) {
                supportActionBar?.setTitle(getString(R.string.gamesToolbarTitleUser))
            } else if (kind.contains("group-")) {
                supportActionBar?.setTitle(getString(R.string.gamesToolbarTitleGroup))
            } else if (kind.contains("place-")) {
                supportActionBar?.setTitle(getString(R.string.gamesToolbarTitlePlace))
            } else {
                supportActionBar?.setTitle(getString(R.string.gamesToolbarTitleAll))
            }
        }
        supportActionBar?.setIcon(R.drawable.toolbarbgbf)

        searchGames.setOnQueryTextListener(this)
        swipeContainerGames.setOnRefreshListener(this)

        if (!kind.equals("")) {
            fab.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val intent = Intent(applicationContext, GamesActivity::class.java)
                    intent.putExtra("kindAdding", kind)
                    startActivityForResult(intent, 1)
                }
            })
        } else {
            fab.setVisibility(false)
        }
    }

    override fun showNotification(rm: RemoteMessage) {
        var nf = NotificationFilter(this,rm)
        nf.chat()
        nf.groupUser()
        nf.groupRemoved()
        nf.meetingModified()
        nf.meetingRemoved()
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun setupRecycler() {
        rvGamesActivity.layoutManager = GridLayoutManager(this, 3)
        rvGamesActivity.adapter = adapter

        adapter.onGameClickedListener = { game ->
            itemDetail(game.id)
        }
        if (kind.equals("")) {
            adapter.onLongClickListener = { game ->
                presenter.addRemoveItem(true, game.id, kindAdding)
            }
        } else {
            adapter.onLongClickListener = { game ->
                DialogFactory.buildConfirmDialog(this,getString(R.string.gamesDelete), Runnable {
                    presenter.addRemoveItem(false, game.id, kind)
                    adapter.gameList.remove(game)
                    adapter.notifyDataSetChanged()
                }).show()
            }
        }
    }

    override fun removeGame(game: Game) {
        adapter.gameList.remove(game)
        adapter.notifyDataSetChanged()
    }

    override fun itemDetail(id: String) {
        val intent = Intent(this, GameDetailActivity::class.java)
        intent.putExtra("id", id)
        if (kindAdding.equals(""))
            intent.putExtra("kind", kind)
        else
            intent.putExtra("kind", kindAdding)
        startActivity(intent)
    }

    override fun onDestroy() {
        presenter.setView(null, "")
        super.onDestroy()
    }

    override fun setData(games: ArrayList<Game>) {
        adapter.gameList.clear()
        adapter.gameList.addAll(games)
        adapter.notifyDataSetChanged()
    }

    override fun showError(stringId: Int) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showSuccess(stringId: Int) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showProgress(isLoading: Boolean) {
        progressBar?.setVisibility(isLoading)
        rvGamesActivity?.setVisibility(!isLoading)
        if (!isLoading) {
            swipeContainerGames?.isRefreshing = false
        }
    }

    override fun showProgressDialog(isLoading: Boolean) {
        if (isLoading) {
            progress = ProgressDialog.show(this, getString(R.string.gamesBggPDtitle), getString(R.string.gamesBggPDtext), true)
            rvGamesActivity?.setVisibility(!isLoading)
        } else {
            rvGamesActivity?.setVisibility(isLoading)
            progress.dismiss()
        }
    }

    override fun onRefresh() {
        presenter.dataChooser()
        swipeContainerGames?.isRefreshing = false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        presenter.dataChooser()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        presenter.dataChooser()
        return true
    }

    override fun getSearchData(): String {
        return searchGames.query.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        this.menu = menu
        presenter.getUserProfile()?.let { user ->
            if (kind.contains("buddy") && kind.contains(user.id))
                menu.getItem(3).setVisible(true)
        }
        return true
    }

    override fun getDialogInput(input: String) {
        presenter.getBGGdata(input)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_bgg -> {
                DialogFactory.callbackInput = this
                DialogFactory.buildInputDialog(this@GamesActivity,getString(R.string.gamesToolbarSyncDialog)).show()
            }
        }
        return true
    }

    override fun onBackPressed() {
        val returnIntent = Intent()
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            presenter.dataChooser()
        }
    }

}
