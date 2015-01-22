package com.example.x;

import java.util.ArrayList;

import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
	private static final int DURATION = 400;
	String[] result;
	Context context;
	int[] imageId;
	Menu mymenu;
	HelloGridView myobject;
	MenuItem vi;
	int count = 0;
	JSONObject jsonobject;
	String choices = "";
	String choice = "";
	ArrayList<Integer> sangha = new ArrayList<Integer>();
	private static LayoutInflater inflater = null;
	

	public ImageAdapter(HelloGridView mainActivity, String[] ctgrynameList,
			int[] ctgryImages, Context c) {
		// TODO Auto-generated constructor stub
		result = ctgrynameList;
		context = mainActivity;
		imageId = ctgryImages;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return result.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class Holder {
		TextView tv;
		ImageView img;
		ImageView img1;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Holder holder = new Holder();
		View rowView;

		rowView = inflater.inflate(R.layout.category_list, null);
		
		holder.tv = (TextView) rowView.findViewById(R.id.ctgryname);
		
		holder.img = (ImageView) rowView.findViewById(R.id.ctgryimages);
		holder.img1 = (ImageView) rowView.findViewById(R.id.tick);
		
		holder.tv.setText(result[position]);
		holder.img.setImageResource(imageId[position]);

		holder.img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (count < 4) {
					Log.d("counnnnnttt", count+"");
					if(flip(holder.img, holder.img1, DURATION)==1){
					count++;
					Log.d("flipped", count+"");
					}
					if (count > 0) {
						((MenuItem) vi).setVisible(true);
					}
					sangha.add(position);
					choice = choice + ";"+position;
					choices = choices + ";" + result[position];
					holder.tv.setTypeface(Typeface.DEFAULT_BOLD);

				}

				else {
					flip(holder.img, holder.img, DURATION);
				}
			}
		});
		holder.img1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub5
				count--;
				flip(holder.img1, holder.img, DURATION);
				if (count == 0) {
					((MenuItem) vi).setVisible(false);
				}
				
				String temp[] = choices.split(";");
				String tmp[] = choice.split(";");
				for (int i = 0; i < temp.length; ++i) {
					if (temp[i].equals("" + result[position])) {
						temp[i] = "";
						tmp[i] = "";
					}
				}
				holder.tv.setTypeface(Typeface.DEFAULT);
				String temch = "";
				String tmch = "";
				
				for (int i = 0; i < temp.length; ++i)
				{	temch = temch + ";" + temp[i];
					tmch = tmch+ ";"+tmp[i];
				}
				choices = temch;
				Log.d("Choices" , " "+choices);
			}
		});
		return rowView;
	}

	public int flip(final View front, final View back, final int duration) {
		int i=0;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			AnimatorSet set = new AnimatorSet();
			set.playSequentially(
					ObjectAnimator.ofFloat(front, "rotationY", 90).setDuration(
							duration / 2),
					ObjectAnimator.ofInt(front, "visibility", View.GONE)
							.setDuration(0),
					ObjectAnimator.ofFloat(back, "rotationY", -90).setDuration(
							0),
					ObjectAnimator.ofInt(back, "visibility", View.VISIBLE)
							.setDuration(0),
					ObjectAnimator.ofFloat(back, "rotationY", 0).setDuration(
							duration / 2));
			set.start();
			Log.d("android", "1");
			i=1;
		} else {
			front.animate().rotationY(90).setDuration(duration / 2)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							front.setVisibility(View.GONE);
							back.setRotationY(-90);
							back.setVisibility(View.VISIBLE);
							back.animate().rotationY(0)
									.setDuration(duration / 2)
									.setListener(null);
						}
					});
			i=1;
			Log.d("android", "2");
		}
		return i;
	}

}