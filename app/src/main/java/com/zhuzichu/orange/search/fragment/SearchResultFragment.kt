package com.zhuzichu.orange.search.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.alimama.tunion.trade.TUnionTradeSDK
import com.zhuzichu.mvvm.base.BaseTopBarFragment
import com.zhuzichu.mvvm.utils.bindArgument
import com.zhuzichu.mvvm.utils.showTradeDetail
import com.zhuzichu.mvvm.utils.showTradeUrl
import com.zhuzichu.orange.BR
import com.zhuzichu.orange.R
import com.zhuzichu.orange.checkLogin
import com.zhuzichu.orange.databinding.FragmentSearchResultBinding
import com.zhuzichu.orange.search.viewmodel.SearchResultViewModel
import kotlinx.android.synthetic.main.fragment_search_result.*


/**
 * Created by Android Studio.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-06-13
 * Time: 14:38
 */
class SearchResultFragment : BaseTopBarFragment<FragmentSearchResultBinding, SearchResultViewModel>() {
    companion object {
        const val KEYWORD = "keyword"
    }

    private val keyWord: String by bindArgument(KEYWORD)

    override fun setLayoutId(): Int = R.layout.fragment_search_result

    override fun bindVariableId(): Int = BR.viewModel

    override fun initView() {
        setTitle(keyWord)
        addRightIcon(R.mipmap.ic_top, View.OnClickListener {
            (recycler.layoutManager as GridLayoutManager).scrollToPositionWithOffset(0, 0)
        })
    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        _viewModel.searchShop(keyWord)
    }

    override fun initViewObservable() {
        _viewModel.uc.finishLoadmore.observe(this, Observer {
            refresh.finishLoadMore()
        })

        _viewModel.uc.finishRefreshing.observe(this, Observer {
            refresh.finishRefresh()
            refresh.setNoMoreData(false)
        })

        _viewModel.uc.finishLoadMoreWithNoMoreData.observe(this, Observer {
            refresh.finishLoadMore()
            refresh.setNoMoreData(true)
        })

        _viewModel.uc.onSpanSizeChangeEvent.observe(this, Observer {
            val layoutManager = recycler.layoutManager as GridLayoutManager
            if (layoutManager.childCount <= 0) {
                return@Observer
            }
            val findFirstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            _viewModel.changeSpanSize()
            recycler.postDelayed({
                (recycler.layoutManager as GridLayoutManager).scrollToPositionWithOffset(
                    findFirstVisibleItemPosition,
                    0
                )
            }, 50)
        })

        _viewModel.uc.clickItemResultEvent.observe(this, Observer {
            checkLogin {
                //                showTradeUrl(activity, it.couponurl)
                showTradeDetail(activity, it.itemid)
            }
        })
    }
}