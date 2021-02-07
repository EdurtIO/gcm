package io.edurt.gcm.netty.router;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

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
        return ROUTERS.get(path);
    }

    public static final HashMap<String, Router> getRouters()
    {
        return ROUTERS;
    }
}
