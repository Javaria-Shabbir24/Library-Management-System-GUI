package com.mycompany.assignment3scd;
//import libraries
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
class Library{

    //creating default table model
    DefaultTableModel m1=new DefaultTableModel();
    void loadData() throws FileNotFoundException
    {
        //create frame
        JFrame frame=new JFrame("Library Management System");
        frame.setLayout(new BorderLayout());
        //create Jtable
        JTable table=new JTable(m1);
        //create a scroll bar to scroll the table if it has many rows
        JScrollPane scrollbar=new JScrollPane(table);
        //i want 4 columns
        m1.addColumn("Title");
        m1.addColumn("Author");
        m1.addColumn("Publication Year");
        m1.addColumn("Read Item");
        //create buttons add the button for handling features
        JButton addItem=new JButton("Add Item");
        JButton editItem=new JButton("Edit Item");
        JButton deleteItem=new JButton("Delete Item");
        //add everything to the frame
        frame.add(scrollbar,BorderLayout.CENTER);
        //creating a  panel for buttons in the bottom
        JPanel buttonsPanel=new JPanel();
        buttonsPanel.add(addItem);
        //make additem button functional
        addItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                addBook();
            }
        });
        
        buttonsPanel.add(editItem);
        buttonsPanel.add(deleteItem);
        frame.add(buttonsPanel,BorderLayout.SOUTH);
        //add exit on close for the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,400);
        
        //load the data
        File file=new File("data.txt");//Loading Books from file
        Scanner read= new Scanner(file);
        read.useDelimiter(",");
        int rowIndex=0;
        while(read.hasNextLine())//Till the last line
        {
          String line=read.nextLine();//line by line 
          Object[] value=line.split(",");
          if(value.length==3)
          {
              m1.addRow(new Object[]{value[0],value[1],value[2],"Read Item"});
          }
          rowIndex++;
        }
        // set the custom cell renderer for the 3rd column
        TableColumn column = table.getColumnModel().getColumn(3);
        column.setCellRenderer(new ButtonCellRenderer(new JButton("Read Item")));
      
        //setting visibility equal to true
        frame.setVisible(true);
    }
    
    //add book function
    private void addBook()
    {
        //separate gui screen
        JDialog addBookDialog=new JDialog(); 
        //title of gui screen
        addBookDialog.setTitle("Add Book");
        //set size of dialog box
        addBookDialog.setSize(500,150);
        //setting the layout as grid layout
        addBookDialog.setLayout(new GridLayout(4,2));
        //make text boxes/fields
        JTextField title=new JTextField();
        JTextField author=new JTextField();
        JTextField yearofPublication=new JTextField();
        //add the read book button
        JButton addButton=new JButton("Add Book");
        //add all these things in the dialog box
        addBookDialog.add(new JLabel("Enter the title of the book: "));
        addBookDialog.add(title);
        addBookDialog.add(new JLabel("Enter the author of the book: "));
        addBookDialog.add(author);
        addBookDialog.add(new JLabel("Enter the year of publication: "));
        addBookDialog.add(yearofPublication);
        //first add empty cell 
        addBookDialog.add(new JLabel(""));
        addBookDialog.add(addButton);
        //setting the functionality of the add book button
        addButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String enteredtitle=title.getText();
                String enteredauthor=author.getText();
                String enteredyear=yearofPublication.getText();
                //if the textfields are not empty
                if(!enteredtitle.isEmpty() && !enteredauthor.isEmpty() && !enteredyear.isEmpty())
                {
                    int x=0;
                    try
                    {
                        x=Integer.parseInt(enteredyear);
                        if(x>1 && x<2024)
                        {
                        m1.addRow(new Object[]{enteredtitle,enteredauthor,enteredyear,"Read Item"});
                        try {
                            saveToTextFile(enteredtitle,enteredauthor,enteredyear);
                        } catch (IOException ex) {
                            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        addBookDialog.dispose();
                        }
                        else
                        {
                           throw new InvalidInputException("Invalid Year Entered");
                        }
                        
                    }
                    catch(NumberFormatException ex)
                    {
                        showErrorMessage();
                    
                    } catch (InvalidInputException ex) {
                         JOptionPane.showMessageDialog(null, "Invalid Year Entered, Enter Again");
  
                    }
                   
                }
            }
        });
        //set visibility of the dialog box
        addBookDialog.setVisible(true);
    }
    //saving data to the text file
    private void saveToTextFile(String title, String author,String year) throws IOException
    {
        try(FileWriter filewriter=new FileWriter("data.txt",true)){
            String data="\n"+title+","+author+","+year+"\n";
        filewriter.write(data);
        showSuccessMessage();
        }
        catch(IOException e)
        {
        }
    }
    private void showSuccessMessage()
    {
        JOptionPane.showMessageDialog(null, "Data added successfully !");
    }
    private void showErrorMessage()
    {
        JOptionPane.showMessageDialog(null, "Publication year must be a valid integer");
    }
}
class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}
 class ButtonCellRenderer extends DefaultTableCellRenderer {
    private final JButton button;
    public ButtonCellRenderer(JButton button) {
        this.button = button;
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return button;
    }
}
public class Assignment3SCD {

    public static void main(String[] args) throws FileNotFoundException {
        Library l1=new Library();
        l1.loadData();
    }
   

}
