#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_example_youverifyassessment_utils_AppParameters_getBaseUrl(JNIEnv *env, jobject thiz) {
    return (*env)->NewStringUTF(env, "https://api.escuelajs.co/");
}

JNIEXPORT jstring JNICALL
Java_com_example_youverifyassessment_utils_AppParameters_getEncryptionAlgorithmKeyType(JNIEnv *env,
                                                                                       jobject thiz) {
    return (*env)->NewStringUTF(env, "AES");
}

JNIEXPORT jstring JNICALL
Java_com_example_youverifyassessment_utils_AppParameters_encryptionKey(JNIEnv *env, jobject thiz) {
    return (*env)->NewStringUTF(env, "ieu873hekejs958i");
}

JNIEXPORT jstring JNICALL
Java_com_example_youverifyassessment_utils_AppParameters_encryptionPadding(JNIEnv *env,
                                                                           jobject thiz) {
    return (*env)->NewStringUTF(env, "AES/CBC/PKCS5PADDING");
}

JNIEXPORT jstring JNICALL
Java_com_example_youverifyassessment_utils_AppParameters_getIv(JNIEnv *env, jobject thiz) {
    return (*env)->NewStringUTF(env, "873hdfwyb4+pi_7h");
}

JNIEXPORT jstring JNICALL
Java_com_example_youverifyassessment_utils_AppParameters_getGoogleKey(JNIEnv *env, jobject thiz) {
    return (*env)->NewStringUTF(env, "780213070535-33dosqkjqgugvslotn87d4217ipjiu4n.apps.googleusercontent.com");
}