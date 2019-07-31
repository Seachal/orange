package com.zhuzichu.orange.mine.viewmodel

import android.app.Application
import androidx.appcompat.widget.AppCompatCheckBox
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.ColorPalette
import com.afollestad.materialdialogs.color.colorChooser
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.ali.auth.third.core.model.Session
import com.alibaba.baichuan.trade.biz.login.AlibcLogin
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback
import com.zhuzichu.mvvm.base.BaseViewModel
import com.zhuzichu.mvvm.bus.event.SingleLiveEvent
import com.zhuzichu.mvvm.databinding.command.BindingCommand
import com.zhuzichu.mvvm.global.AppGlobal
import com.zhuzichu.mvvm.global.AppPreference
import com.zhuzichu.mvvm.global.color.ColorGlobal
import com.zhuzichu.mvvm.utils.toast
import com.zhuzichu.orange.R
import com.zhuzichu.orange.login.fragment.LoginFragment
import com.zhuzichu.orange.mine.fragment.*
import com.zhuzichu.orange.utils.showTradeOrder
import com.zhuzichu.orange.utils.showTradeShopCart
import com.zhuzichu.orange.view.plane.PlaneMaker

/**
 * Created by Android Studio.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-06-12
 * Time: 16:43
 */
class MineViewModel(application: Application) : BaseViewModel(application) {
    val global = AppGlobal
    val color = ColorGlobal
    lateinit var checkbox: AppCompatCheckBox
    val preference by lazy { AppPreference() }
    val uc = UIChangeObservable()

    inner class UIChangeObservable {
        val onDarkChangeEvent = SingleLiveEvent<Boolean>()
    }

    val onClickAuthorize = BindingCommand<Any>({
        PlaneMaker.showLoadingDialog(_activity, false)
        AlibcLogin.getInstance().showLogin(object : AlibcLoginCallback {
            override fun onSuccess(i: Int) {
                AppGlobal.isAuth.set(AlibcLogin.getInstance().isLogin)
                AppGlobal.session.set(AlibcLogin.getInstance().session)
                PlaneMaker.dismissLodingDialog()
            }

            override fun onFailure(code: Int, msg: String) {
                msg.toast()
                PlaneMaker.dismissLodingDialog()
            }
        })
    })

    val onClickExitAuthorize = BindingCommand<Any>({
        AlibcLogin.getInstance().logout(object : AlibcLoginCallback {
            override fun onSuccess(i: Int) {
                AppGlobal.isAuth.set(false)
                AppGlobal.session.set(Session())
            }

            override fun onFailure(code: Int, msg: String) {
                msg.toast()
            }
        })
    })


    val onClickLogin = BindingCommand<Any>({
        startFragment(LoginFragment())
    })

    val getDarkCheckBox = BindingCommand<AppCompatCheckBox>(consumer = {
        checkbox = it
    })

    val onClickOrderAll = BindingCommand<Any>({
        showTradeOrder(_activity, 0)
    })

    val onClickOrderOne = BindingCommand<Any>({
        showTradeOrder(_activity, 1)
    })

    val onClickOrderTwo = BindingCommand<Any>({
        showTradeOrder(_activity, 2)
    })

    val onClickOrderThree = BindingCommand<Any>({
        showTradeOrder(_activity, 3)
    })

    val onClickOrderFour = BindingCommand<Any>({
        showTradeOrder(_activity, 4)
    })

    val onClickSetting = BindingCommand<Any>({
        startFragment(SettingFragment())
    })

    val onClickActivies = BindingCommand<Any>({
        "暂未开发".toast()
    })

    val onClickOrder = BindingCommand<Any>({
        showTradeOrder(_activity)
    })

    val onClickShopcar = BindingCommand<Any>({
        showTradeShopCart(_activity)
    })

    val onClickCollection = BindingCommand<Any>({
        "暂未开发".toast()
    })

    val onClickCache = BindingCommand<Any>({
        startFragment(CacheFragment())
    })

    val onClickAbout = BindingCommand<Any>({
        startFragment(AboutFragment())
    })

    val onClickTheme = BindingCommand<Any>({
        R.attr.md_color_button_text
        MaterialDialog(_activity).show {
            cornerRadius(10f)
            title(text = "选择主题颜色")
            colorChooser(
                colors = ColorPalette.Accent,
                subColors = ColorPalette.AccentSub,
                allowCustomArgb = true,
                showAlphaSelector = true
            ) { _, color ->
                preference.colorPrimary = color.apply {
                    this@MineViewModel.color.colorPrimary.value = this
                }
            }
            positiveButton(R.string.select)
            negativeButton(android.R.string.cancel)
            lifecycleOwner(_fragment)
        }
    })

    val onClickDark = BindingCommand<Any>({
        uc.onDarkChangeEvent.value = !color.isDark.value!!
    })

    val onClickSmallProgram = BindingCommand<Any>({
        startFragment(SmallProgramFragment())
    })

    val onClickSettingUser = BindingCommand<Any>({
        startFragment(SettingUserFragment())
    })
}