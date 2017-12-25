/*
 * Copyright (C) 2015 Bilibili <jungly.ik@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smartstudy.uskid.library.share.core.handler;

import android.app.Activity;

import com.smartstudy.uskid.library.share.core.BiliShareConfiguration;
import com.smartstudy.uskid.library.share.core.SocializeMedia;
import com.smartstudy.uskid.library.share.core.handler.generic.CopyHandler;
import com.smartstudy.uskid.library.share.core.handler.generic.GenericHandler;
import com.smartstudy.uskid.library.share.core.handler.qq.QQChatHandler;
import com.smartstudy.uskid.library.share.core.handler.qq.QQZoneHandler;
import com.smartstudy.uskid.library.share.core.handler.sina.SinaTransitHandler;
import com.smartstudy.uskid.library.share.core.handler.wx.WxChatHandler;
import com.smartstudy.uskid.library.share.core.handler.wx.WxMomentHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jungly
 * @email jungly.ik@gmail.com
 * @since 2015/10/12
 */
public class ShareHandlerPool {

    private static ShareHandlerPool ourInstance = new ShareHandlerPool();
    private Map<SocializeMedia, IHandler> mHandlerMap = new HashMap<>();

    private ShareHandlerPool() {
    }

    public static IHandler newHandler(Activity context, SocializeMedia type, BiliShareConfiguration shareConfiguration) {
        IHandler handler;
        switch (type) {
            case WEIXIN:
                handler = new WxChatHandler(context, shareConfiguration);
                break;

            case WEIXIN_MONMENT:
                handler = new WxMomentHandler(context, shareConfiguration);
                break;

            case QQ:
                handler = new QQChatHandler(context, shareConfiguration);
                break;

            case QZONE:
                handler = new QQZoneHandler(context, shareConfiguration);
                break;

            case SINA:
                handler = new SinaTransitHandler(context, shareConfiguration);
                break;

            case COPY:
                handler = new CopyHandler(context, shareConfiguration);
                break;

            default:
                handler = new GenericHandler(context, shareConfiguration);
        }

        ourInstance.mHandlerMap.put(type, handler);

        return handler;
    }

    public static IHandler getCurrentHandler(SocializeMedia type) {
        return ourInstance.mHandlerMap.get(type);
    }

    public static void remove(SocializeMedia type) {
        ourInstance.mHandlerMap.remove(type);
    }

}
