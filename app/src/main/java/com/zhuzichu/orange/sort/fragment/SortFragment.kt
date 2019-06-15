package com.zhuzichu.orange.sort.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.zhuzichu.mvvm.base.BaseTopBarFragment
import com.zhuzichu.orange.BR
import com.zhuzichu.orange.R
import com.zhuzichu.orange.databinding.FragmentSortBinding
import com.zhuzichu.orange.sort.viewmodel.SortViewModel
import kotlinx.android.synthetic.main.fragment_sort.*

/**
 * Created by Android Studio.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-06-12
 * Time: 16:40
 */
class SortFragment : BaseTopBarFragment<FragmentSortBinding, SortViewModel>() {
    override fun setLayoutId(): Int = R.layout.fragment_sort
    override fun bindVariableId(): Int = BR.viewModel

    override fun setTitle(): String = "超级分类"


    override fun initView() {
        mViewModel.showLoading()
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        mViewModel.loadShopSort()
    }

    override fun initViewObservable() {
        super.initViewObservable()
        mViewModel.uc.rightRecyclerToTop.observe(this, Observer {
            (right_recycler.layoutManager as GridLayoutManager).scrollToPositionWithOffset(0, 0)
        })
    }
}