package it.italian.coders.model.group;

import it.italian.coders.model.BaseDocument;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
@Document
public class Group extends BaseDocument {

    @Id
    private String id;
    private List<String> users;
    private String name;

    @Builder(builderMethodName = "newBuilder")
    public Group(Long version, Date created, Date updated, String id, List<String> users, String name) {
        super(version, created, updated);
        this.id = id;
        this.users = users;
        this.name = name;
    }
}
