package sample.edukaquizMoza;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Mosaic_image{
	private Bitmap bmp;
	
	public Mosaic_image(Bitmap image, int dot){
		
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

		this.bmp = b;		
	}
	
	public Bitmap getMosaic(){
		return this.bmp;
	}

}
