## 简介
本项目整合了开发过程中常用工具类,常用技术的示例代码.抱着学无止境的态度,该项目将会持续更新.目前已上架应用宝
[![](https://jitpack.io/v/ansen360/CodeRepository.svg)](https://jitpack.io/#ansen360/CodeRepository)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/42ae3b1def3b4278b1df9ef4bb7d47a0)](https://www.codacy.com/app/ansen360/CodeRepository?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ansen360/CodeRepository&amp;utm_campaign=Badge_Grade)
[![](https://img.shields.io/badge/author-ansen-ff69b4.svg)](http://blog.csdn.net/qq_25804863?viewmode=contents)
#### Toast 工具
> CodeRepository\app\src\main\java\org\code\common\ToastUtils.java

**Usage:**(支持在子线程中Toast)

setp1:(程序入口初始化)
```
public class CodeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.init(this);
    }
}
```
setp2:(使用)
```
    ToastUtils.showSuccess("success");
    ToastUtils.showInfo("Info");
    ToastUtils.showWarning("Warning");
    ToastUtils.showError("Error");
    ToastUtils.showIcon("Icon", R.mipmap.ic_launcher);
    ToastUtils.show("normal");
    ToastUtils.show("normal2", getResources().getColor(R.color.colorBlue));
```
![](https://github.com/ansen360/CodeRepository/blob/master/gif/1_toast.gif)
#### 网速监控悬浮窗
> D:\Workspace_AS\CodeRepository\app\src\main\java\org\code\service\NetSpeedService.java

![](https://github.com/ansen360/CodeRepository/blob/master/gif/2_mobiledata.gif)
#### 获取不同格式的当前时间日期
http://blog.csdn.net/qq_25804863/article/details/49703779
#### DES3,RSA.对称加密和非对称加密Demo
#### Android微信抢红包Demo
#### Android沉浸模式
#### Android相机DemoTabLayout
http://blog.csdn.net/qq_25804863/article/details/49742159
#### Android通知栏大全Notification
#### Android对话框大全Dialog
http://blog.csdn.net/qq_25804863/article/details/48688611
#### 通过Messenger实现的IPC进程间通讯
http://blog.csdn.net/qq_25804863/article/details/43936223
#### JNI Demo
http://blog.csdn.net/qq_25804863/article/details/48566327
#### Socket通讯,tcp&udp协议
http://blog.csdn.net/qq_25804863/article/details/48698873
#### java自定义注解
#### 蓝牙通讯Demo
#### Camera调用Demo

### 自定义控件:
#### TableLayout
#### FlowLayout流式布局
#### 图片轮廓绘制
#### WheelView


#### Android工具类
- ActivityStackUtils
- AnimationUtil
- AppUtils
- Assert
- BadgeUtil
- BitmapLruCache
- Blur
- CollectionUtils
- Config
- ContactsUtils
- DeviceUtils
- DialogUtil
- DisplayUtils
- DriveUUIDUtil
- EditTextViewUtils
- EMailUtils
- FileUtils
- GPSManager
- GsonUtils
- HttpErrorUtils
- HttpUtil
- ImageUtils
- IntentUtils
- IoUtils
- KeyboardsUtils
- LbsUtil
- Logger
- LruCacheUtil
- NetworkUtils
- PackageUtils
- PhoneUtils
- RecoderUtils
- RegularUtils
- ResourceUtils
- ScreenUtils
- SettingsUtils
- SHAUtil
- ShellUtils
- ShockUtil
- SizeUtils
- SpUtils
- SQLiteUtil
- TextViewUtils
- ThreadPoolManager
- TimeUtils
- ToastUtils
- UiUtils
- UmUtils
- Utils
- ViedoUtil
- WeekDateObj
- ZipUtils

## 命名规范
- 包名全部采用小写
- 常量、枚举等均采用大写形式，用下划线区分各单词。使用static final
```
private static final String TAG = "XXXX"
```
- 类名、接口名、枚举名。第一个和后面的单词都要第一个字母大写
```
MainActivity,InstalledAppDetails
```
- 资源文件命名
```
activity_main.xml，ic_launcher.png
```
- 注意图片文件命名只能用小写字母、数字，否则会导致R文件无法编译出来
- 继承自安卓组件的类，一般采用父类名作为后缀，
```
class LoginActivity extends Activity{}
```
- 自定义异常必须以Exception结尾
- 全局变量添加所有者前缀：实例成员变量前缀m（表示member）,类静态变量前缀s（表示static）
```
protected ContentResolver mContentResolver;
```
- 控件变量添加组件前缀,控件前缀+控件属性,控件缩写button->btnLogin,或者bt_login;全局名称mBtnNext局部名称btnNext
- 构造方法采用递增方式（参数多的写在后面），参数少的调用参数多的构造函数。这样也减少初始化代码。比如开源库PagerSlidingTabStrip

##编码规范
- 源文件编码格式为 UTF-8。
- java代码中不出现中文，最多注释中可以出现中文
服务端可以实现的，就不要放在客户端
- 引用第三方库要慎重，避免应用大容量的第三方库，导致客户端包非常大
- 处理应用全局异常和错误，将错误以邮件的形式发送给服务端
- 图片的.9处理
- 使用静态变量方式实现界面间共享要慎重
- 单元测试（逻辑测试、界面测试）
- 不要重用父类的handler，对应一个类的handler也不应该让其子类用到，否则会导致message.what冲突
- activity中在一个View.OnClickListener中处理所有的逻辑
- strings.xml中使用%1$s实现字符串的通配
- 数据一定要效验，例如字符型转数字型，如果转换失败一定要有缺省值；服务端响应数据是否有效判断
- 对于未完成的方法，使用TODO加以标记
- 若功能已完成，但存在效率等潜在问题时，使用XXX加以标记
- 若代码存在严重问题或仅用于调试，使用FIXME加以标记
- values目录下文件名称较固定，不得随意更改

---

### 提交规范
- 代码及时更新,不要和服务器有太大的差别,减少merge的log
- 提交代码时,如果出现冲突,必须仔细分析解决,才可提交
- 提交代码之前先在本地进行测试,确保项目能编译通过,且能够正常运行,本地没有验证的代码原则上不允许提交
- 必须保证服务器上的版本是正确的,项目有错误时,不要进行提交
- 提交之前先更新代码
- 提交时注意不要提交本地自动生成的文件,比如我们AndroidStudio项目中的 idea,build文件夹是不需要提交的
- 不要提交自己不明白的代码
- 提前协调好项目组成员的工作计划,减少冲突
- 对提交的信息尽量写得详细,方便后续定位问题;最好使用提交模板
```
[CAUSE]     :
[SOLUTION]  :
[REVIEW]    : Own
[SIDEEFFECT]: without side effects
[PROJECT]   : V3
[CR]        :V3 SM-2182
[MODULAR]   :
modified:   src/com/android/settings/applications/InstalledAppDetails.java
```
### 架构设计规范
提高架构的拓展性,选择框架时一定要了解好框架本身的扩展性如何，或者对框架有较深的理解，能够自己扩展框架,提高架构的稳定性,架构的文档也是必不可少的。
- activity和fragment里面都会有许多重复的操作以及操作步骤，所以我们都需要提供一个BaseActivity和BaseFragment，让所有的activity和fragment都继承这个基类
- 必要的注释真的会一定程度上的降低你的工作量
比如说我使用Rxjava做加载数据的操作。这里面的流程可能稍显复杂，但是能够step1， step2的写在上面，能够让别人看懂，自己维护也方便。
- 数据提供统一的入口。
无论是在mvp，mvc，还是mvvm中，提供一个统一的数据入口，都可以让代码变得更加易于维护。
比如，我使用的DataManager，里面的http还是preference，还是eventpost ，还是database ，都在DataManger里面进行操作，我们只需要与DataManger打交道。
- 多用组合, 少用继承
- 提取方法, 去除重复代码。
对于必要的工具类抽取也很重要，这在以后的项目中是可以重用的。
- 不要使用魔鬼数字/字符串/尺寸值/颜色值，正确的命名等
- 引入Dagger2 减少模块之间的耦合性
Dagger2 是一个依赖注入框架，使用代码自动生成创建依赖关系需要的代码。减少很多模板化的代码，更易于测试，降低耦合，创建可复用可互换的模块。
- 为你的项目引入Rxjava+RxAndroid这些响应式编程吧。极大的减少逻辑代码，让你爱上写代码停不下来。
- 通过引入 Event Bus（事件总线，这个项目使用的是otto）。它允许我们在Data Layer中发送事件，以便View Layer中的多个组件都能够订阅到这些事件。比如DataManager
中的退出登录方法可以发送一个事件，订阅这个事件的多个Activity在接收到该事件后就能够更改它们的UI视图，从而显示一个登出状态。
当然你也可以有很多的选择，EventBus，Otto，自定义RxBus等。减少回调。
- 添加日志打印，用于查找错误等
需要使用BuildConfig.DEBUG标记对Log进行封装，只在调试时输出重要信息，正式版不输出


架构设计是为了方便软件维护、扩展、安全性
##### 维护性:
1.代码规范
2.框架稳定性
3.封装
4.耦合
##### 扩展性:
1.抽象接口
2.元素重用
3.单一职责
4.替换性
5.耦合
##### 安全性:
1.数据安全性
数据安全就包括数据抓取、数据拦截以及数据修改
2.操作安全性
避免对一个操作重复执行


