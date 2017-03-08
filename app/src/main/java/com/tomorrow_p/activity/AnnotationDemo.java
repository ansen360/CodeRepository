package com.tomorrow_p.activity;

import android.app.Activity;
import android.os.Bundle;

import com.tomorrow_p.common.Logger;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Created by Ansen on 2017/3/8 18:24.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/ansen360
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p.annotation
 * @Description: TODO
 */
public class AnnotationDemo extends Activity {
    public static final String TAG = "ansen";
    /**
     * 使用 @interface 自定义注解时,自动继承了java.lang.annotation.Annotation接口,由编译程序自动完成其他细节。
     * @interface 用来声明一个注解，其中的每一个方法实际上是声明了一个配置参数。
     * 方法的名称就是参数的名称，返回值类型就是参数的类型（返回值类型只能是 基本类型、Class、String、enum）。
     * 可以通过 default 来声明参数的默认值。
     */
    /**
     * 自定义注解格式：
     * public @interface 注解名{注解体}
     * 所有基本数据类型（int,float,boolean,byte,double,char,long,short),String类型,Class类型,enum类型,Annotation类型
     * Annotation类型参数设定:
     * 第一,只能用 public 或 默认(default) 这两个访问权修饰.例如,String value();这里把方法设为 defaul 默认类型；
     * 第二,参数成员只能用基本类型 byte,short,char,int,long,float,double,boolean 八种基本数据类型 和 String,Enum,Class,annotations 等数据类型,以及这一些类型的数组.例如,String value();这里的参数成员就为String;
     * 第三,如果只有一个参数成员,最好把参数名称设为"value",后加小括号.例:下面的例子 Name 注解就只有一个参数成员。
     * 当一个 Annotation 被定义为运行时Annotation后，改注解才是运行时可见的，当class文件被装载时被保存在class文件中的Annotation才会被虚拟机读取。
     */

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Name {
        String value() default "";
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Gender {

        public enum GenderType {

            Male("男"),
            Female("女"),
            Other("不详");
            private String GenderStr;

            GenderType(String gender) {
                this.GenderStr = gender;
            }

            @Override
            public String toString() {
                return GenderStr;
            }
        }

        Gender.GenderType gender() default Gender.GenderType.Male;
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Profile {

        public int id() default -1;

        public int height() default -1;

        public String nativePlace() default "";

    }

    static class CustomUtils {
        /**
         * AnnotatedElement 接口提供了以下四个方法来访问 Annotation 的信息：
         * 方法1：<T extends Annotation> T getAnnotation(Class<T> annotationClass): 返回改程序元素上存在的、指定类型的注解，如果该类型注解不存在，则返回null。
         * 方法2：Annotation[] getAnnotations():返回该程序元素上存在的所有注解。
         * 方法3：boolean is AnnotationPresent(Class<?extends Annotation> annotationClass):判断该程序元素上是否包含指定类型的注解，存在则返回true，否则返回false.
         * 方法4：Annotation[] getDeclaredAnnotations()：返回直接存在于此元素上的所有注释。与此接口中的其他方法不同，该方法将忽略继承的注释。（如果没有注释直接存在于此元素上，则返回长度为零的一个数组。）该方法的调用者可以随意修改返回的数组；这不会对其他调用者返回的数组产生任何影响。
         */

        public static void getInfo(Class<?> clazz) {
            String name = "";
            String gender = "";
            String profile = "";
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(Name.class)) {
                    Name annotation = field.getAnnotation(Name.class);
                    name += annotation.value();
                    Logger.d(TAG, "name= " + name);
                }
                if (field.isAnnotationPresent(Gender.class)) {
                    Gender annotation = field.getAnnotation(Gender.class);
                    gender += annotation.toString();
                    Logger.d(TAG, "gender= " + gender);
                }
                if (field.isAnnotationPresent(Profile.class)) {
                    Profile annotation = field.getAnnotation(Profile.class);
                    profile = annotation.id() + annotation.height() + annotation.nativePlace();
                    Logger.d(TAG, "profile= " + profile);
                }

            }

        }


    }


    @Name("哈哈")
    private String name;

    @Gender(gender = Gender.GenderType.Male)
    private String gender;

    @Profile(id = 001, height = 172, nativePlace = "CN")
    private String profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logger.d(TAG, getName() + getGender() + getProfile());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
