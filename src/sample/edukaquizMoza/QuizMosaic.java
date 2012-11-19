package sample.edukaquizMoza;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class QuizMosaic extends Quiz {
	
    private Bitmap image,mosaic;
    //private int dot=14,i;
    private Timer timer;
	
    public QuizMosaic() {
		// TODO 自動生成されたコンストラクター・スタブ
	}    

	@Override
	public void start() {
		// TODO 自動生成されたメソッド・スタブ
		
		ImageView iv = (ImageView)activity.findViewById(R.id.mosaic);
		iv.setVisibility(View.VISIBLE);		
		this.mosaicCreate();
		
		
	}
	    
    public void mosaicCreate(){
    	if(this.image != null){
    	if(this.timer!=null)
    		this.timer.cancel();
    	
    	final int dot = 14 ;
    	final android.os.Handler handler = new android.os.Handler();
    	
    	this.timer = new Timer();
    	this.timer.schedule(new TimerTask() {

    		@Override
    		public void run() {
    			// TODO 自動生成されたメソッド・スタブ

    			
    			Bitmap bmp = Bitmap.createScaledBitmap(image, 200, 250, false);
    			Bitmap mosaic_original = CreateMosaic.mosaic_image(bmp,dot - (int)(System.currentTimeMillis() - startTime)/800);
    			mosaic = Bitmap.createScaledBitmap(mosaic_original, 400, 300,false);
    			handler.post(new Runnable() {
					
					@Override
					public void run() {
						if(mosaic != null){
							// TODO 自動生成されたメソッド・スタブ
							ImageView iv = (ImageView)activity.findViewById(R.id.mosaic);
							iv.setImageBitmap(mosaic);
							Log.d("QuizMosaic","mosaicCreate_Run");
						}
					}
				});
    		}		
    	},0, 500);
    	}
    }
 
	@Override
	void close() {
		// TODO 自動生成されたメソッド・スタブ
		
		Log.d("モザイク","停止処理");
		//this.deleteHandler.post(CallbackDelete);
		//this.timer.cancel();
		//this.deleteMosaicHandler.post(DeleteMosaic);
		
		if(this.timer != null){
			this.timer.cancel();
			Log.d("aaa","aaa");
		}
		
		this.timer.cancel();
		this.timer = null;		
	
	}

	@Override
	public void judgement(View v) {
		// TODO 自動生成されたメソッド・スタブ
		super.judgement(v);

		Log.d("mosaic","judge");
		this.image = null;
		this.mosaic = null;
		while(this.timer != null){
			Log.d("main","null");
		}
		ImageView iv = (ImageView)activity.findViewById(R.id.mosaic);
		iv.setImageResource(R.drawable.plus);
		iv.setVisibility(View.INVISIBLE);
	}

	@Override
	void stop() {
		// TODO 自動生成されたメソッド・スタブ
		if(this.timer != null)
			this.timer.cancel();
		this.timer = null;
		this.mosaic = null;
		Log.d("mosaic","STOP");
	}

	@Override
	void individualSetup() {
		// TODO 自動生成されたメソッド・スタブ
		
		ImageView iv = (ImageView)activity.findViewById(R.id.mosaic);
		iv.setVisibility(View.VISIBLE);
		int clmIndex = c.getColumnIndex("image");
		
		Resources r= activity.getResources();
		int resId = r.getIdentifier(c.getString(clmIndex), "drawable", activity.getPackageName());		
		
		this.image = BitmapFactory.decodeResource(r, resId);
		this.image = Bitmap.createScaledBitmap(image, 200, 250, true);
		
		int dot = 14;
		Bitmap mosaic = CreateMosaic.mosaic_image(this.image, dot);
		iv.setImageBitmap(Bitmap.createScaledBitmap(mosaic, 400, 300, false));
		this.mosaic = null;
		this.mosaicCreate();
		
	}
	
	
}
    
	/*メインスレッドでモザイクをリアルタイムで作成してたため重くなっていた
	private Runnable CallbackTimer = new Runnable() {

		private int i=0;		
		
		public void run() {
			// TODO 自動生成されたメソッド・スタブ
			timerHandler.postDelayed(this, 500);

			if(image != null){
				Bitmap mosaic = Mosaic_image.mosaic_image(image, dot);
				if(i%4 == 0)
					dot -= 2;
				ImageView iv = (ImageView)offLineActiviy.findViewById(R.id.mosaic);
				iv.setImageBitmap(mosaic);
				if(dot == 0){
					deleteHandler.post(CallbackDelete);
				}
				i++;
			}
		}
	};
	
	private Runnable CallbackDelete = new Runnable() {
        public void run() {
            /* コールバックを削除して周期処理を停止 
            timerHandler.removeCallbacks(CallbackTimer);
        }
    };
	*/
