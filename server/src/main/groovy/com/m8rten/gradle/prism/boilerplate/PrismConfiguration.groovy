package com.m8rten.gradle.prism.boilerplate

import com.bazaarvoice.dropwizard.assets.AssetsBundleConfiguration
import com.bazaarvoice.dropwizard.assets.AssetsConfiguration
import com.fasterxml.jackson.annotation.JsonProperty
import com.yammer.dropwizard.config.Configuration
import org.hibernate.validator.constraints.NotEmpty

import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

public class PrismConfiguration extends Configuration implements AssetsBundleConfiguration{
    @NotEmpty
    @JsonProperty
    private String template;

    @NotEmpty
    @JsonProperty
    private String defaultName = "Stranger";

    public String getTemplate() {
        return template;
    }

    public String getDefaultName() {
        return defaultName;
    }

    @JsonProperty @NotEmpty
    public String mongohost = "localhost";

    @Min(1L)
    @Max(65535L)
    @JsonProperty
    public int mongoport = 27017;

    @JsonProperty @NotEmpty
    public String mongodb = "prismdb";


    @Valid
    @NotNull
    @JsonProperty
    private final AssetsConfiguration assets = new AssetsConfiguration()

    @Override
    AssetsConfiguration getAssetsConfiguration() {
        return assets
    }
}