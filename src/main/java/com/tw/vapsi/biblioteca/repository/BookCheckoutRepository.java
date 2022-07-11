package com.tw.vapsi.biblioteca.repository;

import com.tw.vapsi.biblioteca.model.BookCheckout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCheckoutRepository extends JpaRepository<BookCheckout, Long> {
    List<BookCheckout> findByUserId(long id);

    BookCheckout findByBookIdAndUserId(long bookid, long userid);

    BookCheckout findByBookIdAndIsReturned(long bookid, boolean isReturned);

    List<BookCheckout> findByUserIdAndIsReturnedFalse(long userId);

}
