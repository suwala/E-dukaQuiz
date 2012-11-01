package sample.edukaquiz;

import sample.edukaquiz.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Result extends Activity{
	
	private Handler timerHandler = new Handler();
	private Handler deleteHandler = new Handler();
	private long start;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        
        
        
        //getStringExtra 渡された追加文字列を受け取る　getStringExtra("keyword") 返り値が渡した文字列ぽい
        //staticでも解決できることが判明そっと見送る
        
        //String data = i.getStringExtra("a_count");

        
        TextView tv = (TextView)findViewById(R.id.textView1);
        tv.setText(Question.point.toString()+"POINT獲得！\nあなたの正解数は"+Question.a_c.toString()+"問です！\n間違った回数は"+Question.miss.toString()+"問です");
        this.start = System.currentTimeMillis();
        this.timerHandler.postDelayed(CallbackTimer, 0);
        
        
    }
	
	private Runnable CallbackTimer = new Runnable() {
		
		@Override
		public void run() {
			// TODO 自動生成されたメソッド・スタブ
			
			timerHandler.postDelayed(this,10);
			
			TextView tv = (TextView)findViewById(R.id.textView1);
			int length = (int)(System.currentTimeMillis()-start)/5;
			if(length >=255){
				deleteHandler.postDelayed(CallbackDelete, 0);
				length = 255;
			}
			tv.setTextColor(Color.argb(length, 0, 0, 0));
			
		}
	};
	
	private Runnable CallbackDelete = new Runnable() {
        public void run() {
            /* コールバックを削除して周期処理を停止 */
            timerHandler.removeCallbacks(CallbackTimer);
        }
    };
	
	public void toTitle(View view){
		Intent i=new Intent(this,MainActivity.class);
		//遷移先のアクティビティが稼動済みの場合それより上にあるアクティビティをキルする
		//要するに結果画面＞バックキー＞問題の画面に戻るのを防ぐ
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//良くワカラン調べることi.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		this.startActivity(i);
		this.deleteHandler.postDelayed(CallbackDelete, 0);
	}

}
