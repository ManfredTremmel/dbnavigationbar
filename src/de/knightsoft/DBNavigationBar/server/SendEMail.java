/**
 * This file is part of DBNavigationBar.
 *
 * RiPhone is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * RiPhone is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RiPhone.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 * Copyright (c) 2011 Manfred Tremmel
 *
 * --
 *  Name        Date        Change
 *  TremmelM    2011-01-02  Added a constructor with reply-to
 */
package de.knightsoft.DBNavigationBar.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * <code>SendEMail</code> is a class to send emails.
 *
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-01
 */
public class SendEMail {

    /**
     * SMTP port.
     */
    static final int SMTP_PORT = 25;

    /**
     * Constructor.
     *
     * @param from
     *            email of the sender
     * @param organization
     *            organization of the sender
     * @param to
     *            email of the receiver
     * @param replyTo
     *            email where answers should be sent to
     * @param subject
     *            subject of the email
     * @param mailtext
     *            text of the email
     * @param smtpServer
     *            server to send the mail
     * @throws IOException when sending fails
     */
    public SendEMail(final String from,
                     final String organization,
                     final String to,
                     final String replyTo,
                     final String subject,
                     final String mailtext,
                     final String smtpServer
                   ) throws IOException {
        String replyToText = replyTo;
        if (replyToText == null || "".equals(replyToText)) {
            replyToText = "";
        } else {
            replyToText = "\nReply-To: " + replyTo;
        }
        // setup String with complete mail including headers
        String eMailString =
                "MAIL FROM: " + from
              + "\nRCPT TO: " + to
              + "\nDATA"
              + "\nFrom: " + from
              + "\nTo: " + to
              + "\nOrganization: " + organization
              + replyToText
              + "\nReturn-Path: " + replyTo
              + "\nSubject: " + subject
              + "\nContent-Type: text/plain; charset=\"iso-8859-1\""
              + "\nContent-Transfer-Encoding: 8bit"
              + "\n" + mailtext;

        sendEMailSendmail(eMailString, smtpServer);
    }


    /**
     * Constructor.
     *
     * @param from
     *            email of the sender
     * @param organization
     *            organization of the sender
     * @param to
     *            email of the receiver
     * @param subject
     *            subject of the email
     * @param mailtext
     *            text of the email
     * @param smtpServer
     *            server to send the mail
     * @throws IOException when sending fails
     */
    public SendEMail(final String from,
                     final String organization,
                     final String to,
                     final String subject,
                     final String mailtext,
                     final String smtpServer
                   ) throws IOException {
        this(from, organization, to, null, subject, mailtext, smtpServer);
    }

    /**
     * Constructor.
     *
     * @param from
     *            email of the sender
     * @param organization
     *            organization of the sender
     * @param to
     *            email of the receiver
     * @param subject
     *            subject of the email
     * @param mailtext
     *            text of the email
     * @throws IOException when sending fails
     */
    public SendEMail(final String from,
                     final String organization,
                     final String to,
                     final String subject,
                     final String mailtext
                   ) throws IOException {
        this(from, organization, to, null, subject, mailtext,  "127.0.0.1");
    }

    /**
     * Constructor.
     *
     * @param eMailString
     *            the complete e-mail
     * @throws IOException when sending fails
     */
    public SendEMail(final String eMailString
           ) throws IOException {
        this(eMailString, "127.0.0.1");
    }

    /**
     * Constructor.
     *
     * @param eMailString
     *            the complete e-mail
     * @param smtpServer
     *            server to send the mail
     * @throws IOException when sending fails
     */
    public SendEMail(final String eMailString,
                     final String smtpServer
           ) throws IOException {
        sendEMailSendmail(eMailString, smtpServer);
    }

    /**
     * The <code>SendEMailSendmail</code> method is sending the e-mail.
     *
     * @param eMailString
     *            the complete e-mail
     * @param smtpServer
     *            server to send the mail
     * @throws IOException when sending fails
     */
    public final void sendEMailSendmail(final String eMailString,
                                        final String smtpServer
           ) throws IOException {
        Socket smtpSocket = new Socket(smtpServer, SMTP_PORT);
        String responseline    = null;
        final int mailTabStart = 3;

        if (smtpSocket != null) {
            DataOutputStream os =
                    new DataOutputStream(smtpSocket.getOutputStream());
            BufferedReader   br =
                    new BufferedReader(new InputStreamReader(
                            smtpSocket.getInputStream()));

            String[] mailTab = eMailString.split("\n");

            responseline = br.readLine();
            if (responseline != null
             && responseline.startsWith("220")) {
                os.writeBytes("HELO " + smtpServer + "\r\n");

                responseline = br.readLine();
                if (responseline != null
                 && responseline.startsWith("250")) {
                    os.writeBytes(mailTab[0] + "\r\n");

                    responseline = br.readLine();
                    if (responseline != null
                     && responseline.startsWith("250")) {
                        os.writeBytes(mailTab[1] + "\r\n");

                        responseline = br.readLine();
                        if (responseline != null
                         && responseline.startsWith("250")) {
                            os.writeBytes(mailTab[2] + "\r\n");

                            responseline = br.readLine();
                            if (responseline != null
                             && responseline.startsWith("354")) {
                                for (int i = mailTabStart;
                                     i < mailTab.length; i++) {
                                    os.writeBytes(mailTab[i] + "\r\n");
                                }
                                os.writeBytes("\r\n.\r\n");

                                responseline = br.readLine();
                                if (responseline != null
                                 && responseline.startsWith("250")) {
                                    os.writeBytes("QUIT\r\n");

                                    responseline = br.readLine();
                                    if (responseline == null
                                     || !responseline.startsWith("221")) {
                                        throw new IOException(
                                          "Got no OK after QUIT: "
                                        + responseline);
                                    }
                                } else {
                                    throw new IOException(
                                    "Server didn't accept Message "
                                    + "for delivery: "
                                    + responseline);
                                }
                            } else {
                                throw new IOException(
                                  "Server didn't allow to send Mailbody: "
                                  + responseline);
                            }
                        } else {
                            throw new IOException("Server was not happy "
                                + "with our Receiver: " + responseline);
                        }
                    } else {
                        throw new IOException("Server was not happy with "
                                + "our Sender: " + responseline);
                    }
                } else {
                    throw new IOException("Server was not happy with our "
                            + "HELLO message: " + responseline);
                }
            } else {
                throw new IOException("Got no Hello from the Server: "
                      + responseline);
            }
        }
    }
};
