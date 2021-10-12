package com.example.discogspocbackend.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import java.util.List;

@Value
public class SearchResponse {

    Pagination pagination;
    List<Result> results;

    @Value
    public static class Pagination {
        int page;
        int per_page;
        int pages;
        int items;
        Urls urls;
    }

    @Value
    public static class Urls {
        String last;
        String next;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Value
    public static class Result {
        String title;
        String country;
        String uri;
        List<String> genre;
        String resource_url;
        String type;
        String id;
        String thumb;
        String cover_image;
    }

    @Value
    public static class ArtistView {
        String name;
        String thumb;
        String url;

        public static ArtistView ofResult(Result result) {
            return new ArtistView(result.title, result.thumb, result.resource_url);
        }
    }
}
