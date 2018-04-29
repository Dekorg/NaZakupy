package pl.wojtach.nazakupy

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val adapter = ShoppingListHeadersAdapter()
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

        floating_action_button.setOnClickListener { viewModel.dispatchIntent(MainActivityIntent.AddNewItem) }
        adapter.onRemoveListener = { removedItem -> viewModel.dispatchIntent(MainActivityIntent.RemoveItem(removedItem)) }

        viewModel.viewEvents.observe(this, Observer { viewState ->
            viewState?.apply {
                adapter.items = calculateListState().toList()
            }
        })
    }
}
