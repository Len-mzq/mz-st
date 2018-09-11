package servlet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

public class UploadServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			// String path = request.getServletContext().getRealPath("/") + "/pic";

			String path = "d:/tu/";
			//����һ��DiskFileItemFactory������
			FileItemFactory factory = new DiskFileItemFactory();// Ϊ�����󴴽�һ��DiskFileItemFactory����ͨ��������������ִ�н��������еı���Ŀ��������һ��List�С�
			//����һ��ServletFileUpload���Ķ���
			ServletFileUpload upload = new ServletFileUpload(factory);
			//����request���󣬵õ�һ��������
			//��Ϊ�汾���⵼�£�parseRequest����Ӧ����request����һ�������ٴ���ȥ
			//List<FileItem> items = upload.parseRequest(request);
			List<FileItem> items = upload.parseRequest(new ServletRequestContext(request));

			for (int i = 0; i < items.size(); i++) {
				FileItem item = items.get(i);
				if (item.getFieldName().equals("myPicture")) {
					UUID uuid = UUID.randomUUID();
					String houzhui = item.getName().substring(item.getName().lastIndexOf("."));
					File savedFile = new File(path, uuid.toString() + houzhui);
					item.write(savedFile);
				} 
			}

		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request, response);

	}
}
