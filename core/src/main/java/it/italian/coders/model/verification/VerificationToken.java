package it.italian.coders.model.verification;

import it.italian.coders.model.BaseDocument;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class VerificationToken extends BaseDocument {
    @Id
    private String id;

    private VerificationTypeEnum type;

    private String username;

    private Date expiryDate;

    private VerificationStatusEnum status;

    @Builder(builderMethodName = "newBuilder")
    public VerificationToken(Long version, Date created, Date updated, String id, VerificationTypeEnum type, String username, Date expiryDate, VerificationStatusEnum status) {
        super(version, created, updated);
        this.id = id;
        this.type = type;
        this.username = username;
        this.expiryDate = expiryDate;
        this.status = status;
    }
}
