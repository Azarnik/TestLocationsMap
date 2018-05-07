package com.test.epm.locationmap.ui

interface BasePresenter<in V : BaseView> {
    fun attachView(view: V)
    fun detachView()
}