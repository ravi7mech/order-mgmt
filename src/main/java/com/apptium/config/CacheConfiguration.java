package com.apptium.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.apptium.domain.OrdProductOrder.class.getName());
            createCache(cm, com.apptium.domain.OrdProductOrder.class.getName() + ".ordCharacteristics");
            createCache(cm, com.apptium.domain.OrdProductOrder.class.getName() + ".ordOrderItems");
            createCache(cm, com.apptium.domain.OrdProductOrder.class.getName() + ".ordPaymentRefs");
            createCache(cm, com.apptium.domain.OrdProductOrder.class.getName() + ".ordReasons");
            createCache(cm, com.apptium.domain.OrdProductOrder.class.getName() + ".ordContracts");
            createCache(cm, com.apptium.domain.OrdProductOrder.class.getName() + ".ordFulfillments");
            createCache(cm, com.apptium.domain.OrdProductOrder.class.getName() + ".ordAcquisitions");
            createCache(cm, com.apptium.domain.OrdBillingAccountRef.class.getName());
            createCache(cm, com.apptium.domain.OrdContactDetails.class.getName());
            createCache(cm, com.apptium.domain.OrdCharacteristics.class.getName());
            createCache(cm, com.apptium.domain.OrdNote.class.getName());
            createCache(cm, com.apptium.domain.OrdChannel.class.getName());
            createCache(cm, com.apptium.domain.OrdOrderPrice.class.getName());
            createCache(cm, com.apptium.domain.OrdOrderPrice.class.getName() + ".ordPriceAlterations");
            createCache(cm, com.apptium.domain.OrdOrderItem.class.getName());
            createCache(cm, com.apptium.domain.OrdOrderItem.class.getName() + ".ordOrderItemChars");
            createCache(cm, com.apptium.domain.OrdOrderItemProvisioning.class.getName());
            createCache(cm, com.apptium.domain.OrdOrderItemProvisioning.class.getName() + ".ordProvisiongChars");
            createCache(cm, com.apptium.domain.OrdOrderItemChar.class.getName());
            createCache(cm, com.apptium.domain.OrdProvisiongChar.class.getName());
            createCache(cm, com.apptium.domain.OrdPaymentRef.class.getName());
            createCache(cm, com.apptium.domain.OrdPriceAlteration.class.getName());
            createCache(cm, com.apptium.domain.OrdReason.class.getName());
            createCache(cm, com.apptium.domain.OrdContract.class.getName());
            createCache(cm, com.apptium.domain.OrdContract.class.getName() + ".ordContractCharacteristics");
            createCache(cm, com.apptium.domain.OrdContractCharacteristics.class.getName());
            createCache(cm, com.apptium.domain.OrdFulfillment.class.getName());
            createCache(cm, com.apptium.domain.OrdFulfillment.class.getName() + ".ordFulfillmentChars");
            createCache(cm, com.apptium.domain.OrdFulfillmentChar.class.getName());
            createCache(cm, com.apptium.domain.OrdAcquisition.class.getName());
            createCache(cm, com.apptium.domain.OrdAcquisition.class.getName() + ".ordAcquisitionChars");
            createCache(cm, com.apptium.domain.OrdPriceAmount.class.getName());
            createCache(cm, com.apptium.domain.OrdProductOfferingRef.class.getName());
            createCache(cm, com.apptium.domain.OrdAcquisitionChar.class.getName());
            createCache(cm, com.apptium.domain.OrdOrderItemRelationship.class.getName());
            createCache(cm, com.apptium.domain.OrdProduct.class.getName());
            createCache(cm, com.apptium.domain.OrdProduct.class.getName() + ".ordPlaces");
            createCache(cm, com.apptium.domain.OrdPlace.class.getName());
            createCache(cm, com.apptium.domain.OrdProductCharacteristics.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
