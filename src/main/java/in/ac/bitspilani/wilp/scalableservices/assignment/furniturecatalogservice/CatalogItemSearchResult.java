package in.ac.bitspilani.wilp.scalableservices.assignment.furniturecatalogservice;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogItemSearchResult
{
    private CatalogItem catalogItem;
    private Map<String, Integer> colorWiseStock;
}
