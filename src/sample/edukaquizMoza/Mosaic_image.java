package sample.edukaquizMoza;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Mosaic_image{
	
	public static Bitmap mosaic_image(Bitmap image, int dot){
		
		Bitmap b = image.copy(Bitmap.Config.ARGB_8888, true);

		//int dot = 8;

		dot = dot == 0?1:dot;
		
		for (int i = 0; i < b.getWidth() / dot+1; i++) {
			for (int j = 0; j < b.getHeight() / dot+1; j++) {
				int color = image.getPixel(i, j);

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
