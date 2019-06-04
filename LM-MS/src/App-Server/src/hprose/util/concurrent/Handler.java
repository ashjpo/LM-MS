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
 * Handler.java                                           *
 *                                                        *
 * Handler interface for Java.                            *
 *                                                        *
 * LastModified: Apr 11, 2016                             *
 * Author: Ma Bingyao <andot@hprose.com>                  *
 *                                                        *
\**********************************************************/
package hprose.util.concurrent;


public interface Handler<R, T> {
    R call(T element, int index) throws Throwable;
}
