package com.fan.rx.samples;

import rx.Observer;
import rx.subjects.PublishSubject;

public class PublicResource {
	
	private int publicRes;
	
	private final PublishSubject< Integer > subject = PublishSubject.create( );
	
	private void initAccessSubject( ) {
		subject.subscribe( new Observer<Integer>( ) {

			@Override
			public void onCompleted( ) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError( Throwable e ) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onNext( Integer t ) {
				// TODO Auto-generated method stub
				
			}
		} );
	}
}
