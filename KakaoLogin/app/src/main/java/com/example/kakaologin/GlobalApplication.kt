package com.example.kakaologin

import android.app.Application
import com.kakao.auth.*


class GlobalApplication : Application() {
    private object KakaoSDKAdapter : KakaoAdapter() {
        override fun getApplicationConfig(): IApplicationConfig {
            return IApplicationConfig { getGlobalApplicationContext()?.applicationContext!! }
        }


        override fun getSessionConfig(): ISessionConfig {
            return object : ISessionConfig {
                override fun getAuthTypes(): Array<AuthType> {
                    return arrayOf(AuthType.KAKAO_LOGIN_ALL)
                }

                override fun isUsingWebviewTimer(): Boolean {
                    return false
                }

                override fun isSecureMode(): Boolean {
                    return false
                }

                override fun getApprovalType(): ApprovalType? {
                    return ApprovalType.INDIVIDUAL
                }

                override fun isSaveFormData(): Boolean {
                    return true
                }
            }
        }
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        KakaoSDK.init(KakaoSDKAdapter)
    }


    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }


    companion object {
        var instance: GlobalApplication? = null

        fun getGlobalApplicationContext(): GlobalApplication? {
            checkNotNull(this) { "this application does not inherit com.kakao.GlobalApplication" }
            return instance
        }
    }
}