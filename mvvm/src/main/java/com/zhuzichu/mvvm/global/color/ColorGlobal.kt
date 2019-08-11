package com.zhuzichu.mvvm.global.color

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.zhuzichu.mvvm.R
import com.zhuzichu.mvvm.global.AppPreference
import com.zhuzichu.mvvm.utils.logi
import com.zhuzichu.mvvm.utils.toColorById

object ColorGlobal {
    private val preference by lazy { AppPreference() }

    val isDark = MutableLiveData<Boolean>()
    val colorPrimary: MutableLiveData<Int> = MutableLiveData()
    val contentBackground: ObservableField<Int> = ObservableField()
    val windowBackground: ObservableField<Int> = ObservableField()
    val textColorPrimary: ObservableField<Int> = ObservableField()
    val textColorSecond: ObservableField<Int> = ObservableField()
    val hintColor: ObservableField<Int> = ObservableField()
    val bottomBackground: ObservableField<Int> = ObservableField()

    fun initColor() {
        preference.colorPrimary.toString().logi("zzc")
        preference.isDark.toString().logi("zzc")
        colorPrimary.value = preference.colorPrimary
        setDark(preference.isDark)
    }

    fun setDark(isDark: Boolean) {
        if (isDark) {
            contentBackground.set(R.color.black_333.toColorById())
            windowBackground.set(R.color.colorBackgroundDark.toColorById())
            textColorPrimary.set(R.color.colorPrimaryTextDark.toColorById())
            textColorSecond.set(R.color.colorHintDark.toColorById())
            hintColor.set(R.color.gray_light.toColorById())
            bottomBackground.set(R.color.black_333.toColorById())
        } else {
            contentBackground.set(R.color.colorContent.toColorById())
            windowBackground.set(R.color.colorBackground.toColorById())
            textColorPrimary.set(R.color.colorPrimaryText.toColorById())
            textColorSecond.set(R.color.colorSecondText.toColorById())
            hintColor.set(R.color.colorHint.toColorById())
            bottomBackground.set(R.color.white.toColorById())
        }
        this@ColorGlobal.isDark.value = isDark
    }
}