package io.edurt.gcm.netty.router;

import io.edurt.gcm.common.utils.ObjectUtils;
import io.edurt.gcm.netty.handler.HttpPathHandler;
import io.edurt.gcm.netty.type.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Routers
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Routers.class);
    /**
     * Buffering all routes in the server
     */
    private static final Set<Router> ROUTERS = new HashSet<>(256);

    private Routers()
    {}

    /**
     * Add route buffer information
     *
     * @param router Basic routing information
     */
    public static final void setRouter(final Router router)
    {
        if (ObjectUtils.isEmpty(router)) {
            LOGGER.warn("Load router path or router must not null");
            return;
        }
        ROUTERS.add(router);
    }

    public static final Router getRouter(String url, RequestMethod requestMethod)
    {
        // When the route is not extracted, the path matching parameter pattern extraction is used
        Optional<Router> routerOptional = Routers.ROUTERS.stream()
                .filter(router1 -> router1.getRequestMethod().equals(requestMethod) && router1.getUrl().equals(url))
                .findAny();
        if (routerOptional.isPresent()) {
            return routerOptional.get();
        }
        // Filter PathVariable
        Optional<Router> pathVariableOptional = Routers.ROUTERS.stream()
                .filter(router1 -> HttpPathHandler.verify(url, router1.getUrl()))
                .findAny();
        if (pathVariableOptional.isPresent()) {
            return pathVariableOptional.get();
        }
        return null;
    }

    public static final Set<Router> getRouters()
    {
        return ROUTERS;
    }
}
