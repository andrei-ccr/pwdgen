package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Database {
	
	private Connection conn;
	
	public Database() throws Exception {
		Class.forName("org.h2.Driver");
		conn = DriverManager.getConnection("jdbc:h2:~/pwman;AUTO_SERVER=TRUE","sa","");

	}
	
	public List<String> readAllPasswords() throws Exception {
		
		List<String> passwords = new ArrayList<String>();
	
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM passwords");
		
		ResultSet res = ps.executeQuery();
		
		while(res.next()) {
			passwords.add(res.getString(2));
		}
		
		return passwords;
	}
	
	public List<MPassword> readAll() throws Exception {
		HashMap<Integer, MPassword> passwords = new HashMap<Integer, MPassword>();
		
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM passwords");
		
		ResultSet res = ps.executeQuery();
		
		while(res.next()) {
			passwords.put(res.getInt(1), new MPassword(res.getInt(1), res.getString(2)));
		}
		
		ps = conn.prepareStatement("SELECT * FROM details");
		res = ps.executeQuery();
		
		while(res.next()) {
			passwords.get(res.getInt(4)).Name = res.getString(2);
			passwords.get(res.getInt(4)).Username = res.getString(3);
		}
		
		List<MPassword> list = new ArrayList<MPassword>();
		for(MPassword mp : passwords.values()) {
			list.add(mp);
		}
		return list;
	}
	
	public int getIndexFromId(long id) throws Exception{
		int index = 0;
		for(MPassword mp : readAll()) {
			if(mp.Id == (int)id) return index;
			index++;
		}
		return -1;
	}
	
	public long writePass(String pass) throws Exception{
		
		PreparedStatement ps = conn.prepareStatement("INSERT INTO passwords(pass) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, pass);
		ps.executeUpdate();
		
		try (ResultSet gk = ps.getGeneratedKeys()) {
			gk.next();
			return gk.getLong(1);
		}
	}
	
	public void writePassData(String username, String name, long passid) throws Exception{
		
		PreparedStatement ps = conn.prepareStatement("INSERT INTO details(name, username, pass_id) VALUES(?,?,?)");
		ps.setString(1, name);
		ps.setString(2, username);
		ps.setLong(3, passid);
		ps.executeUpdate();
	}
	
	public void updatePass(String pass, long passid) throws Exception{
		
		PreparedStatement ps = conn.prepareStatement("UPDATE passwords SET pass=? WHERE id=?");
		ps.setString(1, pass);
		ps.setLong(2, passid);
		ps.executeUpdate();
		
	}
	
	public void updatePassData(String username, String name, long passid) throws Exception {
		PreparedStatement ps = conn.prepareStatement("UPDATE details SET name=?, username=? WHERE pass_id=?");
		ps.setString(1, name);
		ps.setString(2, username);
		ps.setLong(3, passid);
		ps.executeUpdate();
	}
	
	public boolean hasDetails(long id) throws Exception {
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM details WHERE pass_id=?");
		ps.setLong(1, id);
		
		ResultSet res = ps.executeQuery();
		return res.next();

	}
	
	public void delete(long id) throws Exception {
		PreparedStatement ps = conn.prepareStatement("DELETE FROM details WHERE pass_id=?");
		ps.setLong(1, id);
		ps.executeUpdate();
		
		ps = conn.prepareStatement("DELETE FROM passwords WHERE id=?");
		ps.setLong(1, id);
		ps.executeUpdate();
	
	}
	
	public void closeConnection() throws Exception{
		this.conn.close();
	}
}
