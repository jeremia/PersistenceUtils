/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jeremiaMorling.utils.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import jeremiaMorling.utils.vector.IntVector;

/**
 *
 * @author Jeremia
 */
public class PersistenceUtils {
    public static void saveBoolean( String recordStoreName, Boolean value ) {
        if( value == null || recordStoreName == null )
            return;
        
        try {
            RecordStore rs = RecordStore.openRecordStore( recordStoreName, true );
            deleteAllRecords( rs );
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream( outputStream );
            dataOutputStream.writeBoolean( value.booleanValue() );
            dataOutputStream.flush();
            byte[] record = outputStream.toByteArray();
            rs.addRecord( record, 0, record.length );
            
            outputStream.close();
            dataOutputStream.close();
            rs.closeRecordStore();
        } catch( Exception e ) {}
    }
    
    public static Boolean readBoolean( String recordStoreName ) {
        if( !doesRecordStoreExist( recordStoreName ) )
            return null;
        
        try {
            RecordStore rs = RecordStore.openRecordStore( recordStoreName, false );
            RecordEnumeration re = rs.enumerateRecords( null, null, false );
            byte[] record = re.nextRecord();
            ByteArrayInputStream inputStream = new ByteArrayInputStream( record );
            DataInputStream dataInputStream = new DataInputStream( inputStream );
            Boolean result = new Boolean( dataInputStream.readBoolean() );

            inputStream.close();
            dataInputStream.close();
            re.destroy();
            rs.closeRecordStore();

            return result;
        } catch( Exception e ) {
            return null;
        }
    }
    
    public static void saveInt( String recordStoreName, int value ) {
        if( recordStoreName == null )
            return;
        
        try {
            RecordStore rs = RecordStore.openRecordStore( recordStoreName, true );
            deleteAllRecords( rs );
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream( outputStream );
            dataOutputStream.writeInt( value );
            dataOutputStream.flush();
            byte[] record = outputStream.toByteArray();
            rs.addRecord( record, 0, record.length );
            
            outputStream.close();
            dataOutputStream.close();
            rs.closeRecordStore();
        } catch( Exception e ) {}
    }
    
    public static int readInt( String recordStoreName ) {
        if( !doesRecordStoreExist( recordStoreName ) )
            return Integer.MIN_VALUE;
        
        try {
            int result;

            RecordStore rs = RecordStore.openRecordStore( recordStoreName, false );
            RecordEnumeration re = rs.enumerateRecords( null, null, false );
            byte[] record = re.nextRecord();
            ByteArrayInputStream inputStream = new ByteArrayInputStream( record );
            DataInputStream dataInputStream = new DataInputStream( inputStream );
            result = dataInputStream.readInt();

            inputStream.close();
            dataInputStream.close();
            re.destroy();
            rs.closeRecordStore();

            return result;
        } catch( Exception e ) {
            return Integer.MIN_VALUE;
        }
    }
    
    public static void saveIntArray( String recordStoreName, int[] value ) {
        if( recordStoreName == null || value == null )
            return;
        
        try {
            RecordStore rs = RecordStore.openRecordStore( recordStoreName, true );
            deleteAllRecords( rs );
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream( value.length*4 );
            DataOutputStream dataOutputStream = new DataOutputStream( outputStream );
            for( int i=0; i<value.length; i++ )
                dataOutputStream.writeInt( value[i] );
            dataOutputStream.flush();
            byte[] record = outputStream.toByteArray();
            rs.addRecord( record, 0, record.length );
            
            outputStream.close();
            dataOutputStream.close();
            rs.closeRecordStore();
        } catch( Exception e ) {}
    }
    
    public static int[] readIntArray( String recordStoreName ) {
        if( !doesRecordStoreExist( recordStoreName ) )
            return null;

        try {
            RecordStore rs = RecordStore.openRecordStore( recordStoreName, false );
            RecordEnumeration re = rs.enumerateRecords( null, null, false );
            byte[] record = re.nextRecord();
            ByteArrayInputStream inputStream = new ByteArrayInputStream( record );
            DataInputStream dataInputStream = new DataInputStream( inputStream );
            IntVector intVector = new IntVector();
            while( dataInputStream.available() > 0 )
                intVector.addElement( dataInputStream.readInt() );
            int[] result = intVector.toIntArray();
            
            inputStream.close();
            dataInputStream.close();
            re.destroy();
            rs.closeRecordStore();
            
            return result;
        } catch( Exception e ) {
            return null;
        }
    }
    
    public static boolean doesRecordStoreExist( String recordStoreName ) {
        String[] recordStores = RecordStore.listRecordStores();
        if( recordStores == null )
            return false;
        
        for( int i=0; i<recordStores.length; i++ ) {
            if( recordStores[i] != null && recordStores[i].equals( recordStoreName ) )
                return true;
        }
        
        return false;
    }

    public static void deleteAllRecords( RecordStore rs ) {
        try {
            RecordEnumeration re = rs.enumerateRecords( null, null, false );
            while( re.hasNextElement() ) {
                int recordId = re.nextRecordId();
                rs.deleteRecord( recordId );
            }
            re.destroy();
        } catch( Exception e ) {}
    }
}
