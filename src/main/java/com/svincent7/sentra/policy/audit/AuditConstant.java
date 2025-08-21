package com.svincent7.sentra.policy.audit;

public final class AuditConstant {
    private AuditConstant() {

    }
    public static final String EVENT_NAME = "Event-Name";
    public static final String START_TIME = "Start-Time";
    public static final String SRN_RESOURCE = "SRN-Resource";

    public static final String EVENT_GET_ALL = "Get All ";
    public static final String EVENT_GET = "Get ";
    public static final String EVENT_CREATE = "Create ";
    public static final String EVENT_UPDATE = "Update ";
    public static final String EVENT_DELETE = "Delete ";
    public static final String EVENT_DECRYPT = "Decrypt ";
    public static final String EVENT_ENCRYPT = "Encrypt ";
    public static final String EVENT_ROTATE_KMS = "Rotate Key KMS ";
    public static final String EVENT_GET_ALL_ROLE = "Get Role ";
    public static final String EVENT_ADD_ROLE = "Add Role ";
    public static final String EVENT_DELETE_ROLE = "Delete Role ";
}
