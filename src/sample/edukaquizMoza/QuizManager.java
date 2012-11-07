package sample.edukaquizMoza;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

public abstract class QuizManager {
	
	public OffLineQuizAcivity question;
	public DBHelper dbh;
	public int offset;
	
	public QuizManager(OffLineQuizAcivity question){
		this.question = question;
	}
	
	public void getQuiz(Integer offset){

		this.offset = offset;
		this.dbh = new DBHelper(this.question);
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor c = db.query(DBHelper.getTableName(), new String[] {"question","answer","dummy1","dummy2","dummy3","image"}, null,null,null,null,null);
		this.question.startManagingCursor(c);
		//ハマリ　c.moveはオフセット現在地からの移動のためtoFirstで最初の地点へ戻す必要があった
		boolean isEof = c.moveToFirst();

		c.move(offset);
		if(isEof){
			
			//問題の取得
			TextView tv = (TextView)this.question.findViewById(R.id.quetion);
			int clmIndex = c.getColumnIndex("question");
			this.question.mondai = c.getString(clmIndex);

			Integer[] select = {1,2,3,4};
			RandomBox.random(select);

			clmIndex = c.getColumnIndex("answer");
			this.question.answer = c.getString(clmIndex);

			for(int i=0;i<select.length;i++){
				tv = (TextView)question.findViewById(question.getResources().getIdentifier("button"+select[i].toString(), "id", question.getPackageName()));
				tv.setText(c.getString(clmIndex));
				clmIndex = c.getColumnIndex("dummy"+String.valueOf(i+1));
			}
			this.setting(c);
		}

		this.dbh.close();
	}

	abstract void setting(Cursor c);
}
