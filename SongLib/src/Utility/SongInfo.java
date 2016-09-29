package Utility;
//Matthew Ya, Taehee Lee	
import java.util.Comparator;

public class SongInfo implements Comparator<SongInfo>{
	private String song;
	private String artist;
	private String year;
	private String album;
	public SongInfo(){

	}
	public String getSong(){
		return this.song;
	}
	public String getArtist(){
		return this.artist;
	}
	public String getYear(){
		return this.year;
	}
	public String getAlbum(){
		return this.album;
	}
	public void setSong(String song){
		this.song = song;
	}
	public void setArtist(String artist){
		this.artist = artist;
	}
	public void setYear(String year){
		this.year = year;
	}
	public void setAlbum(String album){
		this.album = album;
	}
	public String toString(){
		return "Song: "+ this.song + "\n" + "Artist: " + this.artist + "\n"   + "Album: " + this.album + "\n" + "Year: " + this.year + "\n";
	}
	public int compare(SongInfo first, SongInfo second){
		if(first.getSong().equals(second.getSong())){
			return first.getArtist().compareTo(second.getArtist());
		}
		return first.getSong().compareTo(second.getSong());
	}
}
