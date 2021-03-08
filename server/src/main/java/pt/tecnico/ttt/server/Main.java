package pt.tecnico.ttt.server;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class Main {
	
	/** Set flag to true to print debug messages. The flag can be set using the -Ddebug command line option. */
	private static final boolean DEBUG_FLAG = (System.getProperty("debug") != null);

	/** Helper method to print debug messages. */
	private static void debug(String debugMessage) {
		if (DEBUG_FLAG)
			System.err.println(debugMessage);
	}

	/**
	 * Main method.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		System.out.println(Main.class.getSimpleName());

		// receive and print arguments
		debug(String.format("Received %d arguments%n", args.length));
		for (int i = 0; i < args.length; i++) {
			debug(String.format("arg[%d] = %s%n", i, args[i]));
		}

		// check arguments
		if (args.length < 2) {
			System.out.println("Argument(s) missing!");
			System.out.printf("Usage: java %s host port%n", Main.class.getName());
			return;
		}
		final String host = args[0];
		final int port = Integer.parseInt(args[1]);
		final String target = host + ":" + port;

		/* Resources package name */
		final String resourcesPackage = "pt.tecnico.ttt.server";
		debug("Resources package: " + resourcesPackage);

		/* Base URI the Grizzly HTTP server will listen on. */ 
		final String baseURI = "http://" + target + "/ttt/";
		debug("Base URL: " + baseURI);

		/* URI for the Web Application Description Language document. */
		final String wadlURI = baseURI + "application.wadl";
		debug("WADL URL: " + wadlURI);

		final HttpServer server = startServer(resourcesPackage, baseURI);
		System.out.println("Jersey server started and awaiting requests");
		System.out.printf("Description available at %s%n", wadlURI);
		
		System.out.println("Hit enter to stop server...");
		System.in.read();

		server.shutdownNow();
	}

	/**
	 * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
	 * @return Grizzly HTTP server.
	 */
	public static HttpServer startServer(final String packageName, final String baseURI) {
		// create a resource config that scans for JAX-RS resources and providers
		// in example package
		final ResourceConfig rc = new ResourceConfig().packages(packageName);

		// create and start a new instance of grizzly http server
		// exposing the Jersey application at BASE_URI
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(baseURI), rc);
	}

}
