package com.blockchain.wallet.api.service.impl;


import com.blockchain.wallet.api.exception.RuntimeServiceException;
import com.blockchain.wallet.api.po.Pair;
import com.blockchain.wallet.api.service.EmailService;
import com.blockchain.wallet.api.utils.EmailConfig;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {


	private static Logger logger = Logger.getLogger(EmailServiceImpl.class);
	@Autowired
	private EmailConfig emailConfig;


	//邮件的发送者
	@Value("${spring.mail.username}")
	private String from;

	//注入MailSender
	@Autowired
	private JavaMailSender mailSender;

	//发送邮件的模板引擎
	@Autowired
	private FreeMarkerConfigurer configurer;


	@Override
	public void sendSimpleMail(String sendTo, String titel, String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(emailConfig.getEmailFrom());
		message.setTo(sendTo);
		message.setSubject(titel);
		message.setText(content);
		mailSender.send(message);
	}



	@Override
	public void sendAttachmentsMail(String sendTo, String titel, String content, List<Pair<String, File>> attachments) {

		MimeMessage mimeMessage = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom(emailConfig.getEmailFrom());
			helper.setTo(sendTo);
			helper.setSubject(titel);
			helper.setText(content);

			for (Pair<String, File> pair : attachments) {
				helper.addAttachment(pair.getLeft(), new FileSystemResource(pair.getRight()));
			}
		} catch (Exception e) {
			throw new RuntimeServiceException(e);
		}

		mailSender.send(mimeMessage);
	}


	@Override
	public void sendInlineMail() {

		MimeMessage mimeMessage = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom(emailConfig.getEmailFrom());
			helper.setTo("18765287613@163.com");
			helper.setSubject("主题：嵌入静态资源");
			helper.setText("<html><body><img src=\"cid:weixin\" ></body></html>", true);

			FileSystemResource file = new FileSystemResource(new File("weixin.jpg"));
			helper.addInline("weixin", file);
		} catch (Exception e) {
			throw new RuntimeServiceException(e);
		}

		mailSender.send(mimeMessage);
	}

	public void sendHtmlMail(String to, String subject, String content) {
		MimeMessage message = mailSender.createMimeMessage();

		try {
			//true表示需要创建一个multipart message
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			//helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);

			mailSender.send(message);
			logger.info("html邮件发送成功");
		} catch (MessagingException e) {
			logger.error("发送html邮件时发生异常！", e);
		}
	}

	@Override
	public void sendMessageMail(Object params, String title, String templateName,String sendTo) {
		try {

			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom(from);
			helper.setTo(InternetAddress.parse(sendTo));//发送给谁
			helper.setSubject("【" + title + "-" + LocalDate.now() + " " + LocalTime.now().withNano(0) + "】");//邮件标题

			Map<String, Object> model = new HashMap<>();
			model.put("params", params);

			try {
				Template template = configurer.getConfiguration().getTemplate(templateName);
				try {
					String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

					helper.setText(text, true);
					mailSender.send(mimeMessage);
				} catch (TemplateException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

    @Override
    public void sendTemplateMail(String sendTo) {


    }

}
