/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lkh.android.base.log;

import android.text.TextUtils;
import android.util.Log;


public final class LogUtils {

    public static LogMasterSwitch masterSwitch = LogMasterSwitch.ALLON;

    public static boolean allowV = true;
    public static boolean allowD = true;
    public static boolean allowI = true;
    public static boolean allowW = true;
    public static boolean allowE = true;

    public static ILog log = new LogCommand();


    public static void v(String tag, String msg){
        if (masterSwitch == LogMasterSwitch.ALLOFF){
            return;
        }
        if (masterSwitch == LogMasterSwitch.IGNORE && !allowV){
            return;
        }
        if (log != null) {
            log.v(tag, msg);
        }
    }
}
