package ajax.example;

import java.util.ArrayList;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import java.sql.*;

@ManagedBean
@RequestScoped

public class User {
	
	int id;
	String name;
	String email;
	String password;
	String gender;
	String address;
	ArrayList<User> userList;
	Connection connection;
	private Map<String,Object> sessionMap=FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Connection getConnection() {
		try {

			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/User", "sqluser", "sqluserpw");
		} catch (Exception e) {
			System.out.println(e);
		}
		return connection;
	}

	public ArrayList userList() {

		try {
			userList = new ArrayList();
			connection = getConnection();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("select * from users");

			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				user.setGender(rs.getString("gender"));
				user.setAddress(rs.getString("address"));
				userList.add(user);
			}
			// connection.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(connection);

		}

		return userList;

	}

	public static void close(AutoCloseable... closeables) {
		for (AutoCloseable closeable : closeables) {
			if (closeable != null) {
				try {
					closeable.close();
				} catch (Exception e) {

				}
			}
		}

	}

	public String getGenderName(char gender) {
		if (gender == 'M') {
			return "Male";
		} else
			return "Female";
	}

	public String save() {
		int result = 0;
		String query = "insert into users(name,email,password,gender,Address) values(?,?,?,?,?)";
		try {
			connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, name);
			ps.setString(2, email);
			ps.setString(3, password);
			ps.setString(4, gender);
			ps.setString(5, address);
			System.out.println();
			
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(connection);
		}

		if (result != 0) {
			return "index.xhtml?faces-redirect=true";
		} else {
			return "create.xhtml?faces-redirect=true";
		}

	}

	public String edit(int id) {
		System.out.println();
		
		System.out.println(id);
		try {
			connection = getConnection();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("select * from users where id=" + (id));
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setGender(rs.getString("gender"));
				user.setPassword(rs.getString("password"));
				user.setAddress(rs.getString("address"));
				System.out.println(address);
				sessionMap.put("editUser",user);
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			close(connection);
		}
		return "edit.xhtml?faces-redirect=true";

	}

	public String update(User u) {
		
		try {
			connection = getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("update users set name=?,email=?,password=?,gender=?,address=? where id=?");
			stmt.setString(1, u.getName());
			stmt.setString(2, u.getEmail());
			stmt.setString(3, u.getPassword());
			stmt.setString(4, u.getGender());
			stmt.setString(5, u.getAddress());
			stmt.setInt(6, u.getId());
			stmt.executeUpdate();
			connection.close();
		} catch (Exception e) {
			System.out.println();
		}
		return "/index.xhtml?faces-redirect=true";
	}
	public void delete(int id)
	{
		int result=0;
		try {
			connection = getConnection();
			PreparedStatement ps = connection.prepareStatement("delete from users where id=?");
			ps.setInt(1,id);
		    result=ps.executeUpdate();
		} catch (Exception e) {
			System.out.println();
		}
		
		
		
	}
}
