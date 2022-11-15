package com.irlab.testappkotlin

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class KakaoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "from kakao developer")
    }
}