package pure.sauce.service;

import java.io.*;
import java.net.*;
import java.util.*;

import net.sf.json.JSONArray;

public class testSolve {
	public static void main(String[] args) {
		// 发送 GET 请求
		String s = HttpRequest.sendGet("http://localhost:8080/PureSauceService/services/rest/get");
		System.out.println(s);

		// 发送 POST 请求
		Long[] a = {1111L, 2222L};
		JSONArray obj = JSONArray.fromObject(a);
		System.out.println("post: " + obj);
		String sr = HttpRequest.sendPost("http://localhost:8080/PureSauceService/services/rest/solve", obj);
		System.out.println(sr);
	}
	

	public static class HttpRequest {
		/**
		 * 向指定URL发送GET方法的请求
		 * 
		 * @param url
		 *            发送请求的URL
		 * @param param
		 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
		 * @return URL 所代表远程资源的响应结果
		 */
		public static String sendGet(String url) {
			String result = "";
			BufferedReader in = null;
			try {
				String urlNameString = url;
				URL realUrl = new URL(urlNameString);
				// 打开和URL之间的连接
				URLConnection connection = realUrl.openConnection();
				// 设置通用的请求属性
				connection.setRequestProperty("accept", "*/*");
				connection.setRequestProperty("connection", "Keep-Alive");
				connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
				// 建立实际的连接
				connection.connect();
				// 获取所有响应头字段
				Map<String, List<String>> map = connection.getHeaderFields();
				// 遍历所有的响应头字段
				for (String key : map.keySet()) {
					System.out.println(key + "--->" + map.get(key));
				}
				// 定义 BufferedReader输入流来读取URL的响应
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
			} catch (Exception e) {
				System.out.println("发送GET请求出现异常！" + e);
				e.printStackTrace();
			}
			// 使用finally块来关闭输入流
			finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			return result;
		}

		/**
		 * 向指定 URL 发送POST方法的请求
		 * 
		 * @param url
		 *            发送请求的 URL
		 * @param param
		 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
		 * @return 所代表远程资源的响应结果
		 */
		public static String sendPost(String ADD_URL, JSONArray obj) {
			String ret = "failed";
			try { 
	            //创建连接 
	            URL url = new URL(ADD_URL); 
	            HttpURLConnection connection = (HttpURLConnection) url 
	                    .openConnection(); 
	            connection.setDoOutput(true); 
	            connection.setDoInput(true); 
	            connection.setRequestMethod("POST"); 
	            connection.setUseCaches(false); 
	            connection.setInstanceFollowRedirects(true); 
	            connection.setRequestProperty("Content-Type", 
	                    "application/json"); 

	            connection.connect(); 

	            //POST请求 
	            DataOutputStream out = new DataOutputStream( 
	                    connection.getOutputStream()); 

	            out.writeBytes(obj.toString()); 
	            out.flush(); 
	            out.close(); 

	            //读取响应 
	            BufferedReader reader = new BufferedReader(new InputStreamReader( 
	                    connection.getInputStream())); 
	            String lines; 
	            StringBuffer sb = new StringBuffer(""); 
	            while ((lines = reader.readLine()) != null) { 
	                lines = new String(lines.getBytes(), "utf-8"); 
	                sb.append(lines); 
	            } 
	            ret = sb.toString();
	            reader.close(); 
	            // 断开连接 
	            connection.disconnect(); 
	        } catch (MalformedURLException e) { 
	            e.printStackTrace(); 
	        } catch (UnsupportedEncodingException e) { 
	            e.printStackTrace(); 
	        } catch (IOException e) { 
	            e.printStackTrace(); 
	        }
			return ret;
		}
	}

}