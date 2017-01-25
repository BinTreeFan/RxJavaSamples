package com.fan.rx.samples;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.observables.ConnectableObservable;

public class ConnectingSample {
	
	public static void connect( ) {
		ConnectableObservable<Long> connectableObservable = Observable.interval( 1 , TimeUnit.SECONDS ).take( 6 )
				.observeOn( CommonFunction.getSingleScheduler( ) ).publish( );
		Action1< Long > action1 = new Action1< Long >( ) {

			@Override
			public void call( Long t ) {
				// TODO Auto-generated method stub
				System.out.println( "Next: " + t );
			}
		};	
		connectableObservable.subscribe( action1 );
		try {
			Thread.sleep( 3000 );
		}
		catch ( InterruptedException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connectableObservable.connect( );
	}
	
	public static void main( String[ ] args ) {
		// TODO Auto-generated method stub
		connect( );
	}
	
}
