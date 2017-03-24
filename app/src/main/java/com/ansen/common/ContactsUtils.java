package com.ansen.common;

import android.Manifest;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.ansen.common.bean.CallLogBean;
import com.ansen.common.bean.ContactBean;
import com.ansen.common.bean.SmsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ansen on 2016/9/23.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/ansen360
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p.common
 * @Description: TODO
 */
public class ContactsUtils {
    private static final String TAG = "ansen";
    public static final int READ_CALL_LOG = 0;
    public static final int WRITE_CALL_LOG = 1;
    public static final int READ_CALL_LOG_COUNT = 2;
    public static final int READ_CONTACTS = 3;
    public static final int WRITE_CONTACTS = 4;
    public static final int READ_CONTACTS_COUNT = 5;
    public static final int READ_SMS = 6;
    public static final int READ_PHONE_STATE = 7;
    public static final int READ_SMS_COUNT = 8;


    public static boolean isRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return true;
        return false;
    }

    public static String getCallLogCount(Activity activity) {
        if (isRequestPermission()) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALL_LOG}, READ_CALL_LOG_COUNT);
                return null;
            }
        }
        Cursor cursor = activity.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        return cursor.getCount() + "";
    }

    public static List<CallLogBean> getCallLog(Activity activity) {
        if (isRequestPermission()) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALL_LOG}, READ_CALL_LOG);
                return null;
            }
        }
        ArrayList<CallLogBean> callLogBeen = new ArrayList<>();
        Cursor cursor = activity.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            CallLogBean callLog = new CallLogBean();
            String name = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE));
            //通话时间,单位:s
            String duration = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION));
//            int incomingType = CallLog.Calls.INCOMING_TYPE;
            int type = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)));   //呼叫类型
//            SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date date = new Date(Long.parseLong()));
//            String time = sfd.format(date); //呼叫时间
            callLog.setName(name);
            callLog.setMobileNo(number);
            callLog.setCallTime(date);
            callLog.setCallDuration(duration);
            callLog.setCallType(type);
            Logger.d(TAG, callLog.toString());
            callLogBeen.add(callLog);
        }
        return callLogBeen;
    }

    public static void insertCallLog(Activity activity, List<CallLogBean> callLogBeens) {
        if (isRequestPermission()) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_CALL_LOG}, WRITE_CALL_LOG);
                return;
            }
        }
        for (CallLogBean callLog : callLogBeens) {
            ContentValues values = new ContentValues();
            values.put(CallLog.Calls.CACHED_NAME, callLog.getName());
            values.put(CallLog.Calls.NUMBER, callLog.getMobileNo());
            values.put(CallLog.Calls.DATE, callLog.getCallTime());
            values.put(CallLog.Calls.DURATION, callLog.getCallDuration());
            values.put(CallLog.Calls.TYPE, callLog.getCallType());//未接
//            values.put(CallLog.Calls.NEW, 0);//0已看1未看
            activity.getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
        }
    }

    public static void insertSMSAndCheckPermission(final Activity activity, final List<SmsBean> smsBeans, final Handler handler) {
        if (isRequestPermission()) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_SMS}, READ_SMS);
                return;
            }
        }
        insertSMS(activity, smsBeans);
        if (handler == null) {
//            ToastUtils.show(R.string.sms_recover_seccess);
        } else {
            handler.sendEmptyMessage(0);
        }
    }

    public static void insertSMS(final Activity activity, final List<SmsBean> smsBeans) {
        ContentResolver contentResolver = activity.getContentResolver();
        for (SmsBean item : smsBeans) {
            // 判断短信数据库中是否已包含该条短信，如果有，则不需要恢复
            Cursor cursor = contentResolver.query(Uri.parse("content://sms"), new String[]{"date"}, "date" + "=?", new String[]{item.getMessageTime()}, null);
            if (!cursor.moveToFirst()) {// 没有该条短信
                Logger.d(TAG, "insertSMSAndCheckPermission: " + item.toString());
                ContentValues values = new ContentValues();
                values.put("address", item.getMobileNo());
                // 如果是空字符串说明原来的值是null，所以这里还原为null存入数据库
                values.put("date", item.getMessageTime());
                values.put("type", item.getCallType());
                values.put("body", item.getContent());
                values.put("read", 1);
                contentResolver.insert(Uri.parse("content://sms"), values);
            }
            cursor.close();
        }
    }

    public static List<SmsBean> getSmsAndCheckPermission(Activity activity) {
        if (isRequestPermission()) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_SMS}, READ_SMS);
                return null;
            }
        }
        return getSms(activity);
    }

    public static List<SmsBean> getSms(Context activity) {
        List<SmsBean> smsBeans = new ArrayList<SmsBean>();
        ContentResolver contentResolver = activity.getContentResolver();//这个ContentResolver是对别的应用的数据库操作的时候使用的
        //不能对短信的数据库直接操作 要进行转移
        Uri uri = Uri.parse("content://sms/");
        //介绍下面函数的参数 第一个 是数据库的uri 第二个是返回的列 第三个是返回的行  第四个 不知道 第五个 是设置以什么来排序
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (null != cursor) {
            Log.i(TAG, "查询到sms数据库中数据条目" + cursor.getCount());
            while (cursor.moveToNext()) {
                String _id = cursor.getString(cursor.getColumnIndex("_id"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String body = cursor.getString(cursor.getColumnIndex("body"));
                SmsBean smsInfo = new SmsBean(address, date, body, type);
                Logger.d(TAG, smsInfo.toString());
                smsBeans.add(smsInfo);
            }
            cursor.close();
        }
        return smsBeans;
    }

    public static String getSmsCount(Activity activity) {
        if (isRequestPermission()) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_SMS}, READ_SMS_COUNT);
                return null;
            }
        }
        ContentResolver contentResolver = activity.getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse("content://sms/"), null, null, null, null);
        return cursor.getCount() + "";
    }

    public static String getContactCount(Activity activity) {
        if (isRequestPermission()) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_COUNT);
                return null;
            }
        }
        ContentResolver contentResolver = activity.getContentResolver();
        Cursor cursor = contentResolver.query(android.provider.ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        String count = cursor.getCount() + "";
        cursor.close();
        return count;
    }

    public static List getContactsAndCheckPermission(Activity activity) {
        if (isRequestPermission()) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS);
                return null;
            }
        }
        return getContact(activity);
    }

    public static List getContact(Context activity) {
        ArrayList<ContactBean> contactBeans = new ArrayList<ContactBean>();
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri datauri = Uri.parse("content://com.android.contacts/data");
        Cursor cursor = activity.getContentResolver().query(uri, new String[]{"contact_id"}, null, null, null);
        while (cursor != null && cursor.moveToNext()) {
            String contact_id = cursor.getString(0);
            //根据id查询data表的data1跟mimetype_id
            if (contact_id != null) {
                ContactBean contact = new ContactBean();
                //小细节查询的不是data表，查询的是view_data的视图
                Cursor datacursor1 = activity.getContentResolver().query(datauri, new String[]{"data1", "mimetype", "data2"}, "contact_id=?", new String[]{contact_id}, null, null);
                while (datacursor1 != null && datacursor1.moveToNext()) {
                    String data1 = datacursor1.getString(0);
                    String mimtype = datacursor1.getString(1);
                    String data2 = datacursor1.getString(2);

                    if ("vnd.android.cursor.item/name".equals(mimtype)) {
                        //获取名字
                        contact.setNickname(data1);
                    } else if ("vnd.android.cursor.item/phone_v2".equals(mimtype)) {
                        //获取号码
                        if ("1".equals(data2)) {
                            contact.setHomePhone(data1);
                        } else if ("2".equals(data2)) {
                            contact.setPhone(data1);
                        } else if ("3".equals(data2)) {
                            contact.setCompanyPhone(data1);
                        }
                    }
                }
                contactBeans.add(contact);
            }
        }
        return contactBeans;
    }

    public static void insertContactsAndCheckPermission(final Activity activity, final List<ContactBean> contactBeans, final Handler handler) {
        if (isRequestPermission()) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_CONTACTS}, WRITE_CONTACTS);
                return;
            }
        }
        insertContacts(activity, contactBeans);
        if (handler == null) {
//            ToastUtils.show(R.string.contacts_recover_seccess);
        } else {
            handler.sendEmptyMessage(0);
        }
    }

    public static void insertContacts(Activity activity, List<ContactBean> contactBeans) {
        ContentResolver resolver = activity.getContentResolver();
        Uri uriRaw = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri uri = Uri.parse("content://com.android.contacts/data");
        for (ContactBean contact : contactBeans) {
            // 第一个参数：内容提供者的主机名
            // 第二个参数：要执行的操作
            ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
            // 操作1.添加Google账号，这里值为null，表示不添加
            ContentProviderOperation operation = ContentProviderOperation.newInsert(uriRaw)
                    .withValue("account_name", null)// account_name:Google账号
                    .build();
            operations.add(operation);
            // 操作2.添加data表中name字段
            ContentProviderOperation operation1 = ContentProviderOperation.newInsert(uri)
                    // 第二个参数int previousResult:表示上一个操作的位于operations的第0个索引，
                    // 所以能够将上一个操作返回的raw_contact_id作为该方法的参数
                    .withValueBackReference("raw_contact_id", 0)
                    .withValue("mimetype", "vnd.android.cursor.item/name")
                    .withValue("data2", contact.getNickname())
                    .build();
            operations.add(operation1);

            // 操作3.添加data表中phone字段
            ContentProviderOperation operation2 = ContentProviderOperation.newInsert(uri)
                    .withValueBackReference("raw_contact_id", 0)
                    .withValue("mimetype", "vnd.android.cursor.item/phone_v2")
                    .withValue("data2", "1")
                    .withValue("data1", contact.getHomePhone())
                    .build();
            operations.add(operation2);
            // 操作3.添加data表中phone字段
            ContentProviderOperation operation3 = ContentProviderOperation.newInsert(uri)
                    .withValueBackReference("raw_contact_id", 0)
                    .withValue("mimetype", "vnd.android.cursor.item/phone_v2")
                    .withValue("data2", "2")
                    .withValue("data1", contact.getPhone())
                    .build();
            operations.add(operation3);
            // 操作3.添加data表中phone字段
            ContentProviderOperation operation4 = ContentProviderOperation.newInsert(uri)
                    .withValueBackReference("raw_contact_id", 0)
                    .withValue("mimetype", "vnd.android.cursor.item/phone_v2")
                    .withValue("data2", "3")
                    .withValue("data1", contact.getCompanyPhone())
                    .build();
            operations.add(operation4);
            try {
                resolver.applyBatch("com.android.contacts", operations);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
