package kr.book.search;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class BookSearchMain {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("도서 제목을 입력 : ");
            String bookTitle = scanner.nextLine();
            List<Book> books = KakaoBookAPI.searchBooks(bookTitle);

            if (books.isEmpty()){
                System.out.println("결과 없음");
            }else {
                for (Book book : books){
                    System.out.println(book);
                }
                String fileName = "도서목록.pdf";
                PdfGenerator.generateBookListPdf(books, fileName);
                System.out.println(fileName+" 파일이 생성");
            }
        } catch (IOException e) {
            System.out.println("에러 발생" + e.getMessage());
        }
    }
}
