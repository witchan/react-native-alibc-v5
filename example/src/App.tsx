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
    var resp = await AlibcV5.authorize4AppKey("24833809", "轻掌柜", "trv_video_centerPause", 2131492867)
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
