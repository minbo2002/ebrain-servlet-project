package example.jdbc.conn;

import java.sql.*;

public class JdbcUtil {

    public static void close(ResultSet rs) {
        if( rs != null) {
            try {
                rs.close();
            }catch(SQLException e) {}
        }
    }

    public static void close(PreparedStatement pstmt) {
        if( pstmt != null) {
            try {
                pstmt.close();
            }catch(SQLException e) {}
        }
    }

    public static void close(Statement stmt) {
        if( stmt != null) {
            try {
                stmt.close();
            }catch(SQLException e) {}
        }
    }

    public static void close(Connection conn) {
        if( conn != null) {
            try {
                conn.close();
            }catch(SQLException e) {}
        }
    }

    public static void rollback(Connection conn) {
        if( conn != null) {
            try {
                conn.rollback();
            }catch(SQLException e) {}
        }
    }
}
