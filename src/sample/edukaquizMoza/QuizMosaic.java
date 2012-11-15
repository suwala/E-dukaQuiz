package sample.edukaquizMoza;

import java.util.Timer;
import java.util.TimerTask;

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class QuizMosaic extends Quiz {
	
    private Bitmap image;
    private int dot,i;
    private Timer timer;
	
	public QuizMosaic(OffLineQuizAcivity question) {
		super(question);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void setting(Cursor c) {
		// TODO 自動生成されたメソッド・スタブ
		
		ImageView iv = (ImageView)offLineActiviy.findViewById(R.id.mosaic);
		iv.setVisibility(View.VISIBLE);
		int clmIndex = c.getColumnIndex("image");
		
		Resources r= offLineActiviy.getResources();
		int resId = r.getIdentifier(c.getString(clmIndex), "drawable", offLineActiviy.getPackageName());		
		
		this.image = BitmapFactory.decodeResource(r, resId);
		this.image = Bitmap.createScaledBitmap(image, 200, 250, true);
		this.dot = 14;
		this.startTimer();
		//timerHandler = new Handler();
		//this.timerHandler.postDelayed(CallbackTimer,0);
		
	}
    
    public void startTimer(){
    	if(this.timer!=null)
    		this.timer.cancel();
    	
    	final android.os.Handler handler = new android.os.Handler();
    	
    	this.timer = new Timer();
    	this.timer.schedule(new TimerTask() {
    		
			@Override
			public void run() {
				// TODO 自動生成されたメソッド・スタブ
		    	Bitmap bmp = Bitmap.createScaledBitmap(image, 200, 250, false);
				Bitmap mosaic_original = Mosaic_image.mosaic_image(bmp, dot);
				final Bitmap mosaic = Bitmap.createScaledBitmap(mosaic_original, 400, 300,false);
				if(i % 3 ==0)
					dot -= 2;
				i++;
				
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO 自動生成されたメソッド・スタブ
						if(mosaic != null){
							ImageView iv = (ImageView)offLineActiviy.findViewById(R.id.mosaic);
							iv.setImageBitmap(mosaic);
						}
					}
				});
			}		
		},0, 500);
    	
    }

	@Override
	void close() {
		// TODO 自動生成されたメソッド・スタブ
		
		Log.d("モザイク","停止処理");
		//this.deleteHandler.post(CallbackDelete);
		this.timer.cancel();
		this.image = null;
		
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
