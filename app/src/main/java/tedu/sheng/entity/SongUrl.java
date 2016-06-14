package tedu.sheng.entity;

import java.io.Serializable;


public class SongUrl implements Serializable{
	private  String show_link;
	private  String file_size;
	private  String file_bitrate;
	public String getShow_link() {
		return show_link;
	}
	public void setShow_link(String show_link) {
		this.show_link = show_link;
	}
	public String getFile_size() {
		return file_size;
	}
	public void setFile_size(String file_size) {
		this.file_size = file_size;
	}
	public String getFile_bitrate() {
		return file_bitrate;
	}
	public void setFile_bitrate(String file_bitrate) {
		this.file_bitrate = file_bitrate;
	}
	
	
}
