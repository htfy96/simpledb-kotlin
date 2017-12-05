import javax.xml.transform.*
import javax.xml.transform.stream.*
import java.io.*

object TransformXmlFile {
    // Change these file names as appropriate
    var XSLFILE = "XmlToUnnested.xsl"
    var INFILE = "students.xml"
    var OUTFILE = "xx.txt"

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl")

            // Create Source and Result objects for the files
            val xsl = StreamSource(FileReader(XSLFILE))
            val input = StreamSource(FileReader(INFILE))
            val output = StreamResult(FileWriter(OUTFILE))

            // Transform the INFILE to the OUTFILE
            val tf = TransformerFactory.newInstance()
            val trans = tf.newTransformer(xsl)
            trans.transform(input, output)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
