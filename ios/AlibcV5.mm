#import "AlibcV5.h"
#import "ITTools.h"
#import <AlibcTradeUltimateSDK/AlibcTradeUltimateSDK.h>
#import <React/RCTLog.h>

@implementation AlibcV5
RCT_EXPORT_MODULE()

// Example method
// See // https://reactnative.dev/docs/native-modules-ios


/// 初始化SDK
RCT_EXPORT_METHOD(asyncInit:(BOOL)isDebug
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)
{
    [[AlibcTradeUltimateSDK sharedInstance] asyncInitWithSuccess:^{
            RCTLogInfo(@"百川初始化成功");
            NSDictionary *resp = @{@"code": @1, @"msg": @"百川初始化成功"};
            if (isDebug) {
                [[AlibcTradeUltimateSDK sharedInstance] enableAutoShowDebug:YES];
            }
            resolve(resp);
        } failure:^(NSError *error) {
            NSDictionary *resp = @{@"code": @0, @"msg": @"百川初始化失败", @"data": error.userInfo};
            resolve(resp);
        }];
}

// 登录
RCT_EXPORT_METHOD(login:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject) {
    dispatch_async(dispatch_get_main_queue(), ^{
        UIViewController *vc = [ITTools currentViewController];
        
        id<AlibcTradeUltimateLoginService> loginService = [AlibcTradeUltimateSDK sharedInstance].loginService;
        
        if (![loginService isLogin]) {
            [loginService setH5Only:NO];
            [loginService auth:vc success:^(AlibcUser *user) {
                NSMutableDictionary *userInfo = [NSMutableDictionary dictionary];
                userInfo[@"nick"] = user.nick;
                userInfo[@"avatarUrl"] = user.avatarUrl;
                userInfo[@"openId"] = user.openId;
                userInfo[@"openSid"] = user.openSid;
                userInfo[@"topAccessToken"] = user.topAccessToken;
                userInfo[@"topAuthCode"] = user.topAuthCode;
                
                NSDictionary *resp = @{@"code": @1, @"msg": @"登录成功", @"data": userInfo};
                resolve(resp);
            } failure:^(NSError *error) {
                NSDictionary *resp = @{@"code": @0, @"msg": @"登录失败", @"data": error.userInfo};
                resolve(resp);
            }];
        } else {
            AlibcUser *user = [loginService getUser];
            NSMutableDictionary *userInfo = [NSMutableDictionary dictionary];
            userInfo[@"nick"] = user.nick;
            userInfo[@"avatarUrl"] = user.avatarUrl;
            userInfo[@"openId"] = user.openId;
            userInfo[@"openSid"] = user.openSid;
            userInfo[@"topAccessToken"] = user.topAccessToken;
            userInfo[@"topAuthCode"] = user.topAuthCode;
            
            NSDictionary *resp = @{@"code": @1, @"msg": @"登录成功", @"data": userInfo};
            resolve(resp);
        }
    });
}


// 退出
RCT_EXPORT_METHOD(logout:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject) {
    
    [[AlibcTradeUltimateSDK sharedInstance].loginService logout];
    NSDictionary *resp = @{@"code": @1, @"msg": @"退出成功"};
    resolve(resp);
}

/// 授权
RCT_EXPORT_METHOD(authorize4AppKey:(NSString *)appKey
                  appName: (NSString *)appName
                  iOSAppLogo: (NSString *)iOSAppLogo
                  androidAppLogo: (int)androidAppLogo
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)
{
    dispatch_async(dispatch_get_main_queue(), ^{
        UIImage *logo = [UIImage imageNamed:iOSAppLogo];
        if (logo == nil) {
            NSDictionary *resp = @{@"code": @0, @"msg": @"appLogo不存在"};
            resolve(resp);
            return;
        }
        
        
        
        UIViewController *vc = [ITTools currentViewController];
        
        [[AlibcTradeUltimateSDK sharedInstance].tradeService authorize4AppKey:appKey appName:appName appLogo:logo currentVC:vc callBack:^(NSError *error, NSString *accessToken, NSString *expire) {
            if (error != nil) {
                NSDictionary *resp = @{@"code": @0, @"msg": @"授权失败", @"data": error.userInfo};
                resolve(resp);
                return;
            }
            
            NSMutableDictionary *data = [NSMutableDictionary dictionary];
            data[@"accessToken"] = accessToken;
            data[@"expire"] = expire;

            NSDictionary *resp = @{@"code": @1, @"msg": @"授权成功", @"data": data};
            resolve(resp);
        }];
    });
}

/// 详情
RCT_EXPORT_METHOD(openTradeUrl:(NSString *)url
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)
{
    dispatch_async(dispatch_get_main_queue(), ^{
        UIViewController *vc = [ITTools currentViewController];
        
        [[AlibcTradeUltimateSDK sharedInstance].tradeService openTradeUrl:url parentController:vc showParams:nil taoKeParams:nil trackParam:nil openUrlCallBack:^(NSError *error, NSDictionary *result) {
            if (error != nil) {
                NSDictionary *resp = @{@"code": @0, @"msg": @"打开详情失败", @"data": error.userInfo};
                resolve(resp);
                return;
            }
            
            NSDictionary *resp = @{@"code": @1, @"msg": @"打开详情成功", @"data": result ? :@{}};
            resolve(resp);
        }];
    });
}

/// 详情
RCT_EXPORT_METHOD(openTradePageByCode:(NSString *)code
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)
{
    dispatch_async(dispatch_get_main_queue(), ^{
        UIViewController *vc = [ITTools currentViewController];

        [[AlibcTradeUltimateSDK sharedInstance].tradeService openTradePageByCode:code parentController:vc urlParams:nil showParams:nil taoKeParams:nil trackParam:nil openUrlCallBack:^(NSError *error, NSDictionary *result) {
            if (error != nil) {
                NSDictionary *resp = @{@"code": @0, @"msg": @"打开详情失败", @"data": error.userInfo};
                resolve(resp);
                return;
            }
                  
            NSDictionary *resp = @{@"code": @1, @"msg": @"打开详情成功", @"data": result ? :@{}};
            resolve(resp);
        }];
    });
}

@end
