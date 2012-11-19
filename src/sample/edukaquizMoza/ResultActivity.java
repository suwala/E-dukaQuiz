package sample.edukaquizMoza;

import sample.edukaquizMoza.R;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity{
	
	private Handler timerHandler = new Handler();
	private Handler deleteHandler = new Handler();
	public static OAuthAuthorization myOauth;
	public static RequestToken requestToken;
	private long start;
	private final String tweet = "クイズアプリで"+Quiz.getPoint()+"ポイントを獲得！"+" #ictTestQuiz";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        
        
        
        //getStringExtra 渡された追加文字列を受け取る　getStringExtra("keyword") 返り値が渡した文字列ぽい
        //staticでも解決できることが判明そっと見送る
        
        //String data = i.getStringExtra("a_count");

       
        TextView tv = (TextView)findViewById(R.id.textView1);
        tv.setText(Quiz.getPoint()+"POINT獲得！\nあなたの正解数は"+Quiz.getAnswerCount()+"問です！");
        this.start = System.currentTimeMillis();
        this.timerHandler.postDelayed(CallbackTimer, 0);
        
        
    }
	
	private Runnable CallbackTimer = new Runnable() {
		
		@Override
		public void run() {
			// TODO 自動生成されたメソッド・スタブ
			
			timerHandler.postDelayed(this,10);
			
			TextView tv = (TextView)findViewById(R.id.textView1);
			int length = (int)(System.currentTimeMillis()-start)/5;
			if(length >=255){
				deleteHandler.postDelayed(CallbackDelete, 0);
				length = 255;
			}
			tv.setTextColor(Color.argb(length, 0, 0, 0));
			
		}
	};
	
	private Runnable CallbackDelete = new Runnable() {
        public void run() {
            /* コールバックを削除して周期処理を停止 */
            timerHandler.removeCallbacks(CallbackTimer);
        }
    };
	
	public void toSelectMenu(View view){
		Intent i=new Intent(this,SelectMenuActivity.class);
		//遷移先のアクティビティが稼動済みの場合それより上にあるアクティビティをキルする
		//要するに結果画面＞バックキー＞問題の画面に戻るのを防ぐ　finish()してるのでいらなそう
		//i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//良くワカラン調べることi.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		this.startActivity(i);
		this.deleteHandler.postDelayed(CallbackDelete, 0);
	}

	public void tweet(View v){
		
		SharedPreferences pref = getSharedPreferences("twitter", MODE_PRIVATE);

		String oauthToken  = pref.getString("oauth_token", "");
		String oauthTokenSecret = pref.getString("oauth_token_secret", "");

		//Log.d("oauthTOken",oauthToken);
		if(oauthToken.equals("")){
			this.logIn();

		}else{
			Log.d("token",oauthToken+":"+oauthTokenSecret);

			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(TitleActivity.CONSUMER_ID);
			builder.setOAuthConsumerSecret(TitleActivity.CONSUMER_SECRET);
			builder.setOAuthAccessToken(oauthToken);
			builder.setOAuthAccessTokenSecret(oauthTokenSecret);
			Configuration config = builder.build();

			Twitter twitter = new TwitterFactory(config).getInstance();
			try {				
				twitter.updateStatus(this.tweet);
				Toast.makeText(this, "スコアをつぶやきました", Toast.LENGTH_SHORT).show();
				
			} catch (TwitterException e) {
				// TODO 自動生成された catch ブロック
				Toast.makeText(this, "つぶやけませんでした",Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
	}
	
	public void logIn(){
    	
    	ConfigurationBuilder builder = new ConfigurationBuilder();  
        builder.setOAuthConsumerKey(TitleActivity.CONSUMER_ID);
        builder.setOAuthConsumerSecret(TitleActivity.CONSUMER_SECRET);  
        Configuration configuration = builder.build();
        
        myOauth = new OAuthAuthorization(configuration);
        myOauth.setOAuthAccessToken(null);
        try {
        	requestToken = myOauth.getOAuthRequestToken(TitleActivity.CALLBACK_URL);
        } catch (TwitterException e) {
        	// TODO 自動生成された catch ブロック
        	e.printStackTrace();
        }
        Intent intent = new Intent(this,OAuthActivity.class);
        intent.putExtra("auth_url", requestToken.getAuthorizationURL());
        this.startActivityForResult(intent, 0);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO 自動生成されたメソッド・スタブ
		super.onActivityResult(requestCode, resultCode, intent);
		
		//intentがnullの場合もあるので注意！
		if(requestCode == 0 && intent !=null){
    		Log.d("main","main");
    		try {
    			AccessToken accessToken = myOauth.getOAuthAccessToken(requestToken,intent.getExtras().getString("oauth_verifier"));
    			SharedPreferences pref = getSharedPreferences("twitter", MODE_PRIVATE);
    			SharedPreferences.Editor editor = pref.edit();
    			editor.putString("oauth_token", accessToken.getToken());
    			editor.putString("oauth_token_secret", accessToken.getTokenSecret());
    			editor.putString("status", "available");
    			editor.commit();

    			Log.d("result",accessToken.getToken()+":"+accessToken.getTokenSecret());

    		} catch (TwitterException e) {
    			// TODO 自動生成された catch ブロック
    			e.printStackTrace();
    		}
    	}
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO 自動生成されたメソッド・スタブ
    	
    	switch (item.getItemId()) {
		case R.id.menu_settings:
			this.clearPref();
			break;
    	}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO 自動生成されたメソッド・スタブ
		
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.activity_main, menu);
		
		return super.onCreateOptionsMenu(menu);
		
	}
	
	public void clearPref(){
    	
    	SharedPreferences pref = getSharedPreferences("twitter", MODE_PRIVATE);
    	Editor editor = pref.edit();
    	editor.clear().commit();
    	
    }
}
