/**********************************************************\
|                                                          |
|                          hprose                          |
|                                                          |
| Official WebSite: http://www.hprose.com/                 |
|                   http://www.hprose.org/                 |
|                                                          |
\**********************************************************/
/**********************************************************\
 *                                                        *
 * Thenable.java                                          *
 *                                                        *
 * Thenable interface for Java.                           *
 *                                                        *
 * LastModified: Jun 27, 2016                             *
 * Author: Ma Bingyao <andot@hprose.com>                  *
 *                                                        *
\**********************************************************/
package hprose.util.concurrent;

public interface Thenable<V> {
    Promise<?> then(Action<V> onfulfill, Action<Throwable> onreject);
    <R> Promise<R> then(Func<R, V> onfulfill, Func<R, Throwable> onreject);
    <R> Promise<R> then(AsyncFunc<R, V> onfulfill, Func<R, Throwable> onreject);
    <R> Promise<R> then(AsyncFunc<R, V> onfulfill, AsyncFunc<R, Throwable> onreject);
    <R> Promise<R> then(Func<R, V> onfulfill, AsyncFunc<R, Throwable> onreject);
}