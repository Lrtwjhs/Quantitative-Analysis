package pure.sauce.service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pure.sauce.service.Solve.IdType;

public class Parallel {
	
	final String URL = "http://oxfordhk.azure-api.net/academic/v1.0/evaluate?expr=";
	final String KEY = "&subscription-key=f7cc29509a8443c5b3a5e56b0e38b5a6";
	
	final String ID_2_ID			= "F.FId,C.CId,J.JId,AA.AuId";
	
	final String ID_ATTRIBUTES		= "RId,F.FId,C.CId,J.JId,AA.AuId";
	
	final String FID_ATTRIBUTES		= "Id";	//领域
	final String JID_ATTRIBUTES		= "Id";	//期刊
	final String CID_ATTRIBUTES		= "Id";	//会议
	
	final String AUID_ATTRIBUTES	= "Id,AA.AuId,AA.AfId";
	final String AFID_ATTRIBUTES	= "AA.AuId,AA.AfId";
	
	HashSet<String> st;
	HashSet<String> idst;
	ArrayList<Node> list;
	
	Vector<JSONArray> ans;
	
	Vector<step4> step4list;
	
	IdType type1, type2;
	String id1, id2;
	
	public class Node {
		IdType type;
		String id;
		
		public Node(IdType a, String b) {
			type = a;
			id = b;
		}
	}
	
	public Parallel(IdType type1, String id1, IdType type2, String id2) {
		st = new HashSet<>();
		list = new ArrayList<>();
		
		ans = new Vector<>();
		
		this.type1 = type1;
		this.type2 = type2;
		
		this.id1 = id1;
		this.id2 = id2;
		
		idst = new HashSet<>();
		step4list = new Vector<>();
		
		
//		try {
//			PrintWriter out = new PrintWriter(new FileOutputStream(id1 + "-" + id2 + ".out"));
//			out.println("id1: " + type1 + "-" + id1);
//			out.println("id2: " + type2 + "-" + id2);
//			out.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
	}
	
	public void test() {
//		step2 thread2 = new step2();
//		thread2.start();
//		try {
//			thread2.join();
//		} catch (Exception e) {
//		}
//		for(Node it : list) {
//			System.out.println("step2: " + it.type + "-" + it.id);
//		}
	}
	
	public JSONArray solve() {
		
		step1 thread1 = new step1();
		step2 thread2 = new step2();
		
		thread2.run();
		thread1.start();
		
		try {
			thread1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} //等待两者结束
		
//		for(String it : st) {
//			System.out.println("ST: " + it);
//		}

		System.out.println("step3 is running");
		
		Vector<step3> threadList = new Vector<>();
		boolean one = true;
		for(Node it : list) {
			if(one && it.id.equals(id2) && type2 == it.type) {
				one = false;
				JSONArray cur = new JSONArray();
				cur.add(id1);
				cur.add(id2);
				
				ans.add(cur);
			}
			if(st.contains(it.type + it.id)) {
				JSONArray cur = new JSONArray();
				cur.add(id1);
				cur.add(it.id);
				cur.add(id2);
				
				ans.add(cur);
			}
			
			if(it.type == IdType.id || it.type == IdType.afid || it.type == IdType.auid) {
				step3 thread = new step3(it);
				threadList.add(thread);
				thread.start();
			}
		}
		
		for(step3 it : threadList) {
			try {
				it.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for(step4 it : step4list) {
			try {
				it.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("ans number : " + ans.size());
		return JSONArray.fromObject(ans);
	}
	
	public class step4 extends Thread {
		String id;
		
		public step4(String id) {
			this.id = id;
		}
		
		public void add(String y) {
			if(idst.contains(y)) {
				JSONArray cur = new JSONArray();
				cur.add(id1);
				cur.add(y);
				cur.add(id);
				cur.add(id2);
				ans.add(cur);
			}
		}
		
		public void run() {
			JSONArray ja = get("Id", id, ID_2_ID);
			for(int i=0; i<ja.size(); ++i) {
				JSONObject xx = ja.getJSONObject(i);
				
				if(xx.containsKey("AA")) {	
					JSONArray auids = xx.getJSONArray("AA");
					for(int j=0; j<auids.size(); ++j) {
						JSONObject jo = auids.getJSONObject(j);
						if(jo.containsKey("AuId")) {
							if(idst.contains(jo.getString("AuId"))) {
								add(jo.getString("AuId"));
							}
						}
					}
				}
				if(xx.containsKey("F")) {
					JSONArray fids 	= xx.getJSONArray("F");
					for(int j=0; j<fids.size(); ++j) {
						JSONObject jo = fids.getJSONObject(j);
						if(jo.containsKey("FId")) {
							add(jo.getString("FId"));
						}
					}
				}
				if(xx.containsKey("J")) {
					JSONObject jid = xx.getJSONObject("J");
					add(jid.getString("JId"));
				}
				if(xx.containsKey("C")) {
					JSONObject cid = xx.getJSONObject("C");
					add(cid.getString("CId"));
				}
			}
		}
	}
	
	public class step1 extends Thread {
		public void run() {
//			System.out.println("step1 is running");
			if(type2 == IdType.id) {
				JSONArray ja = get("Id", id2, ID_2_ID);
				idUpdateSet(ja);
				
				getRId(setQuery("RId", id2), setCount(1000000), "Id");
				
//				ja = get("RId", id2, "Id");
//				ByteArrayOutputStream cur = getEntity(setQuery("RId", id2), setCount(50000), "Id");
//				System.out.println("Yes");
//				IDQuery idq = new IDQuery(cur.toByteArray());
//				System.out.println("WOW");
//				String str = idq.nextID();
//				while(str != null) {
//					st.add("id" + str);
//					str = idq.nextID();
//				}
				
//				System.out.println("size = " + st.size());
			} else {
				JSONArray ja = get("AA.AuId", id2, AUID_ATTRIBUTES);
				auUpdateSet(ja);
			}
//			System.out.println("step1 is finish");
		}
	}
	
	public class step2 {
		HashSet<String> vis;
		
		public void add(IdType type, String id) {
			String str = id;
			if(vis.contains(str)) return;
			vis.add(str);
			list.add(new Node(type, id));
		}
		
		public void run() {
			vis = new HashSet<>();
			if(type1 == IdType.id) {
				JSONArray ja = get("Id", id1, ID_ATTRIBUTES);
				for(int i=0; i<ja.size(); ++i) {
					JSONObject xx = ja.getJSONObject(i);
					
					if(xx.containsKey("RId")) {
						JSONArray rids = xx.getJSONArray("RId");
						for(int j=0; j<rids.size(); ++j) {
							add(IdType.id, rids.getString(j));
						}
					}
					if(xx.containsKey("AA")) {	
						JSONArray auids = xx.getJSONArray("AA");
						for(int j=0; j<auids.size(); ++j) {
							JSONObject jo = auids.getJSONObject(j);
							if(jo.containsKey("AuId")) {
								add(IdType.auid, jo.getString("AuId"));
								idst.add(jo.getString("AuId"));
							}
						}
					}
					if(xx.containsKey("F")) {
						JSONArray fids 	= xx.getJSONArray("F");
						for(int j=0; j<fids.size(); ++j) {
							JSONObject jo = fids.getJSONObject(j);
							if(jo.containsKey("FId")) {
								add(IdType.fid, jo.getString("FId"));
								idst.add(jo.getString("FId"));
							}
						}
					}
					if(xx.containsKey("J")) {
						JSONObject jid = xx.getJSONObject("J");
						add(IdType.jid, jid.getString("JId"));
						idst.add(jid.getString("JId"));
					}
					if(xx.containsKey("C")) {
						JSONObject cid = xx.getJSONObject("C");
						add(IdType.cid, cid.getString("CId"));
						idst.add(cid.getString("CId"));
					}
				}
			} else if(type1 == IdType.auid) {
				
				JSONArray ja = get("AA.AuId", id1, AUID_ATTRIBUTES);
				
				for(int i=0; i<ja.size(); ++i) {
					JSONObject xx = ja.getJSONObject(i);
					
					if(xx.containsKey("AA")) {
						JSONArray afids = xx.getJSONArray("AA");
						for(int j=0; j<afids.size(); ++j) {
							JSONObject jo = afids.getJSONObject(j);
							if(jo.containsKey("AfId") && jo.containsKey("AuId")) {
								if(jo.getString("AuId").equals(id1)) {
									add(IdType.afid, jo.getString("AfId"));
								}
							}
						}
					}
					if(xx.containsKey("Id")) {
						add(IdType.id, xx.getString("Id"));
					}
				}
				
			}
//			System.out.println("step2 is finish");
		}
	}
	
	class step3 extends Thread {
		Node x;
		HashSet<String> hs;
		
		public step3(Node x) {
			this.x = x;
		}
		
		public void add(IdType type, String id) {
			String str = id;
			
			if(hs.contains(str)) {
				return;
			}
			hs.add(str);
			
			if(st.contains(type + str)) {
				JSONArray cur = new JSONArray();
				
				cur.add(id1);
				cur.add(x.id);
				cur.add(id);
				cur.add(id2);
				
				ans.add(cur);
			}
		}
		
		public void run() {
			this.hs = new HashSet<>();
			System.out.println("step3: " + x.type + x.id);
			if(x.type == IdType.id) {
				JSONArray ja = get("Id", x.id, ID_ATTRIBUTES);
				for(int i=0; i<ja.size(); ++i) {
					JSONObject xx = ja.getJSONObject(i);
					
					if(xx.containsKey("RId")) {
						JSONArray rids = xx.getJSONArray("RId");
						for(int j=0; j<rids.size(); ++j) {
							add(IdType.id, rids.getString(j));
						}
					}
					if(xx.containsKey("AA")) {	
						JSONArray auids = xx.getJSONArray("AA");
						for(int j=0; j<auids.size(); ++j) {
							JSONObject jo = auids.getJSONObject(j);
							if(jo.containsKey("AuId")) {
								add(IdType.auid, jo.getString("AuId"));
							}
						}
					}
					if(xx.containsKey("F")) {
						JSONArray fids 	= xx.getJSONArray("F");
						for(int j=0; j<fids.size(); ++j) {
							JSONObject jo = fids.getJSONObject(j);
							if(jo.containsKey("FId")) {
								add(IdType.fid, jo.getString("FId"));
							}
						}
					}
					if(xx.containsKey("J")) {
						JSONObject jid = xx.getJSONObject("J");
						add(IdType.jid, jid.getString("JId"));
					}
					if(xx.containsKey("C")) {
						JSONObject cid = xx.getJSONObject("C");
						add(IdType.cid, cid.getString("CId"));
					}
				}
			} else if(x.type == IdType.auid) {
//				if(type2 != IdType.id) return;
				JSONArray ja = null;
//				ja = get("AA.AuId", x.id, AUID_ATTRIBUTES);
				if(type2 == IdType.auid) {
					ja = get("AA.AuId", x.id, AUID_ATTRIBUTES);
				} else {
					return;
				}
				
				for(int i=0; i<ja.size(); ++i) {
					JSONObject xx = ja.getJSONObject(i);
					
					if(xx.containsKey("AA")) {
						JSONArray afids = xx.getJSONArray("AA");
						for(int j=0; j<afids.size(); ++j) {
							JSONObject jo = afids.getJSONObject(j);
							if(jo.containsKey("AfId") && jo.containsKey("AuId")) {
								if(jo.getString("AuId").equals(x.id)) {
									add(IdType.afid, jo.getString("AfId"));
								}
							}
						}
					}
				}
				
			} else if(x.type == IdType.afid) {
				if(type2 != IdType.id) return;
				getAuId(x.id, IdType.auid, setQuery("AA.AfId", x.id), setCount(10000000), AFID_ATTRIBUTES);
				
			}
		}
		
		void getAuId(String afid, IdType type, String query, String cnt, String attributes) {
			String url = URL + query + cnt + attributes + KEY;
			
			System.out.println(url);
			
//			System.out.println("connect");
			
			CloseableHttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(url);
			
//			System.out.println("connectfinish");
			
			byte[] buff = new byte[4096];
			char[] line = new char[4096];
			try {
				HttpResponse response = client.execute(request);
				InputStream is = response.getEntity().getContent();
				
//				System.out.println("getcontent finish");
				
				int len = 0;
				
				int totlen = 0, totid = 0;
				
				int strlen = 0;
				while((len = is.read(buff)) != -1) {
//					System.out.println("len = " + len);
					int offset = 0;
					while(offset < len) {
						while(offset < len && (char) buff[offset] != '\n') {
							line[strlen++] = (char) buff[offset++];
						}
						++ offset;
						if(offset <= len) {
							//line end;
//							line[strlen] = '\0';
//							System.out.println(line);
							for(int i=5; i<strlen; ++i) {
								//AuId":
								if(line[i-5] == 'u' && line[i-4] == 'I' &&
										line[i-3] == 'd' && line[i-2] == '"' && line[i-1] == ':') { //find auid
									++ totid;
									
									StringBuffer au = new StringBuffer();
									int p = i;
									while(p < strlen && Character.isDigit(line[p])) {
										au.append(line[p++]);
									}
									
//									System.out.print("AuthId : " + au.toString());
//									
//									System.out.println(" " + line[p] + line[p+1] + line[p+2] + line[p+3] + line[p+4] + line[p+5] + line[p+6] + line[p+7] + line[p+8]);
									
									while(p < strlen && line[p] != '}' &&
									(line[p-5] != 'f' || line[p-4] != 'I' || line[p-3] != 'd' || line[p-2] != '"' || line[p-1] != ':')) {
										++ p;
									}
									
									if(p < strlen && line[p] != '}') { //find afid
										StringBuffer af = new StringBuffer();
										while(p < strlen && Character.isDigit(line[p])) {
											af.append(line[p++]);
										}
										if(af.toString().equals(afid)) {
											add(type, au.toString());
										}
//										System.out.println("{AuId="+au.toString()+",AfId="+af.toString()+"}");
									}
								}
							}
							
							strlen = 0;
						}
					}
					totlen += len;
				}
				System.out.println(url);
				System.out.println("getAuId : totlen = " + totlen + " totId " + totid);
				
			} catch (Exception e) {
				System.out.println("getAuid failed " + e.getMessage());
			}
			
			try {
				client.close();
			} catch (IOException e) {
			}
		}
		
		void getId(IdType type, String query, String cnt, String attributes) {
			String url = URL + query + cnt + attributes + KEY;
			httpGetId(type, url);
		}
		
		public void httpGetId(IdType type, String url) {
			CloseableHttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(url);
			byte[] buff = new byte[4096 + 3];
			try {
				HttpResponse response = client.execute(request);
				InputStream is = response.getEntity().getContent();
				
				int len = 0, offset = 3, totlen = 0, totid = 0;
				StringBuilder sb = new StringBuilder();
				boolean yes = false;
				while((len = is.read(buff, 3, 4096)) != -1) {
					totlen += len;
					while(offset < len) {
						if(!yes) {
							while(offset < len + 3 && 
								(buff[offset - 1] != ':' ||
								 buff[offset - 2] != '"' ||
								 buff[offset - 3] != 'd')) {
								++ offset;
							}
							yes = true;
						}
						
						while(offset < len + 3 && Character.isDigit(buff[offset])) {
							sb.append((char)buff[offset]);
							++ offset;
						}
						
						if(offset < len + 3) {
							add(type, sb.toString());
							++ totid;
							sb = new StringBuilder();
							yes = false;
						}
					}
					
					if(len >= 3) {
						buff[0] = buff[3 + len - 3];
						buff[1] = buff[3 + len - 2];
						buff[2] = buff[3 + len - 1];
					}
					offset = 3;
				}
				System.out.println(url);
				System.out.println("getId : totlen = " + totlen + " totId " + totid);
				
			} catch (Exception e) {
//				System.out.println("getId failed");
			}
			
			try {
				client.close();
			} catch (IOException e) {
//				e.printStackTrace();
			}
		}
	}
	
	void updateSet(JSONArray ja, String key, IdType type) {
		for(int i=0; i<ja.size(); ++i) {
			JSONObject jo = ja.getJSONObject(i);
			if(jo.containsKey(key)) {
				st.add(type + jo.get(key).toString());
			}
		}
	}
	
	void auUpdateSet(JSONArray ja) {
//		System.out.println("auUpdate:");
//		System.out.println(ja);
		for(int i=0; i<ja.size(); ++i) {
			JSONObject x = ja.getJSONObject(i);
			
			if(x.containsKey("AA")) {	
				JSONArray afids = x.getJSONArray("AA");
//				updateSet(afids, "AfId", IdType.afid);
				//找 auId 对应的 afId 而不是所有的 afid
				for(int j=0; j<afids.size(); ++j) {
					JSONObject jo = afids.getJSONObject(j);
					if(jo.containsKey("AfId") && jo.containsKey("AuId")) {
						if(jo.getString("AuId").equals(id2)) {
							st.add(IdType.afid + jo.getString("AfId"));
						}
					}
				}
			}
			if(x.containsKey("Id")) {
				st.add(IdType.id + x.getString("Id"));
				step4 thread = new step4(x.getString("Id"));
				thread.start();
				step4list.add(thread);
			}
		}
	}
	
	
	private void idUpdateSet(JSONArray ja) {
		for(int i=0; i<ja.size(); ++i) {
			JSONObject x = ja.getJSONObject(i);
			
			if(x.containsKey("AA")) {	
				JSONArray auids = x.getJSONArray("AA");
				updateSet(auids, "AuId", IdType.auid);
			}
			if(x.containsKey("F")) {
				JSONArray fids 	= x.getJSONArray("F");
				updateSet(fids, "FId", IdType.fid);
			}
			if(x.containsKey("J")) {
				JSONObject jid = x.getJSONObject("J");
				st.add(IdType.jid + jid.getString("JId"));
			}
			if(x.containsKey("C")) {
				JSONObject cid = x.getJSONObject("C");
				st.add(IdType.cid + cid.getString("CId"));
			}
		}
	}
	
	JSONArray get(String type, String id, String attributes) {
		JSONObject obj = null;
		String str = null; 

		for(int i=0; str == null && i<50; ++i) {
			str = getEntity(setQuery(type, id), setCount(500000), attributes).toString();
		}
		obj = JSONObject.fromObject(str);

		return JSONArray.fromObject(obj.get("entities"));
	}
	
	ByteArrayOutputStream getEntity(String query, String cnt, String attributes) {
		String url = URL + query + cnt + attributes + KEY;
		return httpGet(url);
	}
	
	public String setCount(int num) {
		return "&count=" + num + "&attributes=";
	}
	
	public String setQuery(String type, String id) {
		if(type.equals("Id") || type.equals("RId")) {
			return type + "=" + id;
		}
		return "Composite(" + type + "=" + id + ")";
	}
	
	public boolean isId(String id) {
		return !getEntity(setQuery("AA.AuId", id), setCount(1), "Id").toString().contains("logprob");
	}
	
	public ByteArrayOutputStream httpGet(String url) {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
//		System.out.println(request.getURI());
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buff = new byte[4096];
		try {
			HttpResponse response = client.execute(request);
			InputStream is = response.getEntity().getContent();
			
			int len = 0;
			while((len = is.read(buff)) != -1) {
				os.write(buff, 0, len);
			}
		} catch (Exception e) {
//			System.out.println(e.getMessage());
		}
//		System.out.println("length-" + os.size());
//		if(os.size() > 100000) {
//			System.out.println("length-" + os.size() + request.getURI());
//		}
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return os;
	}

	void getRId(String query, String cnt, String attributes) {
		String url = URL + query + cnt + attributes + KEY;
		httpGetRId(url);
	}
	
	public void httpGetRId(String url) {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
//		System.out.println(request.getURI());
		byte[] buff = new byte[4096 + 3];
		try {
			HttpResponse response = client.execute(request);
			InputStream is = response.getEntity().getContent();
			
			int len = 0, offset = 3;
			StringBuilder sb = new StringBuilder();
			boolean yes = false;
//			System.out.println("get Rid start");
			int totLen = 0;
			while((len = is.read(buff, 3, 4096)) != -1) {
				totLen += len;
				
				while(offset < len) {
					if(!yes) {
						while(offset < len + 3 && 
							(buff[offset - 1] != ':' ||
							 buff[offset - 2] != '"' ||
							 buff[offset - 3] != 'd')) {
							++ offset;
						}
						yes = true;
					}
					
					while(offset < len + 3 && Character.isDigit(buff[offset])) {
						sb.append((char)buff[offset]);
						++ offset;
					}
					
					if(offset < len + 3) {
						if(sb.toString().length() > 0) {
							st.add("id" + sb.toString());
							step4 thread = new step4(sb.toString());
							thread.start();
							step4list.add(thread);
						}
						
	//					System.out.println("getRid: " + sb.toString());
						sb = new StringBuilder();
						yes = false;
					}
				}
				
				if(len >= 3) {
//					System.out.println("len = " + len);
					buff[0] = buff[3 + len - 3];
					buff[1] = buff[3 + len - 2];
					buff[2] = buff[3 + len - 1];
				}
				offset = 3;
			}
//			System.out.println("get Rid finish totLen = " + totLen);
			
		} catch (Exception e) {
//			System.out.println("getRid failed");
		}
		
		try {
			client.close();
		} catch (IOException e) {
//			e.printStackTrace();
		}
	}
}
