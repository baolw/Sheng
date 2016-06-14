package tedu.sheng.entity;


public class SongSerch {
	private String title;
	private String song_id;
	private String author;
	public SongSerch(String title,String song_id,String author){
		this.title=title;
		this.song_id=song_id;
		this.song_id=author;
		
	}
	
	public String getTitle() {
		return title;
	}
	
	
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSong_id() {
		return song_id;
	}
	public void setSong_id(String song_id) {
		this.song_id = song_id;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	

}
