# **ScCode**

# **概述**
本文档提供了ScCode SDK的API使用指南，包括初始化SDK、检测和解码接口、解密密文、解码源码、设置日志等级以及释放资源的方法。

# **快速开始**
## **添加依赖**
在你的 build.gradle 文件中添加以下依赖项：

**在线模式：**
在根build.gradle添加repositories:
```Gradle
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```
在app build.gradle添加依赖
```Gradle
dependencies {
implementation 'com.github.UNI2KTEAM:sccode:Tag(版本号)'
}
```

**离线模式 下载安装 AAR 文件：**

```Gradle
dependencies {
    implementation(name: 'sccode-1.0.0', ext: 'aar')
}
```

# **接口API**
## **1. 初始化SDK**
```Java
public static  boolean prepareModel(Context context);
```
**功能描述:** 初始化SDK。

**参数:**

- context: 上下文 

**返回值:** 成功则返回true，失败则返回false。

## **2. 检测和解码接口**
```Java
public static native UniCodeHintType ScDetectAndDecode(byte[] nv21Data, int pic_width, int pic_height, UniCodeHintType uniCodeHintType);
```
**功能描述:** 对NV21格式图片进行检测和解码。

**参数:**

- nv21Data: 图片信息，必须是NV21格式，并且按照扫码框比例剪裁后传入（参考Sample Demo 中CameraActivity的imageAnalysis()方法）。
- pic_width: 图片宽度。
- pic_height: 图片高度。
- uniCodeHintType: 码参数。

 **返回值:** 返回一个UniCodeHintType对象。

## **3. 解密密文**
```java
public static native UniCodeHintType DecodeSecureContent(String secureContent, String secureKey);
```
**功能描述:** 解密密文内容。

**参数:**

- secureContent: 密文内容。
- secureKey: 秘钥。 

**返回值:** 返回一个UniCodeHintType对象。

## **4. 解码源码**
```Java
public static native UniCodeHintType DecodeWithOriginal(byte[] nv21Data, int pic_width, int pic_height, UniCodeHintType uniCodeHintType);
```
**功能描述:** 对NV21格式图片进行源码解码。

**参数:**

- nv21Data: NV21格式图片数据。
- pic_width: 图片宽度。
- pic_height: 图片高度。
- uniCodeHintType: 参数。 

**返回值:** 返回一个UniCodeHintType对象。

## **5. 设置日志等级**
```Java
public static native void setLogLevel(int level);
```
**功能描述:** 设置日志输出等级，默认为ERROR级别。

**参数:**

- level: 日志等级，可选值：
  - LOG_LEVEL_DEBUG (3)
  - LOG_LEVEL_INFO (4)
  - LOG_LEVEL_WARN (5)
  - LOG_LEVEL_ERROR (6)

## **6. 释放资源**
```Java
public static native void release();
```
**功能描述**: 释放SDK占用的资源。

## **示例代码**
### 初始化
```Java
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UniCodeJNI.prepareModel(this);       
    }
```
### 解码部分
```Java
 UniCodeHintType uniCodeHitType = new UniCodeHintType();
 uniCodeHitType.setEC_LEVEL("H");
 uniCodeHitType.setMARGIN(2);//最少是2 就是无边距
 uniCodeHitType.setSECURE_KEY("UNI2K");//key
 UniCodeHintType outType= UniCodeJNI.ScDetectAndDecode(imgData,img.width(),img.height(),uniCodeHitType);
 String content=outType.getCONTENT();//自定义码内容
```
 ### 释放资源
 ```Java
 @Override
    protected void onDestroy() {
        super.onDestroy();
        UniCodeJNI.release();
    }
```
## **注意事项**
- 在使用任何功能之前，请确保已成功调用 prepareModel 方法来初始化 SDK。
- ScDetectAndDecode 和 DecodeWithOriginal 方法要求输入的图片数据必须是 NV21 格式，并且已经按照扫码框比例剪裁。
- DecodeSecureContent 方法用于解码密文内容，确保提供正确的密钥以避免解码失败。
- 调用 release 方法可以有效释放资源，但在调用后不应再尝试使用 SDK 的其他功能。    