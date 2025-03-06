package com.meizu.hotfix

import android.app.Application
import com.meizu.hotfix.util.HotFixUtil
import java.io.File



/**
 * Created by zhuliyuan on 2018/10/22.
 */
class MainApplication : Application() {

    lateinit var mApplication: MainApplication

    override fun onCreate() {
        super.onCreate()
        mApplication = this
        File(filesDir, "patch/patch1.dex").takeIf { it.exists() }?.let {
            HotFixUtil.inject(classLoader, it)
        }
    }
}
