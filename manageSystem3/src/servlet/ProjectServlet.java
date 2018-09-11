package servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import dao.ProjectDao;
import entity.Project;
import vo.PageVo;

public class ProjectServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
				request.setCharacterEncoding("utf-8");
				String type = request.getParameter("type");
				if (type == null || "search".equals(type)) {
					search(request, response);
				} else if ("showAdd".equals(type)) {
					showAdd(request, response);
				} else if ("doAdd".equals(type)) {
					doAdd(request, response);
				} else if ("doDelete".equals(type)) {
					doDelete(request, response);
				} else if ("showUpdate".equals(type)) {
					showUpdate(request, response);
				} else if ("doUpdate".equals(type)) {
					doUpdate(request, response);
				} else if ("doUpdate3".equals(type)) {
					doUpdate3(request, response);
				}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showProjectByPage(HttpServletRequest request, HttpServletResponse response) {
		try {
			// ��ʾԱ���б�
			String pageNoStr = request.getParameter("pageNo"); // ���ղ���
			int pageNo = 1;
			if (StringUtils.isNotEmpty(pageNoStr)) {
				pageNo = Integer.parseInt(pageNoStr);
			}
			// �����ǰҳ��С��1��Ĭ����ʾ��һҳ
			if (StringUtils.isNotEmpty(pageNoStr) && Integer.parseInt(pageNoStr) < 1) {
				pageNo = 1;
			}

			ProjectDao prod = new ProjectDao();
			PageVo<Project> pagevo = new PageVo<Project>();
			int count = prod.getCount(); // ��ȡ��Ŀ����
			pagevo.setTotalRecords(count);// ��䵽pagevo��
			// �����ǰҳ��������ҳ����Ĭ����ʾ���һҳ
			if (StringUtils.isNotEmpty(pageNoStr) && pageNo > pagevo.getTotalPageSize()) {
				pageNo = pagevo.getTotalPageSize();
			}
			pagevo.setPageNo(pageNo);
			List<Project> list = prod.searchByPage(pagevo.getStartIndex(), pagevo.getPageSize());// ��ȡÿҳԱ���б�
			pagevo.setRecords(list);
			pagevo.setUrl("project?pageNo=");// ����ҳ��
			request.setAttribute("pagevo", pagevo);
			request.getRequestDispatcher("WEB-INF/project/ProjectView.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showAdd(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.getRequestDispatcher("WEB-INF/project/ProjectAdd.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doAdd(HttpServletRequest request, HttpServletResponse response) {

		ProjectDao prod = new ProjectDao();
		String name = request.getParameter("name");
		Project pro = new Project();
		pro.setName(name);
		prod.add(pro);

		try {
			response.sendRedirect("project");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doDelete(HttpServletRequest request, HttpServletResponse response) {
		try {
			ProjectDao prod = new ProjectDao();
			String ID = request.getParameter("selectId"); // ��ȡѡ�е���Ŀid
			prod.deleteBatch(ID);
			response.sendRedirect("project");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showUpdate(HttpServletRequest request, HttpServletResponse response) {
		String pageNoStr = request.getParameter("pageNo"); // ���ղ���
		int pageNo = 1;
		if (StringUtils.isNotEmpty(pageNoStr)) {
			pageNo = Integer.parseInt(pageNoStr);
		}
		try {
			String ID = request.getParameter("selectId"); // ��ȡѡ�еĲ���id
			ProjectDao prod = new ProjectDao();
			List<Project> proList = prod.search(ID);

			PageVo<Project> pagevo = new PageVo<Project>();
			pagevo.setPageNo(pageNo);
			request.setAttribute("proList", proList);
			request.getRequestDispatcher("WEB-INF/project/ProjectUpdate.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showUpdate2(HttpServletRequest request, HttpServletResponse response) {
		try {
			String ids = request.getParameter("selectId"); // ��ȡѡ�е�Ա��id
			ProjectDao prod = new ProjectDao();
			List<Project> proList = prod.search(ids);

			request.setAttribute("pro", proList.get(0));
			request.setAttribute("ids", ids);
			request.getRequestDispatcher("WEB-INF/project/update2.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doUpdate(HttpServletRequest request, HttpServletResponse response) {
		try {
			String pageNo = request.getParameter("pageNo");
			ProjectDao prod = new ProjectDao();
			String[] id = request.getParameterValues("id");
			String[] name = request.getParameterValues("name");
			Project pro = new Project();
			for (int i = 0; i < id.length; i++) {
				pro.setId(Integer.parseInt(id[i]));
				pro.setName(name[i]);
				prod.update(pro);
			}

			response.sendRedirect("project?pageNo=" + pageNo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doUpdate3(HttpServletRequest request, HttpServletResponse response) {
		try {
			String pros = request.getParameter("pros");
			String pageNo = request.getParameter("pageNo");
			String[] array = pros.split(";");
			List<Project> list = new ArrayList<Project>();
			for (int i = 0; i < array.length; i++) {
				String[] tproArray = array[i].split(",");
				Project pro = new Project();
				pro.setId(Integer.parseInt(tproArray[0]));
				pro.setName(tproArray[1]);
				list.add(pro);
			}
			ProjectDao prod = new ProjectDao();
			prod.update(list);

			response.sendRedirect("project?pageNo=" + pageNo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void search(HttpServletRequest request, HttpServletResponse response) {
		try {
			// ��ʾ��Ŀ�б�
			String name = request.getParameter("name"); // ���ղ���
			String pageNoStr = request.getParameter("pageNo"); // ���ղ���
			int pageNo = 1;
			if (StringUtils.isNotEmpty(pageNoStr)) {
				pageNo = Integer.parseInt(pageNoStr);
			}
			// �����ǰҳ��С��1��Ĭ����ʾ��һҳ
			if (StringUtils.isNotEmpty(pageNoStr) && Integer.parseInt(pageNoStr) < 1) {
				pageNo = 1;
			}

			ProjectDao prod = new ProjectDao();
			PageVo<Project> pagevo = new PageVo<Project>();
			Project pro = new Project();
			pro.setName(name);
			int proCount = prod.getSearchCount(pro); // ��ȡ��Ŀ����
			pagevo.setTotalRecords(proCount);// ��䵽pagevo��
			// �����ǰҳ��������ҳ����Ĭ����ʾ���һҳ
			if (StringUtils.isNotEmpty(pageNoStr) && pageNo > pagevo.getTotalPageSize()) {
				pageNo = pagevo.getTotalPageSize();
			}
			pagevo.setPageNo(pageNo);
			List<Project> list = prod.searchByCondition(pro, pagevo.getStartIndex(), pagevo.getPageSize());
			pagevo.setRecords(list);
			pagevo.setUrl("project?");// ����ҳ��
			request.setAttribute("pro", pro);
			request.setAttribute("pagevo", pagevo);
			request.getRequestDispatcher("WEB-INF/project/ProjectView.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request, response);
	}
}
