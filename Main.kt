// Жанры книг
enum class Genre(val title: String) {
    FICTION("Художественная"),
    SCIENTIFIC("Научная"),
    FANTASY("Фантастика"),
    DETECTIVE("Детектив"),
    CHILDREN("Детская")
}

// Статусы книг
enum class BookStatus(val displayName: String) {
    AVAILABLE("Доступна"),
    BORROWED("Выдана"),
    LOST("Потеряна"),
    RESERVED("Зарезервирована")
}

// Класс книги
data class Book(
    val id: Int,
    var title: String,
    var author: String,
    var year: Int,
    var genre: Genre,
    var status: BookStatus
)

// Класс самой библиотеки (хранит каталог и умеет с ним работать)
class Library {
    private val catalog = mutableListOf<Book>()
    private var nextId = 1

    // Метод добавления книги
    fun addBook(title: String, author: String, year: Int, genre: Genre) {
        val newBook = Book(nextId++, title, author, year, genre, BookStatus.AVAILABLE)
        catalog.add(newBook)
        println("Книга '${title}' успешно добавлена (ID: ${newBook.id}).")
    }

    // Метод вывода всех книг
    fun printAll() {
        if (catalog.isEmpty()) {
            println("Библиотека пуста.")
            return
        }
        println("\n--- ВСЕ КНИГИ ---")
        for (book in catalog) {
            println("ID: ${book.id} | ${book.title} | ${book.author} | ${book.year} г. | ${book.genre.title} | ${book.status.displayName}")
        }
    }

    // Метод поиска по названию
    fun searchByTitle(query: String) {
        var found = false
        println("\n--- РЕЗУЛЬТАТЫ ПОИСКА ---")
        for (book in catalog) {
            if (book.title.lowercase().contains(query.lowercase())) {
                println("ID: ${book.id} | ${book.title} | ${book.author} | ${book.status.displayName}")
                found = true
            }
        }
        if (!found) println("Книги с таким названием не найдены.")
    }

    // Метод поиска по автору
    fun searchByAuthor(query: String) {
        var found = false
        println("\n--- РЕЗУЛЬТАТЫ ПОИСКА ---")
        for (book in catalog) {
            if (book.author.lowercase().contains(query.lowercase())) {
                println("ID: ${book.id} | ${book.title} | ${book.author} | ${book.year} г.")
                found = true
            }
        }
        if (!found) println("Книги этого автора не найдены.")
    }

    // Метод фильтрации по жанру
    fun filterByGenre(genre: Genre) {
        var found = false
        println("\n--- КНИГИ В ЖАНРЕ '${genre.title}' ---")
        for (book in catalog) {
            if (book.genre == genre) {
                println("ID: ${book.id} | ${book.title} | ${book.author}")
                found = true
            }
        }
        if (!found) println("В этом жанре книг нет.")
    }

    // Метод редактирования книги
    fun editBook(id: Int) {
        val book = catalog.find { it.id == id }
        if (book == null) {
            println("Книга с ID $id не найдена.")
            return
        }

        println("Редактирование книги: ${book.title}")

        print("Новое название (оставьте пустым, чтобы не менять): ")
        val newTitle = readlnOrNull() ?: ""
        if (newTitle.isNotBlank()) book.title = newTitle

        print("Новый автор (оставьте пустым, чтобы не менять): ")
        val newAuthor = readlnOrNull() ?: ""
        if (newAuthor.isNotBlank()) book.author = newAuthor

        print("Новый год (введите 0, чтобы не менять): ")
        val newYearStr = readlnOrNull() ?: "0"
        val newYear = newYearStr.toIntOrNull() ?: 0
        if (newYear != 0) book.year = newYear

        println("Выберите новый статус:")
        println("1. Доступна\n2. Выдана\n3. Потеряна\n4. Зарезервирована")
        print("Ваш выбор (0 - оставить текущий): ")
        when (readlnOrNull()) {
            "1" -> book.status = BookStatus.AVAILABLE
            "2" -> book.status = BookStatus.BORROWED
            "3" -> book.status = BookStatus.LOST
            "4" -> book.status = BookStatus.RESERVED
        }

        println("Книга успешно обновлена!")
    }

    // Вспомогательный метод для получения жанра по цифре
    fun getGenreByChoice(choice: String): Genre? {
        return when (choice) {
            "1" -> Genre.FICTION
            "2" -> Genre.SCIENTIFIC
            "3" -> Genre.FANTASY
            "4" -> Genre.DETECTIVE
            "5" -> Genre.CHILDREN
            else -> null
        }
    }
}

// ================= ГЛАВНОЕ МЕНЮ =================
fun main() {
    val library = Library()

    // Заранее добавим пару книг для теста
    library.addBook("Мастер и Маргарита", "Булгаков", 1967, Genre.FICTION)
    library.addBook("Краткая история времени", "Хокинг", 1988, Genre.SCIENTIFIC)

    var isRunning = true

    while (isRunning) {
        println("\n=== МЕНЮ БИБЛИОТЕКИ ===")
        println("1. Показать все книги")
        println("2. Добавить книгу")
        println("3. Найти по названию")
        println("4. Найти по автору")
        println("5. Фильтр по жанру")
        println("6. Редактировать книгу")
        println("0. Выход")
        print("Выберите действие: ")

        when (readlnOrNull()) {
            "1" -> library.printAll()

            "2" -> {
                println("\n--- ДОБАВЛЕНИЕ КНИГИ ---")
                print("Название: ")
                val title = readlnOrNull() ?: "Без названия"

                print("Автор: ")
                val author = readlnOrNull() ?: "Неизвестен"

                print("Год издания: ")
                val year = readlnOrNull()?.toIntOrNull() ?: 0

                println("Жанр (1-Художественная, 2-Научная, 3-Фантастика, 4-Детектив, 5-Детская): ")
                val genre = library.getGenreByChoice(readlnOrNull() ?: "") ?: Genre.FICTION

                library.addBook(title, author, year, genre)
            }

            "3" -> {
                print("\nВведите название (или его часть): ")
                library.searchByTitle(readlnOrNull() ?: "")
            }

            "4" -> {
                print("\nВведите автора: ")
                library.searchByAuthor(readlnOrNull() ?: "")
            }

            "5" -> {
                println("\nВыберите жанр:")
                println("1. Художественная\n2. Научная\n3. Фантастика\n4. Детектив\n5. Детская")
                print("Ваш выбор: ")
                val genre = library.getGenreByChoice(readlnOrNull() ?: "")
                if (genre != null) {
                    library.filterByGenre(genre)
                } else {
                    println("Неверный выбор жанра.")
                }
            }

            "6" -> {
                print("\nВведите ID книги для редактирования: ")
                val id = readlnOrNull()?.toIntOrNull()
                if (id != null) {
                    library.editBook(id)
                } else {
                    println("Неверный ID.")
                }
            }

            "0" -> {
                println("Выход из программы. До свидания!")
                isRunning = false
            }

            else -> println("Неверный ввод. Пожалуйста, выберите пункт из меню.")
        }
    }
}