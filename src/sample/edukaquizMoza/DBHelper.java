package sample.edukaquizMoza;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	static final Integer version = 2;
	static final CursorFactory factory = null;
	static private String tableName = "selectionQ";
	private Context context;
	

	public DBHelper(Context context) {
		super(context, null, factory, version);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
		this.context = context;
	}

	//�e�[�u������Ԃ����\�b�h
	public static  String getTableName(){
		return tableName;
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

		
		db.execSQL("create table "+tableName+" ("+
				"_id integer primary key autoincrement,"+
				" question text not null,"+
				" quizcode integer not null,"+
				" genre text not null,"+
				" answer text not null,"+
				" dummy1 text not null,"+
				" dummy2 text not null,"+
				" dummy3 text not null,"+
				" image text"+
				");"
			);


		//execsql(問題,種類,ジャンル,答え,ダミー*3,imageのリソースNAME)を渡す
		
		db.execSQL(execsql("日本で一番面積が小さい都道府県は？",QuizCode.FourSelected,QuizGenre.Zatsugaku,"香川県","沖縄県","群馬県","サイタマー!",null));
		db.execSQL(execsql("X線を発見した人物は？",QuizCode.FourSelected,QuizGenre.Zatsugaku,"レントゲン","キュリー","スケルトン","ダイナマイト",null));
		db.execSQL(execsql("甲子園球場がある都道府県は？",QuizCode.FourSelected,QuizGenre.Learning,"兵庫県","大阪府","京都府","島根県",null));
		db.execSQL(execsql("アルファベットの由来となったものはアルファと何？",QuizCode.FourSelected,QuizGenre.Zatsugaku,"ベータ","ビータ","ベッド","ビーズ",null));
		db.execSQL(execsql("I Love Youを「月が綺麗ですね」と訳した人物は？",QuizCode.FourSelected,QuizGenre.Zatsugaku,"夏目漱石","福沢諭吉","坂本竜馬","芥川龍之介",null));
		db.execSQL(execsql("光速の異名を持ち重力を自在に操る高貴なる女性騎士と言えば？",QuizCode.FourSelected,QuizGenre.GeAni,"ライトニング","エアリス","セリス","ジェクト",null));
		db.execSQL(execsql("ここはどこでしょう。",QuizCode.Mosaic,QuizGenre.Geography,"エジプト","鳥取","北海道","お台場","sample1"));
		db.execSQL(execsql("天智天皇が定めた，日本で最初の令を何と言うでしょう？", QuizCode.FourSelected,QuizGenre.History, "近江令", "飛鳥浄御原令", "養老律令", "大宝律令", null));
		db.execSQL(execsql("江戸時代に「古事記伝」を著し，国学を大成した人は誰でしょう？", QuizCode.FourSelected, QuizGenre.History, "本居宣長", "滝沢馬琴", "前野良沢", "吉田兼好", null));
		db.execSQL(execsql("普通選挙になって１回目の総選挙が行われたのは西暦何年でしょう？", QuizCode.FourSelected, QuizGenre.History, "1928年", "1945年", "1890年", "1911年", null));
		db.execSQL(execsql("次の中から奈良時代に成立した和歌集を選びなさい。", QuizCode.FourSelected, QuizGenre.History, "万葉集", "古今和歌集", "新古今和歌集", "金槐和歌集", null));
		db.execSQL(execsql("羽柴秀吉が，柴田勝家を破った戦いは，次のうちのどれでしょう？", QuizCode.FourSelected, QuizGenre.History, "賤ヶ岳の戦い","長篠の戦い", "富士川の戦い", "姉川の戦い", null));
		db.execSQL(execsql("日本人としてはじめて新婚旅行をしたというエピソードを持っている人物は誰でしょう？", QuizCode.FourSelected, QuizGenre.History, "坂本龍馬", "徳川光圀", "正岡子規", "吉野作造", null));
		db.execSQL(execsql("人間の血管を全て繋げるとおよそどれくらいの長さになるでしょうか？", QuizCode.FourSelected,QuizGenre.Zatsugaku , "10万km", "15万km", "20万km", "5万km", null));
		db.execSQL(execsql("1111×4444はイクツ？", QuizCode.FourSelected, QuizGenre.Learning, "4937284", "4937283", "4937282", "4937281", null));
		db.execSQL(execsql("お札になった人物です", QuizCode.Mosaic, QuizGenre.History, "夏目漱石", "樋口一葉", "聖徳太子", "福沢諭吉", "souseki"));
		db.execSQL(execsql("第92代内閣総理大臣です。", QuizCode.Mosaic, QuizGenre.History, "麻生太郎", "安倍晋三", "福田康夫", "小泉純一郎", "asou"));
		
		


	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		

	}

	/*
	//sql文を返すメソッド
	private String execsql(String question,int quizcode,String genre,String answer,String dummy1,String dummy2,String dummy3,String image){

		return "insert into "+tableName+" (question,quizcode,genre,answer,dummy1,dummy2,dummy3,image) values ('"+question+"','"+quizcode+"','"+genre+"','"+answer+"','"+dummy1+"', '"+dummy2+"','"+dummy3+"','"+image+"');";

	}
	*/
	
	private String execsql(String question,QuizCode quizcode,QuizGenre genre,String answer,String dummy1,String dummy2,String dummy3,String image){

		return "insert into "+tableName+" (question,quizcode,genre,answer,dummy1,dummy2,dummy3,image) values ('"+question+"','"+quizcode.getCode()+"','"+genre.getGenre()+"','"+answer+"','"+dummy1+"', '"+dummy2+"','"+dummy3+"','"+image+"');";

	}


}
