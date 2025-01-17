package com.library.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.library.controller.GenresBooksController;
import com.library.controller.GenresController;
import com.library.model.Genre;
import com.library.model.GenreBookDAO;
import com.library.model.GenreBookDAOInterface;

public class GenreView {
  private GenreBookDAOInterface genreBookDAO = new GenreBookDAO();
  private GenresBooksController genresBooksController = new GenresBooksController(genreBookDAO);
  private GenreBookView genreBookView = new GenreBookView(genresBooksController);

  private GenresController genresController;

  public GenreView(GenresController genresController) {
    this.genresController = genresController;
  }

  public List<Genre> getGenres(Scanner scanner) {
    List<Genre> genres = new ArrayList<>();
    String addMore;
    do {
      System.out.print("Género: ");
      String genreBook = scanner.nextLine();
      genres.add(new Genre(genreBook));
      System.out.print("¿Desea agregar otro género? (s/n): ");
      addMore = scanner.nextLine();
    } while (addMore.equalsIgnoreCase("s"));
    return genres;
  }

  public boolean addGenres(List<Genre> genres, int bookId) {
    boolean success = true;
    for (Genre genre : genres) {
      int genreId = genresController.addGenre(genre);
      if (genreId > 0) {
        success = genreBookView.addGenreBook(genreId, bookId);
      } else {
        success = false;
        System.out.println("Fallo al añadir género.");
      }
    }
    return success;
  }

  public List<Genre> updateGenresByBook(Scanner scanner, int bookId) {
    boolean ask = true;
    List<Genre> genres = new ArrayList<>();
    System.out.println(
        "\nEstos son los géneros vinculados a este libro.");

    while (ask) {
      genres = genresBooksController.getGenresByBookId(bookId);
      genreBookView.showGenresByBookId(genres);
      System.out.println("\033[34m\nElija la opción que quiera realizar para los géneros de este libro: \033[0m");
      System.out.println(" 1. Agregar nuevo género\n 2. Desvincular género\n 3. Salir");

      int genreOption = scanner.nextInt();
      scanner.nextLine();

      switch (genreOption) {
        case 1:
          List<Genre> newGenres = getGenres(scanner);
          addGenres(newGenres, bookId);
          System.out.println("\033[32m\nAgregando género...\033[0m");
          System.out.println(
              "\033[32m\nEstos son los géneros vinculados a este libro actualizados.\033[0m");
          break;
        case 2:
          boolean foundGenre = false;
          while (!foundGenre) {
            System.out.println("\033[33m\nIntroduzca el id del género a desvincular\033[0m");
            int genreId = scanner.nextInt();
            foundGenre = genres.stream().anyMatch(genre -> genre.getIdGenre() == genreId);
            if (foundGenre) {
              System.out.println("\033[32m\nDesvinculando género...\033[0m");
              genresBooksController.deleteGenresBookByBookId(bookId, genreId);
              System.out.println(
                  "\n\033[32m\nEstos son los generos vinculados a este libro actualizados.\033[0m");
            } else {
              System.out.println("\033[31m\nNúmero de id incorrecto.\033[0m");
            }
          }
          break;
        case 3:
          ask = false;
          return genres;
        default:
          System.out.println("\033[31m\nOpción incorrecta. Intente de nuevo. \033[0m");
      }
    }
    return genres;
  }
}