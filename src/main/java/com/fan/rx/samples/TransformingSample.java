
package com.fan.rx.samples;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;


/**
 * User: mcxiaoke Date: 15/7/28 Time: 16:37
 */
public class TransformingSample
{
	
	
	/**
	 * output onNext: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9] onNext: [10, 11, 12, 13,
	 * 14, 15, 16, 17, 18, 19] onNext: [20, 21, 22, 23, 24, 25, 26, 27, 28, 29]
	 * onCompleted
	 */
	// Observable<List<T>> buffer(int count)
	public static void buffer1( )
	{
		Observable.range( 0 , 30 ).buffer( 10 ).subscribe( new Observer< List< Integer >>( )
		{
			@Override
			public void onCompleted( )
			{
				System.out.println( "onCompleted" );
			}
			
			@Override
			public void onError( final Throwable e )
			{
				System.err.println( "onError: " + e );
			}
			
			@Override
			public void onNext( final List< Integer > i )
			{
				System.out.println( "onNext: " + i );
			}
		} );
	}
	
	/**
	 * output onNext: [0, 1, 2, 3, 4] onNext: [3, 4, 5, 6, 7] onNext: [6, 7, 8,
	 * 9] onNext: [9] onCompleted
	 */
	// Observable<List<T>> buffer(int count, int skip)
	public static void buffer2( )
	{
		Observable.range( 0 , 10 ).buffer( 5 , 2 ).subscribe( new Observer< List< Integer >>( )
		{
			@Override
			public void onCompleted( )
			{
				System.out.println( "onCompleted" );
			}
			
			@Override
			public void onError( final Throwable e )
			{
				System.err.println( "onError: " + e );
			}
			
			@Override
			public void onNext( final List< Integer > i )
			{
				System.out.println( "onNext: " + i );
			}
		} );
	}
	
	/**
	 * onNext: [100, 101] onNext: [102, 103, 104] onNext: [105, 106, 107]
	 * onNext: [108, 109, 110] onNext: [] onCompleted
	 */
	// <TClosing> Observable<List<T>> buffer(Func0<? extends
	// Observable<? extends TClosing>> bufferClosingSelector)
	public static void buffer3( )
	{
		Observable.create( new OnSubscribe< Integer >( )
		{
			@Override
			public void call( final Subscriber< ? super Integer > subscriber )
			{
				for ( int i = 100 ; i < 120 ; i++ )
				{
					try
					{
						Thread.sleep( 100 );
					}
					catch ( InterruptedException e )
					{
						e.printStackTrace( );
					}
					if ( !subscriber.isUnsubscribed( ) )
					{
						subscriber.onNext( i );
					}
				}
				subscriber.onCompleted( );
			}
		} ).buffer( new Func0< Observable< Integer >>( )
		{
			@Override
			public Observable< Integer > call( )
			{
				return Observable.just( 100 ).delay( 300 , TimeUnit.MILLISECONDS ).repeat( 4 );
			}
		} ).subscribe( new Observer< List< Integer >>( )
		{
			@Override
			public void onCompleted( )
			{
				System.out.println( "onCompleted" );
			}
			
			@Override
			public void onError( final Throwable e )
			{
				System.err.println( "onError: " + e );
			}
			
			@Override
			public void onNext( final List< Integer > i )
			{
				System.out.println( "onNext: " + i );
			}
		} );
	}
	
	public static void bufferTest( )
	{
		Observable.just( 100 ).delay( 300 , TimeUnit.MILLISECONDS )
		/* .repeat( 4 ) */.subscribe( new Observer< Integer >( )
		{
			
			@Override
			public void onCompleted( )
			{
				// TODO Auto-generated method stub
				System.out.println( "onCompleted" );
			}
			
			@Override
			public void onError( Throwable e )
			{
				// TODO Auto-generated method stub
				System.err.println( "onError: " + e );
			}
			
			@Override
			public void onNext( Integer t )
			{
				// TODO Auto-generated method stub
				System.out.println( "onNext: " + t );
			}
		} );
	}
	
	/**
	 * onNext: Mapped Item 2 onNext: Mapped Item 4 onNext: Mapped Item 6 onNext:
	 * Mapped Item 8 onNext: Mapped Item 10 onCompleted
	 */
	// <R> Observable<R> map(Func1<? super T, ? extends R> func)
	public static void map( )
	{
		// Map操作符对原始Observable发射的每一项数据应用一个你选择的函数，然后返回一个发射这些结果的Observable
		// Map一般用于对原始的参数进行加工处理，返回值还是基本的类型，可以在subscribe中使用(适用)的类型。
		Observable.just( 1 , 2 , 3 , 4 , 5 ).map( new Func1< Integer , String >( )
		{
			@Override
			public String call( final Integer integer )
			{
				return "Mapped Item " + integer * 2;
			}
		} ).subscribe( new Observer< String >( )
		{
			@Override
			public void onCompleted( )
			{
				System.out.println( "onCompleted" );
			}
			
			@Override
			public void onError( final Throwable e )
			{
				System.err.println( "onError: " + e );
			}
			
			@Override
			public void onNext( final String i )
			{
				System.out.println( "onNext: " + i );
			}
		} );
	}
	
	public static void cast( )
	{
		Observable.just( new StringBuilder( "String" ) ).cast( CharSequence.class )
				.subscribe( new Observer< CharSequence >( )
				{
					@Override
					public void onCompleted( )
					{
						System.out.println( "onCompleted" );
					}
					
					@Override
					public void onError( final Throwable e )
					{
						System.err.println( "onError: " + e );
					}
					
					@Override
					public void onNext( final CharSequence i )
					{
						System.out.println( "onNext: " + i );
					}
				} );
	}
	
	/**
	 * onNext: List Item 0 onNext: List Item 2 onNext: List Item 4 onNext: List
	 * Item 6 onNext: List Item 8 onCompleted
	 */
	// <R> Observable<R> flatMap(Func1<? super T, ? extends Observable<? extends
	// R>> func)
	// FlatMap对这些Observables发射的数据做的是合并(merge)操作，因此它们可能是交错的
	// flatMap一般用于输出一个Observable，而其随后的subscribe中的参数也跟Observable中的参数一样，
	// 注意不是Observable，一般用于对原始数据返回一个Observable,这个Observable中数据类型可以是原来的，也可以是其他的
	public static void flatMap1( )
	{
		List< String > strings = new ArrayList<>( );
		for ( int i = 0 ; i < 10 ; i += 1 )
		{
			strings.add( "List Item " + i );
		}
		Observable.just( strings )
				.flatMap( new Func1< List< String > , Observable< String >>( )
				{
					@Override
					public Observable< String > call( final List< String > strings )
					{
						return Observable.from( strings );
					}
				} ).subscribe( new Observer< String >( )
				{
					@Override
					public void onCompleted( )
					{
						System.out.println( "onCompleted" );
					}
					
					@Override
					public void onError( final Throwable e )
					{
						System.err.println( "onError: " + e );
					}
					
					@Override
					public void onNext( final String i )
					{
						System.out.println( "onNext: " + i );
					}
				} );
	}
	
	public static void flatMap2( )
	{
		List< WebUrlInfo > dataList = new ArrayList< WebUrlInfo >( );
		for ( int i = 0 ; i < 10 ; i += 1 )
		{
			WebUrlInfo info = new WebUrlInfo( );
			info.url = "Web Url=== " + i;
			info.title = "Web Title=== " + i;
			dataList.add( info );
		}
		Observable.just( dataList )
				.flatMap( new Func1< List< WebUrlInfo > , Observable< ? extends String > >( )
				{
					public Observable< ? extends String > call( List< WebUrlInfo > list )
					{
						List< String > tempList = new ArrayList< String >( );
						for ( int i = 0 ; i < list.size( ) ; i++ )
						{
							tempList.add( "After flatMap===" + list.get( i ).title );
						}
						return Observable.from( tempList );
					};
				} ).subscribe( new Observer< String >( )
				{
					@Override
					public void onCompleted( )
					{
						System.out.println( "onCompleted" );
					}
					
					@Override
					public void onError( final Throwable e )
					{
						System.err.println( "onError: " + e );
					}
					
					@Override
					public void onNext( final String i )
					{
						System.out.println( "onNext: " + i );
					}
				} );
	}
	
	public static void concatMap( )
	{
		List< String > strings = new ArrayList<>( );
		for ( int i = 0 ; i < 10 ; i += 2 )
		{
			strings.add( "List Item " + i );
		}
		Observable.just( strings )
				.concatMap( new Func1< List< String > , Observable< String >>( )
				{
					@Override
					public Observable< String > call( final List< String > strings )
					{
						return Observable.from( strings );
					}
				} ).subscribe( new Observer< String >( )
				{
					@Override
					public void onCompleted( )
					{
						System.out.println( "onCompleted" );
					}
					
					@Override
					public void onError( final Throwable e )
					{
						System.err.println( "onError: " + e );
					}
					
					@Override
					public void onNext( final String i )
					{
						System.out.println( "onNext: " + i );
					}
				} );
	}
	
	public static void scan( )
	{
		Observable.just( 1 , 2 , 3 , 4 , 5 ).scan( new Func2< Integer , Integer , Integer >( )
		{
			@Override
			public Integer call( Integer sum , Integer item )
			{
				return sum + item;
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
	
	public static void window( ) {
		List< String > strings = new ArrayList<>( );
		for ( int i = 0 ; i < 10 ; i += 1 )
		{
			strings.add( "List Item " + i );
		}
		
		Observable.from( strings ).window( 4 ).subscribe( new Subscriber<Observable< String >>( ) {

			@Override
			public void onCompleted( ) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError( Throwable e ) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onNext( Observable< String > observable ) {
				// TODO Auto-generated method stub
				observable.subscribe( new Subscriber<String>( ) {

					@Override
					public void onCompleted( ) {
						// TODO Auto-generated method stub
						System.out.println( "Sequence complete." );
					}

					@Override
					public void onError( Throwable e ) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onNext( String t ) {
						// TODO Auto-generated method stub
						System.out.println( "Next: " + t );
					}
				} );
			}
		} );
	}
	
	public static void main( String[ ] args )
	{
		// buffer1();
		// buffer2();
		// buffer3();
		// bufferTest( );
		//map( );
		// cast();
		// flatMap1( );
		// flatMap2( );
		// concatMap( );
		// scan( );
		window( );
	}
}
