package sample.edukaquizMoza;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class JudgeActivity extends Activity {

	private Integer time;
	
	
	//onActivityResultはonResumeの前に発生する
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.judge);

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自動生成されたメソッド・スタブ
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0){
			this.time = data.getIntExtra("time", 0);
		}
	}
	

	@Override
	protected void onResume() {
		// TODO 自動生成されたメソッド・スタブ
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStop();
	}
	
	public void toNext(View v){
		
	}

}
