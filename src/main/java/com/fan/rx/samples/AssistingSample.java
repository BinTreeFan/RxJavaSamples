
package com.fan.rx.samples;


import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import rx.Notification;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.schedulers.TimeInterval;
import rx.schedulers.Timestamped;


public class AssistingSample {
	
	// 让原始Observable在发射每项数据之前都暂停一段指定的时间段
	public static void delay( ) {
		Observable< Integer > errorObservable = Observable.error( new Exception( "error" ) );
		
		Observable< Integer > observable = Observable.just( 10 , 20 , 30 ).mergeWith(
				errorObservable );
		
		observable.delay( 2 , TimeUnit.SECONDS , Schedulers.immediate( ) ).subscribe(
				new Subscriber< Integer >( ) {
					
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
					public void onNext( Integer t ) {
						// TODO Auto-generated method stub
						System.out.println( "Next: " + t );
					}
				} );
	}
	
	// 延迟订阅原始Observable
	public static void delaySubscription( ) {
		Observable< Integer > errorObservable = Observable.error( new Exception( "error" ) );
		
		Observable< Integer > observable = Observable.just( 10 , 20 , 30 ).mergeWith(
				errorObservable );
		
		observable.delaySubscription( 3 , TimeUnit.SECONDS , Schedulers.immediate( ) )
				.subscribe( new Subscriber< Integer >( ) {
					
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
					public void onNext( Integer t ) {
						// TODO Auto-generated method stub
						System.out.println( "Next: " + t );
					}
				} );
	}
	
	public static void doOnEach( ) {
		Observable.just( 1 , 2 , 3 )
				.doOnEach( new Action1< Notification< ? super Integer > >( ) {
					
					@Override
					public void call( Notification< ? super Integer > t ) {
						// TODO Auto-generated method stub
						System.out.println( "value===" + t.getValue( ) );
					}
				} ).subscribe( new Subscriber< Integer >( ) {
					
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
					public void onNext( Integer t ) {
						// TODO Auto-generated method stub
						System.out.println( "Next: " + t );
					}
				} );
	}
	
	public static void doOnNext( ) {
		Observable.just( 1 , 2 , 3 ).doOnNext( new Action1< Integer >( ) {
			public void call( Integer t ) {
				if ( t > 2 ) {
					throw new RuntimeException( "Item exceeds maximum value" );
				}
			};
		} ).subscribe( new Subscriber< Integer >( ) {
			
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
			public void onNext( Integer t ) {
				// TODO Auto-generated method stub
				System.out.println( "Next: " + t );
			}
		} );
	}
	
	// 将来自原始Observable的通知转换为Notification对象，然后它返回的Observable会发射这些数据
	public static void materialize( ) {
		Observable.just( 1 , 2 , 3 ).materialize( )
				.subscribe( new Subscriber< Notification< Integer >>( ) {
					
					@Override
					public void onNext( Notification< Integer > t ) {
						// TODO Auto-generated method stub
						System.out.println( "Next: " + t.getValue( ) + "--type--"
								+ t.getKind( ) );
					}
					
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
				} );
	}
	
	// 反转materialize过程，将原始Observable发射的Notification对象还原成Observable的通知
	public static void dematerialize( ) {
		Observable.just( 1 , 2 , 3 ).materialize( ).dematerialize( )
				.subscribe( new Subscriber< Object >( ) {
					
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
					public void onNext( Object t ) {
						// TODO Auto-generated method stub
						System.out.println( "Next: " + t );
					}
				} );
	}
	
	public static Scheduler getNamedScheduler( String name ) {
		return Schedulers.from( Executors.newCachedThreadPool( r -> new Thread( r , name ) ) );
	}
	
	public static void threadInfo( String caller ) {
		System.out.println( caller + " => " + Thread.currentThread( ).getName( ) );
	}
	
	public static void observeOn( ) {
		Observable.just( "RxJava" ).observeOn( getNamedScheduler( "map之前的observeOn" ) )
				.map( new Func1< String , String >( ) {
					public String call( String t ) {
						threadInfo( ".map()-1" );
						return t + "--map1";
					};
				} ).map( new Func1< String , String >( ) {
					public String call( String t ) {
						threadInfo( ".map()-2" );
						return t + "--map2";
					};
				} ).observeOn( getNamedScheduler( "subscribe之前的observeOn" ) )
				.subscribe( new Subscriber< String >( ) {
					
					@Override
					public void onCompleted( ) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onError( Throwable e ) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onNext( String t ) {
						// TODO Auto-generated method stub
						threadInfo( ".onNext()" );
						System.out.println( t + "-onNext" );
					}
				} );
	}
	
	// 强制一个Observable连续调用并保证行为正确
	public static void serialize( ) {
		Observable.just( 1 , 2 , 3 ).map( new Func1< Integer , Integer >( ) {
			
			@Override
			public Integer call( Integer t ) {
				// TODO Auto-generated method stub
				return t + 1;
			}
		} ).serialize( ).subscribe( new Subscriber< Integer >( ) {
			
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
				System.out.println( "Next: " + t );
			}
		} );
	}
	
	public static void subscribeOn( ) {
		Observable.just( 1 , 2 , 3 ).subscribeOn( Schedulers.newThread( ) )
				.observeOn( Schedulers.immediate( ) ).subscribe( );
	}
	
	public static void timeInterval( ) {
		Observable.create( new Observable.OnSubscribe< Integer >( ) {
			@Override
			public void call( Subscriber< ? super Integer > subscriber ) {
				for ( int i = 0 ; i <= 3 ; i++ ) {
					try {
						Thread.sleep( 1000 );
					}
					catch ( InterruptedException e ) {
						e.printStackTrace( );
					}
					subscriber.onNext( i );
				}
				subscriber.onCompleted( );
			}
		} ).subscribeOn( CommonFunction.getSingleScheduler( ) ).timeInterval( )
				.subscribe( new Subscriber< TimeInterval< Integer >>( ) {
					
					@Override
					public void onCompleted( ) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onError( Throwable e ) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onNext( TimeInterval< Integer > t ) {
						// TODO Auto-generated method stub
						System.out.println( "onNext---" + t.getIntervalInMilliseconds( ) );
					}
				} );
	}
	
	public static void timeout( ) {
		Observable< Integer > observable = Observable
				.create( new Observable.OnSubscribe< Integer >( ) {
					@Override
					public void call( Subscriber< ? super Integer > subscriber ) {
						for ( int i = 0 ; i <= 3 ; i++ ) {
							try {
								Thread.sleep( i * 100 );
							}
							catch ( InterruptedException e ) {
								e.printStackTrace( );
							}
							subscriber.onNext( i );
						}
						subscriber.onCompleted( );
					}
				} );
		observable.timeout( 200 , TimeUnit.MILLISECONDS /* , Observable.just( 5
														 * , 6 ) */).subscribe(
				new Subscriber< Integer >( ) {
					
					@Override
					public void onCompleted( ) {
						// TODO Auto-generated method stub
						System.out.println( "Sequence complete." );
					}
					
					@Override
					public void onError( Throwable e ) {
						// TODO Auto-generated method stub
						System.err.println( "Error-----" );
					}
					
					@Override
					public void onNext( Integer t ) {
						// TODO Auto-generated method stub
						System.out.println( "onNext---" + t );
					}
				} );
	}
	
	public static void timeStamp( ) {
		Observable.create( new Observable.OnSubscribe< Integer >( ) {
			@Override
			public void call( Subscriber< ? super Integer > subscriber ) {
				for ( int i = 0 ; i <= 3 ; i++ ) {
					try {
						Thread.sleep( 1000 );
					}
					catch ( InterruptedException e ) {
						e.printStackTrace( );
					}
					subscriber.onNext( i );
				}
				subscriber.onCompleted( );
			}
		} ).subscribeOn( CommonFunction.getSingleScheduler( ) ).timestamp( )
				.subscribe( new Subscriber< Timestamped< Integer >>( ) {
					
					@Override
					public void onCompleted( ) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onError( Throwable e ) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onNext( Timestamped< Integer > t ) {
						// TODO Auto-generated method stub
						System.out.println( "onNext---" + t.getTimestampMillis( ) );
					}
				} );
	}
	
	//创建一个只在Observable生命周期内存在的一次性资源
	public static void using( ) {
		Observable< Long > observable = Observable.using( new Func0< Animal >( ) {
			public Animal call( ) {
				return new Animal( );
			};
		} , new Func1< Animal , Observable< Long > >( ) {
			public Observable< Long > call( Animal t ) {
				return Observable.timer( 3000 , TimeUnit.MILLISECONDS );
			};
		} , new Action1< Animal >( ) {
			
			@Override
			public void call( Animal animal ) {
				// TODO Auto-generated method stub
				animal.relase( );
			}
		} );
		
		Subscriber<Long> subscriber = new Subscriber< Long >( ) {
			
			@Override
			public void onCompleted( ) {
				// TODO Auto-generated method stub
				System.out.println( "Sequence complete." );
			}
			
			@Override
			public void onError( Throwable e ) {
				// TODO Auto-generated method stub
				System.err.println( "Error-----" );
			}
			
			@Override
			public void onNext( Long t ) {
				// TODO Auto-generated method stub
				System.out.println( "onNext---" + t );
			}
		};
		
		observable.subscribe( subscriber );
		
		try {
			Thread.sleep( 5000 );
		}
		catch ( InterruptedException e1 ) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		subscriber.unsubscribe( );
	}
	
	public static void main( String[ ] args ) {
		// TODO Auto-generated method stub
		// delay( );
		// delaySubscription( );
		// doOnEach( );
		// doOnNext( );
		// materialize( );
		// dematerialize( );
		// observeOn( );
		// serialize( );
		// subscribeOn( );
		// timeInterval( );
		// timeout( );
		// timeStamp( );
		using( );
	}
	
}
