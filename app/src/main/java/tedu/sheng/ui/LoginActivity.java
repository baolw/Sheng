package tedu.sheng.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import tedu.sheng.R;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

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
