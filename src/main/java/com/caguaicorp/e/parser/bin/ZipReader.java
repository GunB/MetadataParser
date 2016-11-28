package com.caguaicorp.e.parser.bin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import com.caguaicorp.e.parser.model.ElementHandler;
import com.caguaicorp.e.parser.utiility.FilesUtility;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import com.caguaicorp.e.parser.utiility.XMLUtility;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ZipReader
        implements ElementHandler {

    String strPath;
    String strName;
    String strNewSufij = ".eFixed";
    String strFile2Change = "metadata.xml";

    ZipFile zipFile = null;
    File file = null;

    public ZipReader(String strPath, String strName) throws IOException {
        this.strPath = strPath;
        this.strName = strName;
        String strFile = strPath.concat(File.separator).concat(strName);
        this.file = new File(strFile);
        this.zipFile = new ZipFile(strFile);
    }

    public ZipReader(File fileData) throws IOException {
        this.strPath = fileData.getParent();
        this.strName = fileData.getName();
        this.zipFile = new ZipFile(fileData);
        this.file = fileData;
        System.out.println("Path: " + this.strPath);
        System.out.println("Name: " + this.strName);
        System.out.println("File: " + this.zipFile.getName());
    }

    @Override
    public Document Read()
            throws IOException, SAXException, ParserConfigurationException {
        Document doc = null;

        for (Enumeration e = this.zipFile.entries(); e.hasMoreElements();) {
            ZipEntry entryIn = (ZipEntry) e.nextElement();
            String strName = entryIn.getName();
            if (strName.equalsIgnoreCase(this.strFile2Change)) {
                doc = XMLUtility.newDocumentFromInputStream(this.zipFile.getInputStream(entryIn));
            }
        }

        if (doc == null) {
            try {
                WriteFinish(FilesUtility.XmlFormatBase());
                this.zipFile = new ZipFile(this.file);
            } catch (TransformerException ex) {
                Logger.getLogger(ZipReader.class.getName()).log(Level.SEVERE, null, ex);
            }
            doc = Read();
        }

        return doc;
    }

    @Override
    public void WriteFinish(Document doc)
            throws IOException, TransformerException {
        String strOldFile = this.strPath.concat(File.separator).concat(this.strName);

        String strNewFile = this.strPath.concat(File.separator).concat(this.strName).concat(this.strNewSufij);
        File newFile = new File(strNewFile);

        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(newFile), StandardCharsets.UTF_8);
        System.out.println("Saving [File]: " + strNewFile);

        Boolean isFounded = false;

        //XMLUtility.printDocument(doc, System.out);
        for (Enumeration e = this.zipFile.entries(); e.hasMoreElements();) {
            ZipEntry entryIn = new ZipEntry((ZipEntry) e.nextElement());

            if (!entryIn.getName().equalsIgnoreCase(this.strFile2Change)) {

                ZipEntry destEntry = new ZipEntry(entryIn.getName());
                zos.putNextEntry(destEntry);

                InputStream is = this.zipFile.getInputStream(entryIn);
                byte[] buf = new byte['Ѐ'];
                int len;
                while ((len = is.read(buf)) > 0) {
                    zos.write(buf, 0, len);
                }
            } else {
                isFounded = true;
                ZipEntry destEntry = new ZipEntry(this.strFile2Change);
                zos.putNextEntry(destEntry);
                XMLUtility.printDocument(doc, zos);
            }

            zos.closeEntry();
        }

        if (!isFounded) {
            ZipEntry destEntry = new ZipEntry(this.strFile2Change);
            zos.putNextEntry(destEntry);
            XMLUtility.printDocument(doc, zos);
            zos.closeEntry();
        }
        zos.close();

        this.zipFile.close();

        File oldFile = new File(strOldFile);
        oldFile.delete();

        newFile.renameTo(oldFile);
    }
}
