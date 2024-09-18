package com.hx.vertx.file.monitor.properties;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Context;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class ApplicationProper {

    private Properties properties;
    private Map<String,Properties> propertiesMap;

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Map<String, Properties> getPropertiesMap() {
        return propertiesMap;
    }

    public void setPropertiesMap(Map<String, Properties> propertiesMap) {
        this.propertiesMap = propertiesMap;
    }

    /**
     * 加载主配置文件
     * @param vertx
     * @return
     */
    public void readYaml(Vertx vertx) {
        ConfigStoreOptions store = new ConfigStoreOptions()
                .setType(DefualtProperType.TYPE)
                .setFormat(DefualtProperType.FORMAT)
                .setOptional(DefualtProperType.OPTIONAL)
                .setConfig(new JsonObject().put(DefualtProperType.CLASS_PATH, DefualtProperType.DEFAULT_NAME));
        ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(store));
        retriever.getConfig(prop -> {
            if (prop.succeeded()) {
                this.properties = prop.result().mapTo(Properties.class);
            } else {
                // 加载失败
            }
        });

        if (Optional.ofNullable(this.propertiesMap).isPresent()) {
            this.propertiesMap = new HashMap<>();
        }
        this.propertiesMap.put(DefualtProperType.DEFAULT_NAME, this.properties);
        // 按照循序加载其他配置文件
        Optional.ofNullable(this.properties).ifPresent(p -> {
            String path = p.getProperty(DefualtProperType.CLASS_PROPERTIES);
        });
    }
}
