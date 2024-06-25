# react-native-alibc-v5

阿里百川react-native模块

## SDK下载&版本更新记录

| 平台           | SDK版本                                                      |
| -------------- | ------------------------------------------------------------ |
| Android 旗舰版 | [SDK下载（5.0.2.1）](https://baichuan-sdk-bucket.oss-cn-hangzhou.aliyuncs.com/android/旗舰版_5021.zip?spm=a3c0d.7629140.0.0.5f9ebe48m8oZvf&file=旗舰版_5021.zip) |
| iOS 旗舰版     | [SDK下载（5.0.0.17）](https://baichuan-sdk-bucket.oss-cn-hangzhou.aliyuncs.com/ios/AlibcTradeUltimateSDK_all_package_50017.zip?spm=a3c0d.7629140.0.0.5f9ebe48m8oZvf&file=AlibcTradeUltimateSDK_all_package_50017.zip) |



## 集成流程

### 一、模块集成

1、下载react-native-alibc-v5模块[源码](https://github.com/witchan/react-native-alibc-v5)，解压到你工程的modules(如没请手动创建该文件夹)文件夹中

![image-20240624113153863](https://nas.witchan.com:5802/index.php/s/zTf34MJD5ppQiNe/preview)

2、修改package.json文件

![image-20240624153030617](https://nas.witchan.com:5802/index.php/s/Le4CWxNFggfFeer/preview)



## 二、iOS项目配置

1、[下载](https://baichuan-sdk-bucket.oss-cn-hangzhou.aliyuncs.com/ios/AlibcTradeUltimateSDK_all_package_50017.zip?spm=a3c0d.7629140.0.0.6acabe48JI0wFe&file=AlibcTradeUltimateSDK_all_package_50017.zip)百川原生iOS端SDK并解压到如下位置：

![image-20240624114342819](https://nas.witchan.com:5802/index.php/s/6DqgL5CenzP8Pbc/preview)

2、复制你的安全图片并粘贴到如下位置：

![image-20240624140147049](https://nas.witchan.com:5802/index.php/s/iQeAPesprJKesWk/preview)

3、切换到项目的根目录，并分别执行以下命令：

```bash
rm -rf /Users/witchan/Downloads/AwesomeProject/modules/react-native-alibc-v5/ios/framework/MunionBcAdSDK/MunionBcAdSDK.framework
yarn install
cd ios
pod install
```

提示：如报```target has frameworks with conflicting names: munionbcadsdk.framework```错误，请删除后再重新执行pod install，执行成功后，你的项目的ios目录会有一个xxx.xcworkspace文件。

![image-20240624140921541](https://nas.witchan.com:5802/index.php/s/7bSRei9XPE5gkN9/preview)

4、修复签名错误，双击你项目里iOS目录里的xxx.xcworkspace文件会自动启动并打开iOS项目，修改如下错误：

![image-20240624140834148](https://nas.witchan.com:5802/index.php/s/X2mZ99wwGAjBGeg/preview)

5、配置Info.plist

![image-20240624183207272](https://nas.witchan.com:5802/index.php/s/EAGWsRF8sXqwmto/preview)

6、最后AppDelegate.m文件里增加如下代码：

```
#import "AppDelegate.h"

#import <React/RCTBundleURLProvider.h>
#import <AlibcTradeUltimateSDK/AlibcTradeUltimateSDK.h>

@implementation AppDelegate

- (BOOL)application:(UIApplication *)app openURL:(NSURL *)url options:(NSDictionary<UIApplicationOpenURLOptionsKey,id> *)options {
  return [[AlibcTradeUltimateSDK sharedInstance] application:app openURL:url options:options];
}

@end

```



![image-20240624184350361](https://nas.witchan.com:5802/index.php/s/iMDqKHz5RNgZYpE/preview)



至此，iOS端集成完毕。



## Android项目配置

1、[下载](https://baichuan-sdk-bucket.oss-cn-hangzhou.aliyuncs.com/android/%E6%97%97%E8%88%B0%E7%89%88_5021.zip?spm=a3c0d.7629140.0.0.5f9ebe48m8oZvf&file=%E6%97%97%E8%88%B0%E7%89%88_5021.zip)百川原生Android端SDK并解压到如下位置：

![image-20240624114342819](https://nas.witchan.com:5802/index.php/s/6DqgL5CenzP8Pbc/preview)

2、复制你的安全图片并粘贴到如下位置：

![image-20240624140147049](https://nas.witchan.com:5802/index.php/s/iQeAPesprJKesWk/preview)



## 使用示例

```
import * as React from 'react';

import { StyleSheet, View, Text, Button } from 'react-native';
import { AlibcV5 } from 'react-native-alibc-v5';

export default function App() {

  const asyncInit = async () => {
    var resp = await AlibcV5.asyncInit(true)
    console.log(resp)
  }

  const login = async () => {
    var resp = await AlibcV5.login()
    console.log(resp)
  }

  const logout = async () => {
    var resp = await AlibcV5.logout()
    console.log(resp)
  }
  
  const authorize4AppKey = async () => {
    var resp = await AlibcV5.authorize4AppKey("你的AppKey", "name", "ios图标", android图标的资源id)
    console.log(resp)
  }

  const openTradeUrl = async () => {
    var resp = await AlibcV5.openTradeUrl("https://detail.tmall.com/item.htm?id=641232261335&scm=1007.40986.275655.0&pvid=b1bb724a-24e9-4940-b8cd-c05aaa8e1fb9")
    console.log(resp)
  }

  return (
    <View style={styles.container}>
      <Button
        onPress={asyncInit}
        title="初始化"/>
      <Button
        onPress={login}
        title="登录"/>
      <Button
        onPress={logout}
        title="退出登录"/>
      <Button
        onPress={authorize4AppKey}
        title="淘宝授权"/>
      <Button
        onPress={openTradeUrl}
        title="淘宝详情"/>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor:"white",
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});

```

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
