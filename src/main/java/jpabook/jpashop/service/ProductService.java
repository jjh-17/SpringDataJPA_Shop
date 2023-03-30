package jpabook.jpashop.service;

import jpabook.jpashop.domain.product.Album;
import jpabook.jpashop.domain.product.Book;
import jpabook.jpashop.domain.product.Movie;
import jpabook.jpashop.domain.product.Product;
import jpabook.jpashop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //조회 부분 성능 최적화
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional //단순 조회가 아닌 변경 부분은 따로 표시할 것
    public Long saveProduct(Product product) {
        productRepository.saveProduct(product);
        return product.getId();
    }

    @Transactional
    public void updateAlbum(Long id, String name, int price, int stock, String artist, String etc) {
        Album album = (Album) productRepository.findProductById(id);

        album.setName(name);
        album.setPrice(price);
        album.setStockQuantity(stock);
        album.setArtist(artist);
        album.setEtc(etc);
    }

    @Transactional
    public void updateBook(Long id, String name, int price, int stock, String author, String isbn) {
        Book book = (Book) productRepository.findProductById(id);

        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stock);
        book.setAuthor(author);
        book.setIsbn(isbn);
    }

    @Transactional
    public void updateMovie(Long id, String name, int price, int stock, String actor, String director) {
        Movie movie = (Movie) productRepository.findProductById(id);

        movie.setName(name);
        movie.setPrice(price);
        movie.setStockQuantity(stock);
        movie.setActor(actor);
        movie.setDirector(director);
    }

    public Product findProduct(Long id) {
        return productRepository.findProductById(id);
    }

    public List<Product> findProducts() {
        return productRepository.findProductAll();
    }

}
