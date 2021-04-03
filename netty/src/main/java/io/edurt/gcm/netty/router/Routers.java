package io.edurt.gcm.netty.router;

import io.edurt.gcm.common.utils.ObjectUtils;
import io.edurt.gcm.common.utils.StringUtils;
import io.edurt.gcm.netty.handler.HttpPathHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Routers
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Routers.class);
    /**
     * Buffering all routes in the server
     */
    private static final HashMap<String, Router> ROUTERS = new HashMap<>(256);

    private Routers()
    {}

    /**
     * Add route buffer information
     *
     * @param path Routing access address
     * @param router Basic routing information
     */
    public static final void setRouter(final String path, final Router router)
    {
        if (StringUtils.isEmpty(path) || ObjectUtils.isEmpty(router)) {
            LOGGER.warn("Load router path or router must not null");
            return;
        }
        ROUTERS.put(path, router);
    }

    public static final Router getRouter(String path)
    {
        Router router = ROUTERS.get(path);
        // When the route is not extracted, the path matching parameter pattern extraction is used
        if (ObjectUtils.isEmpty(router)) {
            Optional<Map.Entry<String, Router>> routerEntry = ROUTERS.entrySet()
                    .stream()
                    .filter(entry -> HttpPathHandler.verify(path, entry.getKey()))
                    .findFirst();
            if (routerEntry.isPresent()) {
                router = routerEntry.get().getValue();
            }
        }
        return router;
    }

    public static final HashMap<String, Router> getRouters()
    {
        return ROUTERS;
    }
}
