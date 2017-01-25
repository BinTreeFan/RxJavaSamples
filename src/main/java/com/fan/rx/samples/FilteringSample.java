
package com.fan.rx.samples;


import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class FilteringSample
{
	/**
	 * debounce
	 * 
	 * @Description: TODO
	 *               debounce操作符对源Observable每产生一个结果后，如果在规定的间隔时间内没有别的结果产生，则把这个结果提交给订阅者处理
	 *               ，否则忽略该结果。
	 *               值得注意的是，如果源Observable产生的最后一个结果后在规定的时间间隔内调用了onCompleted
	 *               ，那么通过debounce操作符也会把这个结果提交给订阅者
	 */
	public static void debounce( )
	{
		Observable.create( new Observable.OnSubscribe< Integer >( )
		{
			@Override
			public void call( Subscriber< ? super Integer > subscriber )
			{
				
				if ( subscriber.isUnsubscribed( ) )
					return;
				
				try
				{
					// 产生结果的间隔时间分别为100、200、300...900毫秒
					for ( int i = 1 ; i < 10 ; i++ )
					{
						subscriber.onNext( i );
						Thread.sleep( i * 100 );
					}
					subscriber.onCompleted( );
				}
				catch ( Exception e )
				{
					subscriber.onError( e );
				}
			}
		} ).debounce( 400 , TimeUnit.MILLISECONDS ) // 超时时间为400毫秒
				.subscribe( new Subscriber< Integer >( )
				{
					@Override
					public void onNext( Integer item )
					{
						System.out.println( "Next: " + item );
					}
					
					@Override
					public void onError( Throwable error )
					{
						System.err.println( "Error: " + error.getMessage( ) );
					}
					
					@Override
					public void onCompleted( )
					{
						System.out.println( "onCompleted" );
					}
				} );
	}
	
	public static void distinct( )
	{
		Observable.just( 1 , 2 , 1 , 1 , 2 , 3 ).distinct( )
				.subscribe( new Subscriber< Integer >( )
				{
					@Override
					public void onNext( Integer item )
					{
						System.out.println( "Next: " + item );
					}
					
					@Override
					public void onError( Throwable error )
					{
						System.err.println( "Error: " + error.getMessage( ) );
					}
					
					@Override
					public void onCompleted( )
					{
						System.out.println( "Sequence complete." );
					}
				} );
	}
	
	public static void elementAt( )
	{
		Observable.just( 1 , 2 , 3 , 4 , 5 , 6 ).elementAt( 2 )
				.subscribe( new Subscriber< Integer >( )
				{
					@Override
					public void onNext( Integer item )
					{
						System.out.println( "Next: " + item );
					}
					
					@Override
					public void onError( Throwable error )
					{
						System.err.println( "Error: " + error.getMessage( ) );
					}
					
					@Override
					public void onCompleted( )
					{
						System.out.println( "Sequence complete." );
					}
				} );
	}
	
	public static void filter( )
	{
		// Filter操作符使用你指定的一个谓词函数测试数据项，只有通过测试的数据才会被发射
		Observable.just( 1 , 2 , 3 , 4 , 5 ).filter( new Func1< Integer , Boolean >( )
		{
			@Override
			public Boolean call( Integer item )
			{
				return ( item < 4 );
			}
		} ).subscribe( new Subscriber< Integer >( )
		{
			@Override
			public void onNext( Integer item )
			{
				System.out.println( "Next: " + item );
			}
			
			@Override
			public void onError( Throwable error )
			{
				System.err.println( "Error: " + error.getMessage( ) );
			}
			
			@Override
			public void onCompleted( )
			{
				System.out.println( "Sequence complete." );
			}
		} );
	}
	
	public static void first( )
	{
		// 只发射第一项（或者满足某个条件的第一项）数据
		Observable.just( 1 , 2 , 3 ).first( ).subscribe( new Subscriber< Integer >( )
		{
			@Override
			public void onNext( Integer item )
			{
				System.out.println( "Next: " + item );
			}
			
			@Override
			public void onError( Throwable error )
			{
				System.err.println( "Error: " + error.getMessage( ) );
			}
			
			@Override
			public void onCompleted( )
			{
				System.out.println( "Sequence complete." );
			}
		} );
	}
	
	public static void last( )
	{
		Observable.just( 1 , 2 , 3 ).last( ).subscribe( new Subscriber< Integer >( )
		{
			@Override
			public void onNext( Integer item )
			{
				System.out.println( "Next: " + item );
			}
			
			@Override
			public void onError( Throwable error )
			{
				System.err.println( "Error: " + error.getMessage( ) );
			}
			
			@Override
			public void onCompleted( )
			{
				System.out.println( "Sequence complete." );
			}
		} );
	}
	
	public static void sample( )
	{
		// 定期扫描源Observable产生的结果，在指定的时间间隔范围内对源Observable产生的结果进行采样
		Observable.create( new Observable.OnSubscribe< Integer >( )
		{
			@Override
			public void call( Subscriber< ? super Integer > subscriber )
			{
				if ( subscriber.isUnsubscribed( ) )
					return;
				try
				{
					// 前8个数字产生的时间间隔为1秒，后一个间隔为3秒
					for ( int i = 1 ; i < 9 ; i++ )
					{
						subscriber.onNext( i );
						Thread.sleep( 1000 );
					}
					// Thread.sleep( 2000 );
					subscriber.onNext( 4 );
					subscriber.onCompleted( );
				}
				catch ( Exception e )
				{
					subscriber.onError( e );
				}
			}
		} ).sample( 2200 , TimeUnit.MILLISECONDS ) // 采样间隔时间为2200毫秒
				.subscribe( new Subscriber< Integer >( )
				{
					@Override
					public void onNext( Integer item )
					{
						System.out.println( "Next: " + item );
					}
					
					@Override
					public void onError( Throwable error )
					{
						System.err.println( "Error: " + error.getMessage( ) );
					}
					
					@Override
					public void onCompleted( )
					{
						System.out.println( "Sequence complete." );
					}
				} );
	}
	
	public static void skip( )
	{
		Observable.just( 1 , 2 , 3 , 4 , 5 , 6 , 7 ).skip( 3 )
				.subscribe( new Subscriber< Integer >( )
				{
					@Override
					public void onNext( Integer item )
					{
						System.out.println( "Next: " + item );
					}
					
					@Override
					public void onError( Throwable error )
					{
						System.err.println( "Error: " + error.getMessage( ) );
					}
					
					@Override
					public void onCompleted( )
					{
						System.out.println( "Sequence complete." );
					}
				} );
	}
	
	public static void skipLast( )
	{
		Observable.just( 1 , 2 , 3 , 4 , 5 , 6 , 7 ).skipLast( 3 )
				.subscribe( new Subscriber< Integer >( )
				{
					@Override
					public void onNext( Integer item )
					{
						System.out.println( "Next: " + item );
					}
					
					@Override
					public void onError( Throwable error )
					{
						System.err.println( "Error: " + error.getMessage( ) );
					}
					
					@Override
					public void onCompleted( )
					{
						System.out.println( "Sequence complete." );
					}
				} );
	}
	
	public static void take( )
	{
		Observable.just( 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 ).take( 4 )
				.subscribe( new Subscriber< Integer >( )
				{
					@Override
					public void onNext( Integer item )
					{
						System.out.println( "Next: " + item );
					}
					
					@Override
					public void onError( Throwable error )
					{
						System.err.println( "Error: " + error.getMessage( ) );
					}
					
					@Override
					public void onCompleted( )
					{
						System.out.println( "Sequence complete." );
					}
				} );
	}
	
	public static void takeLast( )
	{
		Observable.just( 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 ).takeLast( 4 )
				.subscribe( new Subscriber< Integer >( )
				{
					@Override
					public void onNext( Integer item )
					{
						System.out.println( "Next: " + item );
					}
					
					@Override
					public void onError( Throwable error )
					{
						System.err.println( "Error: " + error.getMessage( ) );
					}
					
					@Override
					public void onCompleted( )
					{
						System.out.println( "Sequence complete." );
					}
				} );
		
		Observable.just( 1 , 2 ).subscribe( new Action1< Integer >( )
		{

			@Override
			public void call( Integer item )
			{
				// TODO Auto-generated method stub
				System.out.println( "Next: " + item );
			}	
		} );
	}
	
	public static void throttleFirst( )
	{
		Observable.just( 1 , 6 , 2 ).throttleFirst( 600 , TimeUnit.MILLISECONDS ).subscribe( new Subscriber< Integer >( )
		{
			@Override
			public void onNext( Integer item )
			{
				System.out.println( "Next: " + item );
			}
			
			@Override
			public void onError( Throwable error )
			{
				System.err.println( "Error: " + error.getMessage( ) );
			}
			
			@Override
			public void onCompleted( )
			{
				System.out.println( "Sequence complete." );
			}
			
		} );
	}
	
	public static void main( String[ ] args )
	{
		// TODO Auto-generated method stub
		// debounce( );
		// distinct( );
		// elementAt( );
		// filter( );
		// first( );
		// last( );
		// skip( );
		// sample( );
		// skipLast( );
		//take( );
		//takeLast( );
		throttleFirst( );
	}
	
}
