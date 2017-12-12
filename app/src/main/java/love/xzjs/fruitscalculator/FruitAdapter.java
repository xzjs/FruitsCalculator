package love.xzjs.fruitscalculator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by xzjs on 2017/12/12.
 */

public class FruitAdapter extends BaseAdapter {

    private ArrayList<Fruit> fruits = new ArrayList<Fruit>();
    private Context context;

    public FruitAdapter(ArrayList<Fruit> fruits,Context context) {
        this.fruits = fruits;
        this.context=context;
    }

    @Override
    public int getCount() {
        return  fruits.size();
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
        if(view==null){
            view= LayoutInflater.from(this.context).inflate(R.layout.layout_item,null);
            view.setTag(this.getItem(i));
        }
        Fruit fruit=(Fruit)this.getItem(i);
        TextView nameTextView= view.findViewById(R.id.name);
        nameTextView.setText(fruit.getName());
        TextView priceTextView=view.findViewById(R.id.price);
        priceTextView.setText(Double.toString(fruit.getPrice()));
        ImageView imageView=view.findViewById(R.id.img);
//        Bitmap bit = BitmapFactory.decodeFile()
//        imageView.setImageBitmap();
        return view;
    }
}
