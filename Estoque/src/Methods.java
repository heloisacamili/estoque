import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Locale;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Methods {
	
	//you must change according to your database credentials
	private static String url = "";
	private static String user = "";
	private static String password = "";
	private static Connection connection = null;
	
	public static Connection createConnection() throws SQLException {
		try {
			connection = DriverManager.getConnection(url, user, password);
			
			if (connection !=  null) {
				System.out.println("Conexão com o banco de dados bem sucedida.");
			} else {
				System.out.println("Conexão com o banco de dados falhou.");
			}
		} catch (SQLException error) {
			System.out.println("SQLException: " + error);
		}
		return connection;
	}
	
	public static void updateStock() {
		String jsonProducts = "[{\"produto\": \"10.01.0419\", \"cor\": \"00\",\"tamanho\": \"P\", \"deposito\": \"DEP1\",\"data_disponibilidade\":\"2023-05-01\",\"quantidade\": 14},{\"produto\": \"11.01.0568\",\"cor\": \"08\",\"tamanho\": \"P\",\"deposito\": \"DEP1\",\"data_disponibilidade\": \"2023-05-01\",\"quantidade\": 5},{\"produto\": \"11.01.0568\",\"cor\": \"08\",\"tamanho\": \"M\",\"deposito\": \"DEP1\",\"data_disponibilidade\": \"2023-05-01\",\"quantidade\": 4},{\"produto\": \"11.01.0568\",\"cor\": \"08\",\"tamanho\": \"G\",\"deposito\": \"1\",\"data_disponibilidade\": \"2023-05-01\",\"quantidade\": 6},{\"produto\": \"11.01.0568\",\"cor\": \"08\",\"tamanho\": \"P\",\"deposito\": \"DEP1\",\"data_disponibilidade\": \"2023-06-01\",\"quantidade\": 8} ]";
		
		Gson gson = new Gson();
		JsonArray productsList = gson.fromJson(jsonProducts, JsonArray.class);
		
		try {
			connection = createConnection();
			for(int i = 0; i < productsList.size(); i++) {
				JsonObject product = productsList.get(i).getAsJsonObject();
				
				String name = product.get("produto").getAsString();
                String color = product.get("cor").getAsString();
                String size = product.get("tamanho").getAsString();
                String deposit = product.get("deposito").getAsString();
                SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                formatDate.setTimeZone(TimeZone.getTimeZone("UTC")); 
                java.util.Date date = formatDate.parse(product.get("data_disponibilidade").getAsString() + "T00:00:00.000Z");
                java.sql.Timestamp formattedDate = new java.sql.Timestamp(date.getTime());
                int quantity = product.get("quantidade").getAsInt();
                Product item = new Product(name, color, size, deposit, formattedDate, quantity);
                
                boolean verify = alreadyExists(connection, item); 
                if (verify) {
                	updateProduct(connection, item);
                } else {
                	insertProduct(connection, item);
                }
			}
			
		} catch (Exception error) {
			System.out.println("Algo de errado ocorreu: " + error);
		}
		
	}
	
	//this method will be verify if the product already exists at database
	private static boolean alreadyExists(Connection connection, Product product) {
		String instruction = "SELECT * FROM estoque WHERE produto=? AND cor=? AND tamanho=? AND deposito=?";
		PreparedStatement preparedStatement;
		
		try {
			preparedStatement = connection.prepareStatement(instruction);
			preparedStatement.setString(1, product.getProduto());
            preparedStatement.setString(2, product.getCor());
            preparedStatement.setString(3, product.getTamanho());
            preparedStatement.setString(4, product.getDeposito());
            
            ResultSet result = preparedStatement.executeQuery();
			return result.next();
		} catch (Exception error) {
			System.out.println("VERIFY ERROR: " + error);
		}
		return false;
	}
	
	//this method will insert a product at your database
	private static void insertProduct(Connection connection, Product product) {
		String instructionSQL = "INSERT INTO estoque (produto, cor, tamanho, deposito, data_disponibilidade, quantidade) VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement preparedStatement;
		
		try {
			preparedStatement = connection.prepareStatement(instructionSQL);
			preparedStatement.setString(1, product.getProduto());
			preparedStatement.setString(2, product.getCor());
			preparedStatement.setString(3, product.getTamanho());
			preparedStatement.setString(4, product.getDeposito());
			java.sql.Date formattedDate = new java.sql.Date(product.getDataDisponibilidade().getTime());
			preparedStatement.setDate(5, formattedDate);
			preparedStatement.setInt(6, product.getQuantidade());
			preparedStatement.executeUpdate();
		} catch (Exception error) {
			System.out.println("INSERT ERROR: " + error);
		}
	}
	
	//this method will update your product if it exists 
	private static void updateProduct(Connection connection, Product product) throws ParseException {
		String instructionSQL = "UPDATE estoque SET produto=?, cor=?, tamanho=?, deposito=?, data_disponibilidade=?, quantidade=? WHERE produto=? AND cor=? AND tamanho=? AND deposito=? AND data_disponibilidade=?";
		PreparedStatement preparedStatement;
		
		try {
			preparedStatement = connection.prepareStatement(instructionSQL);
			preparedStatement.setString(1, product.getProduto());
			preparedStatement.setString(2, product.getCor());
			preparedStatement.setString(3, product.getTamanho());
			preparedStatement.setString(4, product.getDeposito());
			java.sql.Date formattedDate = new java.sql.Date(product.getDataDisponibilidade().getTime());
			preparedStatement.setDate(5, formattedDate);
			preparedStatement.setInt(6, product.getQuantidade());
			
			preparedStatement.setString(7, product.getProduto());
	        preparedStatement.setString(8, product.getCor());
	        preparedStatement.setString(9, product.getTamanho());
	        preparedStatement.setString(10, product.getDeposito());
	        preparedStatement.setDate(11, formattedDate);
	        
			preparedStatement.executeUpdate();
		} catch (SQLException error) {
			System.out.println("UPDATE ERROR: " + error);
		}
	}

}
