package pl.wojtach.nazakupy

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val adapter = ShoppingListHeaderAdapter()
    private val viewModel: MainActivityViewModel by lazy {
        ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        recycler_view.adapter = adapter

        viewModel.viewStates.observe(this, Observer { viewState ->
            viewState?.run {
                when (this) {
                    is MainActivityViewState.Initial -> {
                        floatingActionButton.setOnClickListener { onElementAdded(ShoppingListHeader(id = null, formattedDate = "dzis", name = "Nowa")) }
                    }
                    is MainActivityViewState.AddedNewElement -> {
                        floatingActionButton.setOnClickListener { onElementAdded(ShoppingListHeader(id = null, formattedDate = "dzis", name = "Nowa")) }
                        adapter.items = calculateListState()
                        adapter.onRemoveListener = onElementRemoved
                    }

                    is MainActivityViewState.RemovedElement -> {
                        floatingActionButton.setOnClickListener { onElementAdded(ShoppingListHeader(id = null, formattedDate = "dzis", name = "Nowa")) }
                        adapter.items = calculateListState()
                        adapter.onRemoveListener = onElementRemoved
                    }

                    is MainActivityViewState.RemovedAllElements -> {
                        floatingActionButton.setOnClickListener { onElementAdded(ShoppingListHeader(id = null, formattedDate = "dzis", name = "Nowa")) }
                        adapter.items = calculateListState()
                        adapter.onRemoveListener = {}
                    }
                    is MainActivityViewState.Loading -> {
                    }
                }
            }
        })


    }
}
