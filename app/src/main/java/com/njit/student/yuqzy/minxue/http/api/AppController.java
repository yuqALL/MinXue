package com.njit.student.yuqzy.minxue.http.api;


import com.njit.student.yuqzy.minxue.http.BaseAppResponse;
import com.njit.student.yuqzy.minxue.model.UpdateInfo;

import retrofit2.http.GET;
import rx.Observable;

public interface AppController {

    @GET("https://github.com/yuqZY/MinXue/updateinfo.json")
    Observable<BaseAppResponse<UpdateInfo>> checkUpdate();
}
