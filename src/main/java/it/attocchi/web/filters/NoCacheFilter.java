/*
    Copyright (c) 2012,2013 Mirco Attocchi
	
    This file is part of WebAppCommon.

    WebAppCommon is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    WebAppCommon is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with WebAppCommon.  If not, see <http://www.gnu.org/licenses/>.
 */

package it.attocchi.web.filters;

import java.io.IOException;

import javax.faces.application.ResourceHandler;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/*
 * Usage:
 * 
 * 	
 <!-- NoCacheFilter -->
 <filter>
 <display-name>NoCacheFilter</display-name>
 <filter-name>NoCacheFilter</filter-name>
 <filter-class>it.attocchi.web.filters.NoCacheFilter</filter-class>
 </filter>
 <filter-mapping>
 <filter-name>NoCacheFilter</filter-name>
 <servlet-name>Faces Servlet</servlet-name>
 </filter-mapping>
 <!-- NoCacheFilter -->
 */
/**
 * <p>NoCacheFilter class.</p>
 *
 * @author mirco
 * @version $Id: $Id
 */
public class NoCacheFilter implements Filter {

	protected final Logger logger = Logger.getLogger(this.getClass().getName());

	/** {@inheritDoc} */
	@Override
	public void destroy() {

	}

	/** {@inheritDoc} */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		/*
		 * Skip JSF resources (CSS/JS/Images/etc)
		 */
		if (!req.getRequestURI().startsWith(req.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER)) {

			/* HTTP 1.1. */
			res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			/* HTTP 1.0. */
			res.setHeader("Pragma", "no-cache");
			/* Proxies. */
			res.setDateHeader("Expires", 0);
		}

		chain.doFilter(request, response);
	}

	/** {@inheritDoc} */
	@Override
	public void init(FilterConfig config) throws ServletException {

	}

}
