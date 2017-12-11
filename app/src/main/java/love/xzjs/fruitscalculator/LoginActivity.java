package love.xzjs.fruitscalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    EditText pwd;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pwd = findViewById(R.id.pwd);
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("pwd", pwd.getText().toString());
                if (pwd.getText().toString().equals("123456")) {
                    Log.i("pwd", pwd.getText().toString());
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, ConfigActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

}

