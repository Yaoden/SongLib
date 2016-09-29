package view;
//Matthew Ya, Taehee Lee	
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import Utility.SongInfo;

public class SongLibController {
	
	@FXML ListView<SongInfo> listView;
	@FXML Button add;
	@FXML Button edit;
	@FXML Button delete;
	@FXML TextField song;
	@FXML TextField artist;
	@FXML TextField album;
	@FXML TextField year;
	@FXML TextArea details;
	
	private ObservableList<SongInfo> obsList;
	private SortedList<SongInfo> sortedSongList;
	
	public void start(){
		
		obsList = FXCollections.observableArrayList();
		sortedSongList = obsList.sorted(new SongInfo());
		
		//when getting items from the file
		/*
		for(int i = 0; i < sortedSongList.size(); i++){
			sortedList.add(sortedSongList.get(i).getSong());
		}
		*/
		listView.getSelectionModel().selectFirst();
		
		//listener for when the user clicks on a song. Details should show.
		listView.setItems(sortedSongList);
		listView
		.getSelectionModel()
		.selectedItemProperty()
		.addListener(
				(obs, oldVal, newVal) -> 
				showItem()); 
	}
	
	//NOTE: ONLY CHANGES THE SONG DETAILS ON ITEM SELECTION BUT NOT WHEN THE ITEM ITSELF IS CHANGED THROUGH THE EDIT BUTTON.
	private void showItem() {                
		int index = listView.getSelectionModel().getSelectedIndex();
		if(index != -1){
			details.setText(sortedSongList.get(index).getInfo());
		}
	}

	//initial method when buttons are pressed. Goes through simple error checking and at the end it parses the obsList into the File
	public void aed(ActionEvent e){
		Button b = (Button)e.getSource();
		
		if(b == add){
			if(song.getText().isEmpty()){
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Song Library");
				alert.setHeaderText("ERROR!");
				alert.setContentText("Song name is required");
				alert.showAndWait();
			}else if(artist.getText().isEmpty()){
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Song Library");
				alert.setHeaderText("ERROR!");
				alert.setContentText("Artist name is required");
				alert.showAndWait();
			}else if(duplicate(song.getText().trim(), artist.getText().trim())){
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Song Library");
				alert.setHeaderText("ERROR!");
				alert.setContentText(song.getText().trim() + " by " + artist.getText().trim() + " already exists in the Song Library!");
				alert.showAndWait();
			}else{
				add();
			}
		}else if(b == delete){
			//error check incase list is empty
			if(sortedSongList.size() == 0){
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Song Library");
				alert.setHeaderText("ERROR!");
				alert.setContentText("Cannot delete from an empty library");
				alert.showAndWait();
			}else if(listView.getSelectionModel().getSelectedIndex() == -1){
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Song Library");
				alert.setHeaderText("ERROR!");
				alert.setContentText("No item selected.");
				alert.showAndWait();
			}else{
				delete();
			}
		}else{
			if(duplicate(song.getText().trim(), artist.getText().trim())){
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Song Library");
				alert.setHeaderText("ERROR!");
				alert.setContentText(song.getText().trim() + " by " + artist.getText().trim() + " already exists in the Song Library!");
				alert.showAndWait();
			}else if(listView.getSelectionModel().getSelectedIndex() == -1){
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Song Library");
				alert.setHeaderText("ERROR!");
				alert.setContentText("No item selected.");
				alert.showAndWait();
			}else{
				edit();
			}
		}
	}
	
	private void add(){
		//create SongInfo object
		SongInfo item = new SongInfo();
		item.setSong(song.getText().trim());
		item.setArtist(artist.getText().trim());
		item.setYear(year.getText().trim());
		item.setAlbum(album.getText().trim());

		//adding them into their corrsponding obsList
		obsList.add(item);
		
		listView.getSelectionModel().select(sortedSongList.indexOf(item));
		//clear textboxes
		song.clear();
		artist.clear();
		year.clear();
		album.clear();
		
		
	}
	
	private void delete(){
		int index = listView.getSelectionModel().getSelectedIndex();
		obsList.remove(sortedSongList.get(index));
		if(sortedSongList.size() != 0){
			if(index+1 > sortedSongList.size()){
				listView.getSelectionModel().selectPrevious();
			}
		}else{
			listView.getSelectionModel().clearSelection();
			details.clear();
		}
	}

	private void edit(){
		
		//index of the selected item
		int index = listView.getSelectionModel().getSelectedIndex(); 	
		//obtains the corresponding song info in the unsorted song info list
		int songinfoindex = obsList.indexOf(sortedSongList.get(index));
		
		
		//creating a new object for the new edit because the SortedList class sorts the List IF*** a new item as added to
		//the obsList and not when the fields in the obsList items are edited.
		SongInfo item = new SongInfo();
		if(!song.getText().isEmpty()){
			item.setSong(song.getText().trim());
		}else{
			item.setSong(sortedSongList.get(index).getSong());
		}
		if(!artist.getText().isEmpty()){
			item.setArtist(artist.getText().trim());
		}else{
			item.setArtist(sortedSongList.get(index).getArtist());
		}
		if(!album.getText().isEmpty()){
			item.setAlbum(album.getText().trim());
		}else{
			item.setAlbum(sortedSongList.get(index).getAlbum());
		}
		if(!year.getText().isEmpty()){
			item.setYear(year.getText().trim());
		}else{
			item.setYear(sortedSongList.get(index).getYear());
		}
		
		obsList.set(songinfoindex, item);
		details.setText(obsList.get(songinfoindex).getInfo());
		
		song.clear();
		artist.clear();
		year.clear();
		album.clear();
		
		
	}
	
	private boolean duplicate(String song, String artist){
		for(int i = 0; i < obsList.size(); i++){
			if(obsList.get(i).getSong().equals(song) && obsList.get(i).getArtist().equals(artist)){
				return true;
			}
		}
		return false;
	}
}
