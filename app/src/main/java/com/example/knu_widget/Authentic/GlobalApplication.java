package com.example.knu_widget.Authentic;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class GlobalApplication extends Application {
    private static GlobalApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        KakaoSdk.init(instance, "73046125eadeb41a4d09c2836bbdafb2");
    }

}
