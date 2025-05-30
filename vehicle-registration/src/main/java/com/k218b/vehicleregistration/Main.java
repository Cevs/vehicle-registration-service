package com.k218b.vehicleregistration;

import com.k218b.vehicleregistration.handler.AccountHandler;
import com.k218b.vehicleregistration.service.impl.DefaultUserService;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

	private static final Logger LOG = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) throws IOException {
		initializeServer();
	}

	private static void initializeServer() throws IOException {
		final HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
		server.setExecutor(createThreadPoolExecutor());
		server.createContext("/account", new AccountHandler(new DefaultUserService()));
		server.start();
		LOG.log(Level.INFO,"Server started on http://localhost:8080");
	}

	private static ThreadPoolExecutor createThreadPoolExecutor() {
		return new ThreadPoolExecutor(8,
									  16,
									  60, TimeUnit.SECONDS,
									  new ArrayBlockingQueue<>(50),
									  Executors.defaultThreadFactory(),
									  new ThreadPoolExecutor.AbortPolicy());
	}

}