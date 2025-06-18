import java.sql.*;

/**
 * Manages database connections and operations for the criminal detection system
 * TODO: Add connection pooling for better performance
 * TODO: Implement proper logging instead of System.err
 */
public class DatabaseConnection {
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/criminal_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Shivang07";  // Consider using environment variables
    
    // Get database connection
    public static Connection getConnection() throws SQLException {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Create connection with UTF-8 encoding
            return DriverManager.getConnection(
                DB_URL + "?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8",
                DB_USER,
                DB_PASS
            );
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found. Please install MySQL Connector/J.", e);
        }
    }
    
    // Close database resources safely
    public static void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            // Close resources in reverse order of creation
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
            
        } catch (SQLException e) {
            // Log error but don't throw - we want to close other resources
            System.err.println("Error closing database resources: " + e.getMessage());
        }
    }
    
    // Retrieve criminal information by ID
    public static Criminal fetchCriminalById(String criminalId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            String query = "SELECT * FROM criminals WHERE id = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, criminalId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                Criminal criminal = new Criminal();
                criminal.setId(rs.getString("id"));
                criminal.setName(rs.getString("name"));
                criminal.setCrime(rs.getString("crime"));
                criminal.setAddress(rs.getString("address"));
                return criminal;
            }
            
            return null;
            
        } catch (SQLException e) {
            System.err.println("Error fetching criminal data: " + e.getMessage());
            return null;
            
        } finally {
            closeResources(conn, ps, rs);
        }
    }
    
    // Add a new criminal to the database
    public static boolean addCriminal(Criminal criminal) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            String query = "INSERT INTO criminals (id, name, crime, address) VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(query);
            ps.setString(1, criminal.getId());
            ps.setString(2, criminal.getName());
            ps.setString(3, criminal.getCrime());
            ps.setString(4, criminal.getAddress());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, ps, null);
        }
    }
}
