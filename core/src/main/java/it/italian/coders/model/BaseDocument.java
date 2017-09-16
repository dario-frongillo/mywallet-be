package it.italian.coders.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import java.util.Date;

/**
 * Created by Dario on 7/7/2017.
 */

public class BaseDocument {
    @Version
    private Long version;
    @CreatedDate
    private Date created;
    @LastModifiedDate
    private Date updated;


    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }



}
