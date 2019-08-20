package com.zhuzichu.orange.search.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.zhuzichu.mvvm.base.ItemViewModel
import com.zhuzichu.mvvm.databinding.command.BindingCommand
import com.zhuzichu.mvvm.global.color.ColorGlobal

/**
 * Created by Android Studio.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-19
 * Time: 15:01
 */
class ItemSearchNavViewModel(
    viewModel: SearchResultPlusViewModel,
    title: String,
    sort: String?

) : ItemViewModel<SearchResultPlusViewModel>(viewModel) {
    val color = ColorGlobal
    val title = MutableLiveData(title)
    val selected = ObservableBoolean(false)
    val position = ObservableInt(0)

    //是否倒叙
    val isDes = ObservableBoolean(true)

    val onClickItem = BindingCommand<Any>({

        if (selected.get() && position.get() != 2) {
            return@BindingCommand
        }

        viewModel.navList.value = viewModel.navList.value?.map { item ->
            item.also {
                it.selected.set(it.position.get() == this.position.get())
            }
        }

        if (sort.isNullOrBlank()) {
            viewModel.sort = sort
        } else {
            if (isDes.get()) {
                viewModel.sort = sort.plus("_asc")
                isDes.set(false)
            } else {
                viewModel.sort = sort.plus("_des")
                isDes.set(true)
            }
        }

        viewModel.pageNo = 1
        viewModel.showLoadingDialog()
        viewModel.loadData()

    })
}