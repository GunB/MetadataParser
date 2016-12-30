package com.caguaicorp.e.parser.utiility;

import com.caguaicorp.e.parser.bin.MetadataParser;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class FilesUtility {

    private static final long unixTime = System.currentTimeMillis() / 1000L;
    private static final String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(Calendar.getInstance().getTime());
    public static final String actualDate = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
    public static String strFile2Change = "metadata.xml";

    public static Document XmlFormatBase() throws ParserConfigurationException, SAXException, IOException {
        String text = ""
                + "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
                + "<cem:cem xmlns:cem=\"http://ltsc.ieee.org/xsd/CEM\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://ltsc.ieee.org/xsd/LOM lomCustom.xsd\" xsi:type=\"cem:cem\">\n"
                + "     <general>\n"
                + "		<identifier>\n"
                + "			<catalog catName=\"edistribution\" catSource=\"http://www.edistribution.co/\"/>\n"
                + "		</identifier>\n"
                + "		<title lang=\"\" subtitle=\"\"></title>\n"
                + "		<description lang=\"\"/>\n"
                + "		<keyword lang=\"\"/>\n"
                + "		<structure schema=\"CEM\"/>\n"
                + "		<aggregationLevel schema=\"CEM\"/>\n"
                + "	</general>\n"
                + "	<lifeCycle>\n"
                + "		<version date=\"" + actualDate + "\">1.0</version>\n"
                + "		<status>Publicado</status>\n"
                //<editor-fold defaultstate="collapsed" desc="TODO:Contribuyentes que luego deberán ser leídos">
                
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Productor ejecutivo</role>\n"
                + "			<entity country=\"CO\" entityForm=\"79786415\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Díaz Ochoa, Gustavo Andrés</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Autor</role>\n"
                + "			<entity country=\"CO\" entityForm=\"79989119\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Perdomo Rodríguez, William</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Autor</role>\n"
                + "			<entity country=\"CO\" entityForm=\"1012329187\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Díaz Martínez, Heidy Milena</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Autor</role>\n"
                + "			<entity country=\"CO\" entityForm=\"52504731\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Páez Cárdenas, Jenny Marisol</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Autor</role>\n"
                + "			<entity country=\"CO\" entityForm=\"1032412686\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Salcedo Carmargo, Laura Catalina</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Autor</role>\n"
                + "			<entity country=\"CO\" entityForm=\"1032413204\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Cortés Barrios, Sindy Paola</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Autor</role>\n"
                + "			<entity country=\"CO\" entityForm=\"1030646632\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Guasca Becerra, Judith Andrea</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Autor</role>\n"
                + "			<entity country=\"CO\" entityForm=\"1128055436\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Ortega Cabarcas, Aura Beatriz</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Autor</role>\n"
                + "			<entity country=\"CO\" entityForm=\"1018449486\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Barbosa Ortegón, Sebastián Mateo</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Autor</role>\n"
                + "			<entity country=\"CO\" entityForm=\"1030602142\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Sánchez López, Carolina</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Autor</role>\n"
                + "			<entity country=\"CO\" entityForm=\"80187759\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Delgado Rodríguez, Camilo Andrés</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Autor</role>\n"
                + "			<entity country=\"CO\" entityForm=\"1032389739\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Restrepo Giraldo, Yovani Andrés</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Autor</role>\n"
                + "			<entity country=\"CO\" entityForm=\"11204475\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Martínez Cantor, Omar Gabriel</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Implementador técnico</role>\n"
                + "			<entity country=\"CO\" entityForm=\"52490996\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Quintero Castiblanco, Sussan Jeannethe</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Implementador técnico</role>\n"
                + "			<entity country=\"CO\" entityForm=\"42145399\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Suárez Trujillo, Luis Fernanda</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Implementador técnico</role>\n"
                + "			<entity country=\"CO\" entityForm=\"46451917\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Medrano Gómez, Margarita Marcela</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Implementador técnico</role>\n"
                + "			<entity country=\"CO\" entityForm=\"1032358250\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">González Pulecio, Alejandro</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Implementador técnico</role>\n"
                + "			<entity country=\"CO\" entityForm=\"1020748674\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Ospina Cabrera, Mariana</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Implementador técnico</role>\n"
                + "			<entity country=\"CO\" entityForm=\"1010207397\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Valenzuela López, Daniel Felipe</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Implementador técnico</role>\n"
                + "			<entity country=\"CO\" entityForm=\"41962305\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Urrea Macias, María Paula</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Implementador técnico</role>\n"
                + "			<entity country=\"CO\" entityForm=\"80871315\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Pikieris Caicedo, Andrei</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Implementador técnico</role>\n"
                + "			<entity country=\"CO\" entityForm=\"1015430677\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Escucha Hilarion, Anlly Vanesa</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Diseñador gráfico</role>\n"
                + "			<entity country=\"CO\" entityForm=\"1075659623\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Anzola Moreno, Laura Victoria</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Diseñador gráfico</role>\n"
                + "			<entity country=\"CO\" entityForm=\"1020796302\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Sombrerero Barrantes, Angie Milena</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Diseñador gráfico</role>\n"
                + "			<entity country=\"CO\" entityForm=\"1023878922\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Cervantes Bermudez, Marlon Andres</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Diseñador gráfico</role>\n"
                + "			<entity country=\"CO\" entityForm=\"1140422168\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Torres Escobar, Jesus Eduardo</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Diseñador gráfico</role>\n"
                + "			<entity country=\"CO\" entityForm=\"1015467045\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Gómez Castellanos, Geovany Hernando</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Diseñador gráfico</role>\n"
                + "			<entity country=\"CO\" entityForm=\"1032451547\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Lozano Moreno, Fabio Alberto</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Diseñador gráfico</role>\n"
                + "			<entity country=\"CO\" entityForm=\"103377209\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">Hernandez Ortiz, Maria Fernanda</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>\n"
                + "		<contribute>\n"
                + "			<role schema=\"CEM\">Diseñador gráfico</role>\n"
                + "			<entity country=\"CO\" entityForm=\"1016049594\" institution=\"eDistribution SAS\" src=\"contacto@edistribution.co\" type=\"Persona\">García Sánchez, Elkin Jair.</entity>\n"
                + "			<date>" + actualDate + "</date>\n"
                + "		</contribute>"
                //*/
                //</editor-fold>
                + "	</lifeCycle>\n"
                + "	<metaMetadata/>\n"
                + "	<technical/>\n"
                + "	<educational>\n"
                + "		<description>\n"
                + "			<recommendedUse lang=\"\"/>\n"
                + "			<triggerQuestion lang=\"\"/>\n"
                + "			<pedagogicalAspect lang=\"\"/>\n"
                + "			<learningGoal lang=\"\"/>\n"
                + "		</description>\n"
                + "	</educational>\n"
                + "	<rights/>\n"
                + "	<classification>\n"
                + "	</classification>\n"
                + "</cem:cem>";
        Document ndXml = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)));

        return ndXml;
    }

    public static void copyFolder(File src, File dest)
            throws IOException {
        if (src.isDirectory()) {

            if (!dest.exists()) {
                dest.mkdir();
                System.out.println("Directory copied from " + src + "  to " + dest);
            }

            String[] files = src.list();

            for (String file : files) {
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);

                copyFolder(srcFile, destFile);
            }

        } else {
            InputStream in = new FileInputStream(src);
            Object out = new FileOutputStream(dest);

            byte[] buffer = new byte['Ѐ'];

            int length;

            while ((length = in.read(buffer)) > 0) {
                ((OutputStream) out).write(buffer, 0, length);
            }

            in.close();
            ((OutputStream) out).close();
            System.out.println("File copied from " + src + " to " + dest);
        }
    }

    public static FileStore getPathFilesystem(String path) throws URISyntaxException, IOException {
        URI rootURI = new URI("file:///");
        Path rootPath = Paths.get(rootURI);
        Path dirPath = rootPath.resolve(path);
        FileStore dirFileStore = Files.getFileStore(dirPath);
        return dirFileStore;
    }

    public static void DrivesnDesc() {
        FileSystemView fsv = FileSystemView.getFileSystemView();

        File[] paths = File.listRoots();

        for (File path : paths) {
            System.out.println("Drive Name: " + path);
            System.out.println("Description: " + fsv.getSystemTypeDescription(path));
        }
    }

    public static String PathRootDesc(String strFilePath) {
        FileSystemView fsv = FileSystemView.getFileSystemView();

        File newFile = new File(strFilePath);
        newFile = GetFullParent(newFile);

        return fsv.getSystemTypeDescription(newFile);
    }

    public static File GetFullParent(File file) {
        try {
            file = GetFullParent(new File(file.getParent()));
        } catch (NullPointerException ex) {
            return file;
        }

        return file;
    }

    public static File CopyFolder(String strPathFolder) {

        File baseFileDirectory = new File(strPathFolder);

        File newFileDirectory
                = new File(baseFileDirectory.getParent() + File.separator
                        + "eFIXED_" + FilesUtility.unixTime + " " + FilesUtility.timeStamp);

        newFileDirectory.mkdirs();
        try {
            FilesUtility.copyFolder(baseFileDirectory, newFileDirectory);
        } catch (IOException ex) {
            Logger.getLogger(MetadataParser.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
            System.exit(9);
        }

        return newFileDirectory;
    }

    public static void printFileStore(FileStore filestore, String path) throws IOException {
        System.out.println("Name: " + filestore.name());
        System.out.println("\tPath: " + path);
        System.out.println("\tSize: " + filestore.getTotalSpace());
        System.out.println("\tUnallocated: " + filestore.getUnallocatedSpace());
        System.out.println("\tUsable: " + filestore.getUsableSpace());
        System.out.println("\tType: " + filestore.type());
    }
}
