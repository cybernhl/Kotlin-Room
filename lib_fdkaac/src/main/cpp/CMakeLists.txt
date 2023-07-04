cmake_minimum_required(VERSION 3.4.1)

#配置动态链接库对应头文件的目录
include_directories(${PROJECT_SOURCE_DIR}/include)

# 添加我们自己的JNI文件编辑出来的库
add_library(
        fdkcodec
        SHARED
        fdkcodec.cpp)


# 之前我们以简单的方式集成第三方C库 set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}../../../../libs/${ANDROID_ABI}")
# 现在我们用复杂的方式集成第三方库 add_library + set_target_properties 套餐
add_library(fdk-aac
        SHARED
        IMPORTED)
set_target_properties(
        fdk-aac
        PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/../jniLibs/armeabi-v7a/libfdk-aac.so)


find_library(
        log-lib
        log)


target_link_libraries(
        fdkcodec

        fdk-aac
        ${log-lib})