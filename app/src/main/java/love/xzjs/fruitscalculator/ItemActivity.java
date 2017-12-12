package love.xzjs.fruitscalculator;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
import java.io.IOException;
import java.util.jar.Manifest;

public class ItemActivity extends AppCompatActivity {

    Button takePhotoBtn, choosePhotoBtn;
    TextView name, price;
    ImageView imageView;
    private static final int CROP_PHOTO = 2;
    private static final int REQUEST_CODE_PICK_IMAGE = 3;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    private File output;
    private Uri imageUri;
    private MyDBHelper myDBHelper;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        takePhotoBtn = findViewById(R.id.take_photo);
        imageView = findViewById(R.id.photo);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);

        myDBHelper = new MyDBHelper(ItemActivity.this, "fruits.db", null, 1);
    }

    public void takePhoto(View view) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CALL_PHONE2);
        } else {
            File file = new File(Environment.getExternalStorageDirectory(), "takePhoto");
            if (!file.exists()) {
                file.mkdir();
            }
            output = new File(file, System.currentTimeMillis() + ".jpg");
            try {
                if (output.exists()) {
                    output.delete();
                }
                output.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageUri = Uri.fromFile(output);
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, CROP_PHOTO);
        }
    }

    public void choosePhoto(View view) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CALL_PHONE2);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");//相片类型
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        }
    }

    public void onActivityResult(int req, int res, Intent data) {
        switch (req) {
            case CROP_PHOTO:
                if (res == RESULT_OK) {
                    try {
                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        imageView.setImageBitmap(bit);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("tag", "onActivityResult: 失败");
                }
                break;
            case REQUEST_CODE_PICK_IMAGE:
                if (res == RESULT_OK) {
                    try {
                        imageUri = data.getData();
                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        imageView.setImageBitmap(bit);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("tag", e.getMessage());
                    }
                } else {
                    Log.i("liang", "失败");
                }

                break;

            default:
                break;
        }
    }

    public void save(View view) {
        try {
            sqLiteDatabase = myDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", name.getText().toString());
            values.put("price", price.getText().toString());
            values.put("img", imageUri.toString());
            sqLiteDatabase.insert("fruits", null, values);

            startActivity(new Intent(ItemActivity.this,ConfigActivity.class));
        }catch (Exception e){
            Log.e("error", e.getMessage());
        }
    }
}
