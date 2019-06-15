package com.zhuzichu.orange.main

import android.content.Context
import android.content.Intent
import com.zhuzichu.mvvm.base.BaseActivity
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator

class MainActivity : BaseActivity() {

    override fun setRootFragment(): ISupportFragment= MainFragment()

    companion object {
        fun start(context: Context) {
            val intent = Intent()
            intent.setClass(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        // 设置横向(和安卓4.x动画相同)
        return DefaultHorizontalAnimator()
    }

}