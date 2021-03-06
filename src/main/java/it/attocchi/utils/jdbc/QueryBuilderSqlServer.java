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

package it.attocchi.utils.jdbc;

import it.attocchi.utils.ListUtils;
import it.webappcommon.lib.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>QueryBuilderSqlServer class.</p>
 *
 * @author mirco
 * @version $Id: $Id
 */
public class QueryBuilderSqlServer {

	private static final String RICERCA_JOLLY_CHAR = "*";
	private static final String RICERCA_STRING_CHAR = "\"";

	/**
	 * 1 = 1
	 */
	public static final String ALWAYS_TRUE = " 1 = 1 "; // Fede dice che e' piu'
														// veloce di TRUE
	/**
	 * FALSE
	 */
	public static final String ALWAYS_FALSE = " 1 = 2 ";

	private static final String PREFIX_FROM = "FROM:";
	private static final String PREFIX_TO = "TO:";

	/**
	 * <p>encodeStringSQL.</p>
	 *
	 * @param aString a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String encodeStringSQL(String aString) {
		return aString.replace("'", "''");
	}

	/**
	 * <p>equal.</p>
	 *
	 * @param nomeCampo a {@link java.lang.String} object.
	 * @param valore a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String equal(String nomeCampo, String valore) {
		StringBuilder equalBulder = new StringBuilder();

		equalBulder.append("( " + nomeCampo + " = '" + encodeStringSQL(valore) + "' )");

		return equalBulder.toString();
	}

	/**
	 * <p>equal.</p>
	 *
	 * @param nomeCampo a {@link java.lang.String} object.
	 * @param valore a int.
	 * @return a {@link java.lang.String} object.
	 */
	public static String equal(String nomeCampo, int valore) {
		StringBuilder equalBulder = new StringBuilder();

		equalBulder.append("( " + nomeCampo + " = " + String.valueOf(valore) + " )");

		return equalBulder.toString();
	}

	/**
	 * <p>equal.</p>
	 *
	 * @param nomeCampo a {@link java.lang.String} object.
	 * @param valore a boolean.
	 * @return a {@link java.lang.String} object.
	 */
	public static String equal(String nomeCampo, boolean valore) {
		StringBuilder equalBulder = new StringBuilder();

		if (valore) {
			equalBulder.append("( " + nomeCampo + " = 1 )");
		} else {
			equalBulder.append("( " + nomeCampo + " = 0 )");
		}

		return equalBulder.toString();
	}

	/**
	 * Verifica che ANNO MESE e GIORNO della DATA del CAMPO corrispondano
	 *
	 * @param nomeCampo a {@link java.lang.String} object.
	 * @param valore a {@link java.util.Date} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String equal(String nomeCampo, Date valore) {
		StringBuilder equalBulder = new StringBuilder();

		int anno = DateUtils.getAnno(valore);
		int mese = DateUtils.getMese(valore);
		int giorno = DateUtils.getGiorno(valore);

		equalBulder.append("(");
		equalBulder.append(" YEAR(" + nomeCampo + ") = " + anno + " AND");
		equalBulder.append(" MONTH(" + nomeCampo + ") = " + mese + " AND");
		equalBulder.append(" DAY(" + nomeCampo + ") = " + giorno + " ");
		equalBulder.append(")");

		return equalBulder.toString();
	}

	/**
	 * <p>betweenDate.</p>
	 *
	 * @param nomeCampo a {@link java.lang.String} object.
	 * @param start a {@link java.util.Date} object.
	 * @param end a {@link java.util.Date} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String betweenDate(String nomeCampo, Date start, Date end) {
		StringBuilder equalBulder = new StringBuilder();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		equalBulder.append("(");
		// equalBulder.append(" YEAR(" + nomeCampo + ") >= " + anno + " AND " +
		// " YEAR(" + nomeCampo + ") <= " + anno);
		// equalBulder.append(" MONTH(" + nomeCampo + ") >= " + mese + " AND" +
		// " MONTH(" + nomeCampo + ") <= " + mese);
		// equalBulder.append(" DAY(" + nomeCampo + ") >= " + giorno + " AND " +
		// " DAY(" + nomeCampo + ") <= " + giorno);
		equalBulder.append(" " + nomeCampo + " BETWEEN '" + sdf.format(start) + "' AND '" + sdf.format(end) + "' ");
		equalBulder.append(")");

		return equalBulder.toString();
	}

	/**
	 * <p>bigger.</p>
	 *
	 * @param nomeCampo a {@link java.lang.String} object.
	 * @param valore a {@link java.util.Date} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String bigger(String nomeCampo, Date valore) {
		StringBuilder equalBulder = new StringBuilder();

		int anno = DateUtils.getAnno(valore);
		int mese = DateUtils.getMese(valore);
		int giorno = DateUtils.getGiorno(valore);

		equalBulder.append("(");
		equalBulder.append(" YEAR(" + nomeCampo + ") >= " + anno + " AND");
		equalBulder.append(" MONTH(" + nomeCampo + ") >= " + mese + " AND");
		equalBulder.append(" DAY(" + nomeCampo + ") >= " + giorno + " ");
		equalBulder.append(")");

		return equalBulder.toString();
	}

	/**
	 * <p>greater.</p>
	 *
	 * @param nomeCampo a {@link java.lang.String} object.
	 * @param valore a int.
	 * @return a {@link java.lang.String} object.
	 */
	public static String greater(String nomeCampo, int valore) {
		StringBuilder equalBulder = new StringBuilder();

		equalBulder.append("(");
		equalBulder.append(" " + nomeCampo + " > " + valore + " ");
		equalBulder.append(")");

		return equalBulder.toString();
	}

	/**
	 * <p>greaterOrEqual.</p>
	 *
	 * @param nomeCampo a {@link java.lang.String} object.
	 * @param valore a int.
	 * @return a {@link java.lang.String} object.
	 */
	public static String greaterOrEqual(String nomeCampo, int valore) {
		StringBuilder equalBulder = new StringBuilder();

		equalBulder.append("(");
		equalBulder.append(" " + nomeCampo + " >= " + valore + " ");
		equalBulder.append(")");

		return equalBulder.toString();
	}

	/**
	 * <p>less.</p>
	 *
	 * @param nomeCampo a {@link java.lang.String} object.
	 * @param valore a int.
	 * @return a {@link java.lang.String} object.
	 */
	public static String less(String nomeCampo, int valore) {
		StringBuilder equalBulder = new StringBuilder();

		equalBulder.append("(");
		equalBulder.append(" " + nomeCampo + " < " + valore + " ");
		equalBulder.append(")");

		return equalBulder.toString();
	}

	/**
	 * <p>lessOrEqual.</p>
	 *
	 * @param nomeCampo a {@link java.lang.String} object.
	 * @param valore a int.
	 * @return a {@link java.lang.String} object.
	 */
	public static String lessOrEqual(String nomeCampo, int valore) {
		StringBuilder equalBulder = new StringBuilder();

		equalBulder.append("(");
		equalBulder.append(" " + nomeCampo + " <= " + valore + " ");
		equalBulder.append(")");

		return equalBulder.toString();
	}

	/**
	 * <p>lower.</p>
	 *
	 * @param nomeCampo a {@link java.lang.String} object.
	 * @param valore a {@link java.util.Date} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String lower(String nomeCampo, Date valore) {
		StringBuilder equalBulder = new StringBuilder();

		int anno = DateUtils.getAnno(valore);
		int mese = DateUtils.getMese(valore);
		int giorno = DateUtils.getGiorno(valore);

		equalBulder.append("(");
		equalBulder.append(" YEAR(" + nomeCampo + ") <= " + anno + " AND");
		equalBulder.append(" MONTH(" + nomeCampo + ") <= " + mese + " AND");
		equalBulder.append(" DAY(" + nomeCampo + ") <= " + giorno + " ");
		equalBulder.append(")");

		return equalBulder.toString();
	}

	/**
	 * <p>between.</p>
	 *
	 * @param nomeCampo a {@link java.lang.String} object.
	 * @param dataStart a {@link java.util.Date} object.
	 * @param dataEnd a {@link java.util.Date} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String between(String nomeCampo, Date dataStart, Date dataEnd) {
		StringBuilder equalBulder = new StringBuilder();

		if (dataStart != null && dataEnd != null) {
			equalBulder.append("(");
			// equalBulder.append(bigger(nomeCampo, dataStart) + " AND ");
			// equalBulder.append(lower(nomeCampo, dataEnd));
			equalBulder.append(betweenDate(nomeCampo, dataStart, dataEnd));
			equalBulder.append(")");
		} else if (dataStart != null) {
			equalBulder.append("(");
			equalBulder.append(bigger(nomeCampo, dataStart));
			equalBulder.append(")");
		} else if (dataEnd != null) {
			equalBulder.append("(");
			equalBulder.append(lower(nomeCampo, dataStart));
			equalBulder.append(")");
		}

		return equalBulder.toString();
	}

	/**
	 * <p>isNotNull.</p>
	 *
	 * @param nomeCampo a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String isNotNull(String nomeCampo) {
		StringBuilder equalBulder = new StringBuilder();

		equalBulder.append("( " + nomeCampo + " IS NOT NULL )");

		return equalBulder.toString();
	}

	/**
	 * <p>isNull.</p>
	 *
	 * @param nomeCampo a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String isNull(String nomeCampo) {
		StringBuilder equalBulder = new StringBuilder();

		equalBulder.append("( " + nomeCampo + " IS NULL )");

		return equalBulder.toString();
	}

	/**
	 * Stesso metodo del like, ma su un singolo campo
	 *
	 * @param semeRicerca a {@link java.lang.String} object.
	 * @param field a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String like(String semeRicerca, String field) {
		return like(semeRicerca, new String[] { field });
	}

	/**
	 * Costruisce una stringa da usare nelle query come fosse un like ma nel
	 * caso di seme di ricerca con piu' parole riesce ad evitare che l'ordine
	 * debba essere corretto
	 *
	 * @param semeRicerca a {@link java.lang.String} object.
	 * @param fields an array of {@link java.lang.String} objects.
	 * @return a {@link java.lang.String} object.
	 */
	public static String like(String semeRicerca, String[] fields) {
		StringBuilder likeBulder = new StringBuilder();
		String[] words = semeRicerca.split(" ");

		/*
		 * Gestisco la Ricerca "Mircosoft .Net*" come fosse una unica parola
		 * anziche' spezzare in tante piccole parole altrimenti cerca su un
		 * campo %Microsoft% AND .Net% e non lo trovera' mai
		 */
		if (semeRicerca.endsWith(QueryBuilderSqlServer.RICERCA_JOLLY_CHAR) || semeRicerca.startsWith(QueryBuilderSqlServer.RICERCA_JOLLY_CHAR) || (semeRicerca.startsWith(QueryBuilderSqlServer.RICERCA_STRING_CHAR) && semeRicerca.endsWith(QueryBuilderSqlServer.RICERCA_STRING_CHAR))) {

			// /* Caso "ciao" */
			// if
			// (semeRicerca.startsWith(QueryBuilderSqlServer.RICERCA_STRING_CHAR)
			// &&
			// semeRicerca.endsWith(QueryBuilderSqlServer.RICERCA_STRING_CHAR))
			// {
			// semeRicerca = StringUtils.removeStart(semeRicerca,
			// QueryBuilderSqlServer.RICERCA_STRING_CHAR);
			// semeRicerca = StringUtils.removeEnd(semeRicerca,
			// QueryBuilderSqlServer.RICERCA_STRING_CHAR);
			// }

			words = new String[] { semeRicerca };

		} else {
			words = semeRicerca.split(" ");
		}

		if (fields.length > 0 && words.length > 0) {

			likeBulder.append("( " + ALWAYS_FALSE);
			for (int i = 0; i < fields.length; i++) {

				likeBulder.append(" OR ( " + ALWAYS_TRUE);
				for (int j = 0; j < words.length; j++) {
					likeBulder.append(" AND ");
					// likeBulder.append(fields[i] + " LIKE '%" +
					// encode(words[j]) + "%'");
					likeBulder.append(likeSimple(fields[i], words[j]));
				}
				likeBulder.append(")");
			}
			likeBulder.append(")");
		}
		return likeBulder.toString();

	}

	/**
	 * Supporta la ricerca str* *str str che corrisponde a like %%
	 *
	 * @param campo a {@link java.lang.String} object.
	 * @param semeRicerca a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String likeSimple(String campo, String semeRicerca) {
		StringBuilder res = new StringBuilder();

		if (semeRicerca.startsWith(QueryBuilderSqlServer.RICERCA_STRING_CHAR) && semeRicerca.endsWith(QueryBuilderSqlServer.RICERCA_STRING_CHAR)) {
			/* CASO "ciao" */
			semeRicerca = StringUtils.removeStart(semeRicerca, QueryBuilderSqlServer.RICERCA_STRING_CHAR);
			semeRicerca = StringUtils.removeEnd(semeRicerca, QueryBuilderSqlServer.RICERCA_STRING_CHAR);

			res.append(campo + " = '" + encodeStringSQL(semeRicerca) + "'");
		} else if (semeRicerca.endsWith(QueryBuilderSqlServer.RICERCA_JOLLY_CHAR)) {
			/* CASO ciao* */
			semeRicerca = semeRicerca.replace(QueryBuilderSqlServer.RICERCA_JOLLY_CHAR, "");

			res.append(campo + " LIKE '" + encodeStringSQL(semeRicerca) + "%'");
		} else if (semeRicerca.startsWith(QueryBuilderSqlServer.RICERCA_JOLLY_CHAR)) {
			/* CASO *ciao */
			semeRicerca = semeRicerca.replace(QueryBuilderSqlServer.RICERCA_JOLLY_CHAR, "");

			res.append(campo + " LIKE '%" + encodeStringSQL(semeRicerca) + "'");
		} else {
			/* CASO ciao */
			res.append(campo + " LIKE '%" + encodeStringSQL(semeRicerca) + "%'");
		}

		return res.toString();
	}

	/**
	 * ciao esegue ricerca = ciao* esegue ricerca ciao% *ciao esegue ricerca
	 * %ciao *ciao* esegue ricerca %ciao%
	 *
	 * @param campo a {@link java.lang.String} object.
	 * @param semeRicerca a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String likeOrEquaByUser(String campo, String semeRicerca) {
		StringBuilder res = new StringBuilder();

		if (semeRicerca.startsWith(QueryBuilderSqlServer.RICERCA_JOLLY_CHAR) && semeRicerca.endsWith(QueryBuilderSqlServer.RICERCA_JOLLY_CHAR)) {
			/* CASO "ciao" */
			semeRicerca = StringUtils.removeStart(semeRicerca, QueryBuilderSqlServer.RICERCA_JOLLY_CHAR);
			semeRicerca = StringUtils.removeEnd(semeRicerca, QueryBuilderSqlServer.RICERCA_JOLLY_CHAR);
			res.append(campo + " LIKE '%" + encodeStringSQL(semeRicerca) + "%'");
		} else if (semeRicerca.endsWith(QueryBuilderSqlServer.RICERCA_JOLLY_CHAR)) {
			/* CASO ciao* */
			semeRicerca = semeRicerca.replace(QueryBuilderSqlServer.RICERCA_JOLLY_CHAR, "");
			res.append(campo + " LIKE '" + encodeStringSQL(semeRicerca) + "%'");
		} else if (semeRicerca.startsWith(QueryBuilderSqlServer.RICERCA_JOLLY_CHAR)) {
			/* CASO *ciao */
			semeRicerca = semeRicerca.replace(QueryBuilderSqlServer.RICERCA_JOLLY_CHAR, "");
			res.append(campo + " LIKE '%" + encodeStringSQL(semeRicerca) + "'");
		} else {
			/* CASO ciao */
			res.append(campo + " = '" + encodeStringSQL(semeRicerca) + "'");
		}

		return res.toString();
	}

	/**
	 * <p>likeOrEquaByUser2.</p>
	 *
	 * @param campo a {@link java.lang.String} object.
	 * @param semeRicerca a {@link java.lang.String} object.
	 * @param multipleValuesSeparator a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String likeOrEquaByUser2(String campo, String semeRicerca, String multipleValuesSeparator) {
		StringBuilder res = new StringBuilder();

		if (semeRicerca.startsWith(QueryBuilderSqlServer.RICERCA_JOLLY_CHAR) && semeRicerca.endsWith(QueryBuilderSqlServer.RICERCA_JOLLY_CHAR)) {
			/* CASO *ciao* */
			semeRicerca = StringUtils.removeStart(semeRicerca, QueryBuilderSqlServer.RICERCA_JOLLY_CHAR);
			semeRicerca = StringUtils.removeEnd(semeRicerca, QueryBuilderSqlServer.RICERCA_JOLLY_CHAR);
			res.append(campo + " LIKE '%" + multipleValuesSeparator + "%" + encodeStringSQL(semeRicerca) + "%" + multipleValuesSeparator + "%'");
		} else if (semeRicerca.endsWith(QueryBuilderSqlServer.RICERCA_JOLLY_CHAR)) {
			/* CERCO ciao* quindi LIKE '%;%CIAO;%' */
			semeRicerca = StringUtils.removeEnd(semeRicerca, QueryBuilderSqlServer.RICERCA_JOLLY_CHAR);
			res.append(campo + " LIKE '%" + multipleValuesSeparator + encodeStringSQL(semeRicerca) + "%" + multipleValuesSeparator + "%'");
		} else if (semeRicerca.startsWith(QueryBuilderSqlServer.RICERCA_JOLLY_CHAR)) {
			/* CERCO *ciao quindi LIKE '%;%CIAO;%' */
			semeRicerca = StringUtils.removeStart(semeRicerca, QueryBuilderSqlServer.RICERCA_JOLLY_CHAR);
			res.append(campo + " LIKE '%" + multipleValuesSeparator + "%" + encodeStringSQL(semeRicerca) + multipleValuesSeparator + "%'");
		} else {
			/* CERCO ciao quindi LIKE '%;ciao;%' */
			res.append(campo + " LIKE '%" + multipleValuesSeparator + encodeStringSQL(semeRicerca) + multipleValuesSeparator + "%'");
		}

		return res.toString();
	}

	/**
	 * <p>isAdvancedSearchCommand.</p>
	 *
	 * @param semeRicerca a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public static boolean isAdvancedSearchCommand(String semeRicerca) {
		boolean res = false;

		res = (semeRicerca.toUpperCase().startsWith(PREFIX_FROM) || semeRicerca.toUpperCase().startsWith(PREFIX_TO));

		return res;
	}

	/**
	 * <p>buildUserCommandSearch.</p>
	 *
	 * @param semeRicerca a {@link java.lang.String} object.
	 * @param fromField a {@link java.lang.String} object.
	 * @param toField a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String buildUserCommandSearch(String semeRicerca, String fromField, String toField) {
		StringBuilder res = new StringBuilder();

		if (semeRicerca.toUpperCase().startsWith(PREFIX_FROM)) {
			res.append(likeSimple(fromField, StringUtils.removeStart(semeRicerca, PREFIX_FROM)));
		} else if (semeRicerca.toUpperCase().startsWith(PREFIX_TO)) {
			res.append(likeSimple(toField, StringUtils.removeStart(semeRicerca, PREFIX_TO)));
		}

		return res.toString();
	}

	/**
	 * <p>inListBuilder.</p>
	 *
	 * @param field a {@link java.lang.String} object.
	 * @param listaID a {@link java.util.List} object.
	 * @param asStrings a boolean.
	 * @return a {@link java.lang.String} object.
	 */
	public static String inListBuilder(String field, List<String> listaID, boolean asStrings) {
		StringBuilder res = new StringBuilder();

		res.append("( " + ALWAYS_FALSE + " ");

		if (ListUtils.isNotEmpty(listaID)) {
			// res.append("(");
			for (String id : listaID) {

				if (res.length() > 0) {
					res.append(" OR ");
				}

				if (asStrings)
					res.append(field + " = '" + encodeStringSQL(id) + "'");
				else
					res.append(field + " = " + id);
			}
			// res.append(")");
		}

		// if (res.length() > 0) {
		// res.insert(0, "(");
		// res.append(")");
		// }
		res.append(")");

		return res.toString();
	}

	/**
	 * Ritorna una composizione del tipo "(a = 1 OR a = 2)"
	 *
	 * @param field a {@link java.lang.String} object.
	 * @param listaID a {@link java.util.List} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String inListBuilder(String field, List<Integer> listaID) {
		StringBuilder res = new StringBuilder();

		res.append("( " + ALWAYS_FALSE + " ");

		if (ListUtils.isNotEmpty(listaID)) {
			// res.append("(");
			for (Integer id : listaID) {

				if (res.length() > 0) {
					res.append(" OR ");
				}

				res.append(field + " = " + id);
			}
			// res.append(")");
		}

		// if (res.length() > 0) {
		// res.insert(0, "(");
		// res.append(")");
		// }
		res.append(")");

		return res.toString();
	}
}
