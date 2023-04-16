package in.ac.bitspilani.wilp.scalableservices.assignment.furniturecatalogservice;

import java.net.URL;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Document
public class CatalogItem
{
    @Id
    @Builder.Default
    private UUID itemId = UUID.randomUUID();
    private String itemName;
    private UUID itemTypeId;
    private String[] colors;
    private URL photoS3Url;
    private Map<String, Object> itemDetails;
}
