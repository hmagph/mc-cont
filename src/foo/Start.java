package foo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * S
 *
 */
public class Start {

    private static final int BUFFER_SIZE = 4096;	
    
	public static void main(String[] args) throws Exception {
		System.out.println(System.getenv("VCAP_APPLICATION"));
		//String host = System.getenv("VCAP_APP_HOST");
		String port = System.getenv("VCAP_APP_PORT");
		if (port != null) {
			System.out.println("VCAP_APP_PORT: "+port);
			File props = new File("server.properties");
			if (!props.exists()) {
				PrintWriter writer = new PrintWriter(props, "UTF-8");
				writer.println(
						"#Minecraft server properties\n" + 
						"#Fri Jan 30 14:21:22 CET 2015\n" + 
						"generator-settings=\n" + 
						"op-permission-level=4\n" + 
						"resource-pack-hash=\n" + 
						"allow-nether=true\n" + 
						"level-name=world\n" + 
						"enable-query=false\n" + 
						"allow-flight=false\n" + 
						"announce-player-achievements=true\n" + 
						"server-port="+port+"\n" + 
						"max-world-size=29999984\n" + 
						"level-type=DEFAULT\n" + 
						"enable-rcon=false\n" + 
						"force-gamemode=false\n" + 
						"level-seed=\n" + 
						"server-ip=\n" + 
						"network-compression-threshold=256\n" + 
						"max-build-height=256\n" + 
						"spawn-npcs=true\n" + 
						"white-list=false\n" + 
						"spawn-animals=true\n" + 
						"snooper-enabled=true\n" + 
						"hardcore=false\n" + 
						"online-mode=true\n" + 
						"resource-pack=\n" + 
						"pvp=true\n" + 
						"difficulty=1\n" + 
						"enable-command-block=false\n" + 
						"player-idle-timeout=0\n" + 
						"gamemode=0\n" + 
						"max-players=20\n" + 
						"max-tick-time=60000\n" + 
						"spawn-monsters=true\n" + 
						"view-distance=10\n" + 
						"generate-structures=true\n" + 
						"motd=Hello from Bluemix");
				writer.close();
				System.out.println("Created "+props.getAbsolutePath());
			}
		}
		
//		String vcap_services = System.getenv("VCAP_SERVICES");
//		if (vcap_services != null) {
//			System.out.println(vcap_services);
//			JsonParser jp = new JsonParser();
//			JsonObject vcap = (JsonObject) jp.parse(vcap_services);
//			String port = vcap.get("VCAP_APP_PORT").getAsString();
//			String host = vcap.get("VCAP_APP_HOST").getAsString();
//			System.out.println(host+":"+port);
//		}
		
		File eula = new File("eula.txt");
		if (!eula.exists()) {
			PrintWriter writer = new PrintWriter(eula, "UTF-8");
			writer.println(
					"#By changing the setting below to TRUE you are indicating your agreement to our EULA (https://account.mojang.com/documents/minecraft_eula).\n" +
					"#Mon Jan 05 14:18:28 CET 2015\n" +
					"eula=true");
			writer.close();
			System.out.println("Created "+eula.getAbsolutePath());
		}
//		for (int i = 0; i < 100; i++) {
//			Thread.sleep(1000);
//			System.out.println(i);
//		}
		net.minecraft.server.MinecraftServer.main(args);
	}
	
    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    public void unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }
	
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }	
}
