package love.xzjs.fruitscalculator;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Map;

public class ConfigActivity extends AppCompatActivity {

    ImageButton addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        addBtn = findViewById(R.id.addItem);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ConfigActivity.this, ItemActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getDelegate().onStart();

        GridView gridView = findViewById(R.id.grid);
        ArrayList<Fruit> fruits = new ArrayList<Fruit>();

        MyDBHelper myDBHelper = new MyDBHelper(ConfigActivity.this, "fruits.db", null, 1);
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        Cursor cursor = database.query("fruits", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));
                String img = cursor.getString(cursor.getColumnIndex("img"));
                Log.i("img", "onStart: " + img);
                fruits.add(new Fruit(id, name, price, img));
            } while (cursor.moveToNext());
        }
        cursor.close();


//        SimpleAdapter adapter=new SimpleAdapter(this,fruits,R.layout.layout_item,)
    }
}
