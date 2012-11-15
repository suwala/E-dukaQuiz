package sample.edukaquizMoza;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class Mosaic_image{
	
	public static Bitmap mosaic_image(Bitmap image, int dot){
		
		Bitmap b = image.copy(Bitmap.Config.ARGB_8888, true);

		//int dot = 8;

		dot = dot == 0?1:dot;
		
		Log.d("dot",String.valueOf(b.getWidth())+":"+String.valueOf(b.getHeight()));
		
		for (int i = 0; i < b.getWidth() / dot+1; i++) {
			for (int j = 0; j < b.getHeight() / dot+1; j++) {

				// �h�b�g�̒��̕��ϒl���g��
				int rr = 0;
				int gg = 0;
				int bb = 0;
				for (int k = 0; k < dot; k++) {
					if(i*dot+k >= b.getWidth())
						break;	
					for (int l = 0; l < dot; l++) {
						
						if(j*dot+l >= b.getHeight())
							break;
						
						//int dotColor = image.getPixel(i *dot + k, j * dot + l);
						int x =  i *dot+(int)(Math.random()*dot);
						int y = j *dot +(int)(Math.random()*dot);
						
						if(x >= b.getWidth())
							x = b.getWidth()-1;
						if(y >= b.getHeight())
							y = b.getHeight()-1;
						
						//Log.d("dot",String.valueOf(k)+":"+String.valueOf(l));
						int dotColor = image.getPixel(x ,y);
						//int dotColor = image.getPixel(i * dot + k, j * dot + l);
						rr += Color.red(dotColor);
						gg += Color.green(dotColor);
						bb += Color.blue(dotColor);
					}
						
				}
				rr = rr / (dot * dot);
				gg = gg / (dot * dot);
				bb = bb / (dot * dot);

				for (int k = 0; k < dot; k++) {
					if(i*dot+k >= b.getWidth())
						break;	
					for (int l = 0; l < dot; l++) {
						if(j*dot+l >= b.getHeight())
							break;
						b.setPixel(i * dot + k, j * dot + l, Color.rgb(rr, gg, bb));
					}
				}
			}
		}

		return b;		
	}

}
