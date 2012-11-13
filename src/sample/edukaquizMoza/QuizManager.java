package sample.edukaquizMoza;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.MaskFilterSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public abstract class QuizManager {
	
	static OffLineQuizAcivity offLineActiviy;
	static DBHelper dbh;
	public int offset;
	//同一パケからも可視出来るのであまり意味ナシ
	protected static long startTime;
	protected static int point=0; 
	protected static String answer;
	protected static int answerCount = 0;
	
	public static void resetResult(){
		answerCount =0;
		point = 0;
	}
	
	public QuizManager(OffLineQuizAcivity question){
		offLineActiviy = question;
	}
	
	public void getQuiz(Integer offset){

		this.offset = offset;
		dbh = new DBHelper(offLineActiviy);
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor c = db.query(DBHelper.getTableName(), new String[] {"question","answer","dummy1","dummy2","dummy3","image"}, null,null,null,null,null);
		offLineActiviy.startManagingCursor(c);
		//ハマリ　c.moveはオフセット現在地からの移動のためtoFirstで最初の地点へ戻す必要があった
		boolean isEof = c.moveToFirst();

		c.move(offset);
		if(isEof){
			
			//問題の取得
			TextView tv = (TextView)offLineActiviy.findViewById(R.id.quetion);
			//this.setTextType(tv, null);
			
			int clmIndex = c.getColumnIndex("question");
			offLineActiviy.mondai = c.getString(clmIndex);

			Integer[] select = {1,2,3,4};
			RandomBox.random(select);

			clmIndex = c.getColumnIndex("answer");
			//this.offLineActiviy.answer = c.getString(clmIndex);
			answer = c.getString(clmIndex);

			for(int i=0;i<select.length;i++){
				tv = (TextView)offLineActiviy.findViewById(offLineActiviy.getResources().getIdentifier("button"+select[i].toString(), "id", offLineActiviy.getPackageName()));
				tv.setText(c.getString(clmIndex));
				tv.setTextColor(Color.BLACK);
				clmIndex = c.getColumnIndex("dummy"+String.valueOf(i+1));
			}
			this.setting(c);
		}

		dbh.close();
		
		//startに現在時刻をセット
		startTime = System.currentTimeMillis();
	}
	
	public long getStartTime(){
		return startTime;
	}
	
	public static int getPoint(){
		return point;
	}
	
	public static int getAnswer(){
		return answerCount;
	}
	
private void setTextType(TextView tv,String str){
	
	

	tv.setTextSize(R.dimen.padding_large);
	tv.setTextColor(Color.BLACK);
	// 光源（x, y, z）の設定値。
	float[] direction = { 2.0f, 2.0f, 2.0f };

	// 環境光の設定値。
	float ambient = 0.5f;

	// 明暗の設定値。
	float specular = 9.0f;

	// ぼかしの設定値。
	float blurRadius = 3.0f;

	// エンボス（浮き出し）フィルタのインスタンスを作成。
	EmbossMaskFilter filter = null;

	// SPAN の作成。
	MaskFilterSpan span = new MaskFilterSpan(null);

	// エンボスを掛けるテキストを用意。
	SpannableString spannable = new SpannableString("");

	// テキストに SPAN を挿入。
	spannable.setSpan(span, 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	tv.setText(spannable);

	tv.setTextSize(R.dimen.padding_large);
	tv.setTextColor(Color.BLACK);

}


	abstract void setting(Cursor c);
	abstract void close();
}
