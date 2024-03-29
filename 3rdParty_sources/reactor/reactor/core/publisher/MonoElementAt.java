/*
 * Copyright (c) 2016-2022 VMware Inc. or its affiliates, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package reactor.core.publisher;

import java.util.Objects;

import reactor.core.CoreSubscriber;
import reactor.core.Fuseable;
import reactor.util.annotation.Nullable;

/**
 * Emits only the element at the given index position or signals a
 * default value if specified or IndexOutOfBoundsException if the sequence is shorter.
 *
 * @param <T> the value type
 * @see <a href="https://github.com/reactor/reactive-streams-commons">Reactive-Streams-Commons</a>
 */
final class MonoElementAt<T> extends MonoFromFluxOperator<T, T>
		implements Fuseable {

	final long index;

	final T defaultValue;

	MonoElementAt(Flux<? extends T> source, long index) {
		super(source);
		if (index < 0) {
			throw new IndexOutOfBoundsException("index >= required but it was " + index);
		}
		this.index = index;
		this.defaultValue = null;
	}

	MonoElementAt(Flux<? extends T> source, long index, T defaultValue) {
		super(source);
		if (index < 0) {
			throw new IndexOutOfBoundsException("index >= required but it was " + index);
		}
		this.index = index;
		this.defaultValue = Objects.requireNonNull(defaultValue, "defaultValue");
	}

	@Override
	public CoreSubscriber<? super T> subscribeOrReturn(CoreSubscriber<? super T> actual) {
		return new ElementAtSubscriber<>(actual, index, defaultValue);
	}

	@Override
	public Object scanUnsafe(Attr key) {
		if (key == Attr.RUN_STYLE) return Attr.RunStyle.SYNC;
		return super.scanUnsafe(key);
	}

	static final class ElementAtSubscriber<T> extends Operators.BaseFluxToMonoOperator<T, T> {
		@Nullable
		final T defaultValue;

		long index;

		final long target;

		boolean done;

		ElementAtSubscriber(CoreSubscriber<? super T> actual, long index, @Nullable T defaultValue) {
			super(actual);
			this.index = index;
			this.target = index;
			this.defaultValue = defaultValue;
		}

		@Override
		@Nullable
		public Object scanUnsafe(Attr key) {
			if (key == Attr.TERMINATED) return done;

			return super.scanUnsafe(key);
		}

		@Override
		public void onNext(T t) {
			if (done) {
				Operators.onNextDropped(t, actual.currentContext());
				return;
			}

			long i = index;
			if (i == 0) {
				done = true;
				s.cancel();

				actual.onNext(t);
				actual.onComplete();
				return;
			}
			index = i - 1;
			Operators.onDiscard(t, actual.currentContext()); //FIXME cache currentcontext
		}

		@Override
		public void onError(Throwable t) {
			if (done) {
				Operators.onErrorDropped(t, actual.currentContext());
				return;
			}
			done = true;

			actual.onError(t);
		}

		@Override
		public void onComplete() {
			if (done) {
				return;
			}
			done = true;

			final T dv = defaultValue;
			if (dv != null) {
				completePossiblyEmpty();
			}
			else{
				long count = target - index;
				actual.onError(Operators.onOperatorError(new IndexOutOfBoundsException(
								"source had " + count + " elements, expected at least " + (target + 1)),
						actual.currentContext()));
			}
		}

		@Override
		T accumulatedValue() {
			return defaultValue;
		}
	}
}
