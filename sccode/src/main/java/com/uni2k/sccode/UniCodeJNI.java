package com.uni2k.sccode;


import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * created by：lynn.shao on 2024/6/24.
 */
public class UniCodeJNI {

    static {
        System.loadLibrary("Uni2kCode");
    }

    /**
     * 将模型路径传入SDK
     * @param context
     */
    public static boolean prepareModel(Context context) {
        File mMNNFile = new File(context.getCacheDir(), "unicode.mnn");
        try {
            copyAssetGetFilePath(context.getAssets().open("unicode.mnn"), mMNNFile);
          return   UniCodeJNI.initMode(mMNNFile.getAbsolutePath());
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 初始化 sdk
     * @param modelPath 模型路径
     * @return
     */
    private static  native boolean  initMode(String modelPath);

    /**
     * 检测和解码接口
     * @param nv21Data 图片信息，必须是nv21格式，且必须按照扫码框比例剪裁后传入 （参考CameraActivity的imageAnalysis()方法）
     * @param pic_with 图片宽度
     * @param pic_height 图片高度
     * @param uniCodeHintType 码参数
     * @return
     */
    public static native UniCodeHintType ScDetectAndDecode(byte[] nv21Data, int pic_with, int pic_height, UniCodeHintType uniCodeHintType);

    /**
     * 解码密文
     * @param secureContent 密文内容
     * @param secureKey 秘钥
     * @return
     */
    public static native UniCodeHintType DecodeSecureContent(String secureContent, String secureKey);

    /**
     * 解码源码
     * @param nv21Data nv21格式图片
     * @param pic_with 图片宽
     * @param pic_height 图片高
     * @param uniCodeHintType 参数
     * @return
     */
    public static native UniCodeHintType DecodeWithOriginal(byte[] nv21Data, int pic_with, int pic_height, UniCodeHintType uniCodeHintType);

    /**
     * 设置log等级 默认输出ERROR等级
     *
     *  LOG_LEVEL_DEBUG 3
     *  LOG_LEVEL_INFO 4
     *  LOG_LEVEL_WARN 5
     *  LOG_LEVEL_ERROR 6
     *
     * @param level
     */
    public static native void setLogLevel(int level);

    /**
     * 释放
     */
    public static  native void   release();


    private static File copyAssetGetFilePath(InputStream is, File outFile) {
        try {

            if (!outFile.exists()) {
                boolean res = outFile.createNewFile();
                if (!res) {
                    return null;
                }
            } else {
                if (outFile.length() > 10) {//表示已经写入一次
                    return outFile;
                }
            }

            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
            return outFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
