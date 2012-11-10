package sample.edukaquizMoza;

public enum QuizCode {
	
	FourSelected(0),Mosaic(1),Scratch(2),marubatu(3),Typing(4),CharaMove(5);
	private int quizCode;
	
	private QuizCode(int code){
		this.quizCode=code;
	}

	public int getCode(){
		return this.quizCode;
	}
}
