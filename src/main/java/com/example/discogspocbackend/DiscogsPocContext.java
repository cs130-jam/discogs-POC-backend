package com.example.discogspocbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import javax.net.ssl.SSLException;

@Import({
        ArtistResource.class
})
public class DiscogsPocContext {

    @Bean
    @SneakyThrows(SSLException.class)
    public SslContext sslContext() {
        return SslContextBuilder.forClient().build();
    }

    @Bean
    public DiscogsWebClientProvider webClientProvider(
            @Value("${discogs.api.base.url}") String baseUrl,
            @Value("${discogs.api.user.agent}") String userAgent,
            @Value("${discogs.api.user.token}") String token,
            SslContext sslContext
    ) {
        return new DiscogsWebClientProvider(baseUrl, userAgent, token, sslContext);
    }

    @Bean
    public DiscogsService discogsService(
            DiscogsWebClientProvider clientProvider,
            @Value("${discogs.api.max.result.count}") int maxResults,
            @Value("${discogs.api.default.results.count}") int defaultResults
    ) {
        return new DiscogsService(clientProvider, maxResults, defaultResults);
    }

    @Primary
    @Bean
    public ObjectMapper objectMapper() {
        return ObjectMapperProvider.get();
    }
}
