package sample.edukaquizMoza;

import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


public class FourQuiz extends QuizManager{

	
	public FourQuiz(OffLineQuizAcivity question) {
		super(question);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void setting(Cursor c) {
		// TODO 自動生成されたメソッド・スタブ
		ImageView iv = (ImageView)offLineActiviy.findViewById(R.id.mosaic);
		iv.setVisibility(View.INVISIBLE);		
	}
	
	

}
