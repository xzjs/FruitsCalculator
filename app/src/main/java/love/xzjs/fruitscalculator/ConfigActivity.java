package love.xzjs.fruitscalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ConfigActivity extends AppCompatActivity {

    ImageButton addBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        addBtn=findViewById(R.id.addItem);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ConfigActivity.this, ItemActivity.class);
                startActivity(intent);
            }
        });
    }
}
