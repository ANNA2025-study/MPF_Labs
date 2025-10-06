package sumdu.edu.ua.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sumdu.edu.ua.core.port.CatalogRepositoryPort;
import sumdu.edu.ua.core.port.CommentRepositoryPort;

import java.io.IOException;

@Configuration
public class ServletConfig {

        public ServletConfig() {
            System.out.println("ServletConfig loaded!");
        }

        @Bean
        public ServletRegistrationBean<BooksServlet> booksServlet(CatalogRepositoryPort bookRepo) {
            return new ServletRegistrationBean<>(new BooksServlet(bookRepo), "/books/*");
        }

        @Bean
        public ServletRegistrationBean<CommentsServlet> commentsServlet(
                CatalogRepositoryPort bookRepo,
                CommentRepositoryPort commentRepo) {
            return new ServletRegistrationBean<>(new CommentsServlet(bookRepo, commentRepo), "/comments/*");
        }

        @Bean
        public ServletRegistrationBean<BooksApiServlet> booksApiServlet(
                CatalogRepositoryPort bookRepo,
                ObjectMapper om) {
            return new ServletRegistrationBean<>(new BooksApiServlet(bookRepo, om), "/api/books/*");
        }


}

