package com.delta.deltanet.models.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public class SendEmailService {
	
	@Autowired
    private JavaMailSender javaMailSender;
	
    @Autowired
    private TemplateEngine templateEngine;
    
	@Value("#{${url}}")
	private String url;
	
    public void sendMail(String to, String subject, String body, String servicio) throws MessagingException{
    	MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED, "utf-8");
		
		
		final Context context = new Context();
		context.setVariable("mensaje", body);
		context.setVariable("type", subject);
		context.setVariable("servicio", servicio);
		context.setVariable("url", url);
		
        String content = this.templateEngine.process("mail", context);
        helper.addInline("logo", new ClassPathResource("images/deltanet.png"), "image/png");
        helper.setText(content, true);
		helper.setTo(to);
		helper.setSubject(subject);
		javaMailSender.send(mimeMessage);
    }

    public void sendMailTicket(String to, String title, String type, String head, String message, String servicio) throws MessagingException{
    	MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED, "utf-8");
		
		
		final Context context = new Context();
		context.setVariable("type", type);
		context.setVariable("mensaje", message);
		context.setVariable("head", head);
		context.setVariable("servicio", servicio);
		context.setVariable("url", url);
		
        String content = this.templateEngine.process("mailTicket", context);
        helper.addInline("logo", new ClassPathResource("images/deltanet.png"), "image/png");
        helper.setText(content, true);
		helper.setTo(to);
		helper.setSubject(title);
		javaMailSender.send(mimeMessage);
    }

    public void sendMailTicketMultiple(String to, String cc, String title, String type, String head, String message, String servicio) throws MessagingException{
    	MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED, "utf-8");
		
		
		final Context context = new Context();
		context.setVariable("type", type);
		context.setVariable("mensaje", message);
		context.setVariable("head", head);
		context.setVariable("servicio", servicio);
		context.setVariable("url", url);
		
        String content = this.templateEngine.process("mailTicket", context);
        helper.addInline("logo", new ClassPathResource("images/deltanet.png"), "image/png");
        helper.setText(content, true);
		helper.setTo(to);
		helper.setCc(cc);
		helper.setSubject(title);
		javaMailSender.send(mimeMessage);
    }

}	
