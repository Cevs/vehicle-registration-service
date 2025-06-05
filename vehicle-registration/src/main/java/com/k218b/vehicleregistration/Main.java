package com.k218b.vehicleregistration;

import com.k218b.vehicleregistration.config.AppConfig;
import com.k218b.vehicleregistration.dao.UserDao;
import com.k218b.vehicleregistration.dao.VehicleRegistrationDao;
import com.k218b.vehicleregistration.dao.impl.DefaultUserDao;
import com.k218b.vehicleregistration.dao.impl.DefaultVehicleRegistrationDao;
import com.k218b.vehicleregistration.filter.BasicAuthFilter;
import com.k218b.vehicleregistration.filter.CorsFilter;
import com.k218b.vehicleregistration.handler.AccountHandler;
import com.k218b.vehicleregistration.handler.StatisticsHandler;
import com.k218b.vehicleregistration.handler.UserHandler;
import com.k218b.vehicleregistration.handler.VehicleRegistrationHandler;
import com.k218b.vehicleregistration.service.UserService;
import com.k218b.vehicleregistration.service.VehicleRegistrationService;
import com.k218b.vehicleregistration.service.impl.DefaultUserService;
import com.k218b.vehicleregistration.service.impl.DefaultVehicleRegistrationService;
import com.k218b.vehicleregistration.util.I18nUtil;
import com.k218b.vehicleregistration.util.JDBCUtil;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
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
		initializeDatabase();
	}

	private static void initializeServer() throws IOException {
		final HttpServer server = HttpServer.create(new InetSocketAddress(AppConfig.getInt("app.server.port")), 0);

		final UserDao userDao = new DefaultUserDao();
		final UserService userService = new DefaultUserService(userDao);
		final VehicleRegistrationDao vehicleDao = new DefaultVehicleRegistrationDao();
		final VehicleRegistrationService vehicleRegistrationService = new DefaultVehicleRegistrationService(vehicleDao);
		final BasicAuthFilter basicAuthFilter = new BasicAuthFilter(userService);
		final CorsFilter corsFilter = new CorsFilter();

		final AccountHandler accountHandler = new AccountHandler(userService);
		final VehicleRegistrationHandler vehicleHandler = new VehicleRegistrationHandler(vehicleRegistrationService);
		final StatisticsHandler statisticsHandler = new StatisticsHandler(vehicleRegistrationService);
		final UserHandler userHandler = new UserHandler(vehicleRegistrationService);

		final List<Filter> generalFilters = List.of(corsFilter, basicAuthFilter);

		server.setExecutor(createThreadPoolExecutor());
		server.createContext("/account", accountHandler)
			  .getFilters()
			  .add(corsFilter);
		server.createContext("/register", vehicleHandler)
			  .getFilters()
			  .addAll(generalFilters);
		server.createContext("/statistics/accountID", statisticsHandler)
			  .getFilters()
			  .addAll(generalFilters);
		server.createContext("/registration/registrationCode", userHandler)
			  .getFilters()
			  .addAll(generalFilters);

		server.start();
		LOG.log(Level.INFO,"Server started on http://localhost:8080");
	}

	private static ThreadPoolExecutor createThreadPoolExecutor() {
		return new ThreadPoolExecutor(AppConfig.getInt("app.server.core.pool.size"),
									  AppConfig.getInt("app.server.max.pool.size"),
									  AppConfig.getInt("app.server.keep.alive"), TimeUnit.SECONDS,
									  new ArrayBlockingQueue<>(AppConfig.getInt("app.server.queue.size")),
									  Executors.defaultThreadFactory(),
									  new ThreadPoolExecutor.AbortPolicy());
	}

	private static void initializeDatabase() {
		JDBCUtil.initDatabase();
		LOG.info(() -> I18nUtil.getMessage(Locale.getDefault(), "db.initialized"));

		// Register a shutdown hook to cleanly shut down Derby
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				DriverManager.getConnection("jdbc:derby:;shutdown=true");
			} catch (SQLException _) {
				// Derby throws SQLState 08006 or XJ015 on successful shutdown — ignore
			}
		}));
	}
}