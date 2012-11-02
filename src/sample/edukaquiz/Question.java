package sample.edukaquiz;

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

public class Question extends Activity{

    public boolean q = true;	//画面状態のフラグ　trueだと問題がfalseだと結果が表示されている
    public Integer questions,q_Index=0;    //DBに登録されている総問題数のカウント　DBのメソッドで解決できる？ q_Index = 現在が難問目かの保持


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

    private int mflg;
    private Handler timerHandler = new Handler();
    private Handler deleteHandler = new Handler();
    private long start;
    private final int pbTime = 10000;//ProgressBarの設定タイム

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);


        DBHelper dbh = new DBHelper(this);
        SQLiteDatabase db = dbh.getReadableDatabase();
        String sql = "SELECT COUNT(*) from "+DBHelper.getTableName();

        //rawQueryは生のSQL文を使える　簡単！
        Cursor cursor = db.rawQuery(sql,null);

        //cursorの自動クローズモード？カラムindexを元に全問題数をゲット *の場合のカラム名が不明
        this.startManagingCursor(cursor);
        //Integer index  = cursor.getColumnIndex("*");
        cursor.moveToFirst();
        this.questions = cursor.getInt(0);

        Log.d("oncre",String.valueOf(this.questions));//総問題数の一致確認
        dbh.close();

        this.order= new Integer[questions];
        //出題順の決定
        this.setOrder();

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

			TextView tv = (TextView)findViewById(R.id.quetions);

			int length = (int)(System.currentTimeMillis()-start)/100;
			//if(dot > 5){
			ImageView iv = (ImageView)findViewById(R.id.quetions2);
			//Bitmap mosaic = mosaic_image(image,dot);
			//dot = dot-3;
			Log.d(Integer.toString(length),"log");
			if(length > 35 && length<70 ){
				iv.setImageBitmap(mosaic2);
			}else if(length > 70){
				iv.setImageBitmap(mosaic3);
			}else{
				iv.setImageBitmap(mosaic);
			}

			//}

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

    public Bitmap mosaic_image(Bitmap image, int dot){


		Bitmap b = image.copy(Bitmap.Config.ARGB_8888, true);

		//int dot = 8;

		for (int i = 0; i < b.getWidth() / dot; i++) {
			for (int j = 0; j < b.getHeight() / dot; j++) {
				int color = image.getPixel(i, j);

				// �h�b�g�̒��̕��ϒl���g��
				int rr = 0;
				int gg = 0;
				int bb = 0;
				for (int k = 0; k < dot; k++) {
					for (int l = 0; l < dot; l++) {
						int dotColor = image.getPixel(i * dot + k, j * dot + l);
						rr += Color.red(dotColor);
						gg += Color.green(dotColor);
						bb += Color.blue(dotColor);
					}
				}
				rr = rr / (dot * dot);
				gg = gg / (dot * dot);
				bb = bb / (dot * dot);

				for (int k = 0; k < dot; k++) {
					for (int l = 0; l < dot; l++) {
						b.setPixel(i * dot + k, j * dot + l, Color.rgb(rr, gg, bb));
					}
				}
			}
		}

		return b;
    }


	//order[現在の問題数]に基づいて問題を取得　答えのみanswerに格納
	 public void question(){

		 if(this.q_Index < this.syutudai){


			 //ハマリ　c.moveはオフセット現在地からの移動のためtoFirstで最初の地点へ戻す必要があった

			 DBHelper dbh = new DBHelper(this);
			 SQLiteDatabase db = dbh.getReadableDatabase();

			 Cursor c = db.query(DBHelper.getTableName(), new String[] {"question","answer","dummy1","dummy2","dummy3","image","mflg"}, null,null,null,null,null);
			 this.startManagingCursor(c);
			 int clmIndex;

			 Log.d("question",String.valueOf(this.q_Index));
			 boolean isEof = c.moveToFirst();
			 if(isEof){
				 //問題の取得

				 c.move(this.order[this.q_Index]);
				 Log.d("question",String.valueOf(this.order[this.q_Index]));

				 TextView tv = (TextView)findViewById(R.id.quetions);

				 //preppy
				 this.mosaic = null;
				 this.mosaic2 = null;
				 this.mosaic3 = null;
				 clmIndex = c.getColumnIndex("mflg");
				 this.mflg = c.getInt(clmIndex);
				 if(this.mflg==1){
					 clmIndex = c.getColumnIndex("image");
					 Resources r = getResources();
					 //c.getString(clmIndex)
					 this.image = BitmapFactory.decodeResource(r, R.drawable.sample1);
					 //this.dot = 15;
					 this.mosaic = mosaic_image(image,40);
					 this.mosaic2 = mosaic_image(image,30);
					 this.mosaic3 = mosaic_image(image,18);
				 }
				 //preppy
				 clmIndex = c.getColumnIndex("question");

				 //tv.setText(c.getString(clmIndex));
				 this.mondai = c.getString(clmIndex);

				 Integer[] select = this.randamSelect();
				 clmIndex = c.getColumnIndex("answer");
				 this.answer = c.getString(clmIndex);

				 for(int i=0;i<select.length;i++){
					 tv = (TextView)this.findViewById(this.getResources().getIdentifier("button"+select[i].toString(), "id", this.getPackageName()));
					 tv.setText(c.getString(clmIndex));
					 clmIndex = c.getColumnIndex("dummy"+String.valueOf(i+1));
				 }
			 }
			 dbh.close();

			 //startに現在時刻をセットし　Handlerを作動させ経過時間を表示させる
			 this.start = System.currentTimeMillis();
			 this.timerHandler.postDelayed(CallbackTimer, 100);

		 }else{

			 Intent i = new Intent(this,Result.class);
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
		for(int i=0;i<this.questions;i++){
			this.order[i] = i;
		}

		for(int i=0;i<this.order.length;i++){
			int dst = (int)Math.floor(Math.random()*(i+1));
			this.swap(this.order,i,dst);
		}
	}

	public void swap(Integer[] box,int _i,int _dst){
		int j = box[_i];
		box[_i] = box[_dst];
		box[_dst] = j;
	}

	public Integer[] randamSelect(){
		Integer[] select = {1,2,3,4};

		for(int i=0;i<select.length;i++){
			int dst = (int)Math.floor(Math.random()*(i+1));
			this.swap(select, i, dst);
		}
		return select;
	}
}
