package sample.edukaquiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	static final Integer version = 1;
	static final CursorFactory factory = null;
	static private String tableName = "selectionQ";
	
	public DBHelper(Context context) {
		super(context, null, factory, version);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
	}

	//�e�[�u������Ԃ����\�b�h
	public static  String getTableName(){
		return tableName;
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
		//answerに答えをdummyに間違えを入れる　受け取った後シャッフルさせる　テーブル名どうしよう　クイズごとに分ける？
		db.execSQL("create table "+tableName+" ("+
				" question text not null,"+
				" answer text not null,"+
				" dummy1 text not null,"+
				" dummy2 text not null,"+
				" dummy3 text not null"+
				");"
			);
		
		//execsql(問題,答え,ダミー*3)を渡す
		db.execSQL(execsql("日本で一番面積が小さい都道府県は？","香川県","沖縄県","群馬県","サイタマー!"));
		db.execSQL(execsql("X線を発見した人物は？","レントゲン","キュリー","スケルトン","ダイナマイト"));
		db.execSQL(execsql("甲子園球場がある都道府県は？","兵庫県","大阪府","京都府","島根県"));
		db.execSQL(execsql("アルファベットの由来となったものはアルファと何？","ベータ","ビータ","ベッド","ビーズ"));
		db.execSQL(execsql("I Love Youを「月が綺麗ですね」と訳した人物は？","夏目漱石","福沢諭吉","坂本竜馬","芥川龍之介"));
		db.execSQL(execsql("光速の異名を持ち重力を自在に操る高貴なる女性騎士と言えば？","ライトニング","エアリス","セリス","ジェクト"));
		
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

	}
	
	//sql文を返すメソッド
	private String execsql(String question,String answer,String dummy1,String dummy2,String dummy3){
				
		return "insert into "+tableName+" (question,answer,dummy1,dummy2,dummy3) values ('"+question+"','"+answer+"','"+dummy1+"', '"+dummy2+"','"+dummy3+"');";
		
	}
	

}
