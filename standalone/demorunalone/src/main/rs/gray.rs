#pragma version(1)
#pragma rs java_package_name(com.example.myapplication)

uchar4 RS_KERNEL gray(uchar4 in) {
    int avg = (int)(0.299f * in.r + 0.587f * in.g + 0.114f * in.b);
    return (uchar4){avg, avg, avg, in.a};
}
