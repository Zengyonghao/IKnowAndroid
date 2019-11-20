package com.iknow.module.user.login.service;

import androidx.annotation.NonNull;

import com.iknow.lib.beans.user.UserInfoBean;
import com.iknow.module.base.service.user.UserService;
import com.iknow.module.user.R;
import com.iknow.module.user.UserInfoManager;
import com.xiaojinzi.component.anno.ServiceAnno;

import java.util.Optional;

import io.reactivex.Observable;

@ServiceAnno(UserService.class)
public class UserServiceImpl implements UserService {

    @Override
    public boolean isLogin() {
        return UserInfoManager.getInstance().isLogin();
    }

    @NonNull
    @Override
    public Observable<Optional<UserInfoBean>> getUserInfo() {
        return UserInfoManager.getInstance().subscribeUserInfo();
    }

    @NonNull
    @Override
    public Observable<Boolean> subscribeLoginState() {
        return UserInfoManager
                .getInstance()
                .subscribeUserInfo()
                .map(item -> item.isPresent());
    }

    @NonNull
    @Override
    public Observable<UserInfoBean> subscribeUser() {
        return null;
    }

    @Override
    public int getDefaultUserAvatar() {
        return R.drawable.user_default_avatar;
    }

}
