
# Library Management System

This project implements a basic library management system in Java. It provides functionalities for librarians to manage book data and visualize book popularity.




## Features

- Load book data from a text file.
- Display book information in a table (title, author, publication year).
- Add new books to the library system.
- Edit existing book details.
- Delete books from the library system.
- Generate a bar chart to visualize book popularity (based on randomly generated values).


## Prerequisites and Working Flow

- Ensure you have Java installed and configured on your system.
- Compile the source code using a Java compiler (e.g., Apache NetBeans IDE).
- Run the main class to see the working of code.

## Data Storage

The system stores book data in a text file named "data.txt".

## Major Functionalities

- **loadData():** This method reads book information from "data.txt" and populates a JTable for display.
- **createBarGraph():** This method generates a bar chart representing book popularity using a separate BarChart class. (Currently, popularity values are randomly generated).
- **addBook():** This method allows adding new books to the system. It opens a dialog to enter book details, adds them to the table, and saves them to the text file.
- **editBook():** This method enables editing existing book details. It allows searching for a book by title and then modifying specific details (title, author, year) in separate dialogs.
- **deleteBook():** This method facilitates deleting books from the system. It prompts the user for the book title and removes it from the table and the text file upon confirmation.
- **saveToTextFile():** This method helps save newly added book data to the "data.txt" file.
