package in.ac.bitspilani.wilp.scalableservices.assignment.furniturecatalogservice;

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
public class ItemType
{
    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();
    private String itemType;
}
