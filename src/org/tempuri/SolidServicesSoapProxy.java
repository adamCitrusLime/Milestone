package org.tempuri;

public class SolidServicesSoapProxy implements org.tempuri.SolidServicesSoap {
  private String _endpoint = null;
  private org.tempuri.SolidServicesSoap solidServicesSoap = null;
  
  public SolidServicesSoapProxy() {
    _initSolidServicesSoapProxy();
  }
  
  public SolidServicesSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initSolidServicesSoapProxy();
  }
  
  private void _initSolidServicesSoapProxy() {
    try {
      solidServicesSoap = (new org.tempuri.SolidServicesLocator()).getSolidServicesSoap();
      if (solidServicesSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)solidServicesSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)solidServicesSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (solidServicesSoap != null)
      ((javax.xml.rpc.Stub)solidServicesSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.tempuri.SolidServicesSoap getSolidServicesSoap() {
    if (solidServicesSoap == null)
      _initSolidServicesSoapProxy();
    return solidServicesSoap;
  }
  
  public boolean logStatus_CheckReboot(java.lang.String ipaddress, java.lang.String firmwareversion, int tillid) throws java.rmi.RemoteException{
    if (solidServicesSoap == null)
      _initSolidServicesSoapProxy();
    return solidServicesSoap.logStatus_CheckReboot(ipaddress, firmwareversion, tillid);
  }
  
  public java.lang.String getOutStandingReceipts(int solidID) throws java.rmi.RemoteException{
    if (solidServicesSoap == null)
      _initSolidServicesSoapProxy();
    return solidServicesSoap.getOutStandingReceipts(solidID);
  }
  
  public java.lang.String getOutStandingLabels(int solidID) throws java.rmi.RemoteException{
    if (solidServicesSoap == null)
      _initSolidServicesSoapProxy();
    return solidServicesSoap.getOutStandingLabels(solidID);
  }
  
  public void bulkSetReceiptStatus(int[] IDList) throws java.rmi.RemoteException{
    if (solidServicesSoap == null)
      _initSolidServicesSoapProxy();
    solidServicesSoap.bulkSetReceiptStatus(IDList);
  }
  
  public void bulkSetLabelStatus(int[] IDList) throws java.rmi.RemoteException{
    if (solidServicesSoap == null)
      _initSolidServicesSoapProxy();
    solidServicesSoap.bulkSetLabelStatus(IDList);
  }
  
  
}