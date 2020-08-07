package application;

import java.util.Random;

public class Generator {
	
	private int length;
	private boolean uppercase, lowercase, numbers, special;
	
	private StringBuilder charset;
	private Random rand;
	
	public Generator() {
		this.length = 12;
		this.uppercase = true;
		this.lowercase = true;
		this.numbers = true;
		this.special = true;
		
	}
	
	public Generator(int l) {
		this();
		this.length = l;	
	}
	
	private void BuildCharset() {
		this.charset = new StringBuilder();
		if(this.uppercase) {
			for(int i=65; i<=90;i++) {
				this.charset.append((char)i);
			}
		}
		
		if(this.lowercase) {
			for(int i=97; i<=122;i++) {
				this.charset.append((char)i);
			}
		}
		
		if(this.numbers) {
			for(int i=48; i<=57;i++) {
				this.charset.append((char)i);
			}
		}
		
		if(this.special) {
			this.charset.append("[];',./`-=!@#$%^&*(){}:\"<>?~_+\\|");
		}
	}
	
	private void ClearCharset() {
		this.charset.delete(0, this.charset.length());
		this.charset.setLength(0);
	}
	
	public String GeneratePassword() {
		String pwd;
		StringBuilder pwdB = new StringBuilder(this.length);
		rand = new Random();
		
		int ind;
		
		BuildCharset();
		
		while(pwdB.length() < this.length) {
			ind = rand.nextInt(this.charset.length());
			pwdB.append(this.charset.charAt(ind));
		}
		
		pwd = pwdB.toString();
		
		
		ClearCharset();
		return pwd;
	}
	

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public boolean isUppercase() {
		return uppercase;
	}

	public void setUppercase(boolean uppercase) {
		this.uppercase = uppercase;
	}

	public boolean isLowercase() {
		return lowercase;
	}

	public void setLowercase(boolean lowercase) {
		this.lowercase = lowercase;
	}

	public boolean isNumbers() {
		return numbers;
	}

	public void setNumbers(boolean numbers) {
		this.numbers = numbers;
	}

	public boolean isSpecial() {
		return special;
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}
	
}