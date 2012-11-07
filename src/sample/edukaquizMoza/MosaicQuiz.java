package sample.edukaquizMoza;

import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MosaicQuiz extends QuizManager {

	
	public MosaicQuiz(OffLineQuizAcivity question) {
		super(question);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void setting(Cursor c) {
		// TODO 自動生成されたメソッド・スタブ
		
		ImageView iv = (ImageView)question.findViewById(R.id.mosaic);
		iv.setVisibility(View.VISIBLE);
		int clmIndex = c.getColumnIndex("image");
		Log.d("draw",String.valueOf(c.getString(clmIndex)));
		
		//iv.setImageResource(question.getResources().getIdentifier(c.getString(clmIndex), "drawable", question.getPackageName()));
		
	}

}
