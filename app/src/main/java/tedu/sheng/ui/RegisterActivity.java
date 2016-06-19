package tedu.sheng.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import tedu.sheng.R;

/**
 * 注册界面
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    // 返回
    private TextView tvBack;
    // 用户名输入框
    private EditText etUserName;
    // 邮箱输入框
    private EditText etEmail;
    // 密码输入框
    private EditText etUserPwd;
    // 确认密码输入框
    private EditText etSurePwd;
    // 清空用户名
    private ImageView imgClearName;
    // 清空邮箱
    private ImageView imgClearEmail;
    // 清空密码
    private ImageView imgClearPwd;
    // 清空确认密码
    private ImageView imgClearSurePwd;
    // 同意用户协议CheckBox
    private CheckBox cbAgree;
    // 注册按钮
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        // 初始化控件
        initView();

        // 设置事件监听
        setListener();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tvBack = (TextView) findViewById(R.id.tv_register_back);
        etUserName = (EditText) findViewById(R.id.et_register_username);
        etEmail = (EditText) findViewById(R.id.et_register_email);
        etUserPwd = (EditText) findViewById(R.id.et_register_userpwd);
        etSurePwd = (EditText) findViewById(R.id.et_register_sure_userpwd);
        imgClearName = (ImageView) findViewById(R.id.img_register_delete_username);
        imgClearEmail = (ImageView) findViewById(R.id.img_register_delete_email);
        imgClearPwd = (ImageView) findViewById(R.id.img_register_delete_userpwd);
        imgClearSurePwd = (ImageView) findViewById(R.id.img_register_delete_sure_userpwd);
        cbAgree = (CheckBox) findViewById(R.id.cb_register_agree);
        btnRegister = (Button) findViewById(R.id.btn_register);
    }

    /**
     * 设置事件监听
     */
    private void setListener() {
        // 返回事件监听
        tvBack.setOnClickListener(this);
        // 注册按钮事件监听
        btnRegister.setOnClickListener(this);
        // 清空用户名事件监听
        imgClearName.setOnClickListener(this);
        // 清空邮箱事件监听
        imgClearEmail.setOnClickListener(this);
        // 清空密码事件监听
        imgClearPwd.setOnClickListener(this);
        // 清空确认密码事件监听
        imgClearSurePwd.setOnClickListener(this);
        // 用户名输入框改变事件监听
        etUserName.addTextChangedListener(NameChangeListener);
        // 邮箱输入框改变事件监听
        etEmail.addTextChangedListener(EmailChangeListener);
        // 密码输入框改变事件监听
        etUserPwd.addTextChangedListener(PwdChangeListener);
        // 确认密码输入框改变事件监听
        etSurePwd.addTextChangedListener(SurePwdListener);
    }


    /**
     * 用户名输入框改变事件监听
     */
    private TextWatcher NameChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // 如果文本长度为0,设置清空控件不可见;否则,可见
            if (s.length() == 0) {
                imgClearName.setVisibility(View.INVISIBLE);
            } else {
                imgClearName.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 密码输入框改变事件监听
     */
    private TextWatcher PwdChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // 如果文本长度为0,设置清空控件不可见;否则,可见
            if (s.length() == 0) {
                imgClearPwd.setVisibility(View.INVISIBLE);
            } else {
                imgClearPwd.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 确认密码输入框改变事件监听
     */
    private TextWatcher SurePwdListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // 如果文本长度为0,设置清空控件不可见;否则,可见
            if (s.length() == 0) {
                imgClearSurePwd.setVisibility(View.INVISIBLE);
            } else {
                imgClearSurePwd.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 邮箱输入框改变事件监听
     */
    private TextWatcher EmailChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // 如果文本长度为0,设置清空控件不可见;否则,可见
            if (s.length() == 0) {
                imgClearEmail.setVisibility(View.INVISIBLE);
            } else {
                imgClearEmail.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回按钮点击事件
            case R.id.tv_register_back:
                finish();
                break;
            // 清空用户名控件监听
            case R.id.img_register_delete_username:
                etUserName.setText("");
                break;
            // 清空邮箱控件监听
            case R.id.img_register_delete_email:
                etEmail.setText("");
                break;
            // 清空密码控件监听
            case R.id.img_register_delete_userpwd:
                etUserPwd.setText("");
                break;
            // 清空确认密码控件监听
            case R.id.img_register_delete_sure_userpwd:
                etSurePwd.setText("");
                break;
            // 注册按钮点击事件
            case R.id.btn_register:
                if (cbAgree.isChecked()) {
                    Toast.makeText(RegisterActivity.this, "注册", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "请先同意用户协议再注册", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 启动注册界面的静态方法
     *
     * @param context
     */
    public static void startRegisterActivity(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }
}
