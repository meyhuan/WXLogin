package cn.sharesdk.demo.tpl.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;


import org.json.JSONObject;

import cn.sharesdk.demo.tpl.R;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/4/5.
 *
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    public final String TAG = "WXEntryActivity";
    public final String WEIXIN_APP_ID = "wxc43c2c004120810f";
    public final String WEIXIN_APP_SECRET = "96306e148dc901d44fedb897566f7a6d";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_layout);

        textView = (TextView) findViewById(R.id.text_view);

        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        SendAuth.Resp resp = new SendAuth.Resp(intent.getExtras());
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            //用户同意
            authToken(resp);
        }
        textView.setText(resp.errCode + ", " + resp.country);
    }

    private void authToken(final BaseResp resp) {
        Log.e("tag", "---ErrCode:" + resp.errCode);
        Toast.makeText(this, "code = " + ((SendAuth.Resp) resp).code,
                Toast.LENGTH_SHORT).show();
        final String code = ((SendAuth.Resp) resp).code;
        String tokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + WEIXIN_APP_ID + "&secret="
                + WEIXIN_APP_SECRET + "&code=" + code
                + "&grant_type=authorization_code";
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(this, tokenUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("Tag",response.toString());
                String accessToken = "";
                String openId = "";
                try {
                    accessToken = response.getString("access_token");
                    openId = response.getString("openid");
                    Log.i("Tag",accessToken +":"+openId);
                    textView.setText(accessToken +":"+openId);
                    getUserInfo(accessToken, openId);
                } catch (Exception e) {

                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void getUserInfo(String accessToken, String openId){
        String infoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token="
                                    + accessToken + "&openid=" + openId;
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(this, infoUrl, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                textView.setText(response.toString());
                Log.d(TAG, "onSuccess: "+response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e("tag", "---ErrCode:" + resp.errCode);
        Toast.makeText(this, "code = " + ((SendAuth.Resp) resp).code,
                Toast.LENGTH_SHORT).show();
        final String code = ((SendAuth.Resp) resp).code;
    }

}
