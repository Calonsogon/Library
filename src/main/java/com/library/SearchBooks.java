/* package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class SearchBooks {

    public void searchBook() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Seleccione una opción para buscar el libro:");
            System.out.println("1. Buscar por título");
            System.out.println("2. Buscar por autor");
            System.out.println("3. Buscar por género");
            System.out.println("4. Salir");
            
            int option = scanner.nextInt();
            scanner.nextLine();  
            
            if (option == 4) {
                System.out.println("Saliendo del programa...");
                break;
            }

            boolean found = false;
            int count = 0;
            switch (option) {
                case 1:
                    count = searchBookByTitle(scanner);
                    found = count > 0;
                    break;
                case 2:
                    count = searchBookByAuthor(scanner);
                    found = count > 0;
                    break;
                case 3:
                    count = searchBookByGender(scanner);
                    found = count > 0;
                    break;
                default:
                    System.out.println("Esta opción no existe: Selecciona la opción de nuevo del 1 al 4:\n");
                    continue;
            }

            if (found) {
                System.out.println("Se han encontrado " + count + " libro(s) con los criterios especificados.");
                break;
            } else {
                System.out.println("No se han encontrado libros con los criterios especificados.");
            }
        }
        scanner.close();
    }

    private int searchBookByTitle(Scanner scanner) {
        System.out.println("¿Cuál es el título del libro que quiere buscar?:");
        String bookTitle = scanner.nextLine();
        return search("SELECT * FROM books WHERE title = ?", bookTitle, true);
    }

    private int searchBookByAuthor(Scanner scanner) {
        System.out.println("¿Quién es el autor del libro que quiere buscar?:");
        String bookAuthor = scanner.nextLine();
        return search("SELECT * FROM books WHERE author = ?", bookAuthor, true);
    }

    private int searchBookByGender(Scanner scanner) {
        System.out.println("¿Cuál es el género del libro que quiere buscar?:");
        String bookGender = scanner.nextLine();
        return search("SELECT * FROM books WHERE gender = ?", bookGender, false);
    }

    private int search(String query, String parameter, boolean includeDescription) {
        int count = 0;
        try (Connection connect = new Cconnection().setConnection();
             PreparedStatement preparedStatement = connect.prepareStatement(query)) {

            preparedStatement.setString(1, parameter);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                count++;
                int id = resultSet.getInt("id_book");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String isbn = resultSet.getString("isbn_code");
                String author = resultSet.getString("author");
                String gender = resultSet.getString("gender"); 

                if (includeDescription) {
                    System.out.println("ID: " + id + ", Título: " + title + ", Descripción: " + description +
                            ", ISBN: " + isbn + ", Autor: " + author + ", Género: " + gender *;
                } else {
                    System.out.println("ID: " + id + ", Título: " + title +
                            ", ISBN: " + isbn + ", Autor: " + author + ", Género: " + gender );
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return count;
    }
}
 */
package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class SearchBooks {

    public void searchBook() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Seleccione una opción para buscar el libro:");
            System.out.println("1. Buscar por título");
            System.out.println("2. Buscar por autor");
            System.out.println("3. Buscar por género");
            System.out.println("4. Salir");
            
            int option = scanner.nextInt();
            scanner.nextLine();  
            
            if (option == 4) {
                System.out.println("Saliendo del programa...");
                break;
            }

            boolean found = false;
            int count = 0;
            switch (option) {
                case 1:
                    count = searchBookByTitle(scanner);
                    found = count > 0;
                    break;
                case 2:
                    count = searchBookByAuthor(scanner);
                    found = count > 0;
                    break;
                case 3:
                    count = searchBookByGender(scanner);
                    found = count > 0;
                    break;
                default:
                    System.out.println("Esta opción no existe: Selecciona la opción de nuevo del 1 al 4:\n");
                    continue;
            }

            if (found) {
                System.out.println("Se han encontrado " + count + " libro(s) con los criterios especificados.");
            } else {
                System.out.println("No se han encontrado libros con los criterios especificados.");
            }
        }
    }

    private int searchBookByTitle(Scanner scanner) {
        System.out.println("¿Cuál es el título del libro que quiere buscar?:");
        String bookTitle = scanner.nextLine();
        return search("SELECT * FROM books WHERE title = ?", bookTitle, true);
    }

    private int searchBookByAuthor(Scanner scanner) {
        System.out.println("¿Quién es el autor del libro que quiere buscar?:");
        String authorFullName = scanner.nextLine();
        String[] nameParts = authorFullName.split(" ");
        String authorName = nameParts[0];
        String authorLastName = nameParts.length > 1 ? nameParts[1] : "";

        return search("SELECT b.id_book, b.title, b.description, b.isbn_code FROM books b INNER JOIN authors_books ab ON b.id_book = ab.id_book INNER JOIN authors a ON ab.id_author = a.id_author WHERE a.name = ? OR a.last_name = ?", authorName, authorLastName, true);
    }

    private int searchBookByGender(Scanner scanner) {
        System.out.println("¿Cuál es el género del libro que quiere buscar?:");
        String bookGender = scanner.nextLine();
        return search("SELECT b.id_book, b.title, b.description, b.isbn_code FROM books b INNER JOIN genres_books gb ON b.id_book = gb.id_book INNER JOIN genres g ON gb.id_genre = g.id_genre WHERE g.genre = ?", bookGender, true);
    }

    private int search(String query, String parameter, boolean includeDescription) {
        int count = 0;
        try (Connection connect = new Cconnection().setConnection();
             PreparedStatement preparedStatement = connect.prepareStatement(query)) {

            preparedStatement.setString(1, parameter);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                count++;
                int id = resultSet.getInt("id_book");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String isbn = resultSet.getString("isbn_code");

                if (includeDescription) {
                    System.out.println("ID: " + id + ", Título: " + title + ", Descripción: " + description +
                            ", ISBN: " + isbn);
                } else {
                    System.out.println("ID: " + id + ", Título: " + title +
                            ", ISBN: " + isbn);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return count;
    }
    private int search(String query, String param1, String param2, boolean includeDescription) {
        int count = 0;
        try (Connection connect = new Cconnection().setConnection();
             PreparedStatement preparedStatement = connect.prepareStatement(query)) {

            preparedStatement.setString(1, param1);
            preparedStatement.setString(2, param2);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                count++;
                int id = resultSet.getInt("id_book");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String isbn = resultSet.getString("isbn_code");

                if (includeDescription) {
                    System.out.println("ID: " + id + ", Título: " + title + ", Descripción: " + description +
                            ", ISBN: " + isbn);
                } else {
                    System.out.println("ID: " + id + ", Título: " + title +
                            ", ISBN: " + isbn);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return count;
    }
}
