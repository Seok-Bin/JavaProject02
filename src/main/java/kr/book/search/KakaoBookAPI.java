package kr.book.search;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.itextpdf.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.List;

public class KakaoBookAPI {
    private static final String API_KEY = "805d7e6f989a7fb7c69c4abd685730b0"; //rest key
    private static final String API_BASE_URL = "https://dapi.kakao.com/v3/search/book";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    //책 검색 메서드
    public static List<Book> searchBooks(String title) throws java.io.IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_BASE_URL).newBuilder(); // url 연결하는 객체 생성
        urlBuilder.addQueryParameter("query", title); // query 변수에 title을 넘겨준다 --> url접속


        // url을 이용하여 서버에 요청
        Request request = new Request.Builder()
                .url(urlBuilder.build()) //url 생성
                .addHeader("Authorization", "KakaoAK " + API_KEY) //key값을 이용하여 요청
                .build();

        try (Response response = client.newCall(request).execute()){ // 서버로 접속 요청
            if (!response.isSuccessful()) throw new IOException("Request failed: " + response); //응답이 성공이 아니면 예외처리

            //정상이라면 json처리
            JsonObject jsonResponse = gson.fromJson(response.body().charStream(), JsonObject.class); //response.body는 데이터이다-> 이 데이터를 케릭터 스트림으로 만들어 jsonObject로 변환을 시킨다.
            JsonArray documents = jsonResponse.getAsJsonArray("documents"); //documents는 배열로 만들어져 있기 때문에 배열로 받는다.


            //책정보를 리스트에 넣기
            List<Book> books = new ArrayList<>();
            for (JsonElement document : documents){ //배열에서 책정보를 하나씩 꺼내기
                JsonObject bookJson = document.getAsJsonObject(); // 하나의 책정보를 object 타입으로 변환시켜준다.
                Book book = new Book(
                        bookJson.get("title").getAsString(),
                        bookJson.get("authors").getAsJsonArray().toString(),
                        bookJson.get("publisher").getAsString(),
                        bookJson.get("thumbnail").getAsString()
                );
                books.add(book);
            }
            return books;

        }
    }

}
