package com.capgemini.sesp.ast.android.ui.activity.document_download;

import java.io.Serializable;

/**
 * This is a light weight presentation of the FieldDocumentInfoTTO.
 * Created by navghosh on 4/19/2018.
 */
public class WoDocumentLiteTO implements Serializable {

    private String changeSignature;
    private String changeTimestamp;
    private String code;
    private String documentAliasName;
    private String documentUrl;
    private String documentVersion;
    private Long id;
    private Long idCaseT;
    private Long idDomain;
    private Long idFieldDocumentT;
    private String info;
    private String tokenCode;
    private String localDBCreateTimestamp;

    public String getChangeSignature() {
        return changeSignature;
    }

    public void setChangeSignature(String changeSignature) {
        this.changeSignature = changeSignature;
    }

    public String getChangeTimestamp() {
        return changeTimestamp;
    }

    public void setChangeTimestamp(String changeTimestamp) {
        this.changeTimestamp = changeTimestamp;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDocumentAliasName() {
        return documentAliasName;
    }

    public void setDocumentAliasName(String documentAliasName) {
        this.documentAliasName = documentAliasName;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String getDocumentVersion() {
        return documentVersion;
    }

    public void setDocumentVersion(String documentVersion) {
        this.documentVersion = documentVersion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCaseT() {
        return idCaseT;
    }

    public void setIdCaseT(Long idCaseT) {
        this.idCaseT = idCaseT;
    }

    public Long getIdDomain() {
        return idDomain;
    }

    public void setIdDomain(Long idDomain) {
        this.idDomain = idDomain;
    }

    public Long getIdFieldDocumentT() {
        return idFieldDocumentT;
    }

    public void setIdFieldDocumentT(Long idFieldDocumentT) {
        this.idFieldDocumentT = idFieldDocumentT;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTokenCode() {
        return tokenCode;
    }

    public void setTokenCode(String tokenCode) {
        this.tokenCode = tokenCode;
    }

    public String getLocalDBCreateTimestamp() {
        return localDBCreateTimestamp;
    }

    public void setLocalDBCreateTimestamp(String localDBCreateTimestamp) {
        this.localDBCreateTimestamp = localDBCreateTimestamp;
    }
}
