package com.njit.student.yuqzy.minxue.http.api;


import com.njit.student.yuqzy.minxue.http.BaseAppResponse;
import com.njit.student.yuqzy.minxue.model.UpdateInfo;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by liyu on 2016/12/1.
 */

public interface AppController {

    @GET("http://api.caoliyu.cn/appupdate.json")
    Observable<BaseAppResponse<UpdateInfo>> checkUpdate();
}
