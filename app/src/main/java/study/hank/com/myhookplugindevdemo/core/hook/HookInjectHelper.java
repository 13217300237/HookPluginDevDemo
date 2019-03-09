package study.hank.com.myhookplugindevdemo.core.hook;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;
import study.hank.com.myhookplugindevdemo.app.MyApplication;

/**
 * Created by baby on 2018/4/2.
 */

public class HookInjectHelper {


    /**
     *
     * 此方法的作用是：插件内的class融合到宿主的classLoader中，让宿主可以直接读取插件内的class
     *
     * @param context
     */
    public static void injectPluginClass(Context context) {
        String cachePath = context.getCacheDir().getAbsolutePath();
        String apkPath = MyApplication.pluginPath;

        //还记不记得dexClassLoader？它是专门用于加载外部apk的classes.dex文件的
        //(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent)
        // 4个参数分别是，外部dex的path，优化之后的目录，lib库文件查找目录，我们这没有用到lib里面的so，所以可以设置为null,最后一个是父ClassLoader
        DexClassLoader dexClassLoader = new DexClassLoader(apkPath, cachePath, null, context.getClassLoader());
        //先构造一个能够读取外部apk的classLoader对象

        //     第一步   找到    插件的Elements数组  dexPathlist  ----？dexElement

        try {
            Class myDexClazzLoader = Class.forName("dalvik.system.BaseDexClassLoader");
            Field myPathListFiled = myDexClazzLoader.getDeclaredField("pathList");
            myPathListFiled.setAccessible(true);
            Object myPathListObject = myPathListFiled.get(dexClassLoader);

            Class myPathClazz = myPathListObject.getClass();
            Field myElementsField = myPathClazz.getDeclaredField("dexElements");
            myElementsField.setAccessible(true);
//          自己插件的  dexElements[]
            Object myElements = myElementsField.get(myPathListObject);

            //     第二步   找到    系统的Elements数组    dexElements
            PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
            Class baseDexClazzLoader = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathListFiled = baseDexClazzLoader.getDeclaredField("pathList");
            pathListFiled.setAccessible(true);
            Object pathListObject = pathListFiled.get(pathClassLoader);

            Class systemPathClazz = pathListObject.getClass();
            Field systemElementsField = systemPathClazz.getDeclaredField("dexElements");
            systemElementsField.setAccessible(true);
            //系统的  dexElements[]
            Object systemElements = systemElementsField.get(pathListObject);
            //     第三步  上面的dexElements  数组  合并成新的  dexElements     然后通过反射重新注入系统的Field （dexElements ）变量中

//       新的     Element[] 对象
//            dalvik.system.Element

            int systemLength = Array.getLength(systemElements);
            int myLength = Array.getLength(myElements);
//            找到 Element  的Class类型   数组    每一个成员的类型
            Class<?> sigleElementClazz = systemElements.getClass().getComponentType();
            int newSysteLength = myLength + systemLength;
            Object newElementsArray = Array.newInstance(sigleElementClazz, newSysteLength);
//融合
            for (int i = 0; i < newSysteLength; i++) {
//                先融合 插件的Elements
                if (i < myLength) {
                    Array.set(newElementsArray, i, Array.get(myElements, i));
                } else {
                    Array.set(newElementsArray, i, Array.get(systemElements, i - myLength));
                }
            }
            Field elementsField = pathListObject.getClass().getDeclaredField("dexElements");
            ;
            elementsField.setAccessible(true);
//            将新生成的EleMents数组对象重新放到系统中去
            elementsField.set(pathListObject, newElementsArray);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static Resources injectPluginResources(Context context) {
        AssetManager assetManager;
        Resources newResource = null;
        String apkPath = MyApplication.pluginPath;
        try {
            assetManager = AssetManager.class.newInstance();
            Method addAssetPathMethod = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
            addAssetPathMethod.setAccessible(true);
            addAssetPathMethod.invoke(assetManager, apkPath);
            Resources supResource = context.getResources();
            newResource = new Resources(assetManager, supResource.getDisplayMetrics(), supResource.getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newResource;
    }
}
