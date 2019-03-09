package study.hank.com.plugin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Plugin2Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin2);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Plugin2Activity.this, Plugin3Activity.class);
                startActivity(i);
            }
        });
    }
}
