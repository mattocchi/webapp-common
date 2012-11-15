package it.espressoft.jsf2;

import org.apache.commons.lang3.StringUtils;

import it.espressoft.web.filters.AuthFilter;

/**
 * Pagina Gestione Utenti Autenticati (Richiede filtro @AuthFilter)
 * 
 * @author Mirco
 * 
 */
public abstract class PageBaseAuth extends PageBaseNoAuth {

	private boolean debug = false;
	
	// @PostConstruct
	// private void postConstruct() {
	// try {
	//
	// if (getIdUtenteLoggato() > 0) {
	// init();
	// } else {
	//
	// setErrorMessage("Necessaria Autenticazione");
	// }
	//
	// } catch (Exception ex) {
	// // logger.error("postConstruct", ex);
	// setErrorMessage("postConstruct", ex);
	// }
	// }

	@Override
	protected void preInit() throws Exception {
		if (getIdUtenteLoggato() > 0) {

			// Login e' stato fatto

		} else {
			logger.error("Necessaria Autenticazione");
			// setErrorMessage("Necessaria Autenticazione");
			throw new Exception("Necessaria Autenticazione");
		}
	}

	protected abstract void init() throws Exception;

	/*
	 * Gestione Autorizzazione Utente in Sessione
	 */
	private int idUtenteLoggato;

	public int getIdUtenteLoggato() {

		idUtenteLoggato = getSessionObjectAsInt(AuthFilter.PARAM_AUTH);

		if (idUtenteLoggato <= 0) {
			int param = getParamObjectAsInt(AuthFilter.PARAM_AUTH);
			
			if (param == 0 && debug) {
				logger.warn("auth debug mode");
				param = 1;
				addWarnMessage("auth debug mode", "");
			}
			
			if (param > 0) {
				idUtenteLoggato = param;
				setSessionObject(AuthFilter.PARAM_AUTH, idUtenteLoggato);
			}
		}

		return idUtenteLoggato;
	}

	public boolean isUtenteLoggato() {
		return getIdUtenteLoggato() > 0;
	}
}