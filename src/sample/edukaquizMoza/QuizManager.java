package sample.edukaquizMoza;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

public abstract class QuizManager {
	
	public OffLineQuizAcivity offLineActiviy;
	public DBHelper dbh;
	public int offset;
	
	public QuizManager(OffLineQuizAcivity question){
		this.offLineActiviy = question;
	}
	
	public void getQuiz(Integer offset){

		this.offset = offset;
		this.dbh = new DBHelper(this.offLineActiviy);
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor c = db.query(DBHelper.getTableName(), new String[] {"question","answer","dummy1","dummy2","dummy3","image"}, null,null,null,null,null);
		this.offLineActiviy.startManagingCursor(c);
		//ハマリ　c.moveはオフセット現在地からの移動のためtoFirstで最初の地点へ戻す必要があった
		boolean isEof = c.moveToFirst();

		c.move(offset);
		if(isEof){
			
			//問題の取得
			TextView tv = (TextView)this.offLineActiviy.findViewById(R.id.quetion);
			int clmIndex = c.getColumnIndex("question");
			this.offLineActiviy.mondai = c.getString(clmIndex);

			Integer[] select = {1,2,3,4};
			RandomBox.random(select);

			clmIndex = c.getColumnIndex("answer");
			this.offLineActiviy.answer = c.getString(clmIndex);

			for(int i=0;i<select.length;i++){
				tv = (TextView)offLineActiviy.findViewById(offLineActiviy.getResources().getIdentifier("button"+select[i].toString(), "id", offLineActiviy.getPackageName()));
				tv.setText(c.getString(clmIndex));
				clmIndex = c.getColumnIndex("dummy"+String.valueOf(i+1));
			}
			this.setting(c);
		}

		this.dbh.close();
	}

	abstract void setting(Cursor c);
}
