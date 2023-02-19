import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.library.Book;
import ru.library.Category;
import ru.library.Newspaper;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static ru.library.GenerateUtils.generateSomeBooks;
import static ru.library.GenerateUtils.generateSomeNewsPapers;
import static ru.library.SearchOperations.getBookByName;
import static ru.library.SearchOperations.getBookByNameAndAuthor;
import static ru.library.SortOperations.*;

class BookTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    // Раздел Category
    @Test
    @DisplayName("Установка категории")
    void shouldSetCategory() {
        final Book book = new Book("bookName", "bookAuthor");
        book.setCategory(new Category("categoryName"));

        assertNotNull(book.getCategory());
    }

    // Раздел Book
    @Test
    @DisplayName("Проверка пустой категории")
    void shouldSetEmptyCategory() {
        final Book book = new Book("bookName", "bookAuthor");
        book.setCategory(new Category(null));

        assertTrue(outContent.toString().startsWith("Не получилось привязать категорию, категория не содержит имени"));
    }

    @Test
    @DisplayName("Установка имени книги")
    void shouldSetBookName() {
        final Book book = new Book("bookName", "bookAuthor");
        book.setCategory(new Category(null));

        assertNotNull(book.getName());
    }

    @Test
    @DisplayName("Установка автора книги")
    void shouldSetBookAuthor() {
        final Book book = new Book("bookName", "bookAuthor");
        book.setCategory(new Category(null));

        assertNotNull(book.getAuthor());
    }


    // Блок Newspaper

    @Test
    @DisplayName("Установка даты новости")
    void shouldSetDateNewspaper() {
        Instant now = Instant.now();
        final Newspaper newspaper = new Newspaper("NewspaperName", now);

        assertNotNull(newspaper.getDate());
    }

    @Test
    @DisplayName("Установка имени новости")
    void shouldSetNameNewspaper() {
        Instant now = Instant.now();
        final Newspaper newspaper = new Newspaper("NewspaperName", now);

        assertNotNull(newspaper.getName());
    }

    // Блок GenerateUtils
    @Test
    @DisplayName("Генерирование книг")
    void shouldGenerateSomeBooks() {
        List<Book> list = generateSomeBooks(5);

        assertEquals(5, list.size());
    }

    @Test
    @DisplayName("Генерирование газет")
    void shouldGenerateSomeNewspapers() {
        List<Newspaper> list = generateSomeNewsPapers(3);

        assertEquals(3, list.size());
    }

    // Блок SearchOperations

    @Test
    @DisplayName("Поиск по имени и автору")
    void shouldGetBookByNameAndAuthor() {
        Random r = new Random();
        List<Book> books = generateSomeBooks(r.nextInt(3, 6));
        Book book = new Book("Два капитана", "В. Каверин");
        Category category1 = new Category("Приключения");
        book.setCategory(category1);
        books.get(0).setCategory(category1);
        books.add(book);
        Optional<Book> foundBook = getBookByNameAndAuthor("Два капитана", "В. Каверин", books);

        assertEquals("Два капитана", foundBook.get().getName());
        assertEquals("В. Каверин", foundBook.get().getAuthor());
    }

    @Test
    @DisplayName("Поиск по имени")
    void shouldGetBookByName() {
        Random r = new Random();
        List<Book> books = generateSomeBooks(r.nextInt(3, 6));
        Book book = new Book("Два капитана", "В. Каверин");
        Category category1 = new Category("Приключения");
        book.setCategory(category1);
        books.get(0).setCategory(category1);
        books.add(book);
        Optional<Book> foundBook = getBookByName("Два капитана", books);

        assertEquals("Два капитана", foundBook.get().getName());
    }


    // Блок SortOperations
    @Test
    @DisplayName("Сортировка новостей по дате")
    void shouldSortNewspapersByDate() {
        Random r = new Random();
        List<Newspaper> newspapers = generateSomeNewsPapers(r.nextInt(3, 6));
        sortNewspapersByDate(newspapers);

        assertNotNull(newspapers.get(0).getDate());
    }

    @Test
    @DisplayName("Проверка сортировки газет")
    void shouldCheckIfBookHasCategory() {
        Instant now = Instant.now();
        final Newspaper newspaper = new Newspaper("NewspaperName", now);
        checkIfBookHasCategory(newspaper);

        assertTrue(outContent.toString().startsWith("Газеты не сортируются по категориям"));

    }

    @Test
    @DisplayName("Проверка категорий книг")
    void shouldGetBooksCategories() {
        Random r = new Random();
        List<Book> books = generateSomeBooks(r.nextInt(3, 6));
        Book book = new Book("Два капитана", "В. Каверин");
        Category category1 = new Category("Приключения");
        book.setCategory(category1);
        books.get(0).setCategory(category1);
        books.add(book);
        List<Book> booksByCategory = getBooksByCategory(books, category1);

        assertEquals("Приключения", booksByCategory.get(0).getCategory().getName());
    }

    @Test
    @DisplayName("Проверка неизвестных категорий книг")
    void shouldGetBooksCategoriesNegativeTest() {
        Random r = new Random();
        List<Book> books = generateSomeBooks(r.nextInt(3, 6));
        Book book = new Book("Два капитана", "В. Каверин");
        Category category1 = new Category("Неизвестная");
        books.add(book);
        getBooksByCategory(books, category1);

        assertTrue(outContent.toString().startsWith("В библиотеке нет книг с заданной категорией"));

    }


}
