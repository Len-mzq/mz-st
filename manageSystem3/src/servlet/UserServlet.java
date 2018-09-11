package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserDao;
import util.CreateMD5;
import util.RandomNumber;
import util.ValidateCode;

public class UserServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		String type = request.getParameter("type");
		if (type == null) {
			showLogin(request, response);
		} else if ("doLogin".equals(type)) {
			doLogin(request, response);
		} else if ("showRegister".equals(type)) {
			showRegister(request, response);
		} else if ("doRegister".equals(type)) {
			doRegister(request, response);
		} else if ("randomImage".equals(type)) {
			randomImage(request, response);
		}
	}

	private void showRegister(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.getRequestDispatcher("WEB-INF/login/Register.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void doRegister(HttpServletRequest request, HttpServletResponse response) {
		try {
			Date d = new Date(); // �������ڶ���
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // ����ģʽ,������ʽ������
			String time = CreateMD5.encryptionByMD5(sdf.format(d)); // ���ո���ģʽ��ʽ�����ڶ���,�������ڽ���MD5����
			PrintWriter out = response.getWriter();
			String user = request.getParameter("username");
			String password = request.getParameter("password");
			String psw = CreateMD5.encryptionByMD5(password + time);
			UserDao ud = new UserDao();
			boolean flag = ud.searchUserName(user);// flag����true��˵���û����Ѵ���
			if (user.equals("")) {
				String data = "username";
				out.print(data);
			} else if (password.equals("")) {
				String data = "password";
				out.print(data);
			} else if (flag) {
				int have = 1;
				out.print(have);
			} else {// flag����false��ʱ��Ž�else
				flag = ud.add(user, psw, time);
				out.print(flag);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showLogin(HttpServletRequest request, HttpServletResponse response) {
		try {
			String name = "";
			Cookie[] cookie = request.getCookies();
			if (cookie != null) {
				for (Cookie cookies : cookie) {
					if ("username".equals(cookies.getName())) {
						name = cookies.getValue();
					}
				}
			}
			request.setAttribute("name", name);
			request.getRequestDispatcher("WEB-INF/login/Login.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doLogin(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession();// ����session����
			PrintWriter out = response.getWriter();
			String user = request.getParameter("username");
			UserDao ud = new UserDao();
			String ver = request.getParameter("verification");
			if (ver.equals(session.getAttribute("random"))) {
				boolean flag = false;
				String time = ud.searchTime(user);// �����û�������û���ע��ʱ��
				if (time != null) {
					String psw = CreateMD5.encryptionByMD5(request.getParameter("password") + time);
					flag = ud.search(user, psw);
					session.setAttribute("user", user);
					Cookie cookie = new Cookie("username", user);// ����cookie����
					cookie.setMaxAge(60 * 60 * 60 * 24);// ����cookie�����ʱ��
					response.addCookie(cookie);
				}
				out.print(flag);
			} else {
				int a = -1;
				out.print(a);
			}

			// if (flag) {
			//
			// request.getRequestDispatcher("WEB-INF/index/index.jsp").forward(request,
			// response);
			// } else {
			// request.getRequestDispatcher("WEB-INF/login/Fail.jsp").forward(request,
			// response);
			// }
		} catch (IOException e) {
			e.printStackTrace();
		}
		// catch (ServletException e) {
		// e.printStackTrace();
		// }
	}

	// ��֤��
	public void randomImage(HttpServletRequest request, HttpServletResponse response) {
		try {
			RandomNumber rn = new RandomNumber();
			// ����ҳ�治����
			response.setContentType("image/jpeg");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);

			ValidateCode vc = rn.generateImage();
			// ����OutputStream������
			ServletOutputStream outStream = response.getOutputStream();

			// ���ͼ��ҳ��
			ImageIO.write(vc.getImage(), "JPEG", outStream);// ����(����ͼƬ��ͼƬ��ʽ��ʹ���ĸ���)
			outStream.close();

			// ����֤�����SESSION
			HttpSession session = request.getSession();
			session.setAttribute("random", vc.getRand());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request, response);
	}

}
