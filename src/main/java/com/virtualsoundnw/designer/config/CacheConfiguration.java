package com.virtualsoundnw.designer.config;

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

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
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
            createCache(cm, com.virtualsoundnw.designer.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.virtualsoundnw.designer.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.virtualsoundnw.designer.domain.User.class.getName());
            createCache(cm, com.virtualsoundnw.designer.domain.Authority.class.getName());
            createCache(cm, com.virtualsoundnw.designer.domain.User.class.getName() + ".authorities");
            createCache(cm, com.virtualsoundnw.designer.domain.Ships.class.getName());
            createCache(cm, com.virtualsoundnw.designer.domain.Ships.class.getName() + ".fittings");
            createCache(cm, com.virtualsoundnw.designer.domain.Ships.class.getName() + ".weapons");
            createCache(cm, com.virtualsoundnw.designer.domain.Ships.class.getName() + ".defenses");
            createCache(cm, com.virtualsoundnw.designer.domain.Ships.class.getName() + ".cargoes");
            createCache(cm, com.virtualsoundnw.designer.domain.Ships.class.getName() + ".vehicles");
            createCache(cm, com.virtualsoundnw.designer.domain.Ships.class.getName() + ".engines");
            createCache(cm, com.virtualsoundnw.designer.domain.Fittings.class.getName());
            createCache(cm, com.virtualsoundnw.designer.domain.Weapons.class.getName());
            createCache(cm, com.virtualsoundnw.designer.domain.Defenses.class.getName());
            createCache(cm, com.virtualsoundnw.designer.domain.Cargoes.class.getName());
            createCache(cm, com.virtualsoundnw.designer.domain.Vehicles.class.getName());
            createCache(cm, com.virtualsoundnw.designer.domain.Engines.class.getName());
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
