package love.xzjs.fruitscalculator;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ArrayList<Fruit> _fruits;
    private AlertDialog _dialog;
    private EditText _text;
    private Account _account;
    private TextView _process, _result, _name;
    private double _total = 0;
    private ArrayList<String> _showStrings;
    private ArrayList<Account> _accounts;
    private View _view;
    private String _symbol = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _process = findViewById(R.id.process);
        _result = findViewById(R.id.result);
        _fruits = new ArrayList<>();
        _showStrings = new ArrayList<>();
        _accounts = new ArrayList<>();

        //数字弹窗
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        _view = inflater.inflate(R.layout.layout_dialog, null, false);
        builder.setView(_view);
        builder.setCancelable(false);
        _dialog = builder.create();
        _name = _view.findViewById(R.id.name);
        _text = _view.findViewById(R.id.num);
    }

    public void config(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (_symbol.equals("=")) {
            clearAll();
        }
        _account = new Account(_fruits.get(i), 1);
        if (_showStrings.size() == 0 || _showStrings.get(_showStrings.size() - 1).equals("+")) {
            _showStrings.add(_account.getFruit().getName());
            _accounts.add(_account);
        } else {
            _showStrings.set(_showStrings.size() - 1, _account.getFruit().getName());
            _accounts.set(_accounts.size() - 1, _account);
        }
        refreshShow();
    }

    private void clearAll() {
        _showStrings.clear();
        _accounts.clear();
        _process.setText("");
        _result.setText("");
        _total = 0;
    }

    public void add(View view) {
        _symbol = "+";
        _showStrings.add("+");
        refreshShow();
    }

    /**
     * 计算器上的C按钮
     *
     * @param view
     */
    public void c(View view) {
        if (_symbol.equals("=")) {
            clearAll();
        } else {
            String s = _showStrings.remove(_showStrings.size() - 1);
            if (!s.equals("+")) {
                _accounts.remove(_accounts.size() - 1);
            }
            refreshShow();
        }
    }

    /**
     * 等于号
     *
     * @param view
     */
    public void getResult(View view) {
        _symbol = "=";
        for (Account account : _accounts) {
            _total += account.getTotal();
        }
        _result.setText("=" + _total);
        _showStrings.clear();
        _accounts.clear();
    }

    private void refreshShow() {
        StringBuilder sb = new StringBuilder();
        for (String s : _showStrings) {
            sb.append(s);
            sb.append(" ");
        }
        _process.setText(sb);
    }

    public void showPayCode(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view_custom = inflater.inflate(R.layout.layout_pay, null, false);
        TextView price = view_custom.findViewById(R.id.priceText);
        price.setText(String.valueOf(_total));
        builder.setView(view_custom);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getDelegate().onStart();

        _fruits.clear();
        GridView gridView = findViewById(R.id.grid);
        MyDBHelper myDBHelper = new MyDBHelper(MainActivity.this, "_fruits.db", null, 1);
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        Cursor cursor = database.query("fruits", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String img = cursor.getString(cursor.getColumnIndex("img"));
            _fruits.add(new Fruit(id, name, price, img));
        }
        cursor.close();

        FruitAdapter adapter = new FruitAdapter(_fruits, this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
    }

    /**
     * 乘号
     *
     * @param view
     */
    public void multiplication(View view) {
        _name.setText(String.format("请输入%s的数量", _account.getFruit().getName()));

        _view.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _result.setText("");
                int num = Integer.parseInt(_text.getText().toString());
                String s = String.format("%s X %d ", _account.getFruit().getName(), num);
                _showStrings.set(_showStrings.size() - 1, s);
                refreshShow();
                _accounts.get(_accounts.size() - 1).setNum(num);
                _dialog.dismiss();
                _text.setText("");
            }
        });

        _view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _dialog.dismiss();
            }
        });

        _dialog.show();
    }
}
