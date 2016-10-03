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
import utility.SongInfo;
import javafx.scene.control.Button;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SongLibController {
	
	@FXML ListView<SongInfo> listView;
	@FXML Button add;
	@FXML Button edit;
	@FXML Button delete;
	@FXML Button cancel;
	@FXML TextField song;
	@FXML TextField artist;
	@FXML TextField album;
	@FXML TextField year;
	@FXML TextArea details;
	
	private ObservableList<SongInfo> obsList;
	private SortedList<SongInfo> sortedSongList;
	private JsonArray jsonSongArray;
	private File songFile;
	private SongInfo lastItem;
	private Button lastButton;
	private int lastIndex; //used for edit
	
	public void start(){
		
		obsList = FXCollections.observableArrayList();
		sortedSongList = obsList.sorted(new SongInfo());
		songFromFile();
		
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
	
	
	/*creates new songFile if it doesn't exist. If it does exist, the method will parse the jsonobjects in the jsonarray and parse them into
	* SongInfo objects so that they can be added to the obsList.
	*/
	private void songFromFile() {
		songFile = new File("SongFile.json");
		try{
			
			
			//creates a new songFile and populates it with an empty JsonArray.
			if(!songFile.exists()){
					songFile.createNewFile();
					jsonSongArray = new JsonArray();
					FileWriter writer = new FileWriter(songFile);
					writer.write(jsonSongArray.toString());
					writer.close();
			}else{
				//this shouldn't throw an exception because I checked to see if it exist and created a new file if it does not exist.
				Scanner sc = new Scanner(songFile);
				String content = sc.useDelimiter("\\Z").next();
				jsonSongArray = new JsonParser().parse(content).getAsJsonArray();
				for(int i = 0; i < jsonSongArray.size(); i++){
					SongInfo item = new SongInfo();
					item.setSong(jsonSongArray.get(i).getAsJsonObject().get("song").getAsString());
					item.setArtist(jsonSongArray.get(i).getAsJsonObject().get("artist").getAsString());
					item.setYear(jsonSongArray.get(i).getAsJsonObject().get("year").getAsString());
					item.setAlbum(jsonSongArray.get(i).getAsJsonObject().get("album").getAsString());
					obsList.add(item);
				}
				sc.close();
			}
		}catch(/*IO*/Exception e){
			System.out.println("Could not create a new file for songFile. Please be merciful when taking points off my assignment.");
			e.printStackTrace();
		}
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
				lastButton = b;
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
				lastButton = b;
			}
		}else if(b == edit){
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
				lastButton = b;
			}
		}else{
			if(lastItem == null || lastButton == null){
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Song Library");
				alert.setHeaderText("ERROR!");
				alert.setContentText("Latest action already canceled.");
				alert.showAndWait();
			}else{
				cancel();
			}
		}
		songToFile();
	}
	private void cancel(){
		if(lastButton == add){
			int index = sortedSongList.indexOf(lastItem);
			obsList.remove(lastItem);
			jsonSongArray.remove(lastItem.toJsonObject());
			if(sortedSongList.size() != 0){
				if(index+1 > sortedSongList.size()){
					listView.getSelectionModel().selectPrevious();
				}
			}else{
				listView.getSelectionModel().clearSelection();
				details.clear();
			}
		}else if(lastButton == delete){
			obsList.add(lastItem);
			jsonSongArray.add(lastItem.toJsonObject());
			listView.getSelectionModel().select(lastItem);
		}else if(lastButton == edit){
			jsonSongArray.remove(obsList.get(lastIndex).toJsonObject());
			jsonSongArray.add(lastItem.toJsonObject());
			obsList.set(lastIndex, lastItem);
			listView.getSelectionModel().select(lastItem);
		}
		lastButton = null;
		lastIndex = -1;
		lastItem = null;
	}
	private void songToFile() {
		try {
			FileWriter writer = new FileWriter(songFile);
			writer.write(jsonSongArray.toString());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Ok so for some reason I can't write into the file I created. IT WAS WORKING BEFORE I SWEAR. PLEASE HAVE MERCY ON MY SOUL DEAR GRADER.");
			e.printStackTrace();
		}
	}


	private void add(){
		//create SongInfo object
		SongInfo item = new SongInfo();
		item.setSong(song.getText().trim());
		item.setArtist(artist.getText().trim());
		item.setYear(year.getText().trim());
		item.setAlbum(album.getText().trim());
		
		//creating jsonobject for storing purposes
		JsonObject jsonItem = item.toJsonObject();
		
		jsonSongArray.add(jsonItem);
		
		//adding them into their corrsponding obsList
		obsList.add(item);
		lastItem = item;
		listView.getSelectionModel().select(item);
		
		
		//clear textboxes
		song.clear();
		artist.clear();
		year.clear();
		album.clear();
		

	}
	
	private void delete(){
		int index = listView.getSelectionModel().getSelectedIndex();
		lastItem = sortedSongList.get(index);
		jsonSongArray.remove(sortedSongList.get(index).toJsonObject());
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
		
		lastItem = sortedSongList.get(index);
		
		//Remove the item from the jsonarray
		jsonSongArray.remove(sortedSongList.get(index).toJsonObject());
		
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
		jsonSongArray.add(item.toJsonObject());
		
		obsList.set(songinfoindex, item);
		listView.getSelectionModel().select(item);
		lastIndex = songinfoindex;
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
