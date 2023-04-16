package in.ac.bitspilani.wilp.scalableservices.assignment.furniturecatalogservice.dao;

import java.util.UUID;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import in.ac.bitspilani.wilp.scalableservices.assignment.furniturecatalogservice.CatalogItem;
import reactor.core.publisher.Mono;

public interface CatalogItemRepository extends ReactiveMongoRepository<CatalogItem, UUID>
{

    public Mono<CatalogItem> findByItemNameAndItemTypeId(String itemName, UUID itemTypeId);
}
