package com.mycompany.assignment3scd;
//import libraries
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.TableCellRenderer;


class BarChart extends JPanel
{
    private Map<String,Integer>data;
    private int xSpace=20;
    //constructor
    public BarChart(Map<String,Integer> data)
    {
        this.data=data;
    }
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        int x=30+xSpace;
        int y=300;
        int barWidth=50;
        int spacing =40;
        int maxY=0;
        //find max count for scaling
        for(Integer count: data.values())
        {
            if(count>maxY)
            {
                maxY=count;
            }
        }
         // Draw the x-axis
        g.setColor(Color.black);
        g.drawLine(30, y, x + data.size() * (barWidth + spacing), y);

        // Draw the y-axis
        g.drawLine(30, 50, 30, y);
        // Draw the y-axis label
        g.setColor(Color.black);
        Graphics2D g2d = (Graphics2D) g;
        g2d.rotate(-Math.PI / 2); // Rotate 90 degrees counter-clockwise
        g2d.drawString("Popularity Counts", -((y + 50) / 2), 10); // Position vertically
        g2d.rotate(Math.PI / 2); // Reset rotation

        // Draw the y-axis values
        g.setColor(Color.black);
        for (int i = 0; i <= maxY; i += 10) {
            int yValue = y - (int) ((double) i / maxY * (y - 50));
            g.drawString(String.valueOf(i), 10, yValue + 5);
            g.drawLine(30, yValue, 25, yValue);
        }
        //draw the bars and labels
        g.setColor(Color.blue);
        for(Map.Entry<String,Integer>entry:data.entrySet())
        {  
            int k=x;
            g.setColor(Color.blue);
            String label=entry.getKey();
            int count=entry.getValue();
            int barHeight=(int) ((double) count / maxY * (y - 50));
            g.fillRect(x, y-barHeight, barWidth, barHeight);
            g.setColor(Color.BLACK);
            //add labels
            g.drawString(label, x, y+20);
            
            // Add the bar height value on top of the bar
            g.drawString(String.valueOf(count), x, y - barHeight - 20);
            
            //set next x coordinate
            x+=barWidth+spacing;
        }
    }
}

class Library{
    Map<Integer,String>rowTextMap=new HashMap<>();
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
        JButton ViewPopularity=new JButton("View Popularity");
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
        ViewPopularity.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String,Integer> bardata=new HashMap<>();
                Random random=new Random();
                for(int i=0;i<m1.getRowCount();i++)
                {
                    for(int j=0;j<m1.getColumnCount();j++)
                    {
                        if(j==0)
                        {
                        //generating random values for popularity count
                        bardata.put((String)m1.getValueAt(i, j), random.nextInt(20)+1);
                        }
                    }
                }
                createBarGraph(bardata);
            }
        });
        buttonsPanel.add(editItem);
        buttonsPanel.add(deleteItem);
        buttonsPanel.add(ViewPopularity);
        frame.add(buttonsPanel,BorderLayout.SOUTH);
        //add exit on close for the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,400);
        
        //load the data
        File file=new File("data.txt");//Loading Books from file
        Scanner read= new Scanner(file);
        read.useDelimiter(",");
        int rowIndex=0;
        String datax=null;
        while(read.hasNextLine())//Till the last line
        {
          String line=read.nextLine();//line by line 
          Object[] value=line.split(",");
          if(value.length==3)
          {
              m1.addRow(new Object[]{value[0],value[1],value[2],"Read Item"});
              //setting the book content
              if(m1.getValueAt(rowIndex, 0).equals("Harry Potter"))
              {
               datax="The story revolves around a young orphan named Harry Potter, who discovers on his eleventh birthday that he is a wizard and has been accepted to attend Hogwarts School of Witchcraft and Wizardry. At Hogwarts, Harry makes friends and learns about the magical world, its history, and its mysteries."  ;
              }
              else if(m1.getValueAt(rowIndex, 0).equals("Cinderella"))
              {
                datax="The tale revolves around a kind and gentle young woman named Cinderella, who is mistreated and abused by her wicked stepmother and stepsisters. Despite her hardships, Cinderella remains resilient and kind. With the help of her magical fairy godmother, she is transformed from a poor, ragged servant into a beautiful princess dressed in a stunning gown, glass slippers, and a pumpkin carriage.";
              }
              else if(m1.getValueAt(rowIndex, 0).equals("Stuart Little"))
              {
                  datax="Despite his diminutive size, Stuart is embraced as a member of the Little family, alongside his human parents and older brother, George. He embarks on various adventures and navigates the challenges of living in a world designed for humans, such as driving a tiny car and interacting with animals who are both friends and foes.";
              }
              else
              {
                  datax=null;
              }
              rowTextMap.put(rowIndex, datax);
          }
          rowIndex++;
        }
         table.getColumnModel().getColumn(3).setCellRenderer(new RenderButtonForTable());
         table.getColumnModel().getColumn(3).setCellEditor(new ButtonEditorForTable(new JTextField(),table,m1,rowTextMap));
         
           
        
        //enable mouse hovering
        table.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0 ) {
                    table.getSelectionModel().setSelectionInterval(row, row);
                    table.setSelectionBackground(Color.LIGHT_GRAY);
                }
                else
                {
                    table.getSelectionModel().clearSelection();
                }
            }
        });
        //setting visibility equal to true
        frame.setVisible(true);
    }
    //create bar graph
    private void createBarGraph(Map<String,Integer>bardata)
    {
        //separate gui screen
        JDialog addPopoularityCountDialog=new JDialog(); 
        //title of gui screen
        addPopoularityCountDialog.setTitle("Bar Graph");
        //set size of dialog box
        addPopoularityCountDialog.setSize(700,500);
        //create JPanel for the bar chart
        JPanel barchart=new BarChart(bardata);
        addPopoularityCountDialog.add(barchart);
        //set visibility
        addPopoularityCountDialog.setVisible(true);

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
        JTextField title=new JTextField(10);
        JTextField author=new JTextField(10);
        JTextField yearofPublication=new JTextField(10);
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
        JTextField title=new JTextField(10);
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
                                    JTextField title=new JTextField(10);
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
                                    JTextField author=new JTextField(10);
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
                                    JTextField year=new JTextField(10);
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
        JTextField title=new JTextField(10);
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

class RenderButtonForTable extends JButton implements TableCellRenderer
{
    public RenderButtonForTable()
    {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText("Read Item");
        return this;
    }
}

class ButtonEditorForTable extends DefaultCellEditor 
{
    private final JButton button;
    private String label;
    private boolean isPushed;

    public ButtonEditorForTable(JTextField textField,JTable table,DefaultTableModel m1, Map<Integer,String>rowTextMap) 
    {
        super(textField);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener((ActionEvent e) -> 
        {
            
//separate gui screen
        JDialog ReadItem=new JDialog(); 
        //title of gui screen
        ReadItem.setTitle("Book Details");
        //set size of dialog box
        ReadItem.setSize(800,700);
        //select the row
        int selectedrow=table.getSelectedRow();
        if(selectedrow>=0)
        {
           this.label=rowTextMap.get(selectedrow);
           if(label!=null)
                {
                JTextArea contentArea=new JTextArea(label);
                ReadItem.add(new JScrollPane(contentArea));
                ReadItem.setSize(400,300);
                ReadItem.setVisible(true);
                }
           else
                {
                    JTextArea contentArea=new JTextArea("No content added");
                    ReadItem.add(new JScrollPane(contentArea));
                    ReadItem.setSize(400,300);
                    ReadItem.setVisible(true);
                }
           
            //add window listener frame
            ReadItem.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    int option = JOptionPane.showConfirmDialog(ReadItem, "Do you really want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        ReadItem.dispose(); // Close the dialog
                    }
                    else if( option==JOptionPane.NO_OPTION)
                    {
                        
                    }
                }
            });

        }
            
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (isSelected) 
        {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else
        {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue()
    {
        if (isPushed) 
        {

        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing()
    {
        isPushed = false;
        return super.stopCellEditing();
    }
}

public class Assignment3SCD {

    public static void main(String[] args) throws FileNotFoundException {
        Library l1=new Library();
        l1.loadData();
    }
   

}
