package experiment.diary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {
    private Button accessBtn;
    private EditText User;
    private EditText newPasswordText;
    //passwordText作为之后的密码输入
    private EditText passwordText;
    RelativeLayout relativeLayout;
    private DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_layout);

        layoutInit();
    }

    //初始化界面
    private void layoutInit() {
        findViews();
        instantiate();

        //设置背景透明度
        relativeLayout.getBackground().setAlpha(200);
        accessBtn.getBackground().setAlpha(150);

        //判断是否存在已创建的用户
        if(databaseAdapter.isUserExist() != null) {
            accessBtn.setText("进入日记");
            User.setText(databaseAdapter.isUserExist());
            newPasswordText.setVisibility(View.GONE);
            //改变控件位置
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) accessBtn.getLayoutParams();
            params.setMargins(0,20,0,0);
        } else {
            accessBtn.setText("创建用户");
            newPasswordText.setVisibility(View.VISIBLE);
        }

        accessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accessBtn.getText().toString().equals("创建用户")) {
                    if (User.getText().toString().equals("")) {
                        makeToast("用户名不能为空");
                    } else if (passwordText.getText().toString().equals("")) {
                        makeToast("密码不能为空");
                    } else if (newPasswordText.getText().toString().equals("")) {
                        makeToast("请确认密码");
                    } else if (!passwordText.getText().toString().equals(newPasswordText.getText().toString())) {
                        makeToast("两次输入的密码不相同");
                    } else {
                        databaseAdapter.insertUser(User.getText().toString(),passwordText.getText().toString());
                        gotoNextActivity();
                    }
                } else {
                    if (User.getText().toString().equals("")) {
                        makeToast("用户名不能为空");
                    } else if (passwordText.getText().toString().equals("")) {
                        makeToast("密码不能为空");
                    } else if (databaseAdapter.isRightUserAndPassword(User.getText().toString(),passwordText.getText().toString())) {
                        gotoNextActivity();
                    } else {
                        makeToast("用户名或密码输入错误");
                    }
                }
            }
        });
    }

    //建立映射
    private void findViews() {
        accessBtn = (Button) findViewById(R.id.accessBtn);
        User = (EditText) findViewById(R.id.username);
        passwordText = (EditText) findViewById(R.id.password);
        newPasswordText = (EditText) findViewById(R.id.newPassword);
        relativeLayout = (RelativeLayout) findViewById(R.id.start_relative);
    }

    //连接数据库
    private void instantiate() {
        databaseAdapter = new DatabaseAdapter(StartActivity.this);
    }

    //进入主界面
    private void gotoNextActivity() {
        Intent mIntent = new Intent(StartActivity.this,DiaryListActivity.class);
        startActivity(mIntent);
        finish();
    }

    //发送通知
    private void makeToast(String s) {
        Toast.makeText(StartActivity.this,s,Toast.LENGTH_SHORT).show();
    }
}
