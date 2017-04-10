package com.aiunit.cmbpay;

import android.content.Intent;
import android.content.Context;

import android.util.Log;
import android.webkit.WebView;
import android.telephony.TelephonyManager;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CMBPayPlugin extends CordovaPlugin {

//    public static final String TAG = "TffConfig";
    private CordovaWebView webView;
//    private String channel = "";   //app发布渠道
    private CallbackContext callbackContext;
    
    /**
     * Sets the context of the Command. This can then be used to do things like
     * get file paths associated with the Activity.
     *
     * @param cordova The context of the main Activity.
     * @param webView The CordovaWebView Cordova is running in.
     */
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        this.webView = webView;

//        Context context = this.cordova.getActivity().getApplicationContext();
//        tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action            The action to execute.
     * @param args              JSONArry of arguments for the plugin.
     * @param callbackContext   The callback id used when calling back into JavaScript.
     * @return                  True if the action was valid, false if not.
     */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {
            if ("pay".equals(action)) {
                // jsapi 参数传入的 payString，参照一网通支付 API http://121.15.180.72/OpenAPI2/API/PWDPayAPI4.aspx#jvch4
                // jsapi 应传入 { url: xxxx, jsonRequestData: xxxx }
                JSONObject json =  args.getJSONObject(0);
                String url = json.getString("url");
                String jsonRequestData = json.getString("jsonRequestData");
                // 得到callbackContext对象
                this.callbackContext = callbackContext;
                // 打开支付 Actiivity
                Intent intent = new Intent(this.cordova.getActivity(), CMBPayActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("jsonRequestData",jsonRequestData);
                // 加入将要传输到activity中的参数
                // 启动activity
                this.cordova.startActivityForResult(this, intent, 0);
                // callbackContext.success("pay return");
                return true;
            }
            callbackContext.error("Invalid Action");
            return false;
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            callbackContext.error(e.getMessage());
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        String statusCode;
        switch (requestCode) {
            case  0:
                if(resultCode == 1) {
                    statusCode = "success";
                    callbackContext.success(statusCode);
                }
                break;
            default:
                break;
        }
    }

}
