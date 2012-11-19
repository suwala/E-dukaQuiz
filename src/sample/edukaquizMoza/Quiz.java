package sample.edukaquizMoza;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Rect;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.MaskFilterSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class Quiz {
	
	//dbh.close()はどのタイミングで呼ぶべきか
	
	static Activity activity;
	static DBHelper dbh;
	static Cursor c;
	public int offset;
	//同一パケからも可視出来るのであまり意味ナシ
	protected static long startTime;
	protected static Integer point=0; 
	protected static String answer;
	protected static int answerCount = 0;
	private String mondai;
	private Boolean state;//trueが出題画面　falseが判定画面
	
	abstract void start();
	abstract void close();
	abstract void stop();
	abstract void individualSetup();
		
	public long getStartTime(){
		return startTime;
	}
	
	public static int getPoint(){
		return point;
	}
	
	public static int getAnswerCount(){
		return answerCount;
	}
	
	public static void resetResult(){
		answerCount =0;
		point = 0;
	}
	
	public void setMainActiviti(Activity activity){
		Quiz.activity = (OffLineQuizActivity)activity;
	}
	
	public Boolean isQuestion(){
		return state;		
	}
	
	public void setupQuiz(Integer offset){

		ImageView iv = (ImageView)activity.findViewById(R.id.image_point);
		iv.setImageBitmap(null);
		this.state = true;
		dbh = new DBHelper(activity);
		this.offset = offset;
		SQLiteDatabase db = dbh.getReadableDatabase();

		c = db.query(DBHelper.getTableName(), new String[] {"question","answer","dummy1","dummy2","dummy3","image"}, null,null,null,null,null);
		activity.startManagingCursor(c);
		//ハマリ　c.moveはオフセット現在地からの移動のためtoFirstで最初の地点へ戻す必要があった
		boolean isEof = c.moveToFirst();

		c.move(offset);
		if(isEof){
			
			//問題の取得
			TextView tv = (TextView)activity.findViewById(R.id.quetion);
			tv.setGravity(Gravity.LEFT);
			
			int clmIndex = c.getColumnIndex("question");
			this.mondai = c.getString(clmIndex);

			Integer[] select = {1,2,3,4};
			BoxShuffle.shuffle(select);

			clmIndex = c.getColumnIndex("answer");
			//this.offLineActiviy.answer = c.getString(clmIndex);
			answer = c.getString(clmIndex);

			for(int i=0;i<select.length;i++){
				tv = (TextView)activity.findViewById(activity.getResources().getIdentifier("button"+select[i].toString(), "id", activity.getPackageName()));
				tv.setText(c.getString(clmIndex));
				tv.setTextColor(Color.BLACK);
				clmIndex = c.getColumnIndex("dummy"+String.valueOf(i+1));
			}
			this.individualSetup();
		}

		dbh.close();
		
		//startに現在時刻をセット
		startTime = System.currentTimeMillis();
	}
	
	
	public String getQuestion(){
		return this.mondai;
	}
	
	//ジャッジメントですの！
	public void judgement(View v){
		this.state =false;
		Button btn;
		String text;
		if(v != null){
			btn=(Button)v;
			text = btn.getText().toString();
		}else{
			text = "timeOut";
		}
		
		btn = (Button) activity.findViewById(R.id.button1);
		btn.setText("");
		btn = (Button) activity.findViewById(R.id.button2);
		btn.setText("");
		btn = (Button) activity.findViewById(R.id.button3);
		btn.setText("");
		btn = (Button) activity.findViewById(R.id.button4);
		btn.setText("");
		
		TextView tv = (TextView)activity.findViewById(R.id.quetion);
		ImageView iv = (ImageView)activity.findViewById(R.id.image_point);
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(30.0f);
		
		//押したbtnのtextを取得しdbの答えと照合　合否で分岐
		if(text.equals(answer)){

			
			btn = (Button)v;
			btn.setText(R.string.maru);
			btn.setTextColor(Color.RED);
			int getPoint=0;

			//1秒で正解だと9000P 5秒で正解だと5000Pが入る計算　マイナスは切り上げて0にする
			if(10000-(int)(System.currentTimeMillis() - startTime) > 0)
				getPoint = 10000-(int)(System.currentTimeMillis() - startTime)-100;

			//正解時にも100P付与
			point += getPoint+100;
			
			tv.setTextColor(Color.RED);
			this.setTextType(tv,"正解！");
			//tv.setText("正解！"+point+"ポイント獲得");
			
			
			iv.setImageBitmap(this.createPointImage(getPoint+100));
			TranslateAnimation translate = new TranslateAnimation(0,0,100,0);
			translate.setDuration(1000);
			iv.startAnimation(translate);
			
			answerCount++;
		}else if(v != null){			
			//this.setTextType(tv,"残念！");
			//tv.setText("残念!");
			
			tv.setTextColor(Color.BLUE);
			this.setTextType(tv, "残念！");
			btn = (Button)v;
			btn.setText("×");
		}else{
			//tv.setText("タイムアップ");
			tv.setTextColor(Color.BLUE);
			this.setTextType(tv, "タイムアップ!");
		}
	}
	
	private void setTextType(TextView tv,String str){
	
		
		// 光源（x, y, z）の設定値。
        float[] direction = { 2.0f, 2.0f, 2.0f };
        
        // 環境光の設定値。
        float ambient = 0.5f;
        
        // 明暗の設定値。
        float specular = 9.0f;
        
        // ぼかしの設定値。
        float blurRadius = 3.0f;
        
        // エンボス（浮き出し）フィルタのインスタンスを作成。
        EmbossMaskFilter filter = new EmbossMaskFilter(direction, ambient, specular, blurRadius);
        
        // SPAN の作成。
        MaskFilterSpan span = new MaskFilterSpan(filter);
        
        // エンボスを掛けるテキストを用意。
        SpannableString spannable = new SpannableString(str);
        
        // テキストに SPAN を挿入。
        spannable.setSpan(span, 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setText(spannable);

	}
	

	public Bitmap createPointImage(Integer point){
		
		View view = activity.findViewById(R.id.frameLayout);
		view = activity.findViewById(R.id.mosaic);
		
		int width = view.getWidth();
		int height = view.getHeight();
		
		Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		
		//リサイズのOFF trueだとBitmapFactoryは自動でリサイズしてくれる
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inScaled = false;
		Bitmap original = BitmapFactory.decodeResource(activity.getResources(), R.drawable.number,opt);
		
		Log.d("originalSize",String.valueOf(original.getWidth())+":"+String.valueOf(original.getHeight()));
		
		
		int scaleSize = 3;
		
		Bitmap plus = BitmapFactory.decodeResource(activity.getResources(), R.drawable.plus,opt);
		canvas.drawBitmap(plus, new Rect(0,0,plus.getWidth(),plus.getHeight()), new Rect(0,0,plus.getWidth()*scaleSize,plus.getHeight()*scaleSize),null);
		
		int imageWight = original.getWidth();
		int imageHight = original.getHeight()/10;
		
		String strPoint = point.toString();
		Log.d("getPoint",strPoint);
		for(int i=0;i<strPoint.length();i++){
			//一文字ずつintに変換
			Log.d("getPoint2",String.valueOf(strPoint.charAt(i)));
			int offSet = new Integer(strPoint.charAt(i) - '0');
			
			if(offSet==0)
				offSet = 10;
			offSet = original.getHeight() - offSet*imageHight;
			int x = plus.getWidth()*scaleSize+imageWight*i*scaleSize;
			
			canvas.drawBitmap(original, new Rect(0,offSet,imageWight,offSet+imageHight), new Rect(x,0,x+imageWight*scaleSize,imageHight*scaleSize),null);
			Log.d(String.valueOf("Quiz#createPI"+i),String.valueOf(strPoint.charAt(i)));
		}
		return bmp;
	}

}
