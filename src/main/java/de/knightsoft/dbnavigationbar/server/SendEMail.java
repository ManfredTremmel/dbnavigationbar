/**
 * This file is part of DBNavigationBar.
 *
 * DBNavigationBar is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * DBNavigationBar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with DBNavigationBar. If not, see <a
 * href="http://www.gnu.org/licenses>http://www.gnu.org/licenses</a>
 *
 *
 * Copyright (c) 2011-2015 Manfred Tremmel
 *
 */

package de.knightsoft.dbnavigationbar.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * <code>SendEMail</code> is a class to send emails.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public class SendEMail {

  /**
   * SMTP port.
   */
  public static final int SMTP_PORT = 25;

  /**
   * ip address of LOCALHOST.
   */
  public static final String LOCALHOST = "127.0.0.1"; // NOPMD

  /**
   * Constructor.
   *
   * @param pFrom email of the sender
   * @param pOrganization organization of the sender
   * @param pTo email of the receiver
   * @param pReplyTo email where answers should be sent to
   * @param pSubject subject of the email
   * @param pMailtext text of the email
   * @param pSmtpServer server to send the mail
   * @throws IOException when sending fails
   */
  public SendEMail(final String pFrom, final String pOrganization, final String pTo, final String pReplyTo,
      final String pSubject, final String pMailtext, final String pSmtpServer) throws IOException {
    String replyToText = pReplyTo;
    if (replyToText == null || "".equals(replyToText)) {
      replyToText = "";
    } else {
      replyToText = "\nReply-To: " + pReplyTo;
    }
    // setup String with complete mail including headers
    final String eMailString = //
        "MAIL FROM: " + pFrom //
            + "\nRCPT TO: " + pTo //
            + "\nDATA" //
            + "\nFrom: " + pFrom //
            + "\nTo: " + pTo //
            + "\nOrganization: " + pOrganization //
            + replyToText //
            + "\nReturn-Path: " + pReplyTo //
            + "\nSubject: " + pSubject //
            + "\nContent-Type: text/plain; charset=\"iso-8859-1\"" //
            + "\nContent-Transfer-Encoding: 8bit" //
            + "\n" + pMailtext;

    this.sendEMailSendmail(eMailString, pSmtpServer);
  }

  /**
   * Constructor.
   *
   * @param pFrom email of the sender
   * @param pOrganization organization of the sender
   * @param pTo email of the receiver
   * @param pSubject subject of the email
   * @param pMailtext text of the email
   * @param pSmtpServer server to send the mail
   * @throws IOException when sending fails
   */
  public SendEMail(final String pFrom, final String pOrganization, final String pTo, final String pSubject,
      final String pMailtext, final String pSmtpServer) throws IOException {
    this(pFrom, pOrganization, pTo, null, pSubject, pMailtext, pSmtpServer);
  }

  /**
   * Constructor.
   *
   * @param pFrom email of the sender
   * @param pOrganization organization of the sender
   * @param pTo email of the receiver
   * @param pSubject subject of the email
   * @param pMailtext text of the email
   * @throws IOException when sending fails
   */
  public SendEMail(final String pFrom, final String pOrganization, final String pTo, final String pSubject,
      final String pMailtext) throws IOException {
    this(pFrom, pOrganization, pTo, null, pSubject, pMailtext, LOCALHOST);
  }

  /**
   * Constructor.
   *
   * @param pEMailString the complete e-mail
   * @throws IOException when sending fails
   */
  public SendEMail(final String pEMailString) throws IOException {
    this(pEMailString, LOCALHOST);
  }

  /**
   * Constructor.
   *
   * @param pEMailString the complete e-mail
   * @param pSmtpServer server to send the mail
   * @throws IOException when sending fails
   */
  public SendEMail(final String pEMailString, final String pSmtpServer) throws IOException {
    this.sendEMailSendmail(pEMailString, pSmtpServer);
  }

  /**
   * The <code>SendEMailSendmail</code> method is sending the e-mail.
   *
   * @param pEMailString the complete e-mail
   * @param pSmtpServer server to send the mail
   * @throws IOException when sending fails
   */
  public final void sendEMailSendmail(final String pEMailString, final String pSmtpServer) throws IOException {
    try (final Socket smtpSocket = new Socket(pSmtpServer, SMTP_PORT)) {

      String responseline = null;
      final int mailTabStart = 3;

      if (smtpSocket != null) {
        try (final DataOutputStream os = new DataOutputStream(smtpSocket.getOutputStream());
            final BufferedReader br = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()))) {

          final String[] mailTab = pEMailString.split("\n");

          responseline = br.readLine();
          if (responseline != null && responseline.startsWith("220")) {
            os.writeBytes("HELO " + pSmtpServer + "\r\n");

            responseline = br.readLine();
            if (responseline != null && responseline.startsWith("250")) {
              os.writeBytes(mailTab[0] + "\r\n");

              responseline = br.readLine();
              if (responseline != null && responseline.startsWith("250")) {
                os.writeBytes(mailTab[1] + "\r\n");

                responseline = br.readLine();
                if (responseline != null && responseline.startsWith("250")) {
                  os.writeBytes(mailTab[2] + "\r\n");

                  responseline = br.readLine();
                  if (responseline != null && responseline.startsWith("354")) {
                    for (int i = mailTabStart; i < mailTab.length; i++) {
                      os.writeBytes(mailTab[i] + "\r\n");
                    }
                    os.writeBytes("\r\n.\r\n");

                    responseline = br.readLine();
                    if (responseline != null && responseline.startsWith("250")) {
                      os.writeBytes("QUIT\r\n");

                      responseline = br.readLine();
                      if (responseline == null || !responseline.startsWith("221")) {
                        throw new IOException("Got no OK after QUIT: " + responseline);
                      }
                    } else {
                      throw new IOException("Server didn't accept Message for delivery: " + responseline);
                    }
                  } else {
                    throw new IOException("Server didn't allow to send Mailbody: " + responseline);
                  }
                } else {
                  throw new IOException("Server was not happy with our Receiver: " + responseline);
                }
              } else {
                throw new IOException("Server was not happy with our Sender: " + responseline);
              }
            } else {
              throw new IOException("Server was not happy with our HELLO message: " + responseline);
            }
          } else {
            throw new IOException("Got no Hello from the Server: " + responseline);
          }
        }
      }
    }
  }
}
