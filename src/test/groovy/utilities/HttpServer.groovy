package utilities

import org.eclipse.jetty.http.HttpVersion
import org.eclipse.jetty.security.ConstraintMapping
import org.eclipse.jetty.security.ConstraintSecurityHandler
import org.eclipse.jetty.security.HashLoginService
import org.eclipse.jetty.security.LoginService
import org.eclipse.jetty.security.authentication.BasicAuthenticator
import org.eclipse.jetty.server.*
import org.eclipse.jetty.server.handler.DefaultHandler
import org.eclipse.jetty.server.handler.HandlerList
import org.eclipse.jetty.server.handler.ResourceHandler
import org.eclipse.jetty.util.security.Constraint
import org.eclipse.jetty.util.ssl.SslContextFactory

class HttpServer {

    static startServer(base, port=8080) throws Exception {
        println(base)

        Server server = new Server(port as Integer)

        ResourceHandler resource_handler = new ResourceHandler()
        resource_handler.setDirectoriesListed(true)
        resource_handler.setResourceBase(base as String)

        HandlerList handlers = new HandlerList()
        handlers.setHandlers([resource_handler, new DefaultHandler()] as Handler[])
        server.setHandler(handlers)

        server.start()

        return server

    }

    static withServer(base, port=8080, cl) throws Exception {
        println(base)

        Server server = new Server(port as Integer)

        ResourceHandler resource_handler = new ResourceHandler()
        resource_handler.setDirectoriesListed(true)
        resource_handler.setResourceBase(base as String)

        HandlerList handlers = new HandlerList()
        handlers.setHandlers([resource_handler, new DefaultHandler()] as Handler[])
        server.setHandler(handlers)

        server.start()

        cl()

        server.stop()
    }
}
