package com.fan.rx.samples;

import rx.Scheduler.Worker;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class SchedulerSample {
	
	private static void useWorker( ) {
		Worker worker = Schedulers.newThread( ).createWorker( );
		worker.schedule( new Action0( ) {
			
			@Override
			public void call( ) {
				// TODO Auto-generated method stub
				while ( true ) {
					System.out.println( "worker is running..." );
				}			
			}
		} );
		
		try {
			Thread.sleep( 3000 );
			worker.unsubscribe( );
		}
		catch ( InterruptedException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public static void main( String[ ] args ) {
		// TODO Auto-generated method stub
		useWorker( );
	}
	
}
