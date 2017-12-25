package com.smartstudy.uskid.library.share.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.openapi.IWXAPI;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by caoruixiang on 2017/6/2.
 */

public class WxApiHandler implements InvocationHandler {
  /**
   * 被代理的原始对象
   */
  Context mContext;
  Object base;

  private static List<Pair<String, String>> key_pkgs = new ArrayList<>();
  Pair<String, String> effective_key_pkg = null;
  static Runnable mAction0;

  static {
    key_pkgs.add(new Pair<>("wxf0a80d0ac2e82aa7", "com.tencent.mobileqq"));
    key_pkgs.add(new Pair<>("wx64f9cf5b17af074d", "com.tencent.mtt"));
    key_pkgs.add(new Pair<>("wx50d801314d9eb858", "com.ss.android.article.news"));
    key_pkgs.add(new Pair<>("wx299208e619de7026", "com.sina.weibo"));
    key_pkgs.add(new Pair<>("wx27a43222a6bf2931", "com.baidu.searchbox"));
    key_pkgs.add(new Pair<>("wx020a535dccd46c11", "com.UCMobile"));
    key_pkgs.add(new Pair<>("wx9e7e2766ee2d0eee", "com.UCMobile.ac"));
    key_pkgs.add(new Pair<>("wx2ace6041e8919680", "com.UCMobile.dev"));
    key_pkgs.add(new Pair<>("wx8781aa7b0facd259", "com.UCMobile.x86"));
    key_pkgs.add(new Pair<>("wxd6415d454a022e1e", "com.UCMobile.love"));
  }

  public static boolean sendReq(Context mContext, IWXAPI mApi, BaseReq var1, Runnable action0) {
    IWXAPI iwxapi = (IWXAPI) Proxy.newProxyInstance(IWXAPI.class.getClassLoader(),
        mApi.getClass().getInterfaces(), new WxApiHandler(mContext, mApi, action0));
    return iwxapi.sendReq(var1);
  }

  private WxApiHandler(Context context, Object base, Runnable action0) {
    this.mContext = context;
    this.base = base;
    this.mAction0 = action0;
  }

  @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if ("sendReq".equals(method.getName())) {
      Bundle bundle = new Bundle();
      for (Object arg : args) {
        if (arg instanceof BaseReq) {
          BaseReq baseReq = (BaseReq) arg;
          baseReq.toBundle(bundle);
        }
      }
      return send(bundle);
    }
    return method.invoke(args);
  }

  private boolean send(Bundle paramBundle) {
    if (mContext != null) {
      Intent localIntent;
      (localIntent = new Intent()).setClassName("com.tencent.mm",
          "com.tencent.mm.plugin.base.stub.WXEntryActivity");
      if (paramBundle != null) {
        localIntent.putExtras(paramBundle);
      }

      if (effective_key_pkg == null) {
        effective_key_pkg = pickCanUseReplacement();
      } else {
        boolean isInstall = appIsInstall(mContext, effective_key_pkg.second);
        if (!isInstall) {
          effective_key_pkg = pickCanUseReplacement();
        }
      }

      if (effective_key_pkg == null) {
        mAction0.run();
        return true;
      }

      //            localIntent.putExtra("_mmessage_appPackage", "com.tencent.mobileqq");
      localIntent.putExtra("_mmessage_appPackage", effective_key_pkg.second);
      //            localIntent.putExtra("_mmessage_content", "weixin://sendreq?appid=wxf0a80d0ac2e82aa7");
      localIntent.putExtra("_mmessage_content",
          "weixin://sendreq?appid=" + effective_key_pkg.first);
      //            localIntent.putExtra("_mmessage_checksum", a("weixin://sendreq?appid=wxf0a80d0ac2e82aa7", "com.tencent.mm"));
      localIntent.putExtra("_mmessage_checksum",
          a("weixin://sendreq?appid=" + effective_key_pkg.first, effective_key_pkg.second));
      localIntent.putExtra("_mmessage_sdkVersion", 553910273);
//      localIntent.addFlags(268435456).addFlags(134217728);

      try {
        mContext.startActivity(localIntent);
      } catch (ActivityNotFoundException var8) {
        Log.e("MMessageAct", "send fail, target ActivityNotFound");
        return false;
      }

      Log.d("MMessageAct", "send mm message, intent=" + localIntent);
      return true;
    } else {
      Log.e("MMessageAct", "send fail, invalid arguments");
      return false;
    }
  }

  private Pair<String, String> pickCanUseReplacement() {
    Pair<String, String> stringPair = null;
    for (int i = 0; i < key_pkgs.size(); i++) {
      Pair<String, String> stringStringPair = key_pkgs.get(i);
      String pkM = stringStringPair.second;
      boolean isInstall = appIsInstall(mContext, pkM);
      if (isInstall) {
        stringPair = stringStringPair;
        break;
      }
    }
    return stringPair;
  }

  private byte[] a(String var0, String var1) {
    StringBuffer var2 = new StringBuffer();
    if (var0 != null) {
      var2.append(var0);
    }

    var2.append(553910273);
    var2.append(var1);
    var2.append("mMcShCsTr");
    return getMessageDigest(var2.toString().substring(1, 9).getBytes()).getBytes();
  }

  private final String getMessageDigest(byte[] var0) {
    char[] var1 = new char[] {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    try {
      MessageDigest var2;
      (var2 = MessageDigest.getInstance("MD5")).update(var0);
      int var8;
      char[] var3 = new char[(var8 = (var0 = var2.digest()).length) * 2];
      int var4 = 0;

      for (int var5 = 0; var5 < var8; ++var5) {
        byte var6 = var0[var5];
        var3[var4++] = var1[var6 >>> 4 & 15];
        var3[var4++] = var1[var6 & 15];
      }

      return new String(var3);
    } catch (Exception var7) {
      return null;
    }
  }

  /**
   * 检查某个应用是否安装
   */
  private static boolean appIsInstall(Context appContext, String packageName) {
    boolean install = false;
    if (!TextUtils.isEmpty(packageName) && null != appContext) {
      PackageInfo packageInfo;
      try {
        packageInfo = appContext.getPackageManager()
            .getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
      } catch (Exception e) {
        packageInfo = null;
        e.printStackTrace();
      }
      if (packageInfo != null) {
        install = true;
      }
    }
    return install;
  }
}
