package love.xzjs.fruitscalculator;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ArrayList<Fruit> fruits;
    private AlertDialog dialog;
    private EditText text;
    private Fruit fruit;
    private TextView process, result;
    private double total = 0;
    private ArrayList<String> showStr;
    private ArrayList<Double> prices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView = findViewById(R.id.grid);
        process = findViewById(R.id.process);
        result = findViewById(R.id.result);
        fruits = new ArrayList<>();
        showStr = new ArrayList<>();
        prices = new ArrayList<>();

        MyDBHelper myDBHelper = new MyDBHelper(MainActivity.this, "fruits.db", null, 1);
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

        FruitAdapter adapter = new FruitAdapter(fruits, this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
    }

    public void config(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = this.getLayoutInflater();
        View view_custom = inflater.inflate(R.layout.layout_dialog, null, false);
        builder.setView(view_custom);
        builder.setCancelable(false);
        dialog = builder.create();

        fruit = fruits.get(i);

        TextView textView = view_custom.findViewById(R.id.name);
        textView.setText(String.format("请输入%s的数量", fruit.getName()));
        text = view_custom.findViewById(R.id.num);

        view_custom.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setText("");
                int num = Integer.parseInt(text.getText().toString());
                String s = String.format("%s * %d ", fruit.getName(), num);
                showStr.add(s);
                show(showStr);

                prices.add(fruit.getPrice() * num);

                dialog.dismiss();
            }
        });

        view_custom.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void add(View view) {
        showStr.add("+");
        show(showStr);
    }

    /**
     * 计算器上的C按钮
     *
     * @param view
     */
    public void c(View view) {
        String s = showStr.remove(showStr.size()-1);
        show(showStr);
        if (s.equals("+")) {
            prices.remove(prices.size()-1);
        }
    }

    public void getResult(View view) {
        for (double price : prices) {
            total += price;
        }
        result.setText("=" + total);
        showStr.clear();
        prices.clear();
    }

    private void show(ArrayList<String> arrayList) {
        StringBuilder sb = new StringBuilder();
        for (String s : arrayList) {
            sb.append(s);
            sb.append(" ");
        }
        process.setText(sb);
    }
}
