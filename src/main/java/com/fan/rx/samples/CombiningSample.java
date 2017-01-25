package com.fan.rx.samples;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import rx.Observable.OnSubscribe;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.Subscriber;

public class CombiningSample {
	
	private static Observable< Integer > createObserver( final int index ) {
		return Observable.create( new OnSubscribe< Integer >( ) {
			public void call(Subscriber<? super Integer> subscriber) {
				for ( int i = 0 ; i < 6 ; i++ ) {
					subscriber.onNext( i * index );
					try {
						Thread.sleep( 1000 );
					}
					catch ( Exception e ) {
						// TODO: handle exception
					}
				}
			};		
		} );
	}
	
	public static void combineLatest( ) {
		Observable< Integer > observable1 = createObserver( 1 );
		Observable< Integer > observable2 = createObserver( 2 );
		
		Observable.combineLatest( observable1 , observable2 , new Func2< Integer , Integer , Integer >( ) {

			@Override
			public Integer call( Integer t1 , Integer t2 ) {
				// TODO Auto-generated method stub
				return t1 + t2;
			}
		} ).subscribe( new Action1< Integer >( ) {

			@Override
			public void call( Integer t ) {
				// TODO Auto-generated method stub
				System.out.println( t + "");
			}
		} );
	}

	public static void join( ) {
		Observable< Long > observable1 = Observable.just( 1000L , 2000L , 300L );
		
		Observable< Long > observable2 = Observable.just( 1000L , 2000L , 300L );
		
		observable1.join( observable2 , new Func1< Long , Observable< Long > >( ) {
			public rx.Observable< Long > call( Long t ) {
				return Observable.just( t ).delay( 600 , TimeUnit.MILLISECONDS );
			};
		} , new Func1< Long , Observable< Long > >( ) {
			public rx.Observable< Long > call( Long t ) {
				return Observable.just( t ).delay( 600 , TimeUnit.MILLISECONDS );
			};
		} , new Func2< Long , Long , Long >( ) {
			public Long call( Long t1 , Long t2 ) {
				return t1 + t2;
			};
		} ).subscribe( new Subscriber< Long >( ) {
			
			@Override
			public void onCompleted( ) {
				// TODO Auto-generated method stub
				System.out.println( "Sequence complete." );
			}
			
			@Override
			public void onError( Throwable e ) {
				// TODO Auto-generated method stub
				System.err.println("Error: " + e.getMessage());
			}
			
			@Override
			public void onNext( Long t ) {
				// TODO Auto-generated method stub
				System.out.println( "Next: " + t );
			}
		} );
	}
	
	public static void groupJoin( ) {
		Observable< Long > observable1 = Observable.just( 1000L , 2000L , 300L );
		
		Observable< Long > observable2 = Observable.just( 1000L , 2000L , 300L );
		
		observable1.groupJoin( observable2 , new Func1< Long , Observable< Long > >( ) {
			public rx.Observable< Long > call( Long t ) {
				return Observable.just( t ).delay( 600 , TimeUnit.MILLISECONDS );
			};
		} , new Func1< Long , Observable< Long > >( ) {
			public rx.Observable< Long > call( Long t ) {
				return Observable.just( t ).delay( 600 , TimeUnit.MILLISECONDS );
			};
		} , new Func2< Long , Observable< Long > , Observable< Long > >( ) {
			@Override
			public Observable< Long > call( Long t1 , Observable< Long > observable ) {
				// TODO Auto-generated method stub
				return observable.map( new Func1< Long , Long >( ) {
					public Long call( Long t ) {
						return t1 + t;
					};
				} );
			}
		} ).subscribe( new Subscriber< Observable< Long > >( ) {
			
			@Override
			public void onCompleted( ) {
				// TODO Auto-generated method stub
				System.out.println( "Sequence complete." );
			}
			
			@Override
			public void onError( Throwable e ) {
				// TODO Auto-generated method stub
				System.err.println( "Error: " + e.getMessage( ) );
			}
			
			@Override
			public void onNext( Observable< Long > observable ) {
				// TODO Auto-generated method stub
				observable.subscribe( new Subscriber< Long >( ) {
					
					@Override
					public void onCompleted( ) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onError( Throwable e ) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onNext( Long t ) {
						// TODO Auto-generated method stub
						System.out.println( "Next: " + t );
					}
				} );
			}
		} );
	}
	
	public static void merge( ) {
		Observable< Integer > odds = Observable.just( 1 , 3 , 5 ).subscribeOn( Schedulers.immediate( ) );
		Observable< Integer > evens = Observable.just( 2 , 4 , 6 );
		Observable.merge( odds , evens ).subscribe( new Subscriber<Integer>( ) {

			@Override
			public void onCompleted( ) {
				// TODO Auto-generated method stub
				System.out.println("Sequence complete.");
			}

			@Override
			public void onError( Throwable e ) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onNext( Integer item ) {
				// TODO Auto-generated method stub
				System.out.println("Next: " + item);
			}
		} );
	}
	
	public static void mergeDelayError( ) {
		Observable< Long > errorObservable = Observable.error( new Exception(
				"error happened!" ) );
		
		Long[ ] array = new Long[ 5 ];
		for ( int i = 0 ; i < array.length ; i++ ) {
			array[ i ] = i * 1000L;
		}
		
		Observable< Long > observable1 = Observable
				.interval( 1000 , TimeUnit.MILLISECONDS , Schedulers.immediate( ) )
				.map( new Func1< Long , Long >( ) {
					
					@Override
					public Long call( Long t ) {
						// TODO Auto-generated method stub
						return t * 5;
					}
				} ).take( 3 ).mergeWith( errorObservable );
		
		Observable< Long > observable2 = Observable
				.interval( 500 , 1000 , TimeUnit.MILLISECONDS , Schedulers.immediate( ) )
				.map( new Func1< Long , Long >( ) {
					@Override
					public Long call( Long aLong ) {
						return aLong * 10;
					}
				} ).take( 5 );
		
		Observable.mergeDelayError( observable1 , observable2 ).subscribe(
				new Subscriber< Long >( ) {
					
					@Override
					public void onCompleted( ) {
						// TODO Auto-generated method stub
						System.out.println( "Sequence complete." );
					}
					
					@Override
					public void onError( Throwable e ) {
						// TODO Auto-generated method stub
						System.err.println( "Error: " + e.getMessage( ) );
					}
					
					@Override
					public void onNext( Long t ) {
						// TODO Auto-generated method stub
						System.out.println( "Next:" + t );
					}
					
				} );
	}
	
	public static void startWith( ) {
		Observable.just( 10 , 20 , 30 ).startWith( 2 , 3 , 4 )
				.subscribe( new Subscriber< Integer >( ) {
					
					@Override
					public void onCompleted( ) {
						// TODO Auto-generated method stub
						System.out.println("Sequence complete.");
					}
					
					@Override
					public void onError( Throwable e ) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onNext( Integer t ) {
						// TODO Auto-generated method stub
						System.out.println("Next:" + t);
					}
				} );
	}
	
	public static void switchOnNext( ) {
		Observable< Observable< Long > > observable = Observable
				.interval( 0 , 500 , TimeUnit.MILLISECONDS , Schedulers.immediate( ) )
				.map( new Func1< Long , Observable< Long > >( ) {
					public Observable< Long > call( Long t ) {
						return Observable.interval( 0 , 200 , TimeUnit.MILLISECONDS , Schedulers.immediate( ) )
								.map( new Func1< Long , Long >( ) {
									
									@Override
									public Long call( Long t ) {
										// TODO Auto-generated method stub
										return t * 10;
									}
								} ).take( 5 );
					};
				} ).take( 2 );
		
		Observable.switchOnNext( observable )
		.observeOn( Schedulers.immediate( ) )
		.subscribe( new Subscriber< Long >( ) {
			
			@Override
			public void onCompleted( ) {
				// TODO Auto-generated method stub
				System.out.println( "Sequence complete." );
			}
			
			@Override
			public void onError( Throwable e ) {
				// TODO Auto-generated method stub
				System.err.println( "Error: " + e.getMessage( ) );
			}
			
			@Override
			public void onNext( Long t ) {
				// TODO Auto-generated method stub
				System.out.println( "Next:" + t );
			}
		} );
	}
	
	public static void zip( ) {
		Observable< Integer > observable1 = Observable.just( 10 , 20 , 30 );
		Observable< Integer > observable2 = Observable.just( 4 , 8 , 12 , 16 );
		
		Observable.zip( observable1 , observable2 ,
				new Func2< Integer , Integer , Integer >( ) {
					
					@Override
					public Integer call( Integer t1 , Integer t2 ) {
						// TODO Auto-generated method stub
						return t1 + t2;
					}
				} ).subscribe( new Subscriber< Integer >( ) {
			
			@Override
			public void onCompleted( ) {
				// TODO Auto-generated method stub
				 System.out.println("Sequence complete.");
			}
			
			@Override
			public void onError( Throwable e ) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onNext( Integer t ) {
				// TODO Auto-generated method stub
				 System.out.println("Next:" + t);
			}
		} );
		
	}
	
	public static void zipWith( ) {
		Observable< Integer > observable1 = Observable.just( 10 , 20 , 30 );
		Observable< Integer > observable2 = Observable.just( 4 , 8 , 12 , 16 );

		observable1.zipWith( observable2 ,
				new Func2< Integer , Integer , Integer >( ) {
					
					@Override
					public Integer call( Integer t1 , Integer t2 ) {
						// TODO Auto-generated method stub
						return t1 + t2;
					}
				} ).subscribe( new Subscriber< Integer >( ) {
					
					@Override
					public void onCompleted( ) {
						// TODO Auto-generated method stub
						 System.out.println("Sequence complete.");
					}
					
					@Override
					public void onError( Throwable e ) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onNext( Integer t ) {
						// TODO Auto-generated method stub
						 System.out.println("Next:" + t);
					}
				} );
	}
	
	public static void main( String[ ] args ) {
		// TODO Auto-generated method stub
		//combineLatest( );
		//join( );
		//groupJoin( );
		//merge( );
		//mergeDelayError( );
		//startWith( );
		//switchOnNext( );
		zip( );
	}
	
}
