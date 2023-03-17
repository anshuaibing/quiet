#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_myapplication_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jfloat JNICALL
Java_com_example_myapplication_JNIActivity_add(JNIEnv *env, jclass clazz, jfloat a, jfloat b) {
    // TODO: implement add()
    return jfloat(a+b);

}
extern "C"
JNIEXPORT jfloat JNICALL
Java_com_example_myapplication_JNIActivity_sub(JNIEnv *env, jclass clazz, jfloat a, jfloat b) {
    // TODO: implement sub()
    return jfloat(a-b);
}
extern "C"
JNIEXPORT jfloat JNICALL
Java_com_example_myapplication_JNIActivity_mul(JNIEnv *env, jclass clazz, jfloat a, jfloat b) {
    // TODO: implement mul()
    return jfloat(a*b);
}
extern "C"
JNIEXPORT jfloat JNICALL
Java_com_example_myapplication_JNIActivity_div(JNIEnv *env, jclass clazz, jfloat a, jfloat b) {
    // TODO: implement div()
    return jfloat(a/b);
}