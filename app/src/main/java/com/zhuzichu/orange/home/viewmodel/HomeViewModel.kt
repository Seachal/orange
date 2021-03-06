package com.zhuzichu.orange.home.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.widget.Toast
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.zhuzichu.mvvm.base.BaseViewModel
import com.zhuzichu.mvvm.bean.BaseRes
import com.zhuzichu.mvvm.bean.GoodsBean
import com.zhuzichu.mvvm.bus.event.SingleLiveEvent
import com.zhuzichu.mvvm.databinding.command.BindingCommand
import com.zhuzichu.mvvm.global.color.ColorGlobal
import com.zhuzichu.mvvm.repository.NetRepositoryImpl
import com.zhuzichu.mvvm.utils.*
import com.zhuzichu.mvvm.view.layout.MultiStateView
import com.zhuzichu.orange.BR
import com.zhuzichu.orange.R
import com.zhuzichu.orange.search.fragment.SearchFragment
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers
import me.tatarka.bindingcollectionadapter2.collections.AsyncDiffObservableList
import me.tatarka.bindingcollectionadapter2.collections.MergeObservableList
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import me.yokeyword.fragmentation.ISupportFragment


/**
 * Created by Android Studio.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-06-13
 * Time: 13:27
 */
@SuppressLint("CheckResult")
class HomeViewModel(application: Application) : BaseViewModel(application) {
    val color = ColorGlobal
    val uc = UIChangeObservable()
    val viewState = MutableLiveData(MultiStateView.VIEW_STATE_LOADING)
    private val imageList = listOf(R.drawable.background02, R.drawable.background01, R.drawable.background03)

    inner class UIChangeObservable {
        val finishRefreshing = SingleLiveEvent<Any>()
        val finishLoadmore = SingleLiveEvent<Any>()
        val finishLoadMoreWithNoMoreData = SingleLiveEvent<Any>()
    }

    val listBanner = MutableLiveData<List<Any>>()

    val itemBindBanner = itemBindingOf<Any>(BR.item, R.layout.item_home_banner)


    val itemBindNavigation =
        itemBindingOf<ItemNavigationViewModel>(BR.item, R.layout.item_home_navigation)
    val listNavigation = ObservableArrayList<ItemNavigationViewModel>().apply {
        addAll(
            listOf(
                ItemNavigationViewModel(this@HomeViewModel, "榜单", R.mipmap.baitu),
                ItemNavigationViewModel(this@HomeViewModel, "品牌", R.mipmap.bianfu),
                ItemNavigationViewModel(this@HomeViewModel, "抢购", R.mipmap.bianselong),
                ItemNavigationViewModel(this@HomeViewModel, "抖商", R.mipmap.daishu),
                ItemNavigationViewModel(this@HomeViewModel, "达人说", R.mipmap.daxiang),
                ItemNavigationViewModel(this@HomeViewModel, "xxx", R.mipmap.gongji),
                ItemNavigationViewModel(this@HomeViewModel, "xxx", R.mipmap.hainiu),
                ItemNavigationViewModel(this@HomeViewModel, "xxx", R.mipmap.huanxiong),
                ItemNavigationViewModel(this@HomeViewModel, "xxx", R.mipmap.jiakechong),
                ItemNavigationViewModel(this@HomeViewModel, "xxx", R.mipmap.jingyu)
            )
        )
    }

    val itemBind = OnItemBindClass<Any>().apply {
        map<ItemHomeClassViewModel>(BR.item, R.layout.item_home_class)
        map<ItemHomeDeserveViewModel>(BR.item, R.layout.item_home_deserve)
        map<ItemHomeHotViewModel>(BR.item, R.layout.item_home_hot)
        map<ItemHomeJuTaoVIewModel>(BR.item, R.layout.item_home_jutao)
    }.toItemBinding()

    private val deserveList = AsyncDiffObservableList(itemDiffOf<ItemHomeDeserveViewModel> { oldItem, newItem ->
        oldItem.goodsBean.itemid == newItem.goodsBean.itemid
    })

    private val hotShopList = AsyncDiffObservableList(itemDiffOf<ItemHomeHotViewModel> { oldItem, newItem ->
        oldItem.goodsBean.itemid == newItem.goodsBean.itemid
    })

    private val juTaoShopList = AsyncDiffObservableList(itemDiffOf<ItemHomeJuTaoVIewModel> { oldItem, newItem ->
        oldItem.goodsBean.itemid == newItem.goodsBean.itemid
    })

    val list: ObservableArrayList<Any> = ObservableArrayList()
//        .insertItem(ItemHomeClassViewModel(this@HomeViewModel, "今日值得买", deserveList, R.drawable.background02))
//        .insertList(deserveList)
//        .insertItem(ItemHomeClassViewModel(this@HomeViewModel, "爆单系列", hotShopList, R.drawable.background01))
//        .insertList(hotShopList)
//        .insertItem(ItemHomeClassViewModel(this@HomeViewModel, "聚淘专区", juTaoShopList, R.drawable.background03))
//        .insertList(juTaoShopList)

    val spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            val item = list[position]
            if (item is ItemHomeClassViewModel)
                return 2
            if (item is ItemHomeJuTaoVIewModel)
                return 2
            return 1
        }
    }

    val onErrorCommand = BindingCommand<Any>({
        viewState.value = MultiStateView.VIEW_STATE_LOADING
        loadHomeData()
    })

    val onClickSearch = BindingCommand<Any>({
        startFragment(SearchFragment(), launchMode = ISupportFragment.SINGLETASK)
    })

    fun loadHomeData() {
        NetRepositoryImpl.getHomeData()
            .bindToException()
            .bindToLifecycle(getLifecycleProvider())
            .bindToSchedulers()
            .doFinally {
                uc.finishRefreshing.call()
            }
            .subscribe({
                listBanner.value = it.data[0].list.map { item ->
                    ItemHomeBannerViewModel(this, item)
                }

                val data = mutableListOf<Any>()
                it.data.subList(1, it.data.size).mapIndexed { index, item ->
                    data.add(ItemHomeClassViewModel(this, item.name, imageList[index % 3]))
                    when (item.showType) {
                        1 -> {
                            data.addAll(item.list.map { child ->
                                ItemHomeDeserveViewModel(this, child)
                            })
                        }
                        2 -> {
                            data.addAll(item.list.map { child ->
                                ItemHomeHotViewModel(this, child)
                            })
                        }
                        3 -> {
                            data.addAll(item.list.map { child ->
                                ItemHomeJuTaoVIewModel(this, child)
                            })
                        }
                        else -> {
                            data.addAll(item.list.map { child ->
                                ItemHomeDeserveViewModel(this, child)
                            })
                        }
                    }

                }
                list.clear()
                list.addAll(data)
                viewState.value = MultiStateView.VIEW_STATE_CONTENT
            }, {
                viewState.value = MultiStateView.VIEW_STATE_ERROR
                handleThrowable(it)
            })

//        Flowable.zip(
//            NetRepositoryImpl.getHomeBannerList().subscribeOn(Schedulers.io()).compose(exceptionTransformer()).compose(
//                bindToLifecycle(getLifecycleProvider())
//            ),
//            NetRepositoryImpl.getDeserveList().subscribeOn(Schedulers.io()).compose(exceptionTransformer()).compose(
//                bindToLifecycle(getLifecycleProvider())
//            ),
//            NetRepositoryImpl.getHomeHotShopList().subscribeOn(Schedulers.io()).compose(exceptionTransformer()).compose(
//                bindToLifecycle(getLifecycleProvider())
//            ).take(12),
//            NetRepositoryImpl.getHomeJuTaoShopList().subscribeOn(Schedulers.io()).compose(exceptionTransformer()).compose(
//                bindToLifecycle(getLifecycleProvider())
//            ).take(12),
//            Function4<BaseRes<List<GoodsBean>>, BaseRes<List<GoodsBean>>, BaseRes<List<GoodsBean>>, BaseRes<List<GoodsBean>>, HomeData> { t1, t2, t3, t4 ->
//                HomeData(t1, t2, t3, t4)
//            }
//        ).observeOn(AndroidSchedulers.mainThread())
//            .compose(bindToLifecycle(getLifecycleProvider()))
//            .doFinally {
//                uc.finishRefreshing.call()
//            }
//            .subscribe({
//                listBanner.value = it.salesList.data.map { item ->
//                    ItemHomeBannerViewModel(this@HomeViewModel, item)
//                }
//
//                deserveList.update(it.goodsList.item_info.map { item ->
//                    ItemHomeDeserveViewModel(this@HomeViewModel, item)
//                })
//
//                hotShopList.update(it.hotList.data.map { item ->
//                    ItemHomeHotViewModel(this@HomeViewModel, item)
//                })
//
//                juTaoShopList.update(it.jutaoList.data.map { item ->
//                    ItemHomeJuTaoVIewModel(this@HomeViewModel, item)
//                })
//
//                viewState.value = MultiStateView.VIEW_STATE_CONTENT
//            }, {
//                viewState.value = MultiStateView.VIEW_STATE_ERROR
//                handleThrowable(it)
//            })
    }

    data class HomeData(
        var salesList: BaseRes<List<GoodsBean>>,
        var goodsList: BaseRes<List<GoodsBean>>,
        var hotList: BaseRes<List<GoodsBean>>,
        var jutaoList: BaseRes<List<GoodsBean>>
    )
}