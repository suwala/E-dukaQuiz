package sample.edukaquizMoza;

public class RandomBox {
	
	public static void random(Integer[] box){
		for(int i=0;i<box.length;i++){
			int dst = (int)Math.floor(Math.random()*(i+1));
			int j = box[i];
			box[i] = box[dst];
			box[dst] = j;
		}
	}
	
	public static void random(int[] box){
		for(int i=0;i<box.length;i++){
			int dst = (int)Math.floor(Math.random()*(i+1));
			int j = box[i];
			box[i] = box[dst];
			box[dst] = j;
		}
	}
	
	private RandomBox(){
		
	}

}
