package application;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Manager {
	Database db;
	List<MPassword> passList;
	
	public Manager() throws Exception {
		db = new Database();
		passList = db.readAll();
	}
	
	public String findPassword(int id) {
		for(MPassword mp : this.passList) {
			if(id==mp.Id) return mp.Password;
		}
		
		return null;
	}
	
	public String findUsername(int id) {
		for(MPassword mp : this.passList) {
			if(id==mp.Id) return mp.Username;
		}
		
		return null;
	}
	
	public String findName(int id) {
		for(MPassword mp : this.passList) {
			if(id==mp.Id) return mp.Name;
		}
		
		return null;
	}
	
	public String getPassword(int index) {
		return passList.get(index).Password;
	}
	
	public String getUsername(int index) {
		return passList.get(index).Username;
	}
	
	public String getName(int index) {
		return passList.get(index).Name;
	}
	
	public int getId(int index) {
		return passList.get(index).Id;
	}
	
	public int getLength() {
		return passList.size();
	}
	
	public void addEntry(String pass, String u, String n) throws Exception {
		if(pass.length() > 0) {
			long pwdid = db.writePass(pass);
				
			if(n.length()>0 || u.length()>0) {
				db.writePassData(u, n, pwdid);
			}
		
		}
		else {
			throw new Exception();
		}
		
		passList = db.readAll();
			
	}
	
	public void updateEntry(int id, String pass, String u, String n) throws Exception {
		if(pass != findPassword(id) && pass !=null) {
			db.updatePass(pass, id);
		}
		
		if(u != findUsername(id) && u != null) {
			if(db.hasDetails(id))
				db.updatePassData(u, findName(id), id);
			else
				db.writePassData(u, findName(id), id);
		}
		
		if(n != findName(id) && n != null) {
			db.updatePassData(findUsername(id), n, id);
		}
	}
	
	public void deleteRow(int id) {
		try {
			db.delete(id);
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public HBox makeRow(Integer id, String name, String username, String pass) {
		TextField tfUsername, tfName;
		PasswordField pfPassword;
		Hyperlink linkRowCopy, linkRowDelete;
		
		tfUsername = new TextField();
		tfUsername.setId("rowUsername" + id.toString());
		tfUsername.setText(username);
		
		tfUsername.minWidth(185);
		tfUsername.setPromptText(" -------- No Username -------- ");
		tfUsername.getStyleClass().add("row-textinputs");
		tfUsername.addEventHandler(KeyEvent.KEY_RELEASED, new EventUsernameChanged());
		
		tfName = new TextField();
		tfName.setId("rowName" + id.toString());
		tfName.setText(name);
		tfName.minWidth(185);
		tfName.getStyleClass().add("row-textinputs");
		tfName.setPromptText(" --------------------------------- ");
		
		pfPassword = new PasswordField();
		pfPassword.setId("rowPassword" + id.toString());
		
		pfPassword.setText(pass);
		pfPassword.minWidth(110);
		pfPassword.getStyleClass().add("row-textinputs");
		
		linkRowCopy = new Hyperlink("Copy");
		linkRowCopy.setId("rowLinkCopy" + id.toString());
		linkRowCopy.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				StringSelection ss = new StringSelection(pfPassword.getText());
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(ss, null);
			}
			
		});
		
		linkRowDelete = new Hyperlink("Delete");
		linkRowDelete.setId("rowLinkDelete" + id.toString());
		linkRowDelete.setOnAction(new EventRowDelete());
		
		HBox hbox = new HBox();
		hbox.setStyle("-fx-padding: 20px");
		hbox.getChildren().add(pfPassword);
		hbox.getChildren().add(linkRowCopy);
		hbox.getChildren().add(linkRowDelete);
		hbox.getChildren().add(tfUsername);
		hbox.getChildren().add(tfName);
		

		return hbox;
	}
	
	public class EventUsernameChanged implements EventHandler<Event>{
        @Override
        public void handle(Event evt) {
        	String nodeid = ((Control)evt.getSource()).getId();
    		Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(nodeid);
            if(!m.find()) {
            	System.out.println("Error: Regex Error. Entry won't be updated!");
            	return;
            }
            
            int i = Integer.parseInt(m.group());
            String username = ((TextField)evt.getSource()).getText();
            
            try {
            	updateEntry(i, null, username, null);
            }
            catch(Exception ex) {
            	System.out.println("Error: Couldn't update entry");
            	ex.printStackTrace();
            }
        }
    }
	
	public class EventRowDelete implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent evt) {
        	String nodeid = ((Control)evt.getSource()).getId();
    		Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(nodeid);
            if(!m.find()) {
            	System.out.println("Error: Regex error. Entry won't be deleted!");
            	return;
            }
            
            int i = Integer.parseInt(m.group());
            
            try {
            	((VBox)((HBox)((Control)evt.getSource()).getParent()).getParent()).getChildren().remove(db.getIndexFromId(i));
            	deleteRow(i);
            	
            }
            catch(Exception ex) {
            	System.out.println("Error: Couldn't delete entry");
            	ex.printStackTrace();
            }
        }
    }
	
}
