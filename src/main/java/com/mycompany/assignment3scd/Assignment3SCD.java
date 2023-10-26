package com.mycompany.assignment3scd;
//import libraries
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
class Library{
    void loadData() throws FileNotFoundException
    {
        //create frame
        JFrame frame=new JFrame("Library Management System");
        frame.setLayout(new BorderLayout());
        //creating default table model
        DefaultTableModel m1=new DefaultTableModel();
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
              m1.addRow(new Object[]{value[0],value[1],value[2],new JButton("Read Item")});
          }
        }
        
        //setting visibility equal to true
        frame.setVisible(true);
    }

}

public class Assignment3SCD {

    public static void main(String[] args) throws FileNotFoundException {
        Library l1=new Library();
        l1.loadData();
    }
   

}
