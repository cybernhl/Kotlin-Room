cmake_minimum_required(VERSION 3.4.1)


#配置动态链接库所在的目录
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}../../../../libs/${ANDROID_ABI}")

include_directories(${PROJECT_SOURCE_DIR}/include)

# 添加easyyuv.cpp源文件，创建一个yuvutil的静态库
add_library(
        yuvutil
        SHARED
        yuvutils.cpp)

# 将查找名为 log 的库文件，并将其路径保存到变量 log-lib 中
find_library(
        log-lib
        log)

target_link_libraries(
        yuvutil
        yuv

        ${log-lib}
        jnigraphics
        android)