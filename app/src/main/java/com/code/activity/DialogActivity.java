package com.code.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.EditText;

import com.code.R;
import com.code.common.ToastUtils;

/**
 * Created by Ansen on 2015/09/08 18:21.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/ansen360
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p.activity
 * @Description: TODO
 */
public class DialogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
    }

    /**
     * 确定取消对话框
     *
     * @param v
     */
    public void click1(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("友情提示");
        builder.setMessage("若练此功,必先自宫,是否继续");
        builder.setPositiveButton("是的,想好了", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastUtils.show("啊...");
                ToastUtils.show("即使自宫,未必成功...");
            }
        });
        builder.setNegativeButton("想想在说", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastUtils.show("若不自宫,一定不成功");

            }
        });
        builder.show();

    }

    /**
     * 单选对话框
     *
     * @param v
     */
    public void click2(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择性别");
        final String[] items = {"男", "女", "未知"};
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ToastUtils.show(items[which]);

            }
        });
        builder.show();
    }

    /**
     * 多选对话框2
     *
     * @param v
     */
    public void click3(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入你喜欢的年龄");
        final String[] items = {"18", "19", "20", "21"};
        final boolean[] checkedItems = new boolean[]{false, false, false,
                false};
        builder.setMultiChoiceItems(items, checkedItems,
                new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        checkedItems[which] = isChecked;
                        ToastUtils.show(items[which]);

                    }
                });
        builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        sb.append(items[i] + ",");
                    }
                }
                ToastUtils.show("你选择的是:" + sb.toString());
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.show();
    }

    /**
     * 列表对话框
     *
     * @param v
     */
    public void click4(View v) {
        new AlertDialog.Builder(this).setTitle("列表框")
                .setItems(new String[]{"Item1", "Item2"}, null)
                .setNegativeButton("确定", null).show();

    }

    /**
     * 三选对话框
     *
     * @param v
     */
    public void click5(View v) {
        Dialog dialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.btn_star).setTitle("智商考验")
                .setMessage("你是猪吗？")
                .setPositiveButton("是的", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        ToastUtils.show("这智商...");
                    }
                }).setNegativeButton("好像是的", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        ToastUtils.show("这智商......");
                    }
                }).setNeutralButton("应该是的", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        ToastUtils.show("这智商.........");
                    }
                }).create();

        dialog.show();
    }

    public void click6(View v) {
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(this).setTitle("请输入")
                .setIcon(android.R.drawable.ic_dialog_info).setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtils.show(editText.getText().toString());

                    }
                }).setNegativeButton("取消", null).show();

    }

    /**
     * 进度对话框1
     *
     * @param v
     */
    public void process1(View v) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("提醒");
        pd.setMessage("正在处理...");
        pd.show();
        new Thread() {
            public void run() {
                SystemClock.sleep(2000);
                pd.dismiss();
            }

            ;
        }.start();
    }

    /**
     * 进度对话框2
     *
     * @param v
     */
    public void process2(View v) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setTitle("提醒");
        pd.setMessage("正在处理...");
        pd.show();
        new Thread() {
            public void run() {
                pd.setMax(100);
                for (int i = 0; i <= 100; i++) {
                    pd.setProgress(i);
                    SystemClock.sleep(80);
                }
                pd.dismiss();
            }

            ;
        }.start();

    }

    public void div(View v) {
        ToastUtils.show("更炫酷的自定义对话框请参看另一篇帖子");
    }
}