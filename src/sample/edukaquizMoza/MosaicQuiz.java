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

public class MosaicQuiz extends QuizManager {

	private Handler timerHandler = new Handler();
    private Handler deleteHandler = new Handler();
    private Bitmap image;
    private int dot;
	
	public MosaicQuiz(OffLineQuizAcivity question) {
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
		Log.d("aaa",String.valueOf(resId));
		
		image = BitmapFactory.decodeResource(r, resId);
		this.image = Bitmap.createScaledBitmap(image, 160, 200, true);
		this.dot = 40;
		this.timerHandler.postDelayed(CallbackTimer,0);
		
	}
	
	private Runnable CallbackTimer = new Runnable() {

		public void run() {
			// TODO 自動生成されたメソッド・スタブ
			timerHandler.postDelayed(this, 500);

			Bitmap mosaic = Mosaic_image.mosaic_image(image, dot);
			dot -= 2;
			ImageView iv = (ImageView)offLineActiviy.findViewById(R.id.mosaic);
			iv.setImageBitmap(mosaic);
			if(dot == 0){
				deleteHandler.post(CallbackDelete);
			}
		}
	};

	private Runnable CallbackDelete = new Runnable() {
        public void run() {
            /* コールバックを削除して周期処理を停止 */
            timerHandler.removeCallbacks(CallbackTimer);
        }
    };
    
}
