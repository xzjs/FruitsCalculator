package love.xzjs.fruitscalculator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by xzjs on 2017/12/12.
 */

public class FruitAdapter extends BaseAdapter {

    private ArrayList<Fruit> fruits = new ArrayList<Fruit>();
    private Context context;

    public FruitAdapter(ArrayList<Fruit> fruits, Context context) {
        super();

        this.fruits = fruits;
        this.context = context;
    }

    @Override
    public int getCount() {
        return fruits.size();
    }

    @Override
    public Object getItem(int i) {
        return fruits.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view != null) {
            viewHolder = (ViewHolder) view.getTag();
        } else {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(this.context).inflate(R.layout.layout_item, null);
            viewHolder.nameTextView = view.findViewById(R.id.name);
            viewHolder.priceTextView = view.findViewById(R.id.price);
            viewHolder.imageView = view.findViewById(R.id.img);
            view.setTag(viewHolder);
        }
        Fruit fruit = (Fruit) this.getItem(i);
        viewHolder.nameTextView.setText(fruit.getName());
        viewHolder.priceTextView.setText(fruit.getPrice() + "元");
        Bitmap bitmap=BitmapFactory.decodeFile(fruit.getImg());
        viewHolder.imageView.setImageBitmap(bitmap);
        return view;
    }

    class ViewHolder {
        public TextView nameTextView, priceTextView;
        public ImageView imageView;
    }
}
