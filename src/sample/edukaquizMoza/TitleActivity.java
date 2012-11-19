package sample.edukaquizMoza;


import sample.edukaquizMoza.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


public class TitleActivity extends Activity {

	static final String CALLBACK_URL = "http://twitter.com/";
	static final String CONSUMER_ID = "CN1krVYeragTQdJYEm4BA";
	static final String CONSUMER_SECRET = "1DESgiLUiUnvfMnoxO90XZPExIhiJt1cS5IAFbI1w";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
                		
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_main, menu);
//        return true;
//    }

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		// TODO �����������ꂽ���\�b�h�E�X�^�u
//		
//				
//		//�W�������I��(menu)������
//		//Intent i = new Intent(this,sample.quiz.Menu.class);
//		
//		Log.d("aaa","aaa");
//		Intent i = new Intent(this,Question.class);
//		this.startActivityForResult(i, 0);
//
//		return super.onTouchEvent(event);
//		
//		
//	}
	
	public void gnrS(View view) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
				
		//�W�������I��(menu)������
		Intent i = new Intent(this,sample.edukaquizMoza.SelectMenuActivity.class);
		
		//Intent i = new Intent(this,Question.class);
		
		this.startActivityForResult(i, 0);

	}

    

    
}
