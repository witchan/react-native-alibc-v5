package com.alibcv5;

import android.app.Activity;
import android.app.Application;
import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

import androidx.annotation.NonNull;

import com.alibaba.alibclogin.AlibcLogin;
import com.alibaba.alibcprotocol.callback.AlibcLoginCallback;
import com.alibaba.alibcprotocol.callback.AlibcTradeCallback;
import com.alibaba.alibcprotocol.param.AlibcShowParams;
import com.alibaba.alibcprotocol.param.OpenType;
import com.alibaba.baichuan.trade.common.AlibcTradeCommon;
import com.baichuan.nb_trade.AlibcTrade;
import com.baichuan.nb_trade.callback.AlibcTradeInitCallback;
import com.baichuan.nb_trade.core.AlibcTradeBiz;
import com.baichuan.nb_trade.core.AlibcTradeSDK;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.randy.alibcextend.auth.AuthCallback;
import com.randy.alibcextend.auth.TopAuth;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ReactModule(name = AlibcV5Module.NAME)
public class AlibcV5Module extends ReactContextBaseJavaModule {
  public static final String NAME = "AlibcV5";

  public AlibcV5Module(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }


  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void multiply(double a, double b, Promise promise) {
    promise.resolve(a * b);
  }

  // 初始化
  @ReactMethod
  public void asyncInit(boolean isDebug, Promise promise) {

    if (isDebug) {
      AlibcTradeCommon.turnOnDebug();
      AlibcTradeCommon.openErrorLog();
      AlibcTradeBiz.turnOnDebug();
    }

    runOnUiThread( () -> {
      // 初始化扩展map（默认可传入空）
      Map<String, Object> params = new HashMap<>();
      params.put("open4GDownload", true);

      AlibcTradeSDK.asyncInit(Objects.requireNonNull(getCurrentActivity()).getApplication(), params, new AlibcTradeInitCallback() {
        @Override
        public void onSuccess() {
          WritableMap resp = Arguments.createMap();
          resp.putInt("code", 1);
          resp.putString("msg", "百川初始化成功");

          promise.resolve(resp);
        }
        @Override
        public void onFailure(int code, String msg) {
          WritableMap resp = Arguments.createMap();
          resp.putInt("code", code);
          resp.putString("msg", msg);
          promise.resolve(resp);
        }
      });
    });
  }

  // 登录
  @ReactMethod
  public void login( Promise promise) {
    if (!AlibcLogin.getInstance().isLogin()) {
      runOnUiThread( () -> {
        AlibcLogin.getInstance().showLogin(new AlibcLoginCallback() {
          @Override
          public void onSuccess(String s, String s1) {
            WritableMap data = Arguments.createMap();
            Map<String, Object> userInfo = AlibcLogin.getInstance().getUserInfo();
            if (userInfo != null) {
              data.putString("nick", (String) userInfo.get("nick"));
              data.putString("openId", (String) userInfo.get("openId"));
              data.putString("topAccessToken", (String) userInfo.get("topAccessToken"));
              data.putString("topAuthCode", (String) userInfo.get("topAuthCode"));
            }
            WritableMap resp = Arguments.createMap();
            resp.putInt("code", 1);
            resp.putString("msg", "登录成功");
            resp.putMap("data", data);
            promise.resolve(resp);
          }

          @Override
          public void onFailure(int code, String msg) {
            WritableMap data = Arguments.createMap();
            data.putInt("err_code", code);
            data.putString("error_msg", msg);
            WritableMap resp = Arguments.createMap();
            resp.putInt("code", 0);
            resp.putString("msg", "登录失败");
            resp.putMap("data", data);
            promise.resolve(resp);
          }
        });
      });
    } else {
      WritableMap data = Arguments.createMap();
      Map<String, Object> userInfo = AlibcLogin.getInstance().getUserInfo();
      if (userInfo != null) {
        data.putString("nick", (String) userInfo.get("nick"));
        data.putString("openId", (String) userInfo.get("userId"));
        data.putString("topAccessToken", (String) userInfo.get("topAccessToken"));
        data.putString("topAuthCode", (String) userInfo.get("topAuthCode"));
      }
      WritableMap resp = Arguments.createMap();
      resp.putInt("code", 1);
      resp.putString("msg", "登录成功");
      resp.putMap("data", data);
      promise.resolve(resp);
    }
  }

  // 是否登录
  @ReactMethod
  public void isLogin(Promise promise) {
    promise.resolve(AlibcLogin.getInstance().isLogin()?1:0);
  }

  @ReactMethod
  public void logout( Promise promise) {
    AlibcLogin.getInstance().logout(new AlibcLoginCallback() {
      @Override
      public void onSuccess(String openId, String nick) {
        WritableMap resp = Arguments.createMap();
        resp.putInt("code", 1);
        resp.putString("msg", "退出成功");
        promise.resolve(resp);
      }
      @Override
      public void onFailure(int code, String msg) {
        WritableMap resp = Arguments.createMap();
        resp.putInt("code", 0);
        resp.putString("msg", msg);
        promise.resolve(resp);
      }
    });
  }

  // 授权
  @ReactMethod
  public void authorize4AppKey(String appKey, String appName, String iOSAppLogo, int androidAppLogo, Promise promise) {

    if (androidAppLogo < 1) {
      WritableMap resp = Arguments.createMap();
      resp.putInt("code", 0);
      resp.putString("msg", "appLogo不存在");
      promise.resolve(resp);
      return;
    }

    runOnUiThread( () ->{
      TopAuth.showAuthDialog(getCurrentActivity(), androidAppLogo, appName, appKey, new AuthCallback() {
        @Override
        public void onSuccess(String accessToken, String expireTime) {
          WritableMap data = Arguments.createMap();
          data.putString("accessToken", accessToken);
          data.putString("expire", expireTime);
          WritableMap resp = Arguments.createMap();
          resp.putInt("code", 1);
          resp.putString("msg", "授权成功");
          resp.putMap("data", data);
          promise.resolve(resp);
        }

        @Override
        public void onError(String errorCode, String errorMsg) {
          WritableMap data = Arguments.createMap();
          int code = Integer.parseInt(errorCode);
          data.putInt("err_code", code);
          data.putString("err_msg", errorMsg);
          WritableMap resp = Arguments.createMap();
          resp.putInt("code", 0);
          resp.putString("msg", "授权失败");
          resp.putMap("data", data);
          promise.resolve(resp);
        }
      });
    });
  }

  // 详情
  @ReactMethod
  public void openTradeUrl(String url, Promise promise) {
    runOnUiThread(() -> {
      AlibcShowParams showParams = new AlibcShowParams();
      showParams.setOpenType(OpenType.Native);
      AlibcTrade.openByUrl(getCurrentActivity(), url, showParams, null, null, new AlibcTradeCallback() {
        @Override
        public void onSuccess(int code, Object o) {
          WritableMap data = Arguments.createMap();
          data.putInt("code", code);
          WritableMap resp = Arguments.createMap();
          resp.putInt("code", 1);
          resp.putString("msg", "打开详情成功");
          resp.putMap("data", data);
          promise.resolve(resp);
        }

        @Override
        public void onFailure(int code, String msg) {
          WritableMap data = Arguments.createMap();
          data.putInt("err_code", code);
          data.putString("err_msg", msg);
          WritableMap resp = Arguments.createMap();
          resp.putInt("code", 0);
          resp.putString("msg", "打开详情失败");
          resp.putMap("data", data);
          promise.resolve(resp);
        }
      });
    });
  }

  // 详情
  @ReactMethod
  public void openTradePageByCode(String url, Promise promise) {
    runOnUiThread( () -> {
      // show参数
      AlibcShowParams showParams = new AlibcShowParams();
      showParams.setOpenType(OpenType.Native);
      AlibcTrade.openByCode(getCurrentActivity(), url, null,showParams, null, null, new AlibcTradeCallback() {
        @Override
        public void onSuccess(int code, Object o) {
          WritableMap data = Arguments.createMap();
          data.putInt("code", code);
          WritableMap resp = Arguments.createMap();
          resp.putInt("code", 1);
          resp.putString("msg", "打开详情成功");
          resp.putMap("data", data);
          promise.resolve(resp);
        }

        @Override
        public void onFailure(int code, String msg) {
          WritableMap data = Arguments.createMap();
          data.putInt("err_code", code);
          data.putString("err_msg", msg);
          WritableMap resp = Arguments.createMap();
          resp.putInt("code", 0);
          resp.putString("msg", "打开详情失败");
          resp.putMap("data", data);
          promise.resolve(resp);
        }
      });
    });
  }
}
