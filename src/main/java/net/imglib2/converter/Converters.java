/*
 * #%L
 * ImgLib2: a general-purpose, multidimensional image processing library.
 * %%
 * Copyright (C) 2009 - 2016 Tobias Pietzsch, Stephan Preibisch, Stephan Saalfeld,
 * John Bogovic, Albert Cardona, Barry DeZonia, Christian Dietz, Jan Funke,
 * Aivar Grislis, Jonathan Hale, Grant Harris, Stefan Helfrich, Mark Hiner,
 * Martin Horn, Steffen Jaensch, Lee Kamentsky, Larry Lindsey, Melissa Linkert,
 * Mark Longair, Brian Northan, Nick Perry, Curtis Rueden, Johannes Schindelin,
 * Jean-Yves Tinevez and Michael Zinsmaier.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imglib2.converter;

import java.util.ArrayList;

import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealRandomAccess;
import net.imglib2.RealRandomAccessible;
import net.imglib2.RealRandomAccessibleRealInterval;
import net.imglib2.Sampler;
import net.imglib2.converter.read.ConvertedIterableInterval;
import net.imglib2.converter.read.ConvertedRandomAccessible;
import net.imglib2.converter.read.ConvertedRandomAccessibleInterval;
import net.imglib2.converter.read.ConvertedRealRandomAccessible;
import net.imglib2.converter.read.ConvertedRealRandomAccessibleRealInterval;
import net.imglib2.converter.readwrite.ARGBChannelSamplerConverter;
import net.imglib2.converter.readwrite.SamplerConverter;
import net.imglib2.converter.readwrite.WriteConvertedIterableInterval;
import net.imglib2.converter.readwrite.WriteConvertedIterableRandomAccessibleInterval;
import net.imglib2.converter.readwrite.WriteConvertedRandomAccessible;
import net.imglib2.converter.readwrite.WriteConvertedRandomAccessibleInterval;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.view.Views;

/**
 * Convenience factory methods for sample conversion.
 *
 * @author Stephan Saalfeld
 * @author Tobias Pietzsch
 */
public class Converters
{
	/**
	 * Create a {@link RandomAccessible} whose {@link RandomAccess
	 * RandomAccesses} {@link RandomAccess#get()} you a converted sample.
	 * Conversion is done on-the-fly when reading values. Writing to the
	 * converted {@link RandomAccessibleInterval} has no effect.
	 *
	 * @param source
	 * @param converter
	 * @param b
	 * @return a converted {@link RandomAccessible} whose {@link RandomAccess
	 *         RandomAccesses} perform on-the-fly value conversion using the
	 *         provided converter.
	 */
	@SuppressWarnings( "unchecked" )
	final static public < A, B extends Type< B > > RandomAccessible< B > convert(
			final RandomAccessible< A > source,
			final Converter< ? super A, ? super B > converter,
			final B b )
	{
		if ( TypeIdentity.class.isInstance( converter ) )
			return ( RandomAccessible< B > ) source;
		return new ConvertedRandomAccessible< A, B >( source, converter, b );
	}

	/**
	 * Create a {@link RandomAccessible} whose {@link RandomAccess
	 * RandomAccesses} {@link RandomAccess#get()} you a converted sample.
	 * Conversion is done on-the-fly both when reading and writing values.
	 *
	 * @param source
	 * @param converter
	 * @return a converted {@link RandomAccessible} whose {@link RandomAccess
	 *         RandomAccesses} perform on-the-fly value conversion using the
	 *         provided converter.
	 */
	final static public < A, B extends Type< B > > WriteConvertedRandomAccessible< A, B > convert(
			final RandomAccessible< A > source,
			final SamplerConverter< ? super A, B > converter )
	{
		return new WriteConvertedRandomAccessible< A, B >( source, converter );
	}

	/**
	 * Create a {@link RandomAccessibleInterval} whose {@link RandomAccess
	 * RandomAccesses} {@link RandomAccess#get()} you a converted sample.
	 * Conversion is done on-the-fly when reading values. Writing to the
	 * converted {@link RandomAccessibleInterval} has no effect.
	 *
	 * @param source
	 * @param converter
	 * @param b
	 * @return a converted {@link RandomAccessibleInterval} whose
	 *         {@link RandomAccess RandomAccesses} perform on-the-fly value
	 *         conversion using the provided converter.
	 */
	@SuppressWarnings( "unchecked" )
	final static public < A, B extends Type< B > > RandomAccessibleInterval< B > convert(
			final RandomAccessibleInterval< A > source,
			final Converter< ? super A, ? super B > converter,
			final B b )
	{
		if ( TypeIdentity.class.isInstance( converter ) )
			return ( RandomAccessibleInterval< B > ) source;
		return new ConvertedRandomAccessibleInterval< A, B >( source, converter, b );
	}

	/**
	 * Create a {@link RandomAccessibleInterval} whose {@link RandomAccess
	 * RandomAccesses} {@link RandomAccess#get()} you a converted sample.
	 * Conversion is done on-the-fly both when reading and writing values.
	 *
	 * @param source
	 * @param converter
	 * @return a converted {@link RandomAccessibleInterval} whose
	 *         {@link RandomAccess RandomAccesses} perform on-the-fly value
	 *         conversion using the provided converter.
	 */
	final static public < A, B extends Type< B > > WriteConvertedRandomAccessibleInterval< A, B > convert(
			final RandomAccessibleInterval< A > source,
			final SamplerConverter< ? super A, B > converter )
	{
		return new WriteConvertedRandomAccessibleInterval< A, B >( source, converter );
	}

	/**
	 * Create a {@link IterableInterval} whose {@link Cursor Cursors}
	 * {@link Cursor#get()} you a converted sample. Conversion is done
	 * on-the-fly when reading values. Writing to the converted
	 * {@link IterableInterval} has no effect.
	 *
	 * @param source
	 * @param converter
	 * @param b
	 * @return a converted {@link IterableInterval} whose {@link Cursor Cursors}
	 *         perform on-the-fly value conversion using the provided converter.
	 */
	@SuppressWarnings( "unchecked" )
	final static public < A, B extends Type< B > > IterableInterval< B > convert(
			final IterableInterval< A > source,
			final Converter< ? super A, ? super B > converter,
			final B b )
	{
		if ( TypeIdentity.class.isInstance( converter ) )
			return ( IterableInterval< B > ) source;
		return new ConvertedIterableInterval< A, B >( source, converter, b );
	}

	/**
	 * Create an {@link IterableInterval} whose {@link Cursor Cursors}
	 * {@link Cursor#get()} you a converted sample. Conversion is done
	 * on-the-fly both when reading and writing values.
	 *
	 * @param source
	 * @param converter
	 * @return a converted {@link IterableInterval} whose {@link Cursor Cursors}
	 *         perform on-the-fly value conversion using the provided converter.
	 */
	final static public < A, B extends Type< B > > WriteConvertedIterableInterval< A, B > convert(
			final IterableInterval< A > source,
			final SamplerConverter< ? super A, B > converter )
	{
		return new WriteConvertedIterableInterval< A, B >( source, converter );
	}

	/**
	 * Create an {@link WriteConvertedIterableRandomAccessibleInterval} whose
	 * {@link RandomAccess RandomAccesses} and {@link Cursor Cursors}
	 * {@link Cursor#get()} you a converted sample. Conversion is done
	 * on-the-fly both when reading and writing values.
	 *
	 * @param source
	 * @param converter
	 * @return a {@link WriteConvertedIterableRandomAccessibleInterval} whose
	 *         {@link Sampler Samplers} perform on-the-fly value conversion
	 *         using the provided converter.
	 */
	final static public < A, B extends Type< B >, S extends RandomAccessible< A > & IterableInterval< A > >
			WriteConvertedIterableRandomAccessibleInterval< A, B, S > convertRandomAccessibleIterableInterval(
					final S source,
					final SamplerConverter< ? super A, B > converter )
	{
		return new WriteConvertedIterableRandomAccessibleInterval< A, B, S >( source, converter );
	}

	/**
	 * Create a {@link RealRandomAccessibleRealInterval} whose {@link RealRandomAccess
	 * RealRandomAccesses} {@link RealRandomAccess#get()} you a converted sample.
	 * Conversion is done on-the-fly when reading values. Writing to the
	 * converted {@link RealRandomAccessibleRealInterval} has no effect.
	 *
	 * @param source
	 * @param converter
	 * @param b
	 * @return a converted {@link RealRandomAccessibleRealInterval} whose
	 *         {@link RealRandomAccess RealRandomAccesses} perform on-the-fly value
	 *         conversion using the provided converter.
	 */
	@SuppressWarnings( "unchecked" )
	final static public < A, B extends Type< B > > RealRandomAccessibleRealInterval< B > convert(
			final RealRandomAccessibleRealInterval< A > source,
			final  Converter< ? super A, ? super B > converter,
			final B b )
	{
		if ( TypeIdentity.class.isInstance( converter ) )
			return ( RealRandomAccessibleRealInterval< B > ) source;
		return new ConvertedRealRandomAccessibleRealInterval< A, B >( source, converter, b );
	}

	/**
	 * Create a {@link RealRandomAccessible} whose {@link RealRandomAccess
	 * RealRandomAccesses} {@link RealRandomAccess#get()} you a converted sample.
	 * Conversion is done on-the-fly when reading values. Writing to the
	 * converted {@link RandomAccessibleInterval} has no effect.
	 *
	 * @param source
	 * @param converter
	 * @param b
	 * @return a converted {@link RealRandomAccessible} whose {@link RealRandomAccess
	 *         RealRandomAccesses} perform on-the-fly value conversion using the
	 *         provided converter.
	 */
	@SuppressWarnings( "unchecked" )
	final static public < A, B extends Type< B > > RealRandomAccessible< B > convert(
			final RealRandomAccessible< A > source,
			final  Converter< ? super A, ? super B > converter,
			final B b )
	{
		if ( TypeIdentity.class.isInstance( converter ) )
			return ( RealRandomAccessible< B > ) source;
		return new ConvertedRealRandomAccessible< A, B >( source, converter, b );
	}

	/**
	 * Create a {@link WriteConvertedRandomAccessibleInterval} to one of the
	 * four channels encoded in a {@link RandomAccessibleInterval} of
	 * {@link ARGBType}.  The source is being modified as expected by writing
	 * into the converted channels.
	 *
	 * @param source
	 * @param channel 0 = alpha, 1 = red, 2 = green, 3 = blue
	 *
	 * @return a converted {@link WriteConvertedRandomAccessibleInterval} whose
	 *         {@link Sampler Samplers} perform on-the-fly value conversion
	 *         into and from one channel of the original {@link ARGBType}.
	 */
	final static public WriteConvertedRandomAccessibleInterval< ARGBType, UnsignedByteType > argbChannel(
			final RandomAccessibleInterval< ARGBType > source,
			final int channel )
	{
		return convert(
				source,
				new ARGBChannelSamplerConverter( channel ) );
	}

	/**
	 * Create a {@link WriteConvertedRandomAccessible} to one of the four
	 * channels encoded in a {@link RandomAccessible} of {@link ARGBType}.
	 * The source is being modified as expected by writing into the converted
	 * channels.
	 *
	 * @param source
	 * @param channel 0 = alpha, 1 = red, 2 = green, 3 = blue
	 *
	 * @return a converted {@link WriteConvertedRandomAccessible} whose
	 *         {@link Sampler Samplers} perform on-the-fly value conversion
	 *         into and from one channel of the original {@link ARGBType}.
	 */
	final static public WriteConvertedRandomAccessible< ARGBType, UnsignedByteType > argbChannel(
			final RandomAccessible< ARGBType > source,
			final int channel )
	{
		return convert(
				source,
				new ARGBChannelSamplerConverter( channel ) );
	}

	/**
	 * Create an (<em>n</em>+1)-dimensional {@link RandomAccessible} of an
	 * <em>n</em>-dimensional {@link RandomAccessible} that maps the four
	 * channels encoded in {@link ARGBType} into a dimension.  The source is
	 * being modified as expected by writing into the converted channels.
	 *
	 * @param source
	 *
	 * @return a converted {@link RandomAccessibleInterval} whose
	 *         {@link Sampler Samplers} perform on-the-fly value conversion
	 *         into and from the corresponding channels of the original
	 *         {@link ARGBType}.
	 */
	final static public RandomAccessibleInterval< UnsignedByteType > argbChannels( final RandomAccessibleInterval< ARGBType > source )
	{
		return Views.stack(
				argbChannel( source, 0 ),
				argbChannel( source, 1 ),
				argbChannel( source, 2 ),
				argbChannel( source, 3 ) );
	}

	/**
	 * Create an (<em>n</em>+1)-dimensional {@link RandomAccessible} of an
	 * <em>n</em>-dimensional {@link RandomAccessible} that maps the four
	 * channels encoded in {@link ARGBType} into a dimension.  The order
	 * of the channels passed as arguments is preserved.  The source is being
	 * modified as expected by writing into the converted channels.
	 *
	 * @param source
	 * @param channels 0 = alpha, 1 = red, 2 = green, 3 = blue
	 *
	 * @return a converted {@link RandomAccessibleInterval} whose
	 *         {@link Sampler Samplers} perform on-the-fly value conversion
	 *         into and from the corresponding channels of the original
	 *         {@link ARGBType}.
	 */
	final static public RandomAccessibleInterval< UnsignedByteType > argbChannels( final RandomAccessibleInterval< ARGBType > source, final int... channels )
	{
		final ArrayList< RandomAccessibleInterval< UnsignedByteType > > hyperSlices = new ArrayList<>();
		for ( final int c : channels )
			hyperSlices.add( argbChannel( source, channels[ c ] ) );

		return Views.stack( hyperSlices );
	}
}
