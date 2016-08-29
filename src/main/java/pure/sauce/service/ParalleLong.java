package pure.sauce.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Vector;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pure.sauce.service.Solve.IdType;

public class ParalleLong {
	
	final String URL = "http://oxfordhk.azure-api.net/academic/v1.0/evaluate?expr=";
	final String KEY = "&subscription-key=f7cc29509a8443c5b3a5e56b0e38b5a6";
	
	final String ID_2_ID			= "F.FId,C.CId,J.JId,AA.AuId";
	
	final String ID_ATTRIBUTES		= "RId,F.FId,C.CId,J.JId,AA.AuId";
	
	final String FID_ATTRIBUTES		= "Id";	//领域
	final String JID_ATTRIBUTES		= "Id";	//期刊
	final String CID_ATTRIBUTES		= "Id";	//会议
	
	final String AUID_ATTRIBUTES	= "Id,AA.AuId,AA.AfId";
	final String AFID_ATTRIBUTES	= "AA.AuId,AA.AfId";
	
	HashSet<Long> st;
	HashSet<Long> idst;
//	HashSet<Long> afst;
	ArrayList<Node> list;
	
	Vector<JSONArray> ans;
	
	Vector<step4> step4list;
	Vector<step5> step5list;
	
	IdType type1, type2;
	String sid1, sid2;
	long id1, id2;
	
	public class Node {
		IdType type;
		long id;
		
		public Node(IdType a, long b) {
			type = a;
			id = b;
		}
	}
	
	public ParalleLong(IdType type1, String sid1, IdType type2, String sid2) {
		st = new HashSet<>();
		list = new ArrayList<>();
		
		ans = new Vector<>();
		
		this.type1 = type1;
		this.type2 = type2;
		
		this.sid1 = sid1;
		this.sid2 = sid2;
		
		id1 = Long.parseLong(sid1);
		id2 = Long.parseLong(sid2);
		
		idst = new HashSet<>();
//		afst = new HashSet<>();
		step4list = new Vector<>();
		step5list = new Vector<>();
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
		
//		for(Long it : st) {
//			System.out.println(it);
//		}

//		System.out.println("step3 is running");
		
		Vector<step3> threadList = new Vector<>();
		boolean one = true;
		for(Node it : list) {
			if(one && it.id == (id2) && type2 == it.type) {
				one = false;
				JSONArray cur = new JSONArray();
				cur.add(id1);
				cur.add(id2);
				
				ans.add(cur);
			}
			if(st.contains(it.id)) {
				JSONArray cur = new JSONArray();
				cur.add(id1);
				cur.add(it.id);
				cur.add(id2);
				
				ans.add(cur);
			}
			
			if(it.type == IdType.id || it.type == IdType.auid) {
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
		
//		System.out.println("ans number : " + ans.size());
		return JSONArray.fromObject(ans);
	}
	
	public class step1 extends Thread {
		public void run() {
			if(type2 == IdType.id) {
				JSONArray ja = get("Id", sid2, ID_2_ID);
				idUpdateSet(ja);
				
				getRId(setQuery("RId", sid2), setCount(500000), "Id");
			
			} else {
				JSONArray ja = get("AA.AuId", sid2, AUID_ATTRIBUTES);
				auUpdateSet(ja);
			}
		}
	}
	
	public class step2 {
		HashSet<Long> vis;
		
		public void add(IdType type, long id) {
			idst.add(id);
			if(vis.contains(id)) return;
			vis.add(id);
			list.add(new Node(type, id));
		}
		
		public void run() {
			vis = new HashSet<>();
			if(type1 == IdType.id) {
				JSONArray ja = get("Id", sid1, ID_ATTRIBUTES);
				for(int i=0; i<ja.size(); ++i) {
					JSONObject xx = ja.getJSONObject(i);
					
					if(xx.containsKey("RId")) {
						JSONArray rids = xx.getJSONArray("RId");
						for(int j=0; j<rids.size(); ++j) {
							add(IdType.id, rids.getLong(j));
						}
					}
					if(xx.containsKey("AA")) {	
						JSONArray auids = xx.getJSONArray("AA");
						for(int j=0; j<auids.size(); ++j) {
							JSONObject jo = auids.getJSONObject(j);
							if(jo.containsKey("AuId")) {
								add(IdType.auid, jo.getLong("AuId"));
							}
						}
					}
					if(xx.containsKey("F")) {
						JSONArray fids 	= xx.getJSONArray("F");
						for(int j=0; j<fids.size(); ++j) {
							JSONObject jo = fids.getJSONObject(j);
							if(jo.containsKey("FId")) {
								add(IdType.fid, jo.getLong("FId"));
							}
						}
					}
					if(xx.containsKey("J")) {
						JSONObject jid = xx.getJSONObject("J");
						add(IdType.jid, jid.getLong("JId"));
					}
					if(xx.containsKey("C")) {
						JSONObject cid = xx.getJSONObject("C");
						add(IdType.cid, cid.getLong("CId"));
					}
				}
			} else if(type1 == IdType.auid) {
				
				JSONArray ja = get("AA.AuId", sid1, AUID_ATTRIBUTES);
				
				for(int i=0; i<ja.size(); ++i) {
					JSONObject xx = ja.getJSONObject(i);
					
					if(xx.containsKey("AA")) {
						JSONArray afids = xx.getJSONArray("AA");
						for(int j=0; j<afids.size(); ++j) {
							JSONObject jo = afids.getJSONObject(j);
							if(jo.containsKey("AfId") && jo.containsKey("AuId")) {
								if(jo.getLong("AuId") == (id1)) {
									long y = jo.getLong("AfId");
									add(IdType.afid, y);
								}
							}
						}
					}
					if(xx.containsKey("Id")) {
						add(IdType.id, xx.getLong("Id"));
					}
				}
				
			}
//			System.out.println("step2 is finish");
		}
	}
	
	public class step3 extends Thread {
		Node x;
		HashSet<Long> hs;
		
		public step3(Node x) {
			this.x = x;
		}
		
		public void add(IdType type, long id) {
			
			if(hs.contains(id)) {
				return;
			}
			hs.add(id);
			
			if(st.contains(id)) {
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
//			System.out.println("step3: " + x.type + x.id);
			if(x.type == IdType.id) {
				JSONArray ja = get("Id", x.id, ID_ATTRIBUTES);
				for(int i=0; i<ja.size(); ++i) {
					JSONObject xx = ja.getJSONObject(i);
					
					if(xx.containsKey("RId")) {
						JSONArray rids = xx.getJSONArray("RId");
						for(int j=0; j<rids.size(); ++j) {
							add(IdType.id, rids.getLong(j));
						}
					}
					if(xx.containsKey("AA")) {	
						JSONArray auids = xx.getJSONArray("AA");
						for(int j=0; j<auids.size(); ++j) {
							JSONObject jo = auids.getJSONObject(j);
							if(jo.containsKey("AuId")) {
								add(IdType.auid, jo.getLong("AuId"));
							}
						}
					}
					if(xx.containsKey("F")) {
						JSONArray fids 	= xx.getJSONArray("F");
						for(int j=0; j<fids.size(); ++j) {
							JSONObject jo = fids.getJSONObject(j);
							if(jo.containsKey("FId")) {
								add(IdType.fid, jo.getLong("FId"));
							}
						}
					}
					if(xx.containsKey("J")) {
						JSONObject jid = xx.getJSONObject("J");
						add(IdType.jid, jid.getLong("JId"));
					}
					if(xx.containsKey("C")) {
						JSONObject cid = xx.getJSONObject("C");
						add(IdType.cid, cid.getLong("CId"));
					}
				}
			} else if(x.type == IdType.auid) {
//				if(type2 != IdType.id) return;
				JSONArray ja = null;
//				ja = get("AA.AuId", x.id, AUID_ATTRIBUTES);
				if(type2 == IdType.auid) {
					ja = get("AA.AuId", x.id, "AA.AuId,AA.AfId");
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
								if(jo.getLong("AuId") == (x.id)) {
									add(IdType.afid, jo.getLong("AfId"));
								}
							}
						}
					}
				}
				
			}
		}
	}
	
	public class step4 extends Thread {
		long id;
		
		public step4(long id) {
			this.id = id;
		}
		
		public void add(long y) {
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
			
//			System.out.println("Step4 " + id);
			
			for(int i=0; i<ja.size(); ++i) {
				JSONObject xx = ja.getJSONObject(i);
				
				if(xx.containsKey("AA")) {	
					JSONArray auids = xx.getJSONArray("AA");
					for(int j=0; j<auids.size(); ++j) {
						JSONObject jo = auids.getJSONObject(j);
						if(jo.containsKey("AuId")) {
							add(jo.getLong("AuId"));
						}
					}
				}
				if(xx.containsKey("F")) {
					JSONArray fids 	= xx.getJSONArray("F");
					for(int j=0; j<fids.size(); ++j) {
						JSONObject jo = fids.getJSONObject(j);
						if(jo.containsKey("FId")) {
							add(jo.getLong("FId"));
						}
					}
				}
				if(xx.containsKey("J")) {
					JSONObject jid = xx.getJSONObject("J");
					add(jid.getLong("JId"));
				}
				if(xx.containsKey("C")) {
					JSONObject cid = xx.getJSONObject("C");
					add(cid.getLong("CId"));
				}
			}
		}
	}
	
	public class step5 extends Thread {
		long id;
		HashSet<Long> afst;
		
		public step5(long id) {
			this.id = id;
		}
		
		public void add(long y) {
			if(afst.contains(y)) {
				return;
			}
			afst.add(y);
			
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
			JSONArray ja = get("AA.AuId", id, "AA.AuId,AA.AfId");
			this.afst = new HashSet<>();
//			System.out.println("Step5: " + id);
			
			for(int i=0; i<ja.size(); ++i) {
				JSONObject xx = ja.getJSONObject(i);
				
				if(xx.containsKey("AA")) {
					JSONArray afids = xx.getJSONArray("AA");
					for(int j=0; j<afids.size(); ++j) {
						JSONObject jo = afids.getJSONObject(j);
						if(jo.containsKey("AfId") && jo.containsKey("AuId")) {
							if(jo.getLong("AuId") == id) {
								add(jo.getLong("AfId"));
							}
						}
					}
				}
			}
		}
	}
	
	void updateSet(JSONArray ja, String key, IdType type) {
		for(int i=0; i<ja.size(); ++i) {
			JSONObject jo = ja.getJSONObject(i);
			if(jo.containsKey(key)) {
				st.add(jo.getLong(key));
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
						if(jo.getLong("AuId") == (id2)) {
							st.add(jo.getLong("AfId"));
						}
					}
				}
			}
			if(x.containsKey("Id")) {
				long y = x.getLong("Id");
				st.add(y);
				
				if(type1 == IdType.id) {
					step4 thread = new step4(y);
					thread.start();
					step4list.add(thread);
				}
			}
		}
	}
	
	
	private void idUpdateSet(JSONArray ja) {
		for(int i=0; i<ja.size(); ++i) {
			JSONObject x = ja.getJSONObject(i);
			
			if(x.containsKey("AA")) {	
				JSONArray auids = x.getJSONArray("AA");
				
				for(int j=0; j<auids.size(); ++j) {
					JSONObject jo = auids.getJSONObject(j);
					if(jo.containsKey("AuId")) {
						long y = jo.getLong("AuId");
						st.add(y);
						
						if(type1 == IdType.auid) {
							step5 thread = new step5(y);
							thread.start();
							step5list.add(thread);
						}
					}
				}
			}
			if(x.containsKey("F")) {
				JSONArray fids 	= x.getJSONArray("F");
				updateSet(fids, "FId", IdType.fid);
			}
			if(x.containsKey("J")) {
				JSONObject jid = x.getJSONObject("J");
				st.add(jid.getLong("JId"));
			}
			if(x.containsKey("C")) {
				JSONObject cid = x.getJSONObject("C");
				st.add(cid.getLong("CId"));
			}
		}
	}
	
	JSONArray get(String type, String id, String attributes) {
		JSONObject obj = null;
		String str = null; 

		str = getEntity(setQuery(type, id), setCount(500000), attributes).toString();
		obj = JSONObject.fromObject(str);

		return JSONArray.fromObject(obj.get("entities"));
	}
	
	JSONArray get(String type, long id, String attributes) {
		JSONObject obj = null;
		String str = null; 

		str = getEntity(setQuery(type, id), setCount(500000), attributes).toString();
			
		if(str.length() > 0 && str.charAt(0) == '{') {
			obj = JSONObject.fromObject(str);
		} else {
			System.out.println("errorUrl: " + setQuery(type, id) + setCount(500000) + attributes);
			System.out.println("get error! str: " + str);
			return new JSONArray();
		}

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
	
	public String setQuery(String type, long id) {
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
		Random ra = new Random();
		
		byte[] buff = new byte[4096];
		HttpResponse response = null; 
		boolean tryAgain = true;
		int trycnt = 0;
		while(tryAgain && trycnt < 1000) {
			if(trycnt > 0) {
				try {
					Thread.sleep(ra.nextInt(100));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				response = client.execute(request);
				tryAgain = false;
			} catch (Exception e) {
				++ trycnt;
				tryAgain = true;
			}
		}
		
		if(!tryAgain) {
			InputStream is = null;
			
			try {
				is = response.getEntity().getContent();
			} catch (UnsupportedOperationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			int len = 0;
			try {
				while((len = is.read(buff)) != -1) {
					os.write(buff, 0, len);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else {
			System.out.println("Error url : " + url);
		}
		
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
		
		Random ra = new Random();
		
		HttpResponse response = null; 
		boolean tryAgain = true;
		int trycnt = 0;
		while(tryAgain && trycnt < 1000) {
			if(trycnt > 0) {
				try {
					Thread.sleep(ra.nextInt(100));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				response = client.execute(request);
				tryAgain = false;
			} catch (Exception e) {
				++ trycnt;
				tryAgain = true;
			}
		}
		
		if(tryAgain) {
			System.out.println("Get RID failed");
			return;
		}
		

		byte[] buff = new byte[4096];
		char[] line = new char[4096];
		
		try {
			InputStream is = response.getEntity().getContent();
			
			int len = 0, offset = 3;
			int strlen = 0;
			while((len = is.read(buff)) != -1) {
//				System.out.println("len = " + len);
				offset = 0;
				while(offset < len) {
					while(offset < len && (char) buff[offset] != '\n') {
						line[strlen++] = (char) buff[offset++];
					}
					++ offset;
					if(offset <= len) {
						for(int i=3; i<strlen; ++i) {
							//Id":
							if(line[i-3] == 'd' && line[i-2] == '"' && line[i-1] == ':') { //find id
								
								StringBuffer sb = new StringBuffer();
								int p = i;
								while(p < strlen && Character.isDigit(line[p])) {
									sb.append(line[p++]);
								}
								
								if(sb.length() > 0) {
									long y = Long.parseLong(sb.toString());
									st.add(y);
									
//									System.out.println("RID: " + y);
									
									if(type1 == IdType.id) {
										step4 thread = new step4(y);
										thread.start();
										step4list.add(thread);
									}
								}
							}
						}
						
						strlen = 0;
					}
				}
			}
			System.out.println(url);
			System.out.println("getRID finish");
		} catch (Exception e) {
			System.out.println("read getRid failed");
		}
		
		try {
			client.close();
		} catch (IOException e) {
//			e.printStackTrace();
		}
	}
}
