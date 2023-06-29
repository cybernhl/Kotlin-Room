cmake_minimum_required(VERSION 3.4.1)


#project("lib_openh264")


#配置动态链接库所在的目录
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}../../../../libs/${ANDROID_ABI}")


#set(openh264_lib_DIR ${CMAKE_SOURCE_DIR}/../jniLibs/${ANDROID_ABI})
#set(openh264_head_dir ${CMAKE_SOURCE_DIR}/openh264)


include_directories(${PROJECT_SOURCE_DIR}/openh264/include)


#add_library(
#        libopenh264
#        SHARED
#        IMPORTED)
#
#set_target_properties(
#        libopenh264
#        PROPERTIES
#        IMPORTED_LOCATION
#        ${openh264_lib_DIR}/libopenh264.so)



add_library(
        openh264util
        SHARED

        openh264util.cpp
        VideoEncoder.cpp
        utils/logger.h
        utils/timer.c)

target_include_directories(
        openh264util
        PRIVATE

        ${PROJECT_SOURCE_DIR}/openh264/include)


find_library(
        log-lib
        log)

target_link_libraries(
        openh264util
        openh264

        ${log-lib}
        jnigraphics
        android)