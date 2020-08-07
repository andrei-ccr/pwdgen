package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SaveDialogController {

	@FXML
	private TextField tfName, tfUsername, tfPassword;
	
	@FXML
	private Button btnCancel, btnSave;
	
	private Manager manager;
	
	void initPassword(String pass, Manager manager) {
		tfPassword.setText(pass);
		this.manager = manager;
	}
	
	@FXML
	private void savePassword() {
		try {
			manager.addEntry(tfPassword.getText(), tfUsername.getText(), tfName.getText());
			((Stage)btnCancel.getScene().getWindow()).close();
		}
		catch(Exception e) {
			System.out.println("Error: Couldn't save the password");
			e.printStackTrace();
		}
		
	}
	
	@FXML
	private void cancel() {
		((Stage)btnCancel.getScene().getWindow()).close();
	}
}
