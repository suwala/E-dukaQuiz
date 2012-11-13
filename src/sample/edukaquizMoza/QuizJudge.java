package sample.edukaquizMoza;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.MaskFilterSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class QuizJudge extends QuizManager {

	
	//Numberは20*340なので20*34サイズで10個に切り分けてね
	public QuizJudge(OffLineQuizAcivity question) {
		super(question);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	void setting(Cursor c) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	void close() {
		// TODO 自動生成されたメソッド・スタブ

	}
	
	//ジャッジメントですの！
	public void judgement(View v){
		
		Button btn=(Button)v;
		String text = btn.getText().toString();
		
		btn = (Button) offLineActiviy.findViewById(R.id.button1);
		btn.setText("");
		btn = (Button) offLineActiviy.findViewById(R.id.button2);
		btn.setText("");
		btn = (Button) offLineActiviy.findViewById(R.id.button3);
		btn.setText("");
		btn = (Button) offLineActiviy.findViewById(R.id.button4);
		btn.setText("");
		
		TextView tv = (TextView)offLineActiviy.findViewById(R.id.quetion);
		ImageView iv = (ImageView)offLineActiviy.findViewById(R.id.image_point);
		
		
		
		
		//押したbtnのtextを取得しdbの答えと照合　合否で分岐
		if(text.equals(answer)){

			
			btn = (Button)v;
			btn.setText(R.string.maru);
			btn.setTextColor(Color.RED);
			int getPoint=0;

			//1秒で正解だと9000P 5秒で正解だと5000Pが入る計算　マイナスは切り上げて0にする
			if(10000-(int)(System.currentTimeMillis() - startTime) > 0)
				getPoint = 10000-(int)(System.currentTimeMillis() - startTime)-100;

			//正解時にも100P付与
			point += getPoint+100;
			
			this.setTextType(tv,"正解！"+point+"ポイント獲得");
			
			//iv.setImageBitmap(this.getImagePoint(getPoint+100));
			
			answerCount++;
		}else{
			
			this.setTextType(tv,"残念！");
			
			btn = (Button)v;
			btn.setText("×");
		}
		
		
	}
	
	private void setTextType(TextView tv,String str){
		
		tv.setTextSize(30.0f);
		tv.setTextColor(Color.RED);
		// 光源（x, y, z）の設定値。
        float[] direction = { 2.0f, 2.0f, 2.0f };
        
        // 環境光の設定値。
        float ambient = 0.5f;
        
        // 明暗の設定値。
        float specular = 9.0f;
        
        // ぼかしの設定値。
        float blurRadius = 3.0f;
        
        // エンボス（浮き出し）フィルタのインスタンスを作成。
        EmbossMaskFilter filter = new EmbossMaskFilter(direction, ambient, specular, blurRadius);
        
        // SPAN の作成。
        MaskFilterSpan span = new MaskFilterSpan(filter);
        
        // エンボスを掛けるテキストを用意。
        SpannableString spannable = new SpannableString(str);
        
        // テキストに SPAN を挿入。
        spannable.setSpan(span, 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setText(spannable);
		
	}

	
	public Bitmap getImagePoint(int point){
		
		int digits = String.valueOf(point).length();
		Log.d("桁数は",String.valueOf(point)+"の桁数は"+String.valueOf(digits));
		
		//上の桁数から調べ　1=340-34 2=340-34*2 340-(数値*34)から34pixcel 20*34に治まる形に成型
		Bitmap plus = BitmapFactory.decodeResource(offLineActiviy.getResources(), R.drawable.plus);
		plus = Bitmap.createBitmap(plus, 15, 15, 30, 30);
		plus = Bitmap.createScaledBitmap(plus, 200, 200, false);
		return plus;
		
	}
}
