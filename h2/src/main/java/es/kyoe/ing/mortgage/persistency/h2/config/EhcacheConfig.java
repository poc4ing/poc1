package es.kyoe.ing.mortgage.persistency.h2.config;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Caching;
import java.time.Duration;

@Configuration
@EnableCaching
public class EhcacheConfig {

    public static final String RATES_PAGE_CACHE = "ratesPageCache";
    public static final String RATE_BY_MATURITY_CACHE = "rateByMaturityCache";

    @Bean
    public CacheManager cacheManager() {
        EhcacheCachingProvider provider = (EhcacheCachingProvider) Caching.getCachingProvider(
                EhcacheCachingProvider.class.getName()
        );
        javax.cache.CacheManager jCacheManager = provider.getCacheManager();

        registerCache(jCacheManager, RATES_PAGE_CACHE, 200, Duration.ofMinutes(10));
        registerCache(jCacheManager, RATE_BY_MATURITY_CACHE, 100, Duration.ofMinutes(10));

        return new JCacheCacheManager(jCacheManager);
    }

    private void registerCache(
            javax.cache.CacheManager jCacheManager,
            String cacheName,
            long heapEntries,
            Duration ttl
    ) {
        if (jCacheManager.getCache(cacheName) != null) {
            return;
        }
        CacheConfiguration<Object, Object> ehcacheConfig = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(
                        Object.class,
                        Object.class,
                        ResourcePoolsBuilder.heap(heapEntries)
                )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(ttl))
                .build();

        javax.cache.configuration.Configuration<Object, Object> jcacheConfig =
                Eh107Configuration.fromEhcacheCacheConfiguration(ehcacheConfig);

        jCacheManager.createCache(cacheName, jcacheConfig);
    }
}

