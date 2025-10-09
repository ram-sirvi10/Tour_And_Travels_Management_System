package com.travelmanagement.scheduler;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppStartupListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println(" Application started, initializing BookingScheduler...");
		BookingScheduler scheduler = BookingScheduler.getInstance();
		scheduler.startAutoCancelTask();
		scheduler.startAutoCompleteTask();

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println(" Application stopping, shutting down BookingScheduler...");
		BookingScheduler.getInstance().shutdown();
	}
}
