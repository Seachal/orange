package com.zhuzichu.orange.find.fragment

import androidx.fragment.app.Fragment
import com.zhuzichu.mvvm.base.BaseTopbarFragment
import com.zhuzichu.mvvm.base.DefaultFragmentPagerAdapter
import com.zhuzichu.orange.BR
import com.zhuzichu.orange.R
import com.zhuzichu.orange.databinding.FragmentFindBinding
import com.zhuzichu.orange.find.viewmodel.FindViewModel
import kotlinx.android.synthetic.main.fragment_find.*

class FindFragment : BaseTopbarFragment<FragmentFindBinding, FindViewModel>() {

    private val titles = listOf("精选单品", "好货专场", "趣味发图")
    private val fragments = listOf<Fragment>(FindOneFragment(), FindTwoFragment(), FindThreeFragment())

    override fun setLayoutId(): Int = R.layout.fragment_find

    override fun bindVariableId(): Int = BR.viewModel

    override fun initView() {
        setTitle("发现")
        initViewPager()

    }

    private fun initViewPager() {
        viewpager.adapter = DefaultFragmentPagerAdapter(childFragmentManager, fragments,titles)
        viewpager.offscreenPageLimit = titles.size
        tabs.setupWithViewPager(viewpager)
    }

    override fun initViewObservable() {


    }


}