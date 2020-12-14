package com.example.kakaologin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.usermgmt.response.model.Profile
import com.kakao.usermgmt.response.model.UserAccount
import com.kakao.util.exception.KakaoException


class LoginActivity: AppCompatActivity() {
    private lateinit var callback: SessionCallback


    override fun onCreate(savedIdInstanceState: Bundle?) {
        super.onCreate(savedIdInstanceState)
        setContentView(R.layout.activity_login)
        callback = SessionCallback()
        Session.getCurrentSession().addCallback(callback)
        Session.getCurrentSession().checkAndImplicitOpen()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }

        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
    }


    private inner class SessionCallback: ISessionCallback {
        override fun onSessionOpened() {
            UserManagement.getInstance().me(object : MeV2ResponseCallback() {
                override fun onSuccess(result: MeV2Response?) {
                    var userAccount: UserAccount? = result?.kakaoAccount
                    if (result == null) {
                        Log.e("result is null", "null...")
                    }
                    var profile: Profile? = userAccount?.profile

                    if (profile == null ){
                        Log.e("profile is null", "null...")
                    }
                    var intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("name", profile?.nickname)
                    Log.d("name ", profile?.nickname.toString())
                    startActivity(intent)
                    finish()
                }


                override fun onSessionClosed(errorResult: ErrorResult?) {
                    Toast.makeText(
                        this@LoginActivity,
                        "세션이 닫혔습니다. 다시 시도해주세요 : ${errorResult.toString()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            // 로그인 세션이 정상적으로 열리지 않았을 때
            if (exception != null) {
                com.kakao.util.helper.log.Logger.e(exception)
                Toast.makeText(
                    this@LoginActivity,
                    "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요 : $exception",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun redirectSignupActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}