package com.Vasily.Slate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent; 
import android.graphics.Bitmap; 
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint; 
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment; 
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu; 
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View; 
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream; 
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
 
 
public class SlateActivity extends Activity
{
	private static final int ACTIVITY_CREATE = 1;
 	public static final String APP_PATH_SD_CARD = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Slate/";
	public static final String Tag = "Slate";
	public int pencilColor = 0;
	public int pencilWidth = 0;
	private static DrawPanel view;    
	private SaveBitmapThread saveBitmap; 
	private static Handler handler;
	
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == RESULT_OK && requestCode == ACTIVITY_CREATE) {
			Bundle bundle = intent.getExtras();
			bundle.getString("The_Width");
			int width = 0;
			int color = -1;
			if (bundle != null)
			{
				width = Integer.valueOf(bundle.getString("The_Width")).intValue();
				color = Integer.valueOf(bundle.getString("The_Color")).intValue(); 
			}
			if (width != 0)
				this.pencilWidth = width;
			if (color > 0)
				this.pencilColor = whichColor(color);
		}
	}
 
	protected void onCreate(Bundle paramBundle)
	{
		super.onCreate(paramBundle);
		requestWindowFeature(1);
		getWindow().setFlags(1024, 1024);
		view = new DrawPanel(this);
		setContentView(view);
		if (this.pencilColor == 0)
			this.pencilColor = Color.WHITE;
		this.pencilWidth = 4; 
		saveBitmap = new SaveBitmapThread();
		handler = new Handler() {
	            public void handleMessage(Message message) {
	            	makeToast(message.what);
	            }
	     };
	}
	
	private void makeToast(int what) {
		String toastMessage;
		switch (what) {
		case 0: 
			toastMessage = getString(R.string.Save_Successful) + APP_PATH_SD_CARD;
		break;
		case 1:
			toastMessage = getString(R.string.Save_Failed_FileNotFoundException);
		break;
		case 2: 
			toastMessage =  getString(R.string.Save_Failed_IOException);
		break;
		case 3:   
			toastMessage = getString(R.string.SDCard_UnMounted);
  	    break;
		default:
			 toastMessage = getString(R.string.generic_error);
		break;
	   }
	  Toast.makeText(SlateActivity.this,toastMessage , Toast.LENGTH_SHORT).show();
	}
 
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		boolean bool = true;
		switch (item.getItemId()) {

		case R.id.menu_pen:
				this.pencilColor = Color.WHITE;
				this.pencilWidth = 4;
		return true;
		
		case R.id.menu_clear:
	                confirmActionDialog();
	 	return true;
	 	
		case R.id.menu_eraser:
			this.pencilColor = Color.BLACK; 
			this.pencilWidth = 10;
		return true;
		
		case R.id.menu_save: 
			 saveBitmap.start(); 
		return true;
		
		case R.id.menu_setting:
		try
		{
			Intent intent = new Intent(this, Setting.class);
			Bundle bundle = new Bundle();
			bundle.putString("currentwidth", String.valueOf(this.pencilWidth));
			intent.putExtras(bundle);
			startActivityForResult(intent, ACTIVITY_CREATE);
		}
		catch (Exception ex)
		{
			Log.v("Slate:", ex.toString());
		}
		return true;
		
	   }
	return bool;	
   }
    
	 /**
     * Displays a Confirmation Dialog before doing an action. 
     */
     private void confirmActionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SlateActivity.this);
                 builder.setTitle("Confirm clear");
                 builder.setPositiveButton(R.string.dialog_true, new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int id) {
                	 view.canvas.drawColor(Color.BLACK);
                 	 view.invalidate();
                    }
                });
         builder.setNegativeButton(R.string.dialog_false, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                      dialog.dismiss();
                    }
                });
         AlertDialog dialog = builder.create();
         dialog.show(); 
    }
	
	 
	public void onPause()
	{
	  super.onStop();
	}

	public boolean onPrepareOptionsMenu(Menu paramMenu)
	{
		super.onPrepareOptionsMenu(paramMenu);
		return true;
	}

	public class SaveBitmapThread extends Thread{
		@Override
		public void run() {
		super.run();
		File localFile = new File(APP_PATH_SD_CARD);
		
		if (!localFile.exists()) 
			localFile.mkdirs();   
 		if( Environment.MEDIA_UNMOUNTED != Environment.getExternalStorageState()){
			SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss",Locale.US);
			Date localDate = new Date();
			String FileName = String.valueOf(new StringBuilder("Slate_").append(localSimpleDateFormat.format(localDate).toString()).append(".png").toString());
			try
			{
				File saveFile = new File(APP_PATH_SD_CARD,FileName);
				FileOutputStream localFileOutputStream = new FileOutputStream(saveFile);
	 			view.bitmap.compress(Bitmap.CompressFormat.PNG, 100, localFileOutputStream);
				localFileOutputStream.flush();
				localFileOutputStream.close(); 
	    		sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + APP_PATH_SD_CARD)));
	    		handler.sendEmptyMessage(0); 
			}catch (FileNotFoundException e) {
				handler.sendEmptyMessage(1);
			} catch (IOException e) {
				handler.sendEmptyMessage(2);
			}
		}else{
			handler.sendEmptyMessage(3);
		}
	  } 
    } 
	 
     
	public int whichColor(int paramInt)
	{
	  int i;
		switch (paramInt)
		{
			default:
				i = this.pencilColor;
			case 1:
			{
				i = Color.rgb(255, 255, 255);
				break;
			}
			case 2:{
				i = Color.rgb(184, 122, 88);
				break;
			}
			case 3:{
				i = Color.rgb(0, 161, 230);
				break;
			}
			case 4:{
				i = Color.rgb(125, 125, 125);
				break;
			}
			case 5:{
				i = Color.rgb(159, 72,161);
				break;
			}
			case 6:{
				i = Color.rgb(151, 214, 230);
				break;
			}
			case 7:{
				i = Color.rgb(237, 28, 36);
				break;
			}
			case 8:{
				i = Color.rgb(235, 223, 174);
				break;
			}
			case 9:{
				i = Color.rgb(255, 127, 39);
				break;
			}
			case 10:{
				i = Color.rgb(255, 174, 201);
				break;
			}
			case 11:{
				i = Color.rgb(255, 242, 0);
				break;
			}
			case 12:{
				i = Color.rgb(0,0,255);
				break;
			}
			case 13:{
				i = Color.rgb(34, 177, 76);
				break;
			}
			case 14:{
				i = Color.rgb(133, 0, 18);
				break;
			}
			case 15:{
				i = Color.rgb(111, 146, 189);
				break;
			}
		}
	return i;
	}

	public class DrawPanel extends View
	{
		public final Bitmap bitmap = Bitmap.createBitmap(getContext().getResources().getDisplayMetrics().widthPixels, getContext().getResources().getDisplayMetrics().heightPixels, Bitmap.Config.ARGB_8888);
		private final Paint bitmappaint = new Paint(4);
		private Canvas canvas = new Canvas(this.bitmap);
		private float oldX;
		private float oldY;
		private final Paint paint = new Paint();
		private final Path path = new Path();

		public DrawPanel(Context c)
		{
			super(c);
			this.paint.setStyle(Paint.Style.STROKE);
			this.paint.setStrokeJoin(Paint.Join.ROUND);
			this.paint.setStrokeCap(Paint.Cap.ROUND);
			this.paint.setAntiAlias(true);
			
		}

		protected void onDraw(Canvas paramCanvas)
		{
			paramCanvas.drawColor(-16777216);
			paramCanvas.drawBitmap(this.bitmap, 0.0F, 0.0F, this.bitmappaint);
			paramCanvas.drawPath(this.path, this.paint);
		}

		public boolean onTouchEvent(MotionEvent event)
		{
		float f1 = event.getX();
		float f2 = event.getY();
		this.paint.setColor(SlateActivity.this.pencilColor);
		this.paint.setStrokeWidth(SlateActivity.this.pencilWidth);
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				this.path.reset();
				this.path.moveTo(f1, f2);
				this.oldX = f1;
				this.oldY = f2;
				invalidate();
			break;
			case MotionEvent.ACTION_MOVE:
				this.path.quadTo(this.oldX, this.oldY, (f1 + this.oldX) / 2.0F, (f2 + this.oldY) / 2.0F);
				this.oldX = f1;
				this.oldY = f2;
				invalidate();
			break; 
			case MotionEvent.ACTION_UP: 
				this.path.lineTo(this.oldX, this.oldY);
				this.canvas.drawPath(this.path, this.paint);
				this.path.reset();
				invalidate();
			break;
		}
	return true;
	}
   }
  }



