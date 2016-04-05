package cn.sharesdk.demo.tpl;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class MainActivity extends AppCompatActivity {

    public final  String WEIXIN_APP_ID = "wxc43c2c004120810f";
    public final  String WEIXIN_APP_SERCET = "wxc43c2c004120810f";
    public final  String WEIXIN_SCOPE = "snsapi_userinfo";
    public final  String WEIXIN_STATE = "wechat_sdk_demo_test";


    Handler handler = new Handler();
    Runnable runnable;

    Button button ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loginWithWeixin();
                handler.post(runnable);
            }
        });
        runnable = new Runnable() {
            int t = 5;
            @Override
            public void run() {
                if(t-- > 0){
                    handler.postDelayed(this, 1000);
                    Log.i("TT", "run: ruu");
                    button.setEnabled(false);
                }else {
                    Log.i("TT", "run: ttt");
                    button.setEnabled(true);
                    t = 5;
                }
            }
        };

    }


    private IWXAPI mWeixinAPI;
    private void loginWithWeixin() {
        if (mWeixinAPI == null) {
            mWeixinAPI = WXAPIFactory.createWXAPI(this, WEIXIN_APP_ID, false);
        }

        if (!mWeixinAPI.isWXAppInstalled()) {
            Toast.makeText(this, "mmmmm", Toast.LENGTH_LONG).show();
            return;
        }

        mWeixinAPI.registerApp(WEIXIN_APP_ID);

        // send oauth request
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        mWeixinAPI.sendReq(req);
    }


}
