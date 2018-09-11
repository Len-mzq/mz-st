package servlet;

import java.io.File;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeletePicsFormServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		String type = request.getParameter("type");
		if(type==null) {
			delete(request, response);
		}
	}

	public void delete(HttpServletRequest request, HttpServletResponse response) {
		String picname = request.getParameter("fileName"); // ���ղ���
		File srcFolder = new File("d:/tu");
		File[] fileArray = srcFolder.listFiles();
		if (fileArray != null) {
			// ������File���飬�õ�ÿһ��File����
			for (File file : fileArray) {
				if (file.getName().equals(picname)) {
					file.delete();
				}
			}
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request, response);
	}

}
