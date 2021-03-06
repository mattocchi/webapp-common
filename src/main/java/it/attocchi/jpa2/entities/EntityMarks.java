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

package it.attocchi.jpa2.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * <p>EntityMarks class.</p>
 *
 * @author mirco
 * @version $Id: $Id
 */
@Embeddable
public class EntityMarks implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "data_creazione")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date dataCreazione;

	@Column(name = "data_modifica")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date dataModifica;

	@Column(name = "data_cancellazione")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date dataCancellazione;

	@Column(name = "utente_creazione_id")
	protected long utenteCreazioneId;

	@Column(name = "utente_modifica_id")
	protected long utenteModificaId;

	@Column(name = "utente_cancellazione_id")
	protected long utenteCancellazioneId;

	/**
	 * <p>Getter for the field <code>dataCreazione</code>.</p>
	 *
	 * @return a {@link java.util.Date} object.
	 */
	public Date getDataCreazione() {
		return dataCreazione;
	}

	/**
	 * <p>Setter for the field <code>dataCreazione</code>.</p>
	 *
	 * @param dataCreazione a {@link java.util.Date} object.
	 */
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	/**
	 * <p>Getter for the field <code>dataModifica</code>.</p>
	 *
	 * @return a {@link java.util.Date} object.
	 */
	public Date getDataModifica() {
		return dataModifica;
	}

	/**
	 * <p>Setter for the field <code>dataModifica</code>.</p>
	 *
	 * @param dataModifica a {@link java.util.Date} object.
	 */
	public void setDataModifica(Date dataModifica) {
		this.dataModifica = dataModifica;
	}

	/**
	 * <p>Getter for the field <code>dataCancellazione</code>.</p>
	 *
	 * @return a {@link java.util.Date} object.
	 */
	public Date getDataCancellazione() {
		return dataCancellazione;
	}

	/**
	 * <p>Setter for the field <code>dataCancellazione</code>.</p>
	 *
	 * @param dataCancellazione a {@link java.util.Date} object.
	 */
	public void setDataCancellazione(Date dataCancellazione) {
		this.dataCancellazione = dataCancellazione;
	}

	/**
	 * <p>Getter for the field <code>utenteCreazioneId</code>.</p>
	 *
	 * @return a long.
	 */
	public long getUtenteCreazioneId() {
		return utenteCreazioneId;
	}

	/**
	 * <p>Setter for the field <code>utenteCreazioneId</code>.</p>
	 *
	 * @param utenteCreazioneId a long.
	 */
	public void setUtenteCreazioneId(long utenteCreazioneId) {
		this.utenteCreazioneId = utenteCreazioneId;
	}

	/**
	 * <p>Getter for the field <code>utenteModificaId</code>.</p>
	 *
	 * @return a long.
	 */
	public long getUtenteModificaId() {
		return utenteModificaId;
	}

	/**
	 * <p>Setter for the field <code>utenteModificaId</code>.</p>
	 *
	 * @param utenteModificaId a long.
	 */
	public void setUtenteModificaId(long utenteModificaId) {
		this.utenteModificaId = utenteModificaId;
	}

	/**
	 * <p>Getter for the field <code>utenteCancellazioneId</code>.</p>
	 *
	 * @return a long.
	 */
	public long getUtenteCancellazioneId() {
		return utenteCancellazioneId;
	}

	/**
	 * <p>Setter for the field <code>utenteCancellazioneId</code>.</p>
	 *
	 * @param utenteCancellazioneId a long.
	 */
	public void setUtenteCancellazioneId(long utenteCancellazioneId) {
		this.utenteCancellazioneId = utenteCancellazioneId;
	}

	/**
	 * <p>deepClone.</p>
	 *
	 * @return a {@link it.attocchi.jpa2.entities.EntityMarks} object.
	 */
	@Deprecated
	public EntityMarks deepClone() {

		EntityMarks copy = new EntityMarks();
		if (this.dataCreazione != null)
			copy.setDataCreazione((Date) this.dataCreazione.clone());
		if (this.dataModifica != null)
			copy.setDataModifica((Date) this.dataModifica.clone());
		if (this.dataCancellazione != null)
			copy.setDataCancellazione((Date) this.dataCancellazione.clone());

		copy.setUtenteCreazioneId(this.utenteCreazioneId);
		copy.setUtenteModificaId(this.utenteModificaId);
		copy.setUtenteModificaId(this.utenteModificaId);

		return copy;
	}
}
