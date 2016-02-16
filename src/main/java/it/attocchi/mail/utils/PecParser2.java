package it.attocchi.mail.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.ParseException;

import org.apache.log4j.Logger;

public class PecParser2 {

	protected static final Logger logger = Logger.getLogger(PecParser2.class.getName());

	int level = 0;
	boolean showStructure = false;
	boolean saveAttachments = true;
	int attnum = 1;
	static String indentStr = "                                               ";
	// String emlFileName = "";
	// boolean emlFound = false;
	// File f;
	// EmailBody testo;

	Map<String, DataHandler> attachments = new HashMap<String, DataHandler>();
	
//	public EmailBody getTesto() {
//		return testo;
//	}

	/**
	 * Ritorna una lista degli allegati all'email.
	 * Gli allegati devono essere specificati come javax.mail.Part.ATTACHMENT altrimenti non vengono considerati
	 * Nel caso di nomi doppi, oppure dello stesso allegato contenuto in un eml allegato il nome viene generato con incluso livello o progessivo
	 * @return
	 */
	public Map<String, DataHandler> getAttachments() {
		return attachments;
	}

	
	public PecParser2() {
		super();
		// String emlFileName, boolean saveFile, File f
//		this.emlFileName = emlFileName;
//		this.saveAttachments = saveFile;
//		this.f = f;
	}

	/*
	 * 
	 */
	public void dumpPart(Part part) throws Exception {
		if (part instanceof Message)
			dumpEnvelope((Message) part);

		/**
		 * Dump input stream ..
		 * 
		 * InputStream is = p.getInputStream(); // If "is" is not already
		 * buffered, wrap a BufferedInputStream // around it. if (!(is
		 * instanceof BufferedInputStream)) is = new BufferedInputStream(is);
		 * int c; while ((c = is.read()) != -1) System.out.write(c);
		 **/

		String ct = part.getContentType();
		try {
			log("CONTENT-TYPE: " + (new ContentType(ct)).toString());
		} catch (ParseException pex) {
			log("BAD CONTENT-TYPE: " + ct);
		}
		String filename = part.getFileName();
		if (filename != null)
			log("FILENAME: " + filename);

		/*
		 * Using isMimeType to determine the content type avoids fetching the
		 * actual content data until we need it.
		 */
		// if (p.isMimeType("text/plain")) {
		// if (p.isMimeType("text/*")) {
		if (part.isMimeType("text/plain") || part.isMimeType("text/html")) {
			log("This is plain text");
			log("---------------------------");
			if (!showStructure && !saveAttachments)
				logger.debug((String) part.getContent());

//			if (emlFound) {
//				testo = new EmailBody((String) part.getContent(), part.getContentType());
//				return;
//			}

		} else if (part.isMimeType("multipart/*")) {
			log("This is a Multipart");
			log("---------------------------");
			Multipart mp = (Multipart) part.getContent();
			level++;
			int count = mp.getCount();
			for (int i = 0; i < count; i++) {
				dumpPart(mp.getBodyPart(i));
//				/*
//				 * Blocco se ne ho trovato il testo (per evitare di assegnare il
//				 * contenuto di eventuali allegati txt
//				 */
//				if (testo != null)
//					break;
			}
			level--;
		} else if (part.isMimeType("message/rfc822")) {
			log("This is a Nested Message");
			log("---------------------------");
			level++;

//			if (filename.equals(emlFileName)) {
//				saveAttachment((Part) part.getContent(), filename);
//				emlFound = true;
//			}

			dumpPart((Part) part.getContent());

//			if (emlFound)
//				return;

			level--;
		} else {
			if (!showStructure && !saveAttachments) {
				/*
				 * If we actually want to see the data, and it's not a MIME type
				 * we know, fetch it and check its Java type.
				 */
				Object o = part.getContent();
				if (o instanceof String) {
					log("This is a string");
					log("---------------------------");
					logger.debug((String) o);
//					if (emlFound) {
//						testo = new EmailBody((String) part.getContent(), part.getContentType());
//						return;
//					}

				} else if (o instanceof InputStream) {
					log("This is just an input stream");
					log("---------------------------");
					InputStream is = (InputStream) o;
					int c;
					while ((c = is.read()) != -1) {
						System.out.write(c);
					}
				} else {
					log("This is an unknown type");
					log("---------------------------");
					log(o.toString());
//					if (emlFound) {
//						testo = new EmailBody(o.toString(), part.getContentType());
//						return;
//					}
				}
			} else {
				// just a separator
				log("---------------------------");
			}
		}

		/*
		 * If we're saving attachments, write out anything that looks like an
		 * attachment into an appropriately named file. Don't overwrite existing
		 * files to prevent mistakes.
		 */
		if (saveAttachments && level != 0 && part instanceof MimeBodyPart && !part.isMimeType("multipart/*")) {
			String disp = part.getDisposition();
			// many mailers don't include a Content-Disposition
			// if (disp == null || disp.equalsIgnoreCase(Part.ATTACHMENT)) {
			if (disp != null && disp.equalsIgnoreCase(Part.ATTACHMENT)) {
				if (filename == null)
					filename = "Attachment" + attnum++;
				MimeUtility.decodeText(filename);
				log("Saving attachment to file " + filename);
				try {
					File f = new File(filename);
					if (f.exists())
						// XXX - could try a series of names
						throw new IOException("file exists");
					// ((MimeBodyPart) part).saveFile(f);
					// attachments.add(((MimeBodyPart) part).getDataHandler());
					/* alcuni allegati potrebbero avere lo stesso nome, nel caso di eml annidati, quindi concateno un tocken univoco per livello*/
					int count = 0;
					while (attachments.containsKey(filename)) {
						String ext = FileNameUtils.getFileExtension(filename, true);
						String filenameNoExt = FileNameUtils.getFileNameWithOutExtension(filename);
						if (count == 0)
							filename = filenameNoExt + "_Level" + level + ext;
						else 
							filename = filenameNoExt + "_Level" + level + "_" + count + ext; 
						count++;
					}
					attachments.put(filename, ((MimeBodyPart) part).getDataHandler());
					
				} catch (IOException ex) {
					log("Failed to save attachment: " + ex);
				}
				log("---------------------------");
			}
		}
	}

	public void dumpEnvelope(Message m) throws Exception {
		log("This is the message envelope");
		log("---------------------------");
		Address[] a;
		// FROM
		if ((a = m.getFrom()) != null) {
			for (int j = 0; j < a.length; j++)
				log("FROM: " + a[j].toString());
		}

		// REPLY TO
		if ((a = m.getReplyTo()) != null) {
			for (int j = 0; j < a.length; j++)
				log("REPLY TO: " + a[j].toString());
		}

		// TO
		if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
			for (int j = 0; j < a.length; j++) {
				log("TO: " + a[j].toString());
				InternetAddress ia = (InternetAddress) a[j];
				if (ia.isGroup()) {
					InternetAddress[] aa = ia.getGroup(false);
					for (int k = 0; k < aa.length; k++)
						log("  GROUP: " + aa[k].toString());
				}
			}
		}

		// SUBJECT
		log("SUBJECT: " + m.getSubject());

		// DATE
		Date d = m.getSentDate();
		log("SendDate: " + (d != null ? d.toString() : "UNKNOWN"));

		// FLAGS
		Flags flags = m.getFlags();
		StringBuffer sb = new StringBuffer();
		Flags.Flag[] sf = flags.getSystemFlags(); // get the system flags

		boolean first = true;
		for (int i = 0; i < sf.length; i++) {
			String s;
			Flags.Flag f = sf[i];
			if (f == Flags.Flag.ANSWERED)
				s = "\\Answered";
			else if (f == Flags.Flag.DELETED)
				s = "\\Deleted";
			else if (f == Flags.Flag.DRAFT)
				s = "\\Draft";
			else if (f == Flags.Flag.FLAGGED)
				s = "\\Flagged";
			else if (f == Flags.Flag.RECENT)
				s = "\\Recent";
			else if (f == Flags.Flag.SEEN)
				s = "\\Seen";
			else
				continue; // skip it
			if (first)
				first = false;
			else
				sb.append(' ');
			sb.append(s);
		}
	}

	public void log(String s) {
		if (showStructure)
			logger.debug(indentStr.substring(0, level * 2));
		logger.debug(s);
	}

//	private void saveAttachment(Part part, String filename) throws Exception {
//		/*
//		 * If we're saving attachments, write out anything that looks like an
//		 * attachment into an appropriately named file. Don't overwrite existing
//		 * files to prevent mistakes.
//		 */
//		// if (saveAttachments && level != 0 && p instanceof MimeBodyPart &&
//		// !p.isMimeType("multipart/*")) {
//		if (saveAttachments && level != 0 && part instanceof MimeMessage) {
//			String disp = part.getDisposition();
//			// many mailers don't include a Content-Disposition
//			// if (disp == null || disp.equalsIgnoreCase(Part.ATTACHMENT)) {
//			if (disp == null || disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE)) {
//				if (filename == null)
//					filename = "Attachment" + attnum++;
//				log("Saving attachment to file " + filename);
//				try {
//					File f = new File(filename);
//					if (f == null)
//						f = new File(filename);
//					if (f.exists())
//						// XXX - could try a series of names
//						throw new IOException("file exists");
//					((MimeBodyPart) p).saveFile(f);
//					MailUtils.saveToEml((MimeMessage) part, f);
//				} catch (IOException ex) {
//					log("Failed to save attachment: " + ex);
//				}
//				log("---------------------------");
//			}
//		}
//	}
}
