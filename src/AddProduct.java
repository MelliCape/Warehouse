import java.sql.*;


//with this class we can add new product to the database.
public class AddProduct {

    //statement allowing to add data from user into database
    public static void addProduct( String name, float quantity, float price, float weight, float capacity, String info, int id) throws Exception{
        String sql = "INSERT INTO warehouse_data VALUES ('"+ name + "','" +  quantity + "','" + price + "','" + weight + "','" + capacity + "','" + info + "','" + id + "')";

        insertData(sql);

    }

        //making a connection with a database
    private static void insertData(String sql) throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/warehouse";
        Connection con = DriverManager.getConnection(url, "root", "1234");
        if(con!=null){
            Statement stmt = con.createStatement();
            int result = stmt.executeUpdate(sql);
            if (result!= -1) {
                System.out.println("Updated " + result + " items.");
            }
            else{
                System.out.println("Something went wrong.");
            }
            stmt.close();
            con.close();
        }
        else{
            System.out.println("Unable to connect to database.");
        }
    }
}
