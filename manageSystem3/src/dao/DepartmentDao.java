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
import entity.Employee;
import entity.Department;
import entity.Project;
import util.JDBCUtil;

public class DepartmentDao {
	List<Department> list = new ArrayList<Department>();

	// ��ѯ���ݿ�
	public List<Department> search() {
		try {
			// 2�����÷��䣬����������class.forName("");
			Class.forName("com.mysql.jdbc.Driver");
			// 3����������
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "root");
			// 4������Statement��SQL���ִ������
			Statement statement = connection.createStatement();
			// 5��ִ��SQL��䣬���õ����
			ResultSet result = statement.executeQuery(
					"SELECT d.id,d.name,COUNT(e.id) from department as d LEFT JOIN employee as e on d_id=d.id GROUP BY d.id");
			// 6���Խ�������д���
			while (result.next()) {
				Department dep = new Department();
				dep.setId(result.getInt("d.id"));
				dep.setName(result.getString("d.name"));
				dep.setCount(result.getInt("COUNT(e.id)"));
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

		// ����id��ѯ����
		public List<Department> search(String id) {
			List<Department> list = new ArrayList<Department>();
		
			Connection conn = null;
			PreparedStatement pstat = null;
			ResultSet rs = null;
			try {
				conn = JDBCUtil.getConnection();
				String sql = "select * from department where id in(" + id + ") ";
				pstat = conn.prepareStatement(sql);
				rs = pstat.executeQuery();
				while (rs.next()) {
					Department emp = new Department();
					emp.setId(rs.getInt("id"));
					emp.setName(rs.getString("name"));
					emp.setCount(rs.getInt("emp_count"));
					list.add(emp);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JDBCUtil.close(conn, pstat, rs);
			}

			return list;
		}
	
	// ����id��ѯ������
	public String search(int id) {
		String name = null;
		Connection conn = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtil.getConnection();
			String sql = "select name from department where id =" + id + " ";
			pstat = conn.prepareStatement(sql);
			rs = pstat.executeQuery();
			while (rs.next()) {
				name = rs.getString("name");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(conn, pstat, rs);
		}
		return name;
	}
	
	// ���ݲ�������ѯid
	public int searchDepId(String name) {
		int id = 0;
		Connection conn = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtil.getConnection();
			String sql = "select id from department where name ='" + name + "' ";
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
		
	// ��ҳ��ȡ������Ϣ
		public List<Department> searchByPage(int startIndex, int pageSize) {
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
					Department dep = new Department();
					dep.setId(rs.getInt("id"));
					dep.setName(rs.getString("name"));
					dep.setCount(rs.getInt("emp_count"));
					list.add(dep);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JDBCUtil.close(conn, pstat, rs);
			}

			return list;
		}
	
		// ����������ѯ���ݿ�Department���е�����
		public List<Department> searchByCondition(Department condition,int startIndex, int pageSize) {
			List<Department> listDep = new ArrayList<Department>();
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "root");
				Statement statement = connection.createStatement();
				String where = "where 1=1 ";
				if (condition.getName()!=null&&!condition.getName().equals("")) {
					where += "and name='"+condition.getName()+"'";
				}
				if (condition.getCount() != -1) {
					where += "and emp_count=" + condition.getCount();
				}
				String sql = "select * from department " + where +" limit "+startIndex+","+pageSize+" ";
				ResultSet result = statement.executeQuery(sql);
				// 6���Խ�������д���
				while (result.next()) {
					Department dep = new Department();
					dep.setId(result.getInt("id"));
					dep.setName(result.getString("name"));
					dep.setCount(result.getInt("emp_count"));
					listDep.add(dep);
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
			return listDep;
		}
		
		// ��ȡ��������
		public int getCount() {
			int count = 0;
			Connection conn = null;
			PreparedStatement pstat = null;
			ResultSet rs = null;
			try {
				conn = JDBCUtil.getConnection();
				String sql = "SELECT COUNT(0) FROM department";
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
	
		// ��ȡ��������ѯ���Ĳ�������
		public int getSearchCount(Department condition) {
			int depCount = 0;
			Connection conn = null;
			PreparedStatement pstat = null;
			ResultSet rs = null;
			try {
				String where = "where 1=1 ";
				if (condition.getName()!=null&&!condition.getName().equals("")) {
					where += "and name='"+condition.getName()+"'";
				}
				if (condition.getCount() != -1) {
					where += "and count=" + condition.getCount();
				}
				conn = JDBCUtil.getConnection();
				String sql = "SELECT COUNT(0) FROM department "+ where;
				pstat = conn.prepareStatement(sql);
				rs = pstat.executeQuery();
				while (rs.next()) {
					depCount = rs.getInt("COUNT(0)");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JDBCUtil.close(conn, pstat, rs);
			}
			return depCount;
		}	
		
	// �����ݿ��������
	public void add(Department dep) {
		try {
			// 2�����÷��䣬����������class.forName("");
			Class.forName("com.mysql.jdbc.Driver");
			// 3����������
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "root");
			// 4������Statement��SQL���ִ������
			Statement statement = connection.createStatement();
			// 5��ִ��SQL��䣬���õ����
			String sql = "insert into department (name) values ('" + dep.getName()+ "')";
			int count = statement.executeUpdate(sql);
			connection.close();
			statement.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// �޸����ݿ������
	public void update(Department emp) {
		try {
			// 2�����÷��䣬����������class.forName("");
			Class.forName("com.mysql.jdbc.Driver");
			// 3����������
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "root");
			// 4������Statement��SQL���ִ������
			Statement statement = connection.createStatement();
			// 5��ִ��SQL��䣬���õ����
			String sql = "update department set name='" + emp.getName() + "' where id=" + emp.getId() + " ";
			int count = statement.executeUpdate(sql);
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

	// �޸����ݿ������(���ݲ�����Ϣ��list)
	public void update(List<Department> list) {
		Connection conn = null;
		PreparedStatement pstat = null;
		try {
			conn = JDBCUtil.getConnection();
			for (int i = 0; i < list.size(); i++) {
				Department dep = list.get(i);
				String sql = "update department set name='" + dep.getName() + "'  where id in(" + dep.getId() + ") ";
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
	
		// ����ɾ������(���ݲ���id)
		public void deleteBatch(String id) {
			Connection conn = null;
			PreparedStatement pstat = null;

			try {
				conn = JDBCUtil.getConnection();
				String sql = "delete from department where id in(" + id + ") ";
				pstat = conn.prepareStatement(sql);
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
	
	// ɾ�����ݿ������
	public void delete(Department emp) {
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
			statement.executeUpdate("update employee set d_id=null where d_id=" + emp.getId() + " ");
			statement.executeUpdate("delete from department where id=" + emp.getId() + " ");
			statement.executeUpdate("delete from r_dep_pro where d_id=" + emp.getId() + " ");
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
}
