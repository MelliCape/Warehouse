import java.awt.*;
import java.awt.event.*;

//Window opened after user chose "menuAdd" option from menu bar

public class AddWindow extends Frame {
    Panel panelWindow, panelButton;
    Label lblName, lblQuantity, lblPrice, lblWeight, lblCapacity, lblInfo;
    TextField txtName, txtQuantity, txtPrice, txtWeight, txtCapacity, txtInfo;
    Button btnAdd;

    String name, info;
    float quantity, price, weight, capacity;
    int id;

    AddWindow() {

        //interface allowing user to easily add new product to database.

        lblName = new Label("Product name: ");
        txtName = new TextField();

        lblQuantity = new Label("Quantity: ");
        txtQuantity = new TextField();

        lblPrice = new Label("Product price: ");
        txtPrice = new TextField();

        lblWeight = new Label("Product weight: ");
        txtWeight = new TextField();

        lblCapacity = new Label("Product capacity in liters: ");
        txtCapacity = new TextField();

        lblInfo = new Label("Additional info: ");
        txtInfo = new TextField();



        panelWindow = new Panel(new GridLayout(7, 2));
        panelWindow.add(lblName);
        panelWindow.add(txtName);
        panelWindow.add(lblQuantity);
        panelWindow.add(txtQuantity);
        panelWindow.add(lblPrice);
        panelWindow.add(txtPrice);
        panelWindow.add(lblWeight);
        panelWindow.add(txtWeight);
        panelWindow.add(lblCapacity);
        panelWindow.add(txtCapacity);
        panelWindow.add(lblInfo);
        panelWindow.add(txtInfo);


        btnAdd = new Button("Add product");
        panelButton = new Panel(new FlowLayout());
        panelButton.add(btnAdd);
        btnAdd.addActionListener(new ActionListener() { //actions after Add button was pressed
            @Override
            public void actionPerformed(ActionEvent e) {
                name = txtName.getText();
                quantity = Float.parseFloat(txtQuantity.getText());
                price = Float.parseFloat(txtPrice.getText());
                weight = Float.parseFloat(txtWeight.getText());
                capacity = Float.parseFloat(txtCapacity.getText());
                info = txtInfo.getText();
                try {                                   //part of code that sends user data into AddProduct class and tries to add it to database
                    AddProduct.addProduct(name, quantity, price, weight, capacity, info, id);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                new ReadData().printDatabase();
                dispose();

            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing (WindowEvent ee){
                dispose();
            }
        });
        add(panelWindow);
        add(panelButton);
        setTitle("Warehouse");
        setLayout(new GridLayout(2,1));
        setVisible(true);
        setSize(500,500);
        setLocationRelativeTo(null);
    }

}