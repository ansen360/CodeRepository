package org.code.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

import org.code.R;

/**
 * Created by Ansen on 2016/12/22 15:55.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/ansen360
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p.activity
 * @Description: TODO
 */
public class VibratorActivity extends Activity implements View.OnClickListener {
    private Vibrator vibrator;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibrator);
        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(this);


    }

    public void onStop() {
        super.onStop();
        vibrator.cancel();
    }

    @Override
    public void onClick(View v) {
        /*
         * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
         * */
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 400, 100, 400};   // 停止 开启 停止 开启
        vibrator.vibrate(pattern, 2);           //重复两次上面的pattern 如果只想震动一次，index设为-1
    }
}
