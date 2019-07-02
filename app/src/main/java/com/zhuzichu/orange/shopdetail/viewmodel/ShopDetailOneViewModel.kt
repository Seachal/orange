package com.zhuzichu.orange.shopdetail.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import com.zhuzichu.mvvm.base.BaseViewModel
import com.zhuzichu.mvvm.utils.bindToLifecycle
import com.zhuzichu.mvvm.utils.exceptionTransformer
import com.zhuzichu.mvvm.utils.schedulersTransformer
import com.zhuzichu.mvvm.utils.toast
import com.zhuzichu.orange.repository.NetRepositoryImpl

@SuppressLint("CheckResult")
class ShopDetailOneViewModel(application: Application) : BaseViewModel(application){
    fun loadShopDetail(itemid: String) {
        NetRepositoryImpl.getShopDetail(itemid)
            .compose(bindToLifecycle(getLifecycleProvider()))
            .compose(schedulersTransformer())
            .compose(exceptionTransformer())
            .subscribe({
                it.data.toast()
            }, {
                handleThrowable(it)
            })
    }
}