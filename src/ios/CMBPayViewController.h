#import <UIKit/UIKit.h>
#define PAY_STATUS @"PayStatusChangeNotification"

@interface CMBPayViewController : UIViewController
- (void)loadUrl:(NSString*)outerURL setParam:(NSString*)loadParam;
- (void)loadURLRequest:(NSURLRequest*)requesturl;
@end
