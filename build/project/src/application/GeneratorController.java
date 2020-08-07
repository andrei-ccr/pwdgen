package application;


import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;


public class GeneratorController {
	
	private int pwdLength = 16;
	
	private Generator generator;
	private Manager manager;
	
	private String hiddenPass;
	
	@FXML
	private Button btnGenerate;
	
	@FXML
	private Button btnSave;
	
	@FXML
	private TextField tfPassword;
	
	@FXML
	private Label labelPwdLength;
	
	@FXML
	private Slider sliderPwdLength;
	
	@FXML
	private CheckBox checkUppercase, checkLowercase, checkNumbers, checkSpecial;
	
	@FXML
	private CheckBox checkHide, checkCopy;
	
	@FXML
	private Hyperlink linkCopy;
	
	@FXML
	private VBox panePasswordListBox;
	
	
	public GeneratorController() { }
	
	public void initialize() {
		this.pwdLength = 16;
		
		sliderPwdLength.valueProperty().addListener((ob, oldval, newval) -> {
			this.pwdLength = (int)sliderPwdLength.getValue();
			labelPwdLength.setText("Password Length: " + this.pwdLength);
		});
		
		try {
			manager = new Manager();
			
			for (int i=0;i<manager.getLength();i++) {
				
				panePasswordListBox.getChildren().add(manager.makeRow(manager.getId(i), manager.getName(i), manager.getUsername(i), manager.getPassword(i)));
			}
		
		} 
		catch(Exception e) {
			System.out.println("Error: Couldn't initialize Manager");
			e.printStackTrace();
		}
		
	}
	
	@FXML
	private void CopyPasswordToClipboard() {
		StringSelection ss = new StringSelection(this.hiddenPass);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(ss, null);
	}
	
	@FXML
	private void ToggleAutocopy() {
		if(checkHide.isSelected()) {
			checkCopy.setSelected(true);
			checkCopy.setDisable(true);
		} else {
			checkCopy.setDisable(false);
		}
	}
	
	@FXML
	private void GeneratePassword() {
		generator = new Generator(this.pwdLength);
		generator.setUppercase(checkUppercase.isSelected());
		generator.setLowercase(checkLowercase.isSelected());
		generator.setNumbers(checkNumbers.isSelected());
		generator.setSpecial(checkSpecial.isSelected());
		
		if(checkHide.isSelected()) {
			hiddenPass = generator.GeneratePassword();
			tfPassword.setText("***** HIDDEN *****");
			CopyPasswordToClipboard();
			
		} else {
			hiddenPass = generator.GeneratePassword();
			tfPassword.setText(hiddenPass);
			if(checkCopy.isSelected()) {
				CopyPasswordToClipboard();
			}
		}
		
		btnSave.setDisable(false);
	}
	
	/*@FXML
	private void SavePassword() {
		try {
			FXMLLoader l = new FXMLLoader(getClass().getResource("SaveDialog.fxml"));
		    Stage stage = new Stage();
		    
		    stage.initModality(Modality.APPLICATION_MODAL);
		    stage.setTitle("Save Password");
		    stage.setResizable(false);
		    stage.setScene(new Scene((BorderPane)l.load(), 400, 270));  
		    
		    l.<SaveDialogController>getController().initPassword(hiddenPass, manager);
		    stage.show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	
	}*/
	
	@FXML
	private void SavePassword() {
		try {
			manager.addEntry(tfPassword.getText(), "", "");
			panePasswordListBox.getChildren().clear();
			for (int i=0; i<manager.getLength(); i++) {
				panePasswordListBox.getChildren().add(manager.makeRow(manager.getId(i), manager.getName(i), manager.getUsername(i), manager.getPassword(i)));
			}
		}
		catch(Exception e) {
			System.out.println("Error: Couldn't add entry");
		}
		
	}
	
	
}
