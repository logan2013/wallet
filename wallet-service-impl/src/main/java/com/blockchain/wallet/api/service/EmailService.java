package com.blockchain.wallet.api.service;


import com.blockchain.wallet.api.po.Pair;

import java.io.File;
import java.util.List;

public interface EmailService {


    /**
     * 发送简单邮件
     * @param sendTo 收件人地址
     * @param titel  邮件标题
     * @param content 邮件内容
     */
    public void sendSimpleMail(String sendTo, String titel, String content);

    /**
     * 发送简单邮件
     * @param sendTo 收件人地址
     * @param titel  邮件标题
     * @param content 邮件内容
     * @param attachments<文件名，附件> 附件列表
     */
    public void sendAttachmentsMail(String sendTo, String titel, String content, List<Pair<String, File>> attachments);

    public void sendHtmlMail(String to, String subject, String content);

    public void sendMessageMail(Object params, String title, String templateName, String sendTo);

    public void sendTemplateMail(String sendTo);

    public void sendInlineMail();




}
