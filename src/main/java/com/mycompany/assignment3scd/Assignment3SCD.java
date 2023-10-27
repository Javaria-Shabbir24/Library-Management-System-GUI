package com.mycompany.assignment3scd;
//import libraries
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        //make deleteitem button functional
        deleteItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBook();
            }
            
        });
        //make editItem button functional
        editItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               editBook();
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
        //enable mouse hovering
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0 ) {
                    table.setSelectionBackground(Color.LIGHT_GRAY);
                    table.setSelectionForeground(Color.BLACK);
                    table.setRowSelectionInterval(row, row);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    table.setSelectionBackground(Color.LIGHT_GRAY);
                    table.setSelectionForeground(Color.BLACK);
                    table.setRowSelectionInterval(row, row);
                }
            }
            //sets the table background color to default color
            @Override
            public void mouseExited(MouseEvent e) {
                table.setSelectionBackground(table.getBackground());
            }
        });
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
        //add the add book button
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
                        if(x>0 && x<2024)
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
    int count=0;
    //edit book function
    private void editBook()
    {
        //separate gui screen
        JDialog editBookDialog=new JDialog(); 
        //title of gui screen
        editBookDialog.setTitle("Edit Book");
        //set size of dialog box
        editBookDialog.setSize(550,100);
        //setting the layout as grid layout
        editBookDialog.setLayout(new GridLayout(2,2));
        //make text boxes/fields
        JTextField title=new JTextField();
        //add the delete book button
        JButton editButton=new JButton("Edit Book");
        editBookDialog.add(new JLabel("Enter the title of the book you want to edit: "));
        editBookDialog.add(title);
        //first add empty cell 
        editBookDialog.add(new JLabel(""));
        editBookDialog.add(editButton);
        //make editbutton functional
        editButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
              String EnteredTitle=title.getText();
              for (;count<m1.getRowCount();count++)
                {
                   String name=(String)m1.getValueAt(count, 0);
                   //if book is in table
                   if(name.equals(EnteredTitle))
                   {
                       //create new dialog box for editing
                       //separate gui screen
                        JDialog editBookDialog2=new JDialog(); 
                        //title of gui screen
                        editBookDialog2.setTitle("Editing the Book");
                        //set size of dialog box
                        editBookDialog2.setSize(550,200);
                        //setting the layout as grid layout
                        editBookDialog2.setLayout(new GridLayout(5,2));
                        //edit title label
                        editBookDialog2.add(new JLabel("Click to edit title of the book: "));
                        //add the title editing book button
                        JButton titleEdit=new JButton("Edit title");
                        editBookDialog2.add(titleEdit);
                        //add actionListener of edit title 
                        titleEdit.addActionListener(new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                     //separate gui screen
                                    JDialog editBookNameDialog=new JDialog(); 
                                    //title of gui screen
                                    editBookNameDialog.setTitle("Edit Book Name");
                                    //set size of dialog box
                                    editBookNameDialog.setSize(550,100);
                                    //setting the layout as grid layout
                                    editBookNameDialog.setLayout(new GridLayout(2,2));
                                    //make text boxes/fields
                                    JTextField title=new JTextField();
                                    //add the delete book button
                                    JButton editButton=new JButton("Done");
                                    editBookNameDialog.add(new JLabel("Enter new title: "));
                                    editBookNameDialog.add(title);
                                    //first add empty cell 
                                    editBookNameDialog.add(new JLabel(""));
                                    editBookNameDialog.add(editButton);
                                    editButton.addActionListener(new ActionListener(){
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                             final String T=title.getText();
                                             if(!T.isEmpty()){
                                             m1.setValueAt(T, count, 0);
                                             showEditSuccessMessage();
                                             editBookNameDialog.dispose();
                                             }
                                        }
                                    });
                                    //make visible
                                    editBookNameDialog.setVisible(true);
                            }
                       
                        });
                        //edit author label
                        editBookDialog2.add(new JLabel("Click to edit author name of the book: "));
                        //add the author editing book button
                        JButton authorEdit=new JButton("Edit Author");
                        editBookDialog2.add(authorEdit);
                        //add actionListener of edit title 
                        authorEdit.addActionListener(new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                     //separate gui screen
                                    JDialog editAuthorNameDialog=new JDialog(); 
                                    //title of gui screen
                                    editAuthorNameDialog.setTitle("Edit Author Name");
                                    //set size of dialog box
                                    editAuthorNameDialog.setSize(550,100);
                                    //setting the layout as grid layout
                                    editAuthorNameDialog.setLayout(new GridLayout(2,2));
                                    //make text boxes/fields
                                    JTextField author=new JTextField();
                                    //add the delete book button
                                    JButton editAuthorButton=new JButton("Done");
                                    editAuthorNameDialog.add(new JLabel("Enter new author name: "));
                                    editAuthorNameDialog.add(author);
                                    //first add empty cell 
                                    editAuthorNameDialog.add(new JLabel(""));
                                    editAuthorNameDialog.add(editAuthorButton);
                                    editAuthorButton.addActionListener(new ActionListener(){
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                             final String A=author.getText();
                                             if(!A.isEmpty()){
                                             m1.setValueAt(A, count, 1);
                                             showEditSuccessMessage();
                                             editAuthorNameDialog.dispose();
                                             }
                                        }
                                    });
                                    //make visible
                                    editAuthorNameDialog.setVisible(true);
                            }
                       
                        });
                        //edit year label
                        editBookDialog2.add(new JLabel("Click to edit year of publication of the book: "));
                        //add the year editing book button
                        JButton yearEdit=new JButton("Edit year");
                        editBookDialog2.add(yearEdit);
                        
                        //add actionListener of edit year 
                        yearEdit.addActionListener(new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                     //separate gui screen
                                    JDialog editYearDialog=new JDialog(); 
                                    //title of gui screen
                                    editYearDialog.setTitle("Edit Year Of Publication");
                                    //set size of dialog box
                                    editYearDialog.setSize(550,100);
                                    //setting the layout as grid layout
                                    editYearDialog.setLayout(new GridLayout(2,2));
                                    //make text boxes/fields
                                    JTextField year=new JTextField();
                                    //add the delete book button
                                    JButton editYearButton=new JButton("Done");
                                    editYearDialog.add(new JLabel("Enter new year of publication: "));
                                    editYearDialog.add(year);
                                    //first add empty cell 
                                    editYearDialog.add(new JLabel(""));
                                    editYearDialog.add(editYearButton);
                                    editYearButton.addActionListener(new ActionListener(){
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                             final String Y=year.getText();
                                             if(!Y.isEmpty())
                                             {
                                                int num=0;
                                                try
                                                {
                                                    num=Integer.parseInt(Y);
                                                    if(num>0 && num<2024)
                                                    {
                                                        m1.setValueAt(Y, count, 2);
                                                         showEditSuccessMessage();
                                                         editYearDialog.dispose();
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
                                    //make visible
                                    editYearDialog.setVisible(true);
                            }
                       
                        });
                        //add the add book button
                        JButton Done=new JButton("Done");
                        //first add empty cell 
                        editBookDialog2.add(new JLabel(""));
                        //first add empty cell 
                        editBookDialog2.add(new JLabel(""));
                        //first add empty cell 
                        editBookDialog2.add(new JLabel(""));
                        editBookDialog2.add(Done);
                        //make done button functional
                        Done.addActionListener(new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                 //update the file
                                try{
                                    //dont append the data just write the data from the table
                                    FileWriter fw=new FileWriter("data.txt",false);
                                    for(int x=0;x<m1.getRowCount();x++)
                                    {
                                        for(int y=0;y<m1.getColumnCount();y++)
                                        {
                                            if(y!=3)
                                            {
                                            //read each cell
                                            Object entry=m1.getValueAt(x, y);
                                            if(entry!=null)
                                            {
                                                fw.write(entry.toString());
                                            }
                                            if(y<m1.getColumnCount()-2)
                                            {
                                                fw.write(",");
                                            }
                                            }

                                        }
                                        fw.write("\n");
                                    }
                                    fw.close();
                                }
                                catch(IOException ex)
                                {
                                }
                                editBookDialog.dispose();
                                editBookDialog2.dispose();
                            }
                            
                        });
                        //set visibility
                        editBookDialog2.setVisible(true);
                        return;
                   }
                }
                //if book not found
                showbookNotFoundMessage();
            }
        });
        //set visibility to true
        editBookDialog.setVisible(true);
        
    }
    //delete book function
    private void deleteBook()
    {
        //separate gui screen
        JDialog deleteBookDialog=new JDialog(); 
        //title of gui screen
        deleteBookDialog.setTitle("Delete Book");
        //set size of dialog box
        deleteBookDialog.setSize(500,100);
        //setting the layout as grid layout
        deleteBookDialog.setLayout(new GridLayout(2,2));
        //make text boxes/fields
        JTextField title=new JTextField();
        //add the delete book button
        JButton deleteButton=new JButton("Delete Book");
        deleteBookDialog.add(new JLabel("Enter the title of the book: "));
        deleteBookDialog.add(title);
        //first add empty cell 
        deleteBookDialog.add(new JLabel(""));
        deleteBookDialog.add(deleteButton);
        deleteButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
            String EnteredTitle=title.getText();
            //remove entry from the JTable
                for (int i=0;i<m1.getRowCount();i++)
                {
                   String name=(String)m1.getValueAt(i, 0);
                   //if book is in table
                   if(name.equals(EnteredTitle))
                   {
                       m1.removeRow(i);
                       showDeleteSuccessMessage();
                       //remove row from file
                        try{
                            //dont append the data just write the data from the table
                            FileWriter fw=new FileWriter("data.txt",false);
                            for(int x=0;x<m1.getRowCount();x++)
                            {
                                for(int y=0;y<m1.getColumnCount();y++)
                                {
                                    if(y!=3)
                                    {
                                    //read each cell
                                    Object entry=m1.getValueAt(x, y);
                                    if(entry!=null)
                                    {
                                        fw.write(entry.toString());
                                    }
                                    if(y<m1.getColumnCount()-2)
                                    {
                                        fw.write(",");
                                    }
                                    }

                                }
                                fw.write("\n");
                            }
                            fw.close();
                        }
                        catch(IOException ex)
                        {
                        }
                        deleteBookDialog.dispose();
                       return;
                   }
                }
                showbookNotFoundMessage();
            }
        });
        deleteBookDialog.setVisible(true);
        
    }
   
    
    //saving data to the text file
    private void saveToTextFile(String title, String author,String year) throws IOException
    {
        try(FileWriter filewriter=new FileWriter("data.txt",true)){
            String data="\n"+title+","+author+","+year;
        filewriter.write(data);
        showSuccessMessage();
        }
        catch(IOException e)
        {
        }
    }
    private void showEditSuccessMessage()
    {
        JOptionPane.showMessageDialog(null, "Edited successfully !");
    }
    private void showSuccessMessage()
    {
        JOptionPane.showMessageDialog(null, "Data added successfully !");
    }
    private void showbookNotFoundMessage()
    {
        JOptionPane.showMessageDialog(null, " Book not found in the Library !");
    }
    private void showDeleteSuccessMessage()
    {
        JOptionPane.showMessageDialog(null, "Book deleted successfully !");
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
