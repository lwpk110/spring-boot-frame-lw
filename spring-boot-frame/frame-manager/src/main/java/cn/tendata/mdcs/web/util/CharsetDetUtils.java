package cn.tendata.mdcs.web.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.Charset;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.ByteOrderMarkDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;

public abstract class CharsetDetUtils {

    private static final CodepageDetectorProxy detector = createCodepageDetectorProxy();
    
    private static CodepageDetectorProxy createCodepageDetectorProxy() {
        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
        // Add the implementations of info.monitorenter.cpdetector.io.ICodepageDetector: 
        // This one is quick if we deal with unicode codepages: 
        detector.add(new ByteOrderMarkDetector()); 
        // The first instance delegated to tries to detect the meta charset attribut in html pages.
        detector.add(new ParsingDetector(true)); // be verbose about parsing.
        // This one does the tricks of exclusion and frequency detection, if first implementation is 
        // unsuccessful:
        detector.add(JChardetFacade.getInstance()); // Another singleton.
        detector.add(ASCIIDetector.getInstance()); // Fallback, see javadoc.
        return detector;
    }
    
    public static CodepageDetectorProxy getDetectorProxy(){
        return detector;
    }
    
    public static Charset detect(File file) throws MalformedURLException, IOException{
        return detector.detectCodepage(file.toURI().toURL());
    }
    
    public static Charset detect(InputStream inputStream, int length) throws IllegalArgumentException, IOException{
        return detector.detectCodepage(inputStream, length);
    }
    
    private CharsetDetUtils(){
        
    }
}
