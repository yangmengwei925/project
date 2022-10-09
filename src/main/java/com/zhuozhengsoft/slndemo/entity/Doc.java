package com.zhuozhengsoft.slndemo.entity;
import org.springframework.format.annotation.DateTimeFormat;

import  java.util.Date;
/**
 * @Author: dong
 * @Date: 2022/7/23 15:16
 * @Version 1.0
 */
public class Doc {
    private int  id;
    private String  docName;
    private String  subject;
    private String  pdfName;
    private String  status;
    private String  docNo;
    private String title;
    private Date issueDate;
    private String issueDept;
    private int Copies;
    private String topicWords;
    private String templateName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDocNo() {
        return docNo;
    }

    public void setDocNo(String docNo) {
        this.docNo = docNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssueDept() {
        return issueDept;
    }

    public void setIssueDept(String issueDept) {
        this.issueDept = issueDept;
    }

    public int getCopies() {
        return Copies;
    }

    public void setCopies(int copies) {
        Copies = copies;
    }

    public String getTopicWords() {
        return topicWords;
    }

    public void setTopicWords(String topicWords) {
        this.topicWords = topicWords;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
