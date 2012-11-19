package sample.edukaquizMoza;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sample.edukaquizMoza.R;
import android.annotation.SuppressLint;
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

public class OffLineQuizActivity extends Activity{

    

	
    public Integer q_Index=0;    //現在が難問目かの保持
    public final Integer totalQuestions = 3; //出題数
    public  Integer[] order;//DBのIndex準拠にした問題の出題順

    
    public int count=4;
    public Quiz quizManager;
    
    private final int QUESTION_SPEED = 150;//問題文の進行速度
    private int quizCode;
    private Handler pbHandler = new Handler();
    private Handler deletePbHandler = new Handler();
    private Handler nextHandler = new Handler();
    private Handler nextDelete = new Handler();
    private final int PBTime = 10000;//ProgressBarの設定タイム    
    
	private static Map<Integer, Quiz> quizType = new HashMap<Integer, Quiz>();
    static{
    	OffLineQuizActivity.quizType.put(QuizCode.FourSelected.getCode(), new QuizFour());
    	OffLineQuizActivity.quizType.put(QuizCode.Mosaic.getCode(),new QuizMosaic());
    }
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);
        
        Quiz.resetResult();
        
        DBHelper dbh = new DBHelper(this);
        SQLiteDatabase db = dbh.getReadableDatabase();
        String sql = "SELECT COUNT(*) from "+DBHelper.getTableName();

        Integer total;
        //rawQueryは生のSQL文を使える　簡単！
        Cursor cursor = db.rawQuery(sql,null);

        //cursorの自動クローズモード？カラムindexを元に全問題数をゲット *の場合のカラム名が不明
        this.startManagingCursor(cursor);
        //Integer index  = cursor.getColumnIndex("*");
        cursor.moveToFirst();
        total = cursor.getInt(0);

        Log.d("oncre総問題数",String.valueOf(total));//総問題数の一致確認
        dbh.close();

        
        this.order= new Integer[total];
        //出題順の決定　配列に0～総問題数を入れる
		for(int i=0;i<total;i++){
			this.order[i] = i;
		}
		//配列をシャッフルする
		BoxShuffle.shuffle(this.order);		        
        
        //モザイク問題を先頭へ
        this.order[0] = 6;this.order[1] = 16;
    

        //経過時間の設定　現在MAX10秒
        ProgressBar pb = (ProgressBar)this.findViewById(R.id.progressBar1);
        pb.setMax(this.PBTime);
  
        this.question();
        
    }

	@Override
	protected void onResume() {
		// TODO 自動生成されたメソッド・スタブ
		super.onResume();
		this.pbHandler.postDelayed(progressTimer, 100);
		this.quizManager.start();
		
	}

	@Override
	protected void onStop() {
		// TODO 自動生成されたメソッド・スタブ
    	Log.d("main","STOP");
		super.onStop();
		this.deletePbHandler.post(progressDelete);
		this.quizManager.stop();
		this.nextDelete.post(NextDelete);
	}
	
	private Runnable progressTimer = new Runnable() {

		
		public void run() {

			if(quizManager.isQuestion()){

				Log.d("Main_progressTimer_Run","run");
				long start = quizManager.getStartTime();
				String mondai = quizManager.getQuestion();
				// TODO 自動生成されたメソッド・スタブ
				pbHandler.postDelayed(this, 10);

				TextView tv = (TextView)findViewById(R.id.quetion);
				tv.setTextSize(16);
				tv.setTextColor(Color.BLACK);

				//0.1秒につき一文字表示
				int length = (int)(System.currentTimeMillis()-start)/QUESTION_SPEED;


				//0.1秒につき問題文を一文字表示する
				if(length > mondai.length())
					length = mondai.length();
				tv.setText(mondai.subSequence(0, length));

				ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar1);
				pb.setProgress((int)(System.currentTimeMillis()-start));
				//時間切れ
				if((int)(System.currentTimeMillis()-start) > 10000){
					deletePbHandler.post(progressDelete);
					Log.d("PBTimer","KILL");
					answer(null);
				}
			}
		}
	};

	private Runnable progressDelete = new Runnable() {
        public void run() {
            /* コールバックを削除して周期処理を停止 */
            pbHandler.removeCallbacks(progressTimer);
        }
    };
    
    private Runnable NextTimer = new Runnable(){
    	
    	private int count=3;
    	
		@Override
		public synchronized void run() {
			// TODO 自動生成されたメソッド・スタブ
			
			TextView tv = (TextView)findViewById(R.id.count);
			if(count == 0){
				
				tv.setText("");
				q_Index++;
				question();
				nextDelete.post(NextDelete);
				count = 3;
				
			}else{

				tv.setText("Next To"+count);
				count--;
			}
			
			nextHandler.postDelayed(NextTimer, 1000);
			
		}
    	
    };
    
    private Runnable NextDelete = new Runnable() {
        public void run() {
            /* コールバックを削除して周期処理を停止 */
            nextHandler.removeCallbacks(NextTimer);
        }
    };

    
    
	//order[現在の問題数]に基づいて問題を取得　答えのみanswerに格納
	 public void question(){

		 if(this.q_Index < this.totalQuestions){
			 
			 DBHelper dbh = new DBHelper(this);
			 SQLiteDatabase db = dbh.getReadableDatabase();

			 Cursor c = db.query(DBHelper.getTableName(), new String[] {"quizcode"}, null,null,null,null,null);
			 this.startManagingCursor(c);
			 int clmIndex = c.getColumnIndex("quizcode");
			 boolean isEof = c.moveToFirst();
			 if(isEof){
				 c.move(this.order[q_Index]);
				 this.quizCode = c.getInt(clmIndex);
				 
				 //hashMapによりクイズの種類を判別し　対応したインスタンスを実行
				 this.quizManager = quizType.get(this.quizCode);
				 this.quizManager.setMainActiviti(this);
				 this.quizManager.setupQuiz(this.order[q_Index]);
				 
			 }
			 
			 dbh.close();
			 
			 //Handlerで経過時間を表示させる
			 this.pbHandler.postDelayed(progressTimer, 100);

		 }else{

			 Intent i = new Intent(this,ResultActivity.class);
			 this.startActivity(i);
			 this.finish();
			 
		 }

	 }

	public void answer(View view){

		if(this.quizManager.isQuestion()){
			/*
			this.count = 4;
			this.nextDelete.post(NextDelete);
			TextView tv = (TextView)this.findViewById(R.id.count);
			tv.setText("");
			q_Index++;
			question();
			*/
			Log.d("button","判定");
			//this.nextHandler.post(NextTimer);
			this.deletePbHandler.post(progressDelete);
			this.quizManager.close();
			this.quizManager.judgement(view);
			
			this.count = 4;
			this.nextHandler.post(NextTimer);
		}
		/*
		//quizManagerにQuizJudgeが入ってる時にタッチ＝次の問題へ
		if(this.quizManager.isQuestion()){
			/*
			this.count = 4;
			this.nextDelete.post(NextDelete);
			TextView tv = (TextView)this.findViewById(R.id.count);
			tv.setText("");
			q_Index++;
			question();
			*/
		
		/*
			Log.d("button","判定");
			//this.nextHandler.post(NextTimer);
			this.deletePbHandler.post(progressDelete);
			this.quizManager.close();
			this.quizManager.judgement(view);
			
			this.count = 4;
			this.nextHandler.post(NextTimer);
		}else{
			this.count = 4;
			this.nextDelete.post(NextDelete);
			TextView tv = (TextView)this.findViewById(R.id.count);
			tv.setText("");
			q_Index++;
			question();			
		}*/
	}

}
