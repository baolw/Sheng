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
 * 忘记密码界面
 */
public class ForgetPwdActivity extends BaseActivity implements View.OnClickListener {

    // 返回
    private TextView tvBack;
    // 邮箱输入框
    private EditText etEmail;
    // 清空邮箱输入
    private ImageView imgClearEmail;
    // 找回密码按钮
    private Button btnFindPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);

        // 初始化控件
        initView();

        // 设置事件监听
        setListener();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tvBack = (TextView) findViewById(R.id.tv_forget_pwd_back);
        etEmail = (EditText) findViewById(R.id.et_forget_pwd_email);
        imgClearEmail = (ImageView) findViewById(R.id.img_forget_pwd_delete_email);
        btnFindPwd = (Button) findViewById(R.id.btn_find_pwd);
    }

    /**
     * 设置事件监听
     */
    private void setListener() {
        // 返回事件监听
        tvBack.setOnClickListener(this);
        // 清空邮箱输入事件监听
        imgClearEmail.setOnClickListener(this);
        // 找回密码按钮事件监听
        btnFindPwd.setOnClickListener(this);
        // 邮箱输入框文字变化事件监听
        etEmail.addTextChangedListener(EmailChangedListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回事件监听
            case R.id.tv_forget_pwd_back:
                finish();
                break;
            // 清空邮箱输入框事件监听
            case R.id.img_forget_pwd_delete_email:
                // 设置邮箱输入框为""
                etEmail.setText("");
                break;
            // 找回密码按钮事件监听
            case R.id.btn_find_pwd:
                Toast.makeText(ForgetPwdActivity.this, "找回密码", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 邮箱输入框改变事件监听
     */
    private TextWatcher EmailChangedListener = new TextWatcher() {
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

    /**
     * 启动忘记密码界面的静态方法
     *
     * @param context
     */
    public static void startForgetPwdActivity(Context context) {
        Intent intent = new Intent(context, ForgetPwdActivity.class);
        context.startActivity(intent);
    }
}
