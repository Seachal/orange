package com.zhuzichu.orange.home.viewmodel

import com.zhuzichu.mvvm.base.ItemViewModel
import com.zhuzichu.mvvm.databinding.command.BindingCommand
import com.zhuzichu.orange.bean.SalesBean
import com.zhuzichu.orange.checkLogin
import com.zhuzichu.orange.utils.showTradeDetail

/**
 * Created by Android Studio.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-06-20
 * Time: 18:01
 */
class ItemRankingViewModel(
    viewModel: RankingViewModel,
    var salesBean: SalesBean,
    var top: String
) : ItemViewModel<RankingViewModel>(viewModel) {
    val clickItem = BindingCommand<Any>({
        checkLogin(viewModel._activity) {
            showTradeDetail(viewModel._activity, salesBean.itemid)
        }
    })
}