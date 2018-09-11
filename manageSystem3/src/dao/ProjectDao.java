package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Department;
import entity.Project;
import util.JDBCUtil;

public class ProjectDao {
	List<Project> list = new ArrayList<Project>();

	// ��ѯ������Ŀ
	public List<Project> search() {
		try {
			// 2�����÷��䣬����������class.forName("");
			Class.forName("com.mysql.jdbc.Driver");
			// 3����������
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "root");
			// 4������Statement��SQL���ִ������
			Statement statement = connection.createStatement();
			// 5��ִ��SQL��䣬���õ����
			ResultSet result = statement.executeQuery("SELECT * from project");
			// 6���Խ�������д���
			while (result.next()) {
				Project dep = new Project();
				dep.setId(result.getInt("id"));
				dep.setName(result.getString("name"));
				list.add(dep);
			}
			// 7���ͷ���Դ
			connection.close();
			statement.close();
			result.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	// ��ҳ��ȡ��Ŀ��Ϣ
	public List<Project> searchByPage(int startIndex, int pageSize) {
		Connection conn = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtil.getConnection();
			String sql = "SELECT * FROM department LIMIT ?,?";
			pstat = conn.prepareStatement(sql);
			pstat.setInt(1, startIndex);
			pstat.setInt(2, pageSize);
			rs = pstat.executeQuery();
			while (rs.next()) {
				Project pro = new Project();
				pro.setId(rs.getInt("id"));
				pro.setName(rs.getString("name"));
				list.add(pro);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(conn, pstat, rs);
		}

		return list;
	}
	
	// ��ȡ��Ŀ����
	public int getCount() {
		int count = 0;
		Connection conn = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtil.getConnection();
			String sql = "SELECT COUNT(0) FROM project";
			pstat = conn.prepareStatement(sql);
			rs = pstat.executeQuery();
			while (rs.next()) {
				count = rs.getInt("COUNT(0)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(conn, pstat, rs);
		}
		return count;
	}
	
	// ��ѯѡ��Ĳ������е���Ŀ
	public List<Project> searchPro(int d_id,int startIndex, int pageSize) {
		try {
			// 2�����÷��䣬����������class.forName("");
			Class.forName("com.mysql.jdbc.Driver");
			// 3����������
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "root");
			// 4������Statement��SQL���ִ������
			Statement statement = connection.createStatement();
			// 5��ִ��SQL��䣬���õ����
			String sql = " select p.id,p.name from ((department as d join r_dep_pro as r on((d.id = r.d_id))) "
					+ "join project as p on((p.id = r.p_id))) where d.id=" + d_id + " limit "+startIndex+","+pageSize+"";

			ResultSet result = statement.executeQuery(sql);
			// 6���Խ�������д���
			while (result.next()) {
				Project dep = new Project();
				dep.setId(result.getInt("p.id"));
				dep.setName(result.getString("p.name"));
				list.add(dep);
			}
			// 7���ͷ���Դ
			connection.close();
			statement.close();
			result.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	// ��ѯѡ��Ĳ���û�е���Ŀ
	public List<Project> searchProNo(String depName) {
		List<Project> noList = new ArrayList<Project>();
		try {
			// 2�����÷��䣬����������class.forName("");
			Class.forName("com.mysql.jdbc.Driver");
			// 3����������
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "root");
			// 4������Statement��SQL���ִ������
			Statement statement = connection.createStatement();
			// 5��ִ��SQL��䣬���õ����
			String sql = " select p.id,p.name from project as p "
					+ "where p.id not in (select p.id from ((department as d join r_dep_pro as r on((d.id = r.d_id))) "
					+ "join project as p on((p.id = r.p_id))) " + "where d.name='" + depName + "') ";

			ResultSet result = statement.executeQuery(sql);
			// 6���Խ�������д���
			while (result.next()) {
				Project dep = new Project();
				dep.setId(result.getInt("p.id"));
				dep.setName(result.getString("p.name"));
				noList.add(dep);
			}
			// 7���ͷ���Դ
			connection.close();
			statement.close();
			result.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return noList;
	}

	// ��������
	public List<Project> searchByCondition(String condition) {
		try {
			// 2�����÷��䣬����������class.forName("");
			Class.forName("com.mysql.jdbc.Driver");
			// 3����������
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "root");
			// 4������Statement��SQL���ִ������
			Statement statement = connection.createStatement();
			// 5��ִ��SQL��䣬���õ����
			String where = " where 1=1 ";
			if (!condition.equals("")) {
				where += "and name='" + condition + "'";
			}
			String sql = "SELECT * from project" + where;
			ResultSet result = statement.executeQuery(sql);
			// 6���Խ�������д���
			while (result.next()) {
				Project dep = new Project();
				dep.setId(result.getInt("id"));
				dep.setName(result.getString("name"));
				list.add(dep);
			}
			// 7���ͷ���Դ
			connection.close();
			statement.close();
			result.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	// ����������ѯ���ݿ�project���е�����
	public List<Project> searchByCondition(Project condition,int startIndex, int pageSize) {
		List<Project> listPro = new ArrayList<Project>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "root");
			Statement statement = connection.createStatement();
			String where = "where 1=1 ";
			if (condition.getName()!=null&&!condition.getName().equals("")) {
				where += "and name='"+condition.getName()+"'";
			}
			String sql = "select * from project " + where +" limit "+startIndex+","+pageSize+" ";
			ResultSet result = statement.executeQuery(sql);
			// 6���Խ�������д���
			while (result.next()) {
				Project pro = new Project();
				pro.setId(result.getInt("id"));
				pro.setName(result.getString("name"));
				listPro.add(pro);
			}
			// 7���ͷ���Դ
			connection.close();
			statement.close();
			result.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listPro;
	}

	// ����id��ѯ��Ŀ
	public List<Project> search(String id) {
		List<Project> list = new ArrayList<Project>();
	
		Connection conn = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtil.getConnection();
			String sql = "select * from project where id in(" + id + ") ";
			pstat = conn.prepareStatement(sql);
			rs = pstat.executeQuery();
			while (rs.next()) {
				Project pro = new Project();
				pro.setId(rs.getInt("id"));
				pro.setName(rs.getString("name"));
				list.add(pro);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(conn, pstat, rs);
		}

		return list;
	}
	
	// ������Ŀ������Ŀid
	public int searchId(String name) {
		int id = 0;
		Connection conn = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtil.getConnection();
			String sql = " select id from project where name in('"+name+"') ";
			pstat = conn.prepareStatement(sql);
			rs = pstat.executeQuery();
			while (rs.next()) {
				id = rs.getInt("id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(conn, pstat, rs);
		}

		return id;
	}
	
	// �����ݿ��������
	public void add(Project pro) {
		try {
			// 2�����÷��䣬����������class.forName("");
			Class.forName("com.mysql.jdbc.Driver");
			// 3����������
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "root");
			// 4������Statement��SQL���ִ������
			Statement statement = connection.createStatement();
			// 5��ִ��SQL��䣬���õ����
			String sql = "insert into project (name) values ('" + pro.getName() + "')";
			statement.executeUpdate(sql);

			// 6���Խ�������д���

			// 7���ͷ���Դ
			connection.close();
			statement.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// �޸���Ŀ
	public void update(Project pro) {
		try {
			// 2�����÷��䣬����������class.forName("");
			Class.forName("com.mysql.jdbc.Driver");
			// 3����������
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "root");
			// 4������Statement��SQL���ִ������
			Statement statement = connection.createStatement();
			// 5��ִ��SQL��䣬���õ����
			String sql = "update project set name='" + pro.getName() + "' where id=" + pro.getId() + " ";
			statement.executeUpdate(sql);
			// 6���Խ�������д���

			// 7���ͷ���Դ
			connection.close();
			statement.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// �޸���Ŀ��(������Ŀ��Ϣ��list)
	public void update(List<Project> list) {
		Connection conn = null;
		PreparedStatement pstat = null;
		try {
			conn = JDBCUtil.getConnection();
			for (int i = 0; i < list.size(); i++) {
				Project dep = list.get(i);
				String sql = "update project set name='" + dep.getName() + "'  where id in(" + dep.getId() + ") ";
				pstat = conn.prepareStatement(sql);
				pstat.executeUpdate();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// �ͷ���Դ
			JDBCUtil.close(conn, pstat, null);

		}
	}

	// �������������Ŀ(��֪��Ŀ���Ͳ���id)
	public boolean addProToDep(int d_id, int p_id) {
		boolean flag=false;
		try {
			// 2�����÷��䣬����������class.forName("");
			Class.forName("com.mysql.jdbc.Driver");
			// 3����������
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "root");
			connection.setAutoCommit(false);
			// 4������Statement��SQL���ִ������
			Statement statement = connection.createStatement();
			// 5��ִ��SQL��䣬���õ����
			String sql = "insert into r_dep_pro (d_id,p_id) values (" +d_id+ "," + p_id + ")";
			int rs = statement.executeUpdate(sql);
			// 6���Խ�������д���
			if(rs>0) {
				flag=true;
			}
			connection.commit();
			// 7���ͷ���Դ
			connection.close();
			statement.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}

	// �������������Ŀ(��֪��Ŀ���Ͳ���id)
	public boolean addProToDep2(int d_id, String p_id) {
		boolean flag=false;
		try {
			// 2�����÷��䣬����������class.forName("");
			Class.forName("com.mysql.jdbc.Driver");
			// 3����������
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "root");
			connection.setAutoCommit(false);
			// 4������Statement��SQL���ִ������
			Statement statement = connection.createStatement();
			// 5��ִ��SQL��䣬���õ����
			int rs = 0;
			String[] array = p_id.split(",");
			for (int i = 0; i < array.length; i++) {
				String sql= "insert into r_dep_pro (d_id,p_id) values (" +d_id+ "," + array[i] + ")";
				rs = statement.executeUpdate(sql);
			}
			
			// 6���Խ�������д���
			if(rs>0) {
				flag=true;
			}
			connection.commit();
			// 7���ͷ���Դ
			connection.close();
			statement.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	// ɾ����Ŀ
	public void delete(Project pro) {
		try {
			// 2�����÷��䣬����������class.forName("");
			Class.forName("com.mysql.jdbc.Driver");
			// 3����������
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "root");
			// ����
			connection.setAutoCommit(false);
			// 4������Statement��SQL���ִ������
			Statement statement = connection.createStatement();
			// 5��ִ��SQL��䣬���õ����
			statement.executeUpdate("delete from project where id=" + pro.getId() + " ");
			statement.executeUpdate("delete from r_dep_pro where p_id=" + pro.getId() + " ");
			connection.commit();
			// 6���Խ�������д���
			// 7���ͷ���Դ
			connection.close();
			statement.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// ����ɾ����Ŀ(������Ŀid)
	public void deleteBatch(String id) {
		Connection conn = null;
		PreparedStatement pstat = null;
		try {
			conn = JDBCUtil.getConnection();
			String sql1 = "delete from project where id in(" + id + ") ";
			String sql2 = "delete from r_dep_pro where p_id in(" + id + ") ";
			pstat = conn.prepareStatement(sql1);
			pstat = conn.prepareStatement(sql2);
			pstat.executeUpdate();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// �ͷ���Դ
			JDBCUtil.close(conn, pstat, null);

		}
	}
	
	// ɾ�������е���Ŀ
	public boolean deletePro(String id, String name) {
		boolean flag = false;
		try {
			// 2�����÷��䣬����������class.forName("");
			Class.forName("com.mysql.jdbc.Driver");
			// 3����������
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "root");
			// ����
			connection.setAutoCommit(false);
			// 4������Statement��SQL���ִ������
			Statement statement = connection.createStatement();
			// 5��ִ��SQL��䣬���õ����
			int rs = statement.executeUpdate("delete from r_dep_pro where p_id in(" + id + ") and d_id=(select id from department where name='"+ name + "')");
			connection.commit();
			// 6���Խ�������д���
			if(rs>0) {
				flag=true;
			}
			// 7���ͷ���Դ
			connection.close();
			statement.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	// ��ȡ��������ѯ������Ŀ����
	public int getSearchCount(Project condition) {
		int proCount = 0;
		Connection conn = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		try {
			String where = "where 1=1 ";
			if (condition.getName()!=null&&!condition.getName().equals("")) {
				where += "and name='"+condition.getName()+"'";
			}
			conn = JDBCUtil.getConnection();
			String sql = "SELECT COUNT(0) FROM project "+ where;
			pstat = conn.prepareStatement(sql);
			rs = pstat.executeQuery();
			while (rs.next()) {
				proCount = rs.getInt("COUNT(0)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(conn, pstat, rs);
		}
		return proCount;
	}	
	
		// ��ȡ��Ӧ���ŵ���Ŀ����
		public int getSearchCount(int d_id) {
			int proCount = 0;
			Connection conn = null;
			PreparedStatement pstat = null;
			ResultSet rs = null;
			try {
				String where = "where 1=1 ";
				if (d_id!=-1) {
					where += "and d_id="+d_id+"";
				}
				conn = JDBCUtil.getConnection();
				String sql = "SELECT COUNT(0) FROM r_dep_pro "+ where;
				pstat = conn.prepareStatement(sql);
				rs = pstat.executeQuery();
				while (rs.next()) {
					proCount = rs.getInt("COUNT(0)");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JDBCUtil.close(conn, pstat, rs);
			}
			return proCount;
		}	
	
}
