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

package it.webappcommon.lib.jsf;

/**
 * <p>IOpzioni_pagineDAO interface.</p>
 *
 * @author mirco
 * @version $Id: $Id
 */
public interface IOpzioni_pagineDAO {

	/**
	 * <p>getOpzione_paginaBy.</p>
	 *
	 * @param idUtenteLoggato a int.
	 * @param nome_classe a {@link java.lang.String} object.
	 * @param chiave a {@link java.lang.String} object.
	 * @return a {@link it.webappcommon.lib.jsf.IOpzione_pagina} object.
	 */
	public IOpzione_pagina getOpzione_paginaBy(int idUtenteLoggato, String nome_classe, String chiave);
}
