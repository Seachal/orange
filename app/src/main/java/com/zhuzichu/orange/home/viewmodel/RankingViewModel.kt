package com.zhuzichu.orange.home.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.zhuzichu.mvvm.base.BaseViewModel
import com.zhuzichu.mvvm.bus.event.SingleLiveEvent
import com.zhuzichu.mvvm.databinding.command.BindingAction
import com.zhuzichu.mvvm.databinding.command.BindingCommand
import com.zhuzichu.mvvm.http.exception.ExceptionHandle
import com.zhuzichu.mvvm.http.exception.ResponseThrowable
import com.zhuzichu.mvvm.utils.bindToLifecycle
import com.zhuzichu.mvvm.utils.exceptionTransformer
import com.zhuzichu.mvvm.utils.itemBindingOf
import com.zhuzichu.mvvm.utils.schedulersTransformer
import com.zhuzichu.mvvm.view.layout.MultiStateView
import com.zhuzichu.orange.BR
import com.zhuzichu.orange.R
import com.zhuzichu.orange.repository.NetRepositoryImpl

class RankingViewModel(application: Application) : BaseViewModel(application) {
    private var current = 0
    private var type: Int = 1
    private var back = 10
    private var min_id = 1

    val uc = UIChangeObservable()

    inner class UIChangeObservable {
        val finishLoadmore = SingleLiveEvent<Any>()
        val finishLoadMoreWithNoMoreData = SingleLiveEvent<Any>()
        val finishRefreshing = SingleLiveEvent<Any>()
    }
    val onRefreshCommand = BindingCommand<Any>(BindingAction {
        min_id = 1
        updateData(this.type)
    })
    val onLoadMoreCommand = BindingCommand<Any>(BindingAction {
        updateData(this.type)
    })
    val viewState = ObservableInt(MultiStateView.VIEW_STATE_LOADING)

    val itemBind = itemBindingOf<ItemRankingViewModel>(BR.item, R.layout.item_ranking)
    val list = MutableLiveData<List<ItemRankingViewModel>>().apply { value = ArrayList() }
    val itemBindIndicator = itemBindingOf<ItemRankingIndicatorViewModel>(BR.item, R.layout.item_ranking_indicator)
    val listIndicator = MutableLiveData<List<ItemRankingIndicatorViewModel>>().apply {
        value = listOf(
            ItemRankingIndicatorViewModel(
                this@RankingViewModel,
                "实时爆单榜",
                "近两小时销量排行榜",
                type,
                listOf(R.mipmap.ic_ranking_tab1_white, R.mipmap.ic_ranking_tab1_blue)
            ).apply {
                isSelected.set(true)
            },
            ItemRankingIndicatorViewModel(
                this@RankingViewModel,
                "今日爆单榜",
                "今日商品销量排行榜",
                2,
                listOf(R.mipmap.ic_ranking_tab2_white, R.mipmap.ic_ranking_tab2_blue)
            ),
            ItemRankingIndicatorViewModel(
                this@RankingViewModel,
                "出单指数榜",
                "大牛真实成交指数榜",
                4,
                listOf(R.mipmap.ic_ranking_tab3_white, R.mipmap.ic_ranking_tab3_blue)
            )
        )
    }

    fun selectIndcator(item: ItemRankingIndicatorViewModel) {
        val value = listIndicator.value!!
        val index = value.indexOf(item)
        min_id = 1
        updateData(item.type)
        value[current].isSelected.set(false)
        value[index].isSelected.set(true)
        current = index
    }

    @SuppressLint("CheckResult")
    fun updateData(type: Int) {
        this.type = type
        NetRepositoryImpl.getSalersList(back, this.type, min_id)
            .compose(bindToLifecycle(getLifecycleProvider()))
            .compose(schedulersTransformer())
            .compose(exceptionTransformer())
            .map {
                if (min_id == 1) {
                    list.value = ArrayList()
                    uc.finishRefreshing.call()
                }
                min_id = it.min_id
                val data = it.data
                val mutableList = mutableListOf<ItemRankingViewModel>()
                data.forEachIndexed { index, item ->
                    mutableList.add(ItemRankingViewModel(this, item, index).apply {
                        this.salesBean.itempic = this.salesBean.itempic.plus("_310x310.jpg")
                    })
                }
                mutableList
            }
            .subscribe({
                if (it.size < back) {
                    uc.finishLoadMoreWithNoMoreData.call()
                } else {
                    uc.finishLoadmore.call()
                }
                list.value = list.value!! + it
                viewState.set(MultiStateView.VIEW_STATE_CONTENT)
            }, {
                if (it is ResponseThrowable) {
                    if (it.code == ExceptionHandle.ERROR.NO_DATA && list.value?.size == 0) {
                        viewState.set(MultiStateView.VIEW_STATE_EMPTY)
                    } else {
                        viewState.set(MultiStateView.VIEW_STATE_ERROR)
                    }
                }
            })
    }
}