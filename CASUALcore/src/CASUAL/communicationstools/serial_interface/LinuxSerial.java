/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package CASUAL.communicationstools.serial_interface;


import CASUAL.communicationstools.serial_interface.posix.FileFilter;
import CASUAL.communicationstools.serial_interface.posix.PosixSerialConnectivity;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamoutler
 */
public class LinuxSerial implements InterfaceSerialPort {
    String pathname="/dev/";
    String fileContains="ttyUSB";
    @Override
    public String[] getComPorts() {
        String[] ports=new FileFilter().selectFilesInPathBasedOnName(pathname, fileContains);
        for (int i=0;  i<ports.length; i++){
            if (!ports[i].startsWith(pathname)){
                ports[i]=pathname+ports[i];
            }
        }
        return ports;
    }

    @Override
    public boolean checkPortStatus(String port) {
        try {
            return new PosixSerialConnectivity().verifyConnectivity(port);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(LinuxSerial.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean sendDataToPort(String port, String data, String expectedValue)  {
        try {
            return (new PosixSerialConnectivity().sendCommandToSerial(port, data).contains(expectedValue));
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(LinuxSerial.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public String sendData(String port, String data) {
        try {
            String s=new PosixSerialConnectivity().sendCommandToSerial(port, data);
            return s;
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(LinuxSerial.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    
}