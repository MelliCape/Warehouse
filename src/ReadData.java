import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ReadData extends JFrame implements ActionListener  {
    JFrame readDataFrame;
    static JTable table;
    JMenuBar menuBar;
    JMenuItem menuAdd, menuUpdate, menuDelete;
    String[] columnNames = {"", "Name", "Quantity", "Price", "Weight", "Capacity", "Info", "ID"};
    Connection con;
    PreparedStatement prepareSt;

    ReadData() {
        //makes connection with database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/warehouse";
            con = DriverManager.getConnection(url, "root", "1234");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void printDatabase() {
        menuBar = new JMenuBar();
        menuAdd = new JMenuItem("Add position");
        menuBar.add(menuAdd);
        menuAdd.addActionListener(this);

        menuUpdate = new JMenuItem("Upgrade position");
        menuBar.add(menuUpdate);
        menuUpdate.addActionListener(this);

        menuDelete = new JMenuItem("Delete position");
        menuBar.add(menuDelete);
        menuDelete.addActionListener(this);


        readDataFrame = new JFrame("Warehouse");
        readDataFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        readDataFrame.setLayout(new BorderLayout());

        //it allows to use a checkbox in the table
        DefaultTableModel model = new DefaultTableModel() {
            public Class<?> getColumnClass(int column) {
                if (column == 0) {
                    return Boolean.class;
                }
                return String.class;
            }
        };

        model.setColumnIdentifiers(columnNames);
        table = new JTable();
        table.setModel(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        String name;
        String quantity;
        String price;
        String weight;
        String capacity;
        String info;
        String id;

        try {
            prepareSt = con.prepareStatement("select * from warehouse_data");
            ResultSet resultSet = prepareSt.executeQuery();
            int i = 0;
            while (resultSet.next()) {
                JCheckBox checkBox = new JCheckBox();

                name = resultSet.getString(1);
                quantity = resultSet.getString(2);
                price = resultSet.getString(3);
                weight = resultSet.getString(4);
                capacity = resultSet.getString(5);
                info = resultSet.getString(6);
                id = resultSet.getString(7);

                model.addRow(new Object[]{checkBox, name, quantity, price, weight, capacity, info, id});
                model.setValueAt(false, i, 0);

                i++;
            }
            if (i < 1) {

                JOptionPane.showMessageDialog(null, "No record found", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (i == 1) {
                System.out.println(i + " Record found");
            } else {
                System.out.println(i + " Records found");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        readDataFrame.add(scrollPane);
        readDataFrame.setVisible(true);
        readDataFrame.setSize(1000, 1000);
        readDataFrame.setJMenuBar(menuBar);
    }
    //delete position chosen with a checkbox
    public void deletePosition(){
        try {
            for (int i = 0; i < table.getRowCount(); i++) {
                boolean boxChecked = Boolean.parseBoolean(table.getValueAt(i, 0).toString());
                String idPositionToDelete = table.getValueAt(i, 7).toString();
                if (boxChecked) {
                    String query = "delete from warehouse_data where id = ?";
                    prepareSt = con.prepareStatement(query);
                    prepareSt.setString(1, idPositionToDelete);
                    System.out.println(idPositionToDelete);
                    prepareSt.execute();
                    con.close();
                    readDataFrame.dispose();
                    new ReadData().printDatabase();

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    //allows to update inserted data from the page with the table
    public void updatePosition(){
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        String updateQuery = "UPDATE `warehouse_data` SET `name`=?, `quantity`=? , `price`=?, `weight`=?, `capacity`=?, `info`= ? WHERE `id` = ?";
        try {

            int count = 0;
            for (int i = 0; i < model.getRowCount(); i++) {

                String name = model.getValueAt(i, 1).toString();
                float quantity = Float.parseFloat(model.getValueAt(i, 2).toString());
                float price = Float.parseFloat(model.getValueAt(i, 3).toString());
                float weight = Float.parseFloat(model.getValueAt(i, 4).toString());
                float capacity = Float.parseFloat(model.getValueAt(i, 5).toString());
                String info = model.getValueAt(i, 6).toString();
                int id = Integer.parseInt(model.getValueAt(i, 7).toString());

                prepareSt = con.prepareStatement(updateQuery);
                prepareSt.setString(1, name);
                prepareSt.setFloat(2, quantity);
                prepareSt.setFloat(3, price);
                prepareSt.setFloat(4, weight);
                prepareSt.setFloat(5, capacity);
                prepareSt.setString(6, info);
                prepareSt.setInt(7, id);

                count = prepareSt.executeUpdate();

            }
            System.out.println(count + "row/s updated."); //counts how many rows was updated

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == menuAdd) {
            new AddWindow(); // opens AddWindow
            if (this.readDataFrame != null) {
                readDataFrame.dispose();
            }
        }

        if (ae.getSource() == menuDelete) {
            deletePosition();

        }

        if (ae.getSource() == menuUpdate) {
            updatePosition();
        }


    }

}