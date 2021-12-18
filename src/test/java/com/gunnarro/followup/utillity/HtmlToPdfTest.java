package com.gunnarro.followup.utillity;
/**
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.net.FileRetrieve;
import com.itextpdf.tool.xml.net.FileRetrieveImpl;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
*/
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;

// https://itextpdf.com/en/demos/convert-html-css-to-pdf-free-online
@Disabled
class HtmlToPdfTest {

    public static final String HTML = "src/main/resources/templates/gr-gr-cv-en.html";
    public static final String CSS = "src/main/resources/static/css/cv/bootstrap.min-4.5.2.css";
    public static final String CSS_DIR = "/home/gunnarro/code/github/microapp-master/diet-manager/src/main/resources/static/css/cv/";
    public static final String PDF = "src/test/resources/cv.pdf";
    public static final String IMG_PATH = "src/resources/static/images/profile.jpg";
    public static final String RELATIVE_PATH = "~/code/github/microapp-master/diet-manager";

/*
    @Test
    void convertHtmlToPdf() throws Exception {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("src/test/resources/cv.pdf"));
        document.open();
        XMLWorkerHelper.getCSS(new FileInputStream("src/main/resources/static/css/cv/cv.css"));
        CSSResolver cssResolver = new StyleAttrCSSResolver();
        //cssResolver.addCss(cssFile);
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(HTML));
        document.close();
    }


    @Test
    void createPdfTest() throws Exception {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(PDF));
        writer.setInitialLeading(12.5f);
        // step 3
        document.open();
        // step 4

        // CSS
        CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(false);
        FileRetrieve retrieve = new FileRetrieveImpl(CSS_DIR);
        cssResolver.setFileRetrieve(retrieve);

        // HTML
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        htmlContext.autoBookmark(false);

        // Pipelines
        PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
        HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
        CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

        // XML Worker
        XMLWorker worker = new XMLWorker(css, true);
        XMLParser p = new XMLParser(worker);
        p.parse(new FileInputStream(HTML));

        // step 5
        document.close();
    }

    @Test
    void createPdf() throws Exception {
        // step 1
        Document document = new Document();

        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(PDF));
        writer.setInitialLeading(18f);

        // step 3
        document.open();

        // step 4

        // CSS
        CSSResolver cssResolver = new StyleAttrCSSResolver();
        CssFile cssFile = XMLWorkerHelper.getCSS(new FileInputStream(CSS));
        cssResolver.addCss(cssFile);

        // HTML
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

        // Pipelines
        PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
        HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
        CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

        // XML Worker
        XMLWorker worker = new XMLWorker(css, true);
        XMLParser p = new XMLParser(worker);
        p.parse(new FileInputStream(HTML));

        // step 5
        document.close();
    }

 */
}
