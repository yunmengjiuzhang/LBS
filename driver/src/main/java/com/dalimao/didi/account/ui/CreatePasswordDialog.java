package com.dalimao.didi.account.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dalimao.didi.R;
import com.dalimao.didi.account.model.AccountManagerImpl;
import com.dalimao.didi.account.presenter.IRegisterPresenter;
import com.dalimao.didi.account.presenter.RegisterPresenterImpl;
import com.dalimao.didi.common.http.OkHttpClientImpl;
import com.dalimao.didi.common.utils.ToastUtil;

/**
 * 密码创建/修改
 * Created by liuguangli on 17/2/26.
 */

public class CreatePasswordDialog extends Dialog implements IRegisterView{

    private TextView mTitle;
    private TextView mPhone;
    private EditText mPw;
    private EditText mPw1;
    private Button mBtnConfirm;
    private View mLoading;
    private TextView mTips;
    private IRegisterPresenter presenter;
    private String mPhoneStr;
    public CreatePasswordDialog(Context context, String phone) {
        this(context, R.style.Dialog);
        mPhoneStr = phone;
    }

    public CreatePasswordDialog(Context context, int theme) {
        super(context, theme);
        presenter = new RegisterPresenterImpl(new AccountManagerImpl(OkHttpClientImpl.getInstance()), this);
        presenter.subscribe();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.unSubscribe();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.dialog_create_pw, null);
        setContentView(root);
        initViews();
        checkUser();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        presenter.unSubscribe();
    }

    /**
     *  注册前先检查用户是否已经存在
     */
    private void checkUser() {
        presenter.checkUserExist(mPhoneStr);
        mLoading.setVisibility(View.VISIBLE);
        mTips.setText(getContext().getString(R.string.check_user));
    }

    private void initViews() {


        mPhone = (TextView) findViewById(R.id.phone);
        mPw = (EditText) findViewById(R.id.pw);
        mPw1 = (EditText) findViewById(R.id.pw1);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        mLoading = findViewById(R.id.loading);
        mTips = (TextView) findViewById(R.id.tips);
        mTitle = (TextView) findViewById(R.id.dialog_title);
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        mPhone.setText(mPhoneStr);

    }

    /**
     * 提交注册
     */
    private void submit() {
        if (checkPassword()) {
            String password = mPw.getText().toString();
            presenter.register(mPhoneStr, password);
        }
    }

    /**
     * 检查密码输入
     * @return
     */
    private boolean checkPassword() {
        String password = mPw.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mTips.setVisibility(View.VISIBLE);
            mTips.setText(getContext().getString(R.string.password_is_null));
            mTips.setTextColor(getContext().getResources().getColor(R.color.error_red));
            return false;
        }
        if (!password.equals(mPw1.getText().toString())) {
            mTips.setVisibility(View.VISIBLE);
            mTips.setText(getContext().getString(R.string.password_is_not_equal));
            mTips.setTextColor(getContext().getResources().getColor(R.color.error_red));
            return false;
        }
        return true;
    }


    @Override
    public void showOrHideLoading(boolean show) {
        if (show) {
            mLoading.setVisibility(View.VISIBLE);
            mBtnConfirm.setVisibility(View.GONE);
        } else {
            mLoading.setVisibility(View.GONE);
            mBtnConfirm.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void showRegisterSuc() {
        // 自动登录
        mLoading.setVisibility(View.VISIBLE);
        mBtnConfirm.setVisibility(View.GONE);
        mTips.setVisibility(View.VISIBLE);
        mTips.setTextColor(getContext().getResources().getColor(R.color.color_text_normal));
        mTips.setText(getContext().getString(R.string.register_suc_and_loging));
        presenter.login(mPhoneStr, mPw.getText().toString());

    }

    @Override
    public void showLoginSuc() {
        dismiss();
        ToastUtil.show(getContext(), getContext().getString(R.string.login_suc));
    }

    @Override
    public void showUserExist(boolean exist) {
        if (exist) {
            mTitle.setText(getContext().getString(R.string.modify_pw));
        } else {
            mTitle.setText(getContext().getString(R.string.create_pw));
        }
        mLoading.setVisibility(View.GONE);
        mTips.setVisibility(View.GONE);
    }

    @Override
    public void showServerError() {
        mTips.setTextColor(getContext().getResources().getColor(R.color.error_red));
        mTips.setText(getContext().getString(R.string.error_server));
    }

}
