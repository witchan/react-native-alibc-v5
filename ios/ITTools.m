//
//  ITTools.m
//  react-native-alibc-v5
//
//  Created by witchan on 2024/6/17.
//

#import "ITTools.h"

@implementation ITTools


+ (UIWindow *)keyWindow
{
    if (@available(iOS 13.0, *))
    {
        for (UIWindowScene* windowScene in [UIApplication sharedApplication].connectedScenes) {
            if (windowScene.activationState == UISceneActivationStateForegroundActive)
            {
                for (UIWindow *window in windowScene.windows)
                {
                    if (window.isKeyWindow)
                    {
                        return window;
                    }
                }
            }
        }
    }
    else
    {
        return [UIApplication sharedApplication].keyWindow;
    }
    return nil;
}

+ (UIViewController *)currentViewController {
  
    return [self keyWindow].rootViewController;
}

@end
