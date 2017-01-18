/**
 * SolidServicesLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public class SolidServicesLocator extends org.apache.axis.client.Service implements org.tempuri.SolidServices {

    public SolidServicesLocator() {
    }


    public SolidServicesLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SolidServicesLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SolidServicesSoap
    private java.lang.String SolidServicesSoap_address = "http://ps.citruslime.com/SolidServices.asmx";

    public java.lang.String getSolidServicesSoapAddress() {
        return SolidServicesSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SolidServicesSoapWSDDServiceName = "SolidServicesSoap";

    public java.lang.String getSolidServicesSoapWSDDServiceName() {
        return SolidServicesSoapWSDDServiceName;
    }

    public void setSolidServicesSoapWSDDServiceName(java.lang.String name) {
        SolidServicesSoapWSDDServiceName = name;
    }

    public org.tempuri.SolidServicesSoap getSolidServicesSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SolidServicesSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSolidServicesSoap(endpoint);
    }

    public org.tempuri.SolidServicesSoap getSolidServicesSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.tempuri.SolidServicesSoapStub _stub = new org.tempuri.SolidServicesSoapStub(portAddress, this);
            _stub.setPortName(getSolidServicesSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSolidServicesSoapEndpointAddress(java.lang.String address) {
        SolidServicesSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.tempuri.SolidServicesSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                org.tempuri.SolidServicesSoapStub _stub = new org.tempuri.SolidServicesSoapStub(new java.net.URL(SolidServicesSoap_address), this);
                _stub.setPortName(getSolidServicesSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("SolidServicesSoap".equals(inputPortName)) {
            return getSolidServicesSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "SolidServices");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "SolidServicesSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SolidServicesSoap".equals(portName)) {
            setSolidServicesSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
