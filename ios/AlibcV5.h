
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNAlibcV5Spec.h"

@interface AlibcV5 : NSObject <NativeAlibcV5Spec>
#else
#import <React/RCTBridgeModule.h>

@interface AlibcV5 : NSObject <RCTBridgeModule>
#endif

@end
