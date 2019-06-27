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
		response.addHeader("Access-Control-Allow-Origin","*");    //���ÿ�����
		response.addHeader("Access-Control-Allow-Headers",":Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With;");
		response.setContentType("text/html;charset=utf-8;");  //���������������Ϊutf-8
		String[] v = null; //ǰ�˴����̨�Ĳ���
		String _s = "";    //��������������ݿⷵ��ֵ
		//PrintWriter _o = response.getWriter(); //�������
		Map<String, String[]> _p = request.getParameterMap(); //��ȡֵ
		try {
			String _str = usGetParame(_p);  //��ȡmode����
			if(_str!=""){			//�ж�����mode����
				v = usSplitParame(_str, ',' , true); //����ַ���
				_s = mysql.usDBCall(v); 	//�������ݿ�õ�����ֵ
				zipString(_s,response);   //�ַ���ѹ��
			}
	    } catch (SQLException e) { 	
	    	_s = e.getMessage();  //���ش�����Ϣ
	    }
	}
	
    /** 
     * ѹ�� 
     *  
     * @param str  ���ݿⷵ��ֵ
     * @param response 
     * @return 
     * @throws Exception 
     */  
	
	private String zipString(String str, HttpServletResponse response){
		try {
			byte[] data = str.getBytes(ENCODING);    //���ַ���ת��byte
			byte[] output;
			output = compress(data);   //ѹ���ַ�
			// ����Content-Encoding�����ǹؼ��㣡  
            response.setHeader("Content-Encoding", "gzip");  
            // �����ַ���  
            response.setCharacterEncoding(ENCODING);  
            // �趨����������ݳ���  
            response.setContentLength(output.length);  
  
            OutputStream out = response.getOutputStream();   // os��������ַ������ݻ��߶����Ƶ��ֽ�������
            out.write(output);    //��ӡ����
            out.flush();  		
            out.close();  	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  	
		return "";
		
	}
	
    /** 
     * ѹ�� 
     *  
     * @param data 
     * @return 
     * @throws Exception 
     */  
    private byte[] compress(byte[] data) throws Exception {  
    	  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  //�����
  
        // ѹ��  
        GZIPOutputStream gos = new GZIPOutputStream(baos);  
  
        gos.write(data, 0, data.length);  
  
        gos.finish();  
  
        byte[] output = baos.toByteArray();  
  
        baos.flush();  
        baos.close();  			//�ر�ѹ����
  
        return output;  
    }  
    
    /** 
     * ���ղ��� 
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
     * �ַ���ת���� 
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
