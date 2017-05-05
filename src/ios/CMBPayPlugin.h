#import <Cordova/CDV.h>

@interface CMBPayPlugin : CDVPlugin

- (void) pay:(CDVInvokedUrlCommand*)command;

@property(nonatomic,strong)NSString *currentCallbackId;

@end
