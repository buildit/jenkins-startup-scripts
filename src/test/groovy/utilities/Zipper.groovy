package utilities

import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class Zipper {
    static zip(String zipName, String... files) throws FileNotFoundException, IOException {
        File zipFile = new File(zipName)
        zipFile.mkdirs()
        FileOutputStream fos = new FileOutputStream(zipFile)
        ZipOutputStream zos = new ZipOutputStream(fos)
        files.each {
            System.out.println("Writing '${it}' to zip file")
            File file = new File(it)
            FileInputStream fis = new FileInputStream(file)
            ZipEntry zipEntry = new ZipEntry(it)
            zos.putNextEntry(zipEntry)

            byte[] bytes = new byte[1024]
            int length
            while ((length = fis.read(bytes)) >= 0) {
                zos.write(bytes, 0, length)
            }
            zos.closeEntry()
            fis.close()
        }
        zos.close()
        fos.close()
        return zipFile
    }
}
