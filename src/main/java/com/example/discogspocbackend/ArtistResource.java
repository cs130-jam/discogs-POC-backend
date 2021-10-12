package com.example.discogspocbackend;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ArtistResource {

    private final DiscogsService discogsService;

    @GetMapping(value = "/artist/search", produces = APPLICATION_JSON_VALUE)
    public List<String> search(@RequestParam String artist, @RequestParam(required = false) Integer count) {
//        Object pauseThread = new Object();
//        final List<String> results = new ArrayList<>();
//        synchronized (pauseThread) {
//            discogsService.artistSearch(artist, 100, out -> {
//                results.addAll(out);
//                log.info("results = {}", out);
//                pauseThread.notify();
//            });
//            try {
//                pauseThread.wait();
//                return results;
//            } catch (InterruptedException e) {
//                log.error("artist search error");
//                return List.of();
//            }
//        }

        if (count == null) {
            discogsService.artistSearch(artist, out -> {
                log.info("results count = {}", out.size());
                log.info("results = {}", out);
            });
        } else {
            discogsService.artistSearch(artist, count, out -> {
                log.info("results count = {}", out.size());
                log.info("results = {}", out);
            });
        }
        return List.of("too bad lol");
    }
}
