package com.travelmanagement.scheduler;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppStartupListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println(" Application started, initializing BookingScheduler...");
		BookingScheduler.getInstance().startAutoCancelTask();
		;
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println(" Application stopping, shutting down BookingScheduler...");
		BookingScheduler.getInstance().shutdown();
	}
}
