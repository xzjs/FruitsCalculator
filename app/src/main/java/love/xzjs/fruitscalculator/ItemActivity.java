package love.xzjs.fruitscalculator;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.Manifest;

public class ItemActivity extends AppCompatActivity {

    Button takePhotoBtn;
    TextView name, price;
    ImageView imageView;
    private static final int CROP_PHOTO = 2;
    private static final int REQUEST_CODE_PICK_IMAGE = 3;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    private Uri imageUri;
    private MyDBHelper myDBHelper;
    private int id;
    private Bitmap bitmap = null;
    private Fruit fruit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        takePhotoBtn = findViewById(R.id.take_photo);
        imageView = findViewById(R.id.photo);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);

        myDBHelper = new MyDBHelper(ItemActivity.this, "fruits.db", null, 1);

        Bundle bundle = this.getIntent().getExtras();
        id = bundle != null ? bundle.getInt("id") : 0;
        if (id != 0) {
            fruit = getFruit(id);
            name.setText(fruit.getName());
            price.setText(String.valueOf(fruit.getPrice()));
            imageUri = Uri.parse(fruit.getImg());
            imageView.setImageURI(imageUri);
        }
    }

    @Nullable
    private Fruit getFruit(int id) {
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        Cursor cursor = db.query("fruits", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String img = cursor.getString(cursor.getColumnIndex("img"));
            return new Fruit(id, name, price, img);
        }
        cursor.close();
        return null;
    }

    public void getPhoto(View view) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CALL_PHONE2);
        } else {
            Intent intent;
            switch (view.getId()) {
                case R.id.take_photo:
                    intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivityForResult(intent, CROP_PHOTO);
                    break;
                case R.id.choose_photo:
                    intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");//相片类型
                    startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                    break;
                default:
                    break;
            }
        }
    }

    public void onActivityResult(int req, int res, Intent data) {
        if (res == RESULT_OK) {
            if (req == REQUEST_CODE_PICK_IMAGE) {
                imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Bundle bundle = data.getExtras();
                bitmap = (Bitmap) bundle.get("data");
            }
            imageView.setImageBitmap(bitmap);
        }
    }

    public void save(View view) {
        try {
            SQLiteDatabase db = myDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", name.getText().toString());
            values.put("price", price.getText().toString());
            String path="";
            if(bitmap!=null){
                path = saveBitmap(compressBitmap(bitmap));
            }else{
                path=fruit.getImg();
            }
            values.put("img", path);
            if (id == 0) {
                db.insert("fruits", null, values);
            } else {
                db.update("fruits", values, "id=?", new String[]{String.valueOf(id)});
            }
            this.finish();
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
    }

    public void cancel(View view) {
        this.finish();
    }

    public void delete(View view) {
        if (id != 0) {
            SQLiteDatabase db = myDBHelper.getWritableDatabase();
            db.delete("fruits", "id=?", new String[]{String.valueOf(id)});
            finish();
        }
    }

    /**
     * 压缩图片
     *
     * @param _bitmap 输入图像
     * @return 输出图像
     */
    private Bitmap compressBitmap(Bitmap _bitmap) {
        // 获取图片的宽和高
        float width = _bitmap.getWidth();
        float height = _bitmap.getHeight();
        // 创建操作图片的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = (150) / width;
        float scaleHeight = scaleWidth;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(_bitmap, 0, 0, (int) width, (int) height, matrix, true);
    }

    /**
     * 保存图片
     *
     * @param _bitmap 输入图像
     * @return 存储路径
     */
    private String saveBitmap(Bitmap _bitmap) {
        File file = new File(Environment.getExternalStorageDirectory(), "takePhoto");
        if (!file.exists()) {
            file.mkdir();
        }
        File output = new File(file, System.currentTimeMillis() + ".jpg");
        try {
            if (output.exists()) {
                output.delete();
            }
            output.createNewFile();
            FileOutputStream out = new FileOutputStream(output);
            _bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return output.getPath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
