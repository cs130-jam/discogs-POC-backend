package com.example.discogspocbackend;

import static java.lang.Math.min;
import static java.util.stream.Collectors.toList;

import com.example.discogspocbackend.responses.SearchResponse;
import com.example.discogspocbackend.responses.SearchResponse.ArtistView;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class DiscogsService {

    private static final String ARTIST_SEARCH_TYPE = "artist";
    private final DiscogsWebClientProvider webClientProvider;
    private final int globalMaxCount;
    private final int defaultCount;

    public void artistSearch(String artist, Consumer<List<ArtistView>> artistsConsumer) {
        artistSearch(artist, defaultCount, artistsConsumer);
    }

    public void artistSearch(String artist, int maxCount, Consumer<List<ArtistView>> artistsConsumer) {
        paginatedRequest(
                new SearchRequest(artist, ARTIST_SEARCH_TYPE),
                min(maxCount, globalMaxCount),
                results -> artistsConsumer.accept(results.stream()
                        .map(ArtistView::ofResult)
                        .collect(toList())));
    }

    private void paginatedRequest(
            SearchRequest request,
            int countRemaining,
            Consumer<List<SearchResponse.Result>> resultsConsumer
    ) {
        paginatedRequest(request, countRemaining, 1, List.of(), resultsConsumer);
    }

    private void paginatedRequest(
            SearchRequest request,
            int countRemaining,
            int page,
            List<SearchResponse.Result> currentResults,
            Consumer<List<SearchResponse.Result>> resultsConsumer
    ) {
        request.withPage(page).subscribe(response -> {
            int perPage = response.getPagination().getPer_page();
            List<SearchResponse.Result> combinedResults = Stream.concat(
                    currentResults.stream(),
                    response.getResults()
                            .stream()
                            .limit(countRemaining))
                    .collect(toList());
            if (countRemaining <= perPage || page >= response.getPagination().getPages()) {
                resultsConsumer.accept(combinedResults);
            } else {
                paginatedRequest(request, countRemaining - perPage, page + 1, combinedResults, resultsConsumer);
            }
        });
    }

    @Value
    private class SearchRequest {
        String query;
        String type;

        public Mono<SearchResponse> withPage(int page) {
            return webClientProvider.get()
                    .get()
                    .uri("database/search?q={artist}&type={type}&page={page}", query, type, page)
                    .retrieve()
                    .bodyToMono(SearchResponse.class)
                    .doOnError(Throwable::printStackTrace);
        }
    }
}
