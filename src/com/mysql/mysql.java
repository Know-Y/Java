package com.mysql;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

public class mysql
{
	static String dir = "com.mysql.jdbc.Driver"; 
	private String user = "know";      //閺佺増宓佹惔鎾瑰閸欙拷
	private String pass = "usestudio-1";//閺佺増宓佹惔鎾崇槕閻拷
	private String url = "";
	private Connection ct; 


	//閻€劑绮拋銈囨畱鐠愶箑褰跨�靛棛鐖滈崚娑樼紦閸掕泛鐣鹃惃鍕殶閹诡喖绨遍柧鐐复
	public mysql(String ur)
	{
		this.url = ur;
		try
		{
			conn();
		}
		catch (Exception e)
		{
		}
	}
	//閸掓稑缂撴潻鐐村复
	public void conn() throws ClassNotFoundException, SQLException
	{
		Class.forName(dir); 
		ct = DriverManager.getConnection(url, user, pass); 
		if (ct.isClosed())
		{
			ct = null;
		}
	}

	
    /** 
     * 閹笛嗩攽鐎涙ê鍋嶆潻鍥┾柤瀵版鍩屾潻鏂挎礀閸婏拷 
     *  
     * @param e  閺佺増宓佹惔鎾冲棘閺侊拷
     * @return 
     */  
	public static String usDBCall(String[] e) throws SQLException
	{
		
 		if (e != null && e.length > 0)					//閸掋倖鏌囬弰顖氭儊閺堝寮弫锟�
		{ //濠㈣泛瀚幃濠囧矗閸屾稒娈�										
			int i, k, z, j = 0;
			String _P = "CALL ", _uv = "";
			Connection _ct = null;
			CallableStatement _ctm = null;
			ResultSet _us = null;
			ResultSetMetaData _res = null;
			boolean _tf;
			String _name;
			String _string;
		    Map<String, Object> map;

			for (i = 0; i < e.length; i++)					 //閹峰吋甯撮幍褑顢戠�涙ê鍋嶆潻鍥┾柤閻ㄥ嫬鐡х粭锔胯 缂佹挻鐏夋稉锟� procedurename(?1, ?2 ...)
			{
				try {
					e[i] = URLDecoder.decode(e[i], "utf-8");			//鐟欙絽鐦�
				} 
				catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				
				if (i == 2)
				{
					_P += e[2] + "(";
				}
				else if (i > 1)
				{
					_P += "?" + (i == e.length - 1 ? "" : ",");
				}
			}
			_P += ");";
			
			try
			{
				//鏉╃偞甯撮弫鐗堝祦鎼存搫绱濋懢宄板絿鏉╃偞甯村Ч锟�
				_ct = new mysql("jdbc:mysql://" + e[0] + ":3306/" + e[1] + "?characterEncoding=utf8&useSSL=true&serverTimezone=UTC&zeroDateTimeBehavior=round").ct; 
				
				if (_ct != null)
				{
					_ctm = _ct.prepareCall(_P);					//閹笛嗩攽鐎涙ê鍋嶆潻鍥┾柤
					for (i = 3; i < e.length; i++){				//閸愭瑥鍙嗛弫鐗堝祦鎼存挷绱堕崣锟�
						_ctm.setString(i-2, e[i]);
					}
					
					_tf = _ctm.execute(); 
				    JSONArray array = new JSONArray();		   //閼惧嘲褰囨潻鏂挎礀閻ㄥ嫭鏆熼幑锟�
				    
				    while (_tf)
				    {
						_us = _ctm.getResultSet();
						_res = _us.getMetaData();
						k = _res.getColumnCount();
						
						JSONArray _arr = new JSONArray();			
						while (_us.next())			//瀵邦亞骞嗙紒鎾寸亯闂嗗棴绱濇禒銉攽閻ㄥ嫭鏌熷蹇撳箵瀵邦亞骞�
						{
							map =  new HashMap<String, Object>();//閺傛澘缂撴稉锟界悰灞炬殶閹诡喚娈戦柨顔硷拷鐓庮嚠
							
							for (i = 1; i <= k; i++)		   //瀵邦亞骞嗛幎濠冩殶閹诡喖绨遍惃鍕殶閹诡喗鏂侀崷銊╂暛閸婄厧顕柌锟�	
							{
								_name = _res.getColumnLabel(i); //鐎涙顔岄崥锟�
								_string = _us.getString(i);		//缂佹挻鐏夐梿锟�
								map.put(_name, _string);
							}
							_arr.put(map);		//濞ｈ濮為崚鎵波閺嬫粓娉﹂柌锟�  缂佹挻鐏夐惄绋跨秼娴滐拷 [{"a":"a", "b":"b"},{"a":"a", "b":"b"}]
						}
						array.put(_arr);		  //濞ｈ濮為崚鏉款樋娑擃亞绮ㄩ弸婊堟肠闁诧拷  缂佹挻鐏夐惄绋跨秼娴滐拷 [[{"a":"a", "b":"b"},{"a":"a", "b":"b"}],[{"a":"a", "b":"b"},{"a":"a", "b":"b"}]]
						_tf = _ctm.getMoreResults();
				    }
				    //婵″倹鐏夋潻鏂挎礀閻ㄥ嫮绮ㄩ弸婊堟肠閿涘苯褰ч張澶夌娑擃亪鍋呮稊鍫㈡纯鏉╂柨娲栨稉锟芥稉顏嗙波閺嬫粓娉﹂惃鍒痵onstring閿涘苯鎯侀崚娆掔箲閸ョ偞澧嶉張澶屾畱jsonstring
				    if(array.length() > 1) {
				    	return array.toString();
				    }
				    else if(array.length() > 0) {
				    	return ((JSONArray)array.get(0)).toString();
				    }
				    else{
				    	return "";
				    }
				}
			}
			catch (Exception er)
			{
			    //鏉╂柨娲栭柨娆掝嚖娣団剝浼�
				return er.getMessage();
			}
			finally
			{
			    //濞撳懘娅庢潻鐐村复濮圭媴绱濋悽鍙樼艾sqlserver閻ㄥ嫯绻涢幒銉︾潨閸戣桨绠幇蹇旀灐閻ㄥ嫮菙鐎规熬绱濋幍锟芥禒銉︾槨濞喡ょ箾閹恒儵鍏橀柌濠冩杹
				if (_ct != null)
				{
					_ct.close();
				}
				if (_ctm != null)
				{
					_ctm.close();
				}
				if (_us != null)
				{
					_us.close();
				}
				_ct = null;
				_ctm = null;
				_us = null;
				_res = null;
			}
		}
		return "";
	}
}