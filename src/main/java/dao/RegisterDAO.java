package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegisterDAO {
	
	public List<HHD> findByYear(int year, int userID) {
	    List<HHD> houselist = new ArrayList<>();

	    Connection con = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    String startDate = String.format("%d-01-01", year);  // 年の始まり
	    String endDate = String.format("%d-01-01", year + 1);  // 翌年の始まり

	    // SQL文の作成
	    String sql = "SELECT * FROM household WHERE registerday >= ? AND registerday < ? AND userID = ?";

	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");

            // 環境変数 DB_HOST を取得（なければ localhost）
            String dbHost = System.getenv("DB_HOST");
            if (dbHost == null || dbHost.trim().isEmpty()) {
                dbHost = "localhost";
            }

            // JDBC接続URLを構築
            String jdbcUrl = "jdbc:mysql://" + dbHost + ":3306/household_db?useUnicode=true&characterEncoding=UTF-8";
	        con = DriverManager.getConnection(jdbcUrl, "root", "root12345");

	        stmt = con.prepareStatement(sql);
	        // パラメータの設定
	        stmt.setString(1, startDate);
	        stmt.setString(2, endDate);
	        stmt.setInt(3, userID);  // userIDを設定

	        rs = stmt.executeQuery();

	        while (rs.next()) {
	            int id = rs.getInt("id");
	            Date registerday = rs.getDate("registerday");
	            String contents = rs.getString("contents");
	            int price = rs.getInt("price");
	            String spending = rs.getString("spending");
	            String income = rs.getString("income");
	            String remarks = rs.getString("remarks");
	            int userid = rs.getInt("userID");

	            HHD hhd = new HHD(id, registerday, contents, price, spending, income, remarks , userid);

	            houselist.add(hhd);
	        }
	    } catch (ClassNotFoundException e) {
	        System.out.println("JDBCドライバのロードでエラーが発生しました");
	    } catch (SQLException e) {
	        System.out.println("データベースへのアクセスでエラーが発生しました。A");
	    } finally {
	        try {
	            if (con != null) {
	                con.close();
	            }
	            if (stmt != null) {
	                stmt.close();
	            }
	            if (rs != null) {
	                rs.close();
	            }
	        } catch (SQLException e) {
	            System.out.println("データベースクローズでエラーが発生しました。");
	        }
	    }
	    return houselist;
	}
	
	public List<HHD> findByYearMonth(int year, int month , int userID) {

		List<HHD> houselist = new ArrayList<>();

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// 年月を基にした日付範囲を設定
		int nextYear = month == 12 ? year + 1 : year;
		int nextMonth = month == 12 ? 1 : month + 1;

		String startDate = String.format("%d-%02d-01", year, month);
		String endDate = String.format("%d-%02d-01", nextYear, nextMonth);

		// SQL文の作成
		String sql = "SELECT * FROM household WHERE registerday >= ? AND registerday < ? AND userID = ?";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		 // 環境変数 DB_HOST を取得（なければ localhost）
            String dbHost = System.getenv("DB_HOST");
            if (dbHost == null || dbHost.trim().isEmpty()) {
                dbHost = "localhost";
            }

            // JDBC接続URLを構築
            String jdbcUrl = "jdbc:mysql://" + dbHost + ":3306/household_db?useUnicode=true&characterEncoding=UTF-8";
			con = DriverManager.getConnection(jdbcUrl, "root", "root12345");

			stmt = con.prepareStatement(sql);
			// パラメータの設定
			stmt.setString(1, startDate);
			stmt.setString(2, endDate);
			stmt.setInt(3, userID);  // userIDを設定

			rs = stmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				Date registerday = rs.getDate("registerday");
				String contents = rs.getString("contents");
				int price = rs.getInt("price");
				String spending = rs.getString("spending");
				String income = rs.getString("income");
				String remarks = rs.getString("remarks");
				int userid = rs.getInt("userID");

				HHD hhd = new HHD(id, registerday, contents, price, spending, income, remarks , userid);

				houselist.add(hhd);
			}
		} catch (ClassNotFoundException e) {
			System.out.println("JDBCドライバのロードでエラーが発生しました");
		} catch (SQLException e) {
			System.out.println("データベースへのアクセスでエラーが発生しました。B");
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println("データベースへのアクセスでエラーが発生しました。C");
			}
		}
		return houselist;
	}

	public List<HHD> findByYearMonthDate(int year, int month, int day , int userID) {

		List<HHD> houselist = new ArrayList<>();

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// 日付を設定（年、月、日を適切にフォーマット）
		String specificDate = String.format("%d-%02d-%02d", year, month, day);

		// SQL文の作成
		String sql = "SELECT * FROM household WHERE registerday = ? AND userID = ?";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
            
			// 環境変数 DB_HOST を取得（なければ localhost）
            String dbHost = System.getenv("DB_HOST");
            if (dbHost == null || dbHost.trim().isEmpty()) {
                dbHost = "localhost";
            }

            // JDBC接続URLを構築
            String jdbcUrl = "jdbc:mysql://" + dbHost + ":3306/household_db?useUnicode=true&characterEncoding=UTF-8";
	        con = DriverManager.getConnection(jdbcUrl, "root", "root12345");
			stmt = con.prepareStatement(sql);
			// パラメータの設定
			stmt.setString(1, specificDate);
			stmt.setInt(2, userID);  // userIDを設定

			rs = stmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				Date registerday = rs.getDate("registerday");
				String contents = rs.getString("contents");
				int price = rs.getInt("price");
				String spending = rs.getString("spending");
				String income = rs.getString("income");
				String remarks = rs.getString("remarks");
				int userid = rs.getInt("userID");

				HHD hhd = new HHD(id, registerday, contents, price, spending, income, remarks , userid);

				houselist.add(hhd);
			}
		} catch (ClassNotFoundException e) {
			System.out.println("JDBCドライバのロードでエラーが発生しました");
		} catch (SQLException e) {
			System.out.println("データベースへのアクセスでエラーが発生しました。AA");
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println("データベースへのアクセスでエラーが発生しました。D");
			}
		}
		return houselist;
	}

	public void insertByContent(HHD hhd) {

	    java.sql.Date sqlDate = new java.sql.Date(hhd.getRegisterday().getTime());

	    // 変数の準備
	    Connection con = null;
	    PreparedStatement stmt = null;
	    int rowsInserted = 0;

	    // SQL文の作成
	    String sql = "INSERT INTO household (registerday, contents, price, spending, income, remarks, userID) VALUES "
	            + "(?, ?, ?, ?, ?, ?, ?)"; // userIDを追加

	    try {
	        // JDBCドライバのロード
	        Class.forName("com.mysql.cj.jdbc.Driver");
			// 環境変数 DB_HOST を取得（なければ localhost）
            String dbHost = System.getenv("DB_HOST");
            if (dbHost == null || dbHost.trim().isEmpty()) {
                dbHost = "localhost";
            }

            // JDBC接続URLを構築
            String jdbcUrl = "jdbc:mysql://" + dbHost + ":3306/household_db?useUnicode=true&characterEncoding=UTF-8";

			// データベース接続
	        con = DriverManager.getConnection(jdbcUrl, "root", "root12345");	        // SQL実行準備
	        stmt = con.prepareStatement(sql);

	        stmt.setDate(1, sqlDate);
	        stmt.setString(2, hhd.getContents());
	        stmt.setInt(3, hhd.getPrice());
	        stmt.setString(4, hhd.getSpending());
	        stmt.setString(5, hhd.getIncome());
	        stmt.setString(6, hhd.getRemarks());
	        stmt.setInt(7, hhd.getUserID()); // userIDをセット

	        // インサートのSQL実行    
	        rowsInserted = stmt.executeUpdate();

	        if (rowsInserted > 0) {
	            System.out.println(rowsInserted + " 行が挿入されました。");
	        } else {
	            System.out.println("挿入された行はありません。");
	        }

	    } catch (ClassNotFoundException e) {
	        System.out.println("JDBCドライバのロードでエラーが発生しました");

	    } catch (SQLException e) {
	        System.out.println("データベースへのアクセスでエラーが発生。");
	    } finally {
	        // リソースのクローズ
	        if (stmt != null) {
	            try {
	                stmt.close();
	            } catch (SQLException e) {
	                System.out.println("Statementのクローズでエラーが発生しました。");
	            }
	        }
	        if (con != null) {
	            try {
	                con.close();
	            } catch (SQLException e) {
	                System.out.println("データベースのクローズでエラーが発生しました。");
	            }
	        }
	    }
	}

	public void deleteByContent(HHD hhd) {

		// 変数の準備
		Connection con = null;
		PreparedStatement stmt = null;
		int rowsDeleted = 0;

		// SQL文の作成
		String sql = "DELETE FROM household WHERE id = ?";

		try {
			// JDBCドライバのロード
			Class.forName("com.mysql.cj.jdbc.Driver");
			// 環境変数 DB_HOST を取得（なければ localhost）
            String dbHost = System.getenv("DB_HOST");
            if (dbHost == null || dbHost.trim().isEmpty()) {
                dbHost = "localhost";
            }

            // JDBC接続URLを構築
            String jdbcUrl = "jdbc:mysql://" + dbHost + ":3306/household_db?useUnicode=true&characterEncoding=UTF-8";
	        // データベース接続
			con = DriverManager.getConnection(jdbcUrl, "root", "root12345");			// SQL実行準備
			stmt = con.prepareStatement(sql);

			// idの設定
			stmt.setInt(1, hhd.getId());

			//インサートのSQL実行	
			rowsDeleted = stmt.executeUpdate();

			if (rowsDeleted > 0) {
				System.out.println(rowsDeleted + " 行が削除されました。");
			} else {
				System.out.println("削除された行はありません。");
			}

		} catch (ClassNotFoundException e) {
			System.out.println("JDBCドライバのロードでエラーが発生しました");

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("データベースへのアクセスでエラーが発生。");
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				System.out.println("データベースへのアクセスでエラーが発生しました。E");
			}
		}
	}

	public void updateByContent(HHD hhd) {
		java.sql.Date sqlDate = new java.sql.Date(hhd.getRegisterday().getTime());

		// 変数の準備
		Connection con = null;
		PreparedStatement stmt = null;
		int rowsUpdated = 0;

		// SQL文の作成
		String sql = "UPDATE household SET registerday = ?, contents = ?, price = ?, spending = ?, income = ?, remarks = ? WHERE id = ?";

		try {
			// JDBCドライバのロード
			Class.forName("com.mysql.cj.jdbc.Driver");
			// 環境変数 DB_HOST を取得（なければ localhost）
            String dbHost = System.getenv("DB_HOST");
            if (dbHost == null || dbHost.trim().isEmpty()) {
                dbHost = "localhost";
            }

            // JDBC接続URLを構築
            String jdbcUrl = "jdbc:mysql://" + dbHost + ":3306/household_db?useUnicode=true&characterEncoding=UTF-8";
	        // データベース接続
			con = DriverManager.getConnection(jdbcUrl, "root", "root12345");			// SQL実行準備
			stmt = con.prepareStatement(sql);

			// プレースホルダーに値をセット
			stmt.setDate(1, sqlDate);
			stmt.setString(2, hhd.getContents());
			stmt.setInt(3, hhd.getPrice());
			stmt.setString(4, hhd.getSpending());
			stmt.setString(5, hhd.getIncome());
			stmt.setString(6, hhd.getRemarks());
			stmt.setInt(7, hhd.getId()); // 更新するレコードのID

			// 更新のSQL実行
			rowsUpdated = stmt.executeUpdate();

			if (rowsUpdated > 0) {
				System.out.println(rowsUpdated + " 行が更新されました。");
			} else {
				System.out.println("更新された行はありません。");
			}

		} catch (ClassNotFoundException e) {
			System.out.println("JDBCドライバのロードでエラーが発生しました");
		} catch (SQLException e) {
			System.out.println("データベースへのアクセスでエラーが発生しました。F");
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				System.out.println("データベースクローズ時にエラーが発生しました。");
			}
		}
	}
}