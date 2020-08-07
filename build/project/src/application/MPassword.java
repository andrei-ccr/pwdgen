package application;

public class MPassword {
	public String Password, Name="", Username="";
	public int Id;
	
	public MPassword(int id, String pass) {
		this.Id = id;
		this.Password = pass;
	}
	
	public MPassword(int id, String pass, String username, String name) {
		this.Id = id;
		this.Password = pass;
		this.Name = name;
		this.Username = username;
	}
}
