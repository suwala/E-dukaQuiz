package sample.edukaquizMoza;

import sample.edukaquizMoza.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelectMenuActivity extends Activity{
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        setContentView(R.layout.menu);
        
        
        
       
    }
	
	public void qesS(View view){
		Intent i = new Intent(this,OffLineQuizActivity.class);
		this.startActivityForResult(i,0);
	}

}
