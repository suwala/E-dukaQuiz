package sample.edukaquizMoza;

import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


public class QuizFour extends QuizManager{

	
	public QuizFour(OffLineQuizAcivity question) {
		super(question);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void setting(Cursor c) {
		// TODO 自動生成されたメソッド・スタブ
		ImageView iv = (ImageView)offLineActiviy.findViewById(R.id.mosaic);
		iv.setVisibility(View.INVISIBLE);		
	}

	@Override
	void close() {
		// TODO 自動生成されたメソッド・スタブ
		
	}
	
	

}
