package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SongLibController {
	@FXML ListView<String> listView;
	@FXML Button add;
	@FXML Button edit;
	@FXML Button delete;
	@FXML TextField song;
	@FXML TextField artist;
	@FXML TextField album;
	@FXML TextField year;
	
	private ObservableList<String> obsList;
	private SortedList<String> sortedList;
	
	public void start(Stage mainStage){

		//testing purposes. REMOVE WHEN ASAP.
		obsList = FXCollections.observableArrayList(                               
				"That's Life",                               
				"Summer Wind",                               
				"Chicago",
				"Put Your Dreams Away",
				"My Way",
				"My Kind Of Town",
				"The Way You Look Tonight",
				"Fly Me To The Moon",
				"Just In Time",
				"Bold Arrow of Time",
				"Lucidity");
		
		sortedList = obsList.sorted();
		
		listView.setItems(sortedList);
		listView.getSelectionModel().select(0);
		//listener for when the user clicks on a song. Details should show.
		
		listView
		.getSelectionModel()
		.selectedItemProperty()
		.addListener(
				(obs, oldVal, newVal) -> 
				showItem(mainStage)); 
	}
	private void showItem(Stage mainStage) {                
		int index = listView.getSelectionModel().getSelectedIndex();
		
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
			}
			
		}else if(b == delete){
			if(sortedList.size() == 0){
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Song Library");
				alert.setHeaderText("ERROR!");
				alert.setContentText("Cannot delete from an empty library");
				alert.showAndWait();
			}else{
				int index = listView.getSelectionModel().getSelectedIndex();
				obsList.remove(sortedList.get(index));

				if(sortedList.size() != 0){
					if(index+1 > sortedList.size()){
						listView.getSelectionModel().select(index-1);
					}
				}
			}
		}else{
			
		}
	}
}
