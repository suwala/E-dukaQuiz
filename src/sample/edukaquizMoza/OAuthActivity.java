package sample.edukaquizMoza;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

public class OAuthActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oauthl);
		
		WebView webView = new WebView(this);
		WebSettings webSettings = webView.getSettings();
		//パスワード保存の非表示
		webSettings.setSavePassword(false);
		
		
		
		LinearLayout _ll = (LinearLayout)this.findViewById(R.id.layout01);
    	_ll.addView(webView);
		
    	webView.setWebViewClient(new WebViewClient(){
    		
    		// ページ描画完了時に呼ばれる。
    		public void onPageFinished(WebView view, String url) {
    			super.onPageFinished(view, url);
    			
    			//TwitterDevelopersで登録したCallbackURLが戻ってくるので　URLが一致したとき認証が成功したものとする
    			if(url != null && url.startsWith(TitleActivity.CALLBACK_URL)){
    				//URLパラメータを分解する
    				 String[] urlParameters = url.split("\\?")[1].split("&");
    				 String oauthToken = "";
    				 String oauthVerifier = "";

    				 // oauth_tokenをURLパラメータから切り出す。
    				 if(urlParameters[0].startsWith("oauth_token")){
    					 oauthToken = urlParameters[0].split("=")[1];
    				 }else if(urlParameters[1].startsWith("oauth_token")){
    					 oauthToken = urlParameters[1].split("=")[1];
    				 }
    				 
    				 // oauth_verifierをURLパラメータから切り出す。
    				 if(urlParameters[0].startsWith("oauth_verifier")){
    					 oauthVerifier = urlParameters[0].split("=")[1];
    				 }else if(urlParameters[1].startsWith("oauth_verifier")){
    					 oauthVerifier = urlParameters[1].split("=")[1];
    				 }
    				 
    				 Intent intent = getIntent();
    				 intent.putExtra("oauth_token", oauthToken);
    				 intent.putExtra("oauth_verifier", oauthVerifier);

    				 setResult(0,intent);
    				 finish();
    			}
    		}
		});
    	webView.loadUrl(this.getIntent().getExtras().getString("auth_url"));
	}
}
