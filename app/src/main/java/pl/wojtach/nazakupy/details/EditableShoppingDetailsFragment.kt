package pl.wojtach.nazakupy.details


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import pl.wojtach.nazakupy.R

private const val HEADER_ID_KEY = "header id key"

class EditableShoppingDetailsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_shopping_details, container, false)


    companion object {
        fun newInstance(headerId: Long) =
                EditableShoppingDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putLong(HEADER_ID_KEY, headerId)
                    }
                }
    }
}
