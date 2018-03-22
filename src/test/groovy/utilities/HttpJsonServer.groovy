package utilities

import org.eclipse.jetty.http.HttpVersion
import org.eclipse.jetty.security.ConstraintMapping
import org.eclipse.jetty.security.ConstraintSecurityHandler
import org.eclipse.jetty.security.HashLoginService
import org.eclipse.jetty.security.LoginService
import org.eclipse.jetty.security.authentication.BasicAuthenticator
import org.eclipse.jetty.server.Connector
import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.HttpConfiguration
import org.eclipse.jetty.server.HttpConnectionFactory
import org.eclipse.jetty.server.SecureRequestCustomizer
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.server.SslConnectionFactory
import org.eclipse.jetty.server.handler.DefaultHandler
import org.eclipse.jetty.server.handler.HandlerList
import org.eclipse.jetty.server.handler.ResourceHandler
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jetty.util.security.Constraint
import org.eclipse.jetty.util.ssl.SslContextFactory

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import static utilities.ResourcePath.resourcePath

class HttpJsonServer {

    public static Server startServer(String base, Integer port) throws Exception {

        Server server = new Server(port)

        ServletContextHandler ctx = new ServletContextHandler()
        ctx.setContextPath("/")

        DefaultServlet defaultServlet = new MyDefaultServlet()
        ServletHolder holderPwd = new ServletHolder("default", defaultServlet)
        holderPwd.setInitParameter("resourceBase", base)

        ctx.addServlet(holderPwd, "/*")

        server.setHandler(ctx)

        server.start()

        return server
    }

    private static class MyDefaultServlet extends DefaultServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setHeader("Content-Type", "application/json")
            super.doGet(request, response)
        }
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
