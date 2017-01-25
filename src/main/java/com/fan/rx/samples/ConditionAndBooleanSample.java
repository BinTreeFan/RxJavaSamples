
package com.fan.rx.samples;


import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;


public class ConditionAndBooleanSample {
	
	// 判定是否Observable发射的所有数据都满足某个条件
	public static void all( ) {
		Observable.just( 1 , 2 , 3 , 4 ).all( new Func1< Integer , Boolean >( ) {
			public Boolean call( Integer t ) {
				return t < 3;
			};
		} ).subscribe( new Subscriber< Boolean >( ) {
			@Override
			public void onCompleted( ) {
				System.out.println( "Sequence complete." );
			}
			
			@Override
			public void onError( Throwable e ) {
				System.err.println( "Error-----" );
			}
			
			@Override
			public void onNext( Boolean aBoolean ) {
				System.out.println( "onNext---" + aBoolean );
			}
		} );
	}
	
	// 给定两个或多个Observables，它只发射首先发射数据或通知的那个Observable的所有数据
	public static void amb( ) {
		Observable.amb( Observable.just( 1 , 2 , 3 ).delay( 1 , TimeUnit.SECONDS ) ,
				Observable.just( 4 , 5 , 6 ) ).subscribe( new Subscriber< Integer >( ) {
			@Override
			public void onCompleted( ) {
				System.out.println( "Sequence complete." );
			}
			
			@Override
			public void onError( Throwable e ) {
				System.err.println( "Error-----" );
			}
			
			@Override
			public void onNext( Integer t ) {
				System.out.println( "onNext---" + t );
			}
		} );
	}
	
	// 判定一个Observable是否发射一个特定的值
	public static void contains( ) {
		Observable.just( 4 , 5 , 6 ).contains( 7 ).subscribe( new Action1< Boolean >( ) {
			
			@Override
			public void call( Boolean t ) {
				// TODO Auto-generated method stub
				System.out.println( "onNext---" + t );
			}
		} );
	}
	
	// 判定原始Observable是否没有发射任何数据
	public static void isEmpty( ) {
		Observable.just( 4 , 5 , 6 ).isEmpty( ).subscribe( new Action1< Boolean >( ) {
			
			@Override
			public void call( Boolean t ) {
				// TODO Auto-generated method stub
				System.out.println( "onNext---" + t );
			}
		} );
		
		Observable.create( new Observable.OnSubscribe< Integer >( ) {
			public void call( Subscriber< ? super Integer > subscriber ) {
				subscriber.onCompleted( );
			};
		} ).isEmpty( ).subscribe( new Action1< Boolean >( ) {
			
			@Override
			public void call( Boolean t ) {
				// TODO Auto-generated method stub
				System.out.println( "onNext---" + t );
			}
		} );
	}
	
	// exists操作符，它通过一个谓词函数测试原始Observable发射的数据，
	// 只要任何一项满足条件就返回一个发射true的Observable，
	// 否则返回一个发射false的Observable。
	public static void exists( ) {
		Observable.just( 1 , 4 , 3 ).exists( new Func1< Integer , Boolean >( ) {
			public Boolean call( Integer t ) {
				return t > 5;
			};
		} ).subscribe( new Action1< Boolean >( ) {
			
			@Override
			public void call( Boolean t ) {
				// TODO Auto-generated method stub
				System.out.println( "onNext---" + t );
			}
		} );
	}
	
	// DefaultIfEmpty简单的精确地发射原始Observable的值，如果原始Observable没有发射任何数据正常终止（以onCompletedd的形式），
	// DefaultIfEmpty返回的Observable就发射一个你提供的默认值
	public static void defaultIfEmpty( ) {
		Observable.empty( ).defaultIfEmpty( "123" ).subscribe( new Action1< Object >( ) {
			
			@Override
			public void call( Object t ) {
				// TODO Auto-generated method stub
				System.out.println( "onNext---" + t );
			}
		} );
	}
	
	// 判定两个Observables是否发射相同的数据序列。
	// 传递两个Observable给SequenceEqual操作符，它会比较两个Observable的发射物，
	// 如果两个序列是相同的（相同的数据，相同的顺序，相同的终止状态），它就发射true，否则发射false
	public static void sequenceEqual( ) {
		Observable
				.sequenceEqual( Observable.just( 4 , 5 , 6 ).delay( 1 , TimeUnit.SECONDS ) ,
						Observable.just( 4 , 5 , 6 ) )
				.observeOn( CommonFunction.getSingleScheduler( ) )
				.subscribe( new Subscriber< Boolean >( ) {
					@Override
					public void onCompleted( ) {
						System.out.println( "Sequence complete." );
					}
					
					@Override
					public void onError( Throwable e ) {
						System.err.println( "Error-----" );
					}
					
					@Override
					public void onNext( Boolean t ) {
						System.out.println( "onNext---" + t );
					}
				} );
	}
	
	public static void skipUntil( ) {
		Observable.interval( 1 , TimeUnit.SECONDS ).take( 6 )
				.skipUntil( Observable.just( 2 , 6 , 8 ).delay( 5 , TimeUnit.SECONDS ) )
				.observeOn( CommonFunction.getSingleScheduler( ) )
				.subscribe( new Subscriber< Long >( ) {
					
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
				} );
	}
	
	public static void skipWhile( ) {
		Observable.just( 2 , 6 , 8 ).skipWhile( new Func1< Integer , Boolean >( ) {
			
			@Override
			public Boolean call( Integer t ) {
				// TODO Auto-generated method stub
				return t > 10;
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
				System.err.println( "Error-----" );
			}
			
			@Override
			public void onNext( Integer t ) {
				// TODO Auto-generated method stub
				System.out.println( "onNext---" + t );
			}
		} );
	}
	
	public static void takeUntil( ) {
		Observable.interval( 1 , TimeUnit.SECONDS ).take( 10 )
				.observeOn( CommonFunction.getSingleScheduler( ) )
				.takeUntil( Observable.timer( 3 , TimeUnit.SECONDS ) )
				.subscribe( new Subscriber< Long >( ) {
					
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
				} );
	}
	
	public static void takeWhile( ) {
		Observable.just( 2 , 6 , 8 ).takeWhile( new Func1< Integer , Boolean >( ) {
			
			@Override
			public Boolean call( Integer t ) {
				// TODO Auto-generated method stub
				return t < 6;
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
				System.err.println( "Error-----" );
			}
			
			@Override
			public void onNext( Integer t ) {
				// TODO Auto-generated method stub
				System.out.println( "onNext---" + t );
			}
		} );
	}
	
	public static void main( String[ ] args ) {
		// TODO Auto-generated method stub
		// all( );
		// amb( );
		// contains( );
		// isEmpty( );
		// exists( );
		// defaultIfEmpty( );
		// sequenceEqual( );
		// skipUntil( );
		// skipWhile( );
		//takeUntil( );
		takeWhile( );
	}
}
