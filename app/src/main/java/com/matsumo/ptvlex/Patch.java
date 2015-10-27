/**
 * ptvlex Copyright (C) 2015 matsumo All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.matsumo.ptvlex;

import java.util.AbstractMap;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class Patch implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        // probably works only
        // versionCode="21", versionName="5.0.1-1624448"
        // it is dexguarded.
        if (loadPackageParam.packageName.equals("com.getpebble.android.basalt")) {
            findAndHookMethod("com.getpebble.android.onboarding.fragment.ChooseLanguageFragment",
                loadPackageParam.classLoader, "a", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    AbstractMap<String, String> list = (AbstractMap<String, String>) param.getResult();
                    list.put("jpn-JPN", "ja_JP");       // Japanese
                    // http://developer.nuance.com/public/index.php?task=supportedLanguages
                    }
                });
        }

        if (loadPackageParam.packageName.startsWith("com.google.") || loadPackageParam.packageName.startsWith("com.android.") || loadPackageParam.packageName.compareTo("android")==0) return;
        try{
            findAndHookMethod("com.google.android.gms.ads.AdView",
                loadPackageParam.classLoader,
                "setAdUnitId",
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        param.args[0] = "ca-app-pub-2920750383521778/7321829170";
                    }
                });
        }catch(Throwable e){ }
    }
}
