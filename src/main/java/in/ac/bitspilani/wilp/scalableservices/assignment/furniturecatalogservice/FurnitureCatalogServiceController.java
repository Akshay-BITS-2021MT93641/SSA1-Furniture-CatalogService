package in.ac.bitspilani.wilp.scalableservices.assignment.furniturecatalogservice;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.ac.bitspilani.wilp.scalableservices.assignment.furniturecatalogservice.dao.FurnitureInventoryDao;
import in.ac.bitspilani.wilp.scalableservices.assignment.furniturecatalogservice.dao.CatalogItemRepository;
import in.ac.bitspilani.wilp.scalableservices.assignment.furniturecatalogservice.dao.ItemTypeRepository;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
public class FurnitureCatalogServiceController
{
    
    @Autowired
    private ItemTypeRepository itemTypeRepository;
    
    @Autowired
    private CatalogItemRepository catalogItemRepository;
    
    @Autowired
    private FurnitureInventoryDao catalogInventoryDao;

    @PutMapping("/addItemType")
    public Mono<ItemType> addItemType(@RequestParam String itemType)
    {
        return
                findItemType(itemType)
                .hasElement()
                .flatMap(exists->{
                    
                    if(!exists) {
                        ItemType itemTypeNew = ItemType.builder().itemType(itemType).build();
                        return itemTypeRepository.save(itemTypeNew);
                    } else {
                        Mono.error(new IllegalArgumentException("Item type already exists"));
                    }
                    return null;
                });
    }
    
    @GetMapping("/findItemType")
    public Mono<ItemType> findItemType(@RequestParam String itemType) 
    {
        return itemTypeRepository.findByItemType(itemType);
    }
    
    @PutMapping("/addCatalogItem")
    public Mono<CatalogItem> addCatalogItem(@RequestBody CatalogItem catalogItem) 
    {
        return
                catalogItemRepository.findByItemNameAndItemTypeId(catalogItem.getItemName(), catalogItem.getItemTypeId())
                .hasElement()
                .flatMap(exists->{
                    
                    if(!exists) {
                        return catalogItemRepository.save(catalogItem);
                    } else {
                        Mono.error(new IllegalArgumentException("Catalog item already exists"));
                    }
                    return null;
                });
    }
    
    @GetMapping("/findCatalogItem")
    public Flux<CatalogItemSearchResult> findCatalogItem(
            @Nullable @RequestParam String itemName
            , @Nullable @RequestParam String itemType
            , @Nullable @RequestParam String[] colors)
    {
        return
                findItemType(itemType)
                .switchIfEmpty(Mono.empty())
                .flux()
                .flatMap(itemTypeDb->{
                    
                    CatalogItem example = CatalogItem.builder()
                            .itemId(null) //overwrite default for example search
                            .itemName(itemName)
                            .itemTypeId(Objects.nonNull(itemTypeDb)?itemTypeDb.getId():null)
                            .colors(ArrayUtils.isNotEmpty(colors)?Arrays.asList(colors):null)
                            .build();
                    
                    return catalogItemRepository.findAll(Example.of(example));
                })
                .flatMap(item->catalogInventoryDao.getStock(item.getItemId())
                                        .onErrorResume(t->{
                                            log.warn("Unable to get stock.", t);
                                            return Mono.just(Collections.emptyMap());
                                        })
                                        .map(cs->CatalogItemSearchResult.builder().catalogItem(item).colorWiseStock(cs).build()));
    }
    
    @PostMapping("/updateCatalogItem") 
    public Mono<CatalogItem> updateCatalogItem(@RequestBody CatalogItem catalogItem)
    {
        return
                catalogItemRepository.findByItemNameAndItemTypeId(catalogItem.getItemName(), catalogItem.getItemTypeId())
                .switchIfEmpty(Mono.error(()->new IllegalArgumentException("Catalog item does not exists")))
                .flatMap(exists->catalogItemRepository.save(catalogItem.toBuilder().itemId(exists.getItemId()).build()));
    }
    
    @DeleteMapping("/deleteCatalogItem/{catalogItemId}")
    public Mono<CatalogItem> deleteCatalogItem(@PathVariable UUID catalogItemId)
    {
        return
                catalogItemRepository.findById(catalogItemId)
                .switchIfEmpty(Mono.error(()->new IllegalArgumentException("Catalog item does not exists")))
                .doOnSuccess(item->catalogItemRepository.deleteById(catalogItemId));        
    }
    
    @GetMapping("/getCatalogItem/{catalogItemId}")
    public Mono<CatalogItem> getCatalogItem(@PathVariable UUID catalogItemId)
    {
        return catalogItemRepository.findById(catalogItemId);        
    }
    
    @GetMapping("/getCatalogItemWithStock/{catalogItemId}")
    public Mono<CatalogItemSearchResult> getCatalogItemWithStock(@PathVariable UUID catalogItemId)
    {
        return catalogItemRepository.findById(catalogItemId)
                        .flatMap(item->catalogInventoryDao.getStock(item.getItemId())
                                .onErrorResume(t->{
                                    log.warn("Unable to get stock.", t);
                                    return Mono.just(Collections.emptyMap());
                                })
                                .map(cs->CatalogItemSearchResult.builder().catalogItem(item).colorWiseStock(cs).build()));     
    }
}
