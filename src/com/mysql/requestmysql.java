package com.mysql;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.mysql;

/**
 * Servlet implementation class ajax
 */
@WebServlet("/requestmysql")
public class requestmysql extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static final String ENCODING = "UTF-8";  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public requestmysql() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		GetPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		GetPost(request, response);
	}
	private void GetPost(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		response.addHeader("Access-Control-Allow-Origin","*");    //设置跨域处理
		response.addHeader("Access-Control-Allow-Headers",":Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With;");
		response.setContentType("text/html;charset=utf-8;");  //将浏览器编码设置为utf-8
		String[] v = null; //前端传入后台的参数
		String _s = "";    //定义变量接收数据库返回值
		//PrintWriter _o = response.getWriter(); //输出区域
		Map<String, String[]> _p = request.getParameterMap(); //获取值
		try {
			String _str = usGetParame(_p);  //获取mode参数
			if(_str!=""){			//判断有无mode参数
				v = usSplitParame(_str, ',' , true); //拆分字符串
				_s = mysql.usDBCall(v); 	//请求数据库得到返回值
				zipString(_s,response);   //字符串压缩
			}
	    } catch (SQLException e) { 	
	    	_s = e.getMessage();  //返回错误信息
	    }
	}
	
    /** 
     * 压缩 
     *  
     * @param str  数据库返回值
     * @param response 
     * @return 
     * @throws Exception 
     */  
	
	private String zipString(String str, HttpServletResponse response){
		try {
			byte[] data = str.getBytes(ENCODING);    //将字符串转成byte
			byte[] output;
			output = compress(data);   //压缩字符
			// 设置Content-Encoding，这是关键点！  
            response.setHeader("Content-Encoding", "gzip");  
            // 设置字符集  
            response.setCharacterEncoding(ENCODING);  
            // 设定输出流中内容长度  
            response.setContentLength(output.length);  
  
            OutputStream out = response.getOutputStream();   // os用于输出字符流数据或者二进制的字节流数据
            out.write(output);    //打印数据
            out.flush();  		
            out.close();  	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  	
		return "";
		
	}
	
    /** 
     * 压缩 
     *  
     * @param data 
     * @return 
     * @throws Exception 
     */  
    private byte[] compress(byte[] data) throws Exception {  
    	  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  //输出流
  
        // 压缩  
        GZIPOutputStream gos = new GZIPOutputStream(baos);  
  
        gos.write(data, 0, data.length);  
  
        gos.finish();  
  
        byte[] output = baos.toByteArray();  
  
        baos.flush();  
        baos.close();  			//关闭压缩流
  
        return output;  
    }  
    
    /** 
     * 接收参数 
     *  
     * @param str 	 
     * @return 
     * @throws Exception 
     */  
	private String usGetParame(Map<String, String[]> str)
	{
		if(str.get("mode") != null){ 
			return str.get("mode")[0];
		}
        return "";
    }
	
    /** 
     * 字符串转数组 
     *  
     * @param str 	 
     * @return 
     * @throws Exception 
     */  
	private String[] usSplitParame(String str, char separatorChar, boolean preserveAllTokens)
	{
        // Performance tuned for 2.0 (JDK1.4)
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return null;
        }
        List list = new ArrayList();
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match || preserveAllTokens) {
                    list.add(str.substring(start, i));
                    match = false;
                    lastMatch = true;
                }
                start = ++i;
                continue;
            }
            lastMatch = false;
            match = true;
            i++;
        }
        if (match || (preserveAllTokens && lastMatch)) {
            list.add(str.substring(start, i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

}
