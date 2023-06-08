package ma.multitenant.repositories;

import ma.multitenant.entities.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Integer> {

}
