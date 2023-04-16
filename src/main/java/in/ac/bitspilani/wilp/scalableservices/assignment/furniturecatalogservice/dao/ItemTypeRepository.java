package in.ac.bitspilani.wilp.scalableservices.assignment.furniturecatalogservice.dao;

import java.util.UUID;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import in.ac.bitspilani.wilp.scalableservices.assignment.furniturecatalogservice.ItemType;
import reactor.core.publisher.Mono;

public interface ItemTypeRepository extends ReactiveMongoRepository<ItemType, UUID>
{
    public Mono<ItemType> findByItemType(String itemType);
}
