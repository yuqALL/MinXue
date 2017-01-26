package com.njit.student.yuqzy.minxue.utils;

import com.njit.student.yuqzy.minxue.App;

public class SettingsUtil {

    public static final String THEME = "theme_color";//主题
    public static final String CLEAR_CACHE = "clean_cache";//清空缓存


    public static void setTheme(int themeIndex) {
        SPUtil.put(App.getContext(), THEME, themeIndex);
    }

    public static int getTheme() {
        return (int) SPUtil.get(App.getContext(), THEME, 0);
    }


}
