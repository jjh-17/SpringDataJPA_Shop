package jpabook.jpashop.controller;

import jpabook.jpashop.controller.form.ProductForm;
import jpabook.jpashop.domain.product.Album;
import jpabook.jpashop.domain.product.Book;
import jpabook.jpashop.domain.product.Movie;
import jpabook.jpashop.domain.product.Product;
import jpabook.jpashop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    //추가할 상품 선택
    @GetMapping("/products/new")
    public String decideProductForm() {
        return "products/decideProductForm";
    }

    @GetMapping("/products/new/album")
    public String createAlbumForm(Model model) {
        model.addAttribute("productForm", new ProductForm());
        return "products/createAlbumForm";
    }

    @PostMapping("/products/new/album")
    public String createAlbum(ProductForm albumForm) {
        Album album = new Album();

        album.setName(albumForm.getName());
        album.setPrice(albumForm.getPrice());
        album.setId(albumForm.getId());
        album.setStockQuantity(albumForm.getStockQuantity());
        album.setArtist(albumForm.getArtist());
        album.setEtc(albumForm.getEtc());

        productService.saveProduct(album);
        return "redirect:/products/new";
    }

    @GetMapping("/products/new/book")
    public String createProductForm(Model model) {
        model.addAttribute("productForm", new ProductForm());
        return "products/createBookForm";
    }

    @PostMapping("/products/new/book")
    public String createBook(ProductForm bookForm) {
        Book book = new Book();

        book.setName(bookForm.getName());
        book.setPrice(bookForm.getPrice());
        book.setId(bookForm.getId());
        book.setStockQuantity(bookForm.getStockQuantity());
        book.setAuthor(bookForm.getAuthor());
        book.setIsbn(bookForm.getIsbn());

        productService.saveProduct(book);
        return "redirect:/products/new";
    }

    @GetMapping("/products/new/movie")
    public String createMovieForm(Model model) {
        model.addAttribute("productForm", new ProductForm());
        return "products/createMovieForm";
    }

    @PostMapping("/products/new/movie")
    public String createMovie(ProductForm movieForm) {
        Movie movie = new Movie();

        movie.setName(movieForm.getName());
        movie.setPrice(movieForm.getPrice());
        movie.setId(movieForm.getId());
        movie.setStockQuantity(movieForm.getStockQuantity());
        movie.setActor(movieForm.getActor());
        movie.setDirector(movieForm.getDirector());

        productService.saveProduct(movie);
        return "redirect:/products/new";
    }

    //상품 리스트
    @GetMapping("/products")
    public String list(Model model) {
        List<Product> products = productService.findProducts();
        model.addAttribute("products", products);

        return "products/productList";
    }

    //상품 수정
    @GetMapping("/products/{productId}/edit")
    public String editProductForm(@PathVariable("productId") Long productId, Model model) {
        Product product = productService.findProduct(productId);
        ProductForm productForm = new ProductForm();

        if (product instanceof Album album) {

            productForm.setId(album.getId());
            productForm.setName(album.getName());
            productForm.setPrice(album.getPrice());
            productForm.setStockQuantity(album.getStockQuantity());
            productForm.setArtist(album.getArtist());
            productForm.setEtc(album.getEtc());

            model.addAttribute("form", productForm);

            return "products/updateAlbumForm";
        } else if (product instanceof Book book) {

            productForm.setId(book.getId());
            productForm.setName(book.getName());
            productForm.setPrice(book.getPrice());
            productForm.setStockQuantity(book.getStockQuantity());
            productForm.setAuthor(book.getAuthor());
            productForm.setIsbn(book.getIsbn());

            model.addAttribute("form", productForm);

            return "products/updateBookForm";
        } else if(product instanceof Movie movie) { //movie

            productForm.setId(movie.getId());
            productForm.setName(movie.getName());
            productForm.setPrice(movie.getPrice());
            productForm.setStockQuantity(movie.getStockQuantity());
            productForm.setActor(movie.getActor());
            productForm.setDirector(movie.getDirector());

            model.addAttribute("form", productForm);

            return "products/updateMovieForm";
        }
        return "/products";
    }

    @PostMapping("/products/{productId}/edit")
    public String editProduct(@PathVariable("productId") Long productId, @ModelAttribute("form") ProductForm productForm) {
        //조건은 추후 변경 필요
        if (productForm.getArtist() != null && productForm.getEtc() != null) {
//            Album album = new Album();
//
//            album.setId(productForm.getId());
//            album.setName(productForm.getName());
//            album.setPrice(productForm.getPrice());
//            album.setStockQuantity(productForm.getStockQuantity());
//            album.setArtist(productForm.getArtist());
//            album.setEtc(productForm.getEtc());
//
//            productService.saveProduct(album);

            //위 주석을 대체 ==> Controller에서는 엔티티 생성 지양할 것
            productService.updateAlbum(productId, productForm.getName(), productForm.getPrice(),
                    productForm.getStockQuantity(), productForm.getArtist(), productForm.getEtc());

        } else if (productForm.getAuthor() != null && productForm.getIsbn() != null) {
//            Book book = new Book();
//
//            book.setId(productForm.getId());
//            book.setName(productForm.getName());
//            book.setPrice(productForm.getPrice());
//            book.setStockQuantity(productForm.getStockQuantity());
//            book.setAuthor(productForm.getAuthor());
//            book.setIsbn(productForm.getIsbn());
//
//            productService.saveProduct(book);

            //위 주석을 대체 ==> Controller에서는 엔티티 생성 지양할 것
            productService.updateBook(productId, productForm.getName(), productForm.getPrice(),
                    productForm.getStockQuantity(), productForm.getAuthor(), productForm.getIsbn());

        } else if (productForm.getActor() != null && productForm.getDirector() != null) { //영화
//            Movie movie = new Movie();
//
//            movie.setId(productForm.getId());
//            movie.setName(productForm.getName());
//            movie.setPrice(productForm.getPrice());
//            movie.setStockQuantity(productForm.getStockQuantity());
//            movie.setActor(productForm.getActor());
//            movie.setDirector(productForm.getDirector());
//
//            productService.saveProduct(movie);

            //위 주석을 대체 ==> Controller에서는 엔티티 생성 지양할 것
            productService.updateMovie(productId, productForm.getName(), productForm.getPrice(),
                    productForm.getStockQuantity(), productForm.getActor(), productForm.getDirector());
        }

        return "redirect:/products";
    }
}
