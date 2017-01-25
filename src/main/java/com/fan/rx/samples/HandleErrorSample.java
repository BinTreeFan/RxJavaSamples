
package com.fan.rx.samples;


import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;


public class HandleErrorSample
{
	public static void onErrorReturn( )
	{
		Observable< Integer > observable = Observable
				.create( new Observable.OnSubscribe< Integer >( )
				{
					@Override
					public void call( Subscriber< ? super Integer > subscriber )
					{
						if ( subscriber.isUnsubscribed( ) )
							return;
						// 循环输出数字
						try
						{
							for ( int i = 0 ; i < 10 ; i++ )
							{
								if ( i == 4 )
								{
									throw new Exception( "this is number 4 error！" );
								}
								subscriber.onNext( i );
							}
							subscriber.onCompleted( );
						}
						catch ( Exception e )
						{
							subscriber.onError( e );
						}
					}
				} );
		
		observable.onErrorReturn( new Func1< Throwable , Integer >( )
		{
			@Override
			public Integer call( Throwable throwable )
			{
				return 1004;
			}
		} ).subscribe( new Subscriber< Integer >( )
		{
			@Override
			public void onCompleted( )
			{
				System.out.println( "Sequence complete." );
			}
			
			@Override
			public void onError( Throwable e )
			{
				System.err.println( "Error: " + e.getMessage( ) );
			}
			
			@Override
			public void onNext( Integer value )
			{
				System.out.println( "Next:" + value );
			}
		} );
	}
	
	public static void onErrorResumeNext( )
	{
		Observable< Integer > observable = Observable
				.create( new Observable.OnSubscribe< Integer >( )
				{
					@Override
					public void call( Subscriber< ? super Integer > subscriber )
					{
						if ( subscriber.isUnsubscribed( ) )
							return;
						// 循环输出数字
						try
						{
							for ( int i = 0 ; i < 10 ; i++ )
							{
								if ( i == 4 )
								{
									throw new Exception( "this is number 4 error！" );
								}
								subscriber.onNext( i );
							}
							subscriber.onCompleted( );
						}
						catch ( Exception e )
						{
							subscriber.onError( e );
						}
					}
				} );
		
		observable.onErrorResumeNext(
				new Func1< Throwable , Observable< ? extends Integer >>( )
				{
					@Override
					public Observable< ? extends Integer > call( Throwable throwable )
					{
						return Observable.just( 100 , 101 , 102 );
					}
				} ).subscribe( new Subscriber< Integer >( )
		{
			@Override
			public void onCompleted( )
			{
				System.out.println( "Sequence complete." );
			}
			
			@Override
			public void onError( Throwable e )
			{
				System.err.println( "Error: " + e.getMessage( ) );
			}
			
			@Override
			public void onNext( Integer value )
			{
				System.out.println( "Next:" + value );
			}
		} );
	}
	
	//onExceptionResumeNext 和 onErrorResumeNext 的区别是只捕获 Exception
	public static void onExceptionResumeNext()
	{
		Observable< Integer > observable = Observable
				.create( new Observable.OnSubscribe< Integer >( )
				{
					@Override
					public void call( Subscriber< ? super Integer > subscriber )
					{
						if ( subscriber.isUnsubscribed( ) )
							return;
						// 循环输出数字
						try
						{
							for ( int i = 0 ; i < 10 ; i++ )
							{
								if ( i == 4 )
								{
									//throw new Throwable( "A Throwable" );
									throw new Exception( "this is number 4 error！" );
								}
								subscriber.onNext( i );
							}
							subscriber.onCompleted( );
						}
						catch ( Throwable e )
						{
							subscriber.onError( e );
						}
					}
				} );
		
		observable.onExceptionResumeNext( Observable.just( 100 , 101 , 102 ) ).subscribe(
				new Subscriber< Integer >( )
				{
					@Override
					public void onCompleted( )
					{
						System.out.println( "Sequence complete." );
					}
					
					@Override
					public void onError( Throwable e )
					{
						System.err.println( "Error: " + e.getMessage( ) );
					}
					
					@Override
					public void onNext( Integer value )
					{
						System.out.println( "Next:" + value );
					}
				} );
	}
	
	public static void retry( )
	{
		Observable< Integer > observable = Observable
				.create( new Observable.OnSubscribe< Integer >( )
				{
					@Override
					public void call( Subscriber< ? super Integer > subscriber )
					{
						if ( subscriber.isUnsubscribed( ) )
							return;
						// 循环输出数字
						try
						{
							for ( int i = 0 ; i < 10 ; i++ )
							{
								if ( i == 4 )
								{
									throw new Exception( "this is number 4 error！" );
								}
								subscriber.onNext( i );
							}
							subscriber.onCompleted( );
						}
						catch ( Throwable e )
						{
							subscriber.onError( e );
						}
					}
				} );
		
		observable.retry( 2 ).subscribe( new Subscriber< Integer >( )
		{
			@Override
			public void onCompleted( )
			{
				System.out.println( "Sequence complete." );
			}
			
			@Override
			public void onError( Throwable e )
			{
				System.err.println( "Error: " + e.getMessage( ) );
			}
			
			@Override
			public void onNext( Integer value )
			{
				System.out.println( "Next:" + value );
			}
		} );
	}
	
	public static void retryWhen( )
	{
		Observable< Integer > observable = Observable
				.create( new Observable.OnSubscribe< Integer >( )
				{
					@Override
					public void call( Subscriber< ? super Integer > subscriber )
					{
						System.out.println( "subscribing" );
						subscriber.onError( new RuntimeException( "always fails" ) );
					}
				} );
		
		observable.retryWhen(
				new Func1< Observable< ? extends Throwable > , Observable< ? >>( )
				{
					@Override
					public Observable< ? > call( Observable< ? extends Throwable > observable )
					{
						return observable.zipWith( Observable.range( 1 , 3 ) ,
								new Func2< Throwable , Integer , Integer >( )
								{
									@Override
									public Integer call( Throwable throwable , Integer integer )
									{
										return integer;
									}
								} ).flatMap( new Func1< Integer , Observable< ? >>( ){
							@Override
							public Observable< ? > call( Integer integer )
							{
								System.out
										.println( "delay retry by " + integer + " second(s)" );
								// 每一秒中执行一次
								return Observable.timer( integer , TimeUnit.SECONDS );
							}
						} );
					}
				} ).toBlocking( ).subscribe( new Subscriber< Integer >( ){
			@Override
			public void onCompleted( )
			{
				System.out.println( "Sequence complete." );
			}
			
			@Override
			public void onError( Throwable e )
			{
				System.err.println( "Error: " + e.getMessage( ) );
			}
			
			@Override
			public void onNext( Integer value )
			{
				System.out.println( "Next:" + value );
			}
		} );
	}
	
	public static void main( String[ ] args )
	{
		// TODO Auto-generated method stub
		//onErrorReturn( );
		onErrorResumeNext( );
		//onExceptionResumeNext( );
		//retry( );
		//retryWhen( );
	}
	
}
