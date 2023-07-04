cmake_minimum_required(VERSION 3.4.1)


#配置动态链接库所在的目录
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}../../../../libs/${ANDROID_ABI}")


#配置动态链接库对应头文件的目录
include_directories(${PROJECT_SOURCE_DIR}/openh264/include)


add_library(
        openh264util
        SHARED

        openh264util.cpp
        VideoEncoder.cpp
        utils/logger.h
        utils/timer.c)


find_library(
        log-lib
        log)


target_link_libraries(
        openh264util
        openh264

        ${log-lib}
        jnigraphics
        android)