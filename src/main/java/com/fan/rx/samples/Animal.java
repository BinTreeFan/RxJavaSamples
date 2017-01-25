
package com.fan.rx.samples;


import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;


public class Animal {
	Subscriber<Long> subscriber = new Subscriber<Long>( ) {
		@Override
		public void onCompleted( ) {
			
		}
		
		@Override
		public void onError( Throwable e ) {
			
		}
		
		@Override
		public void onNext( Long o ) {
			System.out.println( "animal eat" );
		}
	};
	
	public Animal( ) {
		System.out.println( "create animal" );
		Observable.interval( 1000 , TimeUnit.MILLISECONDS ).subscribe( subscriber );
	}
	
	public void relase( ) {
		System.out.println( "animal released" );
		subscriber.unsubscribe( );
	}
}
