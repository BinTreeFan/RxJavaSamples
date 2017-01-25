package com.fan.rx.samples;

import java.util.concurrent.Executors;

import rx.Scheduler;
import rx.schedulers.Schedulers;

public class CommonFunction {
	public static Scheduler getNamedScheduler( String name ) {
		return Schedulers.from( Executors.newCachedThreadPool( r -> new Thread( r , name ) ) );
	}
	
	public static Scheduler getSingleScheduler( ) {
		return Schedulers.from( Executors.newSingleThreadExecutor( ) );
	}
}
