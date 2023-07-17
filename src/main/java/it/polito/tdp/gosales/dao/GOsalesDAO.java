package it.polito.tdp.gosales.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.gosales.model.Arco;
import it.polito.tdp.gosales.model.DailySale;
import it.polito.tdp.gosales.model.Products;
import it.polito.tdp.gosales.model.Retailers;

public class GOsalesDAO {
	
	
	/**
	 * Metodo per leggere la lista di tutti i rivenditori dal database
	 * @return
	 */

	public List<Retailers> getAllRetailers(){
		String query = "SELECT * from go_retailers";
		List<Retailers> result = new ArrayList<Retailers>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Retailers(rs.getInt("Retailer_code"), 
						rs.getString("Retailer_name"),
						rs.getString("Type"), 
						rs.getString("Country")));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
	
	/**
	 * Metodo per leggere la lista di tutti i prodotti dal database
	 * @return
	 */
	public List<Products> getAllProducts(){
		String query = "SELECT * from go_products";
		List<Products> result = new ArrayList<Products>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Products(rs.getInt("Product_number"), 
						rs.getString("Product_line"), 
						rs.getString("Product_type"), 
						rs.getString("Product"), 
						rs.getString("Product_brand"), 
						rs.getString("Product_color"),
						rs.getDouble("Unit_cost"), 
						rs.getDouble("Unit_price")));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}

	
	/**
	 * Metodo per leggere la lista di tutte le vendite nel database
	 * @return
	 */
	public List<DailySale> getAllSales(){
		String query = "SELECT * from go_daily_sales";
		List<DailySale> result = new ArrayList<DailySale>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new DailySale(rs.getInt("retailer_code"),
				rs.getInt("product_number"),
				rs.getInt("order_method_code"),
				rs.getTimestamp("date").toLocalDateTime().toLocalDate(),
				rs.getInt("quantity"),
				rs.getDouble("unit_price"),
				rs.getDouble("unit_sale_price")  ));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<String> getNazioni() {
		String query = "SELECT DISTINCT r.Country "
					+"FROM go_retailers r";
		
		List<String> result = new ArrayList<String>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(rs.getString("Country"));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Retailers> getVertici(String nazione) {
		String query = "SELECT r.* "
				+ "FROM go_retailers r "
				+ "WHERE r.Country=?";
	
		List<Retailers> result = new ArrayList<Retailers>();
	
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, nazione);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Retailers r = new Retailers(rs.getInt("Retailer_code"), rs.getString("Retailer_name"), rs.getString("Type"), rs.getString("Country"));
				result.add(r);
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Arco> getArchi(String nazione, int anno, int nMin) {
		
		String query = "SELECT r1.Retailer_code AS rCode1, r2.Retailer_code AS rCode2, COUNT(DISTINCT s1.Product_number) AS n "
				+ "FROM go_daily_sales s1, go_daily_sales s2, go_retailers r1, go_retailers r2 "
				+ "WHERE r1.Country =? AND r1.Country=r2.Country "
				+ "	AND r1.Retailer_code= s1.Retailer_code "
				+ "		AND r2.Retailer_code= s2.Retailer_code "
				+ "			AND s1.Product_number= s2.Product_number "
				+ "				AND r1.Retailer_code< r2.Retailer_code "
				+ "					AND YEAR(s1.Date) = ? AND YEAR(s1.Date) = YEAR(s2.Date) "
				+ "GROUP BY r1.Retailer_code, r2.Retailer_code "
				+ "HAVING n>=?";
		
		/* OPZIONE 2
		String query = "SELECT s1.Retailer_code, s2.Retailer_code, COUNT(DISTINCT s1.Product_number) AS n "
				+"FROM go_daily_sales s1, go_daily_sales s2 "
				+"WHERE s1.Product_number = s2.Product_number " 
				+"AND YEAR(s1.Date)= 2017 AND YEAR(s2.Date)=YEAR(s1.Date) "
				+"AND s1.Retailer_code IN( SELECT r.Retailer_code 
										FROM go_retailers r
										WHERE r.Country="France") "
				+"AND s2.Retailer_code IN( SELECT r.Retailer_code
										FROM go_retailers r
										WHERE r.Country="France") "
				+"AND s1.Retailer_code<s2.Retailer_code "
				+"GROUP BY s1.Retailer_code, s2.Retailer_code "
				+"HAVING n>=3";
		
		*/
	
		List<Arco> result = new ArrayList<Arco>();
	
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, nazione);
			st.setInt(2, anno);
			st.setInt(3, nMin);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Arco arco = new Arco(rs.getInt("rCode1"), rs.getInt("rCode2"), rs.getInt("n"));
				result.add(arco);
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
	public List<Products> getProductsRetailers(Retailers r, int anno){
		String query = "SELECT DISTINCT p.* "
				+ "FROM go_daily_sales s, go_products p "
				+ "WHERE s.Product_number = p.Product_number "
				+ "	AND YEAR(s.Date)=? "
				+ "		AND s.Retailer_code=?";
		
		List<Products> result = new ArrayList<Products>();

		try {
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, anno);
			st.setInt(2, r.getCode());
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Products p = new Products(rs.getInt("Product_number"), rs.getString("Product_line"), rs.getString("Product_type"), rs.getString("Product"), rs.getString("Product_brand"), rs.getString("Product_color"),rs.getDouble("Unit_cost"), rs.getDouble("Unit_price"));
				result.add(p);
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}

	public int getAvgD(Retailers r, Products p , int anno) {
		
		String query = "SELECT s.Retailer_code, 12*30/Count(*) AS avgD "
				+ "FROM go_daily_sales "
				+ "WHERE s.Retailer_code=? AND s.Product_number=? "
				+ "	AND YEAR(s.Date)=?";
	
		int result=0;
	
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, r.getCode());
			st.setInt(2, p.getNumber());
			st.setInt(3, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result= (int)(rs.getDouble("avgD"));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public int getAvgQ(Retailers r, Products p , int anno) {
		
		String query = "SELECT s.Retailer_code, SUM(s.Quantity)/COUNT(*) AS avgQ"
				+ "FROM go_daily_sales s "
				+ "WHERE s.Retailer_code=? AND s.Product_number=? "
				+ "	AND YEAR(s.Date) = ?";
	
		int result=0;
	
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, r.getCode());
			st.setInt(2, p.getNumber());
			st.setInt(3, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result= (int)(rs.getDouble("avgQ"));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
}
