package study.hank.com.myhookplugindevdemo.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import study.hank.com.myhookplugindevdemo.R;

/**
 * 宿主跳到插件的入口
 */
public class MainActivity extends Activity {//前方大坑！这里不能用AppCompatActivity , 只能用android.app.Activity
    //那么就很奇怪了，AppCompatActivity 到底和Activity有什么区别？为什么要出一个兼容性的，这个要具体去查

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ClassLoader cl = getClassLoader();
        Log.i("David", "onCreate: " + cl);//classLoader

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("study.hank.com.myhookplugindevdemo",
                        "study.hank.com.myhookplugindevdemo.ui.Main2Activity"));
                startActivity(intent);
            }
        });

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("study.hank.com.plugin",
                        "study.hank.com.plugin.Plugin1Activity"));
                startActivity(intent);
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("study.hank.com.plugin",
                        "study.hank.com.plugin.Plugin2Activity"));
                startActivity(intent);
            }
        });

        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("study.hank.com.plugin",
                        "study.hank.com.plugin.Plugin3Activity"));
                startActivity(intent);
            }
        });
    }
}
