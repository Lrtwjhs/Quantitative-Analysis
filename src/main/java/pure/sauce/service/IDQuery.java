package pure.sauce.service;

public class IDQuery {
	public byte[] data;
	int offset;
	
	public IDQuery(byte[] bs) {
		this.data = bs;
		offset = 5;
		System.out.println("IDQuery Create");
	}
	
	String nextID() {
		while(offset < data.length && 
			(data[offset - 1] != ':' ||
			 data[offset - 2] != '"' ||
			 data[offset - 3] != 'd')) {
			++ offset;
		}
		if(offset == data.length) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		while(offset < data.length && Character.isDigit(data[offset])) {
			sb.append((char)data[offset]);
			++ offset;
		}
		return sb.toString();
	}
}
