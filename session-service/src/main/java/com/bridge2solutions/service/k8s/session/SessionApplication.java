package com.bridge2solutions.service.k8s.session;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.kubernetes.HazelcastKubernetesDiscoveryStrategyFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class SessionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SessionApplication.class, args);
    }

    @Bean
    public HazelcastInstance hazelcastInstance() {

        HazelcastKubernetesDiscoveryStrategyFactory factory = new HazelcastKubernetesDiscoveryStrategyFactory();
        DiscoveryStrategyConfig discoveryConfig = new DiscoveryStrategyConfig(factory);
//        discoveryConfig.addProperty("namespace", "production");
//        discoveryConfig.addProperty("service-name", "session-service-lb");

        discoveryConfig.addProperty("service-dns", "session-service-hazelcast");
        discoveryConfig.addProperty("service-dns-timeout", 10);

//        discoveryConfig.addProperty("service-label-name", "app");
//        discoveryConfig.addProperty("service-label-value", "gateway-service");

        final Config config = new Config()
//            .setInstanceName("session-service-hazelcast")
            .setProperty("hazelcast.discovery.enabled", "true")
            .addMapConfig(
                new MapConfig()
                    .setName(Constants.SESSIONS_CACHE)
                    .setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE))
                    .setEvictionPolicy(EvictionPolicy.LRU)
                    .setTimeToLiveSeconds(2000));

        final NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.setPort(5701);
        networkConfig.setPortAutoIncrement(false);

        final JoinConfig joinConfig = networkConfig.getJoin();
        joinConfig.getMulticastConfig().setEnabled(false);
        joinConfig.getTcpIpConfig().setEnabled(false);
        joinConfig.getAwsConfig().setEnabled(false);
        joinConfig.getDiscoveryConfig().addDiscoveryStrategyConfig(discoveryConfig);

        return Hazelcast.newHazelcastInstance(config);
    }

}
