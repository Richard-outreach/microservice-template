package org.outreach;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;
import lombok.*;

@DynamoDBTable(tableName = "Beers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beer {
    @DynamoDBHashKey
    private String id;
    private String name;
    @DynamoDBVersionAttribute
    private Long version;
}
