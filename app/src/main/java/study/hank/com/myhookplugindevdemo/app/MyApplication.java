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

        //Hook第一次，绕过manifest检测
        GlobalActivityHookHelper.hook(this);

        //Hook第二次把插件的源文件class导入到系统的ClassLoader中
        HookInjectHelper.injectPluginClass(this);

        //Hook第三次，加载插件资源包，让系统的Resources能够读取插件的资源
        newResource = HookInjectHelper.injectPluginResources(this);
    }

    //重写资源管理器,资源管理器是每个Activity自带的，
    // 而Application的getResources则是所有Activity共有的
    //重写了它，就不必一个一个Activity去重写了
    @Override
    public Resources getResources() {
        return newResource == null ? super.getResources() : newResource;
    }
}
