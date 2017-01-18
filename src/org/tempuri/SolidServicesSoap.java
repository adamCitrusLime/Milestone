/**
 * SolidServicesSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public interface SolidServicesSoap extends java.rmi.Remote {
    public boolean logStatus_CheckReboot(java.lang.String ipaddress, java.lang.String firmwareversion, int tillid) throws java.rmi.RemoteException;
    public java.lang.String getOutStandingReceipts(int solidID) throws java.rmi.RemoteException;
    public java.lang.String getOutStandingLabels(int solidID) throws java.rmi.RemoteException;
    public void bulkSetReceiptStatus(int[] IDList) throws java.rmi.RemoteException;
    public void bulkSetLabelStatus(int[] IDList) throws java.rmi.RemoteException;
}
