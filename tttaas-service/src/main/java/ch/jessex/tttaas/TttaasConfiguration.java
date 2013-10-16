package ch.jessex.tttaas;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.db.DatabaseConfiguration;

/**
 * The base configuration for the service.
 *
 * @author jessex
 * @since 0.0.1
 */
public class TttaasConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    private DatabaseConfiguration database = new DatabaseConfiguration();

    public DatabaseConfiguration getDatabaseConfiguration() {
        return database;
    }
}
