package sample.edukaquizMoza;

public enum QuizGenre {
	
	History("歴史"),Zatsugaku("雑学"),E_duka("飯塚"),Learning("学問"),GeAni("ゲーム＆アニメ"),Geography("地理");
	
	private String genre;
	
	private QuizGenre(String genre){
		this.genre = genre;
	}
	
	public String getGenre(){
		return this.genre;
	}

}
