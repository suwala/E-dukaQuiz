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

    

	public boolean q = true;	//画面状態のフラグ　trueだと問題がfalseだと結果が表示されている
    public Integer totalQuestion,q_Index=0;    //DBに登録されている総問題数のカウント　DBのメソッドで解決できる？ q_Index = 現在が難問目かの保持


    public final Integer syutudai = 3; //出題数

    public  Integer[] order;//DBのIndex準拠にした問題の出題順　総問題数をシャッフルする
    public String answer;

    static Integer a_c,miss,point; //正解数のカウント
    public String mondai;
    public Bitmap mosaic;
    public Bitmap mosaic2;
    public Bitmap mosaic3;
    public Bitmap image;
    public int dot;
    public List<QuizManager> quizType =  new ArrayList<QuizManager>();

    private int quizCode;
    private Handler timerHandler = new Handler();
    private Handler deleteHandler = new Handler();
    private long start;
    private final int pbTime = 10000;//ProgressBarの設定タイム

    
    
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);


        QuizManager four = new FourQuiz(this);
        QuizManager mosaic = new MosaicQuiz(this);
        
        this.quizType.add(four);
        this.quizType.add(mosaic);
        
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
        
        //モザイク問題を先頭へthis.order[0] = 6;

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

			//0.1秒につき一文字表示
			int length = (int)(System.currentTimeMillis()-start)/100;
			
			if(quizCode == 1){
				
				
				
				
				/*
				if(start < 10){--------------------------
					mosaic = mosaic_image(bmp, 40);
				}else if(length == 30000){
					mosaic = mosaic_image(bmp, 20);
				}else if(length == 60000){
					mosaic = mosaic_image(bmp, 5);
				}*/
				
				if(length % 10 == 1){
					ImageView iv = (ImageView)findViewById(R.id.mosaic);
					Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.sample1);
					bmp = Bitmap.createScaledBitmap(bmp, 160, 200, true);
				
					int dot = (100-length)/2;
					if(dot != 0){
						bmp = Mosaic_image.mosaic_image(bmp, (100-length)/2);//mosaic_image(bmp, (100-length)/2);
					}
				
					iv.setImageBitmap(bmp);
				}
				/*
				if(length < 30){
					//bmp = mosaic_image(bmp, 40);
					bmp = mosaic;
					
				}else if(length < 60){
					//bmp = mosaic_image(bmp, 20);
					bmp = mosaic2;
				}else{
					//bmp = mosaic_image(bmp, 5);
					bmp = mosaic3;
				}
				
				
				iv.setImageBitmap(bmp);
				*/
			}
			
			

			//0.1秒につき問題文を一文字表示する
			if(length > mondai.length())
				length = mondai.length();
			tv.setText(mondai.subSequence(0, length));




			ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar1);
			pb.setProgress((int)(System.currentTimeMillis()-start));
		}
	};

	private Runnable CallbackDelete = new Runnable() {
        public void run() {
            /* コールバックを削除して周期処理を停止 */
            timerHandler.removeCallbacks(CallbackTimer);
        }
    };

    @Override
	protected void onStop() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStop();
		this.deleteHandler.post(CallbackDelete);
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
				 Log.d("aaa",String.valueOf(this.order[q_Index]));
				 this.quizType.get(this.quizCode).getQuiz(this.order[q_Index]);
				 //quiz.getQuiz(this.order[q_Index]);
				 
				 
			 }
			 
			 dbh.close();
			 
			 //startに現在時刻をセットし　Handlerを作動させ経過時間を表示させる
			 this.start = System.currentTimeMillis();
			 this.timerHandler.postDelayed(CallbackTimer, 100);

		 }else{

			 Intent i = new Intent(this,ResultActivity.class);
			 i.putExtra("score", point);
			 this.startActivity(i);

			 this.finish();
		 }

	 }

	public void answer(View view){


		//qがtrueの時は問題、falseの時は回答が表示されてる　ということにしよう
		//問題表示の時は回答の消去　回答表示の時は問題の表示の2分岐


		if(q){
			//経過時間のストップ
			this.deleteHandler.postDelayed(CallbackDelete, 0);
			Button btn = (Button)view;

			//押したbtnのtextを取得しdbの答えと照合　合否で分岐
			if(btn.getText().toString().equals(this.answer)){

				btn = (Button) findViewById(R.id.button1);
				btn.setText("");
				btn = (Button) findViewById(R.id.button2);
				btn.setText("");
				btn = (Button) findViewById(R.id.button3);
				btn.setText("");
				btn = (Button) findViewById(R.id.button4);
				btn.setText("");

				btn = (Button)view;

				btn.setText("正解！");

				//1秒で正解だと9000P 5秒で正解だと5000Pが入る計算　マイナスは切り上げて0にする
				if(10000-(int)(System.currentTimeMillis() - this.start) > 0)
					point += 10000-(int)(System.currentTimeMillis() - this.start);

				a_c++;
				q=false;
			}else{
				//選択肢を消去
				btn = (Button) findViewById(R.id.button1);
				btn.setText("");
				btn = (Button) findViewById(R.id.button2);
				btn.setText("");
				btn = (Button) findViewById(R.id.button3);
				btn.setText("");
				btn = (Button) findViewById(R.id.button4);
				btn.setText("");


				btn = (Button)view;

				btn.setText("残念！");

				miss++;
				q=false;
			}
		}else{
			this.q_Index++;
			this.question();
			this.q=true;
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
