package com.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudservey.R;

@SuppressLint("CutPasteId") public class SpinnerAdapter extends ArrayAdapter<String>
{
	private Context ctx;
	private String[] contentArray;
	private String[] imageArray;

	public SpinnerAdapter(Context context, int resource, String[] objects,
			String[] imageArray) {
		super(context,  R.layout.spinner_value_layout, R.id.spinnerTextView, objects);
		this.ctx = context;
		this.contentArray = objects;
		this.imageArray = imageArray;
	}

	@Override
	public View getDropDownView(int position, View convertView,ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) 
	{

		LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.spinner_value_layout, parent, false);

		TextView textView = (TextView)row.findViewById(R.id.spinnerTextView);
		textView.setTextSize(16);
		textView.setTextColor(Color.WHITE);
		textView.setText(contentArray[position]);

		ImageView imageView = (ImageView)row.findViewById(R.id.spinnerImages);
		//imageView.setImageResource(imageArray[position]);
		String bmp="";
		try
		{
			bmp=imageArray[position];
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(60, 60);
			imageView.setLayoutParams(layoutParams);

			try
			{
				byte[] decodedString = Base64.decode(bmp, Base64.DEFAULT);
				Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
				imageView.setImageBitmap(decodedByte);
				imageView.setVisibility(View.VISIBLE);
			}
			catch(Exception e)
			{
				e.getMessage();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return row;    
	}    

}
