package sample.edukaquizMoza;

import java.util.ArrayList;
import java.util.List;

import sample.edukaquizMoza.R;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class OffLineQuizAcivity extends Activity{

    

	
    public Integer totalQuestion,q_Index=0;    //DBに登録されている総問題数のカウント　DBのメソッドで解決できる？ q_Index = 現在が難問目かの保持


    public final Integer syutudai = 3; //出題数

    public  Integer[] order;//DBのIndex準拠にした問題の出題順　総問題数をシャッフルする

    static Integer a_c,miss,point; //正解数のカウント
    public String mondai;
    public int dot,count=4;
    public List<QuizManager> quizType =  new ArrayList<QuizManager>();
    public QuizManager quizManager;
    
    private int quizCode;
    private Handler timerHandler = new Handler();
    private Handler deleteHandler = new Handler();
    private Handler nextHandler = new Handler();
    private Handler nextDelete = new Handler();
    private long start;
    private final int pbTime = 10000;//ProgressBarの設定タイム

    
    
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);

        QuizManager four = new QuizFour(this);
        QuizManager mosaic = new QuizMosaic(this);
        
        this.quizType.add(four);
        this.quizType.add(mosaic);
        
        QuizManager.resetResult();
        
        DBHelper dbh = new DBHelper(this);
        SQLiteDatabase db = dbh.getReadableDatabase();
        String sql = "SELECT COUNT(*) from "+DBHelper.getTableName();

        //rawQueryは生のSQL文を使える　簡単！
        Cursor cursor = db.rawQuery(sql,null);

        //cursorの自動クローズモード？カラムindexを元に全問題数をゲット *の場合のカラム名が不明
        this.startManagingCursor(cursor);
        //Integer index  = cursor.getColumnIndex("*");
        cursor.moveToFirst();
        this.totalQuestion = cursor.getInt(0);

        Log.d("oncre",String.valueOf(this.totalQuestion));//総問題数の一致確認
        dbh.close();

        this.order= new Integer[totalQuestion];
        //出題順の決定
        this.setOrder();
        
        
        //モザイク問題を先頭へ
        this.order[0] = 6;this.order[1] = 16;

        this.question();

        //経過時間の設定　現在MAX5秒
        ProgressBar pb = (ProgressBar)this.findViewById(R.id.progressBar1);
        pb.setMax(this.pbTime);

        a_c = miss = point = 0;
        
        
    }

	private Runnable CallbackTimer = new Runnable() {

		public void run() {
			
			// TODO 自動生成されたメソッド・スタブ
			timerHandler.postDelayed(this, 10);

			TextView tv = (TextView)findViewById(R.id.quetion);
			tv.setTextSize(16);
			tv.setTextColor(Color.BLACK);

			//0.1秒につき一文字表示
			int length = (int)(System.currentTimeMillis()-start)/100;
						

			//0.1秒につき問題文を一文字表示する
			if(length > mondai.length())
				length = mondai.length();
			tv.setText(mondai.subSequence(0, length));

			ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar1);
			pb.setProgress((int)(System.currentTimeMillis()-start));
			if((int)(System.currentTimeMillis()-start) > 10000){
				deleteHandler.post(CallbackDelete);
			}
		}
	};

	private Runnable CallbackDelete = new Runnable() {
        public void run() {
            /* コールバックを削除して周期処理を停止 */
            timerHandler.removeCallbacks(CallbackTimer);
        }
    };
    
    private Runnable NextTimer = new Runnable(){
    	
		@Override
		public synchronized void run() {
			// TODO 自動生成されたメソッド・スタブ
			
			TextView tv = (TextView)findViewById(R.id.count);
			if(count == 0){
				tv.setText("");
				q_Index++;
				question();
				deleteHandler.post(NextDelete);
				count = 4;
			}else{

				tv.setText("Next To"+(count-1));
				count--;
			}
			
			nextHandler.postDelayed(NextTimer, 1500/4);
			
		}
    	
    };
    
    private Runnable NextDelete = new Runnable() {
        public void run() {
            /* コールバックを削除して周期処理を停止 */
            nextHandler.removeCallbacks(NextTimer);
        }
    };

    @Override
	protected void onStop() {
		// TODO 自動生成されたメソッド・スタブ
    	Log.d("main","STOP");
		super.onStop();
		this.deleteHandler.post(CallbackDelete);
		this.quizManager.close();
		this.nextDelete.post(NextDelete);
	}
    
	//order[現在の問題数]に基づいて問題を取得　答えのみanswerに格納
	 public void question(){

		 if(this.q_Index < this.syutudai){
			 
			 DBHelper dbh = new DBHelper(this);
			 SQLiteDatabase db = dbh.getReadableDatabase();

			 Cursor c = db.query(DBHelper.getTableName(), new String[] {"quizcode"}, null,null,null,null,null);
			 this.startManagingCursor(c);
			 int clmIndex = c.getColumnIndex("quizcode");
			 boolean isEof = c.moveToFirst();
			 if(isEof){
				 c.move(this.order[q_Index]);
				 this.quizCode = c.getInt(clmIndex);
				 
				 //quizCodeによりクイズの種類を判別し　対応したインスタンスを実行
				 this.quizManager = this.quizType.get(this.quizCode);
				 this.quizManager.getQuiz(this.order[q_Index]);
				 
			 }
			 
			 dbh.close();
			 
			 //startに現在時刻をセットし　Handlerを作動させ経過時間を表示させる
			 this.timerHandler.postDelayed(CallbackTimer, 100);
			 this.start= System.currentTimeMillis();

		 }else{

			 Intent i = new Intent(this,ResultActivity.class);
			 this.startActivity(i);
			 this.finish();
		 }

	 }

	public void answer(View view){

		//quizManagerにQuizJudgeが入ってる時にタッチ＝次の問題へ
		if(this.quizManager instanceof QuizJudge){
			/*
			this.count = 4;
			this.nextDelete.post(NextDelete);
			TextView tv = (TextView)this.findViewById(R.id.count);
			tv.setText("");
			q_Index++;
			question();
			*/
			
			
		//問題が入っている = QuizAnsweを入れ判定する
		}else if(view != findViewById(R.id.mainLayout)){
			Log.d("aaa","終了");
			this.nextHandler.post(NextTimer);
			this.deleteHandler.post(CallbackDelete);
			this.quizManager.close();
			
			this.quizManager = new QuizJudge(this);
			((QuizJudge)(this.quizManager)).judgement(view);
			
			
		}
	}

	//orderに出題番号を入れる　数字が被ると同じ問題が出てくる点に注意
	public void setOrder(){
		//配列に1～総問題数を入れる
		for(int i=0;i<this.totalQuestion;i++){
			this.order[i] = i;
		}
		
		RandomBox.random(this.order);
		
	}

}
