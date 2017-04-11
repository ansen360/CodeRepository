LOCAL_PATH := $(call my-dir)  #提定当前路径

include $(CLEAR_VARS)         #清除全局配置变量，LOCAL_XXX,除了LOCAL_PATH

LOCAL_MODULE    := hello       #指定生成动态库名hello，生成的动态库文件libhello.so
LOCAL_SRC_FILES := hello.c     #指定生成动态库的源文件

include $(BUILD_SHARED_LIBRARY) #提定生成动态库