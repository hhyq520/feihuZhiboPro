/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package cn.feihutv.zhibofeihu.utils;


import android.util.Log;

import com.orhanobut.logger.Logger;


/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : 调试打印日志类
 *     version: 1.0
 * </pre>
 */
public class AppLogger {

    public static void init() {
//        if (cn.feihutv.zhibofeihu.BuildConfig.DEBUG) {
//            Logger.init("appLogger")                // default PRETTYLOGGER or use just init()
//                    .methodCount(2)                 // default 2
//                    .hideThreadInfo()               // default shown
//                    .logLevel(LogLevel.FULL)        // default LogLevel.FULL
//                    .methodOffset(0)  ;            // default 0
//
//            ; //default AndroidLogAdapter
//        }
    }

    public static void d(String s) {
        Logger.d(s);
    }


    public static void i(String s) {
        Logger.i(s);
    }

    public static void w(String s) {
        Logger.w(s);
    }


    public static void e(String s) {
        Logger.e(s);
    }


    public static void i(String tag,String s){
        if (cn.feihutv.zhibofeihu.BuildConfig.DEBUG) {
            Log.i(tag,s);
        }
    }
    public static void e(String tag,String s){
        if (cn.feihutv.zhibofeihu.BuildConfig.DEBUG) {
            Log.e(tag,s);
        }
    }

    public static void d(String tag,String s){
        if (cn.feihutv.zhibofeihu.BuildConfig.DEBUG) {
            Log.d(tag,s);
        }
    }

    public static void w(String tag,String s){
        if (cn.feihutv.zhibofeihu.BuildConfig.DEBUG) {
            Log.w(tag,s);
        }
    }
}
