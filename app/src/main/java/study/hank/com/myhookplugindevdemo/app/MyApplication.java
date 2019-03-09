package study.hank.com.myhookplugindevdemo.app;

import android.app.Application;
import android.content.res.Resources;

import study.hank.com.myhookplugindevdemo.core.Const;
import study.hank.com.myhookplugindevdemo.core.hook.GlobalActivityHookHelper;
import study.hank.com.myhookplugindevdemo.core.hook.HookInjectHelper;
import study.hank.com.myhookplugindevdemo.utils.AssetUtil;

/**
 * Created by baby on 2018/4/2.
 */

public class MyApplication extends Application {

    private Resources newResource;

    public static String pluginPath = null;

    @Override
    public void onCreate() {
        super.onCreate();
        pluginPath = AssetUtil.copyAssetToCache(this, Const.PLUGIN_FILE_NAME);

        //Hook第一次
        GlobalActivityHookHelper.hook(this);

        //Hook第二次
        HookInjectHelper.injectPluginClass(this);//这里通过Ｈｏｏｋ手段已经把插件的源文件class导入了内存中
        //OK,这个就是我研究的重点，莫名其妙啊！？

        //加载插件资源包
        newResource = HookInjectHelper.injectPluginResources(this);
    }


    @Override
    public Resources getResources() {//重写资源管理器,资源管理器是每个Activity自带的，而Application的getResources则是所有Activity共有的
        return newResource == null ? super.getResources() : newResource;
    }
}
