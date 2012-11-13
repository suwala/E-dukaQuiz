package sample.edukaquizMoza;

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class QuizMosaic extends QuizManager {

	
	
	private Handler timerHandler = new Handler();
    private Handler deleteHandler = new Handler();
    private Bitmap image;
    private int dot;
	
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
		this.dot = 12;
		timerHandler = new Handler();
		this.timerHandler.postDelayed(CallbackTimer,0);
		
	}
	
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
            /* コールバックを削除して周期処理を停止 */
            timerHandler.removeCallbacks(CallbackTimer);
        }
    };

	@Override
	void close() {
		// TODO 自動生成されたメソッド・スタブ
		
		Log.d("モザイク","停止処理");
		this.deleteHandler.post(CallbackDelete);
		this.image = null;
	}
    
}
