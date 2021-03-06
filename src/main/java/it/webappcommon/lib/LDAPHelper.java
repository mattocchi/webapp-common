/*
/*
    Copyright (c) 2012,2018 Mirco Attocchi
	
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

/* 
 * http://java.html.it/articoli/leggi/2497/usare-ldap-con-java/5/
 * 
 */
package it.webappcommon.lib;

import java.util.*;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * <p>LDAPHelper class.</p>
 *
 * @author Mirco
 * @version $Id: $Id
 */
public class LDAPHelper {

	public class UserInfo implements Comparable<UserInfo> {

		private String tel;
		private String nomeCompleto;
		private String email;
		private String ufficio;
		private String account;
		private String gruppi;
		private String nome;
		private String cognome;
		private Map<String, String> attributes = new HashMap<String, String>();

		public String getTel() {
			return tel;
		}

		public void setTel(String tel) {
			this.tel = tel;
		}

		public String getNomeCompleto() {
			return nomeCompleto;
		}

		public void setNomeCompleto(String nome) {
			this.nomeCompleto = nome;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getUfficio() {
			return ufficio;
		}

		public void setUfficio(String ufficio) {
			this.ufficio = ufficio;
		}

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}

		public String getGruppi() {
			return gruppi;
		}

		public void setGruppi(String gruppi) {
			this.gruppi = gruppi;
		}

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		public String getCognome() {
			return cognome;
		}

		public void setCognome(String cognome) {
			this.cognome = cognome;
		}

		@Override
		public int compareTo(UserInfo o) {
			return this.getNomeCompleto().compareTo(o.getNomeCompleto());
		}

		public void addAttribute(String key, String value) {
			if (!attributes.containsKey(key))
				attributes.put(key, value);
		}

		public Map<String, String> getAttributes() {
			return attributes;
		}

		public void setAttributes(Map<String, String> attributes) {
			this.attributes = attributes;
		}
	}

	/** Constant <code>logger</code> */
	protected static Logger logger = Logger.getLogger(LDAPHelper.class.getName());
	/** Constant <code>FIELD_ACCOUNT_NAME="sAMAccountName"</code> */
	public static final String FIELD_ACCOUNT_NAME = "sAMAccountName";
	/** Constant <code>FIELD_EMAIL="mail"</code> */
	public static final String FIELD_EMAIL = "mail";
	/** Constant <code>FIELD_TEL="telephoneNumber"</code> */
	public static final String FIELD_TEL = "telephoneNumber";
	// public static final String FIELD_UFFICIO = "physicalDeliveryOfficeName";
	/** Constant <code>FIELD_UFFICIO="department"</code> */
	public static final String FIELD_UFFICIO = "department";
	/** Constant <code>FIELD_NOME_COMPLETO="cn"</code> */
	public static final String FIELD_NOME_COMPLETO = "cn";
	/** Constant <code>FIELD_NOME="givenName"</code> */
	public static final String FIELD_NOME = "givenName";
	/** Constant <code>FIELD_COGNOME="sn"</code> */
	public static final String FIELD_COGNOME = "sn";
	/** Constant <code>FIELD_NOME_VISUALIZZATO="displayName"</code> */
	public static final String FIELD_NOME_VISUALIZZATO = "displayName";
	/** Constant <code>FIELD_GROUPS="memberOf"</code> */
	public static final String FIELD_GROUPS = "memberOf";
	//
	private static final String INITIAL_CONTEXT = "com.sun.jndi.ldap.LdapCtxFactory";
	//
	private static final String FILTER_USERS_ACTIVE = "(&(objectCategory=user)(!(UserAccountControl:1.2.840.113556.1.4.803:=2)))";
	//
	private String server;
	private String port = "389";
	//
	private String loginDomain;
	private String loginUserName;
	private String loginPassword;

	private String areaWhereSearch;

	//
	/**
	 * <p>Constructor for LDAPHelper.</p>
	 *
	 * @param server a {@link java.lang.String} object.
	 * @param domain a {@link java.lang.String} object.
	 * @param userName a {@link java.lang.String} object.
	 * @param password a {@link java.lang.String} object.
	 * @param areaWhereSearch a {@link java.lang.String} object.
	 */
	public LDAPHelper(String server, String domain, String userName, String password, String areaWhereSearch) {
		this.server = server;
		this.loginDomain = domain;
		this.loginUserName = userName;
		this.loginPassword = password;
		this.areaWhereSearch = areaWhereSearch;
	}

	/**
	 * @param filter LDAP context filter
	 */
	private List<UserInfo> search(String filter) throws NamingException {
		DirContext ctx = null;
		SearchControls ctls = null;
		Properties env = new Properties();
		List<UserInfo> res = new ArrayList<UserInfo>();
		boolean trovatiRisultati = false;

		env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT);

		env.put(Context.PROVIDER_URL, "ldap://" + server + ":" + port);

		env.put(Context.SECURITY_AUTHENTICATION, "simple");

		if (org.apache.commons.lang3.StringUtils.isEmpty(loginDomain)) {
			env.put(Context.SECURITY_PRINCIPAL, loginUserName);
		} else {
			env.put(Context.SECURITY_PRINCIPAL, loginDomain + "\\" + loginUserName);
		}
		env.put(Context.SECURITY_CREDENTIALS, loginPassword);

		try {
			ctx = new InitialDirContext(env);

			ctls = new SearchControls();
			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			// String filter = "";
			// // filter = "(&(objectClass=inetOrgPerson)(objectClass=person))";
			// filter = FILTER_USERS_ACTIVE;

			// Tutti i membri di un gruppo
			// (objectCategory=user)(memberOf=CN=QA Users,OU=Help
			// Desk,DC=dpetri,DC=net)

			// ESEMPI
			// http://www.petri.co.il/ldap_search_samples_for_windows_2003_and_exchange.htm

			// Account disabled
			// (UserAccountControl:1.2.840.113556.1.4.803:=2)

			NamingEnumeration<SearchResult> answer = ctx.search(areaWhereSearch, filter, ctls);

			UserInfo userInfo = null;
			while (answer.hasMoreElements()) {
				trovatiRisultati = true;

				SearchResult a = answer.nextElement();
				// logger.debug(a.getNameInNamespace());

				Attributes result = a.getAttributes();

				if (result == null) {
					// System.out.print("Attributi non presenti");
				} else {
					NamingEnumeration<? extends Attribute> attributi = result.getAll();

					userInfo = new UserInfo();
					while (attributi.hasMoreElements()) {
						Attribute att = attributi.nextElement();
						// logger.debug(att.getID());

						String value = "";
						// for (NamingEnumeration vals = att.getAll();
						// vals.hasMoreElements(); logger.debug("\t" +
						// vals.nextElement()))
						// ;
						NamingEnumeration<?> vals = att.getAll();
						while (vals.hasMoreElements()) {
							Object val = vals.nextElement();

							// logger.debug("\t" + val);
							value = (value.isEmpty()) ? value + val.toString() : value + ";" + val.toString();
						}

						if (att.getID().equalsIgnoreCase(FIELD_ACCOUNT_NAME)) {
							userInfo.setAccount(value);
							userInfo.addAttribute(att.getID(), value);
						} else if (att.getID().equalsIgnoreCase(FIELD_COGNOME)) {
							userInfo.setCognome(value);
							userInfo.addAttribute(att.getID(), value);
						} else if (att.getID().equalsIgnoreCase(FIELD_EMAIL)) {
							userInfo.setEmail(value);
							userInfo.addAttribute(att.getID(), value);
						} else if (att.getID().equalsIgnoreCase(FIELD_GROUPS)) {
//							value = StringUtils.replace(value, "\u003d", "=");
//							value = StringEscapeUtils.unescapeJava(value);
							userInfo.setGruppi(value);
							userInfo.setGruppi(value);
							userInfo.addAttribute(att.getID(), value);
						} else if (att.getID().equalsIgnoreCase(FIELD_NOME)) {
							userInfo.setNome(value);
							userInfo.addAttribute(att.getID(), value);
						} else if (att.getID().equalsIgnoreCase(FIELD_NOME_COMPLETO)) {
							userInfo.setNomeCompleto(value);
							userInfo.addAttribute(att.getID(), value);
						} else if (att.getID().equalsIgnoreCase(FIELD_NOME_VISUALIZZATO)) {
							// userInfo.setNome(value);
//							userInfo.addAttribute(att.getID(), value);
						} else if (att.getID().equalsIgnoreCase(FIELD_TEL)) {
							userInfo.setTel(value);
							userInfo.addAttribute(att.getID(), value);
						} else if (att.getID().equalsIgnoreCase(FIELD_UFFICIO)) {
							userInfo.setUfficio(value);
							userInfo.addAttribute(att.getID(), value);
						}

//						if (fillAttributes) {
//							userInfo.addAttribute(att.getID(), value);
//						}
					}

					// Attribute attr = result.get("cn");
					// if (attr != null) {
					// logger.debug("cn:");
					// for (NamingEnumeration vals = attr.getAll();
					// vals.hasMoreElements(); logger.debug("\t" +
					// vals.nextElement()));
					// }
					//
					// attr = result.get("sn");
					// if (attr != null) {
					// logger.debug("sn:");
					// for (NamingEnumeration vals = attr.getAll();
					// vals.hasMoreElements(); logger.debug("\t" +
					// vals.nextElement()));
					// }
					//
					// attr = result.get("mail");
					// if (attr != null) {
					// logger.debug("mail:");
					// for (NamingEnumeration vals = attr.getAll();
					// vals.hasMoreElements(); logger.debug("\t" +
					// vals.nextElement()));
					// }
					//
					// // attr = result.get("uid");
					// // if (attr != null) {
					// // logger.debug("uid:");
					// // for (NamingEnumeration vals = attr.getAll();
					// vals.hasMoreElements(); logger.debug("\t" +
					// vals.nextElement()));
					// // }
					// //
					// // attr = result.get("userPassword");
					// // if (attr != null) {
					// // logger.debug("userPassword:");
					// // for (NamingEnumeration vals = attr.getAll();
					// vals.hasMoreElements(); logger.debug("\t" +
					// vals.nextElement()));
					// // }

					if (userInfo != null) {
						res.add(userInfo);
					}
				}
			}
		} catch (NamingException ne) {
			// ne.printStackTrace();
			logger.error(ne);
			throw ne;
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
				}
			} catch (Exception e) {
			}
		}

		// Azzero l'hash map
		if (!trovatiRisultati) {
			res = null;
		}

		return res;
	}

	/**
	 * Ricerca l'account specificato
	 *
	 * @param accountName a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 * @throws javax.naming.NamingException if any.
	 */
	public List<UserInfo> findActiveUserByAccountName(String accountName) throws NamingException {
		List<UserInfo> res = null;

		res = this.search("(&(" + FIELD_ACCOUNT_NAME + "=" + accountName + ")(" + FILTER_USERS_ACTIVE + "))");

		if (res != null) {
			// logger.debug(res.get(FIELD_ACCOUNT_NAME));
			// logger.debug(res.get(FIELD_NOME_COMPLETO));
			// logger.debug(res.get(FIELD_EMAIL));
			// logger.debug(res.get(FIELD_TEL));
			// logger.debug(res.get(FIELD_UFFICIO));
			// logger.debug(res.get(FIELD_NOME));
			// logger.debug(res.get(FIELD_COGNOME));
			// logger.debug(res.get(FIELD_GROUPS));
		}

		return res;
	}

	/**
	 * <p>findActiveUsers.</p>
	 *
	 * @return a {@link java.util.List} object.
	 * @throws javax.naming.NamingException if any.
	 */
	public List<UserInfo> findActiveUsers() throws NamingException {
		return findActiveUsers(null);
	}

	/**
	 * Ricerca il testo nei campi FIELD_NOME_COMPLETO, FIELD_EMAIL, FIELD_TEL,
	 * FIELD_UFFICIO
	 *
	 * @param filtroRicerca a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 * @throws javax.naming.NamingException if any.
	 */
	public List<UserInfo> findActiveUsers(String filtroRicerca) throws NamingException {
		List<UserInfo> res = null;
		String query = "";

		if (org.apache.commons.lang3.StringUtils.isEmpty(filtroRicerca)) {
			query = FILTER_USERS_ACTIVE;
		} else {
			query = "(&(|(" + FIELD_NOME_COMPLETO + "=*" + filtroRicerca + "*)(" + FIELD_EMAIL + "=*" + filtroRicerca + "*)(" + FIELD_TEL + "=*" + filtroRicerca + "*)(" + FIELD_UFFICIO + "=*" + filtroRicerca + "*))(" + FILTER_USERS_ACTIVE + "))";
		}

		res = this.search(query);

		// if (res != null) {
		// // logger.debug(res.get(FIELD_ACCOUNT_NAME));
		// // logger.debug(res.get(FIELD_NOME_COMPLETO));
		// // logger.debug(res.get(FIELD_EMAIL));
		// // logger.debug(res.get(FIELD_TEL));
		// // logger.debug(res.get(FIELD_UFFICIO));
		// // logger.debug(res.get(FIELD_NOME));
		// // logger.debug(res.get(FIELD_COGNOME));
		// // logger.debug(res.get(FIELD_GROUPS));
		// }

		return res;
	}

	/**
	 * <p>domainParser.</p>
	 *
	 * @param username a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String domainParser(String username) {
		String res = username;

		if (username.indexOf("\\") >= 0) {
			res = username.substring(username.indexOf("\\") + 1, username.length());
		}

		// if (username.toUpperCase().startsWith(domain.toUpperCase() + "\\")) {
		// res = username.toUpperCase().replaceFirst(domain.toUpperCase() +
		// "\\\\", "");
		// }

		return res;
	}
	
	
}
