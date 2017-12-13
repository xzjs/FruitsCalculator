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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) {
        EditText pwd = findViewById(R.id.pwd);
        if (pwd.getText().toString().equals("123456")) {
            startActivity(new Intent(LoginActivity.this, ConfigActivity.class));
            this.finish();
        }
    }

}

