package com.example.alfonsohernandez.boardgamebestfriends.presentation.games

import android.app.Activity
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
import com.example.alfonsohernandez.boardgamebestfriends.presentation.addgroup.AddGroupActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.gamedetail.GameDetailActivity
import kotlinx.android.synthetic.main.activity_games.*
import javax.inject.Inject

class GamesActivity : AppCompatActivity(),
        GamesContract.View,
        SearchView.OnQueryTextListener,
        SwipeRefreshLayout.OnRefreshListener{

    @Inject
    lateinit var presenter: GamesPresenter

    var adapter = AdapterGames()
    var kind: String = ""
    var kindAdding: String = ""

    private val TAG = "GamesActivity"

    lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games)

        if(savedInstanceState!=null) {
            searchGames.setQuery(savedInstanceState.getString("search", ""), true)
            kind = savedInstanceState.getString("kind","")
            kindAdding = savedInstanceState.getString("kindAdding","")
        }else{
            val extras = intent.extras
            searchGames.setQuery(extras.getString("search", ""), true)
            kind = extras.getString("kind","")
            kindAdding = extras.getString("kindAdding","")
        }

        setSupportActionBar(gamesToolbar)
        if(kind.contains("buddy-"))
            supportActionBar!!.setTitle(getString(R.string.gamesToolbarTitleUser))
        else if (kind.contains("group-"))
            supportActionBar!!.setTitle(getString(R.string.gamesToolbarTitleGroup))
        else if(kind.contains("place-"))
            supportActionBar!!.setTitle(getString(R.string.gamesToolbarTitlePlace))
        else
            supportActionBar!!.setTitle(getString(R.string.gamesToolbarTitleAll))
        supportActionBar!!.setIcon(R.drawable.toolbarbgbf)

        injectDependencies()
        setupRecycler()
        presenter.setView(this,kind)

        searchGames.setOnQueryTextListener(this)
        swipeContainerGames.setOnRefreshListener(this)

        if(!kind.equals("")) {
            fab.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val intent = Intent(applicationContext, GamesActivity::class.java)
                    intent.putExtra("kindAdding", kind)
                    startActivityForResult(intent,1)
                }
            })
        }else{
            fab.setVisibility(false)
        }
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun setupRecycler() {
        rvGamesActivity.layoutManager = GridLayoutManager(this,3)
        rvGamesActivity.adapter = adapter

        adapter.onGameClickedListener = { game ->
            itemDetail(game.id)
        }
        if(kind.equals("")){
            adapter.onLongClickListener = { game ->
                presenter.addRemoveItem(true, game.id,kindAdding)
            }
        }else {
            adapter.onLongClickListener = { game ->
                val alertDilog = AlertDialog.Builder(this).create()
                alertDilog.setTitle(getString(R.string.gamesAlert))
                alertDilog.setMessage(getString(R.string.gamesDelete))

                alertDilog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.gamesYes), { dialogInterface, i ->
                    presenter.addRemoveItem(false, game.id, kind)
                    adapter.gameList.remove(game)
                    adapter.notifyDataSetChanged()
                })

                alertDilog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.gamesNo), { dialogInterface, i ->
                    alertDilog.dismiss()
                })
                alertDilog.show()
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
        if(kindAdding.equals(""))
            intent.putExtra("kind",kind)
        else
            intent.putExtra("kind",kindAdding)
        startActivity(intent)
    }

    fun syncBGG(){
        val builder = AlertDialog.Builder(this@GamesActivity)
        builder.setTitle(getString(R.string.gamesToolbarSyncDialog))

        val input = EditText(this@GamesActivity)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, which ->
            presenter.loadAllGamesDB(input.text.toString())
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
        }

        builder.show()
    }

    override fun onDestroy() {
        presenter.setView(null,"")
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        clearData()
    }

    override fun clearData() {
        adapter.gameList.clear()
    }

    override fun setSingleData(game: Game) {
        if(adapter.gameList.size<120) {
            adapter.gameList.add(game)
            var gameList = ArrayList(adapter.gameList.sortedBy { it.title })
            adapter.gameList.clear()
            adapter.gameList.addAll(gameList)
            adapter.notifyDataSetChanged()
        }
    }

    override fun showErrorLoading() {
        Toast.makeText(this, getString(R.string.gamesErrorLoading), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorBGG() {
        Toast.makeText(this, getString(R.string.gamesErrorBGG), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorRemoveDB() {
        Toast.makeText(this, getString(R.string.gamesErrorRemoveDatabase), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorAddingToUser() {
        Toast.makeText(this, getString(R.string.gamesErrorAddingToUser), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorAddingToGroup() {
        Toast.makeText(this, getString(R.string.gamesErrorAddingToGroup), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorAddingToPlace() {
        Toast.makeText(this, getString(R.string.gamesErrorAddingToPlace), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorRemovingToUser() {
        Toast.makeText(this, getString(R.string.gamesErrorRemoveToUser), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorRemovingToGroup() {
        Toast.makeText(this, getString(R.string.gamesErrorRemoveToGroup), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorRemovingToPlace() {
        Toast.makeText(this, getString(R.string.gamesErrorRemoveToPlace), Toast.LENGTH_SHORT).show()
    }

    override fun successAddingToUser() {
        Toast.makeText(this, getString(R.string.gamesSuccessAddingToUser), Toast.LENGTH_SHORT).show()
    }

    override fun successAddingToGroup() {
        Toast.makeText(this, getString(R.string.gamesSuccessAddingToGroup), Toast.LENGTH_SHORT).show()
    }

    override fun successAddingToPlace() {
        Toast.makeText(this, getString(R.string.gamesSuccessAddingToPlace), Toast.LENGTH_SHORT).show()
    }

    override fun successRemovingToUser() {
        Toast.makeText(this, getString(R.string.gamesSuccessRemoveToUser), Toast.LENGTH_SHORT).show()
    }

    override fun successRemovingToGroup() {
        Toast.makeText(this, getString(R.string.gamesSuccessRemoveToGroup), Toast.LENGTH_SHORT).show()
    }

    override fun successRemovingToPlace() {
        Toast.makeText(this, getString(R.string.gamesSuccessRemoveFromPlace), Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar(isLoading: Boolean) {
        progressBar.setVisibility(isLoading)
        rvGamesActivity.setVisibility(!isLoading)
        if (!isLoading) {
            swipeContainerGames.isRefreshing = false
        }
    }

    override fun onRefresh() {
        presenter.dataChooser()
        swipeContainerGames.isRefreshing = false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        presenter.dataChooser()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        presenter.dataChooser()
        return true
    }

    override fun noResult() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSearchData(): String {
        return searchGames.query.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)

        this.menu = menu
        if(kind.contains("buddy"))
            menu.getItem(3).setVisible(true)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_bgg -> {
                syncBGG()
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
        if(resultCode == Activity.RESULT_OK) {
            presenter.dataChooser()
        }
    }

}
