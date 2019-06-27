package com.expand;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import com.expand.Uuid;

public class FileUpload {
	public static String upload(HttpServletRequest request, FileItem fileItem, String fileName,String path) {
		String url = "";
		try {
			if(fileName == null || "".equals(fileName) || fileName == ""){
				fileName = fileItem.getName();
				
				System.out.println("原文件名：" + fileName);// Koala.jpg

				String suffix = fileName.substring(fileName.lastIndexOf('.'));
				System.out.println("扩展名：" + suffix);// .jpg
				
				fileName = Uuid.next() + suffix;
				System.out.println("新文件名：" + fileName);
			}

			// 5. 调用FileItem的write()方法，写入文件
			File file = new File(request.getSession().getServletContext().getRealPath("/") + path + fileName);
			//File file = new File(request.getSession().getServletContext().getRealPath("/")  + fileName);
			fileItem.write(file);
			url = file.getAbsolutePath();
			Pattern pattern = Pattern.compile("/upload.*");
			Matcher matcher = pattern.matcher(url);

			if (matcher.find()) {
				url  = matcher.group();
			}
			//url = url.replace("dflc_java","zs"); //替换url
			System.out.println(url);
			fileItem.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url ;
	}
}
