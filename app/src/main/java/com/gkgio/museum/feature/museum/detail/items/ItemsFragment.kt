package com.gkgio.museum.feature.museum.detail.items

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.gkgio.museum.R
import com.gkgio.museum.base.BaseFragment
import com.gkgio.museum.di.AppInjector
import com.gkgio.museum.ext.createViewModel
import com.gkgio.museum.ext.observeValue
import com.gkgio.museum.feature.audios.AudioPlayerSheet
import com.gkgio.museum.feature.auth.AuthViewModel
import com.gkgio.museum.feature.model.Exhibition
import com.gkgio.museum.feature.model.Item
import com.gkgio.museum.feature.museum.MuseumsAdapter
import com.gkgio.museum.feature.museum.detail.MuseumDetailFragment
import com.gkgio.museum.utils.FragmentArgumentDelegate
import kotlinx.android.synthetic.main.fragment_items.*
import kotlinx.android.synthetic.main.fragment_museums.*

class ItemsFragment : BaseFragment<ItemsViewModel>() {

    companion object {
        val TAG = ItemsFragment::class.java.simpleName

        fun newInstance(title: String, itemsList: List<Item>) =
            ItemsFragment().apply {
                this.itemsList = itemsList
                this.title = title
            }
    }

    var itemsList: List<Item> by FragmentArgumentDelegate()
    var title: String by FragmentArgumentDelegate()

    var itemsAdapter: ItemsAdapter? = null


    override fun getLayoutId(): Int = R.layout.fragment_items

    override fun provideViewModel() = createViewModel {
        AppInjector.appComponent.itemsViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setTitle(title)
        toolbar.setLeftIconClickListener {
            viewModel.onBackClick()
        }

        viewModel.init(itemsList as MutableList<Item>)

        viewModel.openItemSheet.observeValue(this) {
            showDialog(AudioPlayerSheet.newInstance(it), TAG)
        }

        initItemsRv()

        viewModel.state.observeValue(this) { state ->
            state.itemsList?.let {
                itemsAdapter?.setItemsList(it)
            }
        }
    }

    private fun initItemsRv() {
        itemsAdapter = ItemsAdapter {
            viewModel.onItemClick(it)
        }
        itemsRv.adapter = itemsAdapter
        itemsRv.layoutManager =
            LinearLayoutManager(context)
    }
}