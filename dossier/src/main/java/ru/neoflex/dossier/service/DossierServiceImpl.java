package ru.neoflex.dossier.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.neoflex.dossier.dto.EmailMessage;
import ru.neoflex.dossier.properties.GatewayProperties;

@Service
@RequiredArgsConstructor
@Slf4j
public class DossierServiceImpl implements DossierService {
    @Value("${app.code}")
    private String code;
    @Value("${spring.mail.username}")
    private String emailFrom;

    private final JavaMailSender mailSender;
    private final GatewayProperties properties;

    @Override
    public void send(EmailMessage emailMessage, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(emailMessage.getAddress());
        message.setSubject(emailMessage.getTheme().getValue());
        message.setText(text);
        mailSender.send(message);
        log.info("Message with theme {} send to email {}", emailMessage.getTheme().toString(), emailMessage.getAddress());
    }

    @Override
    public void sendDocumentHtml(EmailMessage emailMessage) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(emailMessage.getAddress());
        helper.setSubject(emailMessage.getTheme().getValue());

        String text = "Кредит одобрен! Для формирования документов нажмите на кнопку: ";
        String url = properties.getHost() + "/deal/document/" + emailMessage.getStatementId().toString() + "/send";
        String content = text + "<br><br>" +
                "<form id=\"postForm\" action=\"" + url + "\" method=\"post\">" +
                "  <button type=\"submit\">Прислать документы</button>" +
                "</form>" +
                "<script>" +
                "document.querySelector('button').addEventListener('click', function() {" +
                "  document.getElementById('postForm').submit();" +
                "});" +
                "</script>";

        helper.setText(content, true);

        mailSender.send(message);
        log.info("Message with theme {} send to email {}", emailMessage.getTheme().toString(), emailMessage.getAddress());
    }

    @Override
    public void signHtml(EmailMessage emailMessage) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(emailMessage.getAddress());
        helper.setSubject(emailMessage.getTheme().getValue());


        String text = "Пожалуйста, выберите одно из следующих действий: ";
        String url = properties.getHost() + "/deal/document/" + emailMessage.getStatementId().toString() + "/sign";

        String content = text + "<br><br>" +
                "<form id=\"signFormAccept\" action=\"" + url + "?isSigned=true\" method=\"post\">" +
                "  <button type=\"submit\">Согласиться</button>" +
                "</form>" +
                "<form id=\"signFormDecline\" action=\"" + url + "?isSigned=false\" method=\"post\">" +
                "  <button type=\"submit\">Отказаться</button>" +
                "</form>";

        helper.setText(content, true);

        mailSender.send(message);
        log.info("Message with theme {} send to email {}", emailMessage.getTheme().toString(), emailMessage.getAddress());
    }

    @Override
    public void verifyCodeHtml(EmailMessage emailMessage) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(emailMessage.getAddress());
        helper.setSubject(emailMessage.getTheme().getValue());

        String text = "Для завершения оформления, пройдите по ссылке и введите код: " + code;
        String url = properties.getHost() + "/deal/document/" + emailMessage.getStatementId().toString() + "/code";


        String content = text + "<br><br>" +
                "<form id=\"verifyForm\" action=\"" + url + "?code=" + code + "\" method=\"post\">" +
                "  <button type=\"submit\">Проверить код</button>" +
                "</form>";
        helper.setText(content, true);

        mailSender.send(message);
        log.info("Message with theme {} send to email {}", emailMessage.getTheme().toString(), emailMessage.getAddress());
    }
}
