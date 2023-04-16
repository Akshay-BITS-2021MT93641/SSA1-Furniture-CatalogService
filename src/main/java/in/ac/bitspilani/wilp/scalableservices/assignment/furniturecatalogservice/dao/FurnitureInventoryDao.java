package in.ac.bitspilani.wilp.scalableservices.assignment.furniturecatalogservice.dao;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Repository
public class FurnitureInventoryDao
{

    @Value("${ssa1.furniture.inventory.getStockUrl}")
    private String getStockInventoryUrl;
    
    @SuppressWarnings("unchecked")
    public Mono<Map<String, Integer>> getStock(UUID catalogItemId)
    {
        return
                WebClient.builder()
                .build()
                .get()
                .uri(getStockInventoryUrl, catalogItemId.toString())
                .retrieve()
                .bodyToMono(Map.class)
                .switchIfEmpty(Mono.just(Collections.emptyMap()))
                .map(m->(Map<String, Integer>)m.get("colorWiseStock"))
                .switchIfEmpty(Mono.just(Collections.emptyMap()));        
    }
}
