package pure.sauce.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pure.sauce.service.Solve.IdType;

public class testHttp {
	final String URL = "http://oxfordhk.azure-api.net/academic/v1.0/evaluate?expr=";
	final String KEY = "&subscription-key=f7cc29509a8443c5b3a5e56b0e38b5a6";
	
	final String ID_2_ID			= "F.FId,C.CId,J.JId,AA.AuId";
	
	final String ID_ATTRIBUTES		= "RId,F.FId,C.CId,J.JId,AA.AuId";
	
	final String FID_ATTRIBUTES		= "Id";	//领域
	final String JID_ATTRIBUTES		= "Id";	//期刊
	final String CID_ATTRIBUTES		= "Id";	//会议
	
	final String AUID_ATTRIBUTES	= "Id,AA.AfId";
	final String AFID_ATTRIBUTES	= "AA.AuId";
	
	
	
	@Test
	public void testGet() throws ClientProtocolException, IOException {
//		System.out.println(isId("56590836"));
//		System.out.println(isId("2145115012"));
		//System.out.println(getIdEdge("2145115012"));2112903990
		//System.out.println(getEntity(setQuery("Id", "1968806977"), setCount(10000), ID_ATTRIBUTES));
		//System.out.println(getEntity(setQuery("RId", "2100837269"), setCount(10000), "Id"));
		//solveId2Id("0", "2100837269");
		
//		String s = httpGet("http://oxfordhk.azure-api.net/academic/v1.0/evaluate?expr=Composite(AA.AuId=621499171)&count=500000&attributes=Id,AA.AfId&subscription-key=f7cc29509a8443c5b3a5e56b0e38b5a6");
//		
//		System.out.println(s);
		
		//JSONArray ans = bf(getType("621499171"), "621499171", getType("2100837269"), "2100837269");
		//System.out.println(ans);
		
//		tt("2147152072", "189831743");
		ttlong("2147152912", "307743305");
		
//		String s = "{  \"expr\" : \"RId=2100837269\",  \"entities\" :   [{ \"logprob\":-14.932, \"Id\":2288801195 },   { \"logprob\":-16.757, \"Id\":2046176730 },   { \"logprob\":-16.982, \"Id\":1966758700 },   { \"logprob\":-17.045, \"Id\":2043856196 },   { \"logprob\":-17.090, \"Id\":2055155126 },   { \"logprob\":-17.118, \"Id\":2144447780 },   { \"logprob\":-17.206, \"Id\":1969458273 },   { \"logprob\":-17.244, \"Id\":2044853699 },   { \"logprob\":-17.322, \"Id\":1991631628 },   { \"logprob\":-17.322, \"Id\":2104305294 },   { \"logprob\":-17.343, \"Id\":2039393024 },   { \"logprob\":-17.360, \"Id\":2108733083 },   { \"logprob\":-17.386, \"Id\":2093573816 },   { \"logprob\":-17.386, \"Id\":2099907467 },   { \"logprob\":-17.401, \"Id\":2016693266 },   { \"logprob\":-17.410, \"Id\":2150893441 },   { \"logprob\":-17.446, \"Id\":1987453246 },   { \"logprob\":-17.473, \"Id\":2172006543 },   { \"logprob\":-17.477, \"Id\":576420132 },   { \"logprob\":-17.499, \"Id\":2079482620 },   { \"logprob\":-17.512, \"Id\":2066180106 },   { \"logprob\":-17.555, \"Id\":2139781265 },   { \"logprob\":-17.561, \"Id\":2102610155 },   { \"logprob\":-17.566, \"Id\":2062030121 },   { \"logprob\":-17.568, \"Id\":2012116939 },   { \"logprob\":-17.581, \"Id\":2095989309 },   { \"logprob\":-17.593, \"Id\":2072526806 },   { \"logprob\":-17.612, \"Id\":1613446635 },   { \"logprob\":-17.626, \"Id\":1973383826 },   { \"logprob\":-17.631, \"Id\":2038818582 },   { \"logprob\":-17.634, \"Id\":2083904901 },   { \"logprob\":-17.655, \"Id\":1974560842 },   { \"logprob\":-17.687, \"Id\":2111877521 },   { \"logprob\":-17.689, \"Id\":2012671659 },   { \"logprob\":-17.698, \"Id\":2049827480 },   { \"logprob\":-17.725, \"Id\":1975355256 },   { \"logprob\":-17.735, \"Id\":2101871915 },   { \"logprob\":-17.749, \"Id\":2042978244 },   { \"logprob\":-17.755, \"Id\":2041270901 },   { \"logprob\":-17.775, \"Id\":133253872 },   { \"logprob\":-17.782, \"Id\":2052922251 },   { \"logprob\":-17.786, \"Id\":1985585224 },   { \"logprob\":-17.786, \"Id\":2292970177 },   { \"logprob\":-17.800, \"Id\":2060786724 },   { \"logprob\":-17.801, \"Id\":1483301458 },   { \"logprob\":-17.802, \"Id\":1969334218 },   { \"logprob\":-17.834, \"Id\":2093586876 },   { \"logprob\":-17.840, \"Id\":2115003030 },   { \"logprob\":-17.848, \"Id\":2027736364 },   { \"logprob\":-17.868, \"Id\":2054326159 },   { \"logprob\":-17.878, \"Id\":1527612695 },   { \"logprob\":-17.888, \"Id\":2125563018 },   { \"logprob\":-17.893, \"Id\":2135938816 },   { \"logprob\":-17.898, \"Id\":2062818585 },   { \"logprob\":-17.899, \"Id\":1965027673 },   { \"logprob\":-17.905, \"Id\":1992497040 },   { \"logprob\":-17.909, \"Id\":164369306 }";
//		
		
		
	}
	
	void tt(String id1, String id2) throws ClientProtocolException, IOException {
		Parallel parallel = new Parallel(getType(id1), id1, getType(id2), id2);

		JSONArray ans = parallel.solve();
		System.out.println(ans);
		
//		parallel.test();
	}
	void ttlong(String id1, String id2) throws ClientProtocolException, IOException {
		ParalleLong parallel = new ParalleLong(getType(id1), id1, getType(id2), id2);

		JSONArray ans = parallel.solve();
		System.out.println(ans);
		
//		parallel.test();
	}
	
	IdType getType(String id) throws ClientProtocolException, IOException {
		if(isId(id)) {
			return IdType.id;
		} else {
			return IdType.auid;
		}
	}
	
	public class Node {
		IdType type;
		String id;
		Integer lv;
		ArrayList<String> path;
		
		public Node(IdType type, String id, int lv) {
			this.type = type;
			this.id = id;
			this.lv = lv;
		}
	}
	
	void updateSet(JSONArray ja, String key, IdType type, HashSet<String> s) {
		for(int i=0; i<ja.size(); ++i) {
			JSONObject jo = ja.getJSONObject(i);
			if(jo.containsKey(key)) {
				s.add(type + "-" + jo.get(key));
			}
		}
	}
	
	void auUpdateSet(JSONArray ja, HashSet<String> s) {
		for(int i=0; i<ja.size(); ++i) {
			JSONObject x = ja.getJSONObject(i);
			
			if(x.containsKey("AA")) {	
				JSONArray afids = x.getJSONArray("AA");
				updateSet(afids, "AfId", IdType.afid, s);
			}
			if(x.containsKey("Id")) {
				s.add(IdType.id + "-" + x.getString("Id"));
			}
		}
	}
	
	
	private void idUpdateSet(JSONArray ja, HashSet<String> s) {
		for(int i=0; i<ja.size(); ++i) {
			JSONObject x = ja.getJSONObject(i);
			
			if(x.containsKey("AA")) {	
				JSONArray auids = x.getJSONArray("AA");
				updateSet(auids, "AuId", IdType.auid, s);
			}
			if(x.containsKey("F")) {
				JSONArray fids 	= x.getJSONArray("F");
				updateSet(fids, "FId", IdType.fid, s);
			}
			if(x.containsKey("J")) {
				JSONObject jid = x.getJSONObject("J");
				s.add(IdType.jid + "-" + jid.getString("JId"));
			}
			if(x.containsKey("C")) {
				JSONObject cid = x.getJSONObject("C");
				s.add(IdType.cid + "-" + cid.getString("CId"));
			}
		}
	}
	
	void add(Node x, IdType typeY, String idY, IdType type2, String id2, 
			ArrayList<JSONArray> v, Queue<Node> q, HashSet<String> s, 
			HashMap<String, ArrayList<JSONArray>> mp, 
			JSONArray ret, HashMap<String, TreeSet<String>> pre) {
		String strY = typeY + "-" + (x.lv + 1) + "-" + idY;
		String keyY = typeY + "-" + idY;
		
		if(typeY == type2 && idY.equals(id2)) { //Y就是终点
			for(JSONArray it : v) {
				JSONArray newAns = JSONArray.fromObject(it);
				newAns.add(id2);
				ret.add(newAns);
			}
		}
		
		if(s.contains(keyY)) { //再走一步能到终点
			for(JSONArray it : v) {
				JSONArray newAns = JSONArray.fromObject(it);
				newAns.add(idY);
				newAns.add(id2);
				ret.add(newAns);
			}
			if(x.lv == 1) return;
		}
		
		if(x.lv == 1) {
			return;
		}
		
		if(!mp.containsKey(strY)) {
			mp.put(strY, new ArrayList<JSONArray>());
			pre.put(strY, new TreeSet<String>());
			q.add(new Node(typeY, idY, x.lv + 1));
		}
		if(!pre.get(strY).contains(x.id)) {
			pre.get(strY).add(x.id);
			ArrayList<JSONArray> list = mp.get(strY);
			for(JSONArray it : v) {
				JSONArray newPath = JSONArray.fromObject(it);
				newPath.add(idY);
				list.add(newPath);
			}
		}
	}
	
	JSONArray bf(IdType type1, String id1, IdType type2, String id2) throws ClientProtocolException, IOException {
		System.out.println("BF " + type1 + "-" + id1 + " " + type2 + "-" + id2);
		
		HashMap<String, TreeSet<String>> pre = new HashMap<>();
		HashSet<String> s = new HashSet<>();
		JSONArray ret = new JSONArray();
		
		if(type2 == IdType.id) {
			JSONArray ja = get("Id", id2, ID_2_ID);
			idUpdateSet(ja, s);
			ja = get("RId", id2, "Id");
			System.out.println("ja-length: " + ja.size());
			for(int i=0; i<ja.size(); ++i) {
				s.add(IdType.id + "-" + ja.getJSONObject(i).getString("Id"));
			}
		} else {
			JSONArray ja = get("AA.AuId", id2, AUID_ATTRIBUTES);
			auUpdateSet(ja, s);
		}
		
		System.out.println("size: " + s.size());
		
		HashMap<String, ArrayList<JSONArray>> mp = new HashMap<>();
		Queue<Node> q = new LinkedList<>();
		ArrayList<JSONArray> path = new ArrayList<>();
		JSONArray tmp = new JSONArray();
		tmp.add(id1);
		path.add(tmp);
		mp.put(type1+"-0-"+id1, path);
		q.add(new Node(type1, id1, 0));
		while(!q.isEmpty()) {
			Node x = q.poll();
			System.out.println(x.type + "-" + x.id + "-" + x.lv);
			
			if(x.type == IdType.id) {
				JSONArray ja = get("Id", x.id, ID_ATTRIBUTES);
				for(int i=0; i<ja.size(); ++i) {
					JSONObject xx = ja.getJSONObject(i);
					
					path = mp.get(x.type + "-" + x.lv + "-" + x.id);
					
					if(xx.containsKey("RId")) {
						JSONArray rids = xx.getJSONArray("RId");
						for(int j=0; j<rids.size(); ++j) {
							add(x, IdType.id, rids.getString(j), type2, id2, path, q, s, mp, ret, pre);
						}
					}
					if(xx.containsKey("AA")) {	
						JSONArray auids = xx.getJSONArray("AA");
						for(int j=0; j<auids.size(); ++j) {
							JSONObject jo = auids.getJSONObject(j);
							if(jo.containsKey("AuId")) {
								add(x, IdType.auid, jo.getString("AuId"), type2, id2, path, q, s, mp, ret, pre);
							}
						}
					}
					if(xx.containsKey("F")) {
						JSONArray fids 	= xx.getJSONArray("F");
						for(int j=0; j<fids.size(); ++j) {
							JSONObject jo = fids.getJSONObject(j);
							if(jo.containsKey("FId")) {
								add(x, IdType.fid, jo.getString("FId"), type2, id2, path, q, s, mp, ret, pre);
							}
						}
					}
					if(xx.containsKey("J")) {
						JSONObject jid = xx.getJSONObject("J");
						add(x, IdType.jid, jid.getString("JId"), type2, id2, path, q, s, mp, ret, pre);
					}
					if(xx.containsKey("C")) {
						JSONObject cid = xx.getJSONObject("C");
						add(x, IdType.cid, cid.getString("CId"), type2, id2, path, q, s, mp, ret, pre);
					}
				}
			} else if(x.type == IdType.auid) {
				
				JSONArray ja = get("AA.AuId", x.id, AUID_ATTRIBUTES);
				
				for(int i=0; i<ja.size(); ++i) {
					JSONObject xx = ja.getJSONObject(i);
					
					if(xx.containsKey("AA")) {
						JSONArray afids = xx.getJSONArray("AA");
						for(int j=0; j<afids.size(); ++j) {
							JSONObject jo = afids.getJSONObject(j);
							if(jo.containsKey("AfId")) {
								add(x, IdType.afid, jo.getString("AfId"), type2, id2, path, q, s, mp, ret, pre);
							}
						}
					}
					if(xx.containsKey("Id")) {
						add(x, IdType.id, xx.getString("Id"), type2, id2, path, q, s, mp, ret, pre);
					}
				}
				
			} else if(x.type == IdType.afid) {
				
				JSONArray ja = get("AA.AfId", x.id, AFID_ATTRIBUTES);

				for(int i=0; i<ja.size(); ++i) {
					JSONObject xx = ja.getJSONObject(i);
					
					if(xx.containsKey("AA")) {
						JSONArray afids = xx.getJSONArray("AA");
						for(int j=0; j<afids.size(); ++j) {
							JSONObject jo = afids.getJSONObject(j);
							if(jo.containsKey("AuId")) {
								add(x, IdType.auid, jo.getString("AuId"), type2, id2, path, q, s, mp, ret, pre);
							}
						}
					}
					
				}
				
			} else if(x.type == IdType.cid) {
				
				JSONArray ja = get("C.CId", x.id, CID_ATTRIBUTES);

				for(int i=0; i<ja.size(); ++i) {
					JSONObject xx = ja.getJSONObject(i);
					
					if(xx.containsKey("Id")) {
						add(x, IdType.id, xx.getString("Id"), type2, id2, path, q, s, mp, ret, pre);
					}
					
				}
				
			} else if(x.type == IdType.fid) {

				JSONArray ja = get("F.FId", x.id, FID_ATTRIBUTES);

				for(int i=0; i<ja.size(); ++i) {
					JSONObject xx = ja.getJSONObject(i);
					
					if(xx.containsKey("Id")) {
						add(x, IdType.id, xx.getString("Id"), type2, id2, path, q, s, mp, ret, pre);
					}
					
				}
				
			} else if(x.type == IdType.jid) {

				JSONArray ja = get("J.JId", x.id, JID_ATTRIBUTES);

				for(int i=0; i<ja.size(); ++i) {
					JSONObject xx = ja.getJSONObject(i);
					
					if(xx.containsKey("Id")) {
						add(x, IdType.id, xx.getString("Id"), type2, id2, path, q, s, mp, ret, pre);
					}
					
				}
				
			}
		}
		
		return ret;
	}
	
	JSONArray get(String type, String id, String attributes) throws ClientProtocolException, IOException {
		JSONObject obj = JSONObject.fromObject(getEntity(setQuery(type, id), setCount(500000), attributes));
		return JSONArray.fromObject(obj.get("entities"));
	}
	
	String getEntity(String query, String cnt, String attributes) throws ClientProtocolException, IOException {
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
	
	public boolean isId(String id) throws ClientProtocolException, IOException {
		return !getEntity(setQuery("AA.AuId", id), setCount(1), "Id").contains("logprob");
	}
	
	public String httpGet(String url) throws IOException {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		System.out.println(request.getURI());
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
			System.out.println(e.getMessage());
		}
		System.out.println("length : " + os.size());
		
		client.close();
		return os.toString();
	}
}
