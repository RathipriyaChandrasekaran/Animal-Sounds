package com.rathipriya.zoodirectory.adapter;

import com.rathipriya.zoodirectory.R;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

	int width;
	int height;

	private Context mContext;

	// Keep all Images in array
	public Integer[] thumb = {
			R.drawable.elephant_3,
			R.drawable.fox, R.drawable.monkey, R.drawable.gorilla, 
			R.drawable.hippopotamus, R.drawable.bear, R.drawable.wolf,
			R.drawable.pandas, R.drawable.white_rhinoceros,
			R.drawable.lionname,R.drawable.tiger,R.drawable.cheetah,
			R.drawable.deer, R.drawable.camel,R.drawable.hyena

	};


	// Constructor
	public ImageAdapter(Context c, int width,int height){
		mContext = c;
		this.width = width;
		this.height = height;
	}

	@Override
	public int getCount() {

		return thumb.length;
	}

	@Override
	public Object getItem(int position) {
		return thumb[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Inflate the item layout and set the views
		/**		View listItem = convertView;
    				int pos = position;
    				if (listItem == null) {
    					 LayoutInflater inflater = (LayoutInflater) mContext
    						        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    					listItem = inflater.inflate(R.layout.row_item, null);
    				}*/
		ImageView imageView = new ImageView(mContext);
		imageView.setImageResource(thumb[position]);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

		imageView.setLayoutParams(new GridView.LayoutParams(width, height));
		return imageView;
	}


}
