package com.zhuzichu.orange.sort.viewmodel

import androidx.databinding.ObservableBoolean
import com.zhuzichu.mvvm.base.ItemViewModel
import com.zhuzichu.mvvm.bean.CategoryBean
import com.zhuzichu.mvvm.databinding.command.BindingCommand
import com.zhuzichu.mvvm.global.color.ColorGlobal

class ItemLeftViewModel(
    viewModel: SortViewModel,
    val category: CategoryBean
) : ItemViewModel<SortViewModel>(viewModel) {
    val isSelected = ObservableBoolean()
    val color = ColorGlobal
    val clickItem = BindingCommand<Any>({
        val data = mutableListOf<ItemLeftViewModel>()
        viewModel.leftList.map {
            if (it is ItemLeftViewModel) {
                it.isSelected.set(it.category.id == category.id)
                data.add(it)
            }
        }
        viewModel.leftList.update(data.toList())
        viewModel.updateRight(this)
    })

}