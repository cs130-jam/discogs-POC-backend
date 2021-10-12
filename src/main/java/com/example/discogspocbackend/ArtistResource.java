package com.example.discogspocbackend;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.example.discogspocbackend.responses.SearchResponse.ArtistView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ArtistResource {

    private final DiscogsService discogsService;

    @GetMapping(value = "/artist/search", produces = APPLICATION_JSON_VALUE)
    public List<ArtistView> search(@RequestParam String artist, @RequestParam(required = false) Integer count) {
        try {
            if (count == null) {
                return discogsService.artistSearch(artist).get();
            } else {
                return discogsService.artistSearch(artist, count).get();
            }
        } catch (InterruptedException e) {
            log.error("Future interrupted for artist search {}, error: {}", artist, e);
            throw new SearchException();
        } catch (ExecutionException e) {
            log.error("Future execution failed for artist search {}, error: {}", artist, e);
            throw new SearchException();
        }
    }
}
