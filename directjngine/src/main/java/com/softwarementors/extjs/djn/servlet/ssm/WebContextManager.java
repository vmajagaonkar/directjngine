/*
 * Copyright © 2008, 2012 Pedro Agulló Soliveres.
 * 
 * This file is part of DirectJNgine.
 *
 * DirectJNgine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * Commercial use is permitted to the extent that the code/component(s)
 * do NOT become part of another Open Source or Commercially developed
 * licensed development library or toolkit without explicit permission.
 *
 * DirectJNgine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DirectJNgine.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * This software uses the ExtJs library (http://extjs.com), which is 
 * distributed under the GPL v3 license (see http://extjs.com/license).
 */

package com.softwarementors.extjs.djn.servlet.ssm;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import edu.umd.cs.findbugs.annotations.NonNull;

public class WebContextManager {
  @NonNull private static ThreadLocal<WebContext> webContext = new ThreadLocal<WebContext>();
  
  public static WebContext get() {
    assert isWebContextAttachedToCurrentThread();
    
    return webContext.get();
  }

  public static void attachWebContextToCurrentThread( WebContext context ) {
    assert context != null;
    assert !isWebContextAttachedToCurrentThread();
    
    webContext.set( new WebContext(context));
  }
  
  public static void detachFromCurrentThread() {
    assert isWebContextAttachedToCurrentThread();

    webContext.get().close();
    webContext.remove();
  }
  
  public static WebContext  initializeWebContextForCurrentThread( HttpServlet servlet, HttpServletRequest request, HttpServletResponse response) {
    assert request != null;
    assert response != null;
    assert servlet != null;
    assert !isWebContextAttachedToCurrentThread();
    
    WebContext result = new WebContext(servlet, request, response );
    webContext.set( result );
    return result;
  }
  
  public static boolean isWebContextAttachedToCurrentThread() {
    WebContext context = webContext.get(); 
    return context != null;
  }
  
}
