package tedu.sheng.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.open.utils.Util;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import tedu.sheng.R;
import tedu.sheng.util.Consts;
import tedu.sheng.util.TencentUtil;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener ,Consts{

    // 取消登录
    private TextView tvLoginCancel;
    // 忘记密码
    private TextView tvForgetPwd;
    // 新用户
    private TextView tvNewUser;
    // 用户名输入框
    private EditText etUserName;
    // 密码输入框
    private EditText etUserPwd;
    // 清空用户名
    private ImageView imgClearUserName;
    // 清空密码
    private ImageView imgClearUserPwd;
    // 登录按钮
    private Button btnLogin;

    private TextView tvLoginByQQ;

    private Tencent mTencent;

    //获取的开放接口id
    private String openidString;
    //QQ头像
    private Bitmap bitmap;

//qq昵称
    private String nicknameString;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {


        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化控件
        initView();

        // 设置监听器
        setListener();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        tvLoginCancel = (TextView) findViewById(R.id.tv_login_cancel);
        tvForgetPwd = (TextView) findViewById(R.id.tv_login_forget_pwd);
        tvNewUser = (TextView) findViewById(R.id.tv_login_new_user);
        etUserName = (EditText) findViewById(R.id.et_login_username);
        etUserPwd = (EditText) findViewById(R.id.et_login_userpwd);
        imgClearUserName = (ImageView) findViewById(R.id.img_login_delete_username);
        imgClearUserPwd = (ImageView) findViewById(R.id.img_login_delete_userpwd);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvLoginByQQ= (TextView) findViewById(R.id.tv_login_byqq);
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        // 取消登录监听
        tvLoginCancel.setOnClickListener(this);
        // 新用户监听
        tvNewUser.setOnClickListener(this);
        // 忘记密码监听
        tvForgetPwd.setOnClickListener(this);
        // 删除用户名监听
        imgClearUserName.setOnClickListener(this);
        // 删除密码监听
        imgClearUserPwd.setOnClickListener(this);
        // 登录
        btnLogin.setOnClickListener(this);
        // 用户名输入监听
        etUserName.addTextChangedListener(NameTextChangeListener);
        // 密码输入监听
        etUserPwd.addTextChangedListener(PwdTextChangeListener);

        tvLoginByQQ.setOnClickListener(this);
    }

    /**
     * 用户名输入监听器
     */
    private TextWatcher NameTextChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // 如果文本长度为0,则设置清空用户名控件不可见;反之,设置可见
            if (s.length() == 0) {
                imgClearUserName.setVisibility(View.INVISIBLE);
            } else {
                imgClearUserName.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 密码输入监听器
     */
    private TextWatcher PwdTextChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // 如果文本长度为0,则设置清空密码控件不可见;反之,设置可见
            if (s.length() == 0) {
                imgClearUserPwd.setVisibility(View.INVISIBLE);
            } else {
                imgClearUserPwd.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 取消登录点击事件监听
            case R.id.tv_login_cancel:
                finish();
                break;
            // 忘记密码点击事件监听
            case R.id.tv_login_forget_pwd:
                // 调用找回密码界面静态方法启动界面
                ForgetPwdActivity.startForgetPwdActivity(LoginActivity.this);
                break;
            // 注册新用户点击事件监听
            case R.id.tv_login_new_user:
                // 调用注册界面的静态方法跳转
                RegisterActivity.startRegisterActivity(LoginActivity.this);
                break;
            // 清空用户名事件监听
            case R.id.img_login_delete_username:
                // 清空用户名
                etUserName.setText("");
                break;
            // 清空密码事件监听
            case R.id.img_login_delete_userpwd:
                // 清空密码输入框
                etUserPwd.setText("");
                break;
            // 登录界面
            case R.id.btn_login:
                Toast.makeText(LoginActivity.this, "登录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_login_byqq:
                mTencent = Tencent.createInstance("1105481832", this.getApplicationContext());
                mTencent.login(LoginActivity
                        .this,"all",new BaseUiListener());
        }
    }

    private class BaseUiListener implements IUiListener{

        @Override
        public void onComplete(Object response) {
            Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
            try {
                Log.e("tedu", "-------------"+response.toString());
                openidString=((JSONObject)response).getString("openid");
                Log.e("tedu", "-------------"+openidString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            QQToken qqToken = mTencent.getQQToken();
            UserInfo info = new UserInfo(getApplicationContext(), qqToken);
            info.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(final Object response) {
                    // TODO Auto-generated method stub
                    Log.e("tedu", "---------------111111");
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                    Log.e("tedu", "-----111---"+response.toString());
                    /**由于图片需要下载所以这里使用了线程，如果是想获得其他文字信息直接
                     * 在mHandler里进行操作
                     *
                     */
                    new Thread(){

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            JSONObject json = (JSONObject)response;
                            try {
                                bitmap = TencentUtil.getbitmap(json.getString("figureurl_qq_2"));
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            Message msg = new Message();
                            msg.obj = bitmap;
                            msg.what = 1;
                            mHandler.sendMessage(msg);
                        }
                    }.start();

            }

                @Override
                public void onError(UiError uiError) {
Log.e("tedu","错误了");
                }

                @Override
                public void onCancel() {
Log.e("tedu","关闭了");
                }
            });
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    }
    /**
     * 跳转到登录界面的静态方法
     *
     * @param context
     */
    public static void startLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
