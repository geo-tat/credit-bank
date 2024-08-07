package ru.neoflex.dossier.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import ru.neoflex.dossier.dto.EmailMessage;
import ru.neoflex.dossier.enums.TopicType;
import ru.neoflex.dossier.properties.GatewayProperties;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DossierServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private GatewayProperties properties;

    @InjectMocks
    private DossierServiceImpl dossierService;

    private EmailMessage emailMessage;

    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
    emailMessage = EmailMessage.builder()
            .address("test@test.com")
            .theme(TopicType.FINISH_REGISTRATION)
            .statementId(UUID.randomUUID())
            .build();

    mimeMessage = mock(MimeMessage.class);
    }

    @Test
    void testSend() {

        SimpleMailMessage message = new SimpleMailMessage();
        String emailFrom = "test@example.com";
        message.setFrom(emailFrom);
        message.setTo(emailMessage.getAddress());
        message.setSubject(emailMessage.getTheme().getValue());
        message.setText("Test text");

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        dossierService.send(emailMessage, "Test text");

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendDocumentHtml() throws MessagingException {


        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(properties.getHost()).thenReturn("http://localhost");

        ArgumentCaptor<MimeMessageHelper> mimeMessageHelperCaptor = ArgumentCaptor.forClass(MimeMessageHelper.class);

        dossierService.sendDocumentHtml(emailMessage);

        verify(mailSender, times(1)).send(mimeMessage);

        verify(mailSender).send(mimeMessage);
    }

    @Test
    void testSignHtml() throws MessagingException {



        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(properties.getHost()).thenReturn("http://localhost");

        dossierService.signHtml(emailMessage);

        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testVerifyCodeHtml() throws MessagingException {


        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(properties.getHost()).thenReturn("http://localhost");

        dossierService.verifyCodeHtml(emailMessage);

        verify(mailSender, times(1)).send(mimeMessage);
    }
}
