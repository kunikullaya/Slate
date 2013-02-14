package com.Vasily.Slate;
 
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View; 
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener; 
import android.widget.GridView;
import android.widget.SeekBar; 
import android.widget.Toast;

public class Setting extends Activity  implements SeekBar.OnSeekBarChangeListener, OnItemClickListener
	{
		  private int newcolor = 0;
		  private int newwidth = 0;
		  private SeekBar seekbar;

		  public void onBackPressed()
		  {
			    String width = String.valueOf(this.newwidth);
			    String color = String.valueOf(this.newcolor);
			    Intent i = new Intent();
			    i.putExtra("The_Width", width);
			    i.putExtra("The_Color", color);
			    setResult(-1, i);
			    finish();
			    super.onBackPressed();
		  }

		  public void onCreate(Bundle paramBundle)
		  {
			    super.onCreate(paramBundle);
			    setContentView(R.layout.activity_settings);
 			    GridView gv = (GridView)findViewById(R.id.gridView);
 			    gv.setAdapter(new ImageAdapter(this));
			    this.seekbar = ((SeekBar)findViewById(R.id.seekBar));
			    this.seekbar.setOnSeekBarChangeListener(this);
			    String str = getIntent().getExtras().getString("currentwidth");
			    this.seekbar.setProgress(Integer.parseInt(str));
 			    gv.setOnItemClickListener(this);
		  }


		  public void onProgressChanged(SeekBar paramSeekBar, int paramInt, boolean paramBoolean)
		  {
			  this.newwidth = paramInt;
		  }

		  public void onStartTrackingTouch(SeekBar paramSeekBar)
		  {
		  }

		  public void onStopTrackingTouch(SeekBar paramSeekBar)
		  {
		  }

		 public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				this.newcolor=position+1;
				Toast.makeText(this.getApplicationContext(), "Color Changed...",Toast.LENGTH_SHORT ).show();
		}
 	}
 