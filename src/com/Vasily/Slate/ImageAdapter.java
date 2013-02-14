package com.Vasily.Slate;
 
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;  
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView; 

public class ImageAdapter extends BaseAdapter
{
  private Integer[] colorPalette;
  private Context context;

  public ImageAdapter(Context paramContext)
  {
     Integer[] arrayOfInteger = {
    		R.drawable.ffffff , R.drawable.b97a57,
    		R.drawable.bl00a2e8 ,R.drawable.c3c3c3,
    	     R.drawable.c8bfe7,R.drawable.c99d9ea,
    	    R.drawable.ed1c24,R.drawable.efe4b0,
    	     R.drawable.ff7f27,R.drawable.ffaec9,
    	     R.drawable.fff200,R.drawable.b3f48cc,
    	     R.drawable.g22b14c,R.drawable.i880015,
    	    R.drawable.lg7092be 
    }; 
    this.colorPalette = arrayOfInteger;
    this.context = paramContext;
  }

  public int getCount()
  {
    return this.colorPalette.length;
  }

  public Object getItem(int paramInt)
  {
    return null;
  }

  public long getItemId(int paramInt)
  {
    return 0L;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
     ImageView localImageView = null;
	if (paramView == null)
	{
	  localImageView = new ImageView(this.context);
	  localImageView.setLayoutParams(new GridView.LayoutParams(85, 85));
	  localImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	  localImageView.setPadding(8, 8, 8, 8);
	}else {
		localImageView = (ImageView) paramView;
	}
    localImageView.setImageResource(this.colorPalette[paramInt].intValue()); 
    return localImageView;
  }
} 