package com.example.knu_widget;

import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.kakao.sdk.common.KakaoSdk;

import java.security.Key;

public class GlobalApplication extends Application {
    private static GlobalApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        KakaoSdk.init(instance, "73046125eadeb41a4d09c2836bbdafb2");
    }

}
